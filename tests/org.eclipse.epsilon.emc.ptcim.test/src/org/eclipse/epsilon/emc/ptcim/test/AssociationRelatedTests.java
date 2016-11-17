package org.eclipse.epsilon.emc.ptcim.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.epsilon.emc.ptcim.jawin.JawinObject;
import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;
import org.jawin.COMException;
import org.jawin.DispatchPtr;
import org.jawin.win32.Ole32;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AssociationRelatedTests {

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
			throw new EpsilonCOMException(e);
		}
	}
		
	@Test
	public void testAssociationGetter() throws COMException {
		DispatchPtr softwarePackage = (DispatchPtr) model.invoke("Item", "Package", "Software");
		String strObjId = (String) softwarePackage.get("Property", "Id");
		assertEquals("80a27e73-0121-436a-abf7-1e01ebb33c7e", strObjId);
		System.out.println("Test property getter: Success");
	}
	
}
