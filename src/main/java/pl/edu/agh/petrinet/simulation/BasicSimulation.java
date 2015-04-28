package pl.edu.agh.petrinet.simulation;

import pl.edu.agh.petrinet.model.PetriGraph;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by rakiop on 28.04.15.
 */
public abstract class BasicSimulation {

    protected PetriGraph graph;

    protected boolean isAutomatic;

    protected int delay;

    protected List<Integer> possibleTransitions;

    protected int[] currentState;

    protected int[][] tIncidenceMatrix;

    protected int[][] tNegativeMatrix;

    public BasicSimulation(PetriGraph g){
        graph = g;
        tIncidenceMatrix = g.getIncidenceMatrix().getTIncidenceMatrix();
        tNegativeMatrix = g.getIncidenceMatrix().getTNegativeMatrix();
        isAutomatic = true;
        possibleTransitions = new LinkedList<>();
        generatePossibleTransitions();
    }

    public BasicSimulation(PetriGraph g, int d){
        graph = g;
        tIncidenceMatrix = g.getIncidenceMatrix().getTIncidenceMatrix();
        tNegativeMatrix = g.getIncidenceMatrix().getTNegativeMatrix();
        delay = d * 1000;
        isAutomatic = true;
        possibleTransitions = new LinkedList<>();
        generatePossibleTransitions();
    }

    public BasicSimulation(PetriGraph g, boolean ia){
        graph = g;
        tIncidenceMatrix = g.getIncidenceMatrix().getTIncidenceMatrix();
        tNegativeMatrix = g.getIncidenceMatrix().getTNegativeMatrix();
        isAutomatic = ia;
        possibleTransitions = new LinkedList<>();
        generatePossibleTransitions();
    }

    public BasicSimulation(PetriGraph g, boolean ia, int d){
        graph = g;
        tIncidenceMatrix = g.getIncidenceMatrix().getTIncidenceMatrix();
        tNegativeMatrix = g.getIncidenceMatrix().getTNegativeMatrix();
        isAutomatic = ia;
        delay = d * 1000;
        possibleTransitions = new LinkedList<>();
        generatePossibleTransitions();
    }

    public List<Integer> getPossibleTransitions(){
        return this.possibleTransitions;
    }

    public boolean isSimulationEnded(){
        return possibleTransitions.isEmpty();
    }

    public void setDelay(int d){
        this.delay = d;
    }

    protected abstract void generatePossibleTransitions();

    public abstract void automaticSimulate();

    public abstract boolean stepSimulate(int i);

}
