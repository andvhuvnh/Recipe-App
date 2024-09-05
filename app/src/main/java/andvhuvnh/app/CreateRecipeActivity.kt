package andvhuvnh.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import andvhuvnh.recipeapp.recipes.lib.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class CreateRecipeActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var ingredientsLayout: LinearLayout
    private lateinit var instructionsLayout: LinearLayout
    private lateinit var addIngredientsButton: Button
    private lateinit var addInstructionButton: Button
    private lateinit var saveButton: Button

    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
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

            currentUser?.let {user ->
                firestore.collection("users")
                    .document(user.uid)
                    .collection("recipes")
                    .document(recipe.id)
                    .set(recipe)
                    .addOnSuccessListener {
                        Toast.makeText(this,"Recipe added successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener{ e ->
                        Toast.makeText(this, "Error adding recipe: ${e.message}",Toast.LENGTH_SHORT).show()
                    }
            } ?:run{
                Toast.makeText(this,"User not authenticated", Toast.LENGTH_SHORT).show()
            }

        } else{
            Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onResume() {
        super.onResume()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}