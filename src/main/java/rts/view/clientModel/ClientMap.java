package rts.view.clientModel;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.misc.TileDimensions;
import rts.misc.exceptions.OutOfMapException;
import rts.model.map.MapSettings;
import rts.model.map.Tile;
import rts.view.ImagesAndPatterns;

public class ClientMap
{
    private Tile tileMap[][];
    private int pixdimX;
    private int pixdimY;
    private int tiledimX;
    private int tiledimY;
    public static Image grass;
    public static Image testTile;

    public ClientMap(final MapSettings settings, final ImagesAndPatterns imageLoader)
    {
        grass = imageLoader.getTerrainSprite("Grass");
        testTile = imageLoader.getTerrainSprite("Test");
        loadDefault(settings.getDimensions());
    }
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
                tileMap[i][j] = new Tile(j, i, grass);
            }
        }
    }
    public Tile getTile(final Coords p) throws OutOfMapException
    {
        Tile tile = null;
        try
        {
            tile = tileMap[p.getY() >> Tile.SIZE_LOG2][p.getX() >> Tile.SIZE_LOG2];
        }
        catch(final NullPointerException e)
        {
            throw new OutOfMapException(p.toString());
        }
        return tile;
    }
    public List<Tile> getTiles(final ConstRect rect)
    {
        final List<Tile> list = new ArrayList<Tile>();
        // Tile s = tileMap[rect.y>>Tile.SIZE_LOG2][rect.x>>Tile.SIZE_LOG2];
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
    public ConstRect getBounds()
    {
        return new ConstRect(0, 0, pixdimX, pixdimY);
    }
}
