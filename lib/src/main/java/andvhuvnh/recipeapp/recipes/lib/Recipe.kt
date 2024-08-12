package andvhuvnh.recipeapp.recipes.lib
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "recipes")
class Recipe(
    @PrimaryKey val id: String,
    val title: String,
    val ingredients: List<String>,
    val instructions: List<String>
    )

class RecipeTypeConverters{
    @TypeConverter
    fun fromStringList(value: List<String>): String{
        return value.joinToString (separator = ",")
    }

    @TypeConverter
    fun toStringList(value: String): List<String>{
        return value.split(",")
    }
}