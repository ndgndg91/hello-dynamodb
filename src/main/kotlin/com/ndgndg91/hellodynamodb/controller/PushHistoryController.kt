package com.ndgndg91.hellodynamodb.controller

import com.ndgndg91.hellodynamodb.adaptor.domain.PushHistory
import com.ndgndg91.hellodynamodb.controller.dto.request.PushHistoryRequest
import com.ndgndg91.hellodynamodb.controller.dto.response.PushHistoryResponse
import com.ndgndg91.hellodynamodb.repository.PushHistoryRepository
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.time.temporal.ChronoUnit

@RestController
@RequestMapping("/push_history")
class PushHistoryController(private val repository: PushHistoryRepository) {

    @PostMapping
    fun addPushHistory(@RequestBody request: PushHistoryRequest) {
        val createdAt = Instant.now()
        val ttl = createdAt.plus(30, ChronoUnit.DAYS).epochSecond

        val pushHistory = PushHistory(
            accountId = request.accountId,
            categorySubCategoryCreatedAt = "${request.category}#${request.subCategory}#${createdAt.epochSecond}",
            title = request.title,
            contents = request.contents,
            action = request.action,
            ttl = ttl
        )

        repository.save(pushHistory)
    }

    @GetMapping
    fun getPushHistory(
        @RequestParam accountId: Long,
        @RequestParam category: String,
        @RequestParam subCategory: String,
        @RequestParam limit: Int = 20,
        @RequestParam(required = false) lastCreatedAt: Long? = null
    ): PushHistoryResponse {
        val result = repository.findByAccountIdAndCategory(accountId, category, subCategory, limit, lastCreatedAt)
        return PushHistoryResponse(
            items = result.items,
            lastCreatedAt = result.items.lastOrNull()
                ?.categorySubCategoryCreatedAt?.split("#")?.lastOrNull()?.toLong()
        )
    }
}

