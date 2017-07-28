package org.eclipse.epsilon.emc.ptcim;

import com4j.*;

/**
 * IFileServer Interface
 */
@IID("{298F0570-45D6-11D4-939D-0050042FA94C}")
public interface IFileServer extends Com4jObject {
  // Methods:
  /**
   * <p>
   * method OnGetFile
   * </p>
   * @param bstrObjectId Mandatory java.lang.String parameter.
   * @param bstrDIrectory Mandatory java.lang.String parameter.
   * @param bstrFileName Mandatory java.lang.String parameter.
   * @param bstrProjPath Mandatory java.lang.String parameter.
   * @param bstrAuxPath Mandatory java.lang.String parameter.
   */

  @VTID(3)
  void onGetFile(
    java.lang.String bstrObjectId,
    java.lang.String bstrDIrectory,
    java.lang.String bstrFileName,
    java.lang.String bstrProjPath,
    java.lang.String bstrAuxPath);


  /**
   * <p>
   * method OnUpdateProgress
   * </p>
   * @param majorActivity Mandatory java.lang.String parameter.
   * @param info Mandatory java.lang.String parameter.
   * @param progress Mandatory int parameter.
   */

  @VTID(4)
  void updateProgress(
    java.lang.String majorActivity,
    java.lang.String info,
    int progress);


  // Properties:
}
