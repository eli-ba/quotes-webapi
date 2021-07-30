package com.elibouassaba.quotes.user.dto

import com.elibouassaba.quotes.user.entity.User

class ProfileDto(user: User) {
    var fullName: String? = user.fullName
    var username: String? = user.username
}