package com.eliba.quotes

import javax.validation.constraints.NotNull


data class CreateQuoteDto(
    @NotNull
    var content: String? = null
)

data class EditQuoteDto(
    var content: @NotNull String? = null
)

class ProfileDto(user: User) {
    var fullName: String? = user.fullName
    var username: String? = user.username
}

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

data class RegisterDto(
    var fullName: String? = null,
    var username: String? = null,
    var password: String? = null,
)

class VoteHistoryItemDto(vote: VoteHistoryItem) {
    var upvotes: Long = vote.upvotes!!.toLong()
    var downvotes: Long = vote.downvotes!!.toLong()
    var delta: Long = vote.delta!!.toLong()
    var createdAt: Long = vote.createdAt!!.time
}