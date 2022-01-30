import java.util.Objects;

public class Robot {
	
	private Coordinate coord;
	private int priority;
	private Facing direction;
	private Moveset lastMove;
	
	public Robot (Coordinate coord, int priority, Facing direction, Moveset lastMove) 
	{
		this.coord = coord;
		this.priority = priority;
		this.direction = direction;
		this.lastMove = lastMove;
	}

	public Robot() {}

	/**
	 * @return the coord
	 */
	public Coordinate getCoordinate() {
		return coord;
	}

	/**
	 * @param coord the coord to set
	 */
	public void setCoordinate(Coordinate coord) {
		this.coord = coord;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the direction
	 */
	public Facing getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Facing direction) {
		this.direction = direction;
	}

	/**
	 * @return the lastMove
	 */
	public Moveset getLastMove() {
		return lastMove;
	}

	/**
	 * @param lastMove the lastMove to set
	 */
	public void setLastMove(Moveset lastMove) {
		this.lastMove = lastMove;
	}

	@Override
	public int hashCode() {
		return Objects.hash(coord, direction, lastMove, priority);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Robot other = (Robot) obj;
		if(this.lastMove == null && other.lastMove == null) {return true;}
		return coord.equals(other.coord) && direction == other.direction && lastMove == other.lastMove && priority == other.priority;
	}
	
	

}
