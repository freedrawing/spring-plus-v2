package com.freedrawing.springplus.domain.auth.service

import com.freedrawing.springplus.config.PasswordEncoder
import com.freedrawing.springplus.config.TokenProvider
import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.config.util.Token
import com.freedrawing.springplus.domain.auth.dto.reponse.SigninResponseDto
import com.freedrawing.springplus.domain.auth.dto.reponse.SignupResponseDto
import com.freedrawing.springplus.domain.auth.dto.request.SigninRequestDto
import com.freedrawing.springplus.domain.auth.dto.request.SignupRequestDto
import com.freedrawing.springplus.domain.auth.exception.AuthenticationException
import com.freedrawing.springplus.domain.common.exception.EntityAlreadyExistsException
import com.freedrawing.springplus.domain.user.Role
import com.freedrawing.springplus.domain.user.User
import com.freedrawing.springplus.domain.user.annotation.AdminOnly
import com.freedrawing.springplus.domain.user.repository.UserRepository
import com.freedrawing.springplus.domain.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider
) {

    @Transactional
    fun signup(requestDto: SignupRequestDto): SignupResponseDto {

        if (userRepository.existsByEmail(requestDto.email)) {
            throw EntityAlreadyExistsException(ErrorCode.USER_ALREADY_EXISTS)
        }

        val savedUser = userRepository.save(
            User(
                email = requestDto.email,
                password = passwordEncoder.encode(requestDto.password),
                role = Role.of(requestDto.userRole)
            )
        )
        return SignupResponseDto.fromEntity(savedUser)
    }

    fun signin(requestDto: SigninRequestDto): SigninResponseDto {
        val findUser = userService.getUserByEmail(requestDto.email)

        if (passwordEncoder.matches(rawPassword = requestDto.password, encodedPassword = findUser.password).not()) {
            throw AuthenticationException(ErrorCode.INVALID_CREDENTIALS)
        }

        val accessToken = tokenProvider.generateToken(findUser, Token.ACCESS_TOKEN_TYPE, Token.ACCESS_TOKEN_DURATION)
        val refreshToken = tokenProvider.generateToken(findUser, Token.REFRESH_TOKEN_TYPE, Token.REFRESH_TOKEN_DURATION)

        return SigninResponseDto(accessToken = accessToken, refreshToken = refreshToken)
    }
}