package andvhuvnh.app

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import andvhuvnh.recipeapp.recipes.lib.RecipeRepository

class RecipeListActivity : AppCompatActivity() {
    private lateinit var recipeListView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        val recipes = RecipeRepository.getRecipes()
        val recipeTitles = recipes.map { it.title }
        recipeListView = findViewById(R.id.recipeListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, recipeTitles)
        recipeListView.adapter = adapter

        recipeListView.setOnItemClickListener { _, _, position, _ ->
            val selectedRecipe = recipes[position]
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("RECIPE_ID", selectedRecipe.id)
            startActivity(intent)
        }


    }
}
