package pl.edu.agh.petrinet.model;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

/**
 * Representation of Reachability/Coverability Petri Graph
 */
public class PetriStateGraph {

    /**
     * List of states and transitions
     */
    private Graph<PetriStateVertex, PetriStateEdge> graph;

    /**
     * Constrictor
     */
    public PetriStateGraph(){
        graph = new DirectedSparseMultigraph<>();
    }

    /**
     * Add first state (M0)
     * @param m0    first state of PetriNet
     */
    public void addM0(PetriStateVertex m0){
        graph.addVertex(m0);
    }

    /**
     * Add next state
     * @param fromState     previous state
     * @param nextState     new state
     * @param t             used transition
     */
    public void addState(PetriStateVertex fromState, PetriStateVertex nextState, PetriTransition t){
        graph.addVertex(nextState);
        graph.addEdge(new PetriStateEdge(fromState, nextState, t), fromState, nextState);
    }

    /**
     * Close loop - add existing state
     * @param fromState     previous state
     * @param nextState     new state
     * @param t             used transition
     */
    public void addExistingState(PetriStateVertex fromState, PetriStateVertex nextState, PetriTransition t){
        graph.addEdge(new PetriStateEdge(fromState, nextState, t), fromState, nextState);
    }

}
