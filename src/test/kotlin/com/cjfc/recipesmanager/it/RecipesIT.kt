package com.cjfc.recipesmanager.it

import com.cjfc.recipesmanager.domain.error.ErrorType.GENERIC_ERROR
import com.cjfc.recipesmanager.repository.FirestoreRepository
import com.cjfc.recipesmanager.repository.dto.RecipeDto
import kotlin.random.Random
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class RecipesIT : BaseIT() {

    companion object {
        const val RECIPES_PATH = "/recipes-manager/v1/recipes"
        const val GET_RECIPE_BY_ID_PATH = "/recipes-manager/v1/recipes/{recipeId}"
        const val TIME = "10:23"
        const val RECIPE_ID = "RECIPE_ID"
    }

    @MockBean
    lateinit var firestoreRepository: FirestoreRepository

    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun `should return a list of recipes`() {
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
            tags = listOf(randomAlphanumeric(5), randomAlphanumeric(5)),
            course = randomAlphanumeric(5),
            url = randomAlphanumeric(5),
            time = TIME
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
            tags = listOf(randomAlphanumeric(5), randomAlphanumeric(5)),
            course = randomAlphanumeric(5),
            url = randomAlphanumeric(5),
            time = TIME
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
            .jsonPath("$.recipes[0].id").isEqualTo(recipeDto1.id!!)
            .jsonPath("$.recipes[0].name").isEqualTo(recipeDto1.name!!)
            .jsonPath("$.recipes[0].description").isEqualTo(recipeDto1.description!!)
            .jsonPath("$.recipes[0].favourite").isEqualTo(recipeDto1.favourite!!)
            .jsonPath("$.recipes[0].ingredients").isEqualTo(recipeDto1.ingredients!!)
            .jsonPath("$.recipes[0].origin").isEqualTo(recipeDto1.origin!!)
            .jsonPath("$.recipes[0].temperature").isEqualTo(recipeDto1.temperature!!)
            .jsonPath("$.recipes[0].tags[0]").isEqualTo(recipeDto1.tags!![0])
            .jsonPath("$.recipes[0].tags[1]").isEqualTo(recipeDto1.tags!![1])
            .jsonPath("$.recipes[0].tags[2]").isEqualTo(recipeDto1.labels!![0])
            .jsonPath("$.recipes[0].tags[3]").isEqualTo(recipeDto1.labels!![1])
            .jsonPath("$.recipes[0].course").isEqualTo(recipeDto1.course!!)
            .jsonPath("$.recipes[0].url").isEqualTo(recipeDto1.url!!)
            .jsonPath("$.recipes[0].time").isEqualTo(recipeDto1.time!!)
            .jsonPath("$.recipes[1].id").isEqualTo(recipeDto2.id!!)
            .jsonPath("$.recipes[1].name").isEqualTo(recipeDto2.name!!)
            .jsonPath("$.recipes[1].description").isEqualTo(recipeDto2.description!!)
            .jsonPath("$.recipes[1].favourite").isEqualTo(recipeDto2.favourite!!)
            .jsonPath("$.recipes[1].ingredients").isEqualTo(recipeDto2.ingredients!!)
            .jsonPath("$.recipes[1].origin").isEqualTo(recipeDto2.origin!!)
            .jsonPath("$.recipes[1].temperature").isEqualTo(recipeDto2.temperature!!)
            .jsonPath("$.recipes[1].tags[0]").isEqualTo(recipeDto2.tags!![0])
            .jsonPath("$.recipes[1].tags[1]").isEqualTo(recipeDto2.tags!![1])
            .jsonPath("$.recipes[1].tags[2]").isEqualTo(recipeDto2.labels!![0])
            .jsonPath("$.recipes[1].tags[3]").isEqualTo(recipeDto2.labels!![1])
            .jsonPath("$.recipes[1].course").isEqualTo(recipeDto2.course!!)
            .jsonPath("$.recipes[1].url").isEqualTo(recipeDto2.url!!)
            .jsonPath("$.recipes[1].time").isEqualTo(recipeDto2.time!!)

        verify(firestoreRepository).findAll()
    }

    @Test
    fun `should fail the find all recipes when firestore repository returns error`() {
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

    @Test
    fun `should create new recipe`() {
        // GIVEN
        val labelsJsonArray = JSONArray()
            .put(randomAlphanumeric(5))
            .put(randomAlphanumeric(5))

        val recipePayload: JSONObject = JSONObject()
            .put("id", randomAlphanumeric(5))
            .put("name", randomAlphanumeric(5))
            .put("description", randomAlphanumeric(5))
            .put("favourite", Random.nextBoolean())
            .put("ingredients", randomAlphanumeric(5))
            .put("origin", randomAlphanumeric(5))
            .put("temperature", randomAlphanumeric(5))
            .put("tags", labelsJsonArray)
            .put("course", randomAlphanumeric(5))
            .put("url", randomAlphanumeric(5))
            .put("time", TIME)

        val recipeDto = RecipeDto(
            id = null,
            name = recipePayload.getString("name"),
            description = recipePayload.getString("description"),
            favourite = recipePayload.getBoolean("favourite"),
            ingredients = recipePayload.getString("ingredients"),
            origin = recipePayload.getString("origin"),
            temperature = recipePayload.getString("temperature"),
            tags = listOf(labelsJsonArray.getString(0), labelsJsonArray.getString(1)),
            labels = null,
            course = recipePayload.getString("course"),
            url = recipePayload.getString("url"),
            time = recipePayload.getString("time")
        )

        `when`(firestoreRepository.save(recipeDto))
            .thenReturn(Mono.just(recipeDto))

        // WHEN-THEN
        webClient
            .post()
            .uri(RECIPES_PATH)
            .contentType(APPLICATION_JSON)
            .bodyValue(recipePayload.toString())
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.id").doesNotExist()
            .jsonPath("$.name").isEqualTo(recipeDto.name!!)
            .jsonPath("$.description").isEqualTo(recipeDto.description!!)
            .jsonPath("$.favourite").isEqualTo(recipeDto.favourite!!)
            .jsonPath("$.ingredients").isEqualTo(recipeDto.ingredients!!)
            .jsonPath("$.origin").isEqualTo(recipeDto.origin!!)
            .jsonPath("$.temperature").isEqualTo(recipeDto.temperature!!)
            .jsonPath("$.tags[0]").isEqualTo(recipeDto.tags!![0])
            .jsonPath("$.tags[1]").isEqualTo(recipeDto.tags!![1])
            .jsonPath("$.course").isEqualTo(recipeDto.course!!)
            .jsonPath("$.url").isEqualTo(recipeDto.url!!)
            .jsonPath("$.time").isEqualTo(recipeDto.time!!)

        verify(firestoreRepository).save(recipeDto)
    }

    @Test
    fun `should fail the create new recipe when firestore repository returns error`() {
        // GIVEN
        val labelsJsonArray = JSONArray()
            .put(randomAlphanumeric(5))
            .put(randomAlphanumeric(5))

        val recipePayload: JSONObject = JSONObject()
            .put("id", randomAlphanumeric(5))
            .put("name", randomAlphanumeric(5))
            .put("description", randomAlphanumeric(5))
            .put("favourite", Random.nextBoolean())
            .put("ingredients", randomAlphanumeric(5))
            .put("origin", randomAlphanumeric(5))
            .put("temperature", randomAlphanumeric(5))
            .put("tags", labelsJsonArray)
            .put("course", randomAlphanumeric(5))
            .put("url", randomAlphanumeric(5))
            .put("time", TIME)

        val recipeDto = RecipeDto(
            id = null,
            name = recipePayload.getString("name"),
            description = recipePayload.getString("description"),
            favourite = recipePayload.getBoolean("favourite"),
            ingredients = recipePayload.getString("ingredients"),
            origin = recipePayload.getString("origin"),
            temperature = recipePayload.getString("temperature"),
            tags = listOf(labelsJsonArray.getString(0), labelsJsonArray.getString(1)),
            labels = null,
            course = recipePayload.getString("course"),
            url = recipePayload.getString("url"),
            time = recipePayload.getString("time")
        )

        `when`(firestoreRepository.save(recipeDto))
            .thenReturn(Mono.error(Exception()))

        // WHEN-THEN
        webClient
            .post()
            .uri(RECIPES_PATH)
            .contentType(APPLICATION_JSON)
            .bodyValue(recipePayload.toString())
            .exchange()
            .expectStatus()
            .is5xxServerError
            .expectBody()
            .jsonPath("$.error.code").isEqualTo(GENERIC_ERROR.code)
            .jsonPath("$.error.message").isEqualTo(GENERIC_ERROR.message)
            .jsonPath("$.error.severity").isEqualTo(GENERIC_ERROR.severity.toString())

        verify(firestoreRepository).save(recipeDto)
    }

    @Test
    fun `should return a recipe by id`() {
        // GIVEN
        val recipeDto = RecipeDto(
            id = randomAlphanumeric(5),
            name = randomAlphanumeric(5),
            description = randomAlphanumeric(5),
            favourite = Random.nextBoolean(),
            ingredients = randomAlphanumeric(5),
            origin = randomAlphanumeric(5),
            temperature = randomAlphanumeric(5),
            labels = listOf(randomAlphanumeric(5), randomAlphanumeric(5)),
            tags = listOf(randomAlphanumeric(5), randomAlphanumeric(5)),
            course = randomAlphanumeric(5),
            url = randomAlphanumeric(5),
            time = TIME
        )

        `when`(firestoreRepository.findById(RECIPE_ID))
            .thenReturn(Mono.just(recipeDto))

        // WHEN-THEN
        webClient
            .get()
            .uri(buildPath(GET_RECIPE_BY_ID_PATH, RECIPE_ID))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(recipeDto.id!!)
            .jsonPath("$.name").isEqualTo(recipeDto.name!!)
            .jsonPath("$.description").isEqualTo(recipeDto.description!!)
            .jsonPath("$.favourite").isEqualTo(recipeDto.favourite!!)
            .jsonPath("$.ingredients").isEqualTo(recipeDto.ingredients!!)
            .jsonPath("$.origin").isEqualTo(recipeDto.origin!!)
            .jsonPath("$.temperature").isEqualTo(recipeDto.temperature!!)
            .jsonPath("$.tags[0]").isEqualTo(recipeDto.tags!![0])
            .jsonPath("$.tags[1]").isEqualTo(recipeDto.tags!![1])
            .jsonPath("$.tags[2]").isEqualTo(recipeDto.labels!![0])
            .jsonPath("$.tags[3]").isEqualTo(recipeDto.labels!![1])
            .jsonPath("$.course").isEqualTo(recipeDto.course!!)
            .jsonPath("$.url").isEqualTo(recipeDto.url!!)
            .jsonPath("$.time").isEqualTo(recipeDto.time!!)

        verify(firestoreRepository).findById(RECIPE_ID)
    }

    @Test
    fun `should fail the find recipe by id when firestore repository returns error`() {
        // GIVEN
        `when`(firestoreRepository.findById(RECIPE_ID))
            .thenReturn(Mono.error(Exception()))

        // WHEN-THEN
        webClient
            .get()
            .uri(buildPath(GET_RECIPE_BY_ID_PATH, RECIPE_ID))
            .exchange()
            .expectStatus()
            .is5xxServerError
            .expectBody()
            .jsonPath("$.error.code").isEqualTo(GENERIC_ERROR.code)
            .jsonPath("$.error.message").isEqualTo(GENERIC_ERROR.message)
            .jsonPath("$.error.severity").isEqualTo(GENERIC_ERROR.severity.toString())

        verify(firestoreRepository).findById(RECIPE_ID)
    }
}
