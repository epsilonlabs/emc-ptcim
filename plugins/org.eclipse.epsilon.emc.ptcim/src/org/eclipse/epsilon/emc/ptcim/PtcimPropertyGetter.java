 package org.eclipse.epsilon.emc.ptcim;		
 		
 import java.util.ArrayList;		
 import java.util.List;		
 		
 import org.eclipse.epsilon.emc.ptcim.PtcimProperty;	
 import org.eclipse.epsilon.eol.exceptions.EolInternalException;
 import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;		
 import org.eclipse.epsilon.eol.execute.introspection.AbstractPropertyGetter;		
 		
 /**		
  * The Class JawinPropertyGetter.		
  */		
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
 					List<Object> byRefArgs = new ArrayList<Object>();		
 					byRefArgs.add("*");		
 					try {		
 						Object res = jObject.invoke("Items", args);		
 						assert res instanceof PtcimObject;		
 						elements = new PtcimCollection((PtcimObject) res, jObject, property);		
 					} catch (EolInternalException e) {		
 						throw new EolRuntimeException(e.getMessage());		
 					}		
 					o = elements;		
 				}		
 				else {		
 					try {		
 						o = jObject.invoke("Item", args);		
 						if ( o instanceof PtcimObject) {		
 							String strId = (String) ((PtcimObject) o).getAttribute("Property", "Id");		
 							((PtcimObject) o).setId(strId);		
 						}		
 					} catch (EolInternalException e) {		
 						throw new EolRuntimeException(e.getMessage());		
 					}		
 				}		
 			}		
 			else {		
 				o = jObject.getAttribute("Property", property);		
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