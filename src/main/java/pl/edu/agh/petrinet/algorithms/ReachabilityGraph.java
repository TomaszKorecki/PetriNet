package pl.edu.agh.petrinet.algorithms;

import pl.edu.agh.petrinet.model.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rakiop on 26.04.15.
 */
public class ReachabilityGraph {

    private PetriGraph graph;

    private PetriStateGraph stateGraph;

    private int maxLevels;

    private boolean hasCollectorTransitions;

    private boolean isCoverabilityGraph;

    LinkedList<PetriStateVertex> toCompute = new LinkedList<>();

    List<PetriStateVertex> currentStates = new LinkedList<>();

    private int[][] tIncidenceMatrix;

    private int[][] tNegativeMatrix;

    HashMap<Integer, PetriTransition> transitions = new HashMap<>();

    public ReachabilityGraph(PetriGraph graph){
        this.graph = graph;
        this.tIncidenceMatrix = graph.getIncidenceMatrix().getTIncidenceMatrix();
        this.tNegativeMatrix = graph.getIncidenceMatrix().getTNegativeMatrix();

        stateGraph = new PetriStateGraph();

        maxLevels = 50;

        hasCollectorTransitions = graph.getIncidenceMatrix().hasCollectorTransition();
        isCoverabilityGraph = false;
        generate();
    }

    private void generate(){

        for(PetriVertex pv : graph.getGraph().getVertices()){
            if(pv instanceof PetriTransition){
                transitions.put(pv.getId(), (PetriTransition)pv);
            }
        }

        PetriStateVertex sM0 = new PetriStateVertex(graph.getM0(), 0);
        stateGraph.addM0(sM0);
        currentStates.add(sM0);
        toCompute.add(sM0);

        while(toCompute.isEmpty() == false){
            computeState(toCompute.pop());
        }

    }

    private void computeState(PetriStateVertex state){
        boolean canDoTransition;
        int containsIndex;
        for(int i =0 ; i< graph.getTransitionsCount(); i++){
            canDoTransition = true;
            for(int j=0;j<graph.getPlacesCount(); j++){
                if(state.getPlaceMarksCount(j) < tNegativeMatrix[i][j]){
                    canDoTransition = false;
                    break;
                }
            }
            if(canDoTransition){
                int[] nState = new int[graph.getPlacesCount()];
                for(int j=0;j<graph.getPlacesCount(); j++){
                    nState[j] = state.getPlaceMarksCount(j) +
                                    (hasCollectorTransitions && state.getPlaceMarksCount(j) == Integer.MAX_VALUE ? 0 : tIncidenceMatrix[i][j]);
                }

                if(graph.getIncidenceMatrix().isColectorTransition(i)){
                    computeIfPossibleTransition(state, nState, i);
                }

                PetriStateVertex psv = new PetriStateVertex(nState, state.getLevel() + 1, state.getRoute());
                psv.addToRoute(i);

                containsIndex = currentStates.indexOf(psv);
                if(containsIndex < 0){
                    stateGraph.addState(state, psv, transitions.get(i));
                    currentStates.add(psv);
                    toCompute.add(psv);
                }
                else{
                    stateGraph.addExistingState(state, currentStates.get(containsIndex), transitions.get(i));
                }
            }
        }
    }

    private void computeIfPossibleTransition(PetriStateVertex state, int[] nState, int lastTransition){
        List<Integer> possibleTransition = getPossibleTransitions(nState);
        if(possibleTransition.isEmpty() == false){
            int loopId = -1;
            for(Integer nTId : possibleTransition){
                if(nTId == lastTransition){
                    loopId = lastTransition;
                    break;
                }
                if(state.getRoute().contains(nTId)){
                    loopId = nTId;
                    break;
                }
            }

            if(loopId > -1){
                int[] routeStateCount = new int[graph.getPlacesCount()];
                for(int k = 0; k < graph.getPlacesCount(); k++){
                    routeStateCount[k] += tIncidenceMatrix[lastTransition][k];
                }
                for(int nTid : state.getRoute()){
                    for(int k = 0; k < graph.getPlacesCount(); k++){
                        routeStateCount[k] += tIncidenceMatrix[nTid][k];
                    }
                }

                List<Integer> bufforPlacesIds = new LinkedList<>();

                for(int k = 0; k < graph.getPlacesCount(); k++){
                    if(routeStateCount[k] > 0){
                        bufforPlacesIds.add(k);
                    }
                    else if(routeStateCount[k] < 0){
                        bufforPlacesIds.clear();
                        break;
                    }
                }

                if(bufforPlacesIds.isEmpty() == false){
                    isCoverabilityGraph = true;
                    for(int k : bufforPlacesIds){
                        nState[k] = Integer.MAX_VALUE;
                    }
                }
            }
        }
    }

    private List<Integer> getPossibleTransitions(int[] cState){
        List<Integer> ret = new LinkedList<>();
        boolean canDoTransition;
        for(int i =0 ; i< graph.getTransitionsCount(); i++){
            canDoTransition = true;
            for(int j=0;j<graph.getPlacesCount(); j++){
                if(cState[j] < tNegativeMatrix[i][j]){
                    canDoTransition = false;
                    break;
                }
            }
            if(canDoTransition){
                ret.add(i);
            }
        }
        return ret;
    }

    public PetriStateGraph getStateGraph(){
        return this.stateGraph;
    }

    public boolean isCoverabilityGraph(){
        return this.isCoverabilityGraph;
    }

}
