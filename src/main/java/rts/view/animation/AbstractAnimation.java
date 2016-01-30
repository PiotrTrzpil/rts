package rts.view.animation;

import java.util.Timer;
import rts.misc.Coords;
import rts.view.mapView.DrawableImage;

// TODO: Auto-generated Javadoc
/**
 * Ogolny interfejs animacji
 */
public interface AbstractAnimation
{
    /**
     * Czy animacja sie zakonczyla
     * 
     * @return true, if successful
     */
    public boolean hasEnded();
    /**
     * Pobiera aktualny obrazek
     * 
     * @return the drawable image
     */
    public DrawableImage getDrawableImage();
    /**
     * Pobiera aktualny obrazek umieszczajac go w danym polozeniu
     * 
     * @param point the point
     * @return the drawable image
     */
    public DrawableImage getDrawableImage(Coords point);
    /**
     * Uruchamia animacje
     * 
     * @param effectTimer the effect timer
     */
    public void scheduleOn(final Timer effectTimer);
    /**
     * Zatrzymuje animacje
     */
    public void stop();
}
