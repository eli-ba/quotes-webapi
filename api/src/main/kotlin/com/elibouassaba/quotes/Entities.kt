package com.elibouassaba.quotes

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.*
import java.sql.Timestamp
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    var fullName: String? = null

    @NaturalId
    @Column(unique = true)
    var username: String? = null

    var password: String? = null

    var enabled = false

    @CreationTimestamp
    var createdAt: Timestamp? = null

    @UpdateTimestamp
    var updatedAt: Timestamp? = null
}

@Entity
@Table(name = "authorities")
class Authority {
    @Id
    @GeneratedValue
    var id: Long = 0

    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    var user: User? = null

    var authority: String? = null
}

@Entity
@Table(name = "quotes")
class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    var content: String? = null

    var upvotes: Long = 0

    var downvotes: Long = 0

    @OneToOne
    @JoinColumn(name = "user_id")
    var user: User? = null

    @CreationTimestamp
    var createdAt: Timestamp? = null

    @UpdateTimestamp
    var updatedAt: Timestamp? = null
}

@Entity
@Table(name = "vote_snapshots")
class VoteSnapshot {
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
