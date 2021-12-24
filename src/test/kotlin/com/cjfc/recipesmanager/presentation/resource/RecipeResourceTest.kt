package com.cjfc.recipesmanager.presentation.resource

import com.cjfc.recipesmanager.domain.BaseRecipe
import com.cjfc.recipesmanager.mapper.RecipeMapper
import com.cjfc.recipesmanager.presentation.payload.BaseRecipePayload
import com.cjfc.recipesmanager.presentation.payload.BaseRecipesPayload
import com.cjfc.recipesmanager.service.RecipeService
import com.cjfc.recipesmanager.utils.TestUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import uk.co.jemos.podam.api.PodamFactory
import uk.co.jemos.podam.api.PodamFactoryImpl

@ExtendWith(MockitoExtension::class)
class RecipeResourceTest {

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
        val baseRecipe1 = podamFactory.manufacturePojoWithFullData(BaseRecipe::class.java)
        val baseRecipe2 = podamFactory.manufacturePojoWithFullData(BaseRecipe::class.java)
        val baseRecipeList = listOf(baseRecipe1, baseRecipe2)
        val baseRecipePayload1 = podamFactory.manufacturePojoWithFullData(BaseRecipePayload::class.java)
        val baseRecipePayload2 = podamFactory.manufacturePojoWithFullData(BaseRecipePayload::class.java)
        val baseRecipesPayload = BaseRecipesPayload(listOf(baseRecipePayload1, baseRecipePayload2))

        `when`(recipeService.getRecipes())
            .thenReturn(Flux.fromIterable(baseRecipeList))
        `when`(recipeMapper.toPayload(baseRecipe1))
            .thenReturn(baseRecipePayload1)
        `when`(recipeMapper.toPayload(baseRecipe2))
            .thenReturn(baseRecipePayload2)

        // WHEN - THEN
        StepVerifier.create(underTest.getRecipes())
            .expectNextMatches(baseRecipesPayload::equals)
            .verifyComplete()

        verify(recipeService).getRecipes()
        verify(recipeMapper).toPayload(baseRecipe1)
        verify(recipeMapper).toPayload(baseRecipe2)
    }

    @Test
    fun whenCallGetRecipesAndErrorCallingService_thenFail() {
        // GIVEN
        val randomRecipesManagerException = TestUtils.createRandomRecipesManagerException()

        `when`(recipeService.getRecipes())
            .thenReturn(Flux.error(randomRecipesManagerException))

        // WHEN - THEN
        StepVerifier.create(underTest.getRecipes())
            .verifyErrorMatches(randomRecipesManagerException::equals)

        verify(recipeService).getRecipes()
    }
}