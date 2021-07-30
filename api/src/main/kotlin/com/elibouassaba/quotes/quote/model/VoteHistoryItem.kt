package com.elibouassaba.quotes.quote.model

import java.math.BigInteger
import java.sql.Timestamp

data class VoteHistoryItem(
    var downvotes: BigInteger? = null,
    var upvotes: BigInteger? = null,
    var delta: BigInteger? = null,
    var createdAt: Timestamp? = null,
)