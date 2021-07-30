package com.elibouassaba.quotes.quote.repository

import com.elibouassaba.quotes.user.entity.User
import org.springframework.data.repository.PagingAndSortingRepository

interface UserRepository : PagingAndSortingRepository<User?, Long?> {
    fun findByUsername(username: String?): User?
}