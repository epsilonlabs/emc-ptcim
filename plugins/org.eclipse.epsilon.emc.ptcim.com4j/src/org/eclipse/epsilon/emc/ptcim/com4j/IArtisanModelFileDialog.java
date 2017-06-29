package org.eclipse.epsilon.emc.ptcim.com4j;

import com4j.*;

/**
 * IArtisanModelFileDialog Interface
 */
@IID("{CF042284-4EB6-40BE-A670-4603C616AB42}")
public interface IArtisanModelFileDialog extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Create or Open Model
   * </p>
   * @param bOpenFileDialog Mandatory boolean parameter.
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(1) //= 0x1. The runtime will prefer the VTID if present
  @VTID(7)
  java.lang.String create(
    boolean bOpenFileDialog);


  /**
   * <p>
   * Set dialog title
   * </p>
   * @param bstrTitle Mandatory java.lang.String parameter.
   */

  @DISPID(2) //= 0x2. The runtime will prefer the VTID if present
  @VTID(8)
  void setTitle(
    java.lang.String bstrTitle);


  /**
   * <p>
   * Create or Open Model, returning extended informations
   * </p>
   * @param bOpenFileDialog Mandatory boolean parameter.
   * @param pRef Mandatory Holder<java.lang.String> parameter.
   * @param pID Mandatory Holder<java.lang.String> parameter.
   * @param pModelName Mandatory Holder<java.lang.String> parameter.
   */

  @DISPID(3) //= 0x3. The runtime will prefer the VTID if present
  @VTID(9)
  void createEx(
    boolean bOpenFileDialog,
    Holder<java.lang.String> pRef,
    Holder<java.lang.String> pID,
    Holder<java.lang.String> pModelName);


  // Properties:
}
