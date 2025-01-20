//package com.freedrawing.springplus.config
//
//import at.favre.lib.crypto.bcrypt.BCrypt.*
//import org.springframework.stereotype.Component
//
////@Component
//class PasswordEncoder {
//
//    fun encode(rawPassword: String): String {
//        return withDefaults().hashToString(MIN_COST, rawPassword.toCharArray())
//    }
//
//    fun matches(rawPassword: String, encodedPassword: String): Boolean {
//        return verifyer().verify(rawPassword.toCharArray(), encodedPassword).verified
//    }
//}