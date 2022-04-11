package com.cjfc.recipesmanager.presentation.resource

import com.cjfc.recipesmanager.domain.Recipe
import com.cjfc.recipesmanager.mapper.RecipeMapper
import com.cjfc.recipesmanager.presentation.payload.RecipePayload
import com.cjfc.recipesmanager.presentation.payload.RecipesPayload
import com.cjfc.recipesmanager.service.RecipeService
import com.cjfc.recipesmanager.utils.TestUtils.Companion.createRandomRecipesManagerException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import uk.co.jemos.podam.api.PodamFactory
import uk.co.jemos.podam.api.PodamFactoryImpl

@ExtendWith(MockitoExtension::class)
class RecipeResourceTest {

    companion object {
        private const val RECIPE_ID = "RECIPE_ID"
    }

    @Mock
    lateinit var recipeService: RecipeService

    @Mock
    lateinit var recipeMapper: RecipeMapper

    @InjectMocks
    lateinit var underTest: RecipeResource

    private val podamFactory: PodamFactory = PodamFactoryImpl()

    @Test
    fun whenCallGetRecipes_thenSuccess() {
        // GIVEN
        val recipe1 = podamFactory.manufacturePojoWithFullData(Recipe::class.java)
        val recipe2 = podamFactory.manufacturePojoWithFullData(Recipe::class.java)
        val recipeList = listOf(recipe1, recipe2)
        val recipePayload1 = podamFactory.manufacturePojoWithFullData(RecipePayload::class.java)
        val recipePayload2 = podamFactory.manufacturePojoWithFullData(RecipePayload::class.java)
        val recipesPayload = RecipesPayload(listOf(recipePayload1, recipePayload2))

        `when`(recipeService.getRecipes())
            .thenReturn(Flux.fromIterable(recipeList))
        `when`(recipeMapper.toPayload(recipe1))
            .thenReturn(recipePayload1)
        `when`(recipeMapper.toPayload(recipe2))
            .thenReturn(recipePayload2)

        // WHEN - THEN
        StepVerifier.create(underTest.getRecipes())
            .expectNextMatches(recipesPayload::equals)
            .verifyComplete()

        verify(recipeService).getRecipes()
        verify(recipeMapper).toPayload(recipe1)
        verify(recipeMapper).toPayload(recipe2)
    }

    @Test
    fun whenCallGetRecipesAndErrorCallingService_thenFail() {
        // GIVEN
        val randomRecipesManagerException = createRandomRecipesManagerException()

        `when`(recipeService.getRecipes())
            .thenReturn(Flux.error(randomRecipesManagerException))

        // WHEN - THEN
        StepVerifier.create(underTest.getRecipes())
            .verifyErrorMatches(randomRecipesManagerException::equals)

        verify(recipeService).getRecipes()
    }

    @Test
    fun givenARecipeId_whenCallGetRecipeById_thenSuccess() {
        // GIVEN
        val recipe = podamFactory.manufacturePojoWithFullData(Recipe::class.java)
        val recipePayload = podamFactory.manufacturePojoWithFullData(RecipePayload::class.java)

        `when`(recipeService.getRecipeById(RECIPE_ID))
            .thenReturn(Mono.just(recipe))
        `when`(recipeMapper.toPayload(recipe))
            .thenReturn(recipePayload)

        // WHEN - THEN
        StepVerifier.create(underTest.getRecipeById(RECIPE_ID))
            .expectNextMatches(recipePayload::equals)
            .verifyComplete()

        verify(recipeService).getRecipeById(RECIPE_ID)
        verify(recipeMapper).toPayload(recipe)
    }

    @Test
    fun givenARecipeId_whenCallGetRecipeByIdAndErrorCallingService_thenFail() {
        // GIVEN
        val randomRecipesManagerException = createRandomRecipesManagerException()

        `when`(recipeService.getRecipeById(RECIPE_ID))
            .thenReturn(Mono.error(randomRecipesManagerException))

        // WHEN - THEN
        StepVerifier.create(underTest.getRecipeById(RECIPE_ID))
            .verifyErrorMatches(randomRecipesManagerException::equals)

        verify(recipeService).getRecipeById(RECIPE_ID)
    }

    @Test
    fun givenARecipe_whenCallCreateRecipe_thenSuccess() {
        // GIVEN
        val recipePayload = podamFactory.manufacturePojoWithFullData(RecipePayload::class.java)
        val recipe = podamFactory.manufacturePojoWithFullData(Recipe::class.java)

        `when`(recipeMapper.toEntity(recipePayload))
            .thenReturn(recipe)
        `when`(recipeService.createRecipe(recipe))
            .thenReturn(Mono.just(recipe))
        `when`(recipeMapper.toPayload(recipe))
            .thenReturn(recipePayload)

        // WHEN - THEN
        StepVerifier.create(underTest.createRecipe(recipePayload))
            .expectNextMatches(recipePayload::equals)
            .verifyComplete()

        verify(recipeMapper).toEntity(recipePayload)
        verify(recipeService).createRecipe(recipe)
        verify(recipeMapper).toPayload(recipe)
    }

    @Test
    fun givenARecipe_whenCallCreateRecipeAndErrorCallingService_thenFail() {
        // GIVEN
        val recipePayload = podamFactory.manufacturePojoWithFullData(RecipePayload::class.java)
        val recipe = podamFactory.manufacturePojoWithFullData(Recipe::class.java)
        val randomRecipesManagerException = createRandomRecipesManagerException()

        `when`(recipeMapper.toEntity(recipePayload))
            .thenReturn(recipe)
        `when`(recipeService.createRecipe(recipe))
            .thenReturn(Mono.error(randomRecipesManagerException))

        // WHEN - THEN
        StepVerifier.create(underTest.createRecipe(recipePayload))
            .verifyErrorMatches(randomRecipesManagerException::equals)

        verify(recipeMapper).toEntity(recipePayload)
        verify(recipeService).createRecipe(recipe)
    }
}
