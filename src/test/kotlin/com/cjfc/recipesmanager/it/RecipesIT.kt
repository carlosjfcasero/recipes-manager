package com.cjfc.recipesmanager.it

import com.cjfc.recipesmanager.domain.error.ErrorType.GENERIC_ERROR
import com.cjfc.recipesmanager.dto.BaseRecipeDto
import com.cjfc.recipesmanager.repository.FirestoreRepository
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

    private val podamFactory: PodamFactory = PodamFactoryImpl()

    @Test
    fun whenGetRecipes_thenSuccess() {
        // GIVEN
        val baseRecipeDto1 = podamFactory.manufacturePojoWithFullData(BaseRecipeDto::class.java)
        val baseRecipeDto2 = podamFactory.manufacturePojoWithFullData(BaseRecipeDto::class.java)
        val baseRecipeDtoList = listOf(baseRecipeDto1, baseRecipeDto2)

        `when`(firestoreRepository.findAll())
            .thenReturn(Flux.fromIterable(baseRecipeDtoList))

        // WHEN-THEN
        webClient
            .get()
            .uri(RECIPES_PATH)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.recipes[0].id").isEqualTo(baseRecipeDto1.id)
            .jsonPath("$.recipes[0].name").isEqualTo(baseRecipeDto1.name)
            .jsonPath("$.recipes[1].id").isEqualTo(baseRecipeDto2.id)
            .jsonPath("$.recipes[1].name").isEqualTo(baseRecipeDto2.name)

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