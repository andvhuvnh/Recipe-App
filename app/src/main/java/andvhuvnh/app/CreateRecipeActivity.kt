package andvhuvnh.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import andvhuvnh.recipeapp.recipes.lib.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
class CreateRecipeActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var ingredientsEditText: EditText
    private lateinit var instructionsEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_recipe)

        titleEditText = findViewById(R.id.titleEditText)
        ingredientsEditText =findViewById(R.id.ingredientsEditText)
        instructionsEditText = findViewById(R.id.instructionsEditText)
        saveButton = findViewById(R.id.saveButton)

        saveButton.setOnClickListener{
            val title = titleEditText.text.toString()
            val ingredients = ingredientsEditText.text.toString()
            val instructions = instructionsEditText.text.toString()

            if (title.isNotEmpty() && ingredients.isNotEmpty() && instructions.isNotEmpty()) {
                val recipe = Recipe(UUID.randomUUID().toString(), title, ingredients, instructions)
                saveRecipe(recipe)
            } else {
                Toast.makeText(this,"Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveRecipe(recipe: Recipe) {
        val recipeDatabase = (application as RecipeApp).database
        val recipeDao = recipeDatabase.recipeDao()

        CoroutineScope(Dispatchers.IO).launch {
            recipeDao.insert(recipe)
            runOnUiThread {
                Toast.makeText(this@CreateRecipeActivity, "Recipe added successfully", Toast.LENGTH_SHORT).show()
            }
        }
        Toast.makeText(this, "Recipe added successfully", Toast.LENGTH_SHORT).show()
        finish()
    }
}