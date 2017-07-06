package org.eclipse.epsilon.emc.ptcim.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.epsilon.emc.ptcim.IAutomationCaseObject;
import org.eclipse.epsilon.emc.ptcim.PtcimObject;
import org.junit.Before;
import org.junit.Test;

public class AssociationRelatedTests {

PtcimObject theProject, theDictionary, softwarePackage, lightsClass;
	
	@Before
	public void setUp() throws Exception {
		theProject = Utilities.getProject("Traffic Lights");
		theDictionary = new PtcimObject(theProject.item("Dictionary", null).queryInterface(IAutomationCaseObject.class));
		softwarePackage = new PtcimObject(theDictionary.item("Package", "Software").queryInterface(IAutomationCaseObject.class));
		lightsClass = new PtcimObject(theDictionary.item("Class", "Lights").queryInterface(IAutomationCaseObject.class));
	}

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
}
