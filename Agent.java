import java.util.PriorityQueue;

enum Moveset {
    FORWARD,
    TURN,
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
	private World world;
	
	public Agent()
	{
		World world = new World();
	}
	
	
	public void astar(Heuristic h)
	{
		
		PriorityQueue pq = new PriorityQueue();
		
		/*TODO 
		 * choose the right heuristic function
		 * A* implementation
		 * logging the moves chosen, plus we probably want some kind of display for the moves
		 * log the # of nodes expanded
		 * log the total # of moves
		 * 
		 */
		
	}
	
	public static void main(String[] args)
    {
        System.out.println(args[0]);			//should be the filename
        System.out.println(args[1]);			//should be the # of the heuristic
        									/*TODO
        									 *  Map the Heuristic # to one of the 6 enums
        									 *  Make sure that main creates the agent with the right 2 inputs
        									 */
    }
}
