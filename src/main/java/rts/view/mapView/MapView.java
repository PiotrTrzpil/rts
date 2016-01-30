package rts.view.mapView;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.misc.GameVector;
import rts.misc.Line;
import rts.misc.P;
import rts.model.map.PathNode;
import rts.model.map.Tile;
import rts.view.GameView;
import rts.view.ViewModel;
import rts.view.animation.EffectsPool;
import rts.view.clientModel.ObjectHolder;
import rts.view.input.KeyInputEvent;
import rts.view.input.MouseEventHandler;
import rts.view.input.MouseInputEvent;
import rts.view.panels.Minimap;

// TODO: Auto-generated Javadoc
/**
 * Obiekt MapView jest sluzy za okno gracza na swiat gry.
 */
public class MapView// extends MouseEventHandler// implements Runnable
{
    /** Szerokosc widoku. */
    public static final int dimX = GameView.DIMX - 140;// 500;
    /** Wysokosc widoku. */
    public static final int dimY = GameView.DIMY;// 480;
    /**
     * Rozszerzenie widoku uzywane przy wybraniu obszaru do narysowania
     * obiektow.
     */
    private static final int viewUpdateExpansion = 64;
    /** Klasa zarzadzajaca animacjami. */
    private final EffectsPool effects;
    /** Paczka obiektor do narysowania. */
    private DrawPack drawPack;
    /** Aktualny tryb kontroli gracza. */
    private ControlModeAbstract currControl;
    /** Panel dla swinga. */
    private final ViewPanel viewPanel;
    /** The map enviroment. */
    private final ViewModel viewModel;
    /** Minimapa. */
    private final Minimap minimap;
    /** Widocznosc paskow zdrowia. */
    private boolean hpVisible;
    /** Prostokat reprezentujacy widok wzgledem mapy. */
    private ConstRect onMapBounds;
    /** Prostokat reprezentujacy wymiary mapy */
    private final ConstRect mapBounds;

