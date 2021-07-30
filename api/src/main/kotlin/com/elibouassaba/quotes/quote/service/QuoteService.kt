package com.elibouassaba.quotes.quote.service

import com.elibouassaba.quotes.quote.model.VoteHistoryItem
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.sql.Timestamp
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
class QuoteService(
    @PersistenceContext private val entityManager: EntityManager
) {
    fun getVoteHistory(quoteId: Long?, unit: String, limit: Int?): List<VoteHistoryItem> {
        val query = entityManager.createNativeQuery(
            """SELECT 
                max(v.upvotes) upvotes,
                max(v.downvotes) downvotes,
                max(v.upvotes) - max(v.downvotes) delta,
                date_trunc(:unit, v.created_at) as createdAt
                FROM vote_snapshots v WHERE quote_id = :quoteId GROUP BY createdAt ORDER BY createdAt DESC LIMIT :limit"""
        )
        query.setParameter("unit", unit)
        query.setParameter("quoteId", quoteId)
        query.setParameter("limit", limit)
        val result = ArrayList<VoteHistoryItem>()
        for (item in query.resultList) {
            val array = item as Array<*>
            result.add(
                VoteHistoryItem(
                    upvotes = array[0] as BigInteger,
                    downvotes = array[1] as BigInteger,
                    delta = array[2] as BigInteger,
                    createdAt = array[3] as Timestamp,
                )
            )
        }
        return result
    }
}