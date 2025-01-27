package com.freedrawing.springplus.domain.user.dto.request

data class ChangePasswordRequestDto(val currentPassword: String, val newPassword: String)