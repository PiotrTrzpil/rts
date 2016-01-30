package rts.misc;

// TODO: Auto-generated Javadoc
/**
 * Wektor
 */
public class GameVector// extends Shape
{
    /** The x. */
    private final double x;
    /** The y. */
    private final double y;

    /**
     * Instantiates a new game vector.
     */
    public GameVector()
    {
        this(0, 0);
    }
    /**
     * Instantiates a new game vector.
     * 
     * @param x the x
     * @param y the y
     */
    public GameVector(final double x, final double y)
    {
        this.x = x;
        this.y = y;
    }
    /**
     * Instantiates a new game vector.
     * 
     * @param an the an
     */
    public GameVector(final GameVector an)
    {
        x = an.getX();
        y = an.getY();
    }
    /**
     * Mnozy wspolrzedne przez liczbe
     * 
     * @param value the value
     * @return the game vector
     */
    public GameVector multiplyBy(final double value)
    {
        return new GameVector(getX() * value, getY() * value);
    }
    /**
     * Gets the x.
     * 
     * @return the x
     */
    public double getX()
    {
        return x;
    }
    /**
     * Gets the y.
     * 
     * @return the y
     */
    public double getY()
    {
        return y;
    }
    /**
     * Odejmuje dany wektor od tego wektora.
     * 
     * @param vector the vector
     * @return the game vector
     */
    public GameVector substract(final GameVector vector)
    {
        return new GameVector(x - vector.x, y - vector.y);
    }
    /**
     * Neguje wektor.
     * 
     * @return the game vector
     */
    public GameVector negate()
    {
        return new GameVector(-x, -y);
    }
}
