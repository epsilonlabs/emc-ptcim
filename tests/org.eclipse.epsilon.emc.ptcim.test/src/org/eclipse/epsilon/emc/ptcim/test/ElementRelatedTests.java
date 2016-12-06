package org.eclipse.epsilon.emc.ptcim.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.epsilon.emc.ptcim.jawin.JawinObject;
import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.jawin.COMException;
import org.jawin.DispatchPtr;
import org.jawin.win32.Ole32;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ElementRelatedTests {

	private JawinObject model;
	private DispatchPtr theProject;
	
	@Before
	public void setUp() throws Exception {
		theProject = Utilities.getProject("Traffic Lights");
		DispatchPtr modelptr = Utilities.load(theProject);
		model = new JawinObject(modelptr);
	}

	@After
	public void tearDown() throws Exception {
		try {
			Ole32.CoUninitialize();
		} catch (COMException e) {
			throw new EolInternalException(e);
		}
	}
	
	@Test
	public void testGetAllChildrenOfSpecificType() throws COMException {
		DispatchPtr softwarePackage = (DispatchPtr) model.invoke("Item", "Package", "Software");
		int numOfAllClasses = (Integer) softwarePackage.invoke("ItemCount", "Class");
		assertEquals(2, numOfAllClasses);
		System.out.println("Test get all children of specific type: Success");
	}
	
	@Test
	public void testAddAndRemoveElement() throws COMException {
		//TODO: This test works but when the class is removed from package Software it is created in the root element
		DispatchPtr softwarePackage = (DispatchPtr) model.invoke("Item", "Package", "Software");
		softwarePackage.invoke("Add","Class","TestClass");
		int numOfAllClasses = (Integer) softwarePackage.invoke("ItemCount", "Class");
		assertEquals(3, numOfAllClasses);
		// Reset back by removing it
		model.invoke("Remove","Class","TestClass");
		numOfAllClasses = (Integer) softwarePackage.invoke("ItemCount", "Class");
		assertEquals(2, numOfAllClasses);
		System.out.println("Test add and then remove element: Success");
	}
}
