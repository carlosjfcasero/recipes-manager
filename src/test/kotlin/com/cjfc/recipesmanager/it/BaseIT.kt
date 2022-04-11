package com.cjfc.recipesmanager.it

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.util.UriComponentsBuilder

@ExtendWith(SpringExtension::class)
@SpringBootTest(properties = ["spring.cloud.gcp.firestore.enabled=false"])
@AutoConfigureWebTestClient
abstract class BaseIT {

    protected fun buildPath(path: String, vararg uriVariables: String?) =
        UriComponentsBuilder.fromPath(path).buildAndExpand(*uriVariables).path!!
}
