package pl.edu.agh.petrinet.algorithms;

import org.apache.commons.lang3.ArrayUtils;
import pl.edu.agh.petrinet.model.*;

import java.util.*;

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

    private PetriStateVertex sM0;

    /**
     * Main generating function
     */
    private void generate(){
        // Fill hashmap of transitions
        transitions = graph.getTransitionsHash();

        // Create M0 state
        sM0 = new PetriStateVertex(graph.getM0(), 0);

        // For Time graph add times
        if(graph.getType() == PetriGraph.Type.TIME){
            int[] m0Times = new int[graph.getTransitionsCount()];
            for(PetriTransition t : graph.getAllTransitions()){
                m0Times[t.getId()] = t.getTime();
            }
            sM0.setTransitionsTimes(m0Times);
        }

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

        // Possible transitions list
        List<Integer> possibleTransitions = getPossibleTransitions(state);

        // For each possible transition
        for(Integer i : possibleTransitions){
            // Compute state after transition
            int[] nState = new int[graph.getPlacesCount()];
            for(int j=0;j<graph.getPlacesCount(); j++){
                nState[j] = state.getPlaceMarksCount(j) +
                                (hasCollectorTransitions && state.getPlaceMarksCount(j) == Integer.MAX_VALUE ? 0 : tIncidenceMatrix[i][j]);
            }

            // Create new state object
            PetriStateVertex psv = new PetriStateVertex(nState, state.getLevel() + 1, state.getRoute());

            if(graph.getType() == PetriGraph.Type.TIME){
                int time = state.getTransitionsTimes()[i];
                int[] nTimes = state.getTransitionsTimes().clone();
                for(int l=0; l < graph.getTransitionsCount(); l++){
                    if(nTimes[l] > 0){
                        nTimes[l] =- time;
                        if(nTimes[l] <=0)
                            nTimes[l] = graph.getTransition(l).getTime();
                    }
                }
                psv.setTransitionsTimes(nTimes);
            }


            // If this transition might contains collector check is this really true
            if(graph.getIncidenceMatrix().isColectorTransition(i)){
                computeIfPossibleTransition(psv, i);
            }

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

    /**
     * Check for collectors after transition
     * @param state             previous state
     * @param lastTransition    last transition
     */
    private void computeIfPossibleTransition(PetriStateVertex state, int lastTransition){
        // Get list of transition possible from new state
        List<Integer> possibleTransition = getPossibleTransitions(state);

        // If list is not empty
        if(possibleTransition.isEmpty() == false){

            List<Integer> path = null;

           /* if(state.getRoute().contains(lastTransition)){
                List<Integer> tmpList = new LinkedList<>(state.getRoute());
                tmpList.add(lastTransition);
                path = transitionExistToRootFindNext(tmpList, lastTransition);
            }*/

            // Find if any of next possible transition is already on the route
            //if(path != null){
                for(Integer nTId : possibleTransition){
                    // It might bee last transitions
                    if(nTId == lastTransition){
                        path = new LinkedList<>();
                        path.add(lastTransition);
                        break;
                    }

                    // or one from route
                    if(state.getRoute().contains(nTId)){
                        path = transitionExistToRoot(state.getRoute(), nTId, lastTransition);
                        if(path != null)
                            break;
                    }
                }

            //}
            // If we find transition id we have loop
            if(path != null){

                // Calculate mark change on each place on the route so far
                int[] routeStateCount = new int[graph.getPlacesCount()];
                for(int k = 0; k < graph.getPlacesCount(); k++){
                    routeStateCount[k] += tIncidenceMatrix[lastTransition][k];
                }
                for(int nTid : path){
                    for(int k = 0; k < graph.getPlacesCount(); k++){
                        routeStateCount[k] += tIncidenceMatrix[nTid][k];
                    }
                }

                // List of bufers/collectors
                List<Integer> buferPlacesIds = new LinkedList<>();

                for(int k = 0; k < graph.getPlacesCount(); k++){
                    // If status on place is positive it is collector
                    if(routeStateCount[k] > 0 && tIncidenceMatrix[lastTransition][k] > 0){
                        buferPlacesIds.add(k);
                    }
                    // but if any place has negative value it means
                    // that the loop can be done only several times
                    else if(routeStateCount[k] < 0 && graph.getIncidenceMatrix().sumPlaceChanges(k) < 0){
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
                        state.updatePlaceMarksCount(k,Integer.MAX_VALUE);
                    }
                }
            }
        }
    }

    private List<Integer> transitionExistToRoot(List<Integer> route, int startTranistion, int lastTransition){
        for(PetriVertex pv : graph.getGraph().getPredecessors(graph.getTransition(route.get(route.indexOf(startTranistion))))){
            List<Integer> start = new LinkedList<>();
            List<Integer> x = transitionExistToRoot(route, start, route.indexOf(startTranistion), lastTransition, pv.getId());
            if(x != null)
                return x;
        }
        return null;
    }

    private List<Integer> transitionExistToRootFindNext(List<Integer> route, int lastTransition){
        for(PetriVertex pv : graph.getGraph().getPredecessors(graph.getTransition(route.get(route.indexOf(lastTransition))))){
            List<Integer> start = new LinkedList<>();
            start.add(route.get(route.indexOf(lastTransition)));
            List<Integer> x = transitionExistToRoot(route, start, route.indexOf(lastTransition)+1, lastTransition, pv.getId());
            if(x != null)
                return x;
        }
        return null;
    }

    private List<Integer> transitionExistToRoot(List<Integer> route, List<Integer> retRoute, int nextRouteId, int lastTransition, int startPlaceId){
        if(nextRouteId >= route.size())
            return null;

        for(PetriVertex pv : graph.getGraph().getPredecessors(graph.getTransition(route.get(nextRouteId)))){
            if(pv.getId() == startPlaceId){
                if(route.get(nextRouteId) == lastTransition){
                    return retRoute;
                }

                for(PetriVertex pv2 : graph.getGraph().getSuccessors(graph.getTransition(route.get(nextRouteId)))){
                    List<Integer> newList = new LinkedList<>(retRoute);
                    newList.add(route.get(nextRouteId));
                    List<Integer> toRet = transitionExistToRoot(route, newList, nextRouteId+1, lastTransition, pv2.getId());
                    if(toRet != null){
                        return toRet;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Global method generating possible transitions from state
     * @param state
     * @return
     */
    private List<Integer> getPossibleTransitions(PetriStateVertex state){
        switch(graph.getType()){
            case PRIORYTY:
                return getPossibleTransitions(state.getPlaceMarksCounts(), true);
            case TIME:
                return getPossibleTransitions(state.getPlaceMarksCounts(), state.getTransitionsTimes());
            default:
                return getPossibleTransitions(state.getPlaceMarksCounts());
        }
    }

    /**
     * Generate list of possible transition from state for default graph
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
     * Generate transitions for Priority graph
     * @param cState
     * @param isPriority
     * @return
     */
    private List<Integer> getPossibleTransitions(int[] cState, boolean isPriority){
        List<Integer> ret = getPossibleTransitions(cState);
        if(isPriority && graph.getType() == PetriGraph.Type.PRIORYTY && ret.size() > 0){
            List<Integer> priorityRet = new LinkedList<>();

            int transitionPriority;
            boolean add;
            for(Integer i : ret){
                add = true;
                transitionPriority = graph.getTransition(i).getPriority();
                List<PetriTransition> transitionsFromPrecessorPlace = graph.getTransitionsFromPredecessorPlace(i);
                for(PetriTransition pt : transitionsFromPrecessorPlace){
                    if(ret.contains(pt.getId()) && pt.getPriority() < transitionPriority){
                        add = false;
                        break;
                    }
                }
                if(add){
                    priorityRet.add(i);
                }
            }

            return priorityRet;

        }

        return ret;
    }

    /**
     * Get possible transitions for time Petri graph
     * @param currentState
     * @param cTimes
     * @todo  Do poprawienia generowanie możliwych dróg
     * @return
     */
    private List<Integer> getPossibleTransitions(int[] currentState, int[] cTimes){

        int[] currentTimes = cTimes.clone();

        // Find min time value
        int minTime = Collections.min(Arrays.asList(ArrayUtils.toObject(currentTimes)), new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 == o2)
                    return 0;
                if (o1 <= 0)
                    return 1;
                if (o2 <= 0)
                    return -1;
                return o1 < o2 ? -1 : 1;
            }
        });

        // If there is no current active transition set Max
        if(minTime <= 0)
            minTime = Integer.MAX_VALUE;

        boolean canDoTransition;

        List<Integer> possibleTransitions = new LinkedList<>();

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
                if(currentTimes[i] < minTime){
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

        return possibleTransitions;
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
