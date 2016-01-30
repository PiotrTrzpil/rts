package rts.view.animation;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Timer;
import javax.swing.ImageIcon;
import rts.RtsGame;
import rts.controller.Timers;
import rts.misc.Coords;
import rts.misc.CoordsDouble;
import rts.misc.OvalDouble;
import rts.view.mapView.Drawable;
import rts.view.mapView.GameGraphics;
import rts.view.mapView.MapView;

// TODO: Auto-generated Javadoc
/**
 * Klasa zarzadzajaca animacjami
 */
public class EffectsPool
{
    /**
     * Identyfikator animacji
     */
    public enum Effect
    {
        /** The MOV e_ crosshair. */
        MOVE_CROSSHAIR,
        /** The ATTAC k_ crosshair. */
        ATTACK_CROSSHAIR,
        /** The ATTACKMOV e_ crosshair. */
        ATTACKMOVE_CROSSHAIR,
        /** The SELECTION. */
        SELECTION;
    }

    /** Dzialajace globalne animacje. */
    private final List<AbstractAnimation> runningAnimations;
    /** The Constant imagePath. */
    private static final String imagePath = "images/animations/";
    /** Mapa z gotowymi wzorami animacji. */
    private final EnumMap<Effect, AbstractAnimationPattern> effectsMap;
    /** The effect timer. */
    private final Timer effectTimer;
    /** The map view. */
    private final MapView mapView;
    /** The gc. */
    private final GraphicsConfiguration gc;

