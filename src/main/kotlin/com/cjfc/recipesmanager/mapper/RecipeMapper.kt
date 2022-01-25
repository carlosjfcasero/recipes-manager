package com.cjfc.recipesmanager.mapper

import com.cjfc.recipesmanager.domain.Recipe
import com.cjfc.recipesmanager.presentation.payload.RecipePayload
import com.cjfc.recipesmanager.repository.dto.RecipeDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
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
    @Mapping(target = "tags", expression = "java(RecipeMapper.Companion.joinLabelsAndTags(recipeDto))")
    fun toEntity(recipeDto: RecipeDto): Recipe

    /**
     * Transform [Recipe] to [RecipeDto].
     *
     * @param recipe source
     * @return RecipeDto
     */
    @Mapping(target = "labels", ignore = true)
    fun toDto(recipe: Recipe): RecipeDto

    companion object {
        @JvmStatic
        @Named("joinLabelsAndTags")
        fun joinLabelsAndTags(recipeDto: RecipeDto): List<String>? {
            val tags: List<String> = recipeDto.tags.orEmpty()
            val labels: List<String> = recipeDto.labels.orEmpty()
            return listOf(tags, labels).flatten().ifEmpty { null }
        }
    }
}