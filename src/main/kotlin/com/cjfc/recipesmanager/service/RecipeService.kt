package com.cjfc.recipesmanager.service

import com.cjfc.recipesmanager.domain.Recipe
import com.cjfc.recipesmanager.domain.error.RecipesManagerException
import reactor.core.publisher.Flux

interface RecipeService {

    /**
     * Retrieves all the recipes.
     *
     * @return a Flux with the [Recipe] or with a [RecipesManagerException] if error
     */
    fun getRecipes(): Flux<Recipe>
}