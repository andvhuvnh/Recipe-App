package andvhuvnh.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import andvhuvnh.recipeapp.recipes.lib.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditRecipeActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var ingredientsEditText: EditText
    private lateinit var instructionsEditText: EditText
    private lateinit var saveButton: Button

    private var recipeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)

        titleEditText = findViewById(R.id.titleEditText)
        ingredientsEditText = findViewById(R.id.ingredientsEditText)
        instructionsEditText = findViewById(R.id.instructionsEditText)
        saveButton = findViewById(R.id.saveButton)

        recipeId = intent.getStringExtra("RECIPE_ID")
        if(recipeId != null){
            loadRecipeDetails(recipeId!!)
        }

        saveButton.setOnClickListener {
            updateRecipeDetails()
        }
    }

    private fun updateRecipeDetails() {
        val title = titleEditText.text.toString()
        val ingredients = ingredientsEditText.text.toString().split("\n").filter { it.isNotBlank() }
        val instructions = instructionsEditText.text.toString().split("\n").filter { it.isNotBlank() }

        if (title.isNotEmpty() && ingredients.isNotEmpty() && instructions.isNotEmpty()) {
            val recipe = Recipe(recipeId!!, title, ingredients, instructions)
            val recipeDatabase = (application as RecipeApp).database
            val recipeDao = recipeDatabase.recipeDao()

            lifecycleScope.launch(Dispatchers.IO) {
                recipeDao.update(recipe)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditRecipeActivity, "Recipe updated successfully", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
            }
        } else {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadRecipeDetails(recipeId: String) {
        val recipeDatabase = (application as RecipeApp).database
        val recipeDao = recipeDatabase.recipeDao()

        lifecycleScope.launch(Dispatchers.IO) {
            val recipe: Recipe? = recipeDao.getRecipeById(recipeId)
            recipe?.let {
                withContext(Dispatchers.Main) {
                    titleEditText.setText(it.title)
                    ingredientsEditText.setText(it.ingredients.joinToString("\n"))
                    instructionsEditText.setText(it.instructions.joinToString("\n"))
                }
            }
        }
    }
}
