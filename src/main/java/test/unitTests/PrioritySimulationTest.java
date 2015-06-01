package test.unitTests;

import org.junit.Assert;
import org.junit.Test;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriGraphUtils;
import pl.edu.agh.petrinet.simulation.BasicSimulation;
import pl.edu.agh.petrinet.simulation.PetriNetSimulationFactory;

import java.util.List;

/**
 * Created by Tomasz on 6/2/2015.
 */
public class PrioritySimulationTest {
PetriGraph petriGraph;

	@Test(timeout = 3000)
	public void prioritySimulationTest(){

		PetriGraph petriGraph = PetriGraphUtils.createPriorityPetriGraph();

		petriGraph.compute();


		BasicSimulation prioritySimulation = PetriNetSimulationFactory.createSimulation(petriGraph);

		Assert.assertNotNull("Simulation is null", prioritySimulation);

		List<Integer> transitions = prioritySimulation.getPossibleTransitions();

		Assert.assertNotNull("Transitions array is null", transitions);

		System.out.print(transitions.size());

		Assert.assertTrue("Transitions count is greater than zero", transitions.size() > 0);

		Assert.assertTrue("Step success", prioritySimulation.stepSimulate(0));
	}
}
