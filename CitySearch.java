/**
 * A program that reads a file of cities with 
 * longitiude and latitude coordinates and prints out 
 * the shortest path between two cities.
 * 
 * @author Brandon Sinjakovic
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

public class CitySearch 
{
	private CityGraph cities = new CityGraph();
	private HashMap<String,String> cach = new HashMap<>();
	
	/**
     * reads in the .txt file name as a string and splits up
     * the file line by line that builds a graph of cities and connect 
     * them if they are less than 2000 miles apart.
     *
     * @param String inputFile name of the .txt file.
     */
	private void readFile( String inputFile )
	{
		try
		{
			FileReader fin = new FileReader( inputFile );
			BufferedReader read = new BufferedReader( fin );
			
			String line = read.readLine();
			line = read.readLine();
			
			while( line != null )
			{
				String[] str = line.split("(?=\\t)(\\s+)");
				cities.add( str[0], new Double(str[1]),  new Double(str[2]) );
				Set<String> knownCities = cities.graphToSet();
				for( String city : knownCities )
				{
					double d = cities.distance( str[0], city );
					if( d < 2000 )
						cities.setEdge(str[0], city, d);
				}
				line = read.readLine();
			}
			read.close();
			System.out.println("Graph buildingComplete.");
			readInput();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		} 
	}
	
	/**
     * Asks the user for two cities that are in the graph 
     * and prints out the path between the two.
     */
	private void readInput() 
	{
		String start, end;
		while(true)
		{
			System.out.println("Enter start city (\"Q\" to quit):");
			start = askForCity();
			if( start.equals("Q") )
			{
				System.out.println("Terminated.  Goodbye.");
				return;
			}
			if( !cities.inGraph(start) )
			{
				System.out.println(start+" is not part of data-base. Please try again.");
				continue;
			}
			System.out.println("Enter end city (\"Q\" to quit):");
			end = askForCity();
			if( end.equals("Q") )
			{
				System.out.println("Terminated.  Goodbye.");
				return;
			}
			if( !cities.inGraph(end) )
			{
				System.out.println(end+" is not part of data-base. Please try again.");
				continue;
			}
			if( !cach.containsKey(start+ " "+ end) )
			{
				String path = cities.dijkstraPaths( start, end );
				cach.put( start+ " "+ end, path );
				System.out.println( path );
			}
			else
				System.out.println( cach.get(start+ " "+ end) );	
		}
	}
	
	/**
     * A helper method that asks the user for an input.
     * 
     * @return String the users input.
     */
	private String askForCity() 
	{
		String city = null;
		try
		{
				BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
				city = read.readLine();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		} 
		return city;
	}

	public static void main(String[] args) 
	{
		CitySearch test = new CitySearch();
		test.readFile( args[0] ); 
	}
	

}
