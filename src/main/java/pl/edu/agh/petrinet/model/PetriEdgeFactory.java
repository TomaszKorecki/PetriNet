package pl.edu.agh.petrinet.model;

import org.apache.commons.collections15.Factory;

/**
 * Created by Tomasz on 5/2/2015.
 */
public class PetriEdgeFactory implements Factory<PetriEdge> {

    private PetriGraph petriGraph;

    public PetriEdgeFactory(PetriGraph petriGraph){
        this.petriGraph = petriGraph;
    }

    public PetriEdge create() {
        return new PetriEdge(petriGraph.getPlace(0), petriGraph.getTransition(0));
    }
}
