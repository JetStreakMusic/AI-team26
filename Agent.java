import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javafx.util.Pair;

enum Moveset {
    FORWARD,
    TURN_LEFT,
    TURN_RIGHT,
    BASH,
    DEMOLISH;
}

enum Heuristic {
    H1,
    H2,
    H3,
    H4,
    H5,
    H6;
}

enum Facing {
    NORTH,
    EAST,
    SOUTH,
    WEST;
}

public class Agent {
	private static World world;
	private int score = 0;
	private static Facing direction;
	
	public Agent()
	{
		world = new World();
		direction = Facing.NORTH;
	}
	
	//Returns a heuristic of 0
	public static int heuristic1()
	{
		return 0;
	}
	
	//Get the min of the vertical and horizontal distance
	public static int heuristic2(Coordinate currCoordinate)
	{
		int horizontalDist = Math.abs(world.getGoal().getX()- currCoordinate.getX());
		int verticalDist = Math.abs(world.getGoal().getY()- currCoordinate.getY());
		return (verticalDist < horizontalDist) ? verticalDist : horizontalDist;
	}
	
	//Get the max of the vertical and horizontal distance
	public static int heuristic3(Coordinate currCoordinate)
	{
		int horizontalDist = Math.abs(world.getGoal().getX()- currCoordinate.getX());
		int verticalDist = Math.abs(world.getGoal().getY()- currCoordinate.getY());
		return (verticalDist > horizontalDist) ? verticalDist : horizontalDist;
	}
	
	//Get the sum of the vertical and horizontal distance
	public static int heuristic4(Coordinate currCoordinate)
	{
		int horizontalDist = Math.abs(world.getGoal().getX()- currCoordinate.getX());
		int verticalDist = Math.abs(world.getGoal().getY()- currCoordinate.getY());
		return verticalDist + horizontalDist;
	}
	
	public static int heuristic5(Coordinate currCoordinate)
	{
		return 0;
	}
	
	//Heuristic 5 multiplied by 3
	public static int heuristic6(Coordinate currCoordinate)
	{
		return heuristic5(currCoordinate) * 3;
	}
	
	/*
	 * astar
	 * Should return something - probably a list of either Coordinates or moves
	 * Track the # of nodes expanded somehow (visited nodes) - counter
	 */
	public static void astar(Heuristic h, Coordinate start, Coordinate goal)
	{
		Output output = new Output();
		ArrayList<Moveset> totalMoves = new ArrayList();
		int nodesExpanded = 0;
		PriorityQueue<PairComparable> frontierQueue = new PriorityQueue();
		PairComparable initialPair = new PairComparable(start, 0);
		frontierQueue.add(initialPair);
		
		HashMap<Coordinate, ArrayList<Moveset>> came_from = new HashMap<Coordinate,ArrayList<Moveset>>();
		HashMap<Coordinate,Facing> astar_direction = new HashMap<Coordinate,Facing>();
		astar_direction.put(start, Facing.NORTH);		//default
		HashMap<Coordinate, Integer> cost_so_far = new HashMap<Coordinate,Integer>();
		cost_so_far.put(start, 0);
		came_from.put(start, new ArrayList());
		ArrayList<Moveset> options = new ArrayList<Moveset>();
		
		while(!frontierQueue.isEmpty())
		{
			options = new ArrayList<Moveset>();
			PairComparable currentPair = frontierQueue.poll();
			int currentX = currentPair.getKey().getX();
			int currentY = currentPair.getKey().getY();
			
			if (currentPair.getKey().equals(goal))
			{
				int totalNumberOfMoves = came_from.get(currentPair.getKey()).size();
				output.setNumberOfMoves(totalNumberOfMoves);
				totalMoves = came_from.get(currentPair.getKey());
				output.setTotalMoves(totalMoves);
				for (Moveset move : totalMoves)
				{
					System.out.println(move.toString());
				}
				break;
			}
			
			//if previous move was BASH, options = {forward}
			//otherwise its {all 4 moves}
			int finalMoveIndex = 0;
			if(!(currentPair.getKey() == start))
			{
				finalMoveIndex = came_from.get(currentPair.getKey()).size()-1;
			}
			
			// if you're not on top of the 'start' coordinate && your last move was Bash....
			if((!(currentPair.getKey() == start)) && (came_from.get(currentPair.getKey()).get(finalMoveIndex) == Moveset.BASH))
			{				
				options.add(Moveset.FORWARD);
			}
			else
			{
				options.add(Moveset.FORWARD);
				options.add(Moveset.TURN_LEFT);
				options.add(Moveset.TURN_RIGHT);
				options.add(Moveset.BASH);
				options.add(Moveset.DEMOLISH);
			}
			
			for (Moveset m : options)
			{
				nodesExpanded++;
				int new_cost = cost_so_far.get(currentPair.getKey());
				Coordinate next = new Coordinate(0,0); /* calculate the next coordinate */
				if (m == Moveset.FORWARD)
				{
					if (direction == Facing.NORTH)
					{
						next = new Coordinate(currentX-1, currentY);
					}	
					else if (direction == Facing.SOUTH)
					{ next = new Coordinate(currentX+1, currentY);}
					else if (direction == Facing.WEST)
					{ next = new Coordinate(currentX, currentY-1);}
					else if (direction == Facing.EAST)
					{ next = new Coordinate(currentX, currentY+1);}
					
					if(!world.insideBounds(next))
						continue;
				}
				else if (m == Moveset.TURN_LEFT || m == Moveset.TURN_RIGHT)
				{
					next = currentPair.getKey();
					Facing nextDirection = Facing.NORTH;
					if (direction == Facing.NORTH)
					{
						if(m == Moveset.TURN_LEFT)
							nextDirection = Facing.WEST;
						if(m == Moveset.TURN_RIGHT)
							nextDirection = Facing.EAST; 
					}
					else if (direction == Facing.EAST)
					{
						if(m == Moveset.TURN_LEFT)
							nextDirection = Facing.NORTH;
						if(m == Moveset.TURN_RIGHT)
							nextDirection = Facing.SOUTH; 
					}
					else if (direction == Facing.SOUTH)
					{
						if(m == Moveset.TURN_LEFT)
							nextDirection = Facing.EAST;
						if(m == Moveset.TURN_RIGHT)
							nextDirection = Facing.WEST; 
					}
					else if (direction == Facing.WEST)
					{
						if(m == Moveset.TURN_LEFT)
							nextDirection = Facing.SOUTH;
						if(m == Moveset.TURN_RIGHT)
							nextDirection = Facing.NORTH; 
					}
					astar_direction.put(next, nextDirection);
				}
				else if (m == Moveset.BASH)
				{
					if (direction == Facing.NORTH)
					{
						next = new Coordinate(currentX-1, currentY);
					}	
					else if (direction == Facing.SOUTH)
					{ next = new Coordinate(currentX+1, currentY);}
					else if (direction == Facing.WEST)
					{ next = new Coordinate(currentX, currentY-1);}
					else if (direction == Facing.EAST)
					{ next = new Coordinate(currentX, currentY+1);}
					
					if(!world.insideBounds(next))
						continue;
				}
				else if (m == Moveset.DEMOLISH)
				{
					next = currentPair.getKey();
				}
				
				new_cost = new_cost + world.calculateGraphCost(next, m);	
				// if next not in cost_so_far or new_cost < cost_so_far[next]:
				// if ther's no cost known		of		new cost is better
				if (!(cost_so_far.containsKey(next)) || new_cost < cost_so_far.get(next))
				{
					cost_so_far.put(next, new_cost);
					
					int heuristic_value = 0;
					switch (h) {
			            case H1:
						{
							heuristic_value = heuristic1();
							break;
						}
			            case H2:
			            {
							heuristic_value = heuristic2(next);
							break;
						}
			            case H3:
			            {
							heuristic_value = heuristic3(next);
							break;
						}
			            case H4:
			            {
							heuristic_value = heuristic4(next);
							break;
						}
			            default:
			            {
							heuristic_value = heuristic1();
							break;
						}
						
					// add more for each heuristic
					}
					int priority = new_cost + heuristic_value;
					
					PairComparable nextPair = new PairComparable(next, priority);
					ArrayList<Moveset> movesToCurrent = came_from.get(currentPair.getKey());
					movesToCurrent.add(m);
					frontierQueue.add(nextPair);
					came_from.put(next, movesToCurrent);
				}
			}
			
			/*TODO 
			 * choose the right heuristic function
			 * A* implementation
			 * logging the moves chosen, plus we probably want some kind of display for the moves
			 * update the counter for # of nodes expanded
			 * log the total # of moves
			 * 
			 */
		}
		output.setNodesExpanded(nodesExpanded);
		output.print();
	}
	
