package andvhuvnh.recipeapp.recipes.lib

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RecipeRepository(
    private val recipeDao: RecipeDao,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
){

    private val userId: String?
        get()=firebaseAuth.currentUser?.uid

    suspend fun syncRecipesFromFirebase(){
        userId?.let{ userId ->
            val documents = withContext(Dispatchers.IO){
                firestore.collection("users").document(userId).collection("recipes")
                    .get()
                    .await()
            }

            val recipes = documents.map{ document ->
                document.toObject(Recipe::class.java)
            }

            withContext(Dispatchers.IO){
                recipeDao.deleteAll() //clear old data
                recipeDao.insertAll(recipes)
            }

        }
    }
    suspend fun saveRecipe(recipe: Recipe){
        withContext(Dispatchers.IO) {
            recipeDao.insert(recipe)
        }

        userId?.let{userId ->
            withContext(Dispatchers.IO){
                firestore.collection("users").document(userId).collection("recipes")
                    .document(recipe.id)
                    .set(recipe)
                    .await()
            }
        }
    }

    suspend fun getRecipeById(id: String): Recipe?{
        return recipeDao.getRecipeById(id)
    }

    suspend fun getAllRecipes():List<Recipe> {
        return withContext(Dispatchers.IO) {
            recipeDao.getAllRecipes()
        }
    }
}