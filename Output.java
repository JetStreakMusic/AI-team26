import java.util.ArrayList;

public class Output {
	private int score = 0;
	private int numberOfMoves = 0;
	private int nodesExpanded = 0;
	private ArrayList<Moveset> totalMoves = new ArrayList<Moveset>();
	
	public void setScore(int n)
	{
		score = n;
	}
	public void setNodesExpanded(int n)
	{
		nodesExpanded = n;
	}
	public void setNumberOfMoves(int n)
	{
		numberOfMoves = n;
	}
	public void setTotalMoves(ArrayList<Moveset> n)
	{
		totalMoves = n;
	}
	public void print()
	{
		//print all 4 of those values ^
		System.out.println("The score of the path found: " + score);
		System.out.println("The number of actions required to reach the goal: " + numberOfMoves);
		System.out.println("The number of nodes expanded: " + nodesExpanded);
		System.out.println("The series of actions: ");
		for (Moveset move : totalMoves)
		{
			System.out.println(move.toString());
		}
	}
}
