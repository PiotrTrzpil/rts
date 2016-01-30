package rts.model.objectPatterns;

import java.util.HashSet;
import java.util.Set;
import rts.model.Item;
import rts.model.serverBuildings.ItemSlot;

/**
 * Recepta na budowe budynku.
 */
public class BuildRecipe
{
    /** The Constant hitsPerItemNeeded. */
    public static final int hitsPerItemNeeded = 2;
    /** The ingredients. */
    private final Set<ItemSlot> ingredients;
    /** The total hits needed. */
    private int totalHitsNeeded;

    /**
     * Instantiates a new builds the recipe.
     */
    public BuildRecipe()
    {
        ingredients = new HashSet<ItemSlot>();
    }
    /**
     * Adds the build ingredient.
     * 
     * @param item the item
     * @param n the n
     */
    public void addBuildIngredient(final Item item, final int n)
    {
        ingredients.add(new ItemSlot(item, n));
        totalHitsNeeded += n * hitsPerItemNeeded;
    }
    /**
     * Clone slots.
     * 
     * @return the set< item slot>
     */
    public Set<ItemSlot> cloneSlots()
    {
        final Set<ItemSlot> ret = new HashSet<ItemSlot>();
        // ret.ensureCapacity(ingredients.size());
        for(final ItemSlot slot : ingredients)
        {
            ret.add(new ItemSlot(slot.getItemType(), slot.getMaxSpace()));
        }
        return ret;
    }
    /**
     * Hits needed.
     * 
     * @return the int
     */
    public int hitsNeeded()
    {
        return totalHitsNeeded;
    }
}