package com.cjfc.recipesmanager.presentation.payload.error

data class ErrorDetails(
    val code: String,
    val message: String,
    val severity: Severity
)