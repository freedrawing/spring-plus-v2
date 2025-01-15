package com.freedrawing.springplus.domain.auth.controller

import com.freedrawing.springplus.config.util.LoggerUtil
import com.freedrawing.springplus.domain.auth.dto.reponse.SigninResponseDto
import com.freedrawing.springplus.domain.auth.dto.reponse.SignupResponseDto
import com.freedrawing.springplus.domain.auth.dto.request.SigninRequestDto
import com.freedrawing.springplus.domain.auth.dto.request.SignupRequestDto
import com.freedrawing.springplus.domain.auth.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/auth/signup")
    fun signup(@Valid @RequestBody requestDto: SignupRequestDto): ResponseEntity<SignupResponseDto> {
        val response = authService.signup(requestDto)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response)
    }

    @PostMapping("/auth/signin")
    fun signin(@Valid @RequestBody requestDto: SigninRequestDto): ResponseEntity<SigninResponseDto> {
        val response = authService.signin(requestDto)
        return ResponseEntity.ok(response)
    }

}