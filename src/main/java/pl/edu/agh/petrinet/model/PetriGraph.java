package pl.edu.agh.petrinet.model;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import pl.edu.agh.petrinet.algorithms.IncidenceMatrix;
import pl.edu.agh.petrinet.algorithms.ReachabilityGraph;

import java.util.ArrayList;
import java.util.Collection;
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

    private List<String> validationResult;
    private boolean graphIsValid;

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
            System.out.println(places.size());
            int lastPlace = places.size();
            places.remove(p.getId());
            for(int i = p.getId() + 1; i < lastPlace; i++){
                System.out.println("Trying to drcrease id for element with id  " + i);
                PetriPlace pp = places.remove(i);
                pp.setId(i - 1);
                places.put(pp.getId(), pp);
                //PetriPlace futherPetiPlace = places.get(i);
                //futherPetiPlace.setId(futherPetiPlace.getId() - 1);
            }
            System.out.println(places.size());
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
            int lastTransition = transitions.size();
            transitions.remove(t.getId());
            for(int i = t.getId()+1; i < lastTransition; i++){
                PetriTransition pt = transitions.remove(i);
                pt.setId(i - 1);
                transitions.put(pt.getId(), pt);
            }
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

    public HashMap<Integer, PetriTransition> getTransitionsHash(){
        return this.transitions;
    }

    public HashMap<Integer, PetriPlace> getPlacesHash(){
        return this.places;
    }

    private void computeM0() {
        m0 = new int[getPlacesCount()];

        for (PetriPlace pv : this.places.values()) {
            pv.resetMarkersCount();
            m0[pv.getId()] = pv.getMarksersCount();
        }
    }

    /**
     * Validate graph if all places and transitions are correct
     */
    public void validateGraph(){
        validationResult = new ArrayList<>();
        Collection<PetriEdge> edges = graph.getEdges();

        // Temporary variables
        boolean isValid;
        boolean isValid2;

        // Check places
        for(PetriPlace pp : places.values()){
            isValid = false;    // Assume it is wrong place
            for(PetriEdge pe : edges){
                if(pe.getV1() == pp || pe.getV2() == pp){ // If there is way out or in from place it is valid
                    isValid = true;
                    break;
                }
            }
            // Add note if invalid
            if(!isValid){
                validationResult.add("Place " + pp.getName() + " has no incoming or outcoming edges.");
            }
        }

        //Check Transitions
        for(PetriTransition pt : transitions.values()){
            isValid = false;        // Assume has no outcoming edge
            isValid2 = false;       // Assume has no incomming edge
            for(PetriEdge pe : edges){
                if(pe.getV1() == pt){       // Check outcomming
                    isValid = true;
                }
                else if(pe.getV2() == pt){  // Check incomming
                    isValid2 = true;
                }

                if(isValid && isValid2){    // Must have both valid
                    break;
                }
            }

            // If has no outcoming
            if(!isValid){
                validationResult.add("Transition " + pt.getName() + " has no outcoming edges.");
            }

            // If has no incoming
            if(!isValid2){
                validationResult.add("Transition " + pt.getName() + " has no incoming edges.");
            }
        }

        // Graph is valid if has no notes
        graphIsValid = validationResult.isEmpty();
    }

    public void compute() {
        // Validate graph - invalide graph can produce endless loops
        validateGraph();

        if(graphIsValid == false){
            return;
        }

        computeM0();

        System.out.println("m0 computed");

        incidenceMatrix = new IncidenceMatrix(this);
        reachabilityGraph = new ReachabilityGraph(this);
    }

    public int[] getM0() {
        return this.m0;
    }

    public int[] getCurrentState() {
        int[] ret = new int[getPlacesCount()];

        for (PetriPlace pv : this.places.values()) {
            ret[pv.getId()] = pv.getMarksersCount();
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

    public boolean isGraphIsValid(){
        return graphIsValid;
    }

    public List<String> getValidationResults(){
        return validationResult;
    }

    public void resetAllPlacesMarkersCount(){
        places.forEach((integer, petriPlace) -> petriPlace.resetMarkersCount());
    }

    public String toString(){
        return "Places count: " + places.size() + "    Transitions count: " + transitions.size();
    }

}
