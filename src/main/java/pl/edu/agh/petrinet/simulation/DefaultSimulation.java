package pl.edu.agh.petrinet.simulation;

import pl.edu.agh.petrinet.model.PetriGraph;

import java.util.Random;

/**
 * Created by rakiop on 28.04.15.
 */
public class DefaultSimulation extends BasicSimulation {

    public DefaultSimulation(PetriGraph g) {
        super(g);
    }

    public DefaultSimulation(PetriGraph g, int d) {
        super(g, d);
    }

    public DefaultSimulation(PetriGraph g, boolean ia) {
        super(g, ia);
    }

    public DefaultSimulation(PetriGraph g, boolean ia, int d) {
        super(g, ia, d);
    }

    @Override
    protected void generatePossibleTransitions() {
        currentState = graph.getCurrentState();

        possibleTransitions.clear();

        boolean canDoTransition;
        for(int i =0 ; i< graph.getTransitionsCount(); i++){
            canDoTransition = true;
            for(int j=0;j<graph.getPlacesCount(); j++){
                if(currentState[j] < tNegativeMatrix[i][j]){
                    canDoTransition = false;
                    break;
                }
            }
            if(canDoTransition){
                possibleTransitions.add(i);
            }
        }
    }

    @Override
    public void automaticSimulate() {
        int step;
        Random r = new Random();
        while(isSimulationEnded() == false){
            step = r.nextInt(possibleTransitions.size());
            stepSimulate(step);

            try {
                if(delay > 0){
                    Thread.sleep(delay);
                }
            } catch (InterruptedException e) {}
        }
    }

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