    /**
     * Tworzy nowy obiekt MapView.
     * 
     * @param viewModel the map enviroment
     */
    public MapView(final ViewModel viewModel)
    {
        this.viewModel = viewModel;
        minimap = viewModel.getLeftPanel().getMinimap();
        viewPanel = new ViewPanel();
        effects = new EffectsPool(this);
        setControlMode(new ControlModeNormal());
        mapBounds = viewModel.getMap().getBounds();
        onMapBounds = new ConstRect(0, 0, dimX, dimY);
    }
    /**
     * Zbiera obiekty do narysowania i umnieszcza je w obiekcie DrawPack.
     */
    public void drawAll()
    {
        final ConstRect area = getDrawArea();
        final GameVector viewShift = getViewShift();
        final ObjectHolder objectHolder = viewModel.getObjectHolder();
        final List<Tile> tiles = viewModel.getMap().getTiles(area);
        final List<DrawableSprite> drawSprites = objectHolder.getSpritesToDraw(area);
        final DrawPack newPack = new DrawPack(drawSprites, tiles, viewShift);
        final Drawable selectLayer = objectHolder.getSelections();
        newPack.addLayer(objectHolder.getObjectUnderCursorBounds());
        newPack.addLayer(selectLayer);
        final List<Drawable> pathNodes = getPathNodes();
        if(pathNodes != null)
        {
            newPack.addLayer(pathNodes);
        }
        final Drawable controlLayer = currControl.getObjectsToDraw(area);
        newPack.addLayer(controlLayer);
        if(hpVisible)
        {
            newPack.addLayer(objectHolder.getHealthBars(area));
        }
        newPack.addLayer(effects.getAnimations());
        newPack.addLayer(viewModel.getChat().getStringList());
        drawPack = newPack;
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                viewPanel.repaint();
                minimap.repaint();
            }
        });
    }
    /**
     * Pobiera graf sciezek do narysowania.
     * 
     * @return graf sciezek
     */
    private List<Drawable> getPathNodes()
    {
        final List<Drawable> list = new ArrayList<Drawable>();
        final Iterable<PathNode> pathNodes = viewModel.getObjectHolder().getPathNodes();
        if(pathNodes == null)
        {
            return null;
        }
        for(final PathNode node : pathNodes)
        {
            for(final PathNode nei : node.getNeighbors())
            {
                final Line line = new Line(node.getPosition(), nei.getPosition());
                list.add(new ColorShape(Color.white, line)
                {
                    @Override
                    public void draw(final GameGraphics g)
                    {
                        g.setColor(Color.white);
                        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        getShape().draw(g);
                        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                    }
                });
            }
            list.add(new ColorShape(Color.red, node.getPosition())
            {
                @Override
                public void draw(final GameGraphics g)
                {
                    g.setColor(getColor());
                    getShape().draw(g);
                    g.drawString(node.getX() + "," + node.getY(), +node.getX() + 2,
                        +node.getY() + 2);
                }
            });
        }
        return list;
        //
    }
    /**
     * Metoda zwraca obszar prostokatny, elementy pod ktorym powinny zostac
     * narysowane na ekran.
     * 
     * @return the drawable area
     */
    private ConstRect getDrawArea()
    {
        return onMapBounds.expandFromCenter(viewUpdateExpansion).cropBy(mapBounds);
        // return new DrawArea(area,
        // onMapBounds.getUpperLeft().vector().negate());
    }
    /**
     * Zwraca przesuniecie obiektow na ekranie spowodowane przesunieciem widoku
     * wzgledem lewego gornego rogu mapy.
     * 
     * @return wektor przesuniecia
     */
    public GameVector getViewShift()
    {
        return onMapBounds.getUpperLeft().vector().negate();
    }
    /**
     * Wysrodkowuje widok na zadanym punkcie we wspolrzednych mapy.
     * 
     * @param position punkt docelowy
     */
    public void centerOn(final Coords position)
    {
        final GameVector vector = position.vector().substract(onMapBounds.getCenter().vector());
        moveAmount((int) vector.getX(), (int) vector.getY());
    }
    /**
     * Przesuwa widok o zadana ilosc pikseli.
     * 
     * @param dx ilosc pikseli w osi x
     * @param dy ilosc pikseli w osi y
     */
    protected void moveAmount(final int dx, final int dy)
    {
        onMapBounds = onMapBounds.moveBy(dx, dy).boundBy(mapBounds);
        minimap.update(onMapBounds);
    }
    /**
     * Tworzy nowy punkt przeksztalcajac uklad wspolrzednych zadanego punktu z
     * widoku na mape.
     * 
     * @param viewPoint punkt we wspolrzednych widoku
     * @return nowy punkt po przeksztalceniu na wspolrzedne mapy
     */
    public Coords convToMap(final Coords viewPoint)
    {
        return viewPoint.moveBy(onMapBounds.getUpperLeft());
    }
    /**
     * Przeksztalca uklad wspolrzednych zadanego prostokata z widoku na mape.
     * 
     * @param rect prostokat do modyfikacji
     * @return przeksztalcony prostokat
     */
    public ConstRect convToMap(final ConstRect rect)
    {
        return rect.moveBy(onMapBounds.getUpperLeft());
    }
    /**
     * Tworzy nowy punkt przeksztalcajac uklad wspolrzednych zadanego punktu z
     * mapy na widok.
     * 
     * @param mapPoint punkt we wspolrzednych mapy
     * @return nowy punkt po przeksztalceniu na wspolrzedne widoku
     */
    public Coords convToView(final Coords mapPoint)
    {
        return mapPoint.moveBy(onMapBounds.getUpperLeft().negate());
        // return new Coords(mapPoint.getX() - posX, mapPoint.getY() - posY);
    }
    /**
     * Wyswietla zadana animacje na ekranie.
     * 
     * @param effect zadana animacja
     * @param where punkt wyswietlenia
     */
    public void addEffect(final EffectsPool.Effect effect, final Coords where)
    {
        effects.globalEffect(effect, where);
    }
    /**
     * Ustawia nowy tryb sterowania.
     * 
     * @param mode nowy tryb sterowania
     */
    public void setControlMode(final ControlModeAbstract mode)
    {
        if(currControl != null)
        {
            currControl.deactivate();
        }
        currControl = mode;
        currControl.activate(viewModel);
    }
    /**
     * Gets the panel.
     * 
     * @return the panel
     */
    public ViewPanel getViewPanel()
    {
        return viewPanel;
    }
    /**
     * Ustawia widocznosc paskow zdrowia.
     * 
     * @param b the new hp visible
     */
    public void setHpVisible(final boolean b)
    {
        hpVisible = b;
    }
    /**
     * Zwraca aktualny tryb sterowania.
     * 
     * @return the control
     */
    public ControlModeAbstract getControl()
    {
        return currControl;
    }

    /**
     * Klasa bedaca reprezentacja MapView w hierarchii swinga, oraz odbierajaca
     * zdarzenia wejscia.
     */
    public class ViewPanel extends MouseEventHandler
    {
        /**
         * Instantiates a new view panel.
         */
        public ViewPanel()
        {
            super(null);
            setBounds(new Rectangle(150, 0, 150 + dimX, dimY));
            setDoubleBuffered(true);
            setListener(viewModel.getInputListener());
        }
        /*
         * (non-Javadoc)
         *
         * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
         */
        @Override
        public void paintComponent(final Graphics graphics)
        {
            super.paintComponent(graphics);
            if(drawPack != null)
            {
                drawPack.draw(graphics);
            }
        }
        /*
         * (non-Javadoc)
         *
         * @see
         * rts.view.input.MouseEventHandler#handleMouseEvent(rts.view.input.
         * MouseInputEvent)
         */
        @Override
        public void handleMouseEvent(final MouseInputEvent event)
        {
            try
            {
                final Method met = currControl.getClass().getMethod("handleMouseEvent",
                    event.getClass());
                met.invoke(currControl, event);
            }
            catch(final InvocationTargetException e)
            {
                P.er("InvocationTargetException : ");
                e.getCause().printStackTrace();
            }
            catch(final Exception e)
            {
                e.printStackTrace();
            }
        }
        /**
         * Handle key event.
         * 
         * @param event the event
         */
        public void handleKeyEvent(final KeyInputEvent event)
        {
            currControl.handleKeyEvent(event);
        }
    }

    /**
     * Gets the effects pool.
     * 
     * @return the effects pool
     */
    public EffectsPool getEffectsPool()
    {
        return effects;
    }
}
