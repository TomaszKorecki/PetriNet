package pl.edu.agh.petrinet.model;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

/**
 * Created by rakiop on 26.04.15.
 */
public class PetriStateGraph {

    private Graph<PetriStateVertex, PetriStateEdge> graph;

    public PetriStateGraph(){
        graph = new DirectedSparseMultigraph<>();
    }

    public void addM0(PetriStateVertex m0){
        graph.addVertex(m0);
    }

    public void addState(PetriStateVertex fromState, PetriStateVertex nextState, PetriTransition t){
        graph.addVertex(nextState);
        graph.addEdge(new PetriStateEdge(fromState, nextState, t), fromState, nextState);
    }

    public void addExistingState(PetriStateVertex fromState, PetriStateVertex nextState, PetriTransition t){
        graph.addEdge(new PetriStateEdge(fromState, nextState, t), fromState, nextState);
    }

}
