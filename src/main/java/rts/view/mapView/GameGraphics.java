package rts.view.mapView;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.util.Map;
import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.misc.GameVector;
import rts.misc.Line;
import rts.misc.Oval;

// TODO: Auto-generated Javadoc
/**
 * Klasa opakowujaca klase Graphics w celu latwiejszego uzywania do rysowania
 * obiektow swiata gry.
 */
public class GameGraphics
{
    /** Graphics2D. */
    private final Graphics2D g2d;
    /** Przesuniecie obiektow na ekranie w poziomie. */
    private final int drawX;
    /** Przesuniecie obiektow na ekranie w pionie. */
    private final int drawY;
    private boolean filled;
    private boolean onMap;

    /**
     * Instantiates a new game graphics.
     * 
     * @param g obiekt Graphics
     * @param drawShift Przesuniecie obiektow na ekranie
     */
    public GameGraphics(final Graphics g, final GameVector drawShift)
    {
        drawX = (int) drawShift.getX();
        drawY = (int) drawShift.getY();
        g2d = (Graphics2D) g;
        filled = false;
        onMap = true;
        // gonMapz2d.drawImage(null, drawX, drawY, null);
    }
    /**
     * Instantiates a new game graphics.
     * 
     * @param g the g
     */
    public GameGraphics(final Graphics g)
    {
        drawX = 0;
        drawY = 0;
        g2d = (Graphics2D) g;
        filled = false;
        onMap = true;
        // g2d.drawImage(null, drawX, drawY, null);
    }
    // /**
    // * Draw image.
    // *
    // * @param img the img
    // * @param x the x
    // * @param y the y
    // * @return true, if successful
    // */
    // public boolean drawImage(final Image img, final int x, final int y)
    // {
    // return g2d.drawImage(img, x + drawX, y + drawY, null);
    // }
    /**
     * Draw image.
     * 
     * @param image the image
     * @param position the position
     * @return true, if successful
     */
    public boolean drawImage(final Image image, final Coords position)
    {
        final int shiftX = onMap ? drawX : 0;
        final int shiftY = onMap ? drawY : 0;
        return g2d.drawImage(image, position.getX() + shiftX, position.getY() + shiftY, null);
    }
    /**
     * Draw line.
     * 
     * @param line the line
     */
    public void drawLine(final Line line)
    {
        final int shiftX = onMap ? drawX : 0;
        final int shiftY = onMap ? drawY : 0;
        g2d.drawLine(line.getA().getX() + shiftX, line.getA().getY() + shiftX, line.getB().getX()
            + shiftX, line.getB().getY() + shiftY);
    }
    // /**
    // * Draw oval.
    // *
    // * @param position the position
    // * @param width the width
    // * @param height the height
    // */
    // public void drawOval(final Coords position, final int width, final int
    // height)
    // {
    // g2d.drawOval(position.getX() + drawX, position.getY() + drawY, width,
    // height);
    // }
    /**
     * Draw oval.
     * 
     * @param oval the oval
     */
    public void drawOval(final Oval oval)
    {
        final int shiftX = onMap ? drawX : 0;
        final int shiftY = onMap ? drawY : 0;
        final Coords point = oval.getCenter().moveBy(-oval.getWidth() / 2, -oval.getHeight() / 2);
        if(filled)
        {
            g2d.fillOval(point.getX() + shiftX, point.getY() + shiftY, oval.getWidth(), oval
                    .getHeight());
        }
        else
        {
            g2d.drawOval(point.getX() + shiftX, point.getY() + shiftY, oval.getWidth(), oval
                    .getHeight());
        }
    }
    // /**
    // * Fill oval.
    // *
    // * @param oval the oval
    // */
    // public void fillOval(final Oval oval)
    // {
    // final Coords point = oval.getCenter().moveBy(-oval.getWidth() / 2,
    // -oval.getHeight() / 2);
    // g2d.fillOval(point.getX() + drawX, point.getY() + drawY, oval.getWidth(),
    // oval.getHeight());
    // }
    /**
     * Draw rect.
     * 
     * @param rect the rect
     */
    public void drawRect(final ConstRect rect)
    {
        final int shiftX = onMap ? drawX : 0;
        final int shiftY = onMap ? drawY : 0;
        if(filled)
        {
            g2d.fillRect(rect.getX() + shiftX, rect.getY() + shiftY, rect.getWidth(), rect
                    .getHeight());
        }
        else
        {
            g2d.drawRect(rect.getX() + shiftX, rect.getY() + shiftY, rect.getWidth(), rect
                    .getHeight());
        }
    }
    // /**
    // * Fill rect.
    // *
    // * @param rect the rect
    // */
    // public void fillRect(final ConstRect rect)
    // {
    // g2d.fillRect(rect.getX() + drawX, rect.getY() + drawY, rect.getWidth(),
    // rect.getHeight());
    // }
    /**
     * Draw string.
     * 
     * @param str the str
     * @param x the x
     * @param y the y
     */
    public void drawString(final String str, final int x, final int y)
    {
        final int shiftX = onMap ? drawX : 0;
        final int shiftY = onMap ? drawY : 0;
        g2d.drawString(str, x + shiftX, y + shiftY);
    }
    // /**
    // * Draw line.
    // *
    // * @param x1 the x1
    // * @param y1 the y1
    // * @param x2 the x2
    // * @param y2 the y2
    // */
    // public void drawLine(final int x1, final int y1, final int x2, final int
    // y2)
    // {
    //
    // g2d.drawLine(x1 + drawX, y1 + drawY, x2 + drawX, y2 + drawY);
    // }
    // /**
    // * Fill rect.
    // *
    // * @param x the x
    // * @param y the y
    // * @param width the width
    // * @param height the height
    // */
    // public void fillRect(final int x, final int y, final int width, final int
    // height)
    // {
    // g2d.fillRect(x + drawX, y + drawY, width, height);
    // }
    // /**
    // * Draw rect.
    // *
    // * @param x the x
    // * @param y the y
    // * @param width the width
    // * @param height the height
    // */
    // public void drawRect(final int x, final int y, final int width, final int
    // height)
    // {
    // g2d.drawRect(x + drawX, y + drawY, width, height);
    // }
    // /**
    // * Fill oval.
    // *
    // * @param x the x
    // * @param y the y
    // * @param width the width
    // * @param height the height
    // */
    // public void fillOval(final int x, final int y, final int width, final int
    // height)
    // {
    // g2d.fillOval(x + drawX, y + drawY, width, height);
    // }
    /**
     * Sets the color.
     * 
     * @param c the new color
     */
    public void setColor(final Color c)
    {
        g2d.setColor(c);
    }
    /**
     * Sets the composite.
     * 
     * @param comp the new composite
     */
    public void setComposite(final Composite comp)
    {
        g2d.setComposite(comp);
    }
    /**
     * Sets the font.
     * 
     * @param font the new font
     */
    public void setFont(final Font font)
    {
        g2d.setFont(font);
    }
    /**
     * Sets the rendering hints.
     * 
     * @param hints the hints
     */
    public void setRenderingHints(final Map<?, ?> hints)
    {
        g2d.setRenderingHints(hints);
    }
    /**
     * Sets the rendering hint.
     * 
     * @param keyAntialiasing the key antialiasing
     * @param valueAntialiasOn the value antialias on
     */
    public void setRenderingHint(final Key keyAntialiasing, final Object valueAntialiasOn)
    {
        g2d.setRenderingHint(keyAntialiasing, valueAntialiasOn);
    }
    /**
     * Sets the antialiased.
     */
    public void setAntialiased()
    {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
    /**
     * Sets the translucent.
     * 
     * @param amount the new translucent
     */
    public void setTranslucent(float amount)
    {
        if(amount < 0)
        {
            amount = 0f;
        }
        else if(amount > 1)
        {
            amount = 1f;
        }
        AlphaComposite instance = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, amount);
        if(instance == null)
        {
            instance = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
        }
        g2d.setComposite(instance);
    }
    public void setFilled(final boolean bool)
    {
        filled = bool;
    }
    public void setToMapRelation(final boolean bool)
    {
        onMap = bool;
    }
    /**
     * Gets the graphics.
     * 
     * @return the graphics
     */
    public Graphics2D getGraphics()
    {
        return g2d;
    }
    /**
     * Dispose.
     */
    public void dispose()
    {
        g2d.dispose();
    }
}
