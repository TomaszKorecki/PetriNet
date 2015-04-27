package pl.edu.agh.petrinet.algorithms;
import pl.edu.agh.petrinet.model.PetriGraph;

/**
 * Created by rakiop on 26.04.15.
 */
public class Attributes {

    private boolean isConservative;

    private boolean isSave;

    private PetriGraph graph;

    public Attributes(PetriGraph graph){
        this.graph = graph;


        compute();
    }

    private void compute(){

        isConservative = graph.getReachabilityGraph().isCoverabilityGraph() == false;

        int sum;
        if(isConservative){
            int[] m0 = graph.getM0();
            sum = 0;
            for(int i=0; i< m0.length;i++){
                sum += m0[i];
            }

            isSave = sum == 1;
        }
        else{
            isSave = false;
        }
    }

}
