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
import reactor.core.publisher.Mono

@Service
class RecipeServiceImpl(
    private val firestoreRepository: FirestoreRepository,
    private val recipeMapper: RecipeMapper
) : RecipeService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun getRecipes(): Flux<Recipe> =
        log.info("Fetching all recipes")
            .let { firestoreRepository.findAll() }
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

    override fun getRecipeById(recipeId: String): Mono<Recipe> =
        log.info("Fetching recipe by id: {}", recipeId)
            .let { firestoreRepository.findById(recipeId) }
            .doOnSuccess { log.info("Recipe by id {} fetched successfully", recipeId) }
            .doOnError { log.error("Error while fetching recipe by id", it) }
            .map { recipeMapper.toEntity(it) }
            .onErrorMap {
                RecipesManagerException(
                    message = "Error fetching recipe from repository",
                    cause = it,
                    errorType = GENERIC_ERROR
                )
            }

    override fun createRecipe(recipe: Recipe): Mono<Recipe> =
        log.info("Creating new recipe")
            .let { recipeMapper.toDto(recipe) }
            .let { firestoreRepository.save(it) }
            .doOnSuccess { log.info("New recipe created successfully") }
            .doOnError { log.error("Error while creating new recipe", it) }
            .map { recipeMapper.toEntity(it) }
            .onErrorMap {
                RecipesManagerException(
                    message = "Error creating new recipe",
                    cause = it,
                    errorType = GENERIC_ERROR
                )
            }
}
