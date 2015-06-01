package test.unitTests;

import org.junit.Assert;
import org.junit.Test;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriGraphUtils;

/**
 * Created by Tomasz on 5/17/2015.
 */
public class ReachabilityGraphTest {

	PetriGraph petriGraph;

	@Test(timeout = 3000)
	public void createReachabilityGraph() {
		petriGraph = PetriGraphUtils.createTestPetriGraph();
		Assert.assertNotNull("Petri graph is null", petriGraph);

		petriGraph.compute();
		Assert.assertNotNull("Reachability graph is null", petriGraph.getReachabilityGraph());
		Assert.assertNotNull("Petri state graph is null", petriGraph.getReachabilityGraph().getPetriStateGraph());
		Assert.assertNotNull("JUNG graph in reachability graph is null", petriGraph.getReachabilityGraph().getPetriStateGraph().getGraph());
		Assert.assertNotNull("IncidenceMatrix is null", petriGraph.getIncidenceMatrix());
	}
}
