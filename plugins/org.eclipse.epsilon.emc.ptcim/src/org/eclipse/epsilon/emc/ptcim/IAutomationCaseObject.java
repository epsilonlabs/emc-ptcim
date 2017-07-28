package org.eclipse.epsilon.emc.ptcim;

import com4j.*;

/**
 * IAutomationCaseObject Interface
 */
@IID("{C9FF8402-BB2E-11D0-8475-0080C82BFA0C}")
public interface IAutomationCaseObject extends Com4jObject,Iterable<Com4jObject> {
  // Methods:
  /**
   * <p>
   * Accesses a string property of the requested type
   * </p>
   * <p>
   * Setter method for the COM property "Property"
   * </p>
   * @param type Mandatory java.lang.Object parameter.
   * @param index Optional parameter. Default value is com4j.Variant.getMissing()
   * @param retval Mandatory java.lang.Object parameter.
   */

  @DISPID(0) //= 0x0. The runtime will prefer the VTID if present
  @VTID(7)
  @DefaultMethod
  void property(
    @MarshalAs(NativeType.VARIANT) java.lang.Object type,
    @Optional @MarshalAs(NativeType.VARIANT) java.lang.Object index,
    @MarshalAs(NativeType.VARIANT) java.lang.Object retval);


  /**
   * <p>
   * Accesses a string property of the requested type
   * </p>
   * <p>
   * Getter method for the COM property "Property"
   * </p>
   * @param type Optional parameter. Default value is com4j.Variant.getMissing()
   * @param index Optional parameter. Default value is com4j.Variant.getMissing()
   * @return  Returns a value of type java.lang.Object
   */

  @DISPID(0) //= 0x0. The runtime will prefer the VTID if present
  @VTID(8)
  @DefaultMethod
  @ReturnValue(type=NativeType.VARIANT)
  java.lang.Object property(
    @Optional @MarshalAs(NativeType.VARIANT) java.lang.Object type,
    @Optional @MarshalAs(NativeType.VARIANT) java.lang.Object index);


  /**
   * <p>
   * Returns an object related in the requested role
   * </p>
   * @param role Mandatory java.lang.String parameter.
   * @param index Optional parameter. Default value is com4j.Variant.getMissing()
   * @return  Returns a value of type com4j.Com4jObject
   */

  @DISPID(1610743810) //= 0x60020002. The runtime will prefer the VTID if present
  @VTID(9)
  @ReturnValue(type=NativeType.Dispatch)
  com4j.Com4jObject item(
    java.lang.String role,
    @Optional @MarshalAs(NativeType.VARIANT) java.lang.Object index);


  /**
   * <p>
   * Returns a collection of objects related in the requested role
   * </p>
   * @param role Mandatory java.lang.String parameter.
   * @param index Optional parameter. Default value is com4j.Variant.getMissing()
   * @return  Returns a value of type com4j.Com4jObject
   */

  @DISPID(1610743811) //= 0x60020003. The runtime will prefer the VTID if present
  @VTID(10)
  @ReturnValue(type=NativeType.Dispatch)
  com4j.Com4jObject items(
    java.lang.String role,
    @Optional @MarshalAs(NativeType.VARIANT) java.lang.Object index);


  /**
   * <p>
   * Adds and returns an object related in the requested role
   * </p>
   * @param role Mandatory java.lang.String parameter.
   * @param value Optional parameter. Default value is com4j.Variant.getMissing()
   * @return  Returns a value of type com4j.Com4jObject
   */

  @DISPID(1610743812) //= 0x60020004. The runtime will prefer the VTID if present
  @VTID(11)
  @ReturnValue(type=NativeType.Dispatch)
  com4j.Com4jObject add(
    java.lang.String role,
    @Optional @MarshalAs(NativeType.VARIANT) java.lang.Object value);


  /**
   * <p>
   * Removes an object related in the requested role
   * </p>
   * @param role Mandatory java.lang.String parameter.
   * @param index Optional parameter. Default value is com4j.Variant.getMissing()
   */

