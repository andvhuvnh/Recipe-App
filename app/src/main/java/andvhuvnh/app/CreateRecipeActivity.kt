package andvhuvnh.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import andvhuvnh.recipeapp.recipes.lib.Recipe
import andvhuvnh.recipeapp.recipes.lib.RecipeRepository
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
                RecipeRepository.addRecipe(recipe)
                Toast.makeText(this, "Recipe added successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this,"Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}