package org.eclipse.epsilon.emc.ptcim.jawin;

import java.util.Collection;
import java.util.List;

import org.eclipse.epsilon.emc.ptcim.ole.COMModel;
import org.eclipse.epsilon.emc.ptcim.ole.COMObject;
import org.jawin.DispatchPtr;

public class JawinModel extends JawinObject implements COMModel {

	public JawinModel(DispatchPtr delegate) {
		super(delegate);
	}
}
