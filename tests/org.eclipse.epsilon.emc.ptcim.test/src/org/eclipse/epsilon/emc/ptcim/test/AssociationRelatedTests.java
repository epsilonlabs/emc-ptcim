package org.eclipse.epsilon.emc.ptcim.test;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class AssociationRelatedTests extends AbstractTestSetup {

	@Test
	public void testAssociationGetter() {
		long numOfAllOperations = (Long) lightsClass.itemCount("Operation");
		assertEquals(4, numOfAllOperations);
	}
	
	@Test
	public void testAssociationSetter() {
		lightsClass.addByType("Operation", "Operation");
		long numOfAllOperations = (Long) lightsClass.itemCount("Operation");
		assertEquals(5, numOfAllOperations);
		theDictionary.remove("Operation", "Operation1");
		numOfAllOperations = (Long) lightsClass.itemCount("Operation");
		assertEquals(4, numOfAllOperations);
	}
	
	// NORMALISATION TESTS

	@Test
	public void testNormalisation_GetNoSpaceReference() {
		long expected = 16; 
		long ptcName = (Long) controllerStateMachineClass.itemCount("State");
		assertEquals("PTC Name", expected, ptcName);
		long lowerCaps = (Long) controllerStateMachineClass.itemCount("state");
		assertEquals("Lower Caps", expected, lowerCaps);
	}
	
	@Test
	public void testNormalisation_GetSpacedReference() {
		long expected = 1; 
		long ptcName = (Long) maintenanceEng.itemCount("Use Case");
		assertEquals("PTC Name", expected, ptcName);
		long lowerCaps = (Long) maintenanceEng.itemCount("use case");
		assertEquals("Lower Caps", expected, lowerCaps);
		long noSpace = (Long) maintenanceEng.itemCount("UseCase");
		assertEquals("No Space", expected, noSpace);
		long noSpaceLowerCaps = (Long) maintenanceEng.itemCount("usecase");
		assertEquals("No Space Lower Caps", expected, noSpaceLowerCaps);
	}
	
	@Test
	@Ignore
	public void testNormalisation_SetNoSpaceReferene() {
		
	}
	
	@Test
	@Ignore
	public void testNormalisation_SetSpacedReference() {
	
	}
	
	
}
