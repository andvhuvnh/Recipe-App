package andvhuvnh.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var createRecipeButton: Button
    private lateinit var viewRecipesButton: Button
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createRecipeButton = findViewById(R.id.createRecipeButton)
        viewRecipesButton = findViewById(R.id.viewRecipesButton)
        logoutButton = findViewById(R.id.logoutButton)

        createRecipeButton.setOnClickListener{
            startActivity(Intent(this, CreateRecipeActivity::class.java))
        }

        viewRecipesButton.setOnClickListener {
            startActivity(Intent(this,RecipeListActivity::class.java))
        }

        logoutButton.setOnClickListener {
            logout()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun logout(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
