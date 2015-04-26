package pl.edu.agh.petrinet.model;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import pl.edu.agh.petrinet.algorithms.IncidenceMatrix;
import pl.edu.agh.petrinet.algorithms.ReachabilityGraph;

public class PetriGraph {

    private Graph<PetriVertex, PetriEdge> graph;

    private int placesCount;
    private int transitionsCount;

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

    public void addPlace(PetriPlace p){
        graph.addVertex(p);
        ++placesCount;
    }

    public void addTransition(PetriTransition t){
        graph.addVertex(t);
        ++transitionsCount;
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

    public int getPlacesCount(){return this.placesCount;}

    public int getTransitionsCount() {return this.transitionsCount;}

    private void initialize() {
        PetriPlace v1 = new PetriPlace(0, "PP0", 2);
        PetriPlace v2 = new PetriPlace(1, "PP1");
        PetriPlace v3 = new PetriPlace(2, "PP2");

        addPlace(v1);
        addPlace(v2);
        addPlace(v3);

        PetriTransition t1 = new PetriTransition(0);
        PetriTransition t2 = new PetriTransition(1);

        addTransition(t1);
        addTransition(t2);

        addEdge(v1, t1);
        addEdge(t1, v2);
        addEdge(v2, t2);
        addEdge(t2, v3);

        IncidenceMatrix ic = new IncidenceMatrix(this);
        ReachabilityGraph rg = new ReachabilityGraph(this, ic);
    }

}
