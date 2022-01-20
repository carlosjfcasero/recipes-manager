package com.cjfc.recipesmanager.repository

import com.cjfc.recipesmanager.repository.dto.RecipeDto
import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository
import reactor.core.publisher.Flux

interface FirestoreRepository : FirestoreReactiveRepository<RecipeDto> {

    override fun findAll(): Flux<RecipeDto>
}