package pl.edu.agh.petrinet.model;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import pl.edu.agh.petrinet.algorithms.IncidenceMatrix;
import pl.edu.agh.petrinet.algorithms.ReachabilityGraph;

public class PetriGraph {

    private Graph<PetriVertex, PetriEdge> graph;

    private int placesCount;
    private int transitionsCount;

    private IncidenceMatrix incidenceMatrix;

    private ReachabilityGraph reachabilityGraph;

    private int[] m0;

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

    public IncidenceMatrix getIncidenceMatrix(){
        return this.incidenceMatrix;
    }

    public ReachabilityGraph getReachabilityGraph(){
        return this.reachabilityGraph;
    }

    private void initialize() {
        PetriPlace v1 = new PetriPlace(0, "PP0", 1);
        PetriPlace v2 = new PetriPlace(1, "PP1");
        PetriPlace v3 = new PetriPlace(2, "PP2", 1);
        PetriPlace v4 = new PetriPlace(3, "PP3");
        PetriPlace v5 = new PetriPlace(4, "PP4");

        addPlace(v1);
        addPlace(v2);
        addPlace(v3);
        addPlace(v4);
        addPlace(v5);

        PetriTransition t1 = new PetriTransition(0);
        PetriTransition t2 = new PetriTransition(1);
        PetriTransition t3 = new PetriTransition(2);
        PetriTransition t4 = new PetriTransition(3);

        addTransition(t1);
        addTransition(t2);
        addTransition(t3);
        addTransition(t4);

        addEdge(v1, t1);
        addEdge(t1, v2);
        addEdge(v2, t2);
        addEdge(t2, v5, 2);
        addEdge(t2, v1);
        addEdge(v5, t3, 2);
        addEdge(t3, v4);
        addEdge(v4, t4);
        addEdge(t4, v3);
        addEdge(v3, t3);

        computeM0();

        incidenceMatrix = new IncidenceMatrix(this);
        reachabilityGraph = new ReachabilityGraph(this);

    }

    private void computeM0(){
        m0 = new int[getPlacesCount()];

        for(PetriVertex pv : graph.getVertices()){
            if(pv instanceof PetriPlace){
                m0[pv.getId()] = ((PetriPlace) pv).getMarksersCount();
            }
        }
    }

    public int[] getM0(){
        return this.m0;
    }

}
