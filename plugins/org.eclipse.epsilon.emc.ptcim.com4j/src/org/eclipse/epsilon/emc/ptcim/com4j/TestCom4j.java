package org.eclipse.epsilon.emc.ptcim.com4j;

import com4j.Com4jObject;

public class TestCom4j {

	public static void main(String[] args) {
		IAutomationCaseObject co = ClassFactory.createCCaseProjects();
		Com4jObject ap = co.item("Active Project", null);
		IAutomationCaseObject apCo = ap.queryInterface(IAutomationCaseObject.class);
		System.out.println("Ap: " + apCo.property("id", null));
	}

}
