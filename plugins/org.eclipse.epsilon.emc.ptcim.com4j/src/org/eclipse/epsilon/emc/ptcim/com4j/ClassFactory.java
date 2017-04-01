package org.eclipse.epsilon.emc.ptcim.com4j;

import com4j.*;

/**
 * Defines methods to create COM objects
 */
public abstract class ClassFactory {
  private ClassFactory() {} // instanciation is not allowed


  /**
   * Enterprise CaseProjects Class
   */
  public static IAutomationCaseObject createCCaseProjects() {
	  //return COM4J.createInstance(IAutomationCaseObject.class, "{c9ff8402-bb2e-11d0-8475-0080C82BFA0C}");
	  return COM4J.createInstance(IAutomationCaseObject.class, "{594B0CA2-7610-11D1-BA96-444553540000}" );
  }
}
