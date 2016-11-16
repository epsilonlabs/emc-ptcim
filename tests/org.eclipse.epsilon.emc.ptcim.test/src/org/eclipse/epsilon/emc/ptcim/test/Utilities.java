package org.eclipse.epsilon.emc.ptcim.test;

import org.jawin.COMException;
import org.jawin.DispatchPtr;
import org.jawin.Variant;
import org.jawin.win32.Ole32;

public class Utilities {
	
	protected static DispatchPtr getProject(String name) throws COMException {
		Ole32.CoInitialize();
		DispatchPtr app = new DispatchPtr("OMTE.Projects");
		String Role = "Project";
		DispatchPtr dispPtr = (DispatchPtr) app.invokeN("Item", new Object[] {
				Role, name});
		DispatchPtr modelRef = new DispatchPtr();
		modelRef.stealUnknown(dispPtr);
		return modelRef;
	}
	
	protected static DispatchPtr load(DispatchPtr modelRef) throws COMException {
		DispatchPtr model = new DispatchPtr();
		Variant.ByrefHolder varIndex = new Variant.ByrefHolder("Dictionary");
		DispatchPtr dirDispPtr = (DispatchPtr) modelRef.invokeN("Item", new Object[] {
				"Dictionary", "Dictionary" });
		model.stealUnknown(dirDispPtr);
		return model;
	}

	protected static DispatchPtr allofType(DispatchPtr model, String type) throws COMException {
		DispatchPtr allInstances = new DispatchPtr();
		Variant.ByrefHolder varArgument = new Variant.ByrefHolder("*");
		DispatchPtr classDispPtr = (DispatchPtr) model.invokeN("Items", new Object[] {type, varArgument}, 2);
		allInstances.stealUnknown(classDispPtr);
		return allInstances;
	}
	
	protected static int size(DispatchPtr collection) throws COMException {
		Object classDispPtr = collection.invokeN("ItemCount", new Object[] {"Id"});
		return (Integer)classDispPtr;
	}
	
	protected static boolean hasMore(DispatchPtr collection) throws COMException {
		int i = ((Integer) collection.invokeN("MoreItems", new Object[] {})).intValue();
		// -1 -> has more items
		return i != 0;
	}

	protected static DispatchPtr next(DispatchPtr collection) throws COMException {
		DispatchPtr res = new DispatchPtr();
		DispatchPtr dispPtr = (DispatchPtr) collection.invoke("NextItem");
		res.stealUnknown(dispPtr);
		return res;
	}
}
