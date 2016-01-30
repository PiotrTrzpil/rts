package rts.model.map;

//import terrain.*;
//import units.*;
import java.util.ArrayList;
import java.util.List;
import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.misc.TileDimensions;
import rts.misc.exceptions.OutOfMapException;
import rts.view.clientModel.BuildingImage;

// TODO: Auto-generated Javadoc
/**
 * Mapa budynkow.
 */
public class BuildMap
{
    /** The tile map. */
    private Tile tileMap[][];
    /** Szerokosc w pikselach. */
    private int pixdimX;
    /** Wysokosc w pikselach. */
    private int pixdimY;
    /** Szerokosc w polach terenu. */
    private int tiledimX;
    /** Wysokosc w polach terenu. */
    private int tiledimY;

    /**
     * Instantiates a new builds the map.
     * 
     * @param settings the settings
     */
    public BuildMap(final MapSettings settings)
    {
        loadDefault(settings.getDimensions());
        makeEdgesUnbuildable();
    }
    /**
     * Sprawia, ze na brzegu mapy nie mozna wybudowac budynku
     */
    private void makeEdgesUnbuildable()
    {
        for(int i = 0; i < tiledimY; i++)
        {
            for(int j = 0; j < tiledimX; j++)
            {
                if(i == 0 || i == tiledimX - 1 || j == 0 || j == tiledimY - 1)
                {
                    tileMap[j][i].setBuildable(false);
                }
            }
        }
    }
    /**
     * Tworzy podstawowa mape
     * 
     * @param tileDimensions the tile dimensions
     */
    public void loadDefault(final TileDimensions tileDimensions)
    {
        tiledimX = tileDimensions.getDimX();
        tiledimY = tileDimensions.getDimY();
        pixdimX = tiledimX * Tile.SIZE;
        pixdimY = tiledimY * Tile.SIZE;
        tileMap = new Tile[tiledimY][tiledimX];
        for(int i = 0; i < tiledimY; i++)
        {
            for(int j = 0; j < tiledimX; j++)
            {
                tileMap[i][j] = new Tile(j, i, null);
            }
        }
    }
    /**
     * Gets the tile.
     * 
     * @param p the p
     * @return the tile
     * @throws OutOfMapException the out of map exception
     */
    public Tile getTile(final Coords p) throws OutOfMapException
    {
        Tile tile = null;
        try
        {
            tile = tileMap[p.getY() >> Tile.SIZE_LOG2][p.getX() >> Tile.SIZE_LOG2];
        }
        catch(final Exception e)
        {
            throw new OutOfMapException(p.toString());
        }
        return tile;
    }
    /**
     * Zwraca kwadraty terenu pod danym prostokatem
     * 
     * @param rect the rect
     * @return the tiles
     */
    private List<Tile> getTiles(final ConstRect rect)
    {
        final List<Tile> list = new ArrayList<Tile>();
        final int sx = (rect.getX()) >> Tile.SIZE_LOG2;
        final int sy = (rect.getY()) >> Tile.SIZE_LOG2;
        final int ex = ((rect.getX() + rect.getWidth() - 1) >> Tile.SIZE_LOG2);
        final int ey = ((rect.getY() + rect.getHeight() - 1) >> Tile.SIZE_LOG2);
        for(int i = sy; i <= ey; i++)
        {
            for(int j = sx; j <= ex; j++)
            {
                if(i >= 0 && i < tiledimY && j >= 0 && j < tiledimX)
                {
                    list.add(tileMap[i][j]);
                }
            }
        }
        return list;
    }
    /**
     * Sprawdza czy budynek moze zostac zbudowany w danym miejscu.
     * 
     * @param buildingImage sprite okreslajacy obszar zajmowany przez budynek.
     * @param buildPoint punkt okreslajacy polozenie.
     * @return true, jesli budynek moze zostac zbudowany w danym miejscu.
     */
    public boolean canBeBuild(final BuildingImage buildingImage, final Coords buildPoint)
    {
        final ConstRect rect = new ConstRect(buildPoint, buildingImage.getDimensions().add(-1));
        for(final Tile t : getTiles(rect))
        {
            if(!t.isBuildable())
            {
                return false;
            }
        }
        Tile t;
        try
        {
            t = getTile(buildingImage.getDoorPosition().moveBy(buildPoint));
        }
        catch(final OutOfMapException e)
        {
            return false;
        }
        return t.isReachable();
    }
    /**
     * Sprawia, ze teren pod budynkiem staje sie nie do przejscia, i nie do
     * zbudowania
     * 
     * @param bounds prostokat
     */
    public void makeUnreachable(final ConstRect bounds)
    {
        final List<Tile> tiles = getTiles(bounds.expandFromCenter(3));
        for(final Tile t : tiles)
        {
            t.setBuildable(false);
        }
        for(final Tile t : getTiles(bounds))
        {
            t.setReachable(false);
        }
    }
    /**
     * Teren pod prostokatem ponownie staje sie budowalny.
     * 
     * @param bounds prostokat
     */
    public void makeBuildableTerrain(final ConstRect bounds)
    {
        final ConstRect rect = bounds.expandFromCenter(1);
        final List<Tile> tiles = getTiles(rect);
        for(final Tile t : tiles)
        {
            t.setBuildable(true);
        }
    }
    /**
     * Round to tile.
     * 
     * @param position the position
     * @return the coords
     */
    public static Coords roundToTile(final Coords position)
    {
        return new Coords(Math.floor(position.getX() / Tile.SIZE) * Tile.SIZE, Math.floor(position
                .getY()
            / Tile.SIZE)
            * Tile.SIZE);
    }
    /**
     * Correct out of map position.
     * 
     * @param end the end
     * @return the coords
     */
    public Coords correctOutOfMapPosition(final Coords end)
    {
        return new ConstRect(0, 0, pixdimX, pixdimY).bound(end);
    }
}
