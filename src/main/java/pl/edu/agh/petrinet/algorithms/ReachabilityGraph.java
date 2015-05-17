package pl.edu.agh.petrinet.algorithms;

import pl.edu.agh.petrinet.model.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Algorithm for Reachability/Coverability Petri Graph
 */
public class ReachabilityGraph {

    /**
     * Petri net graph
     */
    private PetriGraph graph;

    /**
     * Graph of states
     */
    private PetriStateGraph stateGraph;

    /**
     * Check if this graph has collectors = it might be coverability graph
     */
    private boolean hasCollectorTransitions;

    /**
     * Is this reachability or coverability graph = has or has not collectors
     */
    private boolean isCoverabilityGraph;

    /**
     * FiFo list to create state graph accross
     */
    LinkedList<PetriStateVertex> toCompute = new LinkedList<>();

    /**
     * List of generated states to close loops
     */
    List<PetriStateVertex> currentStates = new LinkedList<>();

    /**
     * Copied transpositioned incidence matrix - first transition then places
     */
    private int[][] tIncidenceMatrix;

    /**
     * Copied transpositioned negative matrix - first transition then places
     */
    private int[][] tNegativeMatrix;

    /**
     * Hashmap of transition to access it via ID
     */
    HashMap<Integer, PetriTransition> transitions = new HashMap<>();

    /**
     * Constructor
     * @param graph     Petri net graph
     */
    public ReachabilityGraph(PetriGraph graph){
        this.graph = graph;

        this.tIncidenceMatrix = graph.getIncidenceMatrix().getTIncidenceMatrix();
        this.tNegativeMatrix = graph.getIncidenceMatrix().getTNegativeMatrix();

        stateGraph = new PetriStateGraph();

        hasCollectorTransitions = graph.getIncidenceMatrix().hasCollectorTransition();
        isCoverabilityGraph = false;
        generate();
    }

    public PetriStateGraph getPetriStateGraph(){
        return  stateGraph;
    }

    /**
     * Main generating function
     */
    private void generate(){
        // Fill hashmap of transitions
        transitions = graph.getTransitionsHash();

        // Create M0 state
        PetriStateVertex sM0 = new PetriStateVertex(graph.getM0(), 0);
        // Add first state vertex
        stateGraph.addM0(sM0);
        // Add to current states
        currentStates.add(sM0);
        // Add to FiFo for next compute
        toCompute.add(sM0);

        // Generate next states until FiFo is not empty
        while(toCompute.isEmpty() == false){
            computeState(toCompute.pop());
        }

    }

    /**
     * Generating next state
     * @param state     current state
     */
    private void computeState(PetriStateVertex state){
        // Check if this is not leaf
        boolean canDoTransition;

        // If computed state exists get its ID in list
        int containsIndex;

        // Check every transition for this state
        for(int i =0 ; i< graph.getTransitionsCount(); i++){
            // Assume we can do transition
            canDoTransition = true;
            // Check marks
            for(int j=0;j<graph.getPlacesCount(); j++){
                // If place has less marks then this transition requires mark transition as dead
                if(state.getPlaceMarksCount(j) < tNegativeMatrix[i][j]){
                    canDoTransition = false;
                    break;
                }
            }

            // If transition is alive
            if(canDoTransition){
                // Compute state after transition
                int[] nState = new int[graph.getPlacesCount()];
                for(int j=0;j<graph.getPlacesCount(); j++){
                    nState[j] = state.getPlaceMarksCount(j) +
                                    (hasCollectorTransitions && state.getPlaceMarksCount(j) == Integer.MAX_VALUE ? 0 : tIncidenceMatrix[i][j]);
                }

                // If this transition might contains collector check is this really true
                if(graph.getIncidenceMatrix().isColectorTransition(i)){
                    computeIfPossibleTransition(state, nState, i);
                }

                // Create new state object
                PetriStateVertex psv = new PetriStateVertex(nState, state.getLevel() + 1, state.getRoute());
                psv.addToRoute(i);

                // Check whether new state already exists - loop
                containsIndex = currentStates.indexOf(psv);
                // If no
                if(containsIndex < 0){
                    // Add new state to graph
                    stateGraph.addState(state, psv, transitions.get(i));
                    // Add new state to list
                    currentStates.add(psv);
                    // Add new state to work with
                    toCompute.add(psv);
                }
                // But if state already exists
                else{
                    // Create loop
                    stateGraph.addExistingState(state, currentStates.get(containsIndex), transitions.get(i));
                    // ...and do nothing more
                }
            }
        }
    }

