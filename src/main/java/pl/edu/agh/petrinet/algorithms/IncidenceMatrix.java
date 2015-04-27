package pl.edu.agh.petrinet.algorithms;

import pl.edu.agh.petrinet.model.PetriEdge;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriPlace;

import java.util.Collection;

/**
 * Generating matrix representation of petri net
 * Created by rakiop on 26.04.15.
 */
public class IncidenceMatrix {

    private PetriGraph graph;

    private int[][] positiveMatrix;
    private int[][] negativeMatrix;
    private int[][] tNegativeMatrix;
    private int[][] incidenceMatrix;
    private int[][] tIncidenceMatrix;

    private boolean[] isCollectorTransition;

    private int placesCount;
    private int transitionsCount;


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
    }

    private void generate(){
        Collection<PetriEdge> edges = graph.getGraph().getEdges();

        try{
            for(PetriEdge pe : edges){
                if(pe.getV1() instanceof PetriPlace){
                    negativeMatrix[ pe.getV1().getId() ][ pe.getV2().getId() ] = pe.getMarkersCount();
                    tNegativeMatrix[ pe.getV2().getId() ][ pe.getV1().getId() ] = pe.getMarkersCount();
                }
                else{
                    positiveMatrix[ pe.getV2().getId() ][ pe.getV1().getId() ] = pe.getMarkersCount();
                }
            }

            for(int i = 0; i < placesCount; i++){
                for(int j =0; j < transitionsCount; j++){
                    incidenceMatrix[i][j] = positiveMatrix[i][j] - negativeMatrix[i][j];
                    tIncidenceMatrix[j][i] = incidenceMatrix[i][j];
                }
            }

        }catch(Exception e){

        }

    }

    public int[][] getIncidenceMatrix(){
        return incidenceMatrix;
    }

    public int[][] getTIncidenceMatrix(){
        return tIncidenceMatrix;
    }

    public int[][] getTNegativeMatrix(){
        return tNegativeMatrix;
    }

    private void initCollectorTransition(){
        if(isCollectorTransition == null){
            isCollectorTransition = new boolean[transitionsCount];

            int sum;
            for(int i = 0; i < transitionsCount; i++){
                sum = 0;
                for(int j=0; j< placesCount; j++){
                    sum += tIncidenceMatrix[i][j];
                }

                isCollectorTransition[i] = sum > 0;
            }

        }
    }

    public boolean hasCollectorTransition(){
        initCollectorTransition();

        for(int i = 0; i < transitionsCount; i++){
            if(isCollectorTransition[i])
                return true;
        }

        return false;
    }

    public boolean isColectorTransition(int id){
        initCollectorTransition();
        return isCollectorTransition[id];
    }

}
