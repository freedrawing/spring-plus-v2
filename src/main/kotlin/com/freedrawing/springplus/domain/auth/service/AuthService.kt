package com.freedrawing.springplus.domain.auth.service

import com.freedrawing.springplus.config.TokenProvider
import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.config.util.Token
import com.freedrawing.springplus.domain.auth.dto.reponse.IssueTokenResponseDto
import com.freedrawing.springplus.domain.auth.dto.reponse.SignupResponseDto
import com.freedrawing.springplus.domain.auth.dto.request.SigninRequestDto
import com.freedrawing.springplus.domain.auth.dto.request.SignupRequestDto
import com.freedrawing.springplus.domain.auth.entity.RefreshToken
import com.freedrawing.springplus.domain.auth.exception.AuthenticationException
import com.freedrawing.springplus.domain.auth.exception.ExpiredTokenException
import com.freedrawing.springplus.domain.auth.repository.RefreshTokenRepository
import com.freedrawing.springplus.domain.common.exception.EntityAlreadyExistsException
import com.freedrawing.springplus.domain.user.entity.Role
import com.freedrawing.springplus.domain.user.entity.User
import com.freedrawing.springplus.domain.user.repository.UserRepository
import com.freedrawing.springplus.domain.user.service.UserService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class AuthService(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val tokenProvider: TokenProvider,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    @Transactional
    fun signup(requestDto: SignupRequestDto): SignupResponseDto {

        if (userRepository.existsByEmail(requestDto.email)) {
            throw EntityAlreadyExistsException(ErrorCode.USER_ALREADY_EXISTS)
        }

        val savedUser = userRepository.save(
            User(
                email = requestDto.email,
                password = bCryptPasswordEncoder.encode(requestDto.password),
                nickname = requestDto.nickname,
                role = Role.of(requestDto.userRole),
                profileImgUrl = requestDto.profileImgUrl
            )
        )
        return SignupResponseDto.fromEntity(savedUser)
    }

    fun signin(requestDto: SigninRequestDto): IssueTokenResponseDto {
        val findUser = userService.getUserByEmail(requestDto.email)
        val rawPassword = requestDto.password
        val encodedPassword = findUser.password

        if (bCryptPasswordEncoder.matches(rawPassword, encodedPassword).not()) {
            throw AuthenticationException(ErrorCode.INVALID_CREDENTIALS)
        }

        val (accessToken, refreshToken) = issueAccessTokenWithRefreshToken(findUser)

        // `redis`에 `RefreshToken` 저장
        refreshTokenRepository.save(
            RefreshToken(
                userId = findUser.id,
                token = refreshToken,
                duration = Token.REFRESH_TOKEN_DURATION
            )
        )

        return IssueTokenResponseDto(accessToken = accessToken, refreshToken = refreshToken,)
    }

    fun reissueTokens(token: String): IssueTokenResponseDto {
        tokenProvider.validateRefreshToken(token)

        val userId = tokenProvider.getUserIdFrom(token)
        val findToken = refreshTokenRepository.findById(userId)
            .orElseThrow { ExpiredTokenException("Refresh Token이 만료되었습니다.") }
        findToken.validateMyRefreshToken(token)

        val findUser = userService.getUserById(userId)
        val (accessToken, refreshToken) = issueAccessTokenWithRefreshToken(findUser)

        refreshTokenRepository.save(
            RefreshToken(
                userId = findUser.id,
                token = refreshToken,
                duration = Token.REFRESH_TOKEN_DURATION
            )
        )

        return IssueTokenResponseDto(accessToken = accessToken, refreshToken = refreshToken)
    }

    fun logout(userId: Long) {
        refreshTokenRepository.deleteById(userId)
    }

    private fun issueAccessTokenWithRefreshToken(user: User): Pair<String, String> {
        val accessToken = tokenProvider.generateToken(user, Token.ACCESS_TOKEN_TYPE, Token.ACCESS_TOKEN_DURATION)
        val refreshToken = tokenProvider.generateToken(user, Token.REFRESH_TOKEN_TYPE, Token.REFRESH_TOKEN_DURATION)

        return Pair(accessToken, refreshToken)
    }
}