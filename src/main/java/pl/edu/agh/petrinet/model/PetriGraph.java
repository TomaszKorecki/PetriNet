package pl.edu.agh.petrinet.model;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * Created by Tomasz on 4/19/2015.
 */
public class PetriGraph {
    private Graph graph;

    public PetriGraph(){
        graph = new SparseMultigraph<PetriPlace, PetriTransition>();


    }

    public Graph getGraph(){
        return graph;
    }

    public void addTransitionVertex(PetriTransition petriTransition){
        graph.addVertex(petriTransition);
    }

    public void addPlaceVertex(PetriPlace petriPlace){
        graph.addVertex(petriPlace);
    }

    public void addEdge(PetriEdge petriEdge, PetriVertex begin, PetriVertex end){
        graph.addEdge(petriEdge, begin, end, EdgeType.DIRECTED);
    }
}
