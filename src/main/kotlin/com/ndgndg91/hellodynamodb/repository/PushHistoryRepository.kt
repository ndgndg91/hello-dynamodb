package com.ndgndg91.hellodynamodb.repository

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.ndgndg91.hellodynamodb.adaptor.domain.PushHistory
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class PushHistoryRepository(
    private val dynamoDBMapper: DynamoDBMapper
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    fun save(pushHistory: PushHistory) {
        dynamoDBMapper.save(pushHistory)
    }

    fun findByAccountIdAndCategory(accountId: Long, categoryPrefix: String, limit: Int): List<PushHistory> {
        val queryExpression = DynamoDBQueryExpression<PushHistory>()
            .withKeyConditionExpression("account_id = :accountId and begins_with(category_sub_category_created_at, :categoryPrefix)")
            .withExpressionAttributeValues(mapOf(":accountId" to AttributeValue().withN(accountId.toString()), ":categoryPrefix" to AttributeValue().withS(categoryPrefix)))
            .withLimit(limit)
            .withScanIndexForward(false)

        return try {
            dynamoDBMapper.query(PushHistory::class.java, queryExpression)
        } catch (e: AmazonDynamoDBException) {
            logger.error("dynamo error", e)
            throw e
        }
    }

    fun findByAccountIdAndCategory(
        accountId: Long,
        category: String,
        subCategory: String,
        limit: Int,
        lastCreatedAt: Long? = null
    ): QueryResult<PushHistory> {
        val categoryPrefix = "$category#$subCategory#"
        val queryExpression = DynamoDBQueryExpression<PushHistory>()
            .withKeyConditionExpression("account_id = :accountId and begins_with(category_sub_category_created_at, :categoryPrefix)")
            .withExpressionAttributeValues(mapOf(
                ":accountId" to AttributeValue().withN(accountId.toString()),
                ":categoryPrefix" to AttributeValue().withS(categoryPrefix)
            ))
            .withLimit(limit)
            .withScanIndexForward(false)

        if (lastCreatedAt != null) {
            queryExpression.withExclusiveStartKey(mapOf(
                "account_id" to AttributeValue().withN(accountId.toString()),
                "category_sub_category_created_at" to AttributeValue().withS("$categoryPrefix#$lastCreatedAt")
            ))
        }

        return try {
            val result = dynamoDBMapper.queryPage(PushHistory::class.java, queryExpression)
            QueryResult(result.results, result.lastEvaluatedKey)
        } catch (e: AmazonDynamoDBException) {
            logger.error("dynamo error", e)
            throw e
        }
    }

    data class QueryResult<T>(
        val items: List<T>,
        val lastEvaluatedKey: Map<String, AttributeValue>?
    )
}