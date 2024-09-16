package andvhuvnh.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import andvhuvnh.recipeapp.recipes.lib.Recipe
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var titleTextView: TextView
    private lateinit var ingredientsTextView: TextView
    private lateinit var instructionsTextView: TextView
    private lateinit var editButton: Button
    private lateinit var imageView: ImageView
    private var recipeId: String? = null
    private var firestore = FirebaseFirestore.getInstance()
    private var currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        titleTextView = findViewById(R.id.titleTextView)
        ingredientsTextView = findViewById(R.id.ingredientsTextView)
        instructionsTextView = findViewById(R.id.instructionsTextView)
        editButton = findViewById(R.id.editButton)
        imageView = findViewById(R.id.recipeImageView)
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
        currentUser?.let{user ->
            firestore.collection("users")
                .document(user.uid)
                .collection("recipes")
                .document(recipeId)
                .get()
                .addOnSuccessListener { document ->
                    val recipe = document.toObject(Recipe::class.java)
                    recipe?.let {
                        displayRecipeDetails(it)
                    }
                }
                .addOnFailureListener { e ->
                }}
    }

    private fun displayRecipeDetails(recipe: Recipe) {
        titleTextView.text = recipe.title
        ingredientsTextView.text = recipe.ingredients.joinToString(separator = "\n")
        instructionsTextView.text = recipe.instructions.joinToString(separator = "\n")
        Glide.with(this)
            .load(recipe.imageUrl)
            .into(imageView)
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
