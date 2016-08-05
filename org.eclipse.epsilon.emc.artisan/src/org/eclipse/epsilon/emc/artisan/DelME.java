package org.eclipse.epsilon.emc.artisan;

import org.eclipse.epsilon.emc.artisan.COM.modelexplorer.ModelExplorer;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WinNT.HRESULT;

//import org.jawin.COMException;
//import org.jawin.DispatchPtr;
//import org.jawin.Variant;
//import org.jawin.win32.Ole32;


public class DelME {
	
	
	
	public static void main(String[] args) {
		try {
			usingJNA();
		} catch (Exception e) {
		  e.printStackTrace();
		}
    }
	
	private static void usingJNA() {
		//HRESULT hr = Ole32.INSTANCE.CoInitializeEx(Pointer.NULL, Ole32.COINIT_MULTITHREADED);
		//HRESULT hr = Ole32.INSTANCE.CoInitialize(null);
        try {
        	ModelExplorer me = null;
        	me = new ModelExplorer();
        	me.openModel("HSUV");
    		System.out.println("Success");
        } finally {
            Ole32.INSTANCE.CoUninitialize();
        }
		
	}
	
//	private void usingJawin() throws COMException {
//		Ole32.CoInitialize();
//		DispatchPtr app = new DispatchPtr("OMTE.Projects");
//		//String Index = "HSUV";
//		String Index = "TEST";
//		String Role = "Project";
//		Variant.ByrefHolder varIndex = new Variant.ByrefHolder(Index);
//		DispatchPtr dispPtr = (DispatchPtr) app.invokeN("Item", new Object[] {
//				Role, varIndex });
//		DispatchPtr theActiveProject = new DispatchPtr();
//		theActiveProject.stealUnknown(dispPtr);
//		
//		DispatchPtr theDictionary = new DispatchPtr();
//		varIndex = new Variant.ByrefHolder("Dictionary");
//		DispatchPtr dirDispPtr = (DispatchPtr) theActiveProject.invokeN("Item", new Object[] {
//				"Dictionary", varIndex });
//		theDictionary.stealUnknown(dirDispPtr);
//		
//		DispatchPtr theClass = new DispatchPtr();
//		varIndex = new Variant.ByrefHolder("AccelerationEquation");
//		DispatchPtr classDispPtr = (DispatchPtr) theDictionary.invokeN("Item", new Object[] {
//				"Class", varIndex });
//		theClass.stealUnknown(classDispPtr);
//		Object o = theClass.getN("Property", new Object[] { "IsAbstract", null });
//		System.out.println(o);
//		//Object theDictionary = theActiveProject.Item("Dictionary", "Dictionary");
//		Ole32.CoUninitialize();
//		System.out.println("Success");
//	}

}
