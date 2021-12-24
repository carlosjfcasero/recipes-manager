package com.cjfc.recipesmanager.presentation.resource

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/keepalive"])
class KeepAliveResource {

    @GetMapping
    fun keepAlive(): String = "KEEPALIVE_OK"
}