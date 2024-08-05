package andvhuvnh.recipeapp.recipes.lib

object RecipeRepository {
    private val recipes = mutableListOf<Recipe>()

    fun addRecipe(recipe: Recipe){
        recipes.add(recipe)
    }

    fun getRecipes(): List<Recipe>{
        return recipes
    }

    fun getRecipeById(id: String): Recipe? {
        return recipes.find {it.id == id }
    }
}