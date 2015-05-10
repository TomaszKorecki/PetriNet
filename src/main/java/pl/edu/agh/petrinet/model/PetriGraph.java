package pl.edu.agh.petrinet.model;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import pl.edu.agh.petrinet.algorithms.IncidenceMatrix;
import pl.edu.agh.petrinet.algorithms.ReachabilityGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PetriGraph {

    public enum Type {
        DEFAULT, PRIORYTY, TIME
    }

    private Type type = Type.DEFAULT;

    private Graph<PetriVertex, PetriEdge> graph;

    private HashMap<Integer, PetriPlace> places;
    private HashMap<Integer, PetriTransition> transitions;

    private IncidenceMatrix incidenceMatrix;

    private ReachabilityGraph reachabilityGraph;

    private int[] m0;

    public PetriGraph() {
        type = Type.DEFAULT;
        graph = new DirectedSparseMultigraph<>();
        places = new HashMap<>();
        transitions = new HashMap<>();
    }

    public PetriGraph(Type t) {
        type = t;
        graph = new DirectedSparseMultigraph<>();
        places = new HashMap<>();
        transitions = new HashMap<>();
    }

    public Graph<PetriVertex, PetriEdge> getGraph() {
        return graph;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setGraph(Graph<PetriVertex, PetriEdge> graph) {
        this.graph = graph;
    }

    public void addPlace(PetriPlace p) {
        graph.addVertex(p);
        places.put(p.getId(), p);
    }

    public boolean removePlace(PetriPlace p) {
        boolean result = graph.removeVertex(p);
        if (result) {
            places.remove(p);
        }
        return result;
    }

    public void addTransition(PetriTransition t) {
        graph.addVertex(t);
        transitions.put(t.getId(), t);
    }

    public boolean removeTransition(PetriTransition t) {
        boolean result = graph.removeVertex(t);
        if (result) {
            transitions.remove(t);
        }
        return result;
    }

    public void addEdge(PetriPlace p, PetriTransition t) {
        graph.addEdge(new PetriEdge(p, t), p, t);
    }

    public void addEdge(PetriTransition t, PetriPlace p) {
        graph.addEdge(new PetriEdge(t, p), t, p);
    }

    public void addEdge(PetriPlace p, PetriTransition t, int m) {
        graph.addEdge(new PetriEdge(p, t, m), p, t);
    }

    public void addEdge(PetriTransition t, PetriPlace p, int m) {
        graph.addEdge(new PetriEdge(t, p, m), t, p);
    }

    public void removeEdge(PetriEdge e) {
        graph.removeEdge(e);
    }

    public int getPlacesCount() {
        return places.size();
    }

    public int getTransitionsCount() {
        return transitions.size();
    }

    public IncidenceMatrix getIncidenceMatrix() {
        return this.incidenceMatrix;
    }

    public ReachabilityGraph getReachabilityGraph() {
        return this.reachabilityGraph;
    }

    public List<PetriPlace> getAllPlaces() {
        return new ArrayList<>(this.places.values());
    }

    public List<PetriTransition> getAllTransitions() {
        return new ArrayList<>(this.transitions.values());
    }

    private void computeM0() {
        m0 = new int[getPlacesCount()];

        for (PetriVertex pv : graph.getVertices()) {
            if (pv instanceof PetriPlace) {
                ((PetriPlace) pv).resetMarkersCount();
                m0[pv.getId()] = ((PetriPlace) pv).getMarksersCount();
            }
        }
    }

    public void compute() {
        computeM0();

        incidenceMatrix = new IncidenceMatrix(this);
        reachabilityGraph = new ReachabilityGraph(this);

    }

    public int[] getM0() {
        return this.m0;
    }

    public int[] getCurrentState() {
        int[] ret = new int[getPlacesCount()];

        for (PetriVertex pv : graph.getVertices()) {
            if (pv instanceof PetriPlace) {
                ret[pv.getId()] = ((PetriPlace) pv).getMarksersCount();
            }
        }
        return ret;
    }

    public Type getType() {
        return this.type;
    }

    public PetriPlace getPlace(int i) {
        if (places.containsKey(i)) {
            return places.get(i);
        }
        return null;
    }

    public PetriTransition getTransition(int i) {
        if (transitions.containsKey(i)) {
            return transitions.get(i);
        }
        return null;
    }

    public int getPlacesSmallestUniqId() {
        ArrayList<PetriPlace> sortedPlaces = new ArrayList<>(places.values());
        sortedPlaces.sort((o1, o2) -> ((Integer) o1.getId()).compareTo(o2.getId()));

        for (int i = 0; i < sortedPlaces.size(); i++) {
            if (sortedPlaces.get(i).getId() != i) return i;
        }
        return sortedPlaces.size();
    }

    public int getTransitionsSmallestUniqId() {
        ArrayList<PetriTransition> sortedTransitions = new ArrayList<>(transitions.values());
        sortedTransitions.sort((o1, o2) -> ((Integer) o1.getId()).compareTo(o2.getId()));

        for (int i = 0; i < sortedTransitions.size(); i++) {
            if (sortedTransitions.get(i).getId() != i) return i;
        }
        return sortedTransitions.size();
    }

}
