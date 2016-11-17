package org.eclipse.epsilon.emc.ptcim.test;

import java.lang.reflect.Method;

public class PUnit {
	
	public void run(Object... testSuites) throws Exception {
		
		for (Object testSuite : testSuites) {
			run(testSuite);
		}
	}
	
	public void run(Object testSuite) throws Exception {
		Method setUp = getMethod(testSuite, "setUp");
		Method tearDown = getMethod(testSuite, "tearDown");
		
		for (Method method : testSuite.getClass().getMethods()) {
			if (method.getName().startsWith("test") && method.getParameterTypes().length == 0) {
				if (setUp != null) setUp.invoke(testSuite);
				method.invoke(testSuite);
				if (tearDown != null) tearDown.invoke(testSuite);
			}
		}
	}
	
	protected Method getMethod(Object object, String name) {
		try {
			return object.getClass().getMethod(name);
		}
		catch (NoSuchMethodException ex) {
			return null;
		}
	}
}