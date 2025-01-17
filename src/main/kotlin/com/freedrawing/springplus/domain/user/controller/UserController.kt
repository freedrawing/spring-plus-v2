package com.freedrawing.springplus.domain.user.controller;

import com.freedrawing.springplus.domain.auth.UserPrincipal
import com.freedrawing.springplus.domain.auth.annotation.Authentication
import com.freedrawing.springplus.domain.user.annotation.AdminOnly
import com.freedrawing.springplus.domain.user.dto.request.ChangePasswordRequestDto
import com.freedrawing.springplus.domain.user.dto.request.ChangeRoleRequestDto
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto
import com.freedrawing.springplus.domain.user.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {

    @GetMapping("/users/{userId}")
    fun getUser(@PathVariable userId: Long): ResponseEntity<UserResponseDto> {
        val response = userService.getUserDetailsById(userId)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/users")
    fun changePassword(
        @Authentication userPrincipal: UserPrincipal,
        @Valid @RequestBody requestDto: ChangePasswordRequestDto
    ): ResponseEntity<Void> {
        userService.changeUserPassword(userPrincipal.userId, requestDto)
        return ResponseEntity.ok()
            .build()
    }

    @AdminOnly
    @PatchMapping("/admin/users/{userId}")
    fun changeRole(
        @PathVariable userId: Long,
        @Authentication userPrincipal: UserPrincipal,
        @RequestBody requestDto: ChangeRoleRequestDto
    ): ResponseEntity<UserResponseDto> {

        val response = userService.changeUserRole(
            currentAdminId = userPrincipal.userId,
            targetUserId = userId,
            requestDto = requestDto
        )
        return ResponseEntity.ok(response)
    }
}
