package com.cjfc.recipesmanager.service.impl

import com.cjfc.recipesmanager.domain.Recipe
import com.cjfc.recipesmanager.domain.error.ErrorType.GENERIC_ERROR
import com.cjfc.recipesmanager.domain.error.RecipesManagerException
import com.cjfc.recipesmanager.mapper.RecipeMapper
import com.cjfc.recipesmanager.repository.FirestoreRepository
import com.cjfc.recipesmanager.service.RecipeService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class RecipeServiceImpl(
    private val firestoreRepository: FirestoreRepository,
    private val recipeMapper: RecipeMapper
) : RecipeService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun getRecipes(): Flux<Recipe> {
        log.info("Fetching all recipes")
        return firestoreRepository.findAll()
            .doOnComplete { log.info("Recipes DTO fetched successfully") }
            .map { recipeMapper.toEntity(it) }
            .doOnComplete { log.info("Recipes fetched successfully") }
            .doOnError { log.error("Error while fetching recipes from repository", it) }
            .onErrorMap {
                RecipesManagerException(
                    message = "Error fetching recipes from repository",
                    cause = it,
                    errorType = GENERIC_ERROR
                )
            }
    }
}