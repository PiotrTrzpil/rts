package rts.view.mapView;

import java.awt.Color;
import rts.controller.Command;
import rts.controller.netEvents.toServerEvents.ToServerCreateBuilding;
import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.misc.GameVector;
import rts.misc.Size;
import rts.misc.exceptions.OutOfMapException;
import rts.model.BuildingType;
import rts.model.map.Tile;
import rts.view.clientModel.BuildingImage;
import rts.view.input.MouseButton;
import rts.view.input.MouseButtonEvent;
import rts.view.input.MouseMovedEvent;

/**
 * Tryb kontroli budowaniem budynkow
 */
public class ControlModeBuild extends ControlModeAbstract
{
    /** Typ budynku. */
    private final BuildingType buildingType;
    /** Obrazek budynku. */
    private final BuildingImage sprite;
    /** The mouse point. */
    private Coords mousePoint;
    /** Przesuniecie budynku wzgledem myszy. */
    private static final GameVector shift = new GameVector(-20, -20);

    /**
     * Instantiates a new control mode build.
     * 
     * @param type typ budynku
     * @param sprite obrazek budynku
     */
    public ControlModeBuild(final BuildingType type, final BuildingImage sprite)
    {
        buildingType = type;
        this.sprite = sprite;
    }
    /*
     * (non-Javadoc)
     * 
     * @see rts.view.mapView.ControlModeAbstract#getObjectsToDraw(rts.misc.Rect)
     */
    @Override
    public Drawable getObjectsToDraw(final ConstRect visibleArea)
    {
        final Coords mousePos = mousePoint;
        final DrawableList drawableObjects = new DrawableList();
        if(mousePos == null)
        {
            return drawableObjects;
        }
        final Coords buildP = convToBuildPoint(mousePos);
        if(buildingIsOnMap(buildP))
        {
            try
            {
                final Tile doorTile = viewModel.getMap().getTile(sprite.getDoorPosition());
                final Color c;
                c = Color.green;
                // final Coords doorDrawPoint = doorTile.getPosition();
                final Coords spriteDrawPoint = buildP.moveBy(sprite.getDrawShift());
                drawableObjects
                        .add(new ColorShape(c, new ConstRect(buildP, sprite.getDimensions())));
                drawableObjects.add(new ColorShape(c, new ConstRect(doorTile.getPosition().moveBy(
                    buildP), new Size(Tile.SIZE))));
                drawableObjects.add(new DrawableImage(sprite.getImage(), spriteDrawPoint));
            }
            catch(final OutOfMapException e)
            {
                e.printStackTrace();
            }
        }
        return drawableObjects;
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * rts.view.mapView.ControlModeAbstract#handleMouseEvent(rts.view.input.
     * MouseMovedEvent)
     */
    @Override
    public void handleMouseEvent(final MouseMovedEvent event)
    {
        super.handleMouseEvent(event);
        final Coords mousePos = event.getLocation();
        mousePoint = mousePos;
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * rts.view.mapView.ControlModeAbstract#handleMouseEvent(rts.view.input.
     * MouseButtonEvent)
     */
    @Override
    public void handleMouseEvent(final MouseButtonEvent event)
    {
        super.handleMouseEvent(event);
        if(event.wasPressed(MouseButton.LMB))
        {
            build(event.getLocation());
            exitBuildMode();
            return;
        }
        else if(event.wasPressed(MouseButton.RMB))
        {
            exitBuildMode();
        }
    }
    /**
     * Exit build mode.
     */
    private void exitBuildMode()
    {
        viewModel.getMapView().setControlMode(new ControlModeNormal());
    }
    /**
     * Przeksztalca punkt myszy na lewy gorny rog budynku majacego znalesc sie
     * na mapie.
     * 
     * @param point punkt pod wskaznikiem myszy
     * @return punkt we wspolrzednych mapy
     */
    private Coords convToBuildPoint(final Coords point)
    {
        Coords coords = viewModel.getMapView().convToMap(point.moveBy(shift));
        coords = coords.moveBy(-coords.getX() % Tile.SIZE, -coords.getY() % Tile.SIZE);
        return coords;
    }
    /**
     * Wysyla chec zbudowania budynku do serwera
     * 
     * @param position miejsce budynku
     */
    public void build(final Coords position)
    {
        try
        {
            viewModel.getQueue().add(new Command()
            {
                @Override
                public void execute()
                {
                    final Coords buildPoint = convToBuildPoint(position);
                    viewModel.send(new ToServerCreateBuilding(buildingType, buildPoint));
                }
            });
            // view.setControlMode(new ControlModeNormal());
        }
        catch(final Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Sprawdza czy budynek znajduje sie na mapie
     * 
     * @param buildP the build p
     * @return true, if successful
     */
    private boolean buildingIsOnMap(final Coords buildP)
    {
        final Size dimensions = sprite.getDimensions();
        final ConstRect building = new ConstRect(buildP, dimensions.setDimY(dimensions.getDimY()
            + Tile.SIZE));
        return viewModel.getMap().getBounds().contains(building);
    }
}
