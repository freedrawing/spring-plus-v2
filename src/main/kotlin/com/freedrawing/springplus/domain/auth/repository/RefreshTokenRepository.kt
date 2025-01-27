package com.freedrawing.springplus.domain.auth.repository

import com.freedrawing.springplus.domain.auth.entity.RefreshToken
import org.springframework.data.repository.CrudRepository

interface RefreshTokenRepository : CrudRepository<RefreshToken, Long>