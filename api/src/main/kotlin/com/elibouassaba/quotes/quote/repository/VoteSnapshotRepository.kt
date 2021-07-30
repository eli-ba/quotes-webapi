package com.elibouassaba.quotes.quote.repository

import com.elibouassaba.quotes.quote.entity.Vote
import org.springframework.data.repository.PagingAndSortingRepository

interface VoteSnapshotRepository : PagingAndSortingRepository<Vote?, Long?>