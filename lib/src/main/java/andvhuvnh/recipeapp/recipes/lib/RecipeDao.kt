package andvhuvnh.recipeapp.recipes.lib

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecipeDao{
    @Insert
    suspend fun insert(recipe: Recipe)

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: String): Recipe?

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<Recipe>

    @Delete
    suspend fun delete(recipe: Recipe)
}