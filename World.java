import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class World {
	
	ArrayList<ArrayList<Integer>> worldAsArray;
	Coordinate start, goal;
	
	public World()
	
	{
		start = new Coordinate(0,0);
		goal = new Coordinate(0,0);
	}
	public Coordinate getStart()
	{
		return start;
	}
	public Coordinate getGoal()
	{
		return goal;
	}
	
	public void setBoard(String input)
	{
		try {
			File file = new File("config/" + input);
			FileReader fr;
			fr = new FileReader(file);

			BufferedReader br= new BufferedReader(fr);
	        int c = 0;

	        ArrayList<ArrayList<Integer>> map = new ArrayList<ArrayList<Integer>>();
	        ArrayList<Integer> row = new ArrayList<Integer>();
	        int r = 0;
	        while((c = br.read()) != -1)
	        {
	              char character = (char) c;
	              if(character == '\n') {
	            	  map.add(row);
	            	 row = new ArrayList<Integer>();
	              } else if(Character.toUpperCase(character) == 'S')
            	  {
	            	row.add(1);
	            	start = new Coordinate(c,r);		//column = x, row = y
            	  }
	            	  
	              else if (Character.toUpperCase(character) == 'G')
	              {
	            	row.add(1);
	            	goal = new Coordinate(c,r);			//column = x, row = y
	              }
	              else if (character != '\t' && character != '\r') {
	            	  row.add(Character.getNumericValue(character));
	              }
	              r++;
	        }

	        fr.close();
	        br.close();
	        worldAsArray = map;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void print() {
	    for (int i = 0; i < worldAsArray.size(); i++) {
	        for (int j = 0; j < worldAsArray.get(i).size(); j++) {
	            System.out.print(worldAsArray.get(i).get(j) + " ");
	        }
	        System.out.println();
	    }
	}
	
	public int calculateGraphCost(Coordinate c, Moveset m)
	{
		if (m == Moveset.FORWARD)
		{
			//just the # on the coordinate
		}
		else if (m == Moveset.TURN_LEFT || m == Moveset.TURN_RIGHT)
		{
			// 1/2 the # on the coordinate
		}
		else if (m == Moveset.BASH)
		{
			return 3;
		}
		else if (m == Moveset.DEMOLISH)
		{
			return 4;
		}
		return 0;
	}
}
