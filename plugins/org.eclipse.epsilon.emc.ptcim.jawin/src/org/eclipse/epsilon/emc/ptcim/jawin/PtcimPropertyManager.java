package org.eclipse.epsilon.emc.ptcim.jawin;

public class PtcimPropertyManager {
	
	public static final PtcimPropertyManager INSTANCE = new PtcimPropertyManager();
	
	public PtcimPropertyManager getInstance() {
		return INSTANCE;
	}
	
	public boolean knowsProperty(Object object, String property) {
		return false;
	}
}
