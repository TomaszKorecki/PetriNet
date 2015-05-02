package pl.edu.agh.petrinet.model;

import org.apache.commons.collections15.Factory;

/**
 * Created by Tomasz on 5/2/2015.
 */
public class PetriVertexFactory implements Factory<PetriVertex> {

    private PetriGraph petriGraph;

    public PetriVertexFactory(PetriGraph petriGraph){
        this.petriGraph = petriGraph;
    }

    @Override
    public PetriVertex create() {
        return new PetriPlace(50, "PetriPlace");
    }
}
