package com.cjfc.recipesmanager.dto

import com.google.cloud.firestore.annotation.DocumentId
import com.google.cloud.firestore.annotation.IgnoreExtraProperties
import org.springframework.cloud.gcp.data.firestore.Document

@IgnoreExtraProperties
@Document(collectionName = "recipes")
data class BaseRecipeDto(
    @DocumentId val id: String,
    val name: String
)