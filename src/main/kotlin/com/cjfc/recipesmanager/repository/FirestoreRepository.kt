package com.cjfc.recipesmanager.repository

import com.cjfc.recipesmanager.repository.dto.RecipeDto
import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface FirestoreRepository : FirestoreReactiveRepository<RecipeDto> {

    override fun findAll(): Flux<RecipeDto>

    override fun <S : RecipeDto> save(entity: S): Mono<S>

    override fun findById(id: String): Mono<RecipeDto>

    override fun deleteById(id: String): Mono<Void>
}
