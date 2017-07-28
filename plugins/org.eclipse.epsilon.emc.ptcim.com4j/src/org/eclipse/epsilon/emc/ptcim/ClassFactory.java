package org.eclipse.epsilon.emc.ptcim;

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
  
  /**
   * ArtisanModelFileDialog Class
   */
  public static IArtisanModelFileDialog createArtisanModelFileDialog() {
    return COM4J.createInstance(IArtisanModelFileDialog.class, "{B54D1192-94AB-482D-AAFF-41A150D26B3D}" );
  }
}
