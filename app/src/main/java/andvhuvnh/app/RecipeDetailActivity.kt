package andvhuvnh.app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import andvhuvnh.recipeapp.recipes.lib.Recipe
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

        lifecycleScope.launch(Dispatchers.IO){
            val recipe: Recipe? = recipeDao.getRecipeById(recipeId)
            recipe?.let {
                withContext(Dispatchers.Main){
                    displayRecipeDetails(it)
                }
            }
        }
    }

    private fun displayRecipeDetails(recipe: Recipe) {
        titleTextView.text = recipe.title
        ingredientsTextView.text = recipe.ingredients.joinToString(separator="\n" )
        instructionsTextView.text = recipe.instructions.joinToString(separator="\n" )
    }
}
