package org.eclipse.epsilon.emc.ptcim.test;

import org.eclipse.epsilon.emc.ptcim.ClassFactory;
import org.eclipse.epsilon.emc.ptcim.IAutomationCaseObject;
import org.eclipse.epsilon.emc.ptcim.PtcimObject;

public class Utilities {
	
	private static IAutomationCaseObject projects;
	
	protected static PtcimObject getProject(String name) {
		projects = ClassFactory.createCCaseProjects();
		PtcimObject theProject = new PtcimObject(projects.item("Project", name).queryInterface(IAutomationCaseObject.class));
		return theProject;
	}
}
