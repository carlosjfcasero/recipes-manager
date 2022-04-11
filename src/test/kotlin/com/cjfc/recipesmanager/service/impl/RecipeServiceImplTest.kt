package com.cjfc.recipesmanager.service.impl

import com.cjfc.recipesmanager.domain.Recipe
import com.cjfc.recipesmanager.domain.error.ErrorType.GENERIC_ERROR
import com.cjfc.recipesmanager.domain.error.RecipesManagerException
import com.cjfc.recipesmanager.mapper.RecipeMapper
import com.cjfc.recipesmanager.repository.FirestoreRepository
import com.cjfc.recipesmanager.repository.dto.RecipeDto
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
internal class RecipeServiceImplTest {

    companion object {
        private const val RECIPE_ID = "RECIPE_ID"
    }

    @Mock
    lateinit var firestoreRepository: FirestoreRepository

    @Mock
    lateinit var recipeMapper: RecipeMapper

    @InjectMocks
    lateinit var underTest: RecipeServiceImpl

    private val podamFactory: PodamFactory = PodamFactoryImpl()

    @Test
    fun whenCallGetRecipes_thenSuccess() {
        // GIVEN
        val recipeDto1 = podamFactory.manufacturePojoWithFullData(RecipeDto::class.java)
        val recipeDto2 = podamFactory.manufacturePojoWithFullData(RecipeDto::class.java)
        val recipeDtoList = listOf(recipeDto1, recipeDto2)
        val recipe1 = podamFactory.manufacturePojoWithFullData(Recipe::class.java)
        val recipe2 = podamFactory.manufacturePojoWithFullData(Recipe::class.java)

        `when`(firestoreRepository.findAll())
            .thenReturn(Flux.fromIterable(recipeDtoList))
        `when`(recipeMapper.toEntity(recipeDto1))
            .thenReturn(recipe1)
        `when`(recipeMapper.toEntity(recipeDto2))
            .thenReturn(recipe2)

        // WHEN - THEN
        StepVerifier.create(underTest.getRecipes())
            .expectNextMatches { it.equals(recipe1) }
            .expectNextMatches { it.equals(recipe2) }
            .verifyComplete()

        verify(firestoreRepository).findAll()
        verify(recipeMapper).toEntity(recipeDto1)
        verify(recipeMapper).toEntity(recipeDto2)
    }

    @Test
    fun whenCallGetRecipesAndErrorCallingRepository_thenSuccess() {
        // GIVEN
        val randomRecipesManagerException = createRandomRecipesManagerException()
        val expectedRecipesManagerException = RecipesManagerException(
            message = "Error fetching recipes from repository",
            cause = randomRecipesManagerException,
            errorType = GENERIC_ERROR
        )

        `when`(firestoreRepository.findAll())
            .thenReturn(Flux.error(randomRecipesManagerException))

        // WHEN - THEN
        StepVerifier.create(underTest.getRecipes())
            .verifyErrorMatches { exception ->
                val recipesManagerException = exception as RecipesManagerException
                recipesManagerException.message == expectedRecipesManagerException.message
                        && recipesManagerException.cause == expectedRecipesManagerException.cause
                        && recipesManagerException.errorType == expectedRecipesManagerException.errorType
            }

        verify(firestoreRepository).findAll()
    }

    @Test
    fun givenARecipeId_whenCallGetRecipeById_thenSuccess() {
        // GIVEN
        val recipeDto = podamFactory.manufacturePojoWithFullData(RecipeDto::class.java)
        val recipe = podamFactory.manufacturePojoWithFullData(Recipe::class.java)

        `when`(firestoreRepository.findById(RECIPE_ID))
            .thenReturn(Mono.just(recipeDto))
        `when`(recipeMapper.toEntity(recipeDto))
            .thenReturn(recipe)

        // WHEN - THEN
        StepVerifier.create(underTest.getRecipeById(RECIPE_ID))
            .expectNextMatches { it.equals(recipe) }
            .verifyComplete()

        verify(firestoreRepository).findById(RECIPE_ID)
        verify(recipeMapper).toEntity(recipeDto)
    }

    @Test
    fun givenARecipeId_whenCallGetRecipeByIdAndErrorCallingRepository_thenFail() {
        // GIVEN
        val randomRecipesManagerException = createRandomRecipesManagerException()
        val expectedRecipesManagerException = RecipesManagerException(
            message = "Error fetching recipe from repository",
            cause = randomRecipesManagerException,
            errorType = GENERIC_ERROR
        )

        `when`(firestoreRepository.findById(RECIPE_ID))
            .thenReturn(Mono.error(randomRecipesManagerException))

        // WHEN - THEN
        StepVerifier.create(underTest.getRecipeById(RECIPE_ID))
            .verifyErrorMatches { exception ->
                val recipesManagerException = exception as RecipesManagerException
                recipesManagerException.message == expectedRecipesManagerException.message
                        && recipesManagerException.cause == expectedRecipesManagerException.cause
                        && recipesManagerException.errorType == expectedRecipesManagerException.errorType
            }

        verify(firestoreRepository).findById(RECIPE_ID)
    }

    @Test
    fun givenARecipe_whenCallCreateRecipe_thenSuccess() {
        // GIVEN
        val recipe = podamFactory.manufacturePojoWithFullData(Recipe::class.java)
        val recipeDto = podamFactory.manufacturePojoWithFullData(RecipeDto::class.java)

        `when`(recipeMapper.toDto(recipe))
            .thenReturn(recipeDto)
        `when`(firestoreRepository.save(recipeDto))
            .thenReturn(Mono.just(recipeDto))
        `when`(recipeMapper.toEntity(recipeDto))
            .thenReturn(recipe)

        // WHEN - THEN
        StepVerifier.create(underTest.createRecipe(recipe))
            .expectNextMatches(recipe::equals)
            .verifyComplete()

        verify(recipeMapper).toDto(recipe)
        verify(firestoreRepository).save(recipeDto)
        verify(recipeMapper).toEntity(recipeDto)
    }

    @Test
    fun givenARecipe_whenCallCreateRecipeAndErrorCallingRepository_thenFail() {
        // GIVEN
        val recipe = podamFactory.manufacturePojoWithFullData(Recipe::class.java)
        val recipeDto = podamFactory.manufacturePojoWithFullData(RecipeDto::class.java)
        val randomRecipesManagerException = createRandomRecipesManagerException()
        val expectedRecipesManagerException = RecipesManagerException(
            message = "Error creating new recipe",
            cause = randomRecipesManagerException,
            errorType = GENERIC_ERROR
        )

        `when`(recipeMapper.toDto(recipe))
            .thenReturn(recipeDto)
        `when`(firestoreRepository.save(recipeDto))
            .thenReturn(Mono.error(randomRecipesManagerException))

        // WHEN - THEN
        StepVerifier.create(underTest.createRecipe(recipe))
            .verifyErrorMatches { exception ->
                val recipesManagerException = exception as RecipesManagerException
                recipesManagerException.message == expectedRecipesManagerException.message
                        && recipesManagerException.cause == expectedRecipesManagerException.cause
                        && recipesManagerException.errorType == expectedRecipesManagerException.errorType
            }

        verify(recipeMapper).toDto(recipe)
        verify(firestoreRepository).save(recipeDto)
    }
}
