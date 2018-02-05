package org.eclipse.epsilon.emc.ptcim.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.epsilon.emc.ptcim.PtcimObject;
import org.junit.Ignore;
import org.junit.Test;

public class ElementRelatedTests extends AbstractTestSetup {

	@Test
	@Ignore
	public void testGetAllChildrenOfSpecificType() throws InterruptedException {
		PtcimObject softwarePackage = theDictionary.item("Package", "Software");
		long numOfAllClasses = (Long) softwarePackage.itemCount("Class");
		assertEquals(2, numOfAllClasses);
	}
	
	@Test
	@Ignore
	public void testAddAndRemoveElement() {
		//TODO: This test works but when the class is removed from package Software it is created in the root element
		softwarePackage.add("Class","TestClass");
		long numOfAllClasses = (Long) softwarePackage.itemCount("Class");
		assertEquals(3, numOfAllClasses);
		
		// Reset back by removing it
		softwarePackage.remove("Class","TestClass");
		numOfAllClasses = (Long) softwarePackage.itemCount("Class");
		assertEquals(2, numOfAllClasses);
	}
	
}