package pl.edu.agh.petrinet.simulation;

import pl.edu.agh.petrinet.model.PetriGraph;

import java.util.Iterator;

/**
 * Simulation of priority Petri net
 */
public class PrioritySimulation extends BasicSimulation {

    /**
     * Constructor
     * @param g     Petri net graph
     */
    public PrioritySimulation(PetriGraph g) throws Exception {
        super(g);
        // Check type of Petri net
        if(g.getType() != PetriGraph.Type.PRIORYTY){
            throw new Exception("This PetriNet is not Priority Net");
        }
    }

    /**
     * Constrictor
     * @param g     Petri net graph
     * @param d     Delay in seconds
     */
    public PrioritySimulation(PetriGraph g, int d) throws Exception {
        super(g, d);
        // Check type of Petri net
        if(g.getType() != PetriGraph.Type.PRIORYTY){
            throw new Exception("This PetriNet is not Priority Net");
        }
    }

    /**
     * Constructor
     * @param g     Petri net graph
     * @param ia    Is automatic simulation?
     */
    public PrioritySimulation(PetriGraph g, boolean ia) throws Exception {
        super(g, ia);
        // Check type of Petri net
        if(g.getType() != PetriGraph.Type.PRIORYTY){
            throw new Exception("This PetriNet is not Priority Net");
        }
    }

    /**
     * Constructor
     * @param g     Petri net graph
     * @param ia    Is automatic simulation?
     * @param d     Delay in seconds
     */
    public PrioritySimulation(PetriGraph g, boolean ia, int d) throws Exception {
        super(g, ia, d);
        // Check type of Petri net
        if(g.getType() != PetriGraph.Type.PRIORYTY){
            throw new Exception("This PetriNet is not Priority Net");
        }
    }

    /**
     * Generating possible transitions from current state
     * Priority Petri net simulation is based on lowest priority from alive transitions
     */
    @Override
    protected void generatePossibleTransitions() {
        currentState = graph.getCurrentState();

        possibleTransitions.clear();

        boolean canDoTransition;
        int minPriority = Integer.MAX_VALUE;
        for(int i =0 ; i< graph.getTransitionsCount(); i++){
            // Check if transition is alive
            canDoTransition = true;
            for(int j=0;j<graph.getPlacesCount(); j++){
                if(currentState[j] < tNegativeMatrix[i][j]){
                    canDoTransition = false;
                    break;
                }
            }
            if(canDoTransition){
                // Update lowest priority?
                if(graph.getTransition(i).getPriority() < minPriority){
                    minPriority = graph.getTransition(i).getPriority();
                }
                // Add to possible list
                possibleTransitions.add(i);
            }
        }

        // If list is empty  - done
        if(possibleTransitions.isEmpty()){
            return;
        }

        // remove alive transition with priority higher then the lowest priority
        int i;
        for(Iterator<Integer> it = possibleTransitions.iterator(); it.hasNext();){
            i = it.next();
            if(graph.getTransition(i).getPriority() > minPriority){
                it.remove();
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
