package com.example.hellokitty.domain.usecase

import com.example.hellokitty.domain.repository.HelloKittyRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetCatImageUsecase @Inject constructor(private val repository: HelloKittyRepository) {

    suspend operator fun invoke(refImageIdList: List<String?>) = coroutineScope {
        refImageIdList.filterNotNull().map {
            async { repository.getCatImage(it) }
        }.awaitAll()
    }
}