package org.eclipse.epsilon.emc.ptcim.test;

import org.eclipse.epsilon.emc.ptcim.PtcimObject;
import org.eclipse.epsilon.emc.ptcim.util.ClassFactory;
import org.eclipse.epsilon.emc.ptcim.util.com4j.IAutomationCaseObject;
import org.junit.BeforeClass;

public abstract class AbstractTestSetup {

	private static IAutomationCaseObject projects;
	protected static PtcimObject theProject, theDictionary, softwarePackage, lightsClass, controllerClass,
			controllerStateMachineClass, usagePackage, maintenanceEng;

	@BeforeClass
	public static void setUp() throws Exception {
		theProject = AbstractTestSetup.getProject("Traffic Lights");
		theDictionary = theProject.item("Dictionary", null);
		softwarePackage = theDictionary.item("Package", "Software");
		lightsClass = theDictionary.item("Class", "Lights");
		controllerClass = softwarePackage.item("Class", "Controller");
		controllerStateMachineClass = controllerClass.item("State Machine", "Controller");
		usagePackage = theDictionary.item("Package", "Usage");
		maintenanceEng = usagePackage.item("Actor", "Maintenance Engineer");
	}

	protected static PtcimObject getProject(String name) {
		projects = ClassFactory.createCCaseProjects();
		PtcimObject theProject = new PtcimObject(
				projects.item("Project", name).queryInterface(IAutomationCaseObject.class));
		return theProject;
	}
}
