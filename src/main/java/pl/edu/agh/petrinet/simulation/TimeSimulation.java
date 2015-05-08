package pl.edu.agh.petrinet.simulation;

import org.apache.commons.lang3.ArrayUtils;
import pl.edu.agh.petrinet.model.PetriGraph;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Simulation of time based petri net
 */
public class TimeSimulation extends BasicSimulation {

    private int[] currentTimes;

    /**
     * Constructor
     * @param g     Petri net graph
     */
    public TimeSimulation(PetriGraph g) throws Exception {
        super(g);
        // Check type of Petri net
        if(g.getType() != PetriGraph.Type.TIME){
            throw new Exception("This PetriNet is not Time Net");
        }
    }

    /**
     * Constrictor
     * @param g     Petri net graph
     * @param d     Delay in seconds
     */
    public TimeSimulation(PetriGraph g, int d) throws Exception {
        super(g, d);
        // Check type of Petri net
        if(g.getType() != PetriGraph.Type.TIME){
            throw new Exception("This PetriNet is not Time Net");
        }
    }

    /**
     * Constructor
     * @param g     Petri net graph
     * @param ia    Is automatic simulation?
     */
    public TimeSimulation(PetriGraph g, boolean ia) throws Exception {
        super(g, ia);
        // Check type of Petri net
        if(g.getType() != PetriGraph.Type.TIME){
            throw new Exception("This PetriNet is not Time Net");
        }
    }

    /**
     * Constructor
     * @param g     Petri net graph
     * @param ia    Is automatic simulation?
     * @param d     Delay in seconds
     */
    public TimeSimulation(PetriGraph g, boolean ia, int d) throws Exception {
        super(g, ia, d);
        // Check type of Petri net
        if(g.getType() != PetriGraph.Type.TIME){
            throw new Exception("This PetriNet is not Time Net");
        }
    }

    /**
     * Generating possible transitions from current state
     * Time based Petri net activates transition if it is alive and then choose transition that has
     * the least time to end
     */
    @Override
    protected void generatePossibleTransitions() {
        if(currentTimes == null){
            currentTimes = new int[graph.getTransitionsCount()];
        }

        currentState = graph.getCurrentState();
        possibleTransitions.clear();

        boolean canDoTransition;

        // Find min time value
        int minTime = Collections.min(Arrays.asList(ArrayUtils.toObject(currentTimes)), new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if(o1 == o2)
                    return 0;
                if(o1 <= 0)
                    return 1;
                if(o2 <= 0)
                    return -1;
                return o1 < o2 ? -1 : 1;
            }
        });

        // If there is no current active transition set Max
        if(minTime <= 0)
            minTime = Integer.MAX_VALUE;

        for(int i =0 ; i< graph.getTransitionsCount(); i++){
            canDoTransition = true;
            // Check if transition is alive
            for(int j=0;j<graph.getPlacesCount(); j++){
                if(currentState[j] < tNegativeMatrix[i][j]){
                    canDoTransition = false;
                    break;
                }
            }
            // If is alive
            if(canDoTransition){
                // Update min time ?
                if(graph.getTransition(i).getCurrentTime() < minTime){
                    minTime = graph.getTransition(i).getCurrentTime();
                }

                // Set current execution time
                currentTimes[i] = graph.getTransition(i).getCurrentTime();
            }
            // if transition is dead
            else{
                // deactivate it
                currentTimes[i] = -1;
            }
        }

        // Select transitions with lowest time execution
        for(int i = 0; i < currentTimes.length; i++){
            if(currentTimes[i] > 0 && currentTimes[i] == minTime){
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
            // Change current petri net state
            for(int j = 0; j < tIncidenceMatrix[i].length; j++){
                graph.getPlace(j).changeMarkersCount(tIncidenceMatrix[i][j]);
            }

            // Decrease execution time for all possible transactions (not only smallest)
            int time = graph.getTransition(i).getCurrentTime();
            for(int j = 0; j < currentTimes.length; j++){
                if(currentTimes[j] > 0){
                    graph.getTransition(j).decreaseTime(time);
                }
            }

            generatePossibleTransitions();
            return true;
        }
        else{
            return false;
        }
    }
}
