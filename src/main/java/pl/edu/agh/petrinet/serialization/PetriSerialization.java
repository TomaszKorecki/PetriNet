package pl.edu.agh.petrinet.serialization;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import pl.edu.agh.petrinet.model.PetriEdge;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriPlace;
import pl.edu.agh.petrinet.model.PetriTransition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Petri net serialization
 */
public class PetriSerialization {

    /**
     * Petri net graph
     */
    protected PetriGraph graph;

    /**
     * Default Constructor
     */
    public PetriSerialization(){

    }

    /**
     * Constructor
     * @param g     Petri net graph
     */
    public PetriSerialization(PetriGraph g){
        this.graph = g;
    }

    /**
     * Set Petri net graph
     * @param g     Petri net graph
     */
    public void setGraph(PetriGraph g){
        this.graph = g;
    }


    public void serialize(String filename) throws Exception{
        serialize(new File(filename));
    }

    /**
     * Serialize petri net to given xml file
     * @param file          XML file path
     * @throws Exception
     */
    public void serialize(File file) throws Exception {
        // We have something to serialize
        if(this.graph == null){
            throw new Exception("No graph...");
        }

        Serializer s = new Persister();

        // Create Serializable object
        SPetriGraph sp = new SPetriGraph();

        // Set Petri net type
        sp.type = graph.getType() == PetriGraph.Type.PRIORYTY ? "Priority" :
                    graph.getType() == PetriGraph.Type.TIME ? "Time" : "Default";

        // Add places
        sp.places = graph.getAllPlaces();

        // Add transitions
        sp.transitions = graph.getAllTransitions();

        // Create serializable list of edges
        List<SPetriEdge> edges = new ArrayList<>();
        for(PetriEdge e : graph.getGraph().getEdges()){
            SPetriEdge spe = new SPetriEdge(
                    (e.getV1() instanceof PetriPlace ? "Place" : "Transition"),
                    e.getV1().getId(),
                    (e.getV2() instanceof PetriPlace ? "Place" : "Transition"),
                    e.getV2().getId(),
                    e.getMarkersCount()
            );
            edges.add(spe);
        }
        sp.edges = edges;

        // Write to file
        s.write(sp, file);
    }

    public PetriGraph deserialize(String filename) throws Exception{
        return deserialize(new File(filename));
    }

    /**
     * Deserrialize PetriNet Graph from XML file
     * @param file      XML file path
     * @return          PetriNet Graph
     * @throws Exception
     */
    public PetriGraph deserialize(File file) throws Exception {
        // Clear current graph
        this.graph = null;

        // Read serialized graph
        SPetriGraph spg;
        Serializer s = new Persister();

        spg = s.read(SPetriGraph.class, file);

        // If no error occurred
        if(spg != null){
            // Create new Petri Net Graph
            this.graph = new PetriGraph();

            // Set graph type
            this.graph.setType(spg.type.equals("Time") ? PetriGraph.Type.TIME :
                    (spg.type.equals("Priority") ? PetriGraph.Type.PRIORYTY : PetriGraph.Type.DEFAULT));

            // Add places
            for(PetriPlace p : spg.places){
                p.resetMarkersCount();
                this.graph.addPlace(p);
            }

            // Add transitions
            for(PetriTransition t : spg.transitions){
                t.setType(this.graph.getType());
                this.graph.addTransition(t);
            }

            // Add Edges
            for(SPetriEdge spe : spg.edges){
                if(spe.getVertexOneType().equals("Place")){
                    this.graph.addEdge(this.graph.getPlace(spe.getVertexOneId()), this.graph.getTransition(spe.getVertexTwoId()), spe.getMarkersCount());
                }
                else{
                    this.graph.addEdge(this.graph.getTransition(spe.getVertexOneId()), this.graph.getPlace(spe.getVertexTwoId()), spe.getMarkersCount());
                }

            }

            this.graph.resetAllVerticesSpecialValues();

        }

        return this.graph;

    }

}
