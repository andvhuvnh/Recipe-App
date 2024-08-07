package andvhuvnh.app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import andvhuvnh.recipeapp.recipes.lib.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var titleTextView: TextView
    private lateinit var ingredientsTextView: TextView
    private lateinit var instructionsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        titleTextView = findViewById(R.id.titleTextView)
        ingredientsTextView = findViewById(R.id.ingredientsTextView)
        instructionsTextView = findViewById(R.id.instructionsTextView)

        val recipeId = intent.getStringExtra("RECIPE_ID")
        if (recipeId != null){
            fetchRecipeDetails(recipeId)
        }
    }

    private fun fetchRecipeDetails(recipeId: String) {
        val recipeDatabase = (application as RecipeApp).database
        val recipeDao = recipeDatabase.recipeDao()

        CoroutineScope(Dispatchers.IO).launch{
            val recipe = recipeDao.getRecipeById(recipeId)
            withContext(Dispatchers.Main){
                recipe?.let {
                    displayRecipeDetails(it)
                }
            }
        }
    }

    private fun displayRecipeDetails(recipe: Recipe) {
        titleTextView.text = recipe.title
        ingredientsTextView.text = recipe.ingredients
        instructionsTextView.text = recipe.instructions
    }
}
