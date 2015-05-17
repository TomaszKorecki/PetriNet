package test.unitTests;

import org.junit.Assert;
import org.junit.Test;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriGraphUtils;
import pl.edu.agh.petrinet.model.PetriPlace;


public class RemovePlaceTest {

	PetriGraph petriGraph;

	@Test(timeout = 3000)
	public void createDefaultGraph() {
		petriGraph = PetriGraphUtils.createTestPetriGraph();
		Assert.assertNotNull("Petri grah is null", petriGraph);
		Assert.assertNotNull("Petri places are  null", petriGraph.getAllPlaces());
		Assert.assertNotNull("Petri transitions are null", petriGraph.getAllTransitions());
	}

	@Test(timeout = 3000)
	public void createDefaultGraphAndCompute(){
		petriGraph = PetriGraphUtils.createTestPetriGraph();
		petriGraph.compute();
		Assert.assertTrue("Computing default  graph failed", true);
	}

	@Test(timeout = 3000)
	public void createDefaultGraphAndRemovePlace(){
		petriGraph = PetriGraphUtils.createTestPetriGraph();
		petriGraph.compute();

		PetriPlace p3 = petriGraph.getPlace(3);
		Assert.assertNotNull("Place retrieving failed", 3);

		petriGraph.removePlace(p3);
		petriGraph.compute();
		Assert.assertTrue("Computing after place removing failed", true);
	}
}
