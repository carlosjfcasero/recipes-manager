package com.cjfc.recipesmanager.mapper

import com.cjfc.recipesmanager.domain.BaseRecipe
import com.cjfc.recipesmanager.dto.BaseRecipeDto
import com.cjfc.recipesmanager.presentation.payload.BaseRecipePayload
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mapstruct.factory.Mappers
import org.mockito.junit.jupiter.MockitoExtension
import uk.co.jemos.podam.api.PodamFactory
import uk.co.jemos.podam.api.PodamFactoryImpl

@ExtendWith(MockitoExtension::class)
class RecipeMapperTest {

    private val podamFactory: PodamFactory = PodamFactoryImpl()
    private val underTest: RecipeMapper = Mappers.getMapper(RecipeMapper::class.java)

    @Test
    fun givenBaseRecipe_whenMapToPayload_thenSuccess() {
        // GIVEN
        val baseRecipe = podamFactory.manufacturePojoWithFullData(BaseRecipe::class.java)
        val expectedBaseRecipePayload = BaseRecipePayload(id = baseRecipe.id, name = baseRecipe.name)

        // WHEN
        val result = underTest.toPayload(baseRecipe)

        // THEN
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(expectedBaseRecipePayload)
    }

    @Test
    fun givenBaseRecipeDto_whenMapToEntity_thenSuccess() {
        // GIVEN
        val baseRecipeDto = podamFactory.manufacturePojoWithFullData(BaseRecipeDto::class.java)
        val expectedBaseRecipe = BaseRecipe(id = baseRecipeDto.id, name = baseRecipeDto.name)

        // WHEN
        val result = underTest.toEntity(baseRecipeDto)

        // THEN
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(expectedBaseRecipe)
    }
}