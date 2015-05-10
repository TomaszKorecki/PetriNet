package test;

import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriPlace;
import pl.edu.agh.petrinet.model.PetriTransition;
import pl.edu.agh.petrinet.serialization.PetriSerialization;

/**
 * Created by rakiop on 10.05.15.
 */
public class SimulateSerialize {

    public static void main(String[] args){
        PetriGraph graph = createGraph();
        graph.compute();


        PetriSerialization ps = new PetriSerialization(graph);
        try{
            ps.serialize("tmp_serialize.xml");

            PetriGraph g2 = ps.deserialize("tmp_serialize.xml");

            g2.compute();

            g2.getGraph();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static PetriGraph createGraph(){
        PetriGraph graph = new PetriGraph();
        PetriPlace v1 = new PetriPlace(0, "PP0", 1);
        PetriPlace v2 = new PetriPlace(1, "PP1");
        PetriPlace v3 = new PetriPlace(2, "PP2", 1);
        PetriPlace v4 = new PetriPlace(3, "PP3");
        PetriPlace v5 = new PetriPlace(4, "PP4");

        graph.addPlace(v1);
        graph.addPlace(v2);
        graph.addPlace(v3);
        graph.addPlace(v4);
        graph.addPlace(v5);

        PetriTransition t1 = new PetriTransition(0, graph.getType());
        PetriTransition t2 = new PetriTransition(1, graph.getType());
        PetriTransition t3 = new PetriTransition(2, graph.getType());
        PetriTransition t4 = new PetriTransition(3, graph.getType());

        graph.addTransition(t1);
        graph.addTransition(t2);
        graph.addTransition(t3);
        graph.addTransition(t4);

        graph.addEdge(v1, t1);
        graph.addEdge(t1, v2);
        graph.addEdge(v2, t2);
        graph.addEdge(t2, v5, 2);
        graph.addEdge(t2, v1);
        graph.addEdge(v5, t3, 2);
        graph.addEdge(t3, v4);
        graph.addEdge(v4, t4);
        graph.addEdge(t4, v3);
        graph.addEdge(v3, t3);
        return graph;
    }



}
