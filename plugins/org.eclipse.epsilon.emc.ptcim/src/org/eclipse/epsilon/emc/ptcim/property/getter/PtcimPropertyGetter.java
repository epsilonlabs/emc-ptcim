 package org.eclipse.epsilon.emc.ptcim.property.getter;		
 		
 import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.emc.ptcim.PtcimObject;
import org.eclipse.epsilon.emc.ptcim.operations.contributors.PtcimCollectionOperationContributor;
import org.eclipse.epsilon.emc.ptcim.property.PtcimProperty;
import org.eclipse.epsilon.emc.ptcim.property.manager.PtcimPropertyManager;
import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.introspection.AbstractPropertyGetter;
 		
 public class PtcimPropertyGetter extends AbstractPropertyGetter {		
 		
 	/** The manager. */		
 	private PtcimPropertyManager manager;
 	
 	public PtcimPropertyManager getManager() {
		return manager;
	}

	public void setManager(PtcimPropertyManager manager) {
		this.manager = manager;
	}

	public PtcimPropertyGetter() {}
 	
 	public PtcimPropertyGetter(PtcimPropertyManager manager) {
 		//System.out.println("Just created a normal getter...");
		this.manager = manager;
	}
 			
 	/* (nonJavadoc)		
 	 * @see org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter#invoke(java.lang.Object, java.lang.String)		
 	 */		
 	@Override		
 	public Object invoke(Object object, String property) throws EolRuntimeException {		
 		PtcimObject jObject = (PtcimObject) object;		
 		Object invokedObject = null;		
 		try {		
 			PtcimProperty ptcimPorperty = manager.getPtcProperty(jObject, property);		
 			if (ptcimPorperty == null) {		
 				throw new EolRuntimeException("No such property");		
 			}		
 			if (ptcimPorperty.isAssociation()) {		
 				List<Object> args = new ArrayList<Object>();		
 				args.add(property);		
 				if (ptcimPorperty.isMultiple()) {		
 					PtcimCollectionOperationContributor elements;		
 					Object res = jObject.items(property, null);		
					assert res instanceof PtcimObject;		
					elements = new PtcimCollectionOperationContributor((PtcimObject) res, jObject, property);		
 					invokedObject = elements;		
 				}		
 				else {		
 					invokedObject = jObject.item(property, null);		
					if ( invokedObject instanceof PtcimObject) {		
						String strId = (String) ((PtcimObject) invokedObject).property("Id", null);		
						((PtcimObject) invokedObject).setId(strId);		
					}		
 				}		
 			}		
 			else {		
 				invokedObject = jObject.property(property, null);		
 			}		
 		} catch (EolInternalException e) {		
 			throw new EolRuntimeException(e.getMessage());		
 		} 
 		return invokedObject;		
 	}		
 			
 	/* (nonJavadoc)		
 	 * @see org.eclipse.epsilon.eol.execute.introspection.AbstractPropertyGetter#hasProperty(java.lang.Object, java.lang.String)		
 	 */		
 	@Override		
 	public boolean hasProperty(Object object, String property) {		
 		assert object instanceof PtcimObject;		
 		PtcimObject jObject = (PtcimObject) object;		
 		PtcimProperty p = manager.getPtcProperty(jObject, property);		
 		return p != null;		
 	}		
 }