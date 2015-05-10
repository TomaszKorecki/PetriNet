package pl.edu.agh.petrinet.model;

/**
 * Edge in Reachability/Coverability Petri Graph
 */
public class PetriStateEdge {

    /**
     * Previous state
     */
    private PetriStateVertex fromState;

    /**
     * Next state
     */
    private PetriStateVertex nextState;

    /**
     * Used transition
     */
    private PetriTransition transition;

    /**
     * Constructor
     *
     * @param from previous state
     * @param next next state
     * @param t    used transition
     */
    public PetriStateEdge(PetriStateVertex from, PetriStateVertex next, PetriTransition t) {
        this.fromState = from;
        this.nextState = next;
        this.transition = t;
    }

    /**
     * Return previous state
     *
     * @return previous state
     */
    public PetriStateVertex getFromState() {
        return fromState;
    }

    /**
     * Return next state
     *
     * @return next state
     */
    public PetriStateVertex getNextState() {
        return nextState;
    }

    /**
     * Return used transition
     *
     * @return used transition
     */
    public PetriTransition getTransition() {
        return transition;
    }

    /**
     * Return name of used transition
     *
     * @return
     */
    @Override
    public String toString() {
        return transition.getName();
    }
}
