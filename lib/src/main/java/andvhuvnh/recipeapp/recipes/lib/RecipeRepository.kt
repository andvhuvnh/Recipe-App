package andvhuvnh.recipeapp.recipes.lib

class RecipeRepository(private val recipeDao: RecipeDao) {

    suspend fun addRecipe(recipe: Recipe){
        recipeDao.insert(recipe)
    }

    suspend fun getRecipeById(id: String): Recipe?{
        return recipeDao.getRecipeById(id)
    }

    suspend fun getAllRecipes():List<Recipe> {
        return recipeDao.getAllRecipes()
    }
}