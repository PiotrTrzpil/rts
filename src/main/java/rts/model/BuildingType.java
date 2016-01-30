package rts.model;

import java.io.Serializable;
import rts.model.objectPatterns.BuildingPattern;
import rts.model.objectPatterns.ItemRecipe;
import rts.model.objectPatterns.UnitRecipe;
import rts.model.serverBuildings.Building;
import rts.model.serverBuildings.ProductionBuilding;
import rts.model.serverBuildings.Storehouse;
import rts.model.serverBuildings.UnitTrainBuilding;
import rts.view.ViewModel;
import rts.view.clientModel.BuildingSprite;
import rts.view.panels.BuildingInfoTab;
import rts.view.panels.ProductionInfoTab;
import rts.view.panels.StorehouseInfoTab;
import rts.view.panels.TrainInfoTab;

/**
 * Typ budynku.
 */
public enum BuildingType implements Serializable
{
    /** The BARRACKS. */
    BARRACKS("Barracks", 1200, UnitTrainBuilding.class, TrainInfoTab.class)
    {
        @Override
        public void createPattern(final BuildingPattern pattern)
        {
            UnitRecipe unit;
            unit = new UnitRecipe(UnitType.WARRIOR, 14000);
            unit.addIngredient(Item.SWORD, 1);
            unit.addIngredient(Item.SHIELD, 1);
            unit.addIngredient(Item.PIEROGI, 2);
            unit = new UnitRecipe(UnitType.ARCHER, 12000);
            unit.addIngredient(Item.BOW, 1);
            unit.addIngredient(Item.PIEROGI, 1);
            pattern.addRecipe(unit);
            unit = new UnitRecipe(UnitType.CARRIER, 10000);
            unit.addIngredient(Item.PLANK, 2);
            pattern.addRecipe(unit);
            unit = new UnitRecipe(UnitType.BUILDER, 10000);
            unit.addIngredient(Item.PLANK, 1);
            pattern.addRecipe(unit);
        }
    },
    /** The LUMBE r_ mill. */
    LUMBER_MILL("LumberMill", 550, ProductionBuilding.class, ProductionInfoTab.class)
    {
        @Override
        public void createPattern(final BuildingPattern pattern)
        {
            final ItemRecipe plank = new ItemRecipe(20000);
            plank.addIngredient(Item.LOG, 1);
            plank.addProduct(Item.PLANK, 2);
            pattern.addRecipe(plank);
        }
    },
    /** The WOODCUTTER. */
    WOODCUTTER("Woodcutter", 400, ProductionBuilding.class, ProductionInfoTab.class)
    {
        @Override
        public void createPattern(final BuildingPattern pattern)
        {
            final ItemRecipe log = new ItemRecipe(15000);
            log.addProduct(Item.LOG, 1);
            pattern.addRecipe(log);
        }
    },
    /** The STONECUTTER. */
    STONECUTTER("Stonecutter", 500, ProductionBuilding.class, ProductionInfoTab.class)
    {
        @Override
        public void createPattern(final BuildingPattern pattern)
        {
            final ItemRecipe stone = new ItemRecipe(16000);
            stone.addProduct(Item.STONE, 1);
            pattern.addRecipe(stone);
        }
    },
    /** The BOWMAKER. */
    BOWMAKER("Bowmaker", 800, ProductionBuilding.class, ProductionInfoTab.class)
    {
        @Override
        public void createPattern(final BuildingPattern pattern)
        {
            final ItemRecipe recipe1 = new ItemRecipe(22000);
            recipe1.addIngredient(Item.LOG, 1);
            recipe1.addProduct(Item.BOW, 3);
            pattern.addRecipe(recipe1);
        }
    },
    /** The ARMORER. */
    ARMORER("Armorer", 1000, ProductionBuilding.class, ProductionInfoTab.class)
    {
        @Override
        public void createPattern(final BuildingPattern pattern)
        {
            final ItemRecipe recipe1 = new ItemRecipe(25000);
            recipe1.addIngredient(Item.STONE, 3);
            recipe1.addIngredient(Item.PLANK, 2);
            recipe1.addProduct(Item.SWORD, 2);
            pattern.addRecipe(recipe1);
            final ItemRecipe recipe2 = new ItemRecipe(19000);
            recipe2.addIngredient(Item.PLANK, 3);
            recipe2.addIngredient(Item.STONE, 1);
            recipe2.addProduct(Item.SHIELD, 1);
            recipe2.addProduct(Item.PIEROGI, 3);
            pattern.addRecipe(recipe2);
        }
    },
    /** The STOREHOUSE. */
    STOREHOUSE("Storehouse", 1500, Storehouse.class, StorehouseInfoTab.class);
    /** The name. */
    private String name;
    private int maxHealthPoints;
    /** The b class. */
    private final Class<? extends Building> bClass;
    /** The info class. */
    private final Class<? extends BuildingInfoTab> infoClass;

    /**
     * Instantiates a new building type.
     * 
     * @param n the n
     * @param bClass the b class
     * @param infoClass the info class
     */
    BuildingType(final String n, final int maxHealthPoints, final Class<? extends Building> bClass,
        final Class<? extends BuildingInfoTab> infoClass)
    {
        name = n;
        this.maxHealthPoints = maxHealthPoints;
        this.bClass = bClass;
        this.infoClass = infoClass;
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString()
    {
        return name;
    }
    /**
     * Gets the building class.
     * 
     * @return the building class
     */
    public Class<? extends Building> getBuildingClass()
    {
        return bClass;
    }
    /**
     * Creates the info tab.
     * 
     * @param mapEnviroment the map enviroment
     * @param sprite the sprite
     * @return the building info tab
     */
    public BuildingInfoTab createInfoTab(final ViewModel mapEnviroment, final BuildingSprite sprite)
    {
        BuildingInfoTab infoTab = null;
        try
        {
            infoTab = infoClass.getDeclaredConstructor(ViewModel.class, BuildingSprite.class)
                    .newInstance(mapEnviroment, sprite);
        }
        catch(final Exception e)
        {
            e.printStackTrace();
        }
        return infoTab;
    }
    /**
     * Tworzy wzor cech budynku, czyli recepty na produkcje przedmiotow,
     * jednostek itp.
     * 
     * @param pattern the pattern
     */
    public void createPattern(final BuildingPattern pattern)
    {
    }
    public int getMaxHealthPoints()
    {
        return maxHealthPoints;
    }
}
