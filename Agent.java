import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
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

public class Agent {
	private static World world;
	private int score = 0;
	
	public Agent()
	{
		this.world = new World();
		
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
		
		PriorityQueue<Pair<Coordinate,Integer>> frontierQueue = new PriorityQueue();
		Pair<Coordinate, Integer> initialPair = new Pair(start, 0);
		frontierQueue.add(initialPair);
		
		HashMap<Coordinate, Moveset> came_from = new HashMap<Coordinate,Moveset>();
		HashMap<Coordinate, Integer> cost_so_far = new HashMap<Coordinate,Integer>();
		
		ArrayList<Moveset> options = new ArrayList<Moveset>();
		
		while(!frontierQueue.isEmpty())
		{
			Pair<Coordinate, Integer> current = frontierQueue.poll();
			
			if (current.getKey() == goal)
			{
				//end
			}
			
			for (Moveset m : options)
			{
				int new_cost = cost_so_far.get(current) + world.calculateGraphCost(current.getKey(), m);
				Coordinate next = new Coordinate(0,0); /* calculate the next coordinate */
						
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
					came_from.put(next, m);
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
        astar(Heuristic.H1, world.getStart(), world.getGoal());
        
        //analysis
    }
}
