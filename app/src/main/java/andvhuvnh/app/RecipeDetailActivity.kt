package andvhuvnh.app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import andvhuvnh.recipeapp.R
import andvhuvnh.recipeapp.recipes.lib.RecipeRepository

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
        val recipe = RecipeRepository.getRecipeById(recipeId!!)

        recipe?.let{
            titleTextView.text = it.title
            ingredientsTextView.text= it.ingredients
            instructionsTextView.text = it.instructions
        }
    }
}