  @DISPID(1610743813) //= 0x60020005. The runtime will prefer the VTID if present
  @VTID(12)
  void remove(
    java.lang.String role,
    @Optional @MarshalAs(NativeType.VARIANT) java.lang.Object index);


  /**
   * <p>
   * Returns an object that represents the link to an object in the requested role
   * </p>
   * @param role Mandatory java.lang.String parameter.
   * @param index Mandatory java.lang.Object parameter.
   * @return  Returns a value of type com4j.Com4jObject
   */

  @DISPID(1610743814) //= 0x60020006. The runtime will prefer the VTID if present
  @VTID(13)
  @ReturnValue(type=NativeType.Dispatch)
  com4j.Com4jObject link(
    java.lang.String role,
    @MarshalAs(NativeType.VARIANT) java.lang.Object index);


  /**
   * <p>
   * Resets the query position of the collection to the beginning
   * </p>
   */

  @DISPID(1610743815) //= 0x60020007. The runtime will prefer the VTID if present
  @VTID(14)
  void resetQueryItems();


  /**
   * <p>
   * Returns the next object in the collection
   * </p>
   * @return  Returns a value of type com4j.Com4jObject
   */

  @DISPID(1610743816) //= 0x60020008. The runtime will prefer the VTID if present
  @VTID(15)
  @ReturnValue(type=NativeType.Dispatch)
  com4j.Com4jObject nextItem();


  /**
   * <p>
   * Indicates if there are more objects in the collection
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(1610743817) //= 0x60020009. The runtime will prefer the VTID if present
  @VTID(16)
  int moreItems();


  /**
   * <p>
   * This method maps onto a property of the requested type
   * </p>
   * @param type Mandatory java.lang.Object parameter.
   * @param index Optional parameter. Default value is com4j.Variant.getMissing()
   * @param data Optional parameter. Default value is com4j.Variant.getMissing()
   */

  @DISPID(1610743819) //= 0x6002000b. The runtime will prefer the VTID if present
  @VTID(17)
  void propertySet(
    @MarshalAs(NativeType.VARIANT) java.lang.Object type,
    @Optional @MarshalAs(NativeType.VARIANT) java.lang.Object index,
    @Optional @MarshalAs(NativeType.VARIANT) java.lang.Object data);


  /**
   * <p>
   * This method maps onto a property of the requested type
   * </p>
   * @param type Optional parameter. Default value is com4j.Variant.getMissing()
   * @param index Optional parameter. Default value is com4j.Variant.getMissing()
   * @return  Returns a value of type java.lang.Object
   */

  @DISPID(1610743820) //= 0x6002000c. The runtime will prefer the VTID if present
  @VTID(18)
  @ReturnValue(type=NativeType.VARIANT)
  java.lang.Object propertyGet(
    @Optional @MarshalAs(NativeType.VARIANT) java.lang.Object type,
    @Optional @MarshalAs(NativeType.VARIANT) java.lang.Object index);


  /**
   * <p>
   * returns an enumerator for the collection.
   * </p>
   * <p>
   * Getter method for the COM property "_NewEnum"
   * </p>
   */

  @DISPID(-4) //= 0xfffffffc. The runtime will prefer the VTID if present
  @VTID(19)
  java.util.Iterator<Com4jObject> iterator();

  /**
   * <p>
   * Returns an object identified by its id
   * </p>
   * @param itemId Mandatory java.lang.String parameter.
   * @return  Returns a value of type com4j.Com4jObject
   */

  @DISPID(1610743821) //= 0x6002000d. The runtime will prefer the VTID if present
  @VTID(20)
  @ReturnValue(type=NativeType.Dispatch)
  com4j.Com4jObject findObject(
    java.lang.String itemId);


  /**
   * <p>
   * Returns property descriptors for a class of model object
   * </p>
   * @param className Mandatory java.lang.String parameter.
   * @param index Optional parameter. Default value is com4j.Variant.getMissing()
   * @return  Returns a value of type java.lang.Object
   */

