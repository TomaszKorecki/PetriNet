package pl.edu.agh.petrinet.model;

/**
 * Created by Tomasz on 5/10/2015.
 */
public class PetriGraphUtils {

    public static PetriGraph createTestPetriGraph(){
        PetriGraph petriGraph = new PetriGraph();

        PetriPlace v1 = new PetriPlace(0, "PP0", 1);
        PetriPlace v2 = new PetriPlace(1, "PP1");
        PetriPlace v3 = new PetriPlace(2, "PP2", 1);
        PetriPlace v4 = new PetriPlace(3, "PP3");
        PetriPlace v5 = new PetriPlace(4, "PP4");

        petriGraph.addPlace(v1);
        petriGraph.addPlace(v2);
        petriGraph.addPlace(v3);
        petriGraph.addPlace(v4);
        petriGraph.addPlace(v5);

        PetriTransition t1 = new PetriTransition(0, petriGraph.getType());
        PetriTransition t2 = new PetriTransition(1, petriGraph.getType());
        PetriTransition t3 = new PetriTransition(2, petriGraph.getType());
        PetriTransition t4 = new PetriTransition(3, petriGraph.getType());

        petriGraph.addTransition(t1);
        petriGraph.addTransition(t2);
        petriGraph.addTransition(t3);
        petriGraph.addTransition(t4);

        petriGraph.addEdge(v1, t1);
        petriGraph.addEdge(t1, v2);
        petriGraph.addEdge(v2, t2);
        petriGraph.addEdge(t2, v5, 2);
        petriGraph.addEdge(t2, v1);
        petriGraph.addEdge(v5, t3, 2);
        petriGraph.addEdge(t3, v4);
        petriGraph.addEdge(v4, t4);
        petriGraph.addEdge(t4, v3);
        petriGraph.addEdge(v3, t3);

        return petriGraph;
    }
}
