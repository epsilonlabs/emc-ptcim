package org.eclipse.epsilon.emc.artisan.jawin;

import java.util.Collection;
import java.util.List;

import org.eclipse.epsilon.emc.COM.COMModel;
import org.eclipse.epsilon.emc.COM.COMObject;
import org.jawin.DispatchPtr;

public class JawinModel extends JawinObject implements COMModel {

	public JawinModel(DispatchPtr delegate) {
		super(delegate);
	}
}
