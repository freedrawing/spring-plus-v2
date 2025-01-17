package com.freedrawing.springplus.domain.user.service

import com.freedrawing.springplus.config.PasswordEncoder
import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.domain.common.exception.AccessDeniedException
import com.freedrawing.springplus.domain.common.exception.InvalidRequestException
import com.freedrawing.springplus.domain.common.exception.NotFoundException
import com.freedrawing.springplus.domain.user.Role
import com.freedrawing.springplus.domain.user.User
import com.freedrawing.springplus.domain.user.dto.request.ChangePasswordRequestDto
import com.freedrawing.springplus.domain.user.dto.request.ChangeRoleRequestDto
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto
import com.freedrawing.springplus.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun getUserDetailsById(userId: Long): UserResponseDto {
        val findUser = getUserById(userId)
        return UserResponseDto.fromEntity(findUser)
    }

    @Transactional
    fun changeUserPassword(userId: Long, requestDto: ChangePasswordRequestDto) {
        val findUser = getUserById(userId)

        if (passwordEncoder.matches(rawPassword = requestDto.newPassword, encodedPassword = findUser.password)) {
            throw InvalidRequestException("새 비밀번호와 기존 비밀번호는 같을 수 없습니다.")
        }

        findUser.changePassword(passwordEncoder.encode(requestDto.newPassword))
    }

    /*
        * admin 계정은 강등 불가
        * `user <-> manager`만 바뀔 수 있음.
     */
    @Transactional
    fun changeUserRole(currentAdminId: Long, targetUserId: Long, requestDto: ChangeRoleRequestDto): UserResponseDto {
        val targetUser = getUserById(targetUserId)
        val newRole = Role.of(requestDto.newRole)

        // DB를 한 번 타지만 그래도 when으로 처리하는 게 깔끔한 듯
        when {
            currentAdminId == targetUserId -> throw AccessDeniedException("자기 자신의 권한은 변경할 수 없습니다.")
            targetUser.role.isAdmin() -> throw AccessDeniedException("Admin 계정은 권한 변경이 불가합니다.")
            newRole.isAdmin() -> throw AccessDeniedException("Admin 계정으로 권한 변경은 불가합니다.")
        }

        targetUser.changeRole(newRole)
        return UserResponseDto.fromEntity(targetUser)
    }

    fun getUserById(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { NotFoundException(ErrorCode.USER_NOT_FOUND) }
    }

    fun getUserByEmail(email: String): User {
        return userRepository.findByEmail(email)
            .orElseThrow { NotFoundException(ErrorCode.USER_NOT_FOUND) }
    }
}