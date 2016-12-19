package org.eclipse.epsilon.emc.ptcim.jawin;

public class JawinPropertyManager {
	
	public static final JawinPropertyManager INSTANCE = new JawinPropertyManager();
	
	public JawinPropertyManager getInstance() {
		return INSTANCE;
	}
	
	public boolean knowsProperty(Object object, String property) {
		return false;
	}
}
