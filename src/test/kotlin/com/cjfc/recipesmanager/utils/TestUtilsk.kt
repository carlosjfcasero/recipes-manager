package com.cjfc.recipesmanager.utils

import com.cjfc.recipesmanager.domain.error.RecipesManagerException
import uk.co.jemos.podam.api.PodamFactory
import uk.co.jemos.podam.api.PodamFactoryImpl

class TestUtils {

    companion object {
        private val podamFactory: PodamFactory = PodamFactoryImpl()

        /**
         * Creates a random [RecipesManagerException].
         *
         * @return the generated [RecipesManagerException].
         */
        fun createRandomRecipesManagerException(): RecipesManagerException =
            podamFactory.manufacturePojoWithFullData(RecipesManagerException::class.java)
    }
}
