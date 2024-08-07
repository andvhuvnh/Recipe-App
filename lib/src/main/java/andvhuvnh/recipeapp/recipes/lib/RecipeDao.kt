package andvhuvnh.recipeapp.recipes.lib

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import andvhuvnh.recipeapp.recipes.lib.Recipe

@Dao
interface RecipeDao{
    @Insert
    suspend fun insert(recipe: Recipe)

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: String): Recipe?

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<Recipe>
}