    /**
     * Gets the animations.
     * 
     * @return the animations
     */
    public List<Drawable> getAnimations()
    {
        final List<Drawable> list = new ArrayList<Drawable>();
        for(int i = 0; i < runningAnimations.size(); i++)
        {
            final AbstractAnimation anim = runningAnimations.get(i);
            if(anim.hasEnded())
            {
                runningAnimations.remove(i);
                i--;
            }
            else
            {
                list.add(anim.getDrawableImage());
            }
        }
        return list;
    }
    /**
     * Instantiates a new effects pool.
     * 
     * @param view the view
     */
    public EffectsPool(final MapView view)
    {
        runningAnimations = new ArrayList<AbstractAnimation>();
        mapView = view;
        effectTimer = Timers.animTimer();
        effectsMap = new EnumMap<Effect, AbstractAnimationPattern>(Effect.class);
        gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        makeEffects();
    }
    /**
     * Tworzenie efektow
     */
    public void makeEffects()
    {
        effectsMap.put(Effect.SELECTION, makeSelection());
        effectsMap.put(Effect.MOVE_CROSSHAIR, makeCrosshair("Crosshair_Move.png"));
        effectsMap.put(Effect.ATTACKMOVE_CROSSHAIR, makeCrosshair("Crosshair_AttackMove.png"));
        effectsMap.put(Effect.ATTACK_CROSSHAIR, makeCrosshair("Crosshair_Attack.png"));
    }
    /**
     * Utworzenie statycznej animacji w podanym miejscu na mapie
     * 
     * @param effect Identyfikator animacji
     * @param where miejsce na mapie
     * @return stworzona animacja
     */
    public AbstractAnimation globalEffect(final Effect effect, final Coords where)
    {
        final AbstractAnimationPattern pattern = effectsMap.get(effect);
        final AbstractAnimation anim = pattern.newAnimation(where);
        anim.scheduleOn(effectTimer);
        runningAnimations.add(anim);
        return anim;
    }
    /**
     * Utworzenie nowej animacji, bez jej uruchomienia
     * 
     * @param effect Identyfikator animacji
     * @return stworzona animacja
     */
    public AbstractAnimation getAnimation(final Effect effect)
    {
        final AbstractAnimationPattern pattern = effectsMap.get(effect);
        final AbstractAnimation anim = pattern.newAnimation(new Coords());
        return anim;
    }
    /**
     * Tworzenie animacji zaznaczenia jednostek
     * 
     * @return Wzor animacji stanowej
     */
    public StateAnimationPattern makeSelection()
    {
        GameGraphics gg;
        BufferedImage animFrame;
        final CoordsDouble center = new CoordsDouble(20, 20);
        final int width = 23;
        final int height = 20;
        final Color color = Color.BLUE;
        // ///
        final AnimationPattern pattern1 = new AnimationPattern(40);
        int iterations = 5;
        float transpStart = 0.0f;
        float transpEnd = 1f;
        float transpGain = transpEnd - transpStart;
        float transpStep = transpGain / (iterations - 1);
        for(int i = 0; i < iterations; i++)
        {
            animFrame = newImage(40, 40);
            gg = new GameGraphics(animFrame.createGraphics());
            gg.setColor(color);
            gg.setAntialiased();
            final float currentTransp = transpStart + transpStep * i;
            gg.setTranslucent(currentTransp);
            new OvalDouble(center, width + (iterations - i), height + (iterations - i)).draw(gg);
            gg.dispose();
            pattern1.addFrame(animFrame);
        }
        pattern1.scaleToTime(200);
        // ///////////////
        final AnimationPattern pattern2 = new AnimationPattern(40);
        animFrame = newImage(40, 40);
        gg = new GameGraphics(animFrame.createGraphics());
        gg.setColor(color);
        gg.setAntialiased();
        new OvalDouble(center, width, height).draw(gg);
        gg.dispose();
        pattern2.addFrame(animFrame);
        pattern2.setLooping(true);
        // //////////////
        final AnimationPattern pattern3 = new AnimationPattern(40);
        iterations = 5;
        transpStart = 1f;
        transpEnd = 0.1f;
        transpGain = transpEnd - transpStart;
        transpStep = transpGain / (iterations - 1);
        for(int i = 0; i < iterations; i++)
        {
            animFrame = newImage(40, 40);
            gg = new GameGraphics(animFrame.createGraphics());
            gg.setColor(color);
            final float currentTransp = transpStart + transpStep * i;
            gg.setTranslucent(currentTransp);
            gg.setAntialiased();
            new OvalDouble(center, width + i, height + i).draw(gg);
            gg.dispose();
            pattern3.addFrame(animFrame);
        }
        // ///////////
        final AnimationPattern pattern4 = new AnimationPattern(40);
        iterations = 1;
        for(int i = 0; i < iterations; i++)
        {
            animFrame = newImage(40, 40);
            pattern4.addFrame(animFrame);
        }
        pattern4.setLooping(true);
        final StateAnimationPattern stateAnim = new StateAnimationPattern();
        stateAnim.setLooping(true);
        stateAnim.addAnimation(pattern1, true);
        stateAnim.addAnimation(pattern2, false);
        stateAnim.addAnimation(pattern3, true);
        stateAnim.addAnimation(pattern4, false);
        stateAnim.setShift(-20, -20);
        return stateAnim;
    }
    /**
     * Utworzenie obrazka o zadanych wymiarach
     * 
     * @param width the width
     * @param height the height
     * @return the buffered image
     */
    private BufferedImage newImage(final int width, final int height)
    {
        return gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }
    /**
     * Tworzenie strzalek wyswietlanych przy sterowaniu jednostkami
     * 
     * @param imageName nazwa pliku
     * @return wzor animacji
     */
    public AnimationPattern makeCrosshair(final String imageName)
    {
        final AnimationPattern pattern = new AnimationPattern(40);
        pattern.setShift(-21, -20);
        Image image, animFrame;
        final URL imageURL = RtsGame.class.getResource(imagePath + imageName);
        image = new ImageIcon(imageURL).getImage();
        Graphics2D g;
        Composite alpha;
        AffineTransform transf = new AffineTransform();
        final int centx = 12;
        final int centy = 16;
        final int iterations = 10;
        for(int i = 0; i < iterations; i++)
        {
            transf = new AffineTransform();
            animFrame = gc.createCompatibleImage(40, 40, Transparency.TRANSLUCENT);
            g = (Graphics2D) animFrame.getGraphics();
            alpha = null;
            for(int k = 0; k < 3; k++)
            {
                if(i == k || i == iterations - 1 - k)
                {
                    alpha = AlphaComposite
                            .getInstance(AlphaComposite.SRC_OVER, (float) (k + 1) / 3);
                }
            }
            if(alpha == null)
            {
                alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
            }
            g.setComposite(alpha);
            transf.translate(-iterations + i + centx, centy);
            g.drawImage(image, transf, null);
            g.fillRect(centx + 9, centy + 4, 1, 1);
            transf = new AffineTransform();
            transf.scale(-1, 1);
            transf.translate(-iterations + i - 19 - centx, centy);
            g.drawImage(image, transf, null);
            transf = new AffineTransform();
            transf.translate(centx + 1, -iterations + i + centy);
            transf.rotate(Math.PI / 2, 9, 4);
            g.drawImage(image, transf, null);
            g.dispose();
            pattern.addFrame(animFrame);
        }
        pattern.setLooping(false);
        return pattern;
    }
    /**
     * Gets the anim pattern.
     * 
     * @param effect the effect
     * @return the anim pattern
     */
    public AbstractAnimationPattern getAnimPattern(final Effect effect)
    {
        return effectsMap.get(effect);
    }
}