    /**
     * Check for collectors after transition
     * @param state             previous state
     * @param nState            new computed state
     * @param lastTransition    last transition
     */
    private void computeIfPossibleTransition(PetriStateVertex state, int[] nState, int lastTransition){
        // Get list of transition possible from new state
        List<Integer> possibleTransition = getPossibleTransitions(nState);
        // If list is not empty
        if(possibleTransition.isEmpty() == false){
            // Find if any of next possible transition is already on the route
            int loopId = -1;
            for(Integer nTId : possibleTransition){
                // It might bee last transitions
                if(nTId == lastTransition){
                    loopId = lastTransition;
                    break;
                }
                // or one from route
                if(state.getRoute().contains(nTId)){
                    loopId = nTId;
                    break;
                }
            }

            // If we find transition id we have loop
            if(loopId > -1){

                // Calculate mark change on each place on the route so far
                int[] routeStateCount = new int[graph.getPlacesCount()];
                for(int k = 0; k < graph.getPlacesCount(); k++){
                    routeStateCount[k] += tIncidenceMatrix[lastTransition][k];
                }
                for(int nTid : state.getRoute()){
                    for(int k = 0; k < graph.getPlacesCount(); k++){
                        routeStateCount[k] += tIncidenceMatrix[nTid][k];
                    }
                }

                // List of bufers/collectors
                List<Integer> buferPlacesIds = new LinkedList<>();

                for(int k = 0; k < graph.getPlacesCount(); k++){
                    // If status on place is positive it is collector
                    if(routeStateCount[k] > 0){
                        buferPlacesIds.add(k);
                    }
                    // but if any plac has negative value it means
                    // that the loop can be done only several times
                    else if(routeStateCount[k] < 0){
                        buferPlacesIds.clear();
                        break;
                    }
                }

                // If we have at least one bufer
                if(buferPlacesIds.isEmpty() == false){
                    // It is coverability graph
                    isCoverabilityGraph = true;
                    // Set bufer infinity as Integer max value
                    for(int k : buferPlacesIds){
                        nState[k] = Integer.MAX_VALUE;
                    }
                }
            }
        }
    }

    /**
     * Generate list of possible transition from state
     * @param cState        state of graph
     * @return              list of transition
     */
    private List<Integer> getPossibleTransitions(int[] cState){
        List<Integer> ret = new LinkedList<>();
        boolean canDoTransition;
        // Check every transition
        for(int i =0 ; i< graph.getTransitionsCount(); i++){
            // Assume we can do this transition
            canDoTransition = true;
            for(int j=0;j<graph.getPlacesCount(); j++){
                // If place does not have enough marks then this transition is dead
                if(cState[j] < tNegativeMatrix[i][j]){
                    canDoTransition = false;
                    break;
                }
            }
            // If transition is alive add it to list
            if(canDoTransition){
                ret.add(i);
            }
        }
        return ret;
    }

    /**
     * Get generated graph
     * @return      generated graph
     */
    public PetriStateGraph getStateGraph(){
        return this.stateGraph;
    }

    /**
     * Return information if this is coverability graph?
     * @return  is this coverability graph
     */
    public boolean isCoverabilityGraph(){
        return this.isCoverabilityGraph;
    }

}
