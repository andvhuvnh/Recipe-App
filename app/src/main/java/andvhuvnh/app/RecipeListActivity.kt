package andvhuvnh.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import andvhuvnh.recipeapp.recipes.lib.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RecipeListActivity : AppCompatActivity() {
    private lateinit var recipeListView: ListView
    private val recipeList = mutableListOf<Recipe>()
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var addRecipeButton: Button

    private var firestore = FirebaseFirestore.getInstance()
    private var currentUser = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        recipeListView = findViewById(R.id.recipeListView)
        recipeAdapter = RecipeAdapter(this, recipeList)
        recipeListView.adapter = recipeAdapter
        addRecipeButton = findViewById(R.id.addRecipeButton)

        loadRecipes()

        recipeListView.setOnItemClickListener{_,_, position, _ ->
            val selectedRecipe = recipeList[position]
            Log.d("RecipeListActivity", "Recipe clicked: ${selectedRecipe.title}")
            Toast.makeText(this, "Recipe clicked: ${selectedRecipe.title}", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("RECIPE_ID", selectedRecipe.id)
            startActivity(intent)
        }

        addRecipeButton.setOnClickListener {
            val intent = Intent(this, CreateRecipeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadRecipes() {
        currentUser?.let {user ->
            firestore.collection("users")
                .document(user.uid)
                .collection("recipes")
                .get()
                .addOnSuccessListener { result ->
                    recipeList.clear()
                    for(document in result){
                        val recipe = document.toObject(Recipe::class.java)
                        recipeList.add(recipe)
                    }
                    recipeAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener{ e ->
                    Toast.makeText(this,"Failed to load recipes : ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
