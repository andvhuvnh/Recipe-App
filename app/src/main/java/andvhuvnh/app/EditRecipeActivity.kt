package andvhuvnh.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import andvhuvnh.recipeapp.recipes.lib.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class EditRecipeActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var ingredientsEditText: EditText
    private lateinit var instructionsEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var recipeImageView: ImageView
    private lateinit var uploadImageButton: Button
    private var imageUri: Uri? = null

    private var recipeId: String? = null
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)

        titleEditText = findViewById(R.id.titleEditText)
        ingredientsEditText = findViewById(R.id.ingredientsEditText)
        instructionsEditText = findViewById(R.id.instructionsEditText)
        saveButton = findViewById(R.id.saveButton)
        recipeImageView = findViewById(R.id.recipeImageView)
        uploadImageButton = findViewById(R.id.uploadImageButton)

        recipeId = intent.getStringExtra("RECIPE_ID")
        if (recipeId != null) {
            loadRecipeDetails(recipeId!!)
        }

        uploadImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
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
            currentUser?.let{user ->
                val recipe = Recipe(recipeId!!, title, ingredients, instructions, "")
                if(imageUri != null){
                    val storageRef = storage.reference.child("images/${recipeId!!}")
                    storageRef.putFile(imageUri!!)
                        .addOnSuccessListener {
                            storageRef.downloadUrl.addOnSuccessListener {uri ->
                                recipe.imageUrl = uri.toString()
                                saveRecipeToFireStore(recipe)
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Image upload failed ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    saveRecipeToFireStore(recipe)
                }
            }
        } else {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveRecipeToFireStore(recipe: Recipe) {
        currentUser?.let { user ->
            firestore.collection("users")
                .document(user.uid)
                .collection("recipes")
                .document(recipeId!!)
                .set(recipe)
                .addOnSuccessListener {
                    Toast.makeText(this, "Recipe updated successfully", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error updating recipe: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            imageUri = data.data
            recipeImageView.setImageURI(imageUri)
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

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }
}
