import java.util.Objects;

public class Coordinate {
	private int x, y;
	public Coordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	
	@Override
	public boolean equals(Object anObject) { 
		Coordinate c2 = (Coordinate)anObject;
		return (this.x == c2.getX()) && (this.y == c2.getY());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

}
