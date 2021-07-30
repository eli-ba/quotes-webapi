package com.elibouassaba.quotes.user.controller

import com.elibouassaba.quotes.quote.repository.UserRepository
import com.elibouassaba.quotes.user.dto.ProfileDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("api/profile")
class ProfileController(private val userRepository: UserRepository) {
    @GetMapping
    operator fun get(principal: Principal): ProfileDto {
        val user = userRepository.findByUsername(principal.name)
        return ProfileDto(user!!)
    }
}

