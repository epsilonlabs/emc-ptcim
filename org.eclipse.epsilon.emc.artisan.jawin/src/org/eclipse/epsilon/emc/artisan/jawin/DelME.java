package org.eclipse.epsilon.emc.artisan.jawin;


import java.util.ArrayList;
import java.util.Collection;

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
		DispatchPtr theProject = getActiveProject("EmcTest");
		DispatchPtr model = load(theProject);
//		//DispatchPtr model = load("HSUV");
//		String type = "Class";
//		//DispatchPtr allClasses = allofType(model, type);
//		DispatchPtr allInstances = new DispatchPtr();
//		DispatchPtr classDispPtr = (DispatchPtr) model.invokeN("Items", new Object[] {type});
//		allInstances.stealUnknown(classDispPtr);
//		while (hasMore(allInstances)) {
//			// Find if the class is a requirement 
//			DispatchPtr clzz = next(allInstances);
//			Object name = getAttr(clzz, "Name");
//			System.out.println(name);
//		}
//		System.out.println("\n ====== All Classes withj P* Name ===== \n");
//		DispatchPtr allPs = new DispatchPtr();
//		classDispPtr = (DispatchPtr) model.invokeN("Items", new Object[] {type, "P*"});
//		allPs.stealUnknown(classDispPtr);
//		while (hasMore(allPs)) {
//			// Find if the class is a requirement 
//			DispatchPtr clzz = next(allPs);
//			Object name = getAttr(clzz, "Name");
//			System.out.println(name);
//		}
		
		// Stereotypes
		DispatchPtr allSts = new DispatchPtr();
		DispatchPtr stsDispPtr = (DispatchPtr) model.invoke("Items", "Tag Definition");
		allSts.stealUnknown(stsDispPtr);
		while (hasMore(allSts)) {
			DispatchPtr pItem = next(allSts);
			Object name = getAttr(pItem, "Name");
			System.out.println(name);
			boolean visible = Boolean.valueOf((String)getAttr(pItem, "Released"));
			System.out.println(visible);
			if (!visible) {
				pItem.invoke("PropertySet", "Released", 0, "TRUE");
			}
		}
		
		// Size
		//int i = size(allClasses);
		//System.out.println(i);
		// Loop
//		Collection<Object> names = collect(allClasses, "Name");
//		for (Object n : names) {
//			System.out.println(n);
//		}
//		allClasses.invoke("ResetQueryItems");
		
		// Do we have the type, do a GetClassProperties assume error means calss is not present
//		Object classDispPtr = theProject.invokeN("GetClassProperties", new Object[] {"Class"});
//		List<String> list = Arrays.asList(((String) classDispPtr ).split("\\n"));
//		Object errDispPtr = theProject.invokeN("GetClassProperties", new Object[] {"MyClass"});
//		List<String> errlist = Arrays.asList(((String) classDispPtr ).split("\\n"));
		
		

//		DispatchPtr allPckItem = allofType(theProject, "Package Item");
//		while (hasMore(allPckItem)) {
//			DispatchPtr pItem = next(allPckItem);
//			Object name = getAttr(pItem, "Name");
//			System.out.println(name);
//			
//		}
		
		// Transaction?
		//ArtisanProject("Transaction") = "Begin"
//		theProject.invoke("PropertySet", "Transaction", 0, "Begin");
//		
//		
//		Create a new element by type, use the project
		//DispatchPtr newPck = (DispatchPtr) model.invokeN("Add", new Object[] {"Package"});
		//DispatchPtr newClass = (DispatchPtr) newPck.invokeN("Add", new Object[] {"Class"});
		//DispatchPtr newClassPck = (DispatchPtr) newClass.invokeN("Items", new Object[] {"Package"});
		//newClass.invokeN("Remove",  new Object[] {"Category"});
		//newClass.invokeN("Add",  new Object[] {"Package", newPck});
		
		//DispatchPtr newactorDispPtr = (DispatchPtr) newclassDispPtr.invokeN("AddByType", new Object[] {"Package", "Scoped Package"});
		
		
//		String old_props = null;
//		String props = null;
		
		// Show the class in the model
//		 DispatchPtr editor = new DispatchPtr("Studio.Editor");
//		 editor.invoke("ShowMainWindow");
//		 editor.invoke("SetForegroundWindow");
//		 editor.invoke("OpenModel","EmcTest");
		 
