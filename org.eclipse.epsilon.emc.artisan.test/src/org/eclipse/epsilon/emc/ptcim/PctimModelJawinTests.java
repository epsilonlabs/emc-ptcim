/**
 * 
 */
package org.eclipse.epsilon.emc.ptcim;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.epsilon.emc.ptcim.PtcimModel;
import org.eclipse.epsilon.emc.ptcim.jawin.JawinComBridge;
import org.eclipse.epsilon.emc.ptcim.jawin.JawinPropertyGetter;
import org.eclipse.epsilon.emc.ptcim.ole.COMBridge;
import org.eclipse.epsilon.emc.ptcim.ole.COMObject;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Horacio Hoyos
 *
 */
public class PctimModelJawinTests {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoad() {
		COMBridge<COMObject, COMObject> bridge = new JawinComBridge();
		PtcimModel model = new PtcimModel();
		model.setName("EmcTest");
		try {
			model.load();
		} catch (EolModelLoadingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.disposeModel();
	}
	
	// FIXME the model load should be done elsewhere so it does not affect the test
	@Test
	public void testAllOfType() throws Exception {
		COMBridge<COMObject, COMObject> bridge = new JawinComBridge();
		PtcimModel model = new PtcimModel();
		model.setName("EmcTest");
		try {
			model.load();
		} catch (EolModelLoadingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collection<COMObject> clss = model.getAllOfType("Class");
		Iterator<COMObject> it = clss.iterator();
		Assert.assertTrue(it.hasNext()); 
		model.disposeModel();
		
	}
	
	@Test
	public void testIterateAllOfType() throws Exception {
		COMBridge<COMObject, COMObject> bridge = new JawinComBridge();
		PtcimModel model = new PtcimModel();
		model.setName("EmcTest");
		try {
			model.load();
		} catch (EolModelLoadingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collection<COMObject> clss = model.getAllOfType("Class");
		Iterator<COMObject> it = clss.iterator();
		int i = 0;
		while(it.hasNext()) {
			i++;
			COMObject next = it.next();
			Assert.assertNotNull(next);
		}
		assert i == 2;
		model.disposeModel();
	}
	
	@Test
	public void testGetProperty_Attribute() throws Exception {
		COMBridge<COMObject, COMObject> bridge = new JawinComBridge();
		PtcimModel model = new PtcimModel();
		JawinPropertyGetter pg = new JawinPropertyGetter();
		model.setName("EmcTest");
		try {
			model.load();
		} catch (EolModelLoadingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collection<COMObject> clss = model.getAllOfType("Class");
		Iterator<COMObject> it = clss.iterator();
		while(it.hasNext()) {
			COMObject next = it.next();
			Object id = pg.invoke(next, "Name");
			Assert.assertTrue(id instanceof String);
			break;
		}
		model.disposeModel();
	}
	
	
	@Test
	public void testHasProperty() throws Exception {
		COMBridge<COMObject, COMObject> bridge = new JawinComBridge();
		PtcimModel model = new PtcimModel();
		JawinPropertyGetter pg = new JawinPropertyGetter();
		model.setName("HSUV");
		try {
			model.load();
		} catch (EolModelLoadingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collection<COMObject> clss = model.getAllOfType("Class");
		Iterator<COMObject> it = clss.iterator();
		while(it.hasNext()) {
			COMObject next = it.next();
			// FIXME Either filter by class, or get the attributes by type, not by element...
			// because it is possible that not always C1 is returned first?
			boolean hasName = pg.hasProperty(next, "Name");
			Assert.assertTrue(hasName);
			break;
		}
		model.disposeModel();
	}
	

}