	public static void main(String[] args)
    {
		Agent a = new Agent();
        System.out.println(args[0]);			//should be the filename
        System.out.println(args[1]);			//should be the # of the heuristic
        
        String filename = args[0];
        world.setBoard(filename);
        									/*TODO
        									 *  Map the Heuristic # to one of the 6 enums
        									 *  Make sure that main creates the agent with the right 2 inputs
        									 */
        world.print();
        System.out.println("Start:" + world.getStart().getX() + "," + world.getStart().getY());
        System.out.println("Goal:" + world.getGoal().getX() + "," + world.getGoal().getY());
        astar(Heuristic.H1, world.getStart(), world.getGoal());
        
        //analysis
        System.out.println("End of Main");
    }
	
	public static void makeFile(int row, int column) {
		Random rand = new Random();
		
		try {
			String filename = "config/board" + rand.nextInt(1000) + ".txt";
			File myObj = new File(filename);
			if (myObj.createNewFile()) {
		        System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
			generateWorld(filename, row, column);
			
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	public static void generateWorld (String filename, int row, int column) throws IOException{
		char[][] array = new char[row][column];
		int randomNum;
		
		for (int i = 0; i < array.length; i++) {
	        for (int j = 0; j < array[i].length; j++) {
	        	randomNum = ThreadLocalRandom.current().nextInt(1, 10);  
	            array[i][j] = (char) (randomNum + '0');
	        }
	    }
		
		int goalX = ThreadLocalRandom.current().nextInt(0, row);
		int goalY = ThreadLocalRandom.current().nextInt(0, column);
		int startX = ThreadLocalRandom.current().nextInt(0, row);
		int startY = ThreadLocalRandom.current().nextInt(0, column);
		
		array[goalX][goalY] = 'G';
		array[startX][startY] = 'S';
		
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter(filename));
		
		for (int i = 0; i < array.length; i++) {
	        for (int j = 0; j < array[i].length; j++) {
	        	if(j == array[i].length - 1) {
	        		outputWriter.write(array[i][j]);
	        	} else {
	        		outputWriter.write(array[i][j]+"\t");
	        	}
	        }
	        if(i != array.length - 1) {
	        	outputWriter.newLine();
	        }
	    }
		outputWriter.flush();  
		outputWriter.close();  
	}
}
