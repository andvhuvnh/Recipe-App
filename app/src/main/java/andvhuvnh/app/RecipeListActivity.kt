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
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class RecipeListActivity : AppCompatActivity() {
    private lateinit var recipeListView: ListView
    private val recipeList = mutableListOf<Recipe>()
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var addRecipeButton: Button
    private lateinit var homeButton: Button

    private var firestore = FirebaseFirestore.getInstance()
    private var currentUser = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        recipeListView = findViewById(R.id.recipeListView)
        recipeAdapter = RecipeAdapter(this, recipeList)
        recipeListView.adapter = recipeAdapter
        addRecipeButton = findViewById(R.id.addRecipeButton)
        homeButton = findViewById(R.id.homeButton)

        listenForRecipeUpdates()

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

        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun listenForRecipeUpdates() {
        currentUser?.let {user ->
            firestore.collection("users")
                .document(user.uid)
                .collection("recipes")
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w("RecipeListActivity", "Listen failed", e)
                        return@addSnapshotListener
                    }
                    for (dc in snapshots?.documentChanges!!){
                        when (dc.type){
                            DocumentChange.Type.ADDED->{
                                val recipe = dc.document.toObject(Recipe::class.java)
                                recipeList.add(recipe)
                                recipeAdapter.notifyDataSetChanged()
                            }
                            DocumentChange.Type.MODIFIED->{
                                val recipe = dc.document.toObject(Recipe::class.java)
                                val index = recipeList.indexOfFirst {it.id == recipe.id}
                                if (index != -1){
                                    recipeList[index] = recipe
                                    recipeAdapter.notifyDataSetChanged()
                                }
                            }
                            DocumentChange.Type.REMOVED ->{
                                val recipe = dc.document.toObject(Recipe::class.java)
                                val index = recipeList.indexOfFirst { it.id == recipe.id }
                                if (index != -1) {
                                    recipeList.removeAt(index)
                                    recipeAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
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
