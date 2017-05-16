 package org.eclipse.epsilon.emc.ptcim.com4j;		
 		
 import java.util.ArrayList;		
 import java.util.List;		
 		
 import org.eclipse.epsilon.emc.ptcim.com4j.Com4jPtcimProperty;	
 import org.eclipse.epsilon.eol.exceptions.EolInternalException;
 import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;		
 import org.eclipse.epsilon.eol.execute.introspection.AbstractPropertyGetter;		
 		
 public class Com4jPtcimPropertyGetter extends AbstractPropertyGetter {		
 		
 	/** The manager. */		
 	private Com4jPtcimPropertyManager manager;
 	
 	public Com4jPtcimPropertyManager getManager() {
		return manager;
	}

	public void setManager(Com4jPtcimPropertyManager manager) {
		this.manager = manager;
	}

	public Com4jPtcimPropertyGetter() {}
 	
 	public Com4jPtcimPropertyGetter(Com4jPtcimPropertyManager manager) {
 		//System.out.println("Just created a normal getter...");
		this.manager = manager;
	}
 			
 	/* (nonJavadoc)		
 	 * @see org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter#invoke(java.lang.Object, java.lang.String)		
 	 */		
 	@Override		
 	public Object invoke(Object object, String property) throws EolRuntimeException {		
 		Com4jPtcimObject jObject = (Com4jPtcimObject) object;		
 		Object o = null;		
 		try {		
 			Com4jPtcimProperty p = manager.getPtcProperty(jObject, property);		
 			if (p == null) {		
 				throw new EolRuntimeException("No such property");		
 			}		
 			if (p.isAssociation()) {		
 				List<Object> args = new ArrayList<Object>();		
 				args.add(property);		
 				if (p.isMultiple()) {		
 					Com4jPtcimCollection elements;		
 					Object res = new Com4jPtcimObject(jObject.items(property, null).queryInterface(IAutomationCaseObject.class));		
					assert res instanceof Com4jPtcimObject;		
					elements = new Com4jPtcimCollection((Com4jPtcimObject) res, jObject, property);		
 					o = elements;		
 				}		
 				else {		
 					o = new Com4jPtcimObject(jObject.item(property, null).queryInterface(IAutomationCaseObject.class));		
					if ( o instanceof Com4jPtcimObject) {		
						String strId = (String) ((Com4jPtcimObject) o).property("Id", null);		
						((Com4jPtcimObject) o).setId(strId);		
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
 		assert object instanceof Com4jPtcimObject;		
 		Com4jPtcimObject jObject = (Com4jPtcimObject) object;		
 		Com4jPtcimProperty p = manager.getPtcProperty(jObject, property);		
 		return p != null;		
 	}		
 }