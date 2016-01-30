package rts.view.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import rts.controller.Command;
import rts.controller.GameEventQueue;
import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.view.ViewModel;
import rts.view.input.MouseButtonEvent;
import rts.view.input.MouseEventHandler;
import rts.view.input.MouseMovedEvent;

/**
 * Minimapa przedstawiajaca ogolny, zmniejszony obraz mapy w rogu ekranu.
 */
public class Minimap extends MouseEventHandler
{
    /** Maksymalne wymiary minimapy. */
    private final ConstRect BOUNDS;
    /** The buildings. */
    private final ArrayList<ConstRect> buildings;
    /** The map enviroment. */
    private final ViewModel mapEnviroment;
    /** The queue. */
    private final GameEventQueue queue;
    /** Ramka reprezentujaca aktualna pozycje widoku na mape. */
    private ConstRect frame;
    /** Miniobraz mapy. */
    private final ConstRect mapImage;
    /** Skala minimapy. */
    private final double scale;

    /**
     * Instantiates a new minimap.
     * 
     * @param mapEnviroment the map enviroment
     */
    public Minimap(final ViewModel mapEnviroment)
    {
        this.mapEnviroment = mapEnviroment;
        BOUNDS = new ConstRect(0, 0, LeftPanel.minimapBounds.getWidth(), LeftPanel.minimapBounds
                .getHeight());
        buildings = new ArrayList<ConstRect>();
        queue = mapEnviroment.getQueue();
        final ConstRect mapBounds = mapEnviroment.getMap().getBounds();
        mapImage = mapBounds.scaleToFitIn(BOUNDS).centerIn(BOUNDS);
        scale = (double) mapImage.getWidth() / mapBounds.getWidth();
    }
    /**
     * Ustawia widok na okreslonej pozycji
     * 
     * @param destination okreslona pozycja
     */
    private void setView(final Coords destination)
    {
        mapEnviroment.getMapView().centerOn(convToMap(destination));
    }
    /**
     * Przeksztalca punkt z minimapy na punkt na mapie.
     * 
     * @param point punkt minimapy
     * @return punkt na mapie
     */
    private Coords convToMap(final Coords point)
    {
        return point.moveBy(mapImage.getUpperLeft().negate()).multiplyBy(1 / scale);
    }
    /**
     * Aktualizuje pozycje ramki widoku.
     * 
     * @param viewBounds wymiary i pozycja widoku na mapie
     */
    public void update(final ConstRect viewBounds)
    {
        frame = convToMinimap(viewBounds);
    }
    /**
     * Przeksztalca prostokat we wspolrzednych mapy na prostokat na minimapie.
     * 
     * @param rect prostokat we wspolrzednych mapy
     * @return prostokat na minimapie
     */
    private ConstRect convToMinimap(final ConstRect rect)
    {
        return rect.multiplyBy(scale).expandSize(-1);
    }
    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(final Graphics g)
    {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        drawTerrain(g2d);
        drawBuildings(g2d);
        drawViewFrame(g2d);
    }
    /**
     * Draw terrain.
     * 
     * @param g the g
     */
    private void drawTerrain(final Graphics2D g)
    {
        g.setColor(Color.blue);
        g.fillRect(0, 0, BOUNDS.getWidth(), BOUNDS.getHeight());
        g.setColor(Color.green);
        g.fillRect(mapImage.getX(), mapImage.getY(), mapImage.getWidth(), mapImage.getHeight());
    }
    /**
     * Draw buildings.
     * 
     * @param g the g
     */
    private void drawBuildings(final Graphics2D g)
    {
        g.setColor(Color.WHITE);
        for(final ConstRect r : buildings)
        {
            g.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        }
    }
    /**
     * Draw view frame.
     * 
     * @param g the g
     */
    private void drawViewFrame(final Graphics2D g)
    {
        g.setColor(Color.black);
        g.drawRect(mapImage.getX() + frame.getX(), mapImage.getY() + frame.getY(),
            frame.getWidth(), frame.getHeight());
    }
    /* (non-Javadoc)
     * @see rts.view.input.MouseEventHandler#handleMouseEvent(rts.view.input.MouseMovedEvent)
     */
    @Override
    protected void handleMouseEvent(final MouseMovedEvent event)
    {
        if(event.isMouseDragged())
        {
            queue.add(new Command()
            {
                @Override
                public void execute()
                {
                    setView(event.getLocation());
                }
            });
        }
    };
    /* (non-Javadoc)
     * @see rts.view.input.MouseEventHandler#handleMouseEvent(rts.view.input.MouseButtonEvent)
     */
    @Override
    protected void handleMouseEvent(final MouseButtonEvent event)
    {
        queue.add(new Command()
        {
            @Override
            public void execute()
            {
                setView(event.getLocation());
            }
        });
    };
    /**
     * Adds the object.
     * 
     * @param objectBounds the object bounds
     */
    public void addObject(ConstRect objectBounds)
    {
        objectBounds = convToMinimap(objectBounds);
        buildings.add(objectBounds);
    }
    /**
     * Removes the object.
     * 
     * @param objectBounds the object bounds
     */
    public void removeObject(ConstRect objectBounds)
    {
        objectBounds = convToMinimap(objectBounds);
        buildings.remove(objectBounds);
    }
}
