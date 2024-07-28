package com.ndgndg91.hellodynamodb.controller.dto.request

data class PushHistoryRequest(
    val accountId: Long,
    val category: String,
    val subCategory: String,
    val title: String,
    val contents: String,
    val action: String
)