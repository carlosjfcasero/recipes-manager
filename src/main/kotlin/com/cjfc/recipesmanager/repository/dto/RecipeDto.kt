package com.cjfc.recipesmanager.repository.dto

import com.google.cloud.firestore.annotation.DocumentId
import com.google.cloud.firestore.annotation.IgnoreExtraProperties
import org.springframework.cloud.gcp.data.firestore.Document

@IgnoreExtraProperties
@Document(collectionName = "recipes")
data class RecipeDto(
    @DocumentId
    val id: String,
    val name: String? = null,
    val description: String? = null,
    val favourite: Boolean? = null,
    val ingredients: String? = null,
    val origin: String? = null,
    val temperature: String? = null,
    val labels: List<String>? = null,
    val course: String? = null
)