  @DISPID(1610743822) //= 0x6002000e. The runtime will prefer the VTID if present
  @VTID(21)
  @ReturnValue(type=NativeType.VARIANT)
  java.lang.Object getClassProperties(
    java.lang.String className,
    @Optional @MarshalAs(NativeType.VARIANT) java.lang.Object index);


  /**
   * <p>
   * Indicates whether to do license checking
   * </p>
   * @param bCheck Mandatory int parameter.
   */

  @DISPID(1610743823) //= 0x6002000f. The runtime will prefer the VTID if present
  @VTID(22)
  void checkLicenses(
    int bCheck);


  /**
   * <p>
   * Returns the item uniquely identified by the given ID
   * </p>
   * @param id Mandatory java.lang.String parameter.
   * @return  Returns a value of type com4j.Com4jObject
   */

  @DISPID(1610743824) //= 0x60020010. The runtime will prefer the VTID if present
  @VTID(23)
  @ReturnValue(type=NativeType.Dispatch)
  com4j.Com4jObject itemByID(
    java.lang.String id);


  /**
   * <p>
   * Returns an object related in the requested role
   * </p>
   * @param role Mandatory java.lang.String parameter.
   * @param index Optional parameter. Default value is com4j.Variant.getMissing()
   * @param searchProperty Optional parameter. Default value is com4j.Variant.getMissing()
   * @return  Returns a value of type com4j.Com4jObject
   */

  @DISPID(1610743825) //= 0x60020011. The runtime will prefer the VTID if present
  @VTID(24)
  @ReturnValue(type=NativeType.Dispatch)
  com4j.Com4jObject itemEx(
    java.lang.String role,
    @Optional @MarshalAs(NativeType.VARIANT) java.lang.Object index,
    @Optional @MarshalAs(NativeType.VARIANT) java.lang.Object searchProperty);


  /**
   * <p>
   * Refreshes the cache
   * </p>
   */

  @DISPID(1610743826) //= 0x60020012. The runtime will prefer the VTID if present
  @VTID(25)
  void refresh();


  /**
   * <p>
   * Exports package contents to a file
   * </p>
   * @param directory Mandatory java.lang.String parameter.
   */

  @DISPID(1610743827) //= 0x60020013. The runtime will prefer the VTID if present
  @VTID(26)
  void export(
    java.lang.String directory);


  /**
   * <p>
   * Imports package contents from a file
   * </p>
   * @param directory Mandatory java.lang.String parameter.
   */

  @DISPID(1610743828) //= 0x60020014. The runtime will prefer the VTID if present
  @VTID(27)
  void _import(
    java.lang.String directory);


  /**
   * <p>
   * Compares package contents with a file
   * </p>
   * @param directory Mandatory java.lang.String parameter.
   */

  @DISPID(1610743829) //= 0x60020015. The runtime will prefer the VTID if present
  @VTID(28)
  void diff(
    java.lang.String directory);


  /**
   * <p>
   * Get the number of items down a role
   * </p>
   * @param role Mandatory java.lang.String parameter.
   * @return  Returns a value of type java.lang.Object
   */

  @DISPID(1610743830) //= 0x60020016. The runtime will prefer the VTID if present
  @VTID(29)
  @ReturnValue(type=NativeType.VARIANT)
  java.lang.Object itemCount(
    java.lang.String role);


  /**
   * <p>
   * Change the order of items down a role
   * </p>
   * @param role Mandatory java.lang.String parameter.
   * @param pObject Mandatory com4j.Com4jObject parameter.
   * @param pPredecessorObject Mandatory com4j.Com4jObject parameter.
   * @param pSuccessorObject Mandatory com4j.Com4jObject parameter.
   */

  @DISPID(1610743831) //= 0x60020017. The runtime will prefer the VTID if present
  @VTID(30)
  void reorderItem(
    java.lang.String role,
    @MarshalAs(NativeType.Dispatch) com4j.Com4jObject pObject,
    @MarshalAs(NativeType.Dispatch) com4j.Com4jObject pPredecessorObject,
    @MarshalAs(NativeType.Dispatch) com4j.Com4jObject pSuccessorObject);


  /**
   * <p>
   * Returns extended property descriptors for a class of model object
   * </p>
   * @param className Mandatory java.lang.String parameter.
   * @return  Returns a value of type java.lang.Object
   */

  @DISPID(1610743832) //= 0x60020018. The runtime will prefer the VTID if present
  @VTID(31)
  @ReturnValue(type=NativeType.VARIANT)
  java.lang.Object getExtendedClassProperties(
    java.lang.String className);


  /**
   * <p>
   * Adds and returns an object related in the requested role
   * </p>
   * @param role Mandatory java.lang.String parameter.
   * @param pHint Mandatory com4j.Com4jObject parameter.
   * @return  Returns a value of type com4j.Com4jObject
   */

  @DISPID(1610743833) //= 0x60020019. The runtime will prefer the VTID if present
  @VTID(32)
  @ReturnValue(type=NativeType.Dispatch)
  com4j.Com4jObject addDirected(
    java.lang.String role,
    @MarshalAs(NativeType.Dispatch) com4j.Com4jObject pHint);


  /**
   * <p>
   * Exports package contents to a file
   * </p>
   * @param directory Mandatory java.lang.String parameter.
   * @param bDoSubDirs Mandatory int parameter.
   */

  @DISPID(1610743840) //= 0x60020020. The runtime will prefer the VTID if present
  @VTID(33)
  void exportEx(
    java.lang.String directory,
    int bDoSubDirs);


  /**
   * <p>
   * Deletes a model object
   * </p>
   */

  @DISPID(1610743841) //= 0x60020021. The runtime will prefer the VTID if present
  @VTID(34)
  void delete();


  /**
   * <p>
   * Retrieves the display name for a tag
   * </p>
   * @param tagName Mandatory java.lang.String parameter.
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(1610743842) //= 0x60020022. The runtime will prefer the VTID if present
  @VTID(35)
  java.lang.String displayName(
    java.lang.String tagName);


  /**
   * <p>
   * Creates a clone of an item
   * </p>
   * @param pCloneThis Mandatory com4j.Com4jObject parameter.
   * @return  Returns a value of type com4j.Com4jObject
   */

  @DISPID(1610743843) //= 0x60020023. The runtime will prefer the VTID if present
  @VTID(36)
  @ReturnValue(type=NativeType.Dispatch)
  com4j.Com4jObject createClone(
    @MarshalAs(NativeType.Dispatch) com4j.Com4jObject pCloneThis);


  /**
   * <p>
   * Merges the source item into this item
   * </p>
   * @param pSrcItem Mandatory com4j.Com4jObject parameter.
   */

  @DISPID(1610743844) //= 0x60020024. The runtime will prefer the VTID if present
  @VTID(37)
  void merge(
    @MarshalAs(NativeType.Dispatch) com4j.Com4jObject pSrcItem);


  /**
   * <p>
   * Adds an object of the requested type
   * </p>
   * @param type Mandatory java.lang.String parameter.
   * @param role Mandatory java.lang.String parameter.
   * @return  Returns a value of type com4j.Com4jObject
   */

  @DISPID(1610743845) //= 0x60020025. The runtime will prefer the VTID if present
  @VTID(38)
  @ReturnValue(type=NativeType.Dispatch)
  com4j.Com4jObject addByType(
    java.lang.String type,
    java.lang.String role);


  /**
   * <p>
   * Returns whether the other object is related in the supplied role
   * </p>
   * @param role Mandatory java.lang.String parameter.
   * @param pOtherObject Mandatory com4j.Com4jObject parameter.
   * @return  Returns a value of type int
   */

  @DISPID(1610743846) //= 0x60020026. The runtime will prefer the VTID if present
  @VTID(39)
  int isConnectedTo(
    java.lang.String role,
    @MarshalAs(NativeType.Dispatch) com4j.Com4jObject pOtherObject);


  // Properties:
}
