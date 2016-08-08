/**
 * 
 */
package org.eclipse.epsilon.emc.artisan;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.epsilon.emc.COM.COMBridge;
import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.artisan.jawin.JawinComBridge;
import org.eclipse.epsilon.emc.artisan.jawin.JawinPropertyGetter;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Horacio Hoyos
 *
 */
public class ArtisanJawinTests {

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
		ArtisanModel model = new ArtisanModel(bridge);
		model.setName("HSUV");
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
		ArtisanModel model = new ArtisanModel(bridge);
		model.setName("HSUV");
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
		ArtisanModel model = new ArtisanModel(bridge);
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
			Assert.assertNotNull(next);
		}
		model.disposeModel();
	}
	
	@Test
	public void testGetProperty() throws Exception {
		COMBridge<COMObject, COMObject> bridge = new JawinComBridge();
		ArtisanModel model = new ArtisanModel(bridge);
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
			Object id = pg.invoke(next, "Name");
			Assert.assertTrue(id instanceof String);
			//System.out.println(id);
		}
		model.disposeModel();
	}
	
	@Test
	public void testHasProperty() throws Exception {
		COMBridge<COMObject, COMObject> bridge = new JawinComBridge();
		ArtisanModel model = new ArtisanModel(bridge);
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
			boolean hasName = pg.hasProperty(next, "Name");
			Assert.assertTrue(hasName);
			System.out.println(hasName);
		}
		model.disposeModel();
	}
	

}
