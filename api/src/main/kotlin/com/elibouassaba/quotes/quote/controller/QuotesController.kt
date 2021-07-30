package com.elibouassaba.quotes.quote.controller

import com.elibouassaba.quotes.quote.dto.CreateQuoteDto
import com.elibouassaba.quotes.quote.dto.EditQuoteDto
import com.elibouassaba.quotes.quote.dto.QuoteDto
import com.elibouassaba.quotes.quote.entity.Quote
import com.elibouassaba.quotes.quote.entity.Vote
import com.elibouassaba.quotes.quote.model.VoteHistoryItem
import com.elibouassaba.quotes.quote.repository.QuoteRepository
import com.elibouassaba.quotes.quote.repository.UserRepository
import com.elibouassaba.quotes.quote.repository.VoteSnapshotRepository
import com.elibouassaba.quotes.quote.service.QuoteService
import com.elibouassaba.quotes.user.dto.VoteHistoryItemDto
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*
import java.util.stream.Collectors
import javax.validation.Valid

@RestController
@RequestMapping("api/quotes")
class QuotesController(
    private val userRepository: UserRepository,
    private val quoteRepository: QuoteRepository,
    private val voteCountRepository: VoteSnapshotRepository,
    private val quoteService: QuoteService,
) {
    @PostMapping
    fun create(@RequestBody body: @Valid CreateQuoteDto?, principal: Principal): QuoteDto {
        val user = userRepository.findByUsername(principal.name)

        val quote = Quote()
        quote.content = body!!.content
        quote.user = user
        quoteRepository.save(quote)

        val vote = Vote()
        vote.quote = quote
        voteCountRepository.save(vote)

        return QuoteDto(quote)
    }

    @GetMapping("top10")
    fun top10(): List<QuoteDto>? {
        return quoteRepository.top(PageRequest.of(0, 10))
            ?.stream()
            ?.map { x: Quote? -> QuoteDto(x!!) }
            ?.collect(Collectors.toList())
    }

    @PostMapping("{id}/edit")
    fun edit(
        @PathVariable("id") id: Long,
        @RequestBody body: @Valid EditQuoteDto?, principal: Principal
    ): ResponseEntity<*> {
        val user = userRepository.findByUsername(principal.name)
        val quote: Optional<Quote?> = quoteRepository.findById(id)
        return if (quote.isPresent) {
            if (quote.get().user!!.id == user!!.id) {
                quote.get().content = body!!.content
                quoteRepository.save(quote.get())
                ResponseEntity<Any>(null, HttpStatus.NO_CONTENT)
            } else {
                ResponseEntity<Any>(null, HttpStatus.NOT_FOUND)
            }
        } else {
            ResponseEntity<Any>(null, HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable("id") id: Long, principal: Principal): ResponseEntity<*> {
        val user = userRepository.findByUsername(principal.name)
        val quote: Optional<Quote?> = quoteRepository.findById(id)
        return if (quote.isPresent) {
            if (quote.get().user!!.id == user!!.id) {
                quoteRepository.delete(quote.get())
                ResponseEntity<Any>(null, HttpStatus.NO_CONTENT)
            } else {
                ResponseEntity<Any>(null, HttpStatus.NOT_FOUND)
            }
        } else {
            ResponseEntity<Any>(null, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("flop10")
    fun flop10(): MutableList<QuoteDto>? {
        return quoteRepository.flop(PageRequest.of(0, 10))
            ?.stream()
            ?.map { x: Quote? -> QuoteDto(x!!) }
            ?.collect(Collectors.toList())
    }

    @GetMapping("latest")
    fun latest(): ResponseEntity<*> {
        val latest = quoteRepository.findTopByOrderByCreatedAtDesc()
        return if (latest != null) {
            ResponseEntity(QuoteDto(latest), HttpStatus.OK)
        } else {
            ResponseEntity<Any>(null, HttpStatus.OK)
        }
    }

    @PostMapping("{id}/upvote")
    fun upvote(@PathVariable("id") id: Long): ResponseEntity<*> {
        quoteRepository.upvote(id)
        val quote: Optional<Quote?> = quoteRepository.findById(id)
        val vote = Vote(quote.get())
        voteCountRepository.save(vote)
        return ResponseEntity<Any>(null, HttpStatus.OK)
    }

    @PostMapping("{id}/downvote")
    fun downvote(@PathVariable("id") id: Long): ResponseEntity<*> {
        quoteRepository.downvote(id)
        voteCountRepository.save(Vote(quoteRepository.findById(id).get()))
        return ResponseEntity<Any>(null, HttpStatus.OK)
    }

    @GetMapping("{id}/vote-history/{unit}/{limit}")
    fun voteHistory(
        @PathVariable("id") id: Long?,
        @PathVariable("unit") unit: String,
        @PathVariable("limit") limit: Int?
    ): ResponseEntity<*> {
        if (!unit.equals("year", ignoreCase = true) &&
            !unit.equals("month", ignoreCase = true) &&
            !unit.equals("week", ignoreCase = true) &&
            !unit.equals("day", ignoreCase = true) &&
            !unit.equals("hour", ignoreCase = true) &&
            !unit.equals("minute", ignoreCase = true) &&
            !unit.equals("second", ignoreCase = true)
        ) {
            return ResponseEntity<Any>(HttpStatus.BAD_REQUEST)
        }
        val voteHistory = quoteService.getVoteHistory(id, unit.trim { it <= ' ' }.toLowerCase(), limit)
        return ResponseEntity(
            voteHistory.stream()
                .map { x: VoteHistoryItem? -> VoteHistoryItemDto(x!!) }
                .collect(Collectors.toList()),
            HttpStatus.OK)
    }
}