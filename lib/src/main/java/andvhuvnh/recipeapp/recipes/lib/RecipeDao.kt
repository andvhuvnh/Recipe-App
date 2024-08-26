package andvhuvnh.recipeapp.recipes.lib

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecipeDao{
    @Insert
    suspend fun insert(recipe: Recipe)

    @Insert
    suspend fun insertAll(recipes:List<Recipe>)

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: String): Recipe?

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<Recipe>

    @Delete
    suspend fun delete(recipe: Recipe)

    @Update
    suspend fun update(recipe: Recipe)

    @Query("DELETE FROM recipes")
    suspend fun deleteAll()
}