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
package org.eclipse.epsilon.emc.ptcim.dt;

import org.eclipse.epsilon.common.dt.launching.dialogs.AbstractCachedModelConfigurationDialog;
import org.eclipse.epsilon.emc.ptcim.PtcimModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class PtcimModelConfigurationDialog extends AbstractCachedModelConfigurationDialog {
	
	private static final String MODEL_TYPE = "PTC IM Model";

	protected String getModelName() {
		return "PTC Integrity Modeler Model";
	}

	protected String getModelType() {
		return MODEL_TYPE;
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
	private Label versionLabel;
	private Text versionText;
	private Button fromSelectionButton;
	
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
		referenceLabel.setToolTipText("This is the title/id of the model in the PTC IM repository");
		
		referenceText = new Text(groupContent, SWT.BORDER);
		referenceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		serverLabel = new Label(groupContent, SWT.NONE);
		serverLabel.setText("Server: ");
		serverLabel.setToolTipText("Leave blank to open the model by title.");
		
		serverText = new Text(groupContent, SWT.BORDER);
		serverText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		repositoryLabel = new Label(groupContent, SWT.NONE);
		repositoryLabel.setText("Repository: ");
		repositoryLabel.setToolTipText("Leave blank to open the model by title.");
		
		repositoryText = new Text(groupContent, SWT.BORDER);
		repositoryText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		versionLabel = new Label(groupContent, SWT.NONE);
		versionLabel.setText("Version: ");
		versionLabel.setToolTipText("Leave blank to open the latest version of the model. This option can oly be used to open models by refernce(id)");
		
		versionText = new Text(groupContent, SWT.BORDER);
		versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		fromSelectionButton = new Button(groupContent, SWT.CHECK);
		fromSelectionButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fromSelectionButton.setText("Selected element as root");
		fromSelectionButton.setToolTipText("If checked, Epsilon scripts will be run using the selected element as root. "
				+ "The selected element should prerably be a package. "
				+ "If not, performance might be affected.");
		fromSelectionButton.setSelection(false);
		
		GridData fromSelectionButtonData = new GridData();
		fromSelectionButtonData.horizontalSpan = 2;
		fromSelectionButton.setLayoutData(fromSelectionButtonData);
		
		groupContent.layout();
		groupContent.pack();
	}
	

	
	protected void loadProperties(){
		super.loadProperties();
		if (properties == null) return;
		referenceText.setText(properties.getProperty(PtcimModel.PROPERTY_MODEL_REFERENCE));
		serverText.setText(properties.getProperty(PtcimModel.PROPERTY_SERVER_NAME));
		repositoryText.setText(properties.getProperty(PtcimModel.PROPERTY_REPOSITORY_NAME));
		versionText.setText(properties.getProperty(PtcimModel.PROPERTY_VERSION_NUMBER));
		fromSelectionButton.setSelection(Boolean.getBoolean(properties.getProperty(PtcimModel.PROPERTY_FROM_SELECTION)));
	}
	
	protected void storeProperties(){
		super.storeProperties();
		properties.put(PtcimModel.PROPERTY_MODEL_REFERENCE, referenceText.getText());
		properties.put(PtcimModel.PROPERTY_SERVER_NAME, serverText.getText());
		properties.put(PtcimModel.PROPERTY_REPOSITORY_NAME, repositoryText.getText());
		properties.put(PtcimModel.PROPERTY_VERSION_NUMBER, versionText.getText());
		properties.put(PtcimModel.PROPERTY_FROM_SELECTION, Boolean.toString(fromSelectionButton.getSelection()));
	}
	
}
