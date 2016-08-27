package org.eclipse.epsilon.emc.COM;

public interface COMPropertyManager {

	COMPropertyManager getInstance();

	COMProperty getProperty(COMObject object, String property);
	
	/** Clear the cache */
	void dispose();

}