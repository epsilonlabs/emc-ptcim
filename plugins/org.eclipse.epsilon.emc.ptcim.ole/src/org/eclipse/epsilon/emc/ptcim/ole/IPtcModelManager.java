/*******************************************************************************
 * Copyright (c) 2016 University of York
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Horacio Hoyos - Initial API and implementation
 *******************************************************************************/
package org.eclipse.epsilon.emc.ptcim.ole;

import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;

/**
 * The Interface IPtcModelManager provides the methods to work with the 
 * PTC IM OMTE.Projects interface.
 * 
 * The OMTE.Projects is the root object for Modeler. It is a collection object
 * for all the Project objects you can read, that is, the Models available in
 * the repositories that are bookmarked in your Model Explorer. 
 * 
 */
public interface IPtcModelManager<T extends IPtcObject, S extends IPtcCollection> extends IPtcApp<T> {
	
	/**
	 * Open a model in the Artisan Repository using the Reference attribute of a Model. 
	 * When you use Reference, you can specify the model version or not, in which case
	 * the latest version of the model is used.
	 * Note that the reference string in the format "\\Enabler\&lt;server&gt\&lt;repository&gt\&lt;model&gt\&lt;version&gt"
	 * is passed as individual parameters. 
	 *
	 * @param id the id of the model
	 * @param server the server
	 * @param repository the repository
	 * @param version the version number
	 * @return the Model reference
	 * @throws EpsilonCOMException If an error using the COM connection
	 */
	T getProjectByReference(String id, String server, String repository, String version)
			throws EpsilonCOMException;
	
	/**
	 * Open a model in the Artisan repository by using the Title of the model.
	 * If the Model Explorer has another Model of the same title, the first Model with
	 * a matching title is obtained. 
	 *
	 * @param app the app
	 * @param title the title
	 * @return the Model reference
	 * @throws EpsilonCOMException If an error using the COM connection
	 */
	T getProjectByTitle(String title) throws EpsilonCOMException;
	
	/**
	 * Get the Models in the Projects collection.
	 * @return
	 * @throws EpsilonCOMException
	 */
	S getProjects() throws EpsilonCOMException;
	
	/**
	 * Return the the Model (Project) that is currently open in Modeler. 
	 * @return
	 * @throws EpsilonCOMException
	 */
	T getActiveProjet() throws EpsilonCOMException;
	
	/**
	 * Return a reference to the Diagram that currently has the focus in Modeler, or
	 * if no diagrams are open in Modeler, a relationship to the diagram
	 * selected in the active pane. If no diagrams are open and you select
	 * multiple diagrams in the active pane, Active Diagram is a relationship
	 * with all the selected diagrams.

	 * @return
	 * @throws EpsilonCOMException
	 */
	S getActiveDagrams() throws EpsilonCOMException;
	
	/**
	 * Return a reference to the Dictionary Item that currently has the focus
	 * in the Modeler. If a symbol on a diagram has the focus, a relationship
	 * with the dictionary item that is the underlying dictionary item for
	 * that symbol. If you select multiple items in the active pane, Active
	 * Dictionary Item will return a collection with all the selected items.

	 * @return
	 * @throws EpsilonCOMException
	 */
	S getActiveItems() throws EpsilonCOMException;
	
	/**
	 * A reference to the items that make up the path to a Part or Port in
	 * the model. 
	 * 
	 * Active Selection Context is useful when working with virtual
	 * redefinitions, as it returns the path of items as seen in the Parts
	 * browser.
	 * 
	 * @return
	 * @throws EpsilonCOMException
	 */
	S getActiveSelectinContext() throws EpsilonCOMException;
	
	/**
	 * Returns a reference to the symbol that is currently selected on the
	 * diagram that has the focus in Modeler. If you select multiple symbols
	 * on a diagram, return a collection of all the selected symbols.
	 * 
	 * @return
	 * @throws EpsilonCOMException
	 */
	S getActiveSymbols() throws EpsilonCOMException;
	
}
