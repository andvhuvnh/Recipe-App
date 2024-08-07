package andvhuvnh.app

import andvhuvnh.recipeapp.recipes.lib.RecipeDatabase
import android.app.Application
import androidx.room.Room

class RecipeApp : Application() {
    lateinit var database: RecipeDatabase
    override fun onCreate(){
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            RecipeDatabase::class.java,
            "recipe_database"
        ).build()
    }
}