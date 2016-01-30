package rts.model.map;

//import terrain.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.misc.Line;
import rts.misc.exceptions.OutOfMapException;
import rts.model.serverBuildings.Building;
import rts.view.clientModel.BuildingImage;

// TODO: Auto-generated Javadoc
/**
 * Graf sciezek sluzy do wyszukiwania trasy przy poruszaniu jednostek tak, by
 * omijaly budynki.
 */
public class PathGraph
{
    /**
     * przy tworzeniu wierzcholkow dla budynku, jest to odstep wierzcholka od
     * prostokata budynku.
     */
    public static final int NODE_SPACE = 8;
    //    /** Wierzcholki */
    private final TreeSet<PathNode> nodes;
    /** Mapa terenu zajetego przez budynki */
    private final BuildMap buildMap;
    /** The Constant CHECK_GAP. */
    public static final int CHECK_GAP = 6;
    private final Map<Building, ArrayList<PathNode>> buildingNodes;

    /**
     * Instantiates a new path graph.
     * 
     * @param map2 the map2
     */
    public PathGraph(final BuildMap map2)
    {
        buildMap = map2;
        nodes = new TreeSet<PathNode>();
        buildingNodes = new HashMap<Building, ArrayList<PathNode>>();
    }

    /**
     * Komparator porownuje wierzcholki pod wzgledem kosztu przejscia przy
     * wyszukiwaniu trasy
     */
    private static class CostCmp implements Comparator<PathNode>
    {
        /*
         * (non-Javadoc)
         *
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(final PathNode o1, final PathNode o2)
        {
            return o1.compareCost(o2);
        }
    }

    /**
     * Znajduje najktrotrza trase miedzy wierzcholkiem poczatkowym i koncowym
     * przy pomocy algorytmu A*
     * 
     * @param startNode wierzcholek poczatkowy
     * @param goalNode wierzcholek koncowy
     * @return lista punktow trasy lub null jesli nie znaleziono trasy
     */
    private LinkedList<Coords> findPath(final PathNode startNode, final PathNode goalNode)
    {
        final LinkedList<PathNode> openList = new LinkedList<PathNode>();
        final LinkedList<PathNode> visitedList = new LinkedList<PathNode>();
        PathNode node;
        startNode.costFromStart = 0;
        startNode.estimatedCostToGoal = startNode.getEstimatedCost(goalNode);
        startNode.previous = null;
        openList.add(startNode);
        while(!openList.isEmpty())
        {
            node = openList.removeFirst();
            if(node.equals(goalNode))
            {
                return constructPath(goalNode);
            }
            for(final PathNode nb : node.getNeighbors())
            {
                final boolean isOpen = openList.contains(nb);
                final boolean isClosed = visitedList.contains(nb);
                final float costFromStart = node.costFromStart + node.getEstimatedCost(nb);
                if((!isOpen && !isClosed) || costFromStart < nb.costFromStart)
                {
                    nb.previous = node;
                    nb.costFromStart = costFromStart;
                    nb.estimatedCostToGoal = nb.getEstimatedCost(goalNode);
                    if(isClosed)
                    {
                        visitedList.remove(nb);
                    }
                    if(!isOpen)
                    {
                        openList.add(nb);
                        Collections.sort(openList, new CostCmp());
                    }
                }
            }
            visitedList.add(node);
        }
        return null;
    }
    /**
     * Buduje trase od koncowego wierzcholka do poczatkowego korzystajac z
     * zapisanych w wierzcholkach poprzednikow
     * 
     * @param node wierzcholek koncowy
     * @return lista punktow trasy ( bez poczatkowego )
     */
    private LinkedList<Coords> constructPath(PathNode node)
    {
        final LinkedList<Coords> path = new LinkedList<Coords>();
        while(node.previous != null)
        {
            path.addFirst(node.getPosition());
            node = node.previous;
        }
        return path;
    }
    /**
     * Wyszukuje najkrotrza trase miedzy punktem poczatkowym a koncowym
     * 
     * @param begin punkt poczatkowy
     * @param end punkt koncowy
     * @return lista punktow trasy lub null jesli nie znaleziono trasy
     */
    public List<Coords> findPath(final Coords begin, Coords end)
    {
        if(begin.equals(end))
        {
            return null;
        }
        end = buildMap.correctOutOfMapPosition(end);
        if(checkFreeWay(begin, end))
        {
            final LinkedList<Coords> way = new LinkedList<Coords>();
            way.add(end);
            return way;
        }
        final PathNode tmpA = createTmpNode(begin);
        final PathNode tmpB = createTmpNode(end);
        final List<Coords> path = findPath(tmpA, tmpB);
        removeTmpNode(tmpA);
        removeTmpNode(tmpB);
        if(path == null)
        {
            return null;
        }
        final List<Coords> way = optimizePath(path, begin);
        // way.add(end);
        return way;
    }
    /**
     * Optymalizuje trase skracajac ja, jesli mozna miedzy punktami przejsc na
     * wprost.
     * 
     * @param trasa bez punktu poczatkowego
     * @param beg punkt poczatkowy
     * @return zoptymalizowana trasa
     */
    private List<Coords> optimizePath(final List<Coords> path, final Coords beg)
    {
        final ArrayList<Coords> goodWay = new ArrayList<Coords>();
        Coords prev = beg;
        Coords a, b = null;
        a = beg;
        final ListIterator<Coords> it = path.listIterator();
        while(it.hasNext())
        {
            b = it.next();
            if(!checkFreeWay(a, b))
            {
                goodWay.add(prev);
                a = prev;
                it.previous();
            }
            prev = new Coords(b);
        }
        if(goodWay.isEmpty() || (b != null && !goodWay.get(goodWay.size() - 1).equals(b)))
        {
            goodWay.add(b);
        }
        return goodWay;
    }
    /**
     * Sprawdza czy mozna przejsc na wprost miedzy dwoma punktami
     * 
     * @param a pierwszy punkt
     * @param b drugi punkt
     * @return true, jesli mozna przejsc
     */
    private boolean checkFreeWay(final Coords a, final Coords b)
    {
        if(!a.equals(b))
        {
            final Line line = new Line(a, b);
            final List<Coords> pointsOnLine = line.getPointsOnLine(CHECK_GAP);
            for(final Coords coords : pointsOnLine)
            {
                Tile tile;
                try
                {
                    tile = buildMap.getTile(coords);
                    if(!tile.isReachable())
                    {
                        return false;
                    }
                }
                catch(final OutOfMapException e)
                {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * Tworzy wierzcholek
     * 
     * @param wspolrzedne wierzcholka
     * @return stworzony wierzcholek
     */
    private PathNode createNode(final Coords location)
    {
        PathNode node = new PathNode(location);
        if(!nodes.add(node))
        {
            node = nodes.ceiling(node);
        }
        connectToAll(node);
        return node;
    }
    /**
     * Tworzy wierzcholek tymczasowy ( punkt poczatkowy lub koncowy trasy)
     * 
     * @param wspolrzedne wierzcholka
     * @return stworzony wierzcholek
     */
    private PathNode createTmpNode(final Coords location)
    {
        final PathNode node = new PathNode(location);
        connectToAll(node);
        return node;
    }
    /**
     * Usuwa wierzcholek
     * 
     * @param dany wierzcholek
     */
    private void removeNode(final PathNode node)
    {
        for(final PathNode neigh : node.getNeighbors())
        {
            neigh.getNeighbors().remove(node);
        }
        node.getNeighbors().clear();
    }
    /**
     * Usuwa wierzcholek tymczasowy
     * 
     * @param dany wierzcholek
     */
    private void removeTmpNode(final PathNode node)
    {
        for(final PathNode pn : node.getNeighbors())
        {
            pn.getNeighbors().remove(node);
        }
        node.getNeighbors().clear();
    }
    /**
     * Laczy wierzcholek z kazdym innym wierzcholkiem chyba ze miedzy nimi jest
     * przeszkoda
     * 
     * @param dany wierzcholek
     */
    private void connectToAll(final PathNode node)
    {
        final Coords a = node.getPosition();
        Coords b = null;
        for(final PathNode pn : nodes)
        {
            b = pn.getPosition();
            if(checkFreeWay(a, b))
            {
                connect(node, pn);
            }
        }
    }
    /**
     * Laczy dwa wierzcholki
     * 
     * @param node pierwszy wierzcholek
     * @param other inny wierzcholek
     */
    private void connect(final PathNode node, final PathNode other)
    {
        if(node.equals(other))
        {
            return;
        }
        if(!node.getNeighbors().contains(other))
        {
            node.addNeighbour(other);
        }
        if(!other.getNeighbors().contains(node))
        {
            other.addNeighbour(node);
        }
    }
    /**
     * Tworzy cztery wierzcholki grafu dla budynku
     * 
     * @param building budynek
     */
    public void createPathNodes(final Building building)
    {
        final int space = NODE_SPACE;
        final ArrayList<PathNode> pathNodes = new ArrayList<PathNode>();
        //        final BuildingPathNodes pathNodes = new BuildingPathNodes();
        final ConstRect rect = building.getBounds();
        pathNodes.add(createNode(rect.getUpperLeft().moveBy(-space, -space)));
        pathNodes.add(createNode(rect.getUpperRight().moveBy(space, -space)));
        pathNodes.add(createNode(rect.getLowerLeft().moveBy(-space, space)));
        pathNodes.add(createNode(rect.getLowerRight().moveBy(space, space)));
        // pathNodes.connectAll();
        buildingNodes.put(building, pathNodes);
        //        building.setPathNodes(pathNodes);
    }
    /**
     * Usuwa cztery wierzcholki grafu dla budynku
     * 
     * @param building budynek
     */
    public void deletePathNodes(final Building building)
    {
        final ArrayList<PathNode> arrayList = buildingNodes.get(building);//building.getPathNodes();
        for(final PathNode node : arrayList)
        {
            removeNode(node);
        }
        arrayList.clear();
    }

    /**
     * Klasa przechowujaca wierzcholki grafu dla kazdego budynku
     */
    public class BuildingPathNodes
    {
        /** The corner nodes. */
        List<PathNode> cornerNodes;

        /**
         * Instantiates a new building path nodes.
         */
        BuildingPathNodes()
        {
            cornerNodes = new ArrayList<PathNode>();
        }
        /**
         * Adds the corner node.
         * 
         * @param newNode the new node
         */
        private void addCornerNode(final PathNode newNode)
        {
            cornerNodes.add(newNode);
        }
    }

    /**
     * Gets the nodes.
     * 
     * @return the nodes
     */
    public Set<PathNode> getNodes()
    {
        return nodes;
    }
    public void insertBuilding(final Building building)
    {
        buildMap.makeUnreachable(building.getBounds());
        createPathNodes(building);
    }
    public void removeBuilding(final Building building)
    {
        buildMap.makeBuildableTerrain(building.getBounds());
        deletePathNodes(building);
    }
    public boolean canBeBuild(final BuildingImage buildingSprite, final Coords position)
    {
        // TODO Auto-generated method stub
        return buildMap.canBeBuild(buildingSprite, position);
    }
}
