package com.elibouassaba.quotes.user.dto

data class RegisterDto(
    var fullName: String? = null,
    var username: String? = null,
    var password: String? = null,
)