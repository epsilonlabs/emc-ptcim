 package org.eclipse.epsilon.emc.ptcim.com4j;		
 		
 import java.util.ArrayList;		
 import java.util.List;		
 		
 import org.eclipse.epsilon.emc.ptcim.com4j.PtcimProperty;	
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
 		Object o = null;		
 		try {		
 			PtcimProperty p = manager.getPtcProperty(jObject, property);		
 			if (p == null) {		
 				throw new EolRuntimeException("No such property");		
 			}		
 			if (p.isAssociation()) {		
 				List<Object> args = new ArrayList<Object>();		
 				args.add(property);		
 				if (p.isMultiple()) {		
 					PtcimCollection elements;		
 					Object res = new PtcimObject(jObject.items(property, null).queryInterface(IAutomationCaseObject.class));		
					assert res instanceof PtcimObject;		
					elements = new PtcimCollection((PtcimObject) res, jObject, property);		
 					o = elements;		
 				}		
 				else {		
 					o = new PtcimObject(jObject.item(property, null).queryInterface(IAutomationCaseObject.class));		
					if ( o instanceof PtcimObject) {		
						String strId = (String) ((PtcimObject) o).property("Id", null);		
						((PtcimObject) o).setId(strId);		
					}		
 				}		
 			}		
 			else {		
 				o = jObject.property(property, null);		
 			}		
 		} catch (EolInternalException e) {		
 			throw new EolRuntimeException(e.getMessage());		
 		}		
 		return o;		
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