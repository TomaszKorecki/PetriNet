package pl.edu.agh.petrinet.model;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import pl.edu.agh.petrinet.algorithms.IncidenceMatrix;
import pl.edu.agh.petrinet.algorithms.ReachabilityGraph;

import java.util.HashMap;

public class PetriGraph {

    public enum Type{
        DEFAULT, PRIORYTY, TIME
    }

    private Type type = Type.DEFAULT;

    private Graph<PetriVertex, PetriEdge> graph;

    private HashMap<Integer, PetriPlace> places;
    private HashMap<Integer, PetriTransition> transitions;

    private IncidenceMatrix incidenceMatrix;

    private ReachabilityGraph reachabilityGraph;

    private int[] m0;

    public PetriGraph(){
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

    public void setGraph(Graph<PetriVertex, PetriEdge> graph) {
        this.graph = graph;
    }

    public void addPlace(PetriPlace p){
        graph.addVertex(p);
        places.put(p.getId(), p);
    }

    public void addTransition(PetriTransition t){
        graph.addVertex(t);
        transitions.put(t.getId(), t);
    }

    public void addEdge(PetriPlace p, PetriTransition t){
        graph.addEdge(new PetriEdge(p, t), p, t);
    }

    public void addEdge(PetriTransition t, PetriPlace p){
        graph.addEdge(new PetriEdge(t, p), t, p);
    }

    public void addEdge(PetriPlace p, PetriTransition t, int m){
        graph.addEdge(new PetriEdge(p, t, m), p, t);
    }

    public void addEdge(PetriTransition t,PetriPlace p, int m){
        graph.addEdge(new PetriEdge(t, p, m), t, p);
    }

    public int getPlacesCount(){return places.size();}

    public int getTransitionsCount() {return transitions.size();}

    public IncidenceMatrix getIncidenceMatrix(){
        return this.incidenceMatrix;
    }

    public ReachabilityGraph getReachabilityGraph(){
        return this.reachabilityGraph;
    }

    private void computeM0(){
        m0 = new int[getPlacesCount()];

        for(PetriVertex pv : graph.getVertices()){
            if(pv instanceof PetriPlace){
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

    public int[] getM0(){
        return this.m0;
    }

    public int[] getCurrentState(){
        int[] ret = new int[getPlacesCount()];

        for(PetriVertex pv : graph.getVertices()){
            if(pv instanceof PetriPlace){
                ret[pv.getId()] = ((PetriPlace) pv).getMarksersCount();
            }
        }
        return ret;
    }

    public Type getType(){
        return this.type;
    }

    public PetriPlace getPlace(int i){
        if(places.containsKey(i)){
            return places.get(i);
        }
        return null;
    }

    public PetriTransition getTransition(int i){
        if(transitions.containsKey(i)){
            return transitions.get(i);
        }
        return null;
    }

}
