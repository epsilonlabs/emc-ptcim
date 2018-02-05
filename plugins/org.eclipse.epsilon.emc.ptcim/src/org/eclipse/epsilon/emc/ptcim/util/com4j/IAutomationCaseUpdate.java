package org.eclipse.epsilon.emc.ptcim.util.com4j;

import com4j.*;

/**
 * IAutomationCaseUpdate Interface
 */
@IID("{CE5B2ED2-0D3C-4C22-B43B-518D779EC423}")
public interface IAutomationCaseUpdate extends Com4jObject {
	// Methods:
	/**
	 * <p>
	 * method OnUpdate
	 * </p>
	 * 
	 * @param modification
	 *            Mandatory java.lang.String parameter.
	 * @param objectId
	 *            Mandatory java.lang.String parameter.
	 * @param relatedObjectId
	 *            Mandatory java.lang.String parameter.
	 * @param otherObjectId
	 *            Mandatory java.lang.String parameter.
	 * @param roleName
	 *            Mandatory java.lang.String parameter.
	 * @param propertyName
	 *            Mandatory java.lang.String parameter.
	 */

	@VTID(3)
	void onUpdate(java.lang.String modification, java.lang.String objectId, java.lang.String relatedObjectId,
			java.lang.String otherObjectId, java.lang.String roleName, java.lang.String propertyName);

	// Properties:
}
