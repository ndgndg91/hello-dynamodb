package com.ndgndg91.hellodynamodb.adaptor.domain

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable

@DynamoDBTable(tableName = "push_history")
data class PushHistory(
    @DynamoDBHashKey(attributeName = "account_id")
    var accountId: Long = 0,
    @DynamoDBRangeKey(attributeName = "category_sub_category_created_at")
    var categorySubCategoryCreatedAt: String = "",
    var title: String = "",
    var contents: String = "",
    var action: String = "",
    var ttl: Long = 0
)