package andvhuvnh.app

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import andvhuvnh.recipeapp.recipes.lib.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RecipeAdapter (
    private val context: Context,
    private val recipes: MutableList<Recipe>
): ArrayAdapter<Recipe>(context, 0, recipes){

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val recipe = recipes[position]

        val recipeTitleTextView = view.findViewById<TextView>(R.id.recipeTitleTextView)
        val deleteButton = view.findViewById<ImageButton>(R.id.deleteButton)

        recipeTitleTextView.text = recipe.title

        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog(recipe, recipe.id)
        }

        // Set up the item click listener to view recipe details
        view.setOnClickListener {
            val intent = Intent(context, RecipeDetailActivity::class.java).apply {
                putExtra("RECIPE_ID", recipe.id)
            }
            context.startActivity(intent)
        }
        return view
    }

    private fun showDeleteConfirmationDialog(recipe: Recipe, id: String) {
        AlertDialog.Builder(context).apply{
            setTitle("Delete")
            setMessage("Are you sure you want to delete this recipe?")
            setPositiveButton("Yes"){_, _ ->
                deleteRecipe(recipe, id)
            }
            setNegativeButton("No", null)
            create().show()
        }
    }

    private fun deleteRecipe(recipe: Recipe, id: String) {
        val currentUser = auth.currentUser

        if (currentUser == null){
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        android.util.Log.d("RecipeAdapter", "Attempting to delete recipe at position: $id")
        android.util.Log.d("RecipeAdapter", "Current recipe list size before deletion: ${recipes.size}")

        firestore.collection("users")
            .document(currentUser.uid)
            .collection("recipes")
            .document(recipe.id)
            .delete()
            .addOnSuccessListener {
                    Toast.makeText(context, "Successfully deleted recipe", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                Toast.makeText(
                    context, "Error deleting recipe: ${e.message}", Toast.LENGTH_SHORT
                ).show()
            }
    }
}
