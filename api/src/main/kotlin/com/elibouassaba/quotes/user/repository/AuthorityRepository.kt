package com.elibouassaba.quotes.user.repository

import com.elibouassaba.quotes.user.entity.Authority
import org.springframework.data.repository.PagingAndSortingRepository

interface AuthorityRepository : PagingAndSortingRepository<Authority?, Long?>