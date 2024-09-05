package andvhuvnh.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import andvhuvnh.recipeapp.recipes.lib.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditRecipeActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var ingredientsEditText: EditText
    private lateinit var instructionsEditText: EditText
    private lateinit var saveButton: Button

    private var recipeId: String? = null
    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)

        titleEditText = findViewById(R.id.titleEditText)
        ingredientsEditText = findViewById(R.id.ingredientsEditText)
        instructionsEditText = findViewById(R.id.instructionsEditText)
        saveButton = findViewById(R.id.saveButton)

        recipeId = intent.getStringExtra("RECIPE_ID")
        if (recipeId != null) {
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

            currentUser?.let{user ->
                firestore.collection("users")
                    .document(user.uid)
                    .collection("recipes")
                    .document(recipeId!!)
                    .set(recipe)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Recipe updated Successfully", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    }
                    .addOnFailureListener{ e ->
                        Toast.makeText(this, "Error updating Recipe: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        } else {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadRecipeDetails(recipeId: String) {
       currentUser?.let{user ->
           firestore.collection("users")
               .document(user.uid)
               .collection("recipes")
               .document(recipeId)
               .get()
               .addOnSuccessListener { document ->
                   val recipe = document.toObject(Recipe::class.java)
                   recipe?.let{
                       titleEditText.setText(it.title)
                       ingredientsEditText.setText(it.ingredients.joinToString("\n"))
                       instructionsEditText.setText(it.instructions.joinToString("\n"))
                   }
               }
               .addOnFailureListener{ e ->
                   Toast.makeText(this, "Error loading Recipe", Toast.LENGTH_SHORT).show()
               }
       }
    }
}
