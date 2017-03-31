package org.eclipse.epsilon.emc.ptcim.com4j;

import com4j.Com4jObject;

public class TestCom4j {

	public static void main(String[] args) {
		
		// You need to run com4j tlbimp.jar once which queries and gets back all the available COM methods that the application supports.
		// For PTC IM it generated 3 interfaces containing all the known methods PTC IM offer through the automation interface (thos available in VB as well)
		// It also generates a ClassFactory which creates a singleton class of the top COM object.
		// All the other methods available in the 3 interfaces are calling the appropriate COM method using the COM4J library at runtime.
		
		IAutomationCaseObject co = ClassFactory.createCCaseProjects();
		// Use the "item" method of the auto generated interface that invokes the "item" method of the COM object at runtime.
		Com4jObject ap = co.item("Active Project", null);
		// According to documentation this is the correct (only) way of casting objects in com4j (equivalent to (IAutomationCaseObject) ap which is not supported by com4j)
		IAutomationCaseObject apCo = ap.queryInterface(IAutomationCaseObject.class);
		// Invoke the "property" interface method which invokes the "property" COM method at runtime.
		System.out.println("Ap: " + apCo.property("id", null));
	}

}
