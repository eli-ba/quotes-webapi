package com.elibouassaba.quotes.quote.entity

import com.elibouassaba.quotes.user.entity.User
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "quote")
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