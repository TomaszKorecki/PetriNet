package pl.edu.agh.petrinet.algorithms;

import pl.edu.agh.petrinet.model.PetriEdge;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriPlace;

import java.util.Collection;

/**
 * Generating matrix representation of petri net
 */
public class IncidenceMatrix {

    /**
     * Petri net graph
     */
    private PetriGraph graph;

    /**
     * Matrix of positive marks change in places
     */
    private int[][] positiveMatrix;

    /**
     * Matrix of negative marks change in places
     */
    private int[][] negativeMatrix;

    /**
     * Transposition of negative marks change in places
     */
    private int[][] tNegativeMatrix;

    /**
     * Incidence petri net representations
     */
    private int[][] incidenceMatrix;

    /**
     * Transposition of incidence petri net
     */
    private int[][] tIncidenceMatrix;

    /**
     * Tells which transition might be collector
     */
    private boolean[] isCollectorTransition;

    /**
     * Count of places
     */
    private int placesCount;

    /**
     * Count of transitions
     */
    private int transitionsCount;

    /**
     * Constructor
     * @param graph     Petri net graph
     */
    public IncidenceMatrix(PetriGraph graph){
        this.graph = graph;
        placesCount = graph.getPlacesCount();
        transitionsCount = graph.getTransitionsCount();

        positiveMatrix = new int[placesCount][transitionsCount];
        negativeMatrix = new int[placesCount][transitionsCount];
        incidenceMatrix = new int[placesCount][transitionsCount];
        tIncidenceMatrix = new int[transitionsCount][placesCount];
        tNegativeMatrix = new int[transitionsCount][placesCount];

        generate();
        initCollectorTransition();
    }

    /**
     * Fill up matrices
     */
    private void generate(){
        // Get all edges
        Collection<PetriEdge> edges = graph.getGraph().getEdges();

        // To prevent ArrayIndexOutOfBoundsException exception
        try{
            for(PetriEdge pe : edges){
                // If it is an edge from Place to Transition fill up removing count
                if(pe.getV1() instanceof PetriPlace){
                    if(graph.getPlace(pe.getV1().getId()) == pe.getV1() && graph.getTransition(pe.getV2().getId()) == pe.getV2()){
                        negativeMatrix[ pe.getV1().getId() ][ pe.getV2().getId() ] = pe.getMarkersCount();
                        tNegativeMatrix[ pe.getV2().getId() ][ pe.getV1().getId() ] = pe.getMarkersCount();
                    }
                }
                // But if edge goes from transition to place = we add markers
                else if(graph.getPlace(pe.getV2().getId()) == pe.getV2() && graph.getTransition(pe.getV1().getId()) == pe.getV1()){
                    positiveMatrix[ pe.getV2().getId() ][ pe.getV1().getId() ] = pe.getMarkersCount();
                }
            }

            // Calculate incidence matrix simply by subtracting
            for(int i = 0; i < placesCount; i++){
                for(int j =0; j < transitionsCount; j++){
                    incidenceMatrix[i][j] = positiveMatrix[i][j] - negativeMatrix[i][j];
                    tIncidenceMatrix[j][i] = incidenceMatrix[i][j];
                }
            }

        }catch(Exception e){ }

    }

    /**
     * Check whether this Petri Net might have Collector Transition
     */
    private void initCollectorTransition(){
        if(isCollectorTransition == null){
            isCollectorTransition = new boolean[transitionsCount];

            int sum;
            for(int i = 0; i < transitionsCount; i++){
                sum = 0;
                // Add all marks changes is transition
                for(int j=0; j< placesCount; j++){
                    sum += tIncidenceMatrix[i][j];
                }

                // If it is positive value we may have collector after this transition
                isCollectorTransition[i] = sum > 0;
            }

        }
    }

    /**
     * Check if exists at least one possible collector transition
     * @return
     */
    public boolean hasCollectorTransition(){
        initCollectorTransition();

        for(int i = 0; i < transitionsCount; i++){
            if(isCollectorTransition[i])
                return true;
        }

        return false;
    }

    /**
     * Check if send transition is possible collector transition
     * @param id    transition id
     * @return      is collector transition?
     */
    public boolean isColectorTransition(int id){
        initCollectorTransition();
        return isCollectorTransition[id];
    }

    /**
     * Get Incidence Matrix
     * @return Incidence Matrix
     */
    public int[][] getIncidenceMatrix(){
        return incidenceMatrix;
    }

    /**
     * Get Transposition of Incidence Matrix
     * @return Transposition of Incidence Matrix
     */
    public int[][] getTIncidenceMatrix(){
        return tIncidenceMatrix;
    }

    /**
     * Get Transposition of Negative Matrix
     * @returnTransposition of Negative Matrix
     */
    public int[][] getTNegativeMatrix(){
        return tNegativeMatrix;
    }



}
