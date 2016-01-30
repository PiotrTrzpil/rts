package rts.model;

import java.awt.Color;
import java.util.ArrayList;
import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.view.mapView.ColorShape;
import rts.view.mapView.Drawable;
import rts.view.mapView.DrawableNull;

/**
 * Przedmioty uzywane w grze
 */
public enum Item
{
    /** Nic. */
    NULL_ITEM("Nothing"),
    /** Kloda drewna. */
    LOG("Log")
    {
        @Override
        public void setPriority()
        {
            demanderPriority.add(BuildingType.LUMBER_MILL);
            demanderPriority.add(BuildingType.BOWMAKER);
        }
        @Override
        public Drawable getImage()
        {
            return new ColorShape(Color.ORANGE, new Coords(0, 0));
        }
    },
    /** Deska. */
    PLANK("Plank")
    {
        @Override
        public void setPriority()
        {
            demanderPriority.add(BuildingType.BARRACKS);
            demanderPriority.add(BuildingType.ARMORER);
        }
        @Override
        public Drawable getImage()
        {
            return new ColorShape(Color.ORANGE, new Coords(0, 0));
        }
    },
    /** Kamien. */
    STONE("Stone")
    {
        @Override
        public void setPriority()
        {
            demanderPriority.add(BuildingType.ARMORER);
        }
        @Override
        public Drawable getImage()
        {
            return new ColorShape(Color.GRAY, new ConstRect(0, 0, 4, 4));
        }
    },
    BOW("Bow")
    {
        @Override
        public void setPriority()
        {
            demanderPriority.add(BuildingType.BARRACKS);
        }
        @Override
        public Drawable getImage()
        {
            return new ColorShape(Color.blue, new ConstRect(0, 0, 4, 4));
        }
    },
    SWORD("Sword")
    {
        @Override
        public void setPriority()
        {
            demanderPriority.add(BuildingType.BARRACKS);
        }
        @Override
        public Drawable getImage()
        {
            return new ColorShape(Color.BLACK, new ConstRect(0, 0, 4, 4));
        }
    },
    SHIELD("Shield")
    {
        @Override
        public void setPriority()
        {
            demanderPriority.add(BuildingType.BARRACKS);
        }
        @Override
        public Drawable getImage()
        {
            return new ColorShape(Color.WHITE, new ConstRect(0, 0, 4, 4));
        }
    },
    PIEROGI("Pierogi")
    {
        @Override
        public void setPriority()
        {
            demanderPriority.add(BuildingType.BARRACKS);
        }
        @Override
        public Drawable getImage()
        {
            return new ColorShape(Color.PINK, new ConstRect(0, 0, 4, 4));
        }
    },
    ;
    /** Nazwa tekstowa. */
    private String name;
    public ArrayList<BuildingType> demanderPriority;
    static
    {
        for(final Item item : values())
        {
            item.demanderPriority = new ArrayList<BuildingType>();
            item.setPriority();
        }
    }

    /**
     * Instantiates a new item.
     * 
     * @param name the name
     */
    private Item(final String name)
    {
        this.name = name;
    }
    public void setPriority()
    {
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
    public Drawable getImage()
    {
        return new DrawableNull();
    }
}