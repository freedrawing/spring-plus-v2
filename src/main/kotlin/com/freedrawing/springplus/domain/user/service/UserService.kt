package com.freedrawing.springplus.domain.user.service

import com.freedrawing.springplus.config.PasswordEncoder
import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.domain.common.exception.AccessDeniedException
import com.freedrawing.springplus.domain.common.exception.InvalidRequestException
import com.freedrawing.springplus.domain.common.exception.NotFoundException
import com.freedrawing.springplus.domain.user.Role
import com.freedrawing.springplus.domain.user.User
import com.freedrawing.springplus.domain.user.annotation.AdminOnly
import com.freedrawing.springplus.domain.user.dto.request.ChangePasswordRequestDto
import com.freedrawing.springplus.domain.user.dto.request.ChangeRoleRequestDto
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto
import com.freedrawing.springplus.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

    @Transactional
    fun changeUserRole(currentUserId: Long, targetUserId: Long, requestDto: ChangeRoleRequestDto): UserResponseDto {
        if (currentUserId == targetUserId) { // 본인 `Role`은 본인이 변경 불가
            throw AccessDeniedException("자기 자신의 권한은 변경할 수 없습니다.")
        }

        val findUser = getUserById(targetUserId)
        findUser.changeRole(Role.of(requestDto.newRole))

        return UserResponseDto.fromEntity(findUser)
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