package pl.edu.agh.petrinet.simulation;

import pl.edu.agh.petrinet.model.PetriGraph;

/**
 * Simulation of default Petri Net
 */
public class DefaultSimulation extends BasicSimulation {

    /**
     * Constructor
     * @param g     Petri net graph
     */
    public DefaultSimulation(PetriGraph g) {
        super(g);
    }

    /**
     * Constrictor
     * @param g     Petri net graph
     * @param d     Delay in seconds
     */
    public DefaultSimulation(PetriGraph g, int d) {
        super(g, d);
    }

    /**
     * Constructor
     * @param g     Petri net graph
     * @param ia    Is automatic simulation?
     */
    public DefaultSimulation(PetriGraph g, boolean ia) {
        super(g, ia);
    }

    /**
     * Constructor
     * @param g     Petri net graph
     * @param ia    Is automatic simulation?
     * @param d     Delay in seconds
     */
    public DefaultSimulation(PetriGraph g, boolean ia, int d) {
        super(g, ia, d);
    }

    /**
     * Generating possible transitions from current state
     * Default Petri net simulation is based on the fact if transition is alive or not
     */
    @Override
    protected void generatePossibleTransitions() {
        currentState = graph.getCurrentState();

        possibleTransitions.clear();

        boolean canDoTransition;
        for(int i =0 ; i< graph.getTransitionsCount(); i++){
            canDoTransition = true;
            for(int j=0;j<graph.getPlacesCount(); j++){
                // If we do not have enough marks transition is dead
                if(currentState[j] < tNegativeMatrix[i][j]){
                    canDoTransition = false;
                    break;
                }
            }
            // If transition is alive add it as possible
            if(canDoTransition){
                possibleTransitions.add(i);
            }
        }
    }

    /**
     * Do one step simulation
     * @param i     transition id
     * @return      Done step?
     */
    @Override
    public boolean stepSimulate(int i) {
        if(possibleTransitions.contains(i)){
            for(int j = 0; j < tIncidenceMatrix[i].length; j++){
                graph.getPlace(j).changeMarkersCount(tIncidenceMatrix[i][j]);
            }
            generatePossibleTransitions();
            return true;
        }
        else{
            return false;
        }
    }
}
