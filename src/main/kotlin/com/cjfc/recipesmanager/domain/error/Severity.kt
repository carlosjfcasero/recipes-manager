package com.cjfc.recipesmanager.domain.error

enum class Severity(val value: String) {

    CRITICAL("critical"),
    ERROR("error"),
    WARNING("warning"),
    INFO("info");
}