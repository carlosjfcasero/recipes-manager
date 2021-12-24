package com.cjfc.recipesmanager.presentation.resource

import com.cjfc.recipesmanager.mapper.RecipeMapper
import com.cjfc.recipesmanager.presentation.payload.BaseRecipesPayload
import com.cjfc.recipesmanager.service.RecipeService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
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
    fun getRecipes(): Mono<BaseRecipesPayload> {
        log.info("GET - v1/recipes - Fetching all recipes")

        return recipeService.getRecipes()
            .map { recipeMapper.toPayload(it) }
            .collectList()
            .map { BaseRecipesPayload(it) }
            .doOnSuccess { log.info("Recipes base fetched successfully") }
            .doOnError { log.error("Error while fetching recipes", it) }
    }
}