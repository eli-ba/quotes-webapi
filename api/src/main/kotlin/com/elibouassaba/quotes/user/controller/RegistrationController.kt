package com.elibouassaba.quotes.user.controller

import com.elibouassaba.quotes.quote.repository.UserRepository
import com.elibouassaba.quotes.user.dto.RegisterDto
import com.elibouassaba.quotes.user.entity.Authority
import com.elibouassaba.quotes.user.entity.User
import com.elibouassaba.quotes.user.repository.AuthorityRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/registrations")
class RegistrationController(
    private val userRepository: UserRepository,
    private val authorityRepository: AuthorityRepository
) {
    @PostMapping
    fun register(@RequestBody body: RegisterDto) {
        val user = User()
        user.fullName = body.fullName
        user.username = body.username
        user.password = BCryptPasswordEncoder().encode(body.password)
        user.enabled = true
        userRepository.save(user)

        val authority = Authority()
        authority.user = user
        authority.authority = "ROLE_ADMIN"
        authorityRepository.save(authority)
    }
}