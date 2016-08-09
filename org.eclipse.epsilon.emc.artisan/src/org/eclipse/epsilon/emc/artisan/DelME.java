package org.eclipse.epsilon.emc.artisan;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jawin.COMException;
import org.jawin.DispatchPtr;
import org.jawin.Variant;
import org.jawin.win32.Ole32;

public class DelME {
	
	
	
	public static void main(String[] args) {
		try {
			usingJawin();
		} catch (Exception e) {
		  e.printStackTrace();
		}
    }
	
//	private static void usingJNA() {
//		//HRESULT hr = Ole32.INSTANCE.CoInitializeEx(Pointer.NULL, Ole32.COINIT_MULTITHREADED);
//		//HRESULT hr = Ole32.INSTANCE.CoInitialize(null);
//        try {
//        	ModelExplorer me = null;
//        	me = new ModelExplorer();
//        	me.openModel("HSUV");
//    		System.out.println("Success");
//        } finally {
//            Ole32.INSTANCE.CoUninitialize();
//        }
//		
//	}
	
	private static void usingJawin() throws COMException {
		DispatchPtr model = load("EmcTest");
		//DispatchPtr model = load("HSUV");
		DispatchPtr allClasses = allofType(model, "Class");
		// Size
		//int i = size(allClasses);
		//System.out.println(i);
		// Loop
		Collection<Object> names = collect(allClasses, "Name");
		for (Object n : names) {
			System.out.println(n);
		}
		allClasses.invoke("ResetQueryItems");
		while (hasMore(allClasses)) {
			DispatchPtr clzz = next(allClasses);
			Object name = getAttr(clzz, "Attribute Order");
			System.out.println(name);
			
			//if (name.equals("C1")) {
				Object cargo = clzz.getN("Property", new Object[] { "All Property Descriptors", null });
				String props = (String) cargo;
				List<String> list = Arrays.asList(((String) props ).split("\\n"));
				for (String type : list) {
					String[] info = type.split(",");
					//String name1 = info[0];
					//name1 = name1.replaceAll("^\"|\"$", "");
					System.out.print(type);
				}
//				// For multivalue properties, Property only returns the first element (name)
//				Object cargo = clzz.getN("Property", new Object[] { "parts", null });
//				// Blocks have parts
//				DispatchPtr allParts = new DispatchPtr();
//				Variant.ByrefHolder varArgument = new Variant.ByrefHolder("*");
//				DispatchPtr classDispPtr = (DispatchPtr) clzz.invokeN("Items", new Object[] {"All Property Descriptors", varArgument}, 2);
//				allParts.stealUnknown(classDispPtr);
//				names = collect(allParts, "Aggregate");
//				for (Object n : names) {
//					System.out.println(n);
//				}
				break;
			//}
		}
		Ole32.CoUninitialize();
		System.out.println("Success");
	}
	
	private static int size(DispatchPtr allClasses) throws COMException {
		DispatchPtr count = new DispatchPtr();
		Variant.ByrefHolder varArgument = new Variant.ByrefHolder("*");
		Object classDispPtr = allClasses.invokeN("ItemCount", new Object[] {"Id"});
		//count.stealUnknown(classDispPtr);
		return (Integer)classDispPtr;
	}

	private static DispatchPtr load(String name) throws COMException {
		Ole32.CoInitialize();
		DispatchPtr app = new DispatchPtr("OMTE.Projects");
		String Index = name;
		String Role = "Project";
		Variant.ByrefHolder varIndex = new Variant.ByrefHolder(Index);
		DispatchPtr dispPtr = (DispatchPtr) app.invokeN("Item", new Object[] {
				Role, varIndex });
		DispatchPtr modelRef = new DispatchPtr();
		modelRef.stealUnknown(dispPtr);
		DispatchPtr model = new DispatchPtr();
		varIndex = new Variant.ByrefHolder("Dictionary");
		DispatchPtr dirDispPtr = (DispatchPtr) modelRef.invokeN("Item", new Object[] {
				"Dictionary", varIndex });
		model.stealUnknown(dirDispPtr);
		return model;
	}
	
	
	private static DispatchPtr allofType(DispatchPtr model, String type) throws COMException {
		DispatchPtr allInstances = new DispatchPtr();
		Variant.ByrefHolder varArgument = new Variant.ByrefHolder("*");
		DispatchPtr classDispPtr = (DispatchPtr) model.invokeN("Items", new Object[] {type, varArgument}, 2);
		allInstances.stealUnknown(classDispPtr);
		return allInstances;
	}
	

	private static Collection<Object> collect(DispatchPtr allClasses, String attr) throws COMException {
		ArrayList<Object> res = new ArrayList<Object>();
		while (hasMore(allClasses)) {
			DispatchPtr clzz = next(allClasses);
			res.add(getAttr(clzz, attr));
		}
		return res;
	}
	
	private static boolean hasMore(DispatchPtr allClasses) throws COMException {
		int i = ((Integer) allClasses.invokeN("MoreItems", new Object[] {})).intValue();
		// -1 -> has more items
		return i != 0;
	}

	private static DispatchPtr next(DispatchPtr allClasses) throws COMException {
		DispatchPtr res = new DispatchPtr();
		DispatchPtr dispPtr = (DispatchPtr) allClasses.invoke("NextItem");
		res.stealUnknown(dispPtr);
		return res;
	}
	
	private static Object getAttr(DispatchPtr clzz, String name) throws COMException {
		Object o = clzz.getN("Property", new Object[] { name, null });
		return o;
	}	

}
