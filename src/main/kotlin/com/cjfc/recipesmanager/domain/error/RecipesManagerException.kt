package com.cjfc.recipesmanager.domain.error

class RecipesManagerException(
    override val message: String,
    override val cause: Throwable? = null,
    val errorType: ErrorType
) :
    RuntimeException(message, cause) {
    var errorCode: Long? = null
    var source: String? = null
    var target: String? = null
    var innerErrors: List<RecipesManagerException> = emptyList()
}