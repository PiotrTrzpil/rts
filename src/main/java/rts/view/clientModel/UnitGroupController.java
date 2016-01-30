package rts.view.clientModel;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import rts.controller.UserPlayer;
import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.view.animation.EffectsPool;
import rts.view.clientModel.misc.RelationFlag;
import rts.view.mapView.MapView;

// TODO: Auto-generated Javadoc
/**
 * Kontroler grup
 */
public class UnitGroupController
{
    /** The unit groups. */
    private final List<UnitGroup> unitGroups;
    /** Aktualnie wybrana przez uzytkownika grupa. */
    private UnitGroup current;
    /** The owner. */
    private final UserPlayer owner;
    /** The view. */
    private final MapView view;
    /** The object holder. */
    private final ObjectHolder objectHolder;
    /** The chooser. */
    private final CommandChooser chooser;

    /**
     * Instantiates a new unit group controller.
     * 
     * @param owner the owner
     * @param view the view
     * @param objectHolder the object holder
     * @param chooser the chooser
     */
    public UnitGroupController(final UserPlayer owner, final MapView view,
        final ObjectHolder objectHolder, final CommandChooser chooser)
    {
        this.owner = owner;
        this.view = view;
        this.objectHolder = objectHolder;
        this.chooser = chooser;
        unitGroups = new LinkedList<UnitGroup>();
    }
    /**
     * Sets the current.
     * 
     * @param group the new current
     */
    public void setCurrent(final UnitGroup group)
    {
        if(group != null || current != null)
        {
            Collection<ControllableUnit> symmetricDifference = null;
            if(current != null)
            {
                symmetricDifference = current.symmetricDifference(group);
                unitGroups.remove(current);
            }
            else if(group != null)
            {
                symmetricDifference = group.symmetricDifference(current);
                unitGroups.add(group);
            }
            for(final ControllableUnit unit : symmetricDifference)
            {
                unit.getSelectionAnimation().nextKeyState();
            }
            current = group;
        }
    }
    /**
     * Odznacza jednostki
     */
    public void deselect()
    {
        if(current != null)
        {
            for(final ControllableUnit unit : current.getUnits())
            {
                unit.getSelectionAnimation().nextKeyState();
            }
            unitGroups.remove(current);
            current = null;
        }
    }
    /**
     * Polecenie ataku lub przejscia w dany punkt
     * 
     * @param destination the p
     */
    public void moveOrAttack(final Coords destination)
    {
        final List<? extends PlayerObjectSprite> objects = objectHolder.extractObjects(null, null,
            null, new ConstRect(destination, destination));
        if(!objects.isEmpty())
        {
            final PlayerObjectSprite obj = objects.get(0);
            final RelationFlag relation = obj.getRelationTo(owner);
            if(obj instanceof UnitSprite)
            {
                final UnitSprite u = (UnitSprite) obj;
                if(relation == RelationFlag.FRIENDLY)
                {
                    view.addEffect(EffectsPool.Effect.MOVE_CROSSHAIR, u.getPosition());
                    //    current.attack(u);
                    current.follow(u);
                }
                else if(relation == RelationFlag.HOSTILE)
                {
                    // System.out.println("Follow");
                    view.addEffect(EffectsPool.Effect.ATTACK_CROSSHAIR, u.getPosition());
                    current.attack(u);
                }
            }
            else if(obj instanceof BuildingSprite)
            {
                final BuildingSprite b = (BuildingSprite) obj;
                if(relation == RelationFlag.FRIENDLY)
                {
                    view.addEffect(EffectsPool.Effect.MOVE_CROSSHAIR, b.getBounds().getCenter());
                    //        current.goToBuilding(b, p);
                    // current.attack(b);
                }
                else if(relation == RelationFlag.HOSTILE)
                {
                    view.addEffect(EffectsPool.Effect.ATTACK_CROSSHAIR, b.getBounds().getCenter());
                    current.attack(b);
                }
            }
        }
        else
        {
            view.addEffect(EffectsPool.Effect.MOVE_CROSSHAIR, destination);
            current.move(destination);
        }
    }
    /**
     * Porusza jednostki do danego punktu, lub atakuje dowolny obiekt
     * 
     * @param clickPoint the click point
     */
    public void agressiveMove(final Coords clickPoint)
    {
        final List<? extends PlayerObjectSprite> objects = objectHolder.extractObjects(null, null,
            null, new ConstRect(clickPoint));
        if(objects != null && !objects.isEmpty())
        {
            view.addEffect(EffectsPool.Effect.ATTACK_CROSSHAIR, clickPoint);
            current.attack(objects.get(0));
        }
        else
        {
            view.addEffect(EffectsPool.Effect.MOVE_CROSSHAIR, clickPoint);
            current.move(clickPoint);
        }
    }
    /**
     * Odlacza jednostke od wszystkich grup
     * 
     * @param controllable the controllable
     */
    public void unlinkFromAll(final ControllableUnit controllable)
    {
        for(final UnitGroup group : unitGroups)
        {
            group.remove(controllable);
        }
        if(current != null && current.isEmpty())
        {
            objectHolder.deselect();
        }
        //unitGroups.clear();
    }
    /**
     * Dodaje lub usuwa jednostki zaleznie od nowo zaznaczonych. Jesli w nowo
     * zaznaczonych znajduja sie jednostki aktualnie wybrane, usuwa je z
     * aktualnej grup. W przciwnym wypadku dodaje nowo wybrane do aktualnie
     * wybranej grupy.
     * 
     * @param newSelected the new selected
     */
    public void addOrRemove(final Collection<ControllableUnit> newSelected)
    {
        final Collection<ControllableUnit> unitsChanged = new HashSet<ControllableUnit>();
        final Collection<ControllableUnit> currentUnits = current.getUnits();
        //final UnitGroup group = (UnitGroup) selection;
        //        boolean added = false;
        for(final ControllableUnit unit : newSelected)
        {
            if(!currentUnits.contains(unit))
            {
                current.add(unit);
                unitsChanged.add(unit);
                //                added = true;
            }
        }
        if(unitsChanged.isEmpty())
        {
            for(final ControllableUnit unit : newSelected)
            {
                current.remove(unit);
                unitsChanged.add(unit);
            }
        }
        for(final ControllableUnit unit : unitsChanged)
        {
            unit.getSelectionAnimation().nextKeyState();
        }
        if(current.isEmpty())
        {
            current = null;
            objectHolder.deselect();
        }
    }
    /**
     * Nowo wybrane jednostki
     * 
     * @param list the list
     * @return the unit group
     */
    public UnitGroup select(final List<ControllableUnit> list)
    {
        UnitGroup group = null;
        if(!list.isEmpty())
        {
            group = new UnitGroup(chooser);
            group.initialize();
            group.addAll(list);
        }
        setCurrent(group);
        return group;
    }
}
