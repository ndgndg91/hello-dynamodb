package com.ndgndg91.hellodynamodb.controller.dto.response

import com.ndgndg91.hellodynamodb.adaptor.domain.PushHistory

data class PushHistoryResponse(
    val items: List<PushHistory>,
    val lastCreatedAt: Long?
)