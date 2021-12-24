package com.cjfc.recipesmanager.presentation.payload.error

enum class Severity(val value: String) {

    CRITICAL("critical"),
    ERROR("error"),
    WARNING("warning"),
    INFO("info");
}