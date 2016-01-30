package rts.model.objectPatterns;

import java.util.Set;
import java.util.TreeSet;
import rts.model.Item;

// TODO: Auto-generated Javadoc
/**
 * Recepta na jakis wyrob.
 */
public class Recipe
{
    /** Skladniki. */
    private final Set<StaticItemSlot> ingredients;
    /** Czas produkcji. */
    private final int productionTime;

    /**
     * Instantiates a new recipe.
     * 
     * @param productionTime the production time
     */
    public Recipe(final int productionTime)
    {
        ingredients = new TreeSet<StaticItemSlot>();
        this.productionTime = productionTime;
    }
    /**
     * Adds the ingredient.
     * 
     * @param item the item
     * @param n the n
     */
    public void addIngredient(final Item item, final int n)
    {
        ingredients.add(new StaticItemSlot(item, n));
    }
    /**
     * Gets the production time.
     * 
     * @return the production time
     */
    public int getProductionTime()
    {
        return productionTime;
    }
    /**
     * Gets the ingredients.
     * 
     * @return the ingredients
     */
    public Set<StaticItemSlot> getIngredients()
    {
        return ingredients;
    }
}
