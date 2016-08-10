package org.eclipse.epsilon.emc.artisan.jawin.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.epsilon.emc.COM.COMBridge;
import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
import org.eclipse.epsilon.emc.artisan.jawin.JawinCollection;
import org.eclipse.epsilon.emc.artisan.jawin.JawinObject;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.jawin.COMException;
import org.jawin.DispatchPtr;
import org.jawin.Variant;
import org.jawin.win32.Ole32;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JawinCollectionTests {
	
	private JawinObject model;
	
	@Before
	public void setUp() throws Exception {
		DispatchPtr modelRef = getActiveProject("EmcTest");
		DispatchPtr modelptr = load(modelRef);
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
	public void testAddJawinObject() {
		fail("Not yet implemented");
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
		DispatchPtr ptr = getAssociation(model.getDelegate(), "Class");
		JawinObject coll = new JawinObject(ptr);
		Collection<JawinObject> clss = new JawinCollection(coll, model, "Class");
		// Get the first class
		List<Object> args = new ArrayList<Object>();
		args.add("Class");
		JawinObject objPtr = (JawinObject) model.invoke("Item", args);
		String strId = (String) objPtr.get("Property", "Id");
		objPtr.setId(strId);
		assertTrue(clss.contains(objPtr));
		// Get the first Attribute
		args = new ArrayList<Object>();
		args.add("Attribute");
		JawinObject actPtr = (JawinObject) model.invoke("Item", args);
		String actId = (String) actPtr.get("Property", "Id");
		actPtr.setId(actId);
		assertFalse(clss.contains(actPtr));
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
	public void testRetainAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testClear() {
		fail("Not yet implemented");
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
