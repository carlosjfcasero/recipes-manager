package com.cjfc.recipesmanager.repository.dto

import com.google.cloud.firestore.annotation.DocumentId
import com.google.cloud.firestore.annotation.IgnoreExtraProperties
import org.springframework.cloud.gcp.data.firestore.Document

@IgnoreExtraProperties
@Document(collectionName = "recipes")
data class RecipeDto(
    @DocumentId
    val id: String?,
    val name: String?,
    val description: String?,
    val favourite: Boolean?,
    val ingredients: String?,
    val origin: String?,
    val temperature: String?,
    val labels: List<String>?,
    val tags: List<String>?,
    val course: String?
)