package pl.edu.agh.petrinet.simulation;

import pl.edu.agh.petrinet.model.PetriGraph;

/**
 * Created by Tomasz on 6/1/2015.
 */
public class PetriNetSimulationFactory {
	public static BasicSimulation createSimulation(PetriGraph petriGraph) {
		try {
			PetriGraph.Type petriGraphType = petriGraph.getType();
			if (petriGraphType == PetriGraph.Type.DEFAULT) {
				return new DefaultSimulation(petriGraph);
			} else if (petriGraphType == PetriGraph.Type.PRIORYTY) {
				return new PrioritySimulation(petriGraph);
			} else if (petriGraphType == PetriGraph.Type.TIME) {
				return new TimeSimulation(petriGraph);
			} else return null;
		} catch (Exception e) {
			System.out.printf(e.getMessage());
		}

		return null;
	}
}
