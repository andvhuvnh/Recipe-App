package andvhuvnh.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import andvhuvnh.recipeapp.recipes.lib.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID

class CreateRecipeActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var ingredientsLayout: LinearLayout
    private lateinit var instructionsLayout: LinearLayout
    private lateinit var addIngredientsButton: Button
    private lateinit var addInstructionButton: Button
    private lateinit var saveButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_recipe)

        titleEditText = findViewById(R.id.titleEditText)
        ingredientsLayout = findViewById(R.id.ingredientsLayout)
        instructionsLayout = findViewById(R.id.instructionsLayout)
        saveButton = findViewById(R.id.saveButton)
        addIngredientsButton = findViewById(R.id.addIngredientButton)
        addInstructionButton = findViewById(R.id.addInstructionsButton)

        addIngredientField()
        addInstructionField()

        addIngredientsButton.setOnClickListener{
            addIngredientField()
        }

        addInstructionButton.setOnClickListener {
            addInstructionField()
        }

        saveButton.setOnClickListener {
            saveRecipe()
        }
    }

    private fun addInstructionField() {
        val instructionEditText = EditText(this)
        instructionEditText.hint = "Enter Instruction"
        instructionsLayout.addView(instructionEditText)
    }

    private fun addIngredientField() {
        val ingredientEditText = EditText(this)
        ingredientEditText.hint = "Enter Ingredient"
        ingredientsLayout.addView(ingredientEditText)
    }

    private fun saveRecipe() {
        val title = titleEditText.text.toString()
        val ingredients = mutableListOf<String>()
        for (i in 0 until ingredientsLayout.childCount){
            val ingredientEditText = ingredientsLayout.getChildAt(i) as EditText
            ingredients.add(ingredientEditText.text.toString())
        }

        val instructions = mutableListOf<String>()
        for(i in 0 until instructionsLayout.childCount){
            val instructionEditText = instructionsLayout.getChildAt(i) as EditText
            instructions.add(instructionEditText.text.toString())
        }

        if (title.isNotEmpty()&& ingredients.isNotEmpty() && instructions.isNotEmpty()){
            val recipe = Recipe(UUID.randomUUID().toString(), title, ingredients, instructions)
            val recipeDatabase = (application as RecipeApp).database
            val recipeDao = recipeDatabase.recipeDao()

            GlobalScope.launch(Dispatchers.IO) {
                recipeDao.insert(recipe)
            }

            Toast.makeText(this, "Recipe added successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else{
            Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_SHORT).show()
        }


    }
}