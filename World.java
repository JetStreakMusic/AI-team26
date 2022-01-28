
public class World {
	int[][] worldAsArray;
	
	public void setBoard(String input)
	{
		//read the lines
		//update worldasarray
		
		
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
