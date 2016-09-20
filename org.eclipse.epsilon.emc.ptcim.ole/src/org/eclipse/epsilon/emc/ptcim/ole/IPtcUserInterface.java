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
 * The Interface IPtcUserInterface. Provides access to the methods to manipulate
 * the Modeler user interface. 
 * 
 * Using these functions you can control the Modeler user interface as follows:
 * <p><ul>
 * <li>Control the Modeler window - show, hide or move it, or make it active.
 * <li>Open models or get the model reference of the active model.
 * <li>Open diagrams, select symbols, scroll options and the zoom setting.
 * <li>Select items in panes.
 * <li>Display messages in the Output pane and clear its content.
 * <li>Get item ids from the Results panes, add items to the Results panes and
 * 		clear the content of the Results pane.
 *  <li>Refresh the Modeler views.
 * </ul><p>
 *
 * The interface also provides the methods for working with models, such as:
 * creation, versioning, deletion, etc...
 * 
 */
public interface IPtcUserInterface<T extends IPtcObject> extends IPtcApp<T> {
	
	/**
	 * Creates a model in a repository.
	 *  
	 * @param server is a string that specifies the name of the server on which
	 * 	the Repository resides. The case of the name must be correct. 
	 * @param repository is a string that specifies the name of the Repository
	 * 	in which you want to create the model. The case of the name must be correct. 
	 * @param name is a string that specifies the name of the model you want to create.
	 * @return the the reference of the model that is created.
	 * @throws EpsilonCOMException the epsilon COM exception
	 */
	String createModel(String server, String repository, String name) throws EpsilonCOMException;

	
	/**
	 * Opens a diagram in your model using the Diagram Object's Id property.
	 * This function returns a Boolean value to indicate success or failure. 
	 * 
	 *
	 * @param id the id of the diagram to open
	 * @return true, if successful
	 */
	boolean openDiagram(String id);
	
	/**
	 * Opens a model using its Name property. The function returns a Boolean 
	 * value to indicate success or failure. If you are mapped to Models that
	 * have the same names, you may want to use the {@link #openModelLocation()}
	 * method that allows you to specify the Model name and directory. 
	 *
	 * @param name the name
	 * @return true, if successful
	 * @see #openModelLocation()
	 */
	boolean openModel(String name);
	
	/**
	 * Opens a model using its Name and Directory attributes. The function
	 * returns a Boolean value to indicate success or failure. 
	 * 
	 * When you specify the directory you must use the value of the Model's
	 * Directory attribute as returned through the automation interface,
	 * because it is returned as a short path. 
	 *
	 * @param name the name
	 * @param directory is a string that specifies the path of the model to
	 * 	open, as returned through the Model's Directory attribute. 
	 * @return true, if successful
	 * @see #openModel(String)
	 */
	boolean openModelLocation(String name, String directory);
	
	/**
	 * Uses the Dictionary Item's Id property to select an item in a specified
	 * pane.
	 * 
	 * Note that if the specified pane is closed, the function shows the pane
	 * and finds the item.
	 *
	 * @param itemId a string that specifies the id of the item to select.
	 * @param pane a string that specifies the name of the explorer type pane,
	 * 	'Active' for the active pane, or the id of a profile package.
	 * @return true, if successful
	 */
	boolean selectBrowserItem(String itemId, String pane);
	
	/**
	 * Select symbol selects a symbol on a diagram using the Id properties of
	 * the diagram and the symbol. The function searches for the requested
	 * symbol on the diagram and returns a Boolean value to indicate success
	 * or failure. 
	 * 
	 * SelectSymbol allows for the selection of symbols not represented in the
	 * dictionary, such as class instances on a Sequence Diagram.
	 *
	 * @param diagramId a string that specifies the id of the diagram.
	 * @param itemId a string that specifies the id of the item to find
	 * @return true, if successful
	 */
	boolean selectSymbol(String diagramId, String itemId);
	
	
	/**
	 * Activates the Modeler window and brings it to the front of your screen.
	 */
	void setForegroundWindow();
	
	/**
	 * ShowMainWindow allows you to view the main Modeler window. By default
	 * the main window is hidden when you open Modeler using the Visual Basic
	 * Interface. 
	 */
	void showMainWindow();

}
