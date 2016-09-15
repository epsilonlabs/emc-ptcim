package org.eclipse.epsilon.emc.ptcim.ole;

public interface COMPropertyManager {

	COMPropertyManager getInstance();

	COMProperty getProperty(COMObject object, String property);
	
	/** Clear the cache */
	void dispose();

}