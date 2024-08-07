package andvhuvnh.recipeapp.recipes.lib
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
class Recipe (
    @PrimaryKey val id: String,
    val title: String,
    val ingredients: String,
    val instructions: String
    )