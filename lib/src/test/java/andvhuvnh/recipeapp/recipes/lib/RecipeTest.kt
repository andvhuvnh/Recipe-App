package andvhuvnh.recipeapp.recipes.lib

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RecipeTest {

    @Test
    fun testGetId() {
        for (i in 0..100) {
            val recipe = Recipe(i.toString(), "Test Recipe $i", "Ingredients $i", "Instructions $i")
            assertEquals(i.toString(), recipe.id)
        }
    }

    @Test
    fun testGetTitle() {
        for (i in 0..100) {
            val recipeTitle = "Test Recipe $i"
            val recipe = Recipe(i.toString(), recipeTitle, "Ingredients $i", "Instructions $i")
            assertEquals(recipeTitle, recipe.title)
        }
    }

    @Test
    fun testGetIngredients() {
        for (i in 0..100) {
            val recipeIngredients = "Ingredients $i"
            val recipe = Recipe(i.toString(), "Test Recipe $i", recipeIngredients, "Instructions $i")
            assertEquals(recipeIngredients, recipe.ingredients)
        }
    }

    @Test
    fun testGetInstructions() {
        for (i in 0..100) {
            val recipeInstructions = "Instructions $i"
            val recipe = Recipe(i.toString(), "Test Recipe $i", "Ingredients $i", recipeInstructions)
            assertEquals(recipeInstructions, recipe.instructions)
        }
    }
}
