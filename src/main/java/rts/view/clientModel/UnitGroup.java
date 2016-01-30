package rts.view.clientModel;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import rts.misc.Coords;
import rts.misc.GameVector;
import rts.view.clientModel.misc.Selectable;
import rts.view.mapView.Drawable;
import rts.view.mapView.GameGraphics;
import rts.view.panels.InfoTab;

// TODO: Auto-generated Javadoc
/**
 * Grupa jednostek
 */
public class UnitGroup implements Selectable// extends OwnableObject
{
    /**
     * Checks if is empty.
     * 
     * @return true, if is empty
     */
    public boolean isEmpty()
    {
        return units.isEmpty();
    }

    /** The formation. */
    private final Formation formation = new Square();
    /** The units. */
    private final Collection<ControllableUnit> units = new HashSet<ControllableUnit>();
    /** The info tab. */
    private InfoTab infoTab;
    /** The chooser. */
    private final CommandChooser chooser;

    // private final AbstractPlayer owner;
    /**
     * Instantiates a new unit group.
     * 
     * @param chooser the chooser
     */
    public UnitGroup(final CommandChooser chooser)
    {
        this.chooser = chooser;
    }
    /**
     * Initialize.
     */
    public void initialize()
    {
        infoTab = new InfoTab()
        {
            @Override
            public void initialize()
            {
            }
            @Override
            protected void paintComponent(final Graphics g)
            {
                super.paintComponent(g);
                int line = 0;
                g.drawString("Units selected: " + units.size(), 0, (line += 20));
                if(units.size() == 1)
                {
                    g.drawString("HP: " + 99, 0, (line += 20));
                }
            }
        };
    }
    /**
     * Roznica symetryczna jednostek grup
     * 
     * @param other the other
     * @return the collection< controllable unit>
     */
    public Collection<ControllableUnit> symmetricDifference(final UnitGroup other)
    {
        final List<ControllableUnit> list = new LinkedList<ControllableUnit>();
        if(other == null)
        {
            return units;
        }
        for(final ControllableUnit unit : other.getUnits())
        {
            if(!units.contains(unit))
            {
                list.add(unit);
            }
        }
        for(final ControllableUnit unit : units)
        {
            if(!other.units.contains(unit))
            {
                list.add(unit);
            }
        }
        return list;
    }
    /* (non-Javadoc)
     * @see rts.view.clientModel.misc.Selectable#getSelection()
     */
    @Override
    public Drawable getSelection()
    {
        final List<Drawable> drawableList = new ArrayList<Drawable>();
        for(final ControllableUnit unit : units)
        {
            drawableList.add(unit.getSelection());
            //  drawableList.add(new Oval(unit.getPosition(), 30, 25).colored(Color.green));
        }
        return new Drawable()
        {
            @Override
            public void draw(final GameGraphics g)
            {
                for(final Drawable selection : drawableList)
                {
                    selection.draw(g);
                }
            }
        };
    }
    /**
     * Adds the all.
     * 
     * @param list the list
     */
    public void addAll(final List<ControllableUnit> list)
    {
        if(list == null)
        {
            return;
        }
        ControllableUnit c;
        for(final PlayerObjectSprite sel : list)
        {
            c = (ControllableUnit) sel;
            units.add(c);
        }
    }
    /**
     * Adds the.
     * 
     * @param unitToAdd the unit to add
     */
    public void add(final ControllableUnit unitToAdd)
    {
        units.add(unitToAdd);
        // unitToAdd.linkToGroup(this);
    }
    /**
     * Removes the.
     * 
     * @param unit the unit
     */
    public void remove(final ControllableUnit unit)
    {
        units.remove(unit);
    }
    /**
     * Attack.
     * 
     * @param obj the obj
     */
    protected void attack(final PlayerObjectSprite obj)
    {
        for(final ControllableUnit c : units)
        {
            if(obj != c)
            {
                chooser.attack(c, obj);
            }
        }
    }
    /**
     * Attack move.
     * 
     * @param p the p
     */
    protected void attackMove(final Coords p)
    {
        // Res.mapView.addEffect(EffectsPool.Effect.ATTACKMOVE_CROSSHAIR, p);
        formation.attackMove(p);
    }
    /**
     * Move.
     * 
     * @param p the p
     */
    protected void move(final Coords p)
    {
        formation.move(p);
    }
    /**
     * Follow.
     * 
     * @param u the u
     */
    protected void follow(final UnitSprite u)
    {
        for(final ControllableUnit c : units)
        {
            if(u != c)
            {
                chooser.follow(c, u);
            }
        }
    }
    /**
     * Equals.
     * 
     * @param another the another
     * @return true, if successful
     */
    public boolean equals(final UnitGroup another)
    {
        return units.containsAll(another.units);
    }
    /**
     * Checks for.
     * 
     * @param unit the unit
     * @return true, if successful
     */
    public boolean has(final ControllableUnit unit)
    {
        return units.contains(unit);
    }
    /* (non-Javadoc)
     * @see rts.view.clientModel.misc.Selectable#getInfoTab()
     */
    @Override
    public InfoTab getInfoTab()
    {
        return infoTab;
    }
    /* (non-Javadoc)
     * @see rts.view.clientModel.misc.Selectable#setInfoTab(rts.view.panels.InfoTab)
     */
    @Override
    public void setInfoTab(final InfoTab info)
    {
    }
    /**
     * Gets the units.
     * 
     * @return the units
     */
    public Collection<ControllableUnit> getUnits()
    {
        return units;
    }

    /**
     * The Class Formation.
     */
    public abstract class Formation
    {
        /**
         * Move.
         * 
         * @param p the p
         */
        protected abstract void move(Coords p);
        /**
         * Attack move.
         * 
         * @param p the p
         */
        protected abstract void attackMove(Coords p);
    }

    /**
     * The Class Square.
     */
    public class Square extends Formation
    {
        /** The space. */
        private final int space = 22;

        /* (non-Javadoc)
         * @see rts.view.clientModel.UnitGroup.Formation#move(rts.misc.Coords)
         */
        @Override
        protected void move(final Coords p)
        {
            int col = 0;
            int row = 0;
            final int unitsInRow = (int) Math.ceil(Math.sqrt(getUnits().size()));
            final int shift = -(unitsInRow / 2) * space;
            for(final ControllableUnit unit : getUnits())
            {
                chooser.move(unit, p.moveBy(new GameVector(col * space + shift, row * space)));
                col++;
                if(col == unitsInRow)
                {
                    row++;
                    col = 0;
                }
            }
        }
        /* (non-Javadoc)
         * @see rts.view.clientModel.UnitGroup.Formation#attackMove(rts.misc.Coords)
         */
        @Override
        protected void attackMove(final Coords p)
        {
            int col = 0;
            int row = 0;
            final int unitsInRow = (int) Math.ceil(Math.sqrt(getUnits().size()));
            for(final ControllableUnit c : getUnits())
            {
                col++;
                if(col == unitsInRow)
                {
                    row++;
                    col = 0;
                }
            }
        }
    }
}
