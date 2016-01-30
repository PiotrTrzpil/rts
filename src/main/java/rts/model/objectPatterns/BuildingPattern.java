package rts.model.objectPatterns;

import java.util.ArrayList;
import java.util.EnumMap;
import rts.model.BuildingType;
import rts.model.Item;
import rts.model.UnitType;
import rts.model.serverBuildings.ItemSlot;
import rts.view.clientModel.BuildingImage;

/**
 * Wzor budynku. Kazdy typ budynku ma swoj wzor przechowujacy informacje wspolne
 * dla budynkow danego typu.
 */
public class BuildingPattern
{
    /** The type. */
    private final BuildingType type;
    /** The sprite. */
    private final BuildingImage sprite;
    /** The ingr slots to copy. */
    private final EnumMap<Item, ItemSlot> ingrSlotsToCopy;
    /** The prod slots to copy. */
    private final EnumMap<Item, ItemSlot> prodSlotsToCopy;
    /** The item recipes. */
    private final ArrayList<ItemRecipe> itemRecipes;
    /** The build recipe. */
    private final BuildRecipe buildRecipe;
    // private final ArrayList<UnitRecipe> unitRecipes;
    /** The unit recipes. */
    private final EnumMap<UnitType, UnitRecipe> unitRecipes;

    /**
     * Instantiates a new building pattern.
     * 
     * @param type the type
     * @param sprite the sprite
     */
    public BuildingPattern(final BuildingType type, final BuildingImage sprite)
    {
        this.type = type;
        //        name = type.getName();
        this.sprite = sprite;
        buildRecipe = new BuildRecipe();
        // sprite = Res.imageLoader.getBuildingSprite(name);
        ingrSlotsToCopy = new EnumMap<Item, ItemSlot>(Item.class);
        prodSlotsToCopy = new EnumMap<Item, ItemSlot>(Item.class);
        itemRecipes = new ArrayList<ItemRecipe>();
        unitRecipes = new EnumMap<UnitType, UnitRecipe>(UnitType.class);
    }
    /**
     * Clone ingredient slots.
     * 
     * @return the set< item slot>
     */
    public EnumMap<Item, ItemSlot> cloneIngredientSlots()
    {
        final EnumMap<Item, ItemSlot> ret = new EnumMap<Item, ItemSlot>(Item.class);
        for(final ItemSlot slot : ingrSlotsToCopy.values())
        {
            ret.put(slot.getItemType(), new ItemSlot(slot.getItemType(), slot.getMaxSpace()));
        }
        return ret;
    }
    /**
     * Clone product slots.
     * 
     * @return the set< item slot>
     */
    public EnumMap<Item, ItemSlot> cloneProductSlots()
    {
        final EnumMap<Item, ItemSlot> ret = new EnumMap<Item, ItemSlot>(Item.class);
        for(final ItemSlot slot : prodSlotsToCopy.values())
        {
            ret.put(slot.getItemType(), new ItemSlot(slot.getItemType(), slot.getMaxSpace()));
        }
        return ret;
    }
    /**
     * Metoda tworzy sloty na przedmioty w zaleznosci od posiadanych przez
     * budynek recept.
     */
    public void slotsFromRecipes()
    {
        for(final ItemRecipe rec : itemRecipes)
        {
            for(final StaticItemSlot slot : rec.getIngredients())
            {
                ingrSlotsToCopy.put(slot.getItemType(), new ItemSlot(slot.getItemType()));
            }
            for(final StaticItemSlot slot : rec.getProducts())
            {
                prodSlotsToCopy.put(slot.getItemType(), new ItemSlot(slot.getItemType()));
            }
        }
        // recipes.
        for(final UnitRecipe rec : unitRecipes.values())
        {
            for(final StaticItemSlot slot : rec.getIngredients())
            {
                ingrSlotsToCopy.put(slot.getItemType(), new ItemSlot(slot.getItemType()));
            }
        }
    }
    //    /**
    //     * Gets the name.
    //     *
    //     * @return the name
    //     */
    //    public String getName()
    //    {
    //        return name;
    //    }
    /**
     * Gets the sprite.
     * 
     * @return the sprite
     */
    public BuildingImage getSprite()
    {
        return sprite;
    }
    /**
     * Adds the recipe.
     * 
     * @param rec the rec
     */
    public void addRecipe(final ItemRecipe rec)
    {
        itemRecipes.add(rec);
    }
    /**
     * Adds the recipe.
     * 
     * @param rec the rec
     */
    public void addRecipe(final UnitRecipe rec)
    {
        unitRecipes.put(rec.getType(), rec);
    }
    public void addBuildIngredient(final Item item, final int n)
    {
        buildRecipe.addBuildIngredient(item, n);
    }
    /**
     * Gets the item recipes.
     * 
     * @return the item recipes
     */
    public ArrayList<ItemRecipe> getItemRecipes()
    {
        return itemRecipes;
    }
    /**
     * Gets the unit recipes.
     * 
     * @return the unit recipes
     */
    public EnumMap<UnitType, UnitRecipe> getUnitRecipes()
    {
        return unitRecipes;
    }
    /**
     * Gets the type.
     * 
     * @return the type
     */
    public BuildingType getType()
    {
        return type;
    }
}
