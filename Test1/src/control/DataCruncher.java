package control;

import java.util.List;

import entity.Defect;
import entity.Effort;

public class DataCruncher {

	// XXX Note to future author: what this class does is rather vague and up to group discussion.

	/**
	 * A struct to hold the total and average time statistics.
	 */
	public class TimeStats
	{
		public double totalTime;
		public double averageTime;
	}

	/**
	 * Compute time statistics on a list of effort.
	 * @param le given effort
	 * @return TimeStats for the given effort
	 * @see TimeStats
	 */
	public TimeStats computeTimeStats(List<Effort> le)
	{
		// TODO
		return null;
	}

	/**
	 * Find the number of defects that are associated to some project.
	 * @param ld given defects
	 * @param le given effort
	 * @return Number of defects on a given project
	 */
	public int numDefectsOnProject(List<Defect> ld, List<Effort> le)
	/* XXX Note to the future author: I don't know how those parameters can do what the function says,
	 * the parameters should probably change as you see fit. */
	{
		// TODO
		return -1;
	}
}
