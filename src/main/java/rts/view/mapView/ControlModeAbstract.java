package rts.view.mapView;

import java.awt.event.KeyEvent;
import rts.controller.Command;
import rts.misc.ConstRect;
import rts.view.ViewModel;
import rts.view.input.KeyInputEvent;
import rts.view.input.MouseButton;
import rts.view.input.MouseButtonEvent;
import rts.view.input.MouseMovedEvent;

// TODO: Auto-generated Javadoc
/**
 * Abstrakcyjny tryb kontroli po ktorym dziedzicza wszystkie inne. Tryby
 * kontroli sluza do roznorodnego sterowania gra przez uzytkownika
 */
public abstract class ControlModeAbstract
{
    /** Czy przesuwanie okna gry jest aktywne. */
    private boolean moveViewPressed;
    /** The map enviroment. */
    protected ViewModel viewModel;

    /**
     * Przekazuje referencje do MapEnviroment. Ten parametr nie jest
     * przekazywany w kontruktorze, aby mozna bylo latwiej zmieniac tryb
     * sterowania z dowolnego miejsca.
     * 
     * @param mapEnviroment the map enviroment
     */
    protected void activate(final ViewModel mapEnviroment)
    {
        viewModel = mapEnviroment;
    }
    /**
     * Pobiera obiekty do narysowania zwiazane z tym trybem kontroli
     * 
     * @param visibleArea the visible area
     * @return the objects to draw
     */
    public Drawable getObjectsToDraw(final ConstRect visibleArea)
    {
        return new DrawableNull();
    }
    /**
     * Obsluga poruszania i przeciagania mysza
     * 
     * @param event zdarzenie poruszenia lub przeciagniecia mysza
     */
    public void handleMouseEvent(final MouseMovedEvent event)
    {
        if(moveViewPressed)
        {
            viewModel.getQueue().add(new Command()
            {
                @Override
                public void execute()
                {
                    viewModel.getMapView().moveAmount(-event.getMoveDistanceX(),
                        -event.getMoveDistanceY());
                }
            });
        }
    }
    /**
     * Obsluga przyciskow myszy
     * 
     * @param event zdarzenie przycisku muyszy
     */
    public void handleMouseEvent(final MouseButtonEvent event)
    {
        if(event.wasPressed(MouseButton.CMB))
        {
            moveViewPressed = true;
        }
        else if(event.wasReleased(MouseButton.CMB))
        {
            moveViewPressed = false;
        }
    }
    /**
     * Obsluga przyciskow klawiatury
     * 
     * @param event the event
     */
    public void handleKeyEvent(final KeyInputEvent event)
    {
        if(event.wasPressed(KeyEvent.VK_ENTER))
        {
            viewModel.getChat().activate();
        }
    }
    public void deactivate()
    {
    }
}