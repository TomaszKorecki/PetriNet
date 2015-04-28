package pl.edu.agh.petrinet.simulation;

import pl.edu.agh.petrinet.model.PetriGraph;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by rakiop on 28.04.15.
 */
public class PrioritySimulation extends BasicSimulation {

    public PrioritySimulation(PetriGraph g) throws Exception {
        super(g);
        if(g.getType() != PetriGraph.Type.PRIORYTY){
            throw new Exception("This PetriNet is not Priority Net");
        }
    }

    public PrioritySimulation(PetriGraph g, int d) throws Exception {
        super(g, d);
        if(g.getType() != PetriGraph.Type.PRIORYTY){
            throw new Exception("This PetriNet is not Priority Net");
        }
    }

    public PrioritySimulation(PetriGraph g, boolean ia) throws Exception {
        super(g, ia);
        if(g.getType() != PetriGraph.Type.PRIORYTY){
            throw new Exception("This PetriNet is not Priority Net");
        }
    }

    public PrioritySimulation(PetriGraph g, boolean ia, int d) throws Exception {
        super(g, ia, d);
        if(g.getType() != PetriGraph.Type.PRIORYTY){
            throw new Exception("This PetriNet is not Priority Net");
        }
    }

    @Override
    protected void generatePossibleTransitions() {
        currentState = graph.getCurrentState();

        possibleTransitions.clear();

        boolean canDoTransition;
        int minPriority = Integer.MAX_VALUE;
        for(int i =0 ; i< graph.getTransitionsCount(); i++){
            canDoTransition = true;
            for(int j=0;j<graph.getPlacesCount(); j++){
                if(currentState[j] < tNegativeMatrix[i][j]){
                    canDoTransition = false;
                    break;
                }
            }
            if(canDoTransition){
                if(graph.getTransition(i).getPriority() < minPriority){
                    minPriority = graph.getTransition(i).getPriority();
                }
                possibleTransitions.add(i);
            }
        }

        if(possibleTransitions.isEmpty()){
            return;
        }

        int i;
        for(Iterator<Integer> it = possibleTransitions.iterator(); it.hasNext();){
            i = it.next();
            if(graph.getTransition(i).getPriority() > minPriority){
                it.remove();
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
