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
public class TimeSimulationTest {

	@Test(timeout = 3000)
	public void timeSimulationTest(){

		PetriGraph petriGraph = PetriGraphUtils.createTimePetriGraph();

		petriGraph.compute();


		BasicSimulation timeSimulation = PetriNetSimulationFactory.createSimulation(petriGraph);

		Assert.assertNotNull("Simulation is null", timeSimulation);

		List<Integer> transitions = timeSimulation.getPossibleTransitions();
		Assert.assertNotNull("Transitions array is null", transitions);
		Assert.assertTrue("Transitions count is greater than zero", transitions.size() > 0);

		//Only possible transition should be the one with lowest time. It should be transition 0
		Assert.assertTrue("Wrong transition is possible", transitions.get(0) == 0);

		Assert.assertTrue("Step success", timeSimulation.stepSimulate(0));

		petriGraph.getAllTransitions().forEach(petriTransition -> {
			System.out.println("Transition: [" + petriTransition.getId() + "] has got current time " + petriTransition.getCurrentTime());
		});

		Assert.assertTrue("Transitions current values are wrong after first step",
				petriGraph.getTransition(0).getCurrentTime() == 2 && petriGraph.getTransition(1).getCurrentTime() == 1);


	}
}
