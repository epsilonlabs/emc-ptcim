package org.eclipse.epsilon.emc.artisan.jawin;

import org.eclipse.epsilon.emc.COM.COMModel;
import org.jawin.DispatchPtr;

public class JawinModel extends JawinObject implements COMModel {

	public JawinModel(DispatchPtr delegate) {
		super(delegate);
	}
}
