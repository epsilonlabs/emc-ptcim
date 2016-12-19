 package org.eclipse.epsilon.emc.ptcim.jawin;		
 		
 import java.util.ArrayList;		
 import java.util.List;		
 		
 import org.eclipse.epsilon.emc.ptcim.jawin.PtcProperty;	
 import org.eclipse.epsilon.eol.exceptions.EolInternalException;
 import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;		
 import org.eclipse.epsilon.eol.execute.introspection.AbstractPropertyGetter;		
 		
 /**		
  * The Class JawinPropertyGetter.		
  */		
 public class JawinPropertyGetter extends AbstractPropertyGetter {		
 		
 	/** The manager. */		
 	private final JawinPropertyManager manager = JawinPropertyManager.INSTANCE;		
 			
 	/* (nonJavadoc)		
 	 * @see org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter#invoke(java.lang.Object, java.lang.String)		
 	 */		
 	@Override		
 	public Object invoke(Object object, String property) throws EolRuntimeException {		
 		JawinObject jObject = (JawinObject) object;		
 		Object o = null;		
 		try {		
 			PtcProperty p = manager.getPtcProperty(jObject, property);		
 			if (p == null) {		
 				throw new EolRuntimeException("No such property");		
 			}		
 			if (p.isAssociation()) {		
 				List<Object> args = new ArrayList<Object>();		
 				args.add(property);		
 				if (p.isMultiple()) {		
 					JawinCollection elements;		
 					List<Object> byRefArgs = new ArrayList<Object>();		
 					byRefArgs.add("*");		
 					try {		
 						Object res = jObject.invoke("Items", args);		
 						assert res instanceof JawinObject;		
 						elements = new JawinCollection((JawinObject) res, jObject, property);		
 					} catch (EolInternalException e) {		
 						throw new EolRuntimeException(e.getMessage());		
 					}		
 					o = elements;		
 				}		
 				else {		
 					try {		
 						o = jObject.invoke("Item", args);		
 						if ( o instanceof JawinObject) {		
 							String strId = (String) ((JawinObject) o).getAttribute("Property", "Id");		
 							((JawinObject) o).setId(strId);		
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
 		assert object instanceof JawinObject;		
 		JawinObject jObject = (JawinObject) object;		
 		PtcProperty p = manager.getPtcProperty(jObject, property);		
 		return p != null;		
 	}		
 }