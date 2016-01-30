package rts.model.map;

//import terrain.*;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;
import rts.misc.Coords;
import rts.misc.exceptions.Exc;

public class PathNode implements Comparable<PathNode>, Serializable
{
    private final Coords position;
    private final Set<PathNode> neighbours = new TreeSet<PathNode>();
    protected float costFromStart;
    protected float estimatedCostToGoal;
    protected PathNode previous;

    protected PathNode(final Coords pos)
    {
        position = pos;
    }
    public Set<PathNode> getNeighbors()
    {
        return neighbours;
    }
    protected void addNeighbour(final PathNode b)
    {
        neighbours.add(b);
    }
    protected float getEstimatedCost(final PathNode b)
    {
        final float dx = b.getX() - getX();
        final float dy = b.getY() - getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
    @Override
    public boolean equals(final Object obj)
    {
        if(obj instanceof PathNode)
        {
            final PathNode pathNode = (PathNode) obj;
            return position.equals(pathNode.position);
        }
        return false;
    }
    public int compareTo(final PathNode b)
    {
        if(getX() == b.getX() && getY() == b.getY())
        {
            return 0;
        }
        if(getX() < b.getX() || (getX() == b.getX() && getY() < b.getY()))
        {
            return -1;
        }
        if(getX() > b.getX() || (getX() == b.getX() && getY() > b.getY()))
        {
            return 1;
        }
        else
        {
            throw new Exc("BAD COMPARE TO");
        }
    }
    protected int compareCost(final PathNode b)
    {
        final float otherValue = b.getCost();
        final float thisValue = getCost();
        return (int) Math.signum(thisValue - otherValue);
    }
    private float getCost()
    {
        return costFromStart + estimatedCostToGoal;
    }
    public Coords getPosition()
    {
        return position;
    }
    public int getX()
    {
        return position.getX();
    }
    public int getY()
    {
        return position.getY();
    }
    @Override
    public String toString()
    {
        return super.toString() + " (" + position.getX() + "," + position.getY() + ")";
    }
}
