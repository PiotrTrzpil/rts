package rts.view;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import rts.RtsGame;
import rts.misc.GameVector;
import rts.misc.P;
import rts.misc.Size;
import rts.misc.TileDimensions;
import rts.model.BuildingType;
import rts.model.Item;
import rts.model.UnitType;
import rts.model.objectPatterns.BuildingPattern;
import rts.view.clientModel.BuildingImage;
import rts.view.clientModel.ObjectImage;

// TODO: Auto-generated Javadoc
/**
 * Klasa wczytujaca i przechowujaca obrazki, a takze wzory na budynki
 */
public class ImagesAndPatterns
{
    /**
     * The Class IconSet.
     */
    private static class IconSet
    {
        /** The width. */
        private int width;
        /** The height. */
        private int height;
        /** The unpressed. */
        private ImageIcon unpressed;
        /** The rollover. */
        private ImageIcon rollover;
        /** The pressed. */
        private ImageIcon pressed;
        /** The image. */
        private Image image;
    }

    /** The button path. */
    private static String buttonPath = "images/buttons/";
    /** The terrain path. */
    private static String terrainPath = "images/terrain/";
    /** The object path. */
    private static String objectPath = "images/objects/";
    /** The building path. */
    private static String buildingPath = "images/buildings/";
    /** The unit path. */
    private static String unitPath = "images/units/";
    /** The basic. */
    private final Map<String, Image> basic;
    /** The terrain. */
    private final Map<String, Image> terrain;
    /** The buildings. */
    private final EnumMap<BuildingType, BuildingImage> buildings;
    /** The icons. */
    private final Map<String, IconSet> icons;
    /** The units. */
    private final EnumMap<UnitType, ObjectImage> units;
    /** The objects. */
    private final Map<String, ObjectImage> objects;
    /** The default icon. */
    private IconSet defaultIcon;
    /** The images to process. */
    private final List<IconSet> imagesToProcess;
    /** The tracker. */
    private final MediaTracker tracker;
    /** The default building image. */
    private BuildingImage defaultBuildingImage;
    /** The graphics configuration. */
    private final GraphicsConfiguration graphicsConfiguration;
    /** The patterns. */
    private final EnumMap<BuildingType, BuildingPattern> patterns;
    /** The Constant resourceClass. */
    private static final Class<RtsGame> resourceClass = RtsGame.class;

