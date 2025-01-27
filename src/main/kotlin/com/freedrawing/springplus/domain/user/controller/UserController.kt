package com.freedrawing.springplus.domain.user.controller;

import com.freedrawing.springplus.domain.auth.UserPrincipal
import com.freedrawing.springplus.domain.user.dto.request.ChangePasswordRequestDto
import com.freedrawing.springplus.domain.user.dto.request.ChangeRoleRequestDto
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto
import com.freedrawing.springplus.domain.user.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class UserController(
    private val userService: UserService
) {

    @GetMapping("/users/{userId}")
    fun getUser(@PathVariable userId: Long): ResponseEntity<UserResponseDto> {
        val response = userService.getUserDetailsById(userId)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/users/profile-image")
    fun changeProfileImage(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestPart("image") profileImg: MultipartFile
    ): ResponseEntity<UserResponseDto> {
        val response = userService.changeProfileImage(userPrincipal.userId, profileImg)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PatchMapping("/users")
    fun changePassword(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @Valid @RequestBody requestDto: ChangePasswordRequestDto
    ): ResponseEntity<Void> {
        userService.changeUserPassword(userPrincipal.userId, requestDto)
        return ResponseEntity.ok()
            .build()
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/admin/users/{userId}")
    fun changeRole(
        @PathVariable userId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
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
