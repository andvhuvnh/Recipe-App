package andvhuvnh.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import andvhuvnh.recipeapp.recipes.lib.Recipe
import kotlinx.coroutines.launch

class RecipeListActivity : AppCompatActivity() {
    private lateinit var recipeListView: ListView
    private val recipeList = mutableListOf<Recipe>()
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        recipeListView = findViewById(R.id.recipeListView)
        recipeAdapter = RecipeAdapter(this, recipeList)
        recipeListView.adapter = recipeAdapter

        loadRecipes()

        recipeListView.setOnItemClickListener{_,_, position, _ ->
            val selectedRecipe = recipeList[position]
            Log.d("RecipeListActivity", "Recipe clicked: ${selectedRecipe.title}")
            Toast.makeText(this, "Recipe clicked: ${selectedRecipe.title}", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("RECIPE_ID", selectedRecipe.id)
            startActivity(intent)
        }
    }

    private fun loadRecipes() {
        val recipeDao = (application as RecipeApp).database.recipeDao()

        lifecycleScope.launch {
            val recipes = recipeDao.getAllRecipes()
            recipeList.clear()
            recipeList.addAll(recipes)
            recipeAdapter.notifyDataSetChanged()
        }
    }
}
