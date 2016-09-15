package org.eclipse.epsilon.emc.artisan.jawin.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.epsilon.emc.ptcim.jawin.JawinCollection;
import org.eclipse.epsilon.emc.ptcim.jawin.JawinObject;
import org.eclipse.epsilon.emc.ptcim.ole.EpsilonCOMException;
import org.jawin.COMException;
import org.jawin.DispatchPtr;
import org.jawin.Variant;
import org.jawin.win32.Ole32;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// TODO The model and project should probably bee a mock. The specific actions for modifying the Artisan model
// should be in another test class
public class JawinCollectionTests {
	
	private JawinObject model;
	private DispatchPtr theProject;
	
	@Before
	public void setUp() throws Exception {
		theProject = getActiveProject("EmcTest");
		DispatchPtr modelptr = load(theProject);
		model = new JawinObject(modelptr);
		
	}
	

	@After
	public void tearDown() throws Exception {
		try {
			Ole32.CoUninitialize();
		} catch (COMException e) {
			throw new EpsilonCOMException(e);
		}
	}

	@Test
	public void testSize() throws Exception {
		
		DispatchPtr ptr = getAssociation(model.getDelegate(), "Class");
		JawinObject coll = new JawinObject(ptr);
		Collection<JawinObject> clss = new JawinCollection(coll, model, "Class");
		assertEquals(2, clss.size());
	}

	@Test
	public void testAddJawinObject_OK() throws Exception {
		theProject.invoke("PropertySet", "Transaction", 0, "Begin");
		String type = "Attribute";
		DispatchPtr newAttrPtr = null;
		String id = null;
		try {
			newAttrPtr = (DispatchPtr) model.getDelegate().invokeN("AddByType", new Object[] {type, type.toUpperCase()}, 2);
			newAttrPtr.invoke("PropertySet", "Name", 0, "NewAttr");
			id = (String) newAttrPtr.get("Property", "Id");
		} catch (COMException e) {
			theProject.invoke("PropertySet", "Transaction", 0, "Abort");
			fail("Couldn't create new item");
		}
		JawinObject newAttr = new JawinObject(newAttrPtr);
		newAttr.setId(id);
		DispatchPtr c1Ptr = null;
		try {
			c1Ptr = (DispatchPtr) model.getDelegate().invokeN("Item", new Object[] {"Class", "C1"});
		} catch (COMException e) {
			theProject.invoke("PropertySet", "Transaction", 0, "Abort");
			fail("Couldn't get class C1");
		}
		JawinObject c1 = new JawinObject(c1Ptr);
		DispatchPtr attrPtr = null;
		try {
			attrPtr = getAssociation(c1Ptr, "Attribute");
		} catch (COMException e) {
			theProject.invoke("PropertySet", "Transaction", 0, "Abort");
			fail("Couldn't get C1's attributes");
		}
		JawinObject coll = new JawinObject(attrPtr);
		Collection<JawinObject> clss = new JawinCollection(coll, c1, "Attribute");
		boolean added = clss.add(newAttr);
		theProject.invoke("PropertySet", "Transaction", 0, "Abort");
		assertTrue(added);
		
	}
	
	@Test
	public void testAddJawinObject_ERR() throws Exception {
		// FIXME Seems that the order is important, maybe because of the transanctions
//		theProject.invoke("PropertySet", "Transaction", 0, "Begin");
//		DispatchPtr c1Ptr = null;
//		try {
//			c1Ptr = (DispatchPtr) model.getDelegate().invokeN("Item", new Object[] {"Class", "C1"});
//		} catch (COMException e) {
//			theProject.invoke("PropertySet", "Transaction", 0, "Abort");
//			fail("Couldn't get class C1");
//		}
//		JawinObject c1 = new JawinObject(c1Ptr);
//		DispatchPtr attrPtr = null;
//		try {
//			attrPtr = getAssociation(c1Ptr, "Attribute");
//		} catch (COMException e) {
//			theProject.invoke("PropertySet", "Transaction", 0, "Abort");
//			fail("Couldn't get C1's attributes");
//		}
//		JawinObject coll = new JawinObject(attrPtr);
//		Collection<JawinObject> clss = new JawinCollection(coll, c1, "Attribute");
//		// Attrib1
//		String type = "Attribute";
//		DispatchPtr newAttrPtr = null;
//		String id = null;
//		try {
//			newAttrPtr = (DispatchPtr) model.getDelegate().invokeN("AddByType", new Object[] {type, type.toUpperCase()}, 2);
//			newAttrPtr.invoke("PropertySet", "Name", 0, "NewAttr");
//			id = (String) newAttrPtr.get("Property", "Id");
//		} catch (COMException e) {
//			theProject.invoke("PropertySet", "Transaction", 0, "Abort");
//			fail("Couldn't create new item");
//		}
//		JawinObject newAttr = new JawinObject(newAttrPtr);
//		newAttr.setId(id);
//		boolean add = clss.add(newAttr);
//		assertTrue(add);
//		// Attrib2
//		DispatchPtr newAttrPtr1 = null;
//		id = null;
//		try {
//			newAttrPtr1 = (DispatchPtr) model.getDelegate().invokeN("AddByType", new Object[] {type, type.toUpperCase()}, 2);
//			newAttrPtr1.invoke("PropertySet", "Name", 0, "NewAttr");
//			id = (String) newAttrPtr1.get("Property", "Id");
//		} catch (COMException e) {
//			theProject.invoke("PropertySet", "Transaction", 0, "Abort");
//			fail("Couldn't create new item");
//		}
//		JawinObject newAttr1 = new JawinObject(newAttrPtr1);
//		newAttr1.setId(id);
//		// We can't add attrib with same name
//		boolean added = clss.add(newAttr1);
//		theProject.invoke("PropertySet", "Transaction", 0, "Abort");
//		assertFalse(added);
	}

	@Test
	public void testIterator() throws Exception {
		DispatchPtr ptr = getAssociation(model.getDelegate(), "Class");
		JawinObject coll = new JawinObject(ptr);
		Collection<JawinObject> clss = new JawinCollection(coll, model, "Class");
		Iterator<JawinObject> it = clss.iterator();
		assertNotNull(it);
		assertTrue(it.hasNext());
		assertNotNull(it.next());
	}

	@Test
	public void testRemoveObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveAllCollectionOfQ() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEmpty() throws Exception {
		DispatchPtr ptr = getAssociation(model.getDelegate(), "Class");
		JawinObject coll = new JawinObject(ptr);
		Collection<JawinObject> clss = new JawinCollection(coll, model, "Class");
		assertFalse(clss.isEmpty());
		ptr = getAssociation(model.getDelegate(), "Actor");
		coll = new JawinObject(ptr);
		Collection<JawinObject> actors = new JawinCollection(coll, model, "Actor");
		assertTrue(actors.isEmpty());
	}

	@Test
	public void testContains() throws Exception {
		// FIXME Seems that the order is important, maybe because of the transactions
//		DispatchPtr ptr = getAssociation(model.getDelegate(), "Class");
//		JawinObject coll = new JawinObject(ptr);
//		Collection<JawinObject> clss = new JawinCollection(coll, model, "Class");
//		// Get the first class
//		List<Object> args = new ArrayList<Object>();
//		args.add("Class");
//		JawinObject objPtr = (JawinObject) model.invoke("Item", args);
//		String strId = (String) objPtr.get("Property", "Id");
//		objPtr.setId(strId);
//		assertTrue(clss.contains(objPtr));
//		// Get the first Attribute
//		args = new ArrayList<Object>();
//		args.add("Attribute");
//		JawinObject actPtr = (JawinObject) model.invoke("Item", args);
//		String actId = (String) actPtr.get("Property", "Id");
//		actPtr.setId(actId);
//		assertFalse(clss.contains(actPtr));
	}
	

	@Test
	public void testToArray() throws Exception {
		DispatchPtr ptr = getAssociation(model.getDelegate(), "Class");
		JawinObject coll = new JawinObject(ptr);
		Collection<JawinObject> clss = new JawinCollection(coll, model, "Class");
		Object[] arr = clss.toArray();
		int i = 0;
		Iterator<JawinObject> it = clss.iterator();
		while (it.hasNext()) {
			assertEquals(it.next(), arr[i++]);
		}
	}

	@Test
	public void testToArrayTArray() throws Exception {
		DispatchPtr ptr = getAssociation(model.getDelegate(), "Class");
		JawinObject coll = new JawinObject(ptr);
		Collection<JawinObject> clss = new JawinCollection(coll, model, "Class");
		JawinObject[] arr = clss.toArray(new JawinObject[]{});
		for(int i = 0;i<arr.length;i++) {
			assertTrue(arr[i] instanceof JawinObject);
		}
	}

	@Test
	public void testContainsAll() throws Exception {
		DispatchPtr ptr = getAssociation(model.getDelegate(), "Class");
		JawinObject coll = new JawinObject(ptr);
		Collection<JawinObject> clss = new JawinCollection(coll, model, "Class");
		Collection<JawinObject> clss2 = new ArrayList<JawinObject>();
		Iterator<JawinObject> it = clss.iterator();
		while (it.hasNext()) {
			clss2.add(it.next());
		}
		assertTrue(clss.containsAll(clss2));
	}

	@Test
	public void testAddAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetainAll() throws Exception {
		// TODO we need to override this method or add the remove behavior to the iterator
		fail("Not yet implemented");
//		theProject.invoke("PropertySet", "Transaction", 0, "Begin");
//		DispatchPtr c1Ptr = null;
//		try {
//			c1Ptr = (DispatchPtr) model.getDelegate().invokeN("Item", new Object[] {"Class", "C1"});
//		} catch (COMException e) {
//			theProject.invoke("PropertySet", "Transaction", 0, "Abort");
//			fail("Couldn't get class C1");
//		}
//		JawinObject c1 = new JawinObject(c1Ptr);
//		DispatchPtr attrsPtr = null;
//		try {
//			attrsPtr = getAssociation(c1Ptr, "Attribute");
//		} catch (COMException e) {
//			theProject.invoke("PropertySet", "Transaction", 0, "Abort");
//			fail("Couldn't get C1's attributes");
//		}
//		JawinObject coll = new JawinObject(attrsPtr);
//		Collection<JawinObject> attrs = new JawinCollection(coll, c1, "Attribute");
//		assertFalse(attrs.isEmpty());
//		DispatchPtr a2Ptr = null;
//		try {
//			a2Ptr = (DispatchPtr) c1Ptr.invoke("ItemEx","Attribute","C1A2","Name");
//		} catch (COMException e) {
//			theProject.invoke("PropertySet", "Transaction", 0, "Abort");
//			fail("Couldn't get C1's attributes");
//		}
//		JawinObject a2 = new JawinObject(a2Ptr);
//		Collection<JawinObject> otherattrs = new ArrayList<JawinObject>();
//		otherattrs.add(a2);
//		attrs.retainAll(otherattrs);
//		int finalSize = attrs.size();
//		// TODO We can verify A1 no longer exists
//		theProject.invoke("PropertySet", "Transaction", 0, "Abort");
//		assertEquals(1, finalSize);
	}

	@Test
	public void testClear() throws Exception {
		theProject.invoke("PropertySet", "Transaction", 0, "Begin");
		DispatchPtr c1Ptr = null;
		try {
			c1Ptr = (DispatchPtr) model.getDelegate().invokeN("Item", new Object[] {"Class", "C1"});
		} catch (COMException e) {
			theProject.invoke("PropertySet", "Transaction", 0, "Abort");
			fail("Couldn't get class C1");
		}
		JawinObject c1 = new JawinObject(c1Ptr);
		DispatchPtr attrPtr = null;
		try {
			attrPtr = getAssociation(c1Ptr, "Attribute");
		} catch (COMException e) {
			theProject.invoke("PropertySet", "Transaction", 0, "Abort");
			fail("Couldn't get C1's attributes");
		}
		JawinObject coll = new JawinObject(attrPtr);
		Collection<JawinObject> attrs = new JawinCollection(coll, c1, "Attribute");
		assertFalse(attrs.isEmpty());
		attrs.clear();
		try {
			assertTrue(attrs.isEmpty());
		} catch(IllegalStateException ex) {
			theProject.invoke("PropertySet", "Transaction", 0, "Abort");
			throw ex;
		}
		theProject.invoke("PropertySet", "Transaction", 0, "Abort");
	}
	
	private static DispatchPtr load(DispatchPtr modelRef) throws COMException {
		DispatchPtr model = new DispatchPtr();
		Variant.ByrefHolder varIndex = new Variant.ByrefHolder("Dictionary");
		DispatchPtr dirDispPtr = (DispatchPtr) modelRef.invokeN("Item", new Object[] {
				"Dictionary", varIndex });
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
				Role, varIndex });
		DispatchPtr modelRef = new DispatchPtr();
		modelRef.stealUnknown(dispPtr);
		return modelRef;
	}
	
	/**
	 * Can be used for all of type, or any multivalue association
	 * @param object		Dict or Object
	 * @param associaiton	Name of the association, e.g. Type
	 * @return
	 * @throws COMException
	 */
	private static DispatchPtr getAssociation(DispatchPtr object, String associaiton) throws COMException {
		DispatchPtr allInstances = new DispatchPtr();
		Variant.ByrefHolder varArgument = new Variant.ByrefHolder("*");
		DispatchPtr classDispPtr = (DispatchPtr) object.invokeN("Items", new Object[] {associaiton, varArgument}, 2);
		allInstances.stealUnknown(classDispPtr);
		return allInstances;
	}

}
