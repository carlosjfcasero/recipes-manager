package com.cjfc.recipesmanager.service

import com.cjfc.recipesmanager.domain.Recipe
import com.cjfc.recipesmanager.domain.error.RecipesManagerException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface RecipeService {

    /**
     * Retrieves all the recipes.
     *
     * @return a Flux with the [Recipe] or with a [RecipesManagerException] if error
     */
    fun getRecipes(): Flux<Recipe>

    /**
     * Add new recipe.
     *
     * @return a Mono with the created [Recipe] or with a [RecipesManagerException] if error
     */
    fun createRecipe(recipe: Recipe): Mono<Recipe>
}