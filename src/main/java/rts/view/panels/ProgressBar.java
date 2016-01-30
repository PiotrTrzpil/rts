package rts.view.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.TimerTask;
import javax.swing.JPanel;
import rts.controller.Timers;

// TODO: Auto-generated Javadoc
/**
 * Pasek postepu.
 */
public class ProgressBar extends JPanel
{
    /** The percentage. */
    private float percentage;
    /** The total time ms. */
    private float totalTimeMs;
    /** The rate ms. */
    private int rateMs;
    /** The update task. */
    private UpdateTask updateTask;

    /**
     * Instantiates a new progress bar.
     * 
     * @param time the time
     * @param rate the rate
     */
    public ProgressBar(final float time, final int rate)
    {
        reset(time, rate);
    }
    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g)
    {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        final Rectangle bounds = this.getBounds();
        g2d.setColor(Color.red);
        final int width = (int) (bounds.width * (percentage / 100));
        g2d.fillRect(0, 0, width, bounds.height);
    }
    /**
     * Reset.
     * 
     * @param timeMs the time ms
     * @param rateMs the rate ms
     */
    void reset(final float timeMs, final int rateMs)
    {
        totalTimeMs = timeMs;
        this.rateMs = rateMs;
        percentage = 0;
    }
    /**
     * Start.
     */
    void start()
    {
        updateTask = new UpdateTask();
        Timers.animTimer().scheduleAtFixedRate(updateTask, 0, rateMs);
        //    P.pr(System.currentTimeMillis());
    }
    /**
     * Stop.
     */
    void stop()
    {
        if(updateTask != null)
        {
            updateTask.cancel();
        }
    }
    /**
     * Cancel.
     */
    void cancel()
    {
        stop();
        percentage = 0;
        ///    P.pr(System.currentTimeMillis());
    }

    //static int i;
    /**
     * The Class UpdateTask.
     */
    private class UpdateTask extends TimerTask
    {
        /* (non-Javadoc)
         * @see java.util.TimerTask#run()
         */
        @Override
        public void run()
        {
            //     P.pr(i++);
            percentage += 100 * (rateMs / totalTimeMs);
            if(percentage >= 100)
            {
                percentage = 100;
                stop();
            }
            LeftPanel.orderRepaint(ProgressBar.this);
        }
    }
}
