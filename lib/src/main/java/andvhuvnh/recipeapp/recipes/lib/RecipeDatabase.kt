package andvhuvnh.recipeapp.recipes.lib

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Recipe::class], version = 1)
@TypeConverters(RecipeTypeConverters::class)
abstract class RecipeDatabase : RoomDatabase(){
    abstract fun recipeDao(): RecipeDao

    companion object {
    }
}