//
//		while (hasMore(allClasses)) {
//			// Find if the class is a requirement 
//			DispatchPtr clzz = next(allClasses);
//			Object name = getAttr(clzz, "Id");
//				System.out.println(name);
//				DispatchPtr stereotypes = new DispatchPtr();
//				// We know Requirements have id#
//				DispatchPtr classDispPtr = (DispatchPtr) clzz.invokeN("Items", new Object[] {"Tag Definitions", "id#"});
//				stereotypes.stealUnknown(classDispPtr);
//				Collection<Object> snames = collect(stereotypes, "Name");
//				System.out.println("Info: " + snames);
//				stereotypes.invoke("ResetQueryItems");
//				while (hasMore(stereotypes)) {
//					Object strPropDesc = clzz.get("Property", "Extended Attribute Descriptors");
//					System.out.println("strPropDesc: " + strPropDesc);
//
//					DispatchPtr tag = next(stereotypes);
//					DispatchPtr stereotypes1 = new DispatchPtr();
//					DispatchPtr classDispPtr1 = (DispatchPtr) tag.invokeN("Items", new Object[] {"Stereotype", "Requirement"});
//					stereotypes1.stealUnknown(classDispPtr1);
//					snames = collect(stereotypes1, "Name");
//					System.out.println("Info2: " + snames);
//					
//				}
				
			
			
//			// clzz id
			
//			Object id = clzz.get("Property", "Id");
//			// Find a diagram related to the class
//			DispatchPtr diag = new DispatchPtr();
//			// First Diagram
//			DispatchPtr diagDispPtr = (DispatchPtr) clzz.invoke("Item", "Using Diagram");
//			diag.stealUnknown(diagDispPtr);
//			Object dId = diag.get("Property", "Id");
//			Object objSymbol = clzz.invoke("Item", "Representing Symbol");
//			Object symboldId = ((DispatchPtr) objSymbol).get("Property", "Id");
//			editor.invoke("OpenDiagram", dId);
//			editor.invoke("SelectSymbol2", dId, symboldId);
				
			
//			//DispatchPtr newclass = (DispatchPtr) clzz.invokeN("AddByType", new Object[] {"Class", "CONTAINEDCLASS"}, 2);
//			// Add the new class to the class
//			DispatchPtr newclass = (DispatchPtr) clzz.invokeN("Add", new Object[] {"Contained Class", newclassDispPtr});
//			/*Object*/ name = getAttr(newclass, "Full Name");
//			Object owner = getAttr(newclass, "Scoping Item");
//			
//			System.out.println(name);
//			break;
//			if (name.equals("C1")) {
//				clzz.invokeN("PropertySet", new Object[] {"Name", 0, "C3"});
//			}
//			if (name.equals("C3")) {
//				clzz.invokeN("PropertySet", new Object[] {"Name", 0, "C1"});
//			} 
			// How to set associations? How to set multivalue associations?
			
			
//				Object cargo = clzz.getN("Property", new Object[] {"All Property Descriptors", null });
//				props = (String) cargo;
//				if (old_props != null) {
//					System.out.println(props.equals(old_props));
//				}
//				old_props = props;
//				List<String> list = Arrays.asList(((String) props ).split("\\n"));
//				for (String type : list) {
//					String[] info = type.split(",");
//					//String name1 = info[0];
//					//name1 = name1.replaceAll("^\"|\"$", "");
//					System.out.print(type);
//				}
//				Object type = clzz.getN("Property", new Object[] {"Type"});
//				Variant.ByrefHolder varArgument = new Variant.ByrefHolder(type);
//				Object classDispPtr = modelRef.invokeN("GetClassProperties", new Object[] {type});
//				List<String> list = Arrays.asList(((String) classDispPtr ).split("\\n"));
//				for (String type1 : list) {
//					String[] info = type1.split(",");
//					//String name1 = info[0];
//					//name1 = name1.replaceAll("^\"|\"$", "");
//					System.out.print(type1);
//				}
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
//				break;
			//}
//		}
		//ArtisanProject("Transaction") = "Abort"
//		theProject.invoke("PropertySet", "Transaction", 0, "Abort");
		
		Ole32.CoUninitialize();
		System.out.println("Success");
	}
	
	private static int size(DispatchPtr collection) throws COMException {
		Object classDispPtr = collection.invokeN("ItemCount", new Object[] {"Id"});
		//count.stealUnknown(classDispPtr);
		return (Integer)classDispPtr;
	}

	private static DispatchPtr load(DispatchPtr modelRef) throws COMException {
		DispatchPtr model = new DispatchPtr();
		Variant.ByrefHolder varIndex = new Variant.ByrefHolder("Dictionary");
		DispatchPtr dirDispPtr = (DispatchPtr) modelRef.invokeN("Item", new Object[] {
				"Dictionary", "Dictionary" });
		model.stealUnknown(dirDispPtr);
		return model;
	}

	private static DispatchPtr getActiveProject(String name) throws COMException {
		Ole32.CoInitialize();
		DispatchPtr app = new DispatchPtr("OMTE.Projects");
		String Index = name;
		String Role = "Project";
		Variant.ByrefHolder varIndex = new Variant.ByrefHolder(Index);
		DispatchPtr dispPtr = (DispatchPtr) app.invokeN("Item", new Object[] {
				Role, name});
		DispatchPtr modelRef = new DispatchPtr();
		modelRef.stealUnknown(dispPtr);
		return modelRef;
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
