package misc;

//import input.MouseLink;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import javax.swing.ImageIcon;

public class AnimCursor implements MouseListener
{

    public static Image nullImage;
    public static Image defImage;

    private Image current;
    // private Image def;
    // private Image pressed;
    // private Image rollOver;
    // private final MouseLink mouse;

    // private Image [] shapeTable;
    // p//rivate String [] nameTable;

    private HashMap<String, Image> shapeMap;

    private Point staticPos;

    static
    {
        try
        {
            nullImage = new ImageIcon("images/nullImage.png").getImage();
            defImage = new ImageIcon("images/defImage.png").getImage();
        }
        catch(final RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    public AnimCursor(final String[] curTable)
    {

        // this.mouse = mouse;//mouse;
        loadShapes(curTable);
        current = defImage;
    }

    private void loadShapes(final String[] curTable)
    {
        try
        {
            shapeMap = new HashMap<String, Image>();
            for(final String s : curTable)
            {
                shapeMap.put(s, new ImageIcon("images/" + s + ".png").getImage());
                // shapeTable[i] = new
                // ImageIcon("images/"+curTable[i]+".png").getImage();
            }

            // shapeTable = new Image [curTable.length];
            // for(int i=0; i<curTable.length; i++ )
            // {
            // shapeTable[i] = new
            // ImageIcon("images/"+curTable[i]+".png").getImage();
            // }
            // nameTable = curTable;
        }
        catch(final RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    // public void draw(Graphics2D g2d)
    // {
    // if(current != null && mouse != null && mouse.getPos() != null)
    // g2d.drawImage(current, mouse.getPos().x,mouse.getPos().y, null);
    //
    // if(current == null) System.out.println("curr");
    // if(mouse == null) System.out.println("mouse");
    // if(mouse != null && mouse.getPos() == null )
    // System.out.println("mousePos");
    // }
    public void draw(final Graphics2D g2d, Point p)
    {
        // if(current != null)
        // {
        if(staticPos != null)
        {
            p = staticPos;
        }

        g2d.drawImage(current, p.x, p.y, null);
        // }

    }

    // public void changeTo(int index)
    // {
    // current = shapeTable[index];
    // }

    public void changeTo(final String name)
    {
        current = shapeMap.get(name);
    }

    public void mouseClicked(final MouseEvent e)
    {

    }

    public void mousePressed(final MouseEvent e)
    {
        // current = shapeTable[1];
    }

    public void mouseReleased(final MouseEvent e)
    {
        // current = shapeTable[0];
    }

    public void mouseEntered(final MouseEvent e)
    {
        // current = shapeTable[2];
    }

    public void mouseExited(final MouseEvent e)
    {
        // current = shapeTable[0];
    }

    public void setStatic(final int x, final int y, final Component requester)
    {
        if(staticPos == null)
        {
            staticPos = new Point(x, y);

            // staticPos.x += MapEnviroment.MAP_VIEW.x;
            // staticPos.y += MapEnviroment.MAP_VIEW.y;

            //     staticPos = SwingUtilities.convertPoint(requester, staticPos, Res.gamePanel);// .convertPointToScreen(staticPos,
            // requester);
            // int i =0;
        }
    }

    public void setDynamic()
    {
        staticPos = null;
    }

    public boolean isStatic()
    {
        return staticPos != null;
    }

    // public void setDefault(String string)
    // {
    // def = new ImageIcon(string).getImage();
    // current = def;
    // }
    //
    // public void setPressed(String string)
    // {
    // pressed = new ImageIcon(string).getImage();
    // }
    // public void setRollOver(String string)
    // {
    // rollOver = new ImageIcon(string).getImage();
    // }

}
