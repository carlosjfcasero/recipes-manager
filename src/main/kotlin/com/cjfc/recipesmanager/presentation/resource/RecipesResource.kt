package com.ing.es.cardservicing.presentation.resource

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping(value = ["/card-servicing"])
class RecipesResource {

    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/recipes")
    fun getRecipes(): Mono<String> {
        log.info("PATCH - v1/cards/actions/activate - Activate card with UUID")

        return Mono.just("RECIPES")
    }
}