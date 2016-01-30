package rts.view.clientModel.misc;

import rts.view.mapView.Drawable;
import rts.view.panels.InfoTab;

// TODO: Auto-generated Javadoc
/**
 * Interfejs dla rzeczy mogacych byc zaznaczane.
 */
public interface Selectable
{
    /**
     * Gets the info tab.
     * 
     * @return the info tab
     */
    public InfoTab getInfoTab();
    /**
     * Sets the info tab.
     * 
     * @param info the new info tab
     */
    public void setInfoTab(InfoTab info);
    /**
     * Zwraca reprezentacje graficzna zaznaczenia
     * 
     * @return the selection
     */
    public Drawable getSelection();
}
