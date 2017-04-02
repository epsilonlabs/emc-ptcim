package org.eclipse.epsilon.emc.ptcim.jawin;

import java.util.ArrayList;

import org.jawin.COMException;
import org.jawin.DispatchPtr;
import org.jawin.GUID;
import org.jawin.win32.Ole32;

public class TestJawin {

	public static void main(String[] args) throws COMException {
		System.out.println("Jawin benchmarking started...");
		long start = System.nanoTime();
		Ole32.CoInitialize();
		GUID ptcimAppGuid = new GUID("{594B0CA2-7610-11D1-BA96-444553540000}");
		DispatchPtr ptcimApp = new DispatchPtr(ptcimAppGuid);
		for (int i=0;i<10000;i++) {
			DispatchPtr hsuvProject = (DispatchPtr) ptcimApp.invoke("Item", "Project", "HSUV");
			String id = (String) hsuvProject.get("Property", "id");
			String description = (String) hsuvProject.get("Property", "description");
			String name = (String) hsuvProject.get("Property", "name");
			
			DispatchPtr trafficLightsProject = (DispatchPtr) ptcimApp.invoke("Item", "Project", "Traffic Lights");
			id = (String) trafficLightsProject.get("Property", "id");
			description = (String) trafficLightsProject.get("Property", "description");
			name = (String) trafficLightsProject.get("Property", "name");

			DispatchPtr trafficLightsDictionary = (DispatchPtr) trafficLightsProject.invoke("Item", "Dictionary", "Dictionary");
			DispatchPtr lightsClass = (DispatchPtr) trafficLightsDictionary.invoke("Item", "Class", "Lights");
			description = (String) lightsClass.get("Property", "description");			
		}
		long elapsed = System.nanoTime() - start;
		System.out.println("Elapsed: " + elapsed/1000000000.0 + " secs");
		Ole32.CoUninitialize();
	}

}
