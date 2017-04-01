package org.eclipse.epsilon.emc.ptcim.jacob;

import java.util.ArrayList;
import java.util.List;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class JacobTest {

	public static void main(String[] args) {
		long start = System.nanoTime();
		// Locate the app using its unique clsid
		ActiveXComponent ptcimApp = new ActiveXComponent("clsid:{594B0CA2-7610-11D1-BA96-444553540000}");
		for (int i=0;i<100;i++) {
			Dispatch hsuvProject = Dispatch.call(ptcimApp, "Item", new Object[]{"Project", "HSUV"}).getDispatch();
			Variant id = Dispatch.call(hsuvProject, "Property", "id");
			Variant description = Dispatch.call(hsuvProject, "Property", "description");
			Variant name = Dispatch.call(hsuvProject, "Property", "name");

			Dispatch trafficLights = Dispatch.call(ptcimApp, "Item", new Object[]{"Project", "Traffic Lights"}).getDispatch();
			id = Dispatch.call(trafficLights, "Property", "id");
			description = Dispatch.call(trafficLights, "Property", "description");
			name = Dispatch.call(trafficLights, "Property", "name");

			Dispatch trafficLightsDictionary = Dispatch.call(trafficLights, "Item", new Object[]{"Dictionary", "Dictionary"}).getDispatch();
			Dispatch lightsClass = Dispatch.call(trafficLightsDictionary, "Item", new Object[]{"Class", "Lights"}).getDispatch();
			description = Dispatch.call(lightsClass, "Property", "description");
		}
		long elapsed = System.nanoTime() - start;
		System.out.println("Elapsed: " + elapsed/1000000000.0 + " secs");
	}

}
