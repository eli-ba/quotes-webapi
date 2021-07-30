package com.elibouassaba.quotes.quote.dto

import com.elibouassaba.quotes.quote.entity.Quote

class QuoteDto(quote: Quote) {
    var id: String = java.lang.Long.toString(quote.id)
    var content: String? = quote.content
    var upvotes: String
    var upvotesValue: Long
    var downvotes: String
    var downvotesValue: Long
    var user: String? = null

    init {
        upvotes = upvotesToString(quote.upvotes)
        upvotesValue = quote.upvotes
        downvotes = downvotesToString(quote.downvotes)
        downvotesValue = quote.downvotes
        if (quote.user != null) {
            user = quote.user!!.username
        }
    }

    private fun upvotesToString(votes: Long): String {
        var result = ""
        if (votes > 0) {
            result += "+"
        }
        result += votes.toString()
        return result
    }

    private fun downvotesToString(votes: Long): String {
        var result = ""
        if (votes > 0) {
            result += "-"
        }
        result += votes.toString()
        return result
    }
}