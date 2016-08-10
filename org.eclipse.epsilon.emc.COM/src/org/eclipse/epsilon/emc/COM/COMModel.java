package org.eclipse.epsilon.emc.COM;

import java.util.Collection;

public interface COMModel extends COMObject {
	
	
	Collection<? extends COMObject> wrapInColleciton(COMObject res, COMModel model, String type);
	
	// WE might need to persist this, reason is unclear
//	COMObject getProjectRef();
//	COMObject getCOMApp();

}
