package andvhuvnh.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeListActivity : AppCompatActivity() {
    private lateinit var recipeListView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        recipeListView = findViewById(R.id.recipeListView)
        loadRecipes()
    }

    private fun loadRecipes() {
        val recipeDatabase = (application as RecipeApp).database
        val recipeDao = recipeDatabase.recipeDao()

        CoroutineScope(Dispatchers.IO).launch{
            val recipes = recipeDao.getAllRecipes()
            val recipeTitles = recipes.map {it.title}
            withContext(Dispatchers.Main){
                Log.d("RecipeListActivity", "Recipe Titles: $recipeTitles")
                val adapter = ArrayAdapter(this@RecipeListActivity, android.R.layout.simple_list_item_1, recipeTitles)
                recipeListView.adapter = adapter

                recipeListView.setOnItemClickListener{_,_,position,_ ->
                    val selectedRecipe = recipes[position]
                    val intent = Intent(this@RecipeListActivity, RecipeDetailActivity::class.java)
                    intent.putExtra("RECIPE_ID", selectedRecipe.id)
                    startActivity(intent)
                }
            }
        }
    }
}
