package com.cjfc.recipesmanager.mapper

import com.cjfc.recipesmanager.domain.Recipe
import com.cjfc.recipesmanager.presentation.payload.RecipePayload
import com.cjfc.recipesmanager.repository.dto.RecipeDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mapstruct.factory.Mappers
import org.mockito.junit.jupiter.MockitoExtension
import uk.co.jemos.podam.api.PodamFactory
import uk.co.jemos.podam.api.PodamFactoryImpl

@ExtendWith(MockitoExtension::class)
class RecipeMapperTest {

    private val podamFactory: PodamFactory = PodamFactoryImpl()
    private val underTest: RecipeMapper = Mappers.getMapper(RecipeMapper::class.java)

    @Test
    fun givenRecipe_whenMapToPayload_thenSuccess() {
        // GIVEN
        val recipe = podamFactory.manufacturePojoWithFullData(Recipe::class.java)
        val expectedRecipePayload =
            RecipePayload(
                id = recipe.id,
                name = recipe.name,
                description = recipe.description,
                course = recipe.course,
                favourite = recipe.favourite,
                ingredients = recipe.ingredients,
                labels = recipe.labels,
                origin = recipe.origin,
                temperature = recipe.temperature
            )

        // WHEN
        val result = underTest.toPayload(recipe)

        // THEN
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(expectedRecipePayload)
    }

    @Test
    fun givenRecipeDto_whenMapToEntity_thenSuccess() {
        // GIVEN
        val recipeDto = podamFactory.manufacturePojoWithFullData(RecipeDto::class.java)
        val expectedRecipe = Recipe(
            id = recipeDto.id,
            name = recipeDto.name,
            description = recipeDto.description,
            course = recipeDto.course,
            favourite = recipeDto.favourite,
            ingredients = recipeDto.ingredients,
            labels = recipeDto.labels,
            origin = recipeDto.origin,
            temperature = recipeDto.temperature
        )

        // WHEN
        val result = underTest.toEntity(recipeDto)

        // THEN
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(expectedRecipe)
    }

    @Test
    fun givenRecipePayload_whenMapToEntity_thenSuccess() {
        // GIVEN
        val recipePayload = podamFactory.manufacturePojoWithFullData(RecipePayload::class.java)
        val expectedRecipe = Recipe(
            id = recipePayload.id,
            name = recipePayload.name,
            description = recipePayload.description,
            course = recipePayload.course,
            favourite = recipePayload.favourite,
            ingredients = recipePayload.ingredients,
            labels = recipePayload.labels,
            origin = recipePayload.origin,
            temperature = recipePayload.temperature
        )

        // WHEN
        val result = underTest.toEntity(recipePayload)

        // THEN
        assertThat(result)
            .hasFieldOrPropertyWithValue("id", null)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expectedRecipe)
    }

    @Test
    fun givenRecipe_whenMapToDto_thenSuccess() {
        // GIVEN
        val recipe = podamFactory.manufacturePojoWithFullData(Recipe::class.java)
        val expectedRecipeDto = RecipeDto(
            id = recipe.id,
            name = recipe.name,
            description = recipe.description,
            course = recipe.course,
            favourite = recipe.favourite,
            ingredients = recipe.ingredients,
            labels = recipe.labels,
            origin = recipe.origin,
            temperature = recipe.temperature
        )

        // WHEN
        val result = underTest.toDto(recipe)

        // THEN
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(expectedRecipeDto)
    }
}