package com.cjfc.recipesmanager.it

import com.cjfc.recipesmanager.repository.FirestoreRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(SpringExtension::class)
@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = ["spring.cloud.gcp.firestore.enabled=false"]
)
class ActuatorInfoIT {

    companion object {
        private const val ACTUATOR_INFO_PATH = "/recipes-manager/info"
        private const val BUILD_ARTIFACT = "recipes-manager"
        private const val BUILD_NAME = "RecipesManager"
        private const val BUILD_GROUP = "com.cjfc"
    }

    @MockBean
    lateinit var firestoreRepository: FirestoreRepository

    @LocalManagementPort
    private var managementPort: Int? = null

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun whenCallActuatorInfo_thenSuccess() {
        // WHEN-THEN
        webTestClient
            .get()
            .uri("http://localhost:$managementPort$ACTUATOR_INFO_PATH")
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.git.branch").isNotEmpty
            .jsonPath("$.git.commit.id").isNotEmpty
            .jsonPath("$.git.commit.time").isNotEmpty
            .jsonPath("$.build.artifact").isEqualTo(BUILD_ARTIFACT)
            .jsonPath("$.build.name").isEqualTo(BUILD_NAME)
            .jsonPath("$.build.time").isNotEmpty
            .jsonPath("$.build.version").isNotEmpty
            .jsonPath("$.build.group").isEqualTo(BUILD_GROUP)
    }
}