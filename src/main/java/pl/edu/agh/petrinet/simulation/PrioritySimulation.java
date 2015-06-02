package pl.edu.agh.petrinet.simulation;

import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriTransition;

import java.util.LinkedList;
import java.util.List;

/**
 * Simulation of priority Petri net
 */
public class PrioritySimulation extends BasicSimulation {

    protected List<Integer> temporaryPossibleTransitions;

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

        if(temporaryPossibleTransitions == null){
            temporaryPossibleTransitions = new LinkedList<>();
        }

        possibleTransitions.clear();
        temporaryPossibleTransitions.clear();

        boolean canDoTransition;
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
                // Add to possible list
                temporaryPossibleTransitions.add(i);
            }
        }

        // If list is empty  - done
        if(temporaryPossibleTransitions.isEmpty()){
            return;
        }

        int transitionPriority;
        boolean add;
        for(Integer i : temporaryPossibleTransitions){
            add = true;
            transitionPriority = graph.getTransition(i).getPriority();
            List<PetriTransition> transitionsFromPrecessorPlace = graph.getTransitionsFromPredecessorPlace(i);
            for(PetriTransition pt : transitionsFromPrecessorPlace){
                if(temporaryPossibleTransitions.contains(pt.getId()) && pt.getPriority() < transitionPriority){
                    add = false;
                    break;
                }
            }
            if(add){
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
