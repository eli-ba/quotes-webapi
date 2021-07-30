package com.elibouassaba.quotes.user.dto

import com.elibouassaba.quotes.quote.model.VoteHistoryItem


class VoteHistoryItemDto(vote: VoteHistoryItem) {
    var upvotes: Long = vote.upvotes!!.toLong()
    var downvotes: Long = vote.downvotes!!.toLong()
    var delta: Long = vote.delta!!.toLong()
    var createdAt: Long = vote.createdAt!!.time
}