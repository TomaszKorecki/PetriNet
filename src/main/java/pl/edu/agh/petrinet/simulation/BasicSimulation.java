package pl.edu.agh.petrinet.simulation;

import pl.edu.agh.petrinet.model.PetriGraph;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Abstract class representing simulation of Petri Net
 */
public abstract class BasicSimulation {

    /**
     * Petri Net graph
     */
    protected PetriGraph graph;

    /**
     * Is it automatic simulation flag
     */
    protected boolean isAutomatic;

    /**
     * Delay in automatic simulation in seconds
     */
    protected int delay;

    /**
     * List of possible transition from this state
     */
    protected List<Integer> possibleTransitions;

    /**
     * Current places marks count
     */
    protected int[] currentState;

    /**
     * Hook to transposition incidence matrix
     */
    protected int[][] tIncidenceMatrix;

    /**
     * Hook to transposition negatove matrix
     */
    protected int[][] tNegativeMatrix;

    /**
     * Constructor
     * @param g     Petri net graph
     */
    public BasicSimulation(PetriGraph g){
        graph = g;
        tIncidenceMatrix = g.getIncidenceMatrix().getTIncidenceMatrix();
        tNegativeMatrix = g.getIncidenceMatrix().getTNegativeMatrix();
        isAutomatic = true;
        possibleTransitions = new LinkedList<>();
        generatePossibleTransitions();
    }

    /**
     * Constrictor
     * @param g     Petri net graph
     * @param d     Delay in seconds
     */
    public BasicSimulation(PetriGraph g, int d){
        graph = g;
        tIncidenceMatrix = g.getIncidenceMatrix().getTIncidenceMatrix();
        tNegativeMatrix = g.getIncidenceMatrix().getTNegativeMatrix();
        delay = d * 1000;
        isAutomatic = true;
        possibleTransitions = new LinkedList<>();
        generatePossibleTransitions();
    }

    /**
     * Constructor
     * @param g     Petri net graph
     * @param ia    Is automatic simulation?
     */
    public BasicSimulation(PetriGraph g, boolean ia){
        graph = g;
        tIncidenceMatrix = g.getIncidenceMatrix().getTIncidenceMatrix();
        tNegativeMatrix = g.getIncidenceMatrix().getTNegativeMatrix();
        isAutomatic = ia;
        possibleTransitions = new LinkedList<>();
        generatePossibleTransitions();
    }

    /**
     * Constructor
     * @param g     Petri net graph
     * @param ia    Is automatic simulation?
     * @param d     Delay in seconds
     */
    public BasicSimulation(PetriGraph g, boolean ia, int d){
        graph = g;
        tIncidenceMatrix = g.getIncidenceMatrix().getTIncidenceMatrix();
        tNegativeMatrix = g.getIncidenceMatrix().getTNegativeMatrix();
        isAutomatic = ia;
        delay = d * 1000;
        possibleTransitions = new LinkedList<>();
        generatePossibleTransitions();
    }

    /**
     * Get possible transitions from current petri net state
     * @return
     */
    public List<Integer> getPossibleTransitions(){
        return this.possibleTransitions;
    }

    /**
     * Check wheter simulation is over - we are in leaf
     * @return      Is sumulation over?
     */
    public boolean isSimulationEnded(){
        return possibleTransitions.isEmpty();
    }

    /**
     * Set delay in automatic simulation
     * @param d     delay in seconds
     */
    public void setDelay(int d){
        this.delay = d * 1000;
    }

    /**
     * Abstract method generating possible transition
     */
    protected abstract void generatePossibleTransitions();

    /**
     * Automatic simulation method
     */
    public void automaticSimulate(){
        int step;
        Random r = new Random();
        while(isSimulationEnded() == false){
            // Get next transition from possible list
            step = r.nextInt(possibleTransitions.size());
            stepSimulate(step);

            try {
                // If sleep then sleep :)
                if(delay > 0){
                    Thread.sleep(delay);
                }
            } catch (InterruptedException e) {}
        }
    }

    /**
     * Abstract method doing one step from current state basing
     * on transition id
     * @param i     transition id
     * @return      Can do or done selected transition
     */
    public abstract boolean stepSimulate(int i);

}
