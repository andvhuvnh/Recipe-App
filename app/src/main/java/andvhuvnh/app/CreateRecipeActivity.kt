package andvhuvnh.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import andvhuvnh.recipeapp.recipes.lib.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class CreateRecipeActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var ingredientsLayout: LinearLayout
    private lateinit var instructionsLayout: LinearLayout
    private lateinit var recipeImageView: ImageView
    private lateinit var uploadImageButton: Button
    private lateinit var addIngredientsButton: Button
    private lateinit var addInstructionButton: Button
    private lateinit var saveButton: Button

    private var imageUri: Uri? = null

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
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
        recipeImageView = findViewById(R.id.recipeImageView)
        uploadImageButton = findViewById(R.id.uploadImageButton)

        addIngredientField()
        addInstructionField()

        addIngredientsButton.setOnClickListener{
            addIngredientField()
        }

        addInstructionButton.setOnClickListener {
            addInstructionField()
        }

        uploadImageButton.setOnClickListener { selectImage() }
        saveButton.setOnClickListener {
            saveRecipe()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null){
            imageUri = data.data
            recipeImageView.setImageURI(imageUri)
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
            uploadImageAndSaveRecipe(recipe)
        } else{
            Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageAndSaveRecipe(recipe: Recipe){
        if (imageUri != null) {
            val storageRef = storage.reference.child("images/${UUID.randomUUID()}")
            storageRef.putFile(imageUri!!)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        recipe.imageUrl = uri.toString()  // Save the image URL to the recipe object
                        saveRecipeToFirestore(recipe)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            saveRecipeToFirestore(recipe) // If no image selected, save recipe without image
        }
    }

    private fun saveRecipeToFirestore(recipe: Recipe){
        currentUser?.let { user ->
            firestore.collection("users")
                .document(user.uid)
                .collection("recipes")
                .document(recipe.id)
                .set(recipe)
                .addOnSuccessListener {
                    Toast.makeText(this, "Recipe added successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error adding recipe: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
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

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }
}