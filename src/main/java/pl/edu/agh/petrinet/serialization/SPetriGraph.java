package pl.edu.agh.petrinet.serialization;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import pl.edu.agh.petrinet.model.PetriPlace;
import pl.edu.agh.petrinet.model.PetriTransition;

import java.util.List;

/**
 * Temporary class for Petri net graph serialization
 */
@Root(name="PetriGraph")
public class SPetriGraph {

    /**
     * Type of graph
     */
    @Element(name = "GraphType")
    public String type;

    /**
     * List of places
     */
    @ElementList(name = "Places")
    public List<PetriPlace> places;

    /**
     * List of transitions
     */
    @ElementList(name = "Transitions")
    public List<PetriTransition> transitions;

    /**
     * List of edges
     */
    @ElementList(name = "Edges")
    public List<SPetriEdge> edges;

}
