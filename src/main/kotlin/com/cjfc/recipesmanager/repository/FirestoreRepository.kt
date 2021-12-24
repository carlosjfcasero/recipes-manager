package com.cjfc.recipesmanager.repository

import com.cjfc.recipesmanager.dto.BaseRecipeDto
import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository
import reactor.core.publisher.Flux

interface FirestoreRepository : FirestoreReactiveRepository<BaseRecipeDto> {

    override fun findAll(): Flux<BaseRecipeDto>
}