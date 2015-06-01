package test.unitTests;

import org.junit.Assert;
import org.junit.Test;
import pl.edu.agh.petrinet.algorithms.Attributes;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriGraphUtils;

/**
 * Created by Tomasz on 6/1/2015.
 */
public class ComputeGraphAttributeTest {


	PetriGraph petriGraph;

	@Test(timeout = 3000)
	public void computeGraphAttributesTest() {
		petriGraph = PetriGraphUtils.createTestPetriGraph();
		petriGraph.compute();

		Assert.assertNotNull("Petri Graph is null", petriGraph);

		Attributes attributes = new Attributes(petriGraph);
		Assert.assertNotNull("Attributes object is null", attributes);
	}


}
