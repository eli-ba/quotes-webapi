package com.elibouassaba.quotes.user.entity

import javax.persistence.*

@Entity
@Table(name = "authority")
class Authority {
    @Id
    @GeneratedValue
    var id: Long = 0

    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    var user: User? = null

    var authority: String? = null
}