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

    private IncidenceMatrix incidenceMatrix;

    private PetriStateGraph stateGraph;

    private int maxLevels;

    public ReachabilityGraph(PetriGraph graph, IncidenceMatrix incidenceMatrix){
        this.graph = graph;
        this.incidenceMatrix = incidenceMatrix;
        this.tIncidenceMatrix = incidenceMatrix.getTIncidenceMatrix();
        this.tNegativeMatrix = incidenceMatrix.getTNegativeMatrix();

        stateGraph = new PetriStateGraph();

        maxLevels = 50;

        generate();
    }

    LinkedList<PetriStateVertex> toCompute = new LinkedList<>();

    List<PetriStateVertex> currentStates = new LinkedList<>();

    private int[][] tIncidenceMatrix;
    private int[][] tNegativeMatrix;
    HashMap<Integer, PetriTransition> transitions = new HashMap<>();

    private void generate(){

        int[] m0 = new int[graph.getPlacesCount()];

        for(PetriVertex pv : graph.getGraph().getVertices()){
            if(pv instanceof PetriPlace){
                m0[pv.getId()] = ((PetriPlace) pv).getMarksersCount();
            }
            else{
                transitions.put(pv.getId(), (PetriTransition)pv);
            }
        }

        PetriStateVertex sM0 = new PetriStateVertex(m0, 0);
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
                    nState[j] = state.getPlaceMarksCount(j) + tIncidenceMatrix[i][j];
                }

                PetriStateVertex psv = new PetriStateVertex(nState, state.getLevel() + 1);

                containsIndex = currentStates.indexOf(psv);
                if(containsIndex < 0){
                    stateGraph.addState(state, psv, transitions.get(i));
                    currentStates.add(psv);
                    if(psv.getLevel() < maxLevels){
                        toCompute.add(psv);
                    }
                }
                else{
                    stateGraph.addExistingState(state, currentStates.get(containsIndex), transitions.get(i));
                }
            }
        }
    }

    public PetriStateGraph getStateGraph(){
        return this.stateGraph;
    }

}
