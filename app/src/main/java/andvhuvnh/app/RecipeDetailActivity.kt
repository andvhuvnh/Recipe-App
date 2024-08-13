package andvhuvnh.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
    private lateinit var editButton: Button
    private var recipeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        titleTextView = findViewById(R.id.titleTextView)
        ingredientsTextView = findViewById(R.id.ingredientsTextView)
        instructionsTextView = findViewById(R.id.instructionsTextView)
        editButton = findViewById(R.id.editButton)
        recipeId = intent.getStringExtra("RECIPE_ID")

        if (recipeId != null){
            fetchRecipeDetails(recipeId!!)
        }

        editButton.setOnClickListener {
            val intent = Intent(this, EditRecipeActivity::class.java)
            intent.putExtra("RECIPE_ID", recipeId)
            startActivityForResult(intent, REQUEST_CODE_EDIT)

        }
    }

    private fun fetchRecipeDetails(recipeId: String) {
        val recipeDatabase = (application as RecipeApp).database
        val recipeDao = recipeDatabase.recipeDao()

        lifecycleScope.launch(Dispatchers.IO) {
            val recipe: Recipe? = recipeDao.getRecipeById(recipeId)
            recipe?.let {
                withContext(Dispatchers.Main) {
                    displayRecipeDetails(it)
                }
            }
        }
    }

    private fun displayRecipeDetails(recipe: Recipe) {
        titleTextView.text = recipe.title
        ingredientsTextView.text = recipe.ingredients.joinToString(separator = "\n")
        instructionsTextView.text = recipe.instructions.joinToString(separator = "\n")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK){
            recipeId?.let {fetchRecipeDetails(it) }
        }
    }
    companion object {
        private const val REQUEST_CODE_EDIT = 1
    }
}
