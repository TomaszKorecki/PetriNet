package pl.edu.agh.petrinet.model;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class PetriGraph {

    private Graph<PetriVertex, PetriEdge> graph;

    public PetriGraph() {
        graph = new DirectedSparseMultigraph<>();
        initialize();
    }

    public Graph<PetriVertex, PetriEdge> getGraph() {
        return graph;
    }

    public void setGraph(Graph<PetriVertex, PetriEdge> graph) {
        this.graph = graph;
    }

    private void initialize() {
        PetriVertex v1 = new PetriPlace(1, "PP1");
        PetriVertex v2 = new PetriPlace(2, "PP2");
        PetriVertex v3 = new PetriPlace(3, "PP3");

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);

        graph.addEdge(new PetriEdge(v1, v2), v1, v2);
        graph.addEdge(new PetriEdge(v2, v3), v2, v3);
        graph.addEdge(new PetriEdge(v3, v1), v3, v1);

    }

}
