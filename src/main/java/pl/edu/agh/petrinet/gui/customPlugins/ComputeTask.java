package pl.edu.agh.petrinet.gui.customPlugins;

import pl.edu.agh.petrinet.model.PetriGraph;

import java.util.concurrent.Callable;

/**
 * Created by rakiop on 09.06.15.
 */
public class ComputeTask implements Callable<String> {

    PetriGraph graph;

    public ComputeTask(PetriGraph p){
        graph = p;
    }

    @Override
    public String call() throws Exception {
        graph.compute();
        return "Compute done";
    }
}