    /**
     * Instantiates a new images and patterns.
     * 
     * @param imagePane the image pane
     */
    public ImagesAndPatterns(final Component imagePane)
    {
        basic = new HashMap<String, Image>();
        terrain = new HashMap<String, Image>();
        buildings = new EnumMap<BuildingType, BuildingImage>(BuildingType.class);
        icons = new HashMap<String, IconSet>();
        units = new EnumMap<UnitType, ObjectImage>(UnitType.class);
        objects = new HashMap<String, ObjectImage>();
        imagesToProcess = new LinkedList<IconSet>();
        patterns = new EnumMap<BuildingType, BuildingPattern>(BuildingType.class);
        tracker = new MediaTracker(imagePane);
        graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        loadGameWorldImages();
    }
    /**
     * Creates the building patterns.
     */
    private void createBuildingPatterns()
    {
        for(final BuildingType type : BuildingType.values())
        {
            final BuildingPattern pattern = new BuildingPattern(type, getBuildingSprite(type));
            type.createPattern(pattern);
            pattern.addBuildIngredient(Item.PLANK, 1);
            pattern.slotsFromRecipes();
            patterns.put(type, pattern);
        }
    }
    /**
     * Load basic images.
     */
    private void loadBasicImages()
    {
        URL imageURL;
        imageURL = resourceClass.getResource("images/bg.png");
        final Image background = new ImageIcon(imageURL).getImage();
        basic.put("MainBackground", background);
        imageURL = resourceClass.getResource("images/banner.png");
        final Image banner = new ImageIcon(imageURL).getImage();
        basic.put("MainBanner", banner);
        // / waitForImages();
    }
    /**
     * Wait for images.
     */
    private void waitForImages()
    {
        try
        {
            tracker.waitForAll();
        }
        catch(final InterruptedException ie)
        {
            ie.printStackTrace();
        }
        for(final IconSet set : imagesToProcess)
        {
            set.width = set.image.getWidth(null);
            set.height = set.image.getHeight(null);
        }
    }
    /**
     * Load game world images.
     */
    private void loadGameWorldImages()
    {
        loadBasicImages();
        // loadObjects();
        loadTerrain();
        loadUnits();
        loadBuildings();
        loadIcons();
        loadOther();
        waitForImages();
        createBuildingPatterns();
    }
    /**
     * Load other.
     */
    private void loadOther()
    {
        final URL imageURL = resourceClass.getResource("images/leftPane.png");
        final Image interf = new ImageIcon(imageURL).getImage();
        basic.put("LeftPanel", interf);
    }
    // =========================================================================
    /**
     * Load objects.
     */
    private void loadObjects()
    {
        ObjectImage sprite;
        GameVector drawShift;
        Size dimensions;
        drawShift = new GameVector(-23, -65);
        dimensions = new Size(10, 10);
        sprite = loadObjectSprite("Tree", dimensions, drawShift);
        drawShift = new GameVector(-20, -25);
        dimensions = new Size(10, 10);
        sprite = loadObjectSprite("Stone", dimensions, drawShift);
    }
    /**
     * Load object sprite.
     * 
     * @param name the name
     * @param dimensions the dimensions
     * @param drawShift the draw shift
     * @return the object image
     */
    private ObjectImage loadObjectSprite(final String name, final Size dimensions,
        final GameVector drawShift)
    {
        URL imageURL;
        imageURL = resourceClass.getResource(objectPath + name + ".png");
        final Image image = new ImageIcon(imageURL).getImage();
        final ObjectImage set = new ObjectImage(image, dimensions, drawShift);
        objects.put(name, set);
        return set;
    }
    /**
     * Gets the object sprite.
     * 
     * @param name the name
     * @return the object sprite
     */
    public ObjectImage getObjectSprite(final String name)
    {
        return objects.get(name);
    }
    // =========================================================================
    /**
     * Load terrain.
     */
    private void loadTerrain()
    {
        loadTerrainSprite("Grass");
        loadTerrainSprite("Test");
    }
    /**
     * Load terrain sprite.
     * 
     * @param name the name
     * @return the image
     */
    private Image loadTerrainSprite(final String name)
    {
        URL imageURL;
        Image set;
        imageURL = resourceClass.getResource(terrainPath + name + ".png");
        set = new ImageIcon(imageURL).getImage();
        terrain.put(name, set);
        return set;
    }
    /**
     * Gets the terrain sprite.
     * 
     * @param name the name
     * @return the terrain sprite
     */
    public Image getTerrainSprite(final String name)
    {
        return terrain.get(name);
    }
    // =========================================================================
    /**
     * Load units.
     */
    private void loadUnits()
    {
        ObjectImage sprite;
        GameVector drawShift;
        Size dimensions;
        // for(UnitType type : UnitType.values())
        // {
        //
        // URL imageURL = resourceClass.getResource(unitPath + type.toString() +
        // "Default.png");
        // final Image image = new ImageIcon(imageURL).getImage();
        // final ObjectImage set = new ObjectImage(image, dimensions,
        // drawShift);
        // units.put(type, set);
        // }
        drawShift = new GameVector(-8, -4);
        dimensions = new Size(18, 11);
        sprite = loadUnitSprite(UnitType.CARRIER, dimensions, drawShift);
        drawShift = new GameVector(-8, -4);
        dimensions = new Size(18, 11);
        sprite = loadUnitSprite(UnitType.BUILDER, dimensions, drawShift);
        drawShift = new GameVector(-8, -7);
        dimensions = new Size(18, 11);
        sprite = loadUnitSprite(UnitType.WARRIOR, dimensions, drawShift);
        drawShift = new GameVector(-8, -4);
        dimensions = new Size(18, 11);
        sprite = loadUnitSprite(UnitType.ARCHER, dimensions, drawShift);
    }
    /**
     * Load unit sprite.
     * 
     * @param type the type
     * @param dimensions the dimensions
     * @param drawShift the draw shift
     * @return the object image
     */
    private ObjectImage loadUnitSprite(final UnitType type, final Size dimensions,
        final GameVector drawShift)
    {
        URL imageURL;
        imageURL = resourceClass.getResource(unitPath + type.toString() + "Default.png");
        final Image image = new ImageIcon(imageURL).getImage();
        final ObjectImage set = new ObjectImage(image, dimensions, drawShift);
        units.put(type, set);
        return set;
    }
    /**
     * Gets the unit sprite.
     * 
     * @param type the type
     * @return the unit sprite
     */
    public ObjectImage getUnitSprite(final UnitType type)
    {
        return units.get(type);
    }
    // =========================================================================
    /**
     * Load buildings.
     */
    private void loadBuildings()
    {
        BuildingImage sprite;
        GameVector drawShift;
        TileDimensions tileDimensions;
        tileDimensions = new TileDimensions(3, 2);
        drawShift = new GameVector(0, -9);
        defaultBuildingImage = loadBuildingSprite(BuildingType.WOODCUTTER, tileDimensions,
            drawShift);
        defaultBuildingImage.setDoorX(24);
        // //
        tileDimensions = new TileDimensions(3, 2);
        drawShift = new GameVector(0, -9);
        sprite = loadBuildingSprite(BuildingType.WOODCUTTER, tileDimensions, drawShift);
        sprite.setDoorX(24);
        // ///
        tileDimensions = new TileDimensions(4, 3);
        drawShift = new GameVector(0, 0);
        sprite = loadBuildingSprite(BuildingType.LUMBER_MILL, tileDimensions, drawShift);
        sprite.setDoorX(66);
        // ///
        tileDimensions = new TileDimensions(3, 2);
        drawShift = new GameVector(-2, -47);
        sprite = loadBuildingSprite(BuildingType.BARRACKS, tileDimensions, drawShift);
        sprite.setDoorX(26);
        // ///
        tileDimensions = new TileDimensions(4, 3);
        drawShift = new GameVector(0, -21);
        sprite = loadBuildingSprite(BuildingType.STOREHOUSE, tileDimensions, drawShift);
        sprite.setDoorX(66);
        // /
        tileDimensions = new TileDimensions(3, 2);
        drawShift = new GameVector(0, 0);
        sprite = loadBuildingSprite(BuildingType.STONECUTTER, tileDimensions, drawShift);
        sprite.setDoorX(46);
        // /
        tileDimensions = new TileDimensions(4, 2);
        drawShift = new GameVector(10, -20);
        sprite = loadBuildingSprite(BuildingType.BOWMAKER, tileDimensions, drawShift);
        sprite.setDoorX(66);
        //
        tileDimensions = new TileDimensions(4, 3);
        drawShift = new GameVector(0, 0);
        sprite = loadBuildingSprite(BuildingType.ARMORER, tileDimensions, drawShift);
        sprite.setDoorX(66);
        // /
        tileDimensions = new TileDimensions(4, 3);
        drawShift = new GameVector(0, 0);
        sprite = loadBuildingSprite(BuildingType.LUMBER_MILL, tileDimensions, drawShift);
        sprite.setDoorX(66);
    }
    /**
     * Load building sprite.
     * 
     * @param type the type
     * @param dimensions the dimensions
     * @param drawShift the draw shift
     * @return the building image
     */
    private BuildingImage loadBuildingSprite(final BuildingType type,
        final TileDimensions dimensions, final GameVector drawShift)
    {
        URL imageURL;
        Image finished = null;
        // imageURL = resourceClass.getResource(buildingPath + type.toString() +
        // "Unfinished.png");
        // final Image unfinished = new ImageIcon(imageURL).getImage();
        try
        {
            imageURL = resourceClass.getResource(buildingPath + type.name() + ".png");
            finished = new ImageIcon(imageURL).getImage();
        }
        catch(final Exception e)
        {
            P.er("Image: " + type.name() + ".png" + " not found.");
            finished = defaultBuildingImage.getImage();
        }
        final BuildingImage set = new BuildingImage(finished, dimensions, drawShift);
        // set.setUnfinished(null);
        buildings.put(type, set);
        return set;
    }
    /**
     * Gets the building sprite.
     * 
     * @param type the type
     * @return the building sprite
     */
    public BuildingImage getBuildingSprite(final BuildingType type)
    {
        BuildingImage buildingImage = buildings.get(type);
        if(buildingImage == null)
        {
            buildingImage = defaultBuildingImage;
        }
        return buildingImage;
    }
    // =========================================================================
    /**
     * Load icons.
     */
    private void loadIcons()
    {
        defaultIcon = loadIcon("DefaultIcon");
        loadButton("Start");
        // loadIcon("Warrior");
        // loadIcon("Carrier");
        // loadIcon("Builder");
        loadIcon("BuildingsTab");
        loadIcon("InfoTab");
        for(final UnitType type : UnitType.values())
        {
            loadIcon(type.name());
        }
        for(final BuildingType type : BuildingType.values())
        {
            loadIcon(type.name());
        }
    }
    /**
     * To buffered image.
     * 
     * @param image the image
     * @return the buffered image
     */
    public static BufferedImage toBufferedImage(Image image)
    {
        if(image instanceof BufferedImage)
        {
            return (BufferedImage) image;
        }
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try
        {
            final int transparency = Transparency.TRANSLUCENT;// Transparency.BITMASK;
            final GraphicsDevice gs = ge.getDefaultScreenDevice();
            final GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null),
                transparency);
        }
        catch(final HeadlessException e)
        {
            e.printStackTrace();
        }
        final Graphics g = bimage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }
    /**
     * Load icon.
     * 
     * @param name the name
     * @return the icon set
     */
    public IconSet loadIcon(final String name)
    {
        final IconSet set = new IconSet();
        final URL imageURL = resourceClass.getResource(buttonPath + name + "-Default.png");
        if(imageURL == null)
        {
            P.er("Icon " + name + "-Default.png not found. Using default");
            set.unpressed = defaultIcon.unpressed;
        }
        else
        {
            set.unpressed = new ImageIcon(imageURL);
        }
        set.image = set.unpressed.getImage();
        tracker.addImage(set.image, 1);
        set.rollover = createRolloverIcon(set.image);
        set.pressed = createPressedIcon(set.image);
        icons.put(name, set);
        imagesToProcess.add(set);
        return set;
    }
    /**
     * Load button.
     * 
     * @param name the name
     * @return the icon set
     */
    public IconSet loadButton(final String name)
    {
        final IconSet set = new IconSet();
        URL imageURL = resourceClass.getResource(buttonPath + name + "-Default.png");
        if(imageURL == null)
        {
            P.er("Icon " + name + "-Default.png not found. Using default");
            set.unpressed = defaultIcon.unpressed;
        }
        else
        {
            set.unpressed = new ImageIcon(imageURL);
        }
        set.image = set.unpressed.getImage();
        tracker.addImage(set.image, 1);
        imageURL = resourceClass.getResource(buttonPath + name + "Rollover.png");
        if(imageURL == null)
        {
            set.rollover = defaultIcon.unpressed;
        }
        else
        {
            set.rollover = new ImageIcon(imageURL);
        }
        imageURL = resourceClass.getResource(buttonPath + name + "Pressed.png");
        if(imageURL == null)
        {
            set.pressed = defaultIcon.unpressed;
        }
        else
        {
            set.pressed = new ImageIcon(imageURL);
        }
        // set.rollover = createRolloverIcon(set.image);
        // set.pressed = createPressedIcon(set.image);
        icons.put(name, set);
        imagesToProcess.add(set);
        return set;
    }
    /**
     * Creates the rollover icon.
     * 
     * @param image the image
     * @return the image icon
     */
    private ImageIcon createRolloverIcon(final Image image)
    {
        BufferedImage buffered = createEmptyBufferedImageFrom(image);
        drawOn(image, buffered);
        buffered = brighten(buffered, 0.5f);
        return new ImageIcon(buffered);
    }
    /**
     * Creates the pressed icon.
     * 
     * @param image the image
     * @return the image icon
     */
    private ImageIcon createPressedIcon(final Image image)
    {
        BufferedImage buffered = createEmptyBufferedImageFrom(image);
        final Graphics2D g = buffered.createGraphics();
        g.drawImage(image, 2, 2, null);
        g.dispose();
        buffered = brighten(buffered, 0.5f);
        return new ImageIcon(buffered);
    }
    /**
     * Draw on.
     * 
     * @param image the image
     * @param target the target
     */
    private void drawOn(final Image image, final BufferedImage target)
    {
        final Graphics2D g = target.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }
    /**
     * Brighten.
     * 
     * @param image the image
     * @param factor the factor
     * @return the buffered image
     */
    private BufferedImage brighten(final BufferedImage image, final float factor)
    {
        final RescaleOp op = new RescaleOp(factor, 0, null);
        return op.filter(image, null);
    }
    /**
     * Creates the empty buffered image from.
     * 
     * @param image the image
     * @return the buffered image
     */
    private BufferedImage createEmptyBufferedImageFrom(Image image)
    {
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        try
        {
            final int transparency = Transparency.TRANSLUCENT;// Transparency.BITMASK;
            bimage = graphicsConfiguration.createCompatibleImage(image.getWidth(null), image
                    .getHeight(null), transparency);
        }
        catch(final HeadlessException e)
        {
            e.printStackTrace();
        }
        return bimage;
    }
    /**
     * Creates the button.
     * 
     * @param button the button
     * @param name the name
     */
    public void createButton(final JButton button, final String name)
    {
        final IconSet set = icons.get(name);
        if(set == null)
        {
            // / throw new Exc("ICON " + name + " NOT FOUND");
            P.er("ICON " + name + " NOT FOUND");
            button.setBounds(0, 0, 120, 30);
            button.setText(name);
            // set = defaultIcon;
        }
        else
        {
            button.setBounds(0, 0, set.width, set.height);
            button.setBorder(null);
            button.setContentAreaFilled(false);
            button.setIcon(set.unpressed);
            button.setRolloverIcon(set.rollover);
            button.setPressedIcon(set.pressed);
        }
        button.setName(name);
        button.setIgnoreRepaint(true);
        button.setFocusable(false);
        button.setToolTipText(name);
    }
    /**
     * Creates the button.
     * 
     * @param button the button
     * @param name the name
     * @param x the x
     * @param y the y
     */
    public void createButton(final JButton button, final String name, final int x, final int y)
    {
        createButton(button, name);
        final Rectangle bounds = button.getBounds();
        bounds.x = x;
        bounds.y = y;
        button.setBounds(bounds);
    }
    /**
     * Gets the basic image.
     * 
     * @param name the name
     * @return the basic image
     */
    public Image getBasicImage(final String name)
    {
        return basic.get(name);
    }
    /**
     * Gets the building pattern.
     * 
     * @param type the type
     * @return the building pattern
     */
    public BuildingPattern getBuildingPattern(final BuildingType type)
    {
        return patterns.get(type);
    }
}
