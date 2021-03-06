package pl.edu.agh.petrinet.algorithms;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.algorithms.shortestpath.ShortestPathUtils;
import edu.uci.ics.jung.graph.Graph;
import pl.edu.agh.petrinet.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Attributes {

	private PetriGraph graph;

	//attributes
	private Map<Integer, Integer> placesLimitation;
	private Integer netLimitation;
	private boolean isSafe;
	private boolean isConservative;
	private boolean isReversible;
	private boolean isNetLive;
	private Map<Integer, Boolean> placesLiveness;
	private Map<Integer, Boolean> transitionsLiveness;

	public Attributes(PetriGraph graph) {
		this.graph = graph;
		computeAttributes();
	}

	public Map<Integer, Integer> getPlacesLimitation(){
		return  placesLimitation;
	}

	public Map<Integer, Boolean> getPlacesLiveness(){
		return placesLiveness;
	}

	public Map<Integer, Boolean> getTransitionsLiveness(){
		return transitionsLiveness;
	}

	public Integer getPlaceLimitation(int placeId) {
		return placesLimitation.get(placeId);
	}

	public Integer getNetLimitation() {
		return netLimitation;
	}

	public boolean isNetSafe() {
		return isSafe;
	}

	public boolean isNetConservative() {
		return isConservative;
	}

	public boolean isNetConservative(int[] weightVector) {
		if (weightVector == null || weightVector.length != graph.getPlacesCount()) {
			return false;
		}

		return computeConservatismInTermsOfWeightWevtor(weightVector);
	}

	public boolean isNetReversible() {
		return isReversible;
	}

	public boolean isNetLive() {
		return isNetLive;
	}

	public boolean isTransitionsLive(int transitionId) {
		return transitionsLiveness.get(transitionId);
	}

	public boolean isPlaceLive(int placeId) {
		return placesLiveness.get(placeId);
	}

	@Override
	public String toString() {
		return "Attributes{ " +
				"placesLimitation=" + placesLimitation +
				", netLimitation=" + netLimitation +
				", isSafe=" + isSafe +
				", isConservative=" + isConservative +
				", isReversible=" + isReversible +
				", isNetLive=" + isNetLive +
				", placesLiveness=" + placesLiveness +
				", transitionsLiveness=" + transitionsLiveness +
				'}';
	}

	public String oneColumnString() {
		return "Attributes \n" +
				"placesLimitation=" + placesLimitationString() +
				"\nnetLimitation=" + netLimitationString() +
				"\nisSafe=" + isSafe +
				"\nisConservative=" + isConservative +
				"\nisReversible=" + isReversible +
				"\nisNetLive=" + isNetLive +
				"\nplacesLiveness=" + placesLiveness +
				"\ntransitionsLiveness=" + transitionsLiveness;
	}

	public String placesLimitationString() {
		StringBuilder builder = new StringBuilder();

		placesLimitation.forEach((id, value) -> {
			builder.append("P" + id + " = ");
			if (value == Integer.MAX_VALUE) {
				builder.append('\u221e');
			} else {
				builder.append(value);
			}

			builder.append(", ");
		});

		return builder.toString();
	}

	public String netLimitationString(){
		if(netLimitation == Integer.MAX_VALUE){
			return new StringBuilder().append('\u221e').toString();
		}
		else{
			return netLimitation.toString();
		}
	}

	private void computeAttributes() {
		computeLimitationAndSafety();
		computeConservatism();
		computeReversibility();
		computeLiveness();
		computeReversibility();
	}

	private void computeLimitationAndSafety() {
		//limitation of places
		placesLimitation = new HashMap<>(graph.getPlacesCount());

		for (PetriPlace place : graph.getAllPlaces()) {
			Optional<Integer> max = graph.getReachabilityGraph().getStateGraph().getVertices()
					.stream()
					.map(stateVertex -> stateVertex.getPlaceMarksCount(place.getId()))
					.max(Integer::compareTo);

			placesLimitation.put(place.getId(), max.orElseGet(() -> null));
		}

		//limitation of net
		netLimitation = placesLimitation.values().stream().max(Integer::compareTo).get();

		//safety
		isSafe = netLimitation.equals(1);
	}

	private void computeConservatism() {
		//conservatism
		List<Integer> sums = graph.getReachabilityGraph().getStateGraph().getVertices()
				.stream()
				.map(stateVertex -> IntStream.of(stateVertex.getPlaceMarksCounts()).sum())
				.collect(Collectors.toList());

		int first = sums.get(0);
		isConservative = sums.stream().allMatch(e -> e.equals(first));
	}

	private boolean computeConservatismInTermsOfWeightWevtor(int[] weightVector) {
		//conservatism in terms o weight vector
		List<Integer> sums = graph.getReachabilityGraph().getStateGraph().getVertices()
				.stream()
				.map(PetriStateVertex::getPlaceMarksCounts)
				.map(state -> {
					int[] newState = new int[state.length];

					for (int i = 0; i < state.length; i++) {
						newState[i] = state[i] * weightVector[i];
					}

					return newState;
				})
				.map(state -> IntStream.of(state).sum())
				.collect(Collectors.toList());

		int first = sums.get(0);
		return sums.stream().allMatch(e -> e.equals(first));
	}

	private void computeReversibility() {
		PetriStateGraph stateGraph = graph.getReachabilityGraph().getStateGraph();
		PetriStateVertex m0 = stateGraph.getM0();

		for (PetriStateVertex psv : graph.getReachabilityGraph().getStateGraph().getVertices()) {
			if (psv == m0) {
				continue;
			}

			if (ShortestPathUtils.getPath(stateGraph.getGraph(), new DijkstraShortestPath<>(stateGraph.getGraph()), psv, stateGraph.getM0()).isEmpty()) {
				isReversible = false;
				return;
			}
		}

		isReversible = true;
	}

	private void computeLiveness() {
		//transitions liveness
		transitionsLiveness = new HashMap<>(graph.getTransitionsCount());

		List<Integer> usedTransitions = graph.getReachabilityGraph().getStateGraph().getEdges()
				.stream()
				.map(PetriStateEdge::getTransition)
				.map(PetriTransition::getId)
				.collect(Collectors.toList());

		for (PetriTransition transition : graph.getAllTransitions()) {
			transitionsLiveness.put(transition.getId(), usedTransitions.contains(transition.getId()));
		}

		//places liveness
		placesLiveness = new HashMap<>(graph.getPlacesCount());

		List<int[]> states = graph.getReachabilityGraph().getStateGraph().getVertices()
				.stream()
				.map(PetriStateVertex::getPlaceMarksCounts)
				.collect(Collectors.toList());

		for (PetriPlace place : graph.getAllPlaces()) {
			boolean live = false;
			for (int[] state : states) {
				if (state[place.getId()] > 0) {
					live = true;
					break;
				}
			}

			placesLiveness.put(place.getId(), live);
		}

		//net liveness
		isNetLive = placesLiveness.values().stream().allMatch(e -> e.equals(true));
	}

}
