/**
 * a modified version of a Simple weighted-graph 
 * implementation created by Dr. Allen.
 * Contains methods for Dijkstra's algorithm, to get
 * shortest (by total weight) paths.
 *
 * @author M. Allen
 * @author Brandon Sinjakovic
 *
 */

import java.util.*;

public class CityGraph
{
    //private ArrayList<City> cities = new ArrayList<>();
	private HashMap<String,City> cities = new HashMap<>();
    
    /**
     * Add a new City to the graph (without edges).
     */
    public void add( String d, double x, double y )
    {
        cities.put( d, new City( d, x, y ) );
    }

    /**
     * Return data-element from City at position idx.
     * @param integer index of City.
     * @return data-element of type String.
     */
    public City get( String idx )
    {
        return cities.get( idx );
    }
    
    /**
     * Adds a undirected edge between given cities.
     * @param integer index s of source City.
     * @param integer index t of target City.
     */
    public void setEdge( String s, String t, double w  )
    { 
        City source = cities.get( s );
        City target = cities.get( t );
        
        source.addEdge( target, w );
        target.addEdge(source, w);
    }

    /**
     * Private inner method for returning data elements of a sub-list of
     * cities.
     *
     * @param list of cities.
     * @return String consisting of data elements of list.
     */
    private String printData( LinkedList<City> ls, String start, String end )
    {
    	int totalLenth = 0;
        if ( ls.isEmpty() )
            return "No such path";
        
        String s = "Path from " + start + " To " +  end + ": ";
        for ( City v : ls )
        {
            s += v.data + " => ";
            totalLenth += v.distance;
        }
        s = s.substring(0, s.length()-4);
        
        s+= ". Length = "+ totalLenth + " miles.";
        
        return s;
    }
    
    /**
     * Method called to get the least-weight path from source to target.
     *
     * @param s Index of starting node (in list of nodes).
     * @param t Index of target node (in list of nodes).
     *
     * @return String describing the path (if any such exists).
     */
    public String dijkstraPaths( String s, String t )
    {
        return printData( dijkstra( s, t ), s, t );
    }
    
    /**
     * Private helper method that does the Dijkstra algorithm implementation on
     * nodes.
     *
     * @param s Index of starting node (in list of nodes).
     * @param t Index of target node (in list of nodes).
     *
     * @return LinkedList of cities consisting of path.
     */
    private LinkedList<City> dijkstra( String s, String t )
    {
        LinkedList<City> path = new LinkedList<>();
        City source = cities.get( s );
        City target = cities.get( t );
        
        for ( City v : cities.values() )
        {
            v.distance = Double.POSITIVE_INFINITY;
            v.known = false;
        }
        
        source.distance = 0;
        PriorityQueue<City> pq = new PriorityQueue<>();
        pq.add( source );
        
        while ( !pq.isEmpty() )
        {
            City v = pq.poll();
            v.known = true;
            
            for ( City.VWPair vw : v.adjacent )
            {
                City next = vw.vert;
                if ( !next.known && ( v.distance + vw.weight ) < next.distance )
                {
                    next.distance = v.distance + vw.weight;
                    next.prev = v;
                    pq.add( next );
                }
            }
        }
        path = backtrack( target, source );
        return path;
    }
    
    /**
     * Private helper method that can use the previous values of nodes to
     * generate a back-tracking path.
     *
     * @param end Last node in path.
     * @param start First node in path.
     *
     * @return LinkedList containing cities in order from start -> end.
     */
    private LinkedList<City> backtrack( City end, City start )
    {
        LinkedList<City> path = new LinkedList<>();
        City v = end;
        
        while ( v != null && v != start )
        {
            path.addFirst( v );
            v = v.prev;
            
            if ( path.size() > cities.size() )
                return new LinkedList<City>();
        }
        path.addFirst( start );
        return path;
    }
    
    public double distance( String x, String y )
    {
    	City v1 = cities.get(x);
    	City v2 = cities.get(y);
        double deltaLat = Math.toRadians(v2.lat - v1.lat);
        double deltaLon = Math.toRadians(v2.lon - v1.lon);
        double lat1 = Math.toRadians(v1.lat);
        double lat2 = Math.toRadians(v2.lat);
 
        double a = Math.pow(Math.sin(deltaLat / 2),2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(deltaLon / 2),2);
        double d = 12746 * Math.asin(Math.sqrt(a));
        //convert km to miles
        return 0.612 * d;  
    }
    
    public boolean inGraph( String c )
    {
    	return cities.containsKey( c );
    }
    
    public Set<String> graphToSet()
    {
    	return cities.keySet();
    }
    
    /**
     * private inner class for cities.
     */
    private class City implements Comparable<City>
    {
        // standard elements for data, adjacent nodes, in-degree
        String data;
        LinkedList<VWPair> adjacent;
        double lat, lon;
        
        // extra data elements for doing shortest-path (Dijkstra)
        double distance;
        boolean known;
        City prev;
        
        City( String d, double longitude , double latitude )
        {
            data = d;
            adjacent = new LinkedList<VWPair>(); 
            lat = latitude;
            lon = longitude;
        }
        
        public int compareTo( City v )
        {
            if ( distance < v.distance )
                return -1;
            
            if ( distance > v.distance )
                return 1;
            
            return 0;
        }
        
        private void addEdge( City target, double w )
        {
            adjacent.add( new VWPair( target, w ) );
        }
        
        /**
         * Private inner (inner!) class for weighted edges.
         * Accessed statically, as City.VWPair.
         * Represents weighted edge with two data elements, one for the City
         * that the edge connects this to, and one for the weight on that edge.
         */
        private class VWPair
        {
            City vert;
            double weight;
            
            VWPair( City v, double w )
            {
                vert = v;
                weight = w;
            }
        }
    }
}
