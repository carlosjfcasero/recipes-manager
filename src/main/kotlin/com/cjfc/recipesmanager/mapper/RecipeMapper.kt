package com.cjfc.recipesmanager.mapper

import com.cjfc.recipesmanager.domain.BaseRecipe
import com.cjfc.recipesmanager.dto.BaseRecipeDto
import com.cjfc.recipesmanager.presentation.payload.BaseRecipePayload
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy.ERROR

@Mapper(componentModel = "spring", unmappedTargetPolicy = ERROR)
interface RecipeMapper {

    /**
     * Transform [BaseRecipe] to [BaseRecipePayload].
     *
     * @param baseRecipe source
     * @return RecipeBasePayload
     */
//    @Mapping(target = "copy", ignore = true)
    fun toPayload(baseRecipe: BaseRecipe): BaseRecipePayload

    /**
     * Transform [BaseRecipeDto] to [BaseRecipe].
     *
     * @param baseRecipeDto source
     * @return RecipeBase
     */
//    @Mapping(target = "copy", ignore = true)
    fun toEntity(baseRecipeDto: BaseRecipeDto): BaseRecipe
}