package andvhuvnh.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null){
            //User is signed in, go to home screen
            val intent = Intent(this,RecipeListActivity::class.java)
            startActivity(intent)
        } else{
            //Not logged in -> go to login screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        finish() //close activity
    }
}