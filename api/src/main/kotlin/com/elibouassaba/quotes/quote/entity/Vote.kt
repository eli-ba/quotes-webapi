package com.elibouassaba.quotes.quote.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "vote")
class Vote {
    @Id
    @GeneratedValue
    var id: Long = 0

    var upvotes: Long = 0

    var downvotes: Long = 0

    @ManyToOne
    @JoinColumn(name = "quote_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    var quote: Quote? = null

    @CreationTimestamp
    var createdAt: Timestamp? = null

    constructor()

    constructor(quote: Quote) {
        this.quote = quote
        downvotes = quote.downvotes
        upvotes = quote.upvotes
    }
}
