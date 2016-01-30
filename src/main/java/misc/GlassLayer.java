package misc;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GlassLayer extends JPanel implements AWTEventListener
{
    private final JFrame frame;
    private Point point = new Point();
    private AnimCursor current;

    public GlassLayer(final JFrame frame)
    {
        super(null);
        this.frame = frame;
        setOpaque(false);

        //        EventQueue queue = new EventQueue()
        //        {
        //            @Override
        //            protected void dispatchEvent(AWTEvent event)
        //            {
        //                eventDispatched(event);
        //                super.dispatchEvent(event);
        //            }
        //        };
        //
        //        Toolkit.getDefaultToolkit().getSystemEventQueue().push(queue);

    }

    public void setPoint(final int x, final int y, final Component requester)
    {

        point = new Point(x, y);
        point = SwingUtilities.convertPoint(requester, point, this);
        repaint();
    }

    public void set(final AnimCursor cur)
    {
        current = cur;
    }

    @Override
    protected void paintComponent(final Graphics g)
    {
        final Graphics2D g2d = (Graphics2D) g;

        //  g2d.setColor(Color.red);
        //   g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        //     g2d.fillRect(0, 0, 100, 100);
        //  if(MainGamePanel.shinyCursorOn && point != null)
        current.draw(g2d, point);

        //        g2d.setColor(Color.GREEN.darker());
        //        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        //        int d = 22;
        //        g2d.fillRect(getWidth() - d, 0, d, d);
        //        if (point != null) {
        //            g2d.fillOval(point.x + d, point.y + d, d, d);
        //        }
        g2d.dispose();
    }

    public void eventDispatched(final AWTEvent event)
    {
        if(event instanceof MouseEvent)
        {
            final MouseEvent me = (MouseEvent) event;
            if(!SwingUtilities.isDescendingFrom(me.getComponent(), frame))
            {
                return;
                //  int i=0;
                //    if (me.getID() == MouseEvent.MOUSE_EXITED && me.getComponent() == frame)
                //     {
                //        point = null;
                //   }
            }
            else
            {
                final MouseEvent converted = SwingUtilities.convertMouseEvent(me.getComponent(),
                    me, frame.getGlassPane());
                point = converted.getPoint();

                //                point = me.getPoint();
                //                SwingUtilities.convertPoint(me.getComponent(), point, frame.getGlassPane());

            }
            repaint();
        }
    }

    @Override
    public boolean contains(final int x, final int y)
    {
        if(getMouseListeners().length == 0 && getMouseMotionListeners().length == 0
            && getMouseWheelListeners().length == 0
            && getCursor() == Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
        {
            return false;
        }
        return super.contains(x, y);
    }

}

//
//
//        extends JComponent
//{
//
//    private AnimCursor current;
//
//
//
//
//
//    public GlassLayer(Container contentPane)
//    {
//        CBListener listener = new CBListener(this, contentPane);
//        addMouseListener(listener);
//        addMouseMotionListener(listener);
//        setOpaque(false);
//
//        Toolkit toolkit = Toolkit.getDefaultToolkit();
//        Cursor cursor = toolkit.createCustomCursor(AnimCursor.nullImage, new Point(0,0), "def");
//        setCursor(cursor);
//    }
//
//    @Override
//    protected void paintComponent(Graphics g)
//    {
//        super.paintComponent(g);
//        Graphics2D g2d = (Graphics2D) g;
//
//        g2d.setColor(Color.red);
//        g2d.fillRect(0, 0, 100, 100);
//        current.draw(g2d);
//    }
//
//    public void set(AnimCursor cur)
//    {
//        current = cur;
//    }
//
//
//
//
//
//
//
//
//
//}
//
//
//
//class CBListener implements MouseMotionListener, MouseListener
//{
//
//    Toolkit toolkit;
//
//    GlassLayer glassPane;
//    Container contentPane;
//
//    public CBListener(GlassLayer glassPane, Container contentPane)
//    {
//        toolkit = Toolkit.getDefaultToolkit();
//
//        this.glassPane = glassPane;
//        this.contentPane = contentPane;
//    }
//
//    public void mouseMoved(MouseEvent e)
//    {
//        redispatchMouseEvent(e, false);
//    }
//
//    public void mouseDragged(MouseEvent e)
//    {
//        redispatchMouseEvent(e, false);
//    }
//
//    public void mouseClicked(MouseEvent e)
//    {
//        redispatchMouseEvent(e, false);
//    }
//
//    public void mouseEntered(MouseEvent e)
//    {
//        redispatchMouseEvent(e, false);
//    }
//
//    public void mouseExited(MouseEvent e)
//    {
//        redispatchMouseEvent(e, false);
//    }
//
//    public void mousePressed(MouseEvent e)
//    {
//        redispatchMouseEvent(e, false);
//    }
//
//    public void mouseReleased(MouseEvent e)
//    {
//        redispatchMouseEvent(e, true);
//    }
//
//    //A basic implementation of redispatching events.
//    private void redispatchMouseEvent(MouseEvent e, boolean repaint)
//    {
//    //    System.out.println("RED");
//        Point glassPanePoint = e.getPoint();
//      //  Container container = contentPane;
//        Point containerPoint = SwingUtilities.convertPoint(
//                glassPane,
//                glassPanePoint,
//                contentPane);
//        if (containerPoint.y < 0)
//        { //we're not in the content pane
//
//        }
//        else
//        {
//            //The mouse event is probably over the content pane.
//            //Find out exactly which component it's over.
//            Component component =
//                    SwingUtilities.getDeepestComponentAt(
//                    contentPane,
//                    containerPoint.x,
//                    containerPoint.y);
//
//
//                //Forward events over the check box.
//                Point componentPoint = SwingUtilities.convertPoint(
//                        glassPane,
//                        glassPanePoint,
//                        component);
//                component.dispatchEvent(new MouseEvent(component,
//                        e.getID(),
//                        e.getWhen(),
//                        e.getModifiers(),
//                        componentPoint.x,
//                        componentPoint.y,
//                        e.getClickCount(),
//                        e.isPopupTrigger()));
//
//        }
//
//        //Update the glass pane if requested.
//        if (repaint)
//        {
//        //    glassPane.setPoint(glassPanePoint);
//            glassPane.repaint();
//        }
//    }
//}