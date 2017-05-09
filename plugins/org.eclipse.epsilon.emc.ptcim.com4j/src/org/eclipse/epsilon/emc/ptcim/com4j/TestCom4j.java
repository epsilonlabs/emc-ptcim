package org.eclipse.epsilon.emc.ptcim.com4j;

import com4j.Com4jObject;

public class TestCom4j {

	public static void main(String[] args) {
		System.out.println("com4j benchmarking started...");
		long start = System.nanoTime();
		// You need to run com4j tlbimp.jar once which queries and gets back all the available COM methods that the application supports.
		// For PTC IM it generated 3 interfaces containing all the known methods PTC IM offer through the automation interface (thos available in VB as well)
		// It also generates a ClassFactory which creates a singleton class of the top COM object.
		// All the other methods available in the 3 interfaces are calling the appropriate COM method using the COM4J library at runtime.
		IAutomationCaseObject co = ClassFactory.createCCaseProjects();
		for (int i=0;i<1;i++) {
			Com4jObject hsuvProject = co.item("Project", "HSUV");
			IAutomationCaseObject hsuvProjectCaseObject = hsuvProject.queryInterface(IAutomationCaseObject.class);
			Object id =  hsuvProjectCaseObject.property("id", null);
			Object description = hsuvProjectCaseObject.property("description", null);
			Object name = hsuvProjectCaseObject.property("name", null);
			
			IAutomationCaseObject trafficLightsProject = co.item("Project", "Traffic Lights").queryInterface(IAutomationCaseObject.class);
			//IAutomationCaseObject trafficLightsProject = co.item("Reference", "\\\\Enabler\\DESKTOP-V4OK65F\\Examples\\Traffic Lights").queryInterface(IAutomationCaseObject.class);
			id =  trafficLightsProject.property("id", null);
			description = trafficLightsProject.property("description", null);
			name = trafficLightsProject.property("name", null);
			IAutomationCaseObject trafficLightsDictionary = trafficLightsProject.item("Dictionary", "Dictionary").queryInterface(IAutomationCaseObject.class);
			IAutomationCaseObject lightsClass = trafficLightsDictionary.item("Class", "Lights").queryInterface(IAutomationCaseObject.class);
			Object lightsClassDescription = lightsClass.property("description", null);
		}
		long elapsed = System.nanoTime() - start;
		System.out.println("Elapsed: " + elapsed/1000000000.0 + " secs");
	}

}
