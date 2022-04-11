package com.cjfc.recipesmanager.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
class ContextConfiguration {

    companion object {
        private val ALLOWED_ORIGIN_PATTERNS =
            listOf(
                "http*://192.168.1.*:8082",
                "http*://localhost:8082",
                "http*://cjfc.ignorelist.com:8082",
                "http*://recipes.local.me"
            )
        private val ALLOWED_METHODS = listOf("GET", "POST", "PATCH")
    }

    @Bean
    fun corsWebFilter(): CorsWebFilter? {
        val corsConfig = CorsConfiguration()
        //TODO: specify port
        corsConfig.allowedOriginPatterns = ALLOWED_ORIGIN_PATTERNS
        corsConfig.allowedMethods = ALLOWED_METHODS
        corsConfig.allowedHeaders = listOf("*")
        corsConfig.maxAge = 8000L
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)
        return CorsWebFilter(source)
    }
}
