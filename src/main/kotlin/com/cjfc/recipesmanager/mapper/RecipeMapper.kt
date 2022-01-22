package com.cjfc.recipesmanager.mapper

import com.cjfc.recipesmanager.domain.Recipe
import com.cjfc.recipesmanager.presentation.payload.RecipePayload
import com.cjfc.recipesmanager.repository.dto.RecipeDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy.ERROR

@Mapper(componentModel = "spring", unmappedTargetPolicy = ERROR)
interface RecipeMapper {

    /**
     * Transform [Recipe] to [RecipePayload].
     *
     * @param recipe source
     * @return RecipePayload
     */
    fun toPayload(recipe: Recipe): RecipePayload

    /**
     * Transform [RecipePayload] to [Recipe].
     *
     * @param recipePayload source
     * @return Recipe
     */
    @Mapping(target = "id", ignore = true)
    fun toEntity(recipePayload: RecipePayload): Recipe

    /**
     * Transform [RecipeDto] to [Recipe].
     *
     * @param recipeDto source
     * @return Recipe
     */
    fun toEntity(recipeDto: RecipeDto): Recipe

    /**
     * Transform [Recipe] to [RecipeDto].
     *
     * @param recipe source
     * @return RecipeDto
     */
    fun toDto(recipe: Recipe): RecipeDto
}