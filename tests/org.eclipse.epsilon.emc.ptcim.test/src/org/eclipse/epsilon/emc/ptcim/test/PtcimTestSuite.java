
package org.eclipse.epsilon.emc.ptcim.test;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

@RunWith(Suite.class)
@SuiteClasses({
	AttributeRelatedTests.class,
	AssociationRelatedTests.class,
	OperationContributorTests.class,
	ElementRelatedTests.class})
public class PtcimTestSuite {

	public static Test suite() {
		return new JUnit4TestAdapter(PtcimTestSuite.class);
	}
	
	public static void main (String args[]) {
		Result result = JUnitCore.runClasses(AttributeRelatedTests.class, 
				AssociationRelatedTests.class,
				ElementRelatedTests.class);
		if (!result.wasSuccessful()) {
			System.err.println("Failed tests:");
			for (Failure aFailure : result.getFailures()) {
				System.err.println(aFailure.getDescription());
			}
		} else {
			System.out.println("All tests passed.");
		}
	}
}
