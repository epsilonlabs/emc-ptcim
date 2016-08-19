package org.eclipse.epsilon.emc.COM;


public interface COMCollection {

	/**
	 * Gets the association name used to generate the collection
	 *
	 * @return the association
	 */
	String getAssociation();

	/**
	 * Gets the owner of the association.
	 *
	 * @return the owner
	 */
	COMObject getOwner();

	/**
	 * Gets the object that points to the collection.
	 *
	 * @return the source
	 */
	COMObject getCOMObject();
	
	/**
	 * Returns true if the Collection has been filtered. Optimisations can't be done on filtered
	 * collections. 
	 * @return
	 */
	boolean isFiltered();
		

}