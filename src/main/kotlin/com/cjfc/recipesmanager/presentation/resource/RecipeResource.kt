package com.cjfc.recipesmanager.presentation.resource

import com.cjfc.recipesmanager.mapper.RecipeMapper
import com.cjfc.recipesmanager.presentation.payload.RecipeBasePayload
import com.cjfc.recipesmanager.presentation.payload.RecipePayload
import com.cjfc.recipesmanager.presentation.payload.RecipesPayload
import com.cjfc.recipesmanager.service.RecipeService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping(value = ["/recipes-manager"])
class RecipeResource(
    private val recipeService: RecipeService,
    private val recipeMapper: RecipeMapper
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @ResponseBody
    @GetMapping("/v1/recipes")
    fun getRecipes(): Mono<RecipesPayload> =
        log.info("GET - /v1/recipes - Fetching all recipes")
            .let { recipeService.getRecipes() }
            .map { recipeMapper.toPayload(it) }
            .collectList()
            .map { RecipesPayload(it) }
            .doOnSuccess { log.info("Recipes fetched successfully") }
            .doOnError { log.error("Error while fetching recipes", it) }


    @ResponseBody
    @PostMapping("/v1/recipes")
    fun createRecipe(@RequestBody recipePayload: RecipePayload): Mono<RecipePayload> =
        log.info("POST - /v1/recipes - Creating new recipe")
            .let { recipeMapper.toEntity(recipePayload) }
            .let { recipeService.createRecipe(it) }
            .map { recipeMapper.toPayload(it) }
            .doOnSuccess { log.info("New recipe created successfully") }
            .doOnError { log.error("Error while creating recipe", it) }


    @ResponseBody
    @GetMapping("/v1/recipes/{recipeId}")
    fun getRecipeById(@PathVariable recipeId: String): Mono<RecipePayload> =
        log.info("GET - /v1/recipes/{} - Getting recipe by id", recipeId)
            .let { recipeService.getRecipeById(recipeId) }
            .map { recipeMapper.toPayload(it) }
            .doOnSuccess { log.info("New recipe created successfully") }
            .doOnError { log.error("Error while creating recipe", it) }

    @ResponseBody
    @DeleteMapping("/v1/recipes/{recipeId}")
    fun deleteRecipeById(@PathVariable recipeId: String): Mono<RecipeBasePayload> =
        log.info("DELETE - /v1/recipes/{} - Deleting recipe by id", recipeId)
            .let { recipeService.deleteRecipeById(recipeId) }
            .map { RecipeBasePayload(id = it) }
            .doOnSuccess { log.info("Recipe deleted successfully") }
            .doOnError { log.error("Error while deleting recipe", it) }
}
