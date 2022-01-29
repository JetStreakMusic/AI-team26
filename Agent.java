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
	
	public static int heuristic1()
	{
		return 0;
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
		PriorityQueue<Pair<Coordinate,Integer>> frontierQueue = new PriorityQueue();
		Pair<Coordinate, Integer> initialPair = new Pair(start, 0);
		frontierQueue.add(initialPair);
		
		HashMap<Coordinate, ArrayList<Moveset>> came_from = new HashMap<Coordinate,ArrayList<Moveset>>();
		HashMap<Coordinate, Integer> cost_so_far = new HashMap<Coordinate,Integer>();
		cost_so_far.put(start, 0);
		came_from.put(start, new ArrayList());
		ArrayList<Moveset> options = new ArrayList<Moveset>();
		
		while(!frontierQueue.isEmpty())
		{
			Pair<Coordinate, Integer> currentPair = frontierQueue.poll();
			int currentX = currentPair.getKey().getX();
			int currentY = currentPair.getKey().getY();
			
			if (currentPair.getKey() == goal)
			{
				int totalNumberOfMoves = came_from.get(currentPair.getKey()).size();
				output.setNumberOfMoves(totalNumberOfMoves);
				output.setTotalMoves(came_from.get(currentPair.getKey()));
				break;
			}
			
			//if previous move was BASH, options = {forward}
			//otherwise its {all 4 moves}
			int finalMoveIndex = 0;
			if(!(currentPair.getKey() == start))
			{
				finalMoveIndex = came_from.get(currentPair.getKey()).lastIndexOf(options);
			}
			
			// if you're not on top of the 'start' coordinate && your last move was Bash....
			if((!(currentPair.getKey() == start)) && (came_from.get(currentPair.getKey()).get(finalMoveIndex) == Moveset.BASH))
			{
				options = new ArrayList<Moveset>();
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
				int new_cost = cost_so_far.get(currentPair.getKey()) + world.calculateGraphCost(currentPair.getKey(), m);
				Coordinate next = new Coordinate(0,0); /* calculate the next coordinate */
				if (m == Moveset.FORWARD)
				{
					if (direction == Facing.NORTH)
					{ next = new Coordinate(currentX, currentY-1);}
					else if (direction == Facing.SOUTH)
					{ next = new Coordinate(currentX, currentY+1);}
					else if (direction == Facing.WEST)
					{ next = new Coordinate(currentX-1, currentY);}
					else if (direction == Facing.EAST)
					{ next = new Coordinate(currentX+1, currentY);}
				}
				else if (m == Moveset.TURN_LEFT || m == Moveset.TURN_RIGHT)
				{
					next = currentPair.getKey();
				}
				else if (m == Moveset.BASH)
				{
					next = currentPair.getKey();
				}
				else if (m == Moveset.DEMOLISH)
				{
					next = currentPair.getKey();
				}
						
				// if next not in cost_so_far or new_cost < cost_so_far[next]:
				// if ther's no cost known		of		new cost is better
				if (!cost_so_far.containsKey(next) || new_cost < cost_so_far.get(next))
				{
					cost_so_far.put(next, new_cost);
					
					int heuristic_value = 0;
					if(h == Heuristic.H1)
					{
						heuristic_value = heuristic1();
					}
					// add more for each heuristic
					int priority = new_cost + heuristic_value;
					
					Pair<Coordinate,Integer> nextPair = new Pair(next, priority);
					ArrayList<Moveset> movesToCurrent = came_from.get(currentPair.getKey());
					movesToCurrent.add(m);
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
        System.out.println(world.getStart().getX() + "," + world.getStart().getY());
        astar(Heuristic.H1, world.getStart(), world.getGoal());
        
        //analysis
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
