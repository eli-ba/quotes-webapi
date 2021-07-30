package com.elibouassaba.quotes.quote.repository

import com.elibouassaba.quotes.quote.entity.Quote
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import javax.transaction.Transactional

interface QuoteRepository : PagingAndSortingRepository<Quote?, Long?> {
    fun findTopByOrderByCreatedAtDesc(): Quote?

    @Query("select q from Quote q order by (upvotes - downvotes) desc")
    fun top(pageable: Pageable?): List<Quote?>?

    @Query("select q from Quote q order by (downvotes - upvotes) desc")
    fun flop(pageable: Pageable?): List<Quote?>?

    @Transactional
    @Modifying
    @Query("update Quote set upvotes = upvotes + 1 where id = :id")
    fun upvote(@Param("id") id: Long?)

    @Transactional
    @Modifying
    @Query("update Quote set downvotes = downvotes + 1 where id = :id")
    fun downvote(@Param("id") id: Long?)
}