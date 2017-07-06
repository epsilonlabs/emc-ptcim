package org.eclipse.epsilon.emc.ptcim.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.epsilon.emc.ptcim.ClassFactory;
import org.eclipse.epsilon.emc.ptcim.IAutomationCaseObject;
import org.eclipse.epsilon.emc.ptcim.PtcimObject;
import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.eclipse.epsilon.eol.parse.Eol_EolParserRules.newExpression_return;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ElementRelatedTests {

	PtcimObject theProject, theDictionary;
	
	@Before
	public void setUp() throws Exception {
		theProject = Utilities.getProject("Traffic Lights");
		theDictionary = new PtcimObject(theProject.item("Dictionary", null).queryInterface(IAutomationCaseObject.class));
	}

	@Test
	public void testGetAllChildrenOfSpecificType() throws InterruptedException {
		PtcimObject softwarePackage = new PtcimObject(theDictionary.item("Package", "Software").queryInterface(IAutomationCaseObject.class));
		long numOfAllClasses = (Long) softwarePackage.itemCount("Class");
		assertEquals(2, numOfAllClasses);
	}
	/*
	@Test
	public void testAddAndRemoveElement() {
		//TODO: This test works but when the class is removed from package Software it is created in the root element
		PtcimObject softwarePackage = new PtcimObject(theDictionary.item("Package", "Software").queryInterface(IAutomationCaseObject.class));
		softwarePackage.add("Class","TestClass");
		long numOfAllClasses = (Long) softwarePackage.itemCount("Class");
		assertEquals(3, numOfAllClasses);
		// Reset back by removing it
		softwarePackage.remove("Class","TestClass");
		numOfAllClasses = (Long) softwarePackage.itemCount("Class");
		assertEquals(2, numOfAllClasses);
	}
	*/
}