package andvhuvnh.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var createRecipeButton: Button
    private lateinit var viewRecipesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createRecipeButton = findViewById(R.id.createRecipeButton)
        viewRecipesButton = findViewById(R.id.viewRecipesButton)
        createRecipeButton.setOnClickListener{
            startActivity(Intent(this, CreateRecipeActivity::class.java))
        }

        viewRecipesButton.setOnClickListener {
            startActivity(Intent(this,RecipeListActivity::class.java))
        }
    }
}
