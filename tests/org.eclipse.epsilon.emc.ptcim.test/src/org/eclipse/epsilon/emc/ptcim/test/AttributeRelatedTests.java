package org.eclipse.epsilon.emc.ptcim.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.epsilon.emc.ptcim.IAutomationCaseObject;
import org.eclipse.epsilon.emc.ptcim.PtcimObject;
import org.junit.Before;
import org.junit.Test;

public class AttributeRelatedTests {
	
	PtcimObject theProject, theDictionary, softwarePackage,lightsClass;
	
	@Before
	public void setUp() throws Exception {
		theProject = Utilities.getProject("Traffic Lights");
		theDictionary = new PtcimObject(theProject.item("Dictionary", null).queryInterface(IAutomationCaseObject.class));
		softwarePackage = new PtcimObject(theDictionary.item("Package", "Software").queryInterface(IAutomationCaseObject.class));
		lightsClass = new PtcimObject(theDictionary.item("Class", "Lights").queryInterface(IAutomationCaseObject.class));

	}
		
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
	
	@Test
	public void testPropertyGetterTwiceDifferentElement() {
		String strObjDescription = (String) softwarePackage.property("Description", null);
		assertEquals("Test", strObjDescription);
		PtcimObject hardwarePackage = new PtcimObject(theDictionary.item("Package", "Hardware").queryInterface(IAutomationCaseObject.class));
		String strObjId = (String) hardwarePackage.property("Id", null);
		assertEquals("fda68d40-8346-400c-bf40-70f24c80465d", strObjId);
	}
	
	// NORMALISATION RELATED : Getters
	
	@Test
	public void testNormalisation_getSpacedReferenceOriginal() {
		String tokenName = (String) lightsClass.property("Token Name", null);
		assertEquals("\\£4344218c-fbf2-11d2-a53d-00104bb05af8\\£", tokenName);
	}
	
	@Test
	public void testNormalisation_getSpacedReferenceLowerCaps() {
		String tokenName = (String) lightsClass.property("token name", null);
		assertEquals("\\£4344218c-fbf2-11d2-a53d-00104bb05af8\\£", tokenName);
	}
	
	@Test
	public void testNormalisation_getSpacedReferenceNoSpace() {
		String tokenName = (String) lightsClass.property("TokenName", null);
		assertEquals("\\£4344218c-fbf2-11d2-a53d-00104bb05af8\\£", tokenName);
	}
	
	@Test
	public void testNormalisation_getSpacedReferenceNoSpaceLowerCaps() {
		String tokenName = (String) lightsClass.property("tokenname", null);
		assertEquals("\\£4344218c-fbf2-11d2-a53d-00104bb05af8\\£", tokenName);
	}
	
	// NORMALISATION RELATED : Setters
	
	@Test
	public void testNormalisation_setSpacedReferenceOriginal() {
		String originalValue = (String) lightsClass.property("Token Text", null);
		
		String tempValue = "New value1";
		lightsClass.propertySet("Token Text", 0, tempValue);
		String newValue = (String) lightsClass.property("Token Text", null);
		assertEquals(tempValue, newValue);
		
		lightsClass.propertySet("Token Text", 0, originalValue);
	}
	
	@Test
	public void testNormalisation_setSpacedReferenceLowerCaps() {
		String originalValue = (String) lightsClass.property("Token Text", null);
		
		String tempValue = "New value2";
		lightsClass.propertySet("Token Text", 0, tempValue);
		String newValue = (String) lightsClass.property("token text", null);
		assertEquals(tempValue, newValue);
		
		lightsClass.propertySet("Token Text", 0, originalValue);
	}
	
	@Test
	public void testNormalisation_setSpacedReferenceNoSpace() {
		String originalValue = (String) lightsClass.property("Token Text", null);
		
		String tempValue = "New value3";
		lightsClass.propertySet("Token Text", 0, tempValue);
		String newValue = (String) lightsClass.property("TokenText", null);
		assertEquals(tempValue, newValue);
		
		lightsClass.propertySet("Token Text", 0, originalValue);
	}
	
	@Test
	public void testNormalisation_setSpacedReferenceNoSpaceLowerCaps() {
		String originalValue = (String) lightsClass.property("Token Text", null);

		String tempValue = "New value4";
		lightsClass.propertySet("Token Text", 0, tempValue);
		String newValue = (String) lightsClass.property("tokentext", null);
		assertEquals(tempValue, newValue);
		
		lightsClass.propertySet("Token Text", 0, originalValue);
	}
	
}