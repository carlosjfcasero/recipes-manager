package com.cjfc.recipesmanager.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
class ContextConfiguration {

    @Bean
    fun corsWebFilter(): CorsWebFilter? {
        val corsConfig = CorsConfiguration()
        //TODO: specify port
        corsConfig.allowedOriginPatterns = listOf("*192.168.1.*:[*]", "*localhost:[*]", "*cjfc.ignorelist.com:[*]")
        corsConfig.allowedMethods = listOf("GET", "POST", "PATCH")
        corsConfig.allowedHeaders = listOf("*")
        corsConfig.maxAge = 8000L
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)
        return CorsWebFilter(source)
    }
}
