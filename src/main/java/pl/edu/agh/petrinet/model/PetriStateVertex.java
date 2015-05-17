package pl.edu.agh.petrinet.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Vertex of Reachability/Coverability Petri Graph
 */
public class PetriStateVertex {

	/**
	 * Current Petri Graph status
	 */
	private int[] placeStatus;

	/**
	 * Level of vertex
	 */
	private int level;

	/**
	 * Route from root of this graph
	 */
	private List<Integer> route;

	/**
	 * Contructor
	 *
	 * @param status current status
	 * @param level  current level
	 */
	public PetriStateVertex(int[] status, int level) {
		this.placeStatus = status;
		this.level = level;
		route = new LinkedList<>();
	}

	/**
	 * Constructor
	 *
	 * @param status current status
	 * @param level  current level
	 * @param route  route from root
	 */
	public PetriStateVertex(int[] status, int level, List<Integer> route) {
		this.placeStatus = status;
		this.level = level;
		this.route = new LinkedList<>();

		for (Integer id : route) {
			this.route.add(id);
		}
	}

	/**
	 * Get current level
	 *
	 * @return current level
	 */
	public int getLevel() {
		return this.level;
	}

	/**
	 * Get status of *i* petri net place
	 *
	 * @param i id of petri net place
	 * @return numbers of marks
	 */
	public int getPlaceMarksCount(int i) {
		return placeStatus[i];
	}

	/**
	 * Get current petri net status
	 *
	 * @return current petri net status
	 */
	public int[] getPlaceMarksCounts() {
		return placeStatus;
	}

	/**
	 * Add next transition to route
	 *
	 * @param tId transition id
	 */
	public void addToRoute(int tId) {
		route.add(tId);
	}

	/**
	 * Get route to this state
	 *
	 * @return
	 */
	public List<Integer> getRoute() {
		return route;
	}

	/**
	 * Check for equality with another state
	 *
	 * @param obj PetriStateVertex object
	 * @return Is equal or not
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PetriStateVertex == false)
			return false;

		int[] state = ((PetriStateVertex) obj).getPlaceMarksCounts();
		for (int i = 0; i < placeStatus.length; i++) {
			if (state[i] != placeStatus[i])
				return false;
		}
		return true;
	}

	/**
	 * Print current status
	 *
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");

		for (int i = 0; i < placeStatus.length; i++) {
			if (placeStatus[i] == Integer.MAX_VALUE) {
				builder.append('\u221e' + ", ");
			} else {
				builder.append(placeStatus[i] + ", ");
			}
		}

		builder.append("]");
		return builder.toString();
	}
}
