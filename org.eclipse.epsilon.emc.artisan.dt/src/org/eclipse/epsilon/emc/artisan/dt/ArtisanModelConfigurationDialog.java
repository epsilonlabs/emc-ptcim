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
import org.eclipse.epsilon.emc.artisan.ArtisanModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
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
	
	protected Label referenceLabel;
	protected Text referenceText;
	private Text serverText;
	private Label serverLabel;
	private Text repositoryText;
	private Label repositoryLabel;
	
	protected void createGroups(Composite control) {
		super.createGroups(control);
		//createFilesGroup(control);
		//createProfilesGroup(control);
		createLoadStoreOptionsGroup(control);
	}
	
	@Override
	protected void createNameAliasGroup(Composite parent) {
		final Composite groupContent = createGroupContainer(parent, "Identification", 2);		
		
		nameLabel = new Label(groupContent, SWT.NONE);
		nameLabel.setText("Name: ");
		
		nameText = new Text(groupContent, SWT.BORDER);
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		aliasesLabel = new Label(groupContent, SWT.NONE);
		aliasesLabel.setText("Aliases: ");
		
		aliasesText = new Text(groupContent, SWT.BORDER);
		aliasesText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		referenceLabel = new Label(groupContent, SWT.NONE);
		referenceLabel.setText("Reference: ");
		referenceLabel.setToolTipText("This is the name of the model in the Artisan repository");
		
		referenceText = new Text(groupContent, SWT.BORDER);
		referenceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		serverLabel = new Label(groupContent, SWT.NONE);
		serverLabel.setText("Server: ");
		serverLabel.setToolTipText("Leave blank to use the local server.");
		
		serverText = new Text(groupContent, SWT.BORDER);
		serverText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		repositoryLabel = new Label(groupContent, SWT.NONE);
		repositoryLabel.setText("Repository: ");
		repositoryLabel.setToolTipText("Leave blank to use the default repository.");
		
		repositoryText = new Text(groupContent, SWT.BORDER);
		repositoryText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		groupContent.layout();
		groupContent.pack();
	}
	

	
	protected void loadProperties(){
		super.loadProperties();
		if (properties == null) return;
		referenceText.setText(properties.getProperty(ArtisanModel.PROPERTY_MODEL_REFERENCE));
		serverText.setText(properties.getProperty(ArtisanModel.PROPERTY_SERVER_NAME));
		repositoryText.setText(properties.getProperty(ArtisanModel.PROPERTY_REPOSITORY_NAME));
	}
	
	protected void storeProperties(){
		super.storeProperties();
		properties.put(ArtisanModel.PROPERTY_MODEL_REFERENCE, referenceText.getText());
		properties.put(ArtisanModel.PROPERTY_SERVER_NAME, serverText.getText());
		properties.put(ArtisanModel.PROPERTY_REPOSITORY_NAME, repositoryText.getText());
	}
	
}
