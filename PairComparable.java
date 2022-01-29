import javafx.util.Pair;

public class PairComparable implements Comparable<PairComparable> {

	private Pair<Coordinate, Integer> myPair;
	
	public Pair<Coordinate, Integer> getPair()
	{
		return myPair;
	}
	public PairComparable(Coordinate c, int x)
	{
		myPair = new Pair<Coordinate, Integer>(c, x);
	}

	public int compareTo(PairComparable arg0) {
		int a = myPair.getValue();
		int b = arg0.getPair().getValue();
		return 0;
	}
	public Coordinate getKey() {
		return myPair.getKey();
	}
	public Integer getValue() {
		return myPair.getValue();
	}
}
