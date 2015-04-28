package pl.edu.agh.petrinet.simulation;

import pl.edu.agh.petrinet.model.PetriGraph;

import java.util.Random;

/**
 * Created by rakiop on 28.04.15.
 */
public class TimeSimulation extends BasicSimulation {

    private int[] currentTimes;

    public TimeSimulation(PetriGraph g) throws Exception {
        super(g);
        if(g.getType() != PetriGraph.Type.TIME){
            throw new Exception("This PetriNet is not Time Net");
        }
    }

    public TimeSimulation(PetriGraph g, int d) throws Exception {
        super(g, d);
        if(g.getType() != PetriGraph.Type.TIME){
            throw new Exception("This PetriNet is not Time Net");
        }
    }

    public TimeSimulation(PetriGraph g, boolean ia) throws Exception {
        super(g, ia);
        if(g.getType() != PetriGraph.Type.TIME){
            throw new Exception("This PetriNet is not Time Net");
        }
    }

    public TimeSimulation(PetriGraph g, boolean ia, int d) throws Exception {
        super(g, ia, d);
        if(g.getType() != PetriGraph.Type.TIME){
            throw new Exception("This PetriNet is not Time Net");
        }
    }

    @Override
    protected void generatePossibleTransitions() {
        if(currentTimes == null){
            currentTimes = new int[graph.getTransitionsCount()];
        }

        currentState = graph.getCurrentState();
        possibleTransitions.clear();

        boolean canDoTransition;
        int minTime = Integer.MAX_VALUE;
        for(int i =0 ; i< graph.getTransitionsCount(); i++){
            canDoTransition = true;
            for(int j=0;j<graph.getPlacesCount(); j++){
                if(currentState[j] < tNegativeMatrix[i][j]){
                    canDoTransition = false;
                    break;
                }
            }
            if(canDoTransition){
                if(graph.getTransition(i).getCurrentTime() < minTime){
                    minTime = graph.getTransition(i).getCurrentTime();
                }

                if(currentTimes[i] <= 0){
                    currentTimes[i] = graph.getTransition(i).getCurrentTime();
                }
            }
            else{
                if(currentTimes[i] == graph.getTransition(i).getTime()){
                    currentTimes[i] = -1;
                }
            }
        }

        for(int i = 0; i < currentTimes.length; i++){
            if(currentTimes[i] > 0){
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

            int time = graph.getTransition(i).getCurrentTime();
            for(Integer j : possibleTransitions){
                graph.getTransition(j).decreaseTime(time);
                currentTimes[j] = currentTimes[j] > time ? currentTimes[j] - time : -1;
            }

            generatePossibleTransitions();
            return true;
        }
        else{
            return false;
        }
    }
}
