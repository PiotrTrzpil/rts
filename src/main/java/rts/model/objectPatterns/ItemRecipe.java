package rts.model.objectPatterns;

import java.util.ArrayList;
import rts.model.Item;

/**
 * Recepta na przedmiot(y).
 */
public class ItemRecipe extends Recipe
{
    /** Lista produkowanych przedmiotow. */
    private final ArrayList<StaticItemSlot> products;

    /**
     * Instantiates a new item recipe.
     * 
     * @param productionTime the production time
     */
    public ItemRecipe(final int productionTime)
    {
        super(productionTime);
        products = new ArrayList<StaticItemSlot>();
    }
    /**
     * Adds the product.
     * 
     * @param item the item
     * @param n the n
     */
    public void addProduct(final Item item, final int n)
    {
        products.add(new StaticItemSlot(item, n));
    }
    /**
     * Gets the products.
     * 
     * @return the products
     */
    public ArrayList<StaticItemSlot> getProducts()
    {
        return products;
    }
}