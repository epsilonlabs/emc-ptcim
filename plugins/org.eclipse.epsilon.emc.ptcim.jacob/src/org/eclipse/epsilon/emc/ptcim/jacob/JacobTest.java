package org.eclipse.epsilon.emc.ptcim.jacob;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class JacobTest {

	public static void main(String[] args) {
		// Locate the app using its unique clsid
		ActiveXComponent ptcimApp = new ActiveXComponent("clsid:{594B0CA2-7610-11D1-BA96-444553540000}");
		
		// Invoke Item command with "Active Project" argument to get the active project
		ActiveXComponent activeProject = ptcimApp.invokeGetComponent("Item", new Variant("Active Project"));
		
		// The following commented command is equivalent to the above but using another method ("Dispatch.call" instead of "invokeGetComponent") 
		// which returns a Variant ("casted" to Dispatch using getDispatch). Same functionality - experimentation just to understand how it works
		//Dispatch activeProject = Dispatch.call(ptcimApp, "Item", "Active Project").getDispatch();

		// Call the "Property" method with the "id" argument to get the id
		Variant activeProjectId = Dispatch.call(activeProject, "Property", "id");
		System.out.println("Active project id: " + activeProjectId);
	}

}
