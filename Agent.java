import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
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
	private static int score = 0;
//	private static Facing direction;
	
	public Agent()
	{
		world = new World();
//		direction = Facing.NORTH;
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
//		PriorityQueue<PairComparable> frontierQueue = new PriorityQueue();
//		PairComparable initialPair = new PairComparable(start, 0);
//		frontierQueue.add(initialPair);
		
		Queue<Robot> frontierQueue = new PriorityQueue<Robot>(10, new RobotComparator());
		Robot initialNode = new Robot(start, 0, Facing.NORTH, null);
		frontierQueue.add(initialNode);
		
		HashMap<Robot, Robot> came_from = new HashMap<Robot, Robot>();
		came_from.put(initialNode, null);
		HashMap<Robot, Integer> cost_so_far = new HashMap<Robot,Integer>();
		cost_so_far.put(initialNode, 0);
//		HashMap<Coordinate, ArrayList<Moveset>> came_from = new HashMap<Coordinate,ArrayList<Moveset>>();
//		HashMap<Coordinate, Integer> cost_so_far = new HashMap<Coordinate,Integer>();
//		cost_so_far.put(start, 0);
//		came_from.put(start, new ArrayList());
		ArrayList<Moveset> options = new ArrayList<Moveset>();
		
		Robot endNode = new Robot();
		
		while(!frontierQueue.isEmpty())
		{
			options = new ArrayList<Moveset>();
			Robot currentNode = frontierQueue.poll();
			int currentX = currentNode.getCoordinate().getX();
			int currentY = currentNode.getCoordinate().getY();
			
			if (currentNode.getCoordinate().equals(goal))
			{
				endNode = currentNode;
//				int totalNumberOfMoves = came_from.get(currentNode.getCoordinate()).size();
//				output.setNumberOfMoves(totalNumberOfMoves);
//				totalMoves = came_from.get(currentNode.getCoordinate());
//				output.setTotalMoves(totalMoves);
//				for (Moveset move : totalMoves)
//				{
//					System.out.println(move.toString());
//				}
				score+=100;
				score -= cost_so_far.get(currentNode);
				output.setScore(score);
				break;
			}
			
			//if not on the start node, find the most recent move used
			Moveset finalMove = Moveset.FORWARD;
			if(currentNode.getLastMove() != null)
			{
				finalMove = currentNode.getLastMove();
			}
			
			// if you're not on top of the 'start' coordinate && your last move was Bash....
			// otherwise its {all 4 moves}
			if(finalMove == Moveset.BASH)
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
				
				int new_cost = cost_so_far.get(currentNode);
				Robot nextNode = new Robot();
				Coordinate next = new Coordinate(0,0); /* calculate the next coordinate */
				Facing direction = currentNode.getDirection();
				
				if (m == Moveset.FORWARD)
				{
					if (direction == Facing.NORTH)
					{ next = new Coordinate(currentX-1, currentY);}	
					else if (direction == Facing.SOUTH)
					{ next = new Coordinate(currentX+1, currentY);}
					else if (direction == Facing.WEST)
					{ next = new Coordinate(currentX, currentY-1);}
					else if (direction == Facing.EAST)
					{ next = new Coordinate(currentX, currentY+1);}
					
					nextNode.setDirection(direction);
					
				}
				else if (m == Moveset.TURN_LEFT || m == Moveset.TURN_RIGHT)
				{
					next = currentNode.getCoordinate();
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
					nextNode.setDirection(nextDirection);
//					astar_direction.put(next, nextDirection);
				}
				else if (m == Moveset.BASH)
				{
					if (direction == Facing.NORTH)
					{ next = new Coordinate(currentX-1, currentY);}	
					else if (direction == Facing.SOUTH)
					{ next = new Coordinate(currentX+1, currentY);}
					else if (direction == Facing.WEST)
					{ next = new Coordinate(currentX, currentY-1);}
					else if (direction == Facing.EAST)
					{ next = new Coordinate(currentX, currentY+1);}
					
					nextNode.setDirection(direction);
					
				}
				else if (m == Moveset.DEMOLISH)
				{
					next = currentNode.getCoordinate();
					nextNode.setDirection(direction);
				}
				
				if(!(world.insideBounds(next)))
					continue;
				
				nextNode.setCoordinate(next);
				
				new_cost = new_cost + world.calculateGraphCost(next, m);	
				// if next not in cost_so_far or new_cost < cost_so_far[next]:
				// if ther's no cost known		of		new cost is better
				if (!(cost_so_far.containsKey(nextNode)) || new_cost < cost_so_far.get(nextNode))
				{
					
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
					
//					PairComparable nextPair = new PairComparable(next, priority);
					nextNode.setPriority(priority);
					nextNode.setLastMove(m);
					cost_so_far.put(nextNode, new_cost);
					frontierQueue.add(nextNode);
					came_from.put(nextNode, currentNode);
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
		
		Robot current = endNode;
		ArrayList<Moveset> path = new ArrayList<Moveset>();
		
		while(!current.equals(initialNode))
		{
			if(came_from.get(current) == null) {break;}
			path.add(current.getLastMove());
			current = came_from.get(current);
		}
		
		Collections.reverse(path);
		
		output.setTotalMoves(path);
		output.setNumberOfMoves(path.size());
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
        
//        makeFile(6, 6);
        
        //analysis
        System.out.println("End of Main");
    }
	
	public static void makeFile(int row, int column) 
	{
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
	
	public static void generateWorld (String filename, int row, int column) throws IOException
	{
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
