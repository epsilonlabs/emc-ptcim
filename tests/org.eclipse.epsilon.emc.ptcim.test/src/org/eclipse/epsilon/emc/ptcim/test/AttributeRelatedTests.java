package org.eclipse.epsilon.emc.ptcim.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Timer;
import java.util.TimerTask;

import javax.rmi.CORBA.Util;

import org.eclipse.epsilon.emc.ptcim.jawin.DelME;
import org.eclipse.epsilon.emc.ptcim.jawin.JawinObject;
import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;
import org.jawin.COMException;
import org.jawin.DispatchPtr;
import org.jawin.Variant;
import org.jawin.win32.Ole32;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttributeRelatedTests {
	
	public static void main (String args[]) {
		try {
			AttributeRelatedTests foo = new AttributeRelatedTests();
			foo.setUp();
			foo.testPropertyGetter();
			foo.testPropertySetter();
			foo.testPropertyGetterTwiceSameElementSamePropertyNoChange();
			foo.testPropertyGetterTwiceSameElementSamePropertyWithChange();
			foo.testPropertyGetterTwiceSameElementDifferentProperty();
			foo.testPropertyGetterTwiceDifferentElement();
			foo.tearDown();
		} catch (COMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	public void testPropertyGetter() throws COMException {
		DispatchPtr softwarePackage = (DispatchPtr) model.invoke("Item", "Package", "Software");
		String strObjId = (String) softwarePackage.get("Property", "Id");
		assertEquals("80a27e73-0121-436a-abf7-1e01ebb33c7e", strObjId);
		System.out.println("Test property getter: Success");
	}
	
	@Test
	public void testPropertySetter() throws COMException {
		DispatchPtr softwarePackage = (DispatchPtr) model.invoke("Item", "Package", "Software");
		softwarePackage.invokeN("PropertySet", new Object[] {"Description", 0, "Test 2"});
		String strObjDescription = (String) softwarePackage.get("Property", "Description");
		assertEquals("Test 2", strObjDescription);
		// Reset back
		softwarePackage.invokeN("PropertySet", new Object[] {"Description", 0, "Test"});
		System.out.println("Test property setter: Success");
	}
	
	@Test
	public void testPropertyGetterTwiceSameElementSamePropertyNoChange() throws COMException {
		DispatchPtr softwarePackage = (DispatchPtr) model.invoke("Item", "Package", "Software");
		String strObjId = (String) softwarePackage.get("Property", "Id");
		assertEquals("80a27e73-0121-436a-abf7-1e01ebb33c7e", strObjId);
		strObjId = (String) softwarePackage.get("Property", "Id");
		assertEquals("80a27e73-0121-436a-abf7-1e01ebb33c7e", strObjId);
		System.out.println("Test property getter twice same element, same property, no change: Success");
	}
	
	@Test
	public void testPropertyGetterTwiceSameElementSamePropertyWithChange() throws COMException {
		DispatchPtr softwarePackage = (DispatchPtr) model.invoke("Item", "Package", "Software");
		String strObjDescription = (String) softwarePackage.get("Property", "Description");
		assertEquals("Test", strObjDescription);
		softwarePackage.invokeN("PropertySet", new Object[] {"Description", 0, "Test 2"});
		strObjDescription = (String) softwarePackage.get("Property", "Description");
		assertEquals("Test 2", strObjDescription);
		// Reset back
		softwarePackage.invokeN("PropertySet", new Object[] {"Description", 0, "Test"});
		System.out.println("Test property getter twice same element, same property, with change: Success");
	}
	
	@Test
	public void testPropertyGetterTwiceSameElementDifferentProperty() throws COMException {
		DispatchPtr softwarePackage = (DispatchPtr) model.invoke("Item", "Package", "Software");
		String strObjDescription = (String) softwarePackage.get("Property", "Description");
		assertEquals("Test", strObjDescription);
		String strObjId = (String) softwarePackage.get("Property", "Id");
		assertEquals("80a27e73-0121-436a-abf7-1e01ebb33c7e", strObjId);
		System.out.println("Test property getter twice same element, different property: Success");
	}
	
	@Test
	public void testPropertyGetterTwiceDifferentElement() throws COMException {
		DispatchPtr softwarePackage = (DispatchPtr) model.invoke("Item", "Package", "Software");
		String strObjDescription = (String) softwarePackage.get("Property", "Description");
		assertEquals("Test", strObjDescription);
		DispatchPtr hardwarePackage = (DispatchPtr) model.invoke("Item", "Package", "Hardware");
		String strObjId = (String) hardwarePackage.get("Property", "Id");
		assertEquals("fda68d40-8346-400c-bf40-70f24c80465d", strObjId);
		System.out.println("Test property getter twice different element: Success");
	}
}