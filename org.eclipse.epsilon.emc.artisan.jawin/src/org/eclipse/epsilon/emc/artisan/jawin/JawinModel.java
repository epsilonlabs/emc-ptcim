package org.eclipse.epsilon.emc.artisan.jawin;

import java.util.Collection;

import org.eclipse.epsilon.emc.COM.COMModel;
import org.eclipse.epsilon.emc.COM.COMObject;
import org.jawin.DispatchPtr;

public class JawinModel extends JawinObject implements COMModel {

	public JawinModel(DispatchPtr delegate) {
		super(delegate);
	}

	@Override
	public Collection<? extends COMObject> wrapInColleciton(COMObject comCollection, COMModel owner, String association) {
		
		return new JawinCollection(comCollection, owner, association);
	}
}
