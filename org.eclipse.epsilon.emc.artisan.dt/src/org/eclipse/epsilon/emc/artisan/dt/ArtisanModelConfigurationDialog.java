/*******************************************************************************
 * Copyright (c) 2012 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.emc.artisan.dt;

import org.eclipse.epsilon.common.dt.launching.dialogs.AbstractCachedModelConfigurationDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ArtisanModelConfigurationDialog extends AbstractCachedModelConfigurationDialog {
	
	protected String getModelName() {
		return "Artisan Studio Model";
	}

	
	protected String getModelType() {
		return "Artisan";
	}
	
	protected Label fileTextLabel;
	protected Text fileText;
	protected Label profileDirectoriesLabel;
	protected Text profileDirectoriesText;
	protected Label profileWorkspaceDirectoriesLabel;
	protected Text profileWorkspaceDirectoriesText;
	protected Button browseModelFile;
	protected Button ignoreArgoUmlProfiles;
	
	protected void createGroups(Composite control) {
		super.createGroups(control);
		//createFilesGroup(control);
		//createProfilesGroup(control);
		createLoadStoreOptionsGroup(control);
	}
	

	/*
	protected void loadProperties(){
		super.loadProperties();
		if (properties == null) return;
		fileText.setText(properties.getProperty(ArgoUMLModel.PROPERTY_MODEL_FILE));
		profileDirectoriesText.setText(properties.getProperty(ArgoUMLModel.PROPERTY_PROFILE_DIRECTORIES));
		profileWorkspaceDirectoriesText.setText(properties.getProperty(ArgoUMLModel.PROPERTY_PROFILE_WORKSPACE_DIRECTORIES));
		ignoreArgoUmlProfiles.setSelection(properties.getBooleanProperty(ArgoUMLModel.PROPERTY_IGNORE_ARGOUML_PROFILE_DIRECTORIES, false));
	}
	
	protected void storeProperties(){
		super.storeProperties();
		properties.put(ArgoUMLModel.PROPERTY_PROFILE_DIRECTORIES, profileDirectoriesText.getText());
		properties.put(ArgoUMLModel.PROPERTY_PROFILE_WORKSPACE_DIRECTORIES, profileWorkspaceDirectoriesText.getText());
		properties.put(ArgoUMLModel.PROPERTY_MODEL_FILE, fileText.getText());
		properties.put(ArgoUMLModel.PROPERTY_IGNORE_ARGOUML_PROFILE_DIRECTORIES, ignoreArgoUmlProfiles.getSelection() + "");
	}*/
}
