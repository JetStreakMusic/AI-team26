import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


enum Moveset {
    FORWARD,
    TURN_LEFT,
    TURN_RIGHT,
    BASH;
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
	
	public static int heuristic5(Coordinate currCoordinate, Moveset move)
	{
		if (move != null && (move == Moveset.TURN_LEFT || move == Moveset.TURN_RIGHT)) {
			return heuristic4(currCoordinate) + 1;
		} else {
			return heuristic4(currCoordinate);
		}
	}
	
	//Heuristic 5 multiplied by 3
	public static int heuristic6(Coordinate currCoordinate)
	{
		return heuristic5(currCoordinate, null) * 3;
	}
	
	/*
	 * astar
	 * Should return something - probably a list of either Coordinates or moves
	 * Track the # of nodes expanded somehow (visited nodes) - counter
	 */
	public static void astar(int h, Coordinate start, Coordinate goal)
	{
		Output output = new Output();
		ArrayList<Moveset> totalMoves = new ArrayList();
		int nodesExpanded = 0;
		
		Queue<Robot> frontierQueue = new PriorityQueue<Robot>(10, new RobotComparator());
		Robot initialNode = new Robot(start, 0, Facing.NORTH, null);
		frontierQueue.add(initialNode);
		
		HashMap<Robot, Robot> came_from = new HashMap<Robot, Robot>();
		came_from.put(initialNode, null);
		HashMap<Robot, Integer> cost_so_far = new HashMap<Robot,Integer>();
		cost_so_far.put(initialNode, 0);
		
		ArrayList<Moveset> options = new ArrayList<Moveset>();
		
		Robot endNode = new Robot();
		
		while(!frontierQueue.isEmpty())
		{
			nodesExpanded++;
			options = new ArrayList<Moveset>();
			Robot currentNode = frontierQueue.poll();
			int currentX = currentNode.getCoordinate().getX();
			int currentY = currentNode.getCoordinate().getY();
			
			if (currentNode.getCoordinate().equals(goal))
			{
				endNode = currentNode;
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
			}
			
			for (Moveset m : options)
			{
				
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
				
				if(!(world.insideBounds(next)))
					continue;
				
				nextNode.setCoordinate(next);
				nextNode.setLastMove(m);
				int world_cost = world.calculateGraphCost(next, m);	
				new_cost = new_cost + world_cost;

				// if next not in cost_so_far or new_cost < cost_so_far[next]:
				// if ther's no cost known		of		new cost is better
				if (!(cost_so_far.containsKey(nextNode)) || new_cost < cost_so_far.get(nextNode))
				{
					
					int heuristic_value = 0;
					switch (h) {
			            case 1:
						{
							heuristic_value = heuristic1();
							break;
						}
			            case 2:
			            {
							heuristic_value = heuristic2(next);
							break;
						}
			            case 3:
			            {
							heuristic_value = heuristic3(next);
							break;
						}
			            case 4:
			            {
							heuristic_value = heuristic4(next);
							break;
						}
			            case 5:
			            {
							heuristic_value = heuristic5(next, m);
							break;
						}
			            case 6:
			            {
							heuristic_value = heuristic6(next);
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
					cost_so_far.put(nextNode, new_cost);
					frontierQueue.add(nextNode);
					came_from.put(nextNode, currentNode);
				}
			}
			
		}
		
		Robot current = endNode;
		ArrayList<Moveset> path = new ArrayList<Moveset>();
		
		while(!current.equals(initialNode))
		{
			if(came_from.get(current) == null) {break;}
			path.add(current.getLastMove());
//			System.out.println(current.getPriority() + "\t" + "Coordinate: " + current.getCoordinate().getX() + ","+current.getCoordinate().getY());
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
        int heuristic = Integer.valueOf(args[1]);
        								
        world.print();
        System.out.println("Start:" + world.getStart().getX() + "," + world.getStart().getY());
        System.out.println("Goal:" + world.getGoal().getX() + "," + world.getGoal().getY());
        
        long startTime = System.nanoTime();
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        
        astar(heuristic, world.getStart(), world.getGoal());
        
        long endTime   = System.nanoTime();
        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        
        long totalTime = endTime - startTime;
        long actualMemUsed=afterUsedMem-beforeUsedMem;
        
        System.out.println("The total time needed to run was " + (totalTime/1000000000f) + " seconds");
        System.out.println("The total memory used was " + (actualMemUsed/Math.pow(1024, 3)) + " GB");
        
//        makeFile(450, 450);
        
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
