import java.util.Comparator;

public class RobotComparator implements Comparator<Robot> {

	@Override
	public int compare(Robot node1, Robot node2)
	{
		if (node1.getPriority() < node2.getPriority()) return -1;
        if (node1.getPriority() > node2.getPriority()) return 1;
        return 0;
    }
}
