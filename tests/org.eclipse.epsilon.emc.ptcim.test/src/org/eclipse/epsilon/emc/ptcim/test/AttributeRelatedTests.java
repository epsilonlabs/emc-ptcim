package org.eclipse.epsilon.emc.ptcim.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.epsilon.emc.ptcim.PtcimObject;
import org.junit.Test;

public class AttributeRelatedTests extends AbstractTestSetup {
	
	private static final String DIFF = " DIFF";
		
	@Test
	public void testPropertyGetter() {
		String strObjId = (String) softwarePackage.property("Id", null);
		assertEquals("80a27e73-0121-436a-abf7-1e01ebb33c7e", strObjId);
	}
	
	@Test
	public void testPropertySetter() {
		softwarePackage.propertySet("Description", 0, "New Description");
		String strObjDescription = (String) softwarePackage.property("Description", null);
		assertEquals("New Description", strObjDescription);
		// Reset back
		softwarePackage.propertySet("Description", 0, "Test");
	}
	
	@Test
	public void testPropertyGetterTwiceSameElementSamePropertyNoChange() {
		String strObjId = (String) softwarePackage.property("Id", null);
		assertEquals("80a27e73-0121-436a-abf7-1e01ebb33c7e", strObjId);
		strObjId = (String) softwarePackage.property("Id", null);
		assertEquals("80a27e73-0121-436a-abf7-1e01ebb33c7e", strObjId);
	}
	
	@Test
	public void testPropertyGetterTwiceSameElementSamePropertyWithChange() {
		String strObjDescription = (String) softwarePackage.property("Description", null);
		assertEquals("Test", strObjDescription);
		softwarePackage.propertySet("Description", 0, "New Description");
		strObjDescription = (String) softwarePackage.property("Description", null);
		assertEquals("New Description", strObjDescription);
		// Reset back
		softwarePackage.propertySet("Description", 0, "Test");
	}
	
	@Test
	public void testPropertyGetterTwiceSameElementDifferentProperty() {
		String strObjDescription = (String) softwarePackage.property("Description", null);
		assertEquals("Test", strObjDescription);
		String strObjId = (String) softwarePackage.property("Id", null);
		assertEquals("80a27e73-0121-436a-abf7-1e01ebb33c7e", strObjId);
	}
	
	@Test // FIXME Why do we need twice different element?
	public void testPropertyGetterTwiceDifferentElement() {
		String strObjDescription = (String) softwarePackage.property("Description", null);
		assertEquals("Test", strObjDescription);
		PtcimObject hardwarePackage = theDictionary.item("Package", "Hardware");
		String strObjId = (String) hardwarePackage.property("Id", null);
		assertEquals("fda68d40-8346-400c-bf40-70f24c80465d", strObjId);
	}
	
	// NORMALISATION TESTS
	
	@Test
	public void testNormalisation_GetNoSpaceAttribute() {
		String expected = "80a27e73-0121-436a-abf7-1e01ebb33c7e";
		String ptcName = (String) softwarePackage.property("Id", null);
		assertEquals("PTC Name", expected, ptcName);
		String lowerCaps = (String) softwarePackage.property("id", null);
		assertEquals("Lower Caps", expected, lowerCaps);
	}
	
	@Test
	public void testNormalisation_GetSpacedAttribute() {
		String expected = ""; // TODO find a property with an expected value different than ""
		String ptcName = (String) controllerClass.property("C++ Header Include", null);
		assertEquals("PTC Name", expected, ptcName);
		String lowerCaps = (String) controllerClass.property("c++ header include", null);
		assertEquals("Lower Caps", expected, lowerCaps);
		String noSpace = (String) controllerClass.property("C++HeaderInclude", null);
		assertEquals("No Space", expected, noSpace);
		String noSpaceLowerCaps = (String) controllerClass.property("c++headerinclude", null);
		assertEquals("No Space Lower Caps", expected, noSpaceLowerCaps);
	}
	
	@Test
	public void testNormalisation_SetNoSpaceAttribute() {
		String exp = (String) softwarePackage.property("Description", null);
		softwarePackage.propertySet("Description", 0, exp + DIFF);
		assertEquals( exp + DIFF, (String) softwarePackage.property("Description", null));
		softwarePackage.propertySet("Description", 0, exp); // Reset
		
		softwarePackage.propertySet("description", 0, exp + DIFF);
		assertEquals( exp + DIFF, (String) softwarePackage.property("Description", null));
		softwarePackage.propertySet("description", 0, exp); // Reset
	}
	
	@Test
	public void testNormalisation_SetSpacedAttribute() {
		String exp = (String) controllerClass.property("C++ Header Include", null);
		controllerClass.propertySet("C++ Header Include", 0, exp + DIFF);
		assertEquals( exp + DIFF, (String) controllerClass.property("C++ Header Include", null));
		controllerClass.propertySet("C++ Header Include", 0, exp); // Reset
		assertEquals( exp, (String) controllerClass.property("C++ Header Include", null));
		
		controllerClass.propertySet("c++ header include", 0, exp + DIFF);
		assertEquals( exp + DIFF, (String) controllerClass.property("C++ Header Include", null));
		controllerClass.propertySet("c++ header include", 0, exp); // Reset
		assertEquals( exp, (String) controllerClass.property("C++ Header Include", null));
		
		controllerClass.propertySet("C++HeaderInclude", 0, exp + DIFF);
		assertEquals( exp + DIFF, (String) controllerClass.property("C++ Header Include", null));
		controllerClass.propertySet("C++HeaderInclude", 0, exp); // Reset
		assertEquals( exp, (String) controllerClass.property("C++ Header Include", null));
		
		controllerClass.propertySet("c++headerinclude", 0, exp + DIFF);
		assertEquals( exp + DIFF, (String) controllerClass.property("C++ Header Include", null));
		controllerClass.propertySet("c++headerinclude", 0, exp); // Reset
		assertEquals( exp, (String) controllerClass.property("C++ Header Include", null));
	}
	
	
	@Test
	public void testNormalisation_GetNoSpaceType() {
		String expected = (String) softwarePackage.property("Name", null);
		PtcimObject UU = theDictionary.item("Package", "Software");
		assertEquals("UU", expected, (String) UU.property("Name", null));
		PtcimObject UL = theDictionary.item("Package", "software");
		assertEquals("UL", expected, (String) UL.property("Name", null));
		PtcimObject LU = theDictionary.item("package", "Software");
		assertEquals("LU", expected, (String) LU.property("Name", null));
		PtcimObject LL = theDictionary.item("package", "software");
		assertEquals("LL", expected, (String) LL.property("Name", null));
	}
	
	@Test
	public void testNormalisation_GetSpacedType() {
		PtcimObject controllerSMObject = theDictionary.item("State Machine", "Controller");
		String expected = (String) controllerSMObject.property("Name", null);
		PtcimObject UU = controllerSMObject;
		assertEquals("UU", expected, (String) UU.property("Name", null));
		PtcimObject LU = theDictionary.item("state machine", "Controller");
		assertEquals("LU", expected, (String) LU.property("Name", null));		
		PtcimObject UUS = theDictionary.item("StateMachine", "Controller");
		assertEquals("UUS", expected, (String) UUS.property("Name", null));
		PtcimObject LUS = theDictionary.item("statemachine", "Controller");
		assertEquals("LUS", expected, (String) LUS.property("Name", null));
	}
	
}