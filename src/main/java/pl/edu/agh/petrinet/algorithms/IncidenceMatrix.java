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
    private int[][] incidenceMatrix;

    private int placesCount;
    private int transitionsCount;


    public IncidenceMatrix(PetriGraph graph){
        this.graph = graph;
        placesCount = graph.getPlacesCount();
        transitionsCount = graph.getTransitionsCount();

        positiveMatrix = new int[placesCount][transitionsCount];
        negativeMatrix = new int[placesCount][transitionsCount];
        incidenceMatrix = new int[placesCount][transitionsCount];

        generate();
    }

    private void generate(){
        Collection<PetriEdge> edges = graph.getGraph().getEdges();

        try{
            for(PetriEdge pe : edges){
                if(pe.getV1() instanceof PetriPlace){
                    negativeMatrix[ pe.getV1().getId() ][ pe.getV2().getId() ] = pe.getMarkersCount();
                }
                else{
                    positiveMatrix[ pe.getV2().getId() ][ pe.getV1().getId() ] = pe.getMarkersCount();
                }
            }

            for(int i = 0; i < placesCount; i++){
                for(int j =0; j < transitionsCount; j++){
                    incidenceMatrix[i][j] = positiveMatrix[i][j] - negativeMatrix[i][j];
                }
            }

        }catch(Exception e){

        }

    }

    public int[][] getIncidenceMatrix(){
        return incidenceMatrix;
    }

}
