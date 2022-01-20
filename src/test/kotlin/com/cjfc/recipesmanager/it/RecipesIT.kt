package com.cjfc.recipesmanager.it

import com.cjfc.recipesmanager.domain.error.ErrorType.GENERIC_ERROR
import com.cjfc.recipesmanager.repository.FirestoreRepository
import com.cjfc.recipesmanager.repository.dto.RecipeDto
import kotlin.random.Random
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import uk.co.jemos.podam.api.PodamFactory
import uk.co.jemos.podam.api.PodamFactoryImpl

@ExtendWith(SpringExtension::class)
@SpringBootTest(properties = ["spring.cloud.gcp.firestore.enabled=false"])
@AutoConfigureWebTestClient
class RecipesIT {

    companion object {
        const val RECIPES_PATH = "/recipes-manager/v1/recipes"
    }

    @MockBean
    lateinit var firestoreRepository: FirestoreRepository

    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun whenGetRecipes_thenSuccess() {
        // GIVEN
        val recipeDto1 = RecipeDto(
            id = randomAlphanumeric(5),
            name = randomAlphanumeric(5),
            description = randomAlphanumeric(5),
            favourite = Random.nextBoolean(),
            ingredients = randomAlphanumeric(5),
            origin = randomAlphanumeric(5),
            temperature = randomAlphanumeric(5),
            labels = listOf(randomAlphanumeric(5), randomAlphanumeric(5)),
            course = randomAlphanumeric(5)

        )
        val recipeDto2 = RecipeDto(
            id = randomAlphanumeric(5),
            name = randomAlphanumeric(5),
            description = randomAlphanumeric(5),
            favourite = Random.nextBoolean(),
            ingredients = randomAlphanumeric(5),
            origin = randomAlphanumeric(5),
            temperature = randomAlphanumeric(5),
            labels = listOf(randomAlphanumeric(5), randomAlphanumeric(5)),
            course = randomAlphanumeric(5)

        )
        val recipeDtoList = listOf(recipeDto1, recipeDto2)

        `when`(firestoreRepository.findAll())
            .thenReturn(Flux.fromIterable(recipeDtoList))

        // WHEN-THEN
        webClient
            .get()
            .uri(RECIPES_PATH)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.recipes[0].id").isEqualTo(recipeDto1.id)
            .jsonPath("$.recipes[0].name").isEqualTo(recipeDto1.name!!)
            .jsonPath("$.recipes[0].description").isEqualTo(recipeDto1.description!!)
            .jsonPath("$.recipes[0].favourite").isEqualTo(recipeDto1.favourite!!)
            .jsonPath("$.recipes[0].ingredients").isEqualTo(recipeDto1.ingredients!!)
            .jsonPath("$.recipes[0].origin").isEqualTo(recipeDto1.origin!!)
            .jsonPath("$.recipes[0].temperature").isEqualTo(recipeDto1.temperature!!)
            .jsonPath("$.recipes[0].labels[0]").isEqualTo(recipeDto1.labels!!.get(0))
            .jsonPath("$.recipes[0].labels[1]").isEqualTo(recipeDto1.labels!!.get(1))
            .jsonPath("$.recipes[0].course").isEqualTo(recipeDto1.course!!)
            .jsonPath("$.recipes[1].id").isEqualTo(recipeDto2.id)
            .jsonPath("$.recipes[1].name").isEqualTo(recipeDto2.name!!)
            .jsonPath("$.recipes[1].description").isEqualTo(recipeDto2.description!!)
            .jsonPath("$.recipes[1].favourite").isEqualTo(recipeDto2.favourite!!)
            .jsonPath("$.recipes[1].ingredients").isEqualTo(recipeDto2.ingredients!!)
            .jsonPath("$.recipes[1].origin").isEqualTo(recipeDto2.origin!!)
            .jsonPath("$.recipes[1].temperature").isEqualTo(recipeDto2.temperature!!)
            .jsonPath("$.recipes[1].labels[0]").isEqualTo(recipeDto2.labels!!.get(0))
            .jsonPath("$.recipes[1].labels[1]").isEqualTo(recipeDto2.labels!!.get(1))
            .jsonPath("$.recipes[1].course").isEqualTo(recipeDto2.course!!)

        verify(firestoreRepository).findAll()
    }

    @Test
    fun whenGetRecipesAndErrorCallingFirestore_thenError() {
        // GIVEN
        `when`(firestoreRepository.findAll())
            .thenReturn(Flux.error(Exception()))

        // WHEN-THEN
        webClient
            .get()
            .uri(RECIPES_PATH)
            .exchange()
            .expectStatus()
            .is5xxServerError
            .expectBody()
            .jsonPath("$.error.code").isEqualTo(GENERIC_ERROR.code)
            .jsonPath("$.error.message").isEqualTo(GENERIC_ERROR.message)
            .jsonPath("$.error.severity").isEqualTo(GENERIC_ERROR.severity.toString())

        verify(firestoreRepository).findAll()
    }
}