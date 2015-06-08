package test.unitTests;

import org.junit.Assert;
import org.junit.Test;
import pl.edu.agh.petrinet.algorithms.Attributes;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriGraphUtils;
import pl.edu.agh.petrinet.serialization.PetriSerialization;

/**
 * Created by Tomasz on 6/8/2015.
 */
public class SelfDrivingCarTest {

	PetriGraph petriGraph;

	@Test(timeout = 3000)
	public void testSerialization(){
		PetriGraph graph = PetriGraphUtils.createTestPetriGraph();
		graph.compute();


		PetriSerialization ps = new PetriSerialization(graph);
		try{
			PetriGraph g2 = ps.deserialize("tmp_serialize.xml");
			PetriGraph selfDrivingCar = ps.deserialize("selfDrivingCar.xml");

			g2.compute();

			Assert.assertNotNull(g2);
			Assert.assertNotNull(selfDrivingCar);
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Test(timeout = 3000)
	public void computeGraphAttributesTest() {
		PetriGraph graph = PetriGraphUtils.createTestPetriGraph();
		graph.compute();
		PetriSerialization ps = new PetriSerialization(graph);

		try{
			PetriGraph selfDrivingCar = ps.deserialize("selfDrivingCar.xml");

			Assert.assertNotNull("Self driving car net is null", selfDrivingCar);

			Assert.assertTrue("Remove transition with id = 8 failed", selfDrivingCar.removeTransition(petriGraph.getTransition(8)));

			selfDrivingCar.compute();
		}catch (Exception e){
			System.out.println(e.getMessage());
			Assert.fail("Test failed because " + e.getStackTrace());
		}
	}
}
