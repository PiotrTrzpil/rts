package rts.model.map;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import rts.misc.Coords;
import rts.misc.TileDimensions;

// TODO: Auto-generated Javadoc
/**
 * Ustawienia podstawowej mapy.
 */
public class MapSettings implements Serializable
{
    /** Pozycje startowe graczy. */
    private final List<Coords> startPlaces;
    /** Wymiary mapy. */
    private final TileDimensions dimensions;
    /** Aktualna pozycja. */
    private int place;

    /**
     * Instantiates a new map settings.
     */
    public MapSettings()
    {
        dimensions = new TileDimensions(64, 64);
        startPlaces = new LinkedList<Coords>();
        startPlaces.add(new Coords(520, 520));
        startPlaces.add(new Coords(1640, 520));
        startPlaces.add(new Coords(520, 1640));
        startPlaces.add(new Coords(1640, 1640));
    }
    /**
     * Gets the start places.
     * 
     * @return the start places
     */
    public List<Coords> getStartPlaces()
    {
        return startPlaces;
    }
    /**
     * Gets the number of players.
     * 
     * @return the number of players
     */
    public int getNumberOfPlayers()
    {
        return startPlaces.size();
    }
    /**
     * Gets the dimensions.
     * 
     * @return the dimensions
     */
    public TileDimensions getDimensions()
    {
        return dimensions;
    }
    /**
     * Next start place.
     * 
     * @return the coords
     */
    public Coords nextStartPlace()
    {
        if(place >= startPlaces.size())
        {
            throw new IllegalStateException("No more start places available. Maximum players: " + startPlaces.size());
        }
        return startPlaces.get(place++);
    }
}
