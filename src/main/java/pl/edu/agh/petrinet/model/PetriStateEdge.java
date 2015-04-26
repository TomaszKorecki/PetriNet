package pl.edu.agh.petrinet.model;

/**
 * Created by rakiop on 26.04.15.
 */
public class PetriStateEdge {

    private PetriStateVertex fromState;
    private PetriStateVertex nextState;
    private PetriTransition transition;

    public PetriStateEdge(PetriStateVertex from, PetriStateVertex next, PetriTransition t){
        this.fromState = fromState;
        this.nextState = next;
        this.transition = t;
    }

    @Override
    public String toString() {
        return transition.getName();
    }
}
