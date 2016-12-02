/*******************************************************************************
 * Copyright (c) 2012 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 *     Horacio Hoyos	 - Additional options
 ******************************************************************************/
package org.eclipse.epsilon.emc.ptcim.dt;

import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.epsilon.common.dt.launching.dialogs.AbstractCachedModelConfigurationDialog;
import org.eclipse.epsilon.emc.ptcim.PtcimModel;
import org.eclipse.epsilon.emc.ptcim.jawin.JawinCollection;
import org.eclipse.epsilon.emc.ptcim.jawin.JawinFileDialog;
import org.eclipse.epsilon.emc.ptcim.jawin.JawinFrameworkFactory;
import org.eclipse.epsilon.emc.ptcim.jawin.JawinModelManager;
import org.eclipse.epsilon.emc.ptcim.jawin.JawinObject;
import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

/**
 * The Class PtcimModelConfigurationDialog.
 */
public class PtcimModelConfigurationDialog extends AbstractCachedModelConfigurationDialog {
	
	private static final String MODEL_TYPE = "PTC IM Model";
	
	public static final String PTCIM_OLE_EP_ID = "org.eclipse.epsilon.emc.ptcim.ole";
	
	public static final String ATT_CLASS = "class";

	private JawinFrameworkFactory factory;
	
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
	private Button fromSelectionCheckbox;
	private Label fromSelectionLabel;
	private Label selectedElementIdLabel;
	private Button selectedElementFindIdButton;
	private Text selectedElementIdText;
	private Label selectedElementNameAndTypeLabel;
	private Label selectedElementNameAndTypeTextLabel;
	
	JawinModelManager manager = null;

	private GridData twoCol;

	public PtcimModelConfigurationDialog() {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint(PTCIM_OLE_EP_ID);
		IExtension[] extensions = ep.getExtensions();
		// There should only be one contributor
		IExtension ext = extensions[0];
		IConfigurationElement[] ce = ext.getConfigurationElements();
    	try {
    		factory = (JawinFrameworkFactory) ce[0].createExecutableExtension(ATT_CLASS);
		} catch (CoreException e) {
			throw new IllegalStateException(e);
		}
		try {
			factory.startup();;
		} catch (EpsilonCOMException e) {
			throw new IllegalStateException(e);
		}
		try {
			manager = factory.getModelManager();
		} catch (EpsilonCOMException e) {
			throw new IllegalStateException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#close()
	 */
	@Override
	public boolean close() {
		try {
			manager.disconnect();
			factory.shutdown();
		} catch (EpsilonCOMException e) {
			e.printStackTrace();
		}
		return super.close();
	}
	
	private void createdSelectedElement(final Composite parent, final Composite groupContent) {
		selectedElementIdLabel = new Label(groupContent, SWT.NONE);
		selectedElementIdLabel.setText("Id of the root element:");
		
		selectedElementIdText = new Text(groupContent, SWT.BORDER);
		selectedElementIdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		selectedElementIdText.setToolTipText("Provide a known element ID or use the button below to get the ID of the"
				+ " current selected element");
		selectedElementIdText.setEnabled(false);
		
		selectedElementFindIdButton = new Button(groupContent, SWT.NONE);
		selectedElementFindIdButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		selectedElementFindIdButton.setText("Find ID");
		selectedElementFindIdButton.setToolTipText("Opens the current (or last) opened model and locates the current"
				+ " (or last) selected element. The model information would be updated to match the project.");
		selectedElementFindIdButton.setEnabled(false);
		selectedElementFindIdButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				selectedElementIdText.setText("");
				if (manager != null) {
					JawinObject ap = null;
					try {
						ap = manager.getActiveProjet();
					} catch (EpsilonCOMException e) {
						showErrorMsg("Failed to get active project. Make sure the PTC IM Molder is opened and the"
								+ " desired model loaded, and that an element is selected.");
					}
					if (ap != null) {
						JawinCollection selection = null;
						try {
							selection = manager.getActiveItems();
						} catch (EpsilonCOMException e1) {
							showErrorMsg("Failed to connect to the PTC IM Modeler.");
						}
						if (selection != null) {
							Iterator<JawinObject> it = selection.iterator();
							boolean seletionExists = true;
							if (!it.hasNext()) {
								showErrorMsg("There is no element selected.");
								seletionExists = false;
							}
							if (seletionExists) {
								JawinObject current = it.next();
								Object id = null;
								Object name = null;
								Object type = null;
								try {
									id = current.getAttribute("Property", "Id");
									name = current.getAttribute("Property", "Name");
									type = current.getAttribute("Property", "Type");
								} catch (EpsilonCOMException e) {
									showErrorMsg("Failed to get the current selected element ID.");
								}
								if ((id != null) && (name != null) && (type != null)) {
									selectedElementIdText.setText((String) id);
									String displayedNameAndText = (String) name + " (" + (String) type + ")";
									selectedElementNameAndTypeTextLabel.setText(displayedNameAndText);
									String res = setProjectPropertiesText(ap);
									if (res.length() > 0) {
										showErrorMsg(res);
									}
								}
								try {
									current.disconnect();
								} catch (EpsilonCOMException e) {
									e.printStackTrace();
								}
							}
							try {
								selection.disconnect();
							} catch (EpsilonCOMException e) {
								e.printStackTrace();
							}
						}
						try {
							ap.disconnect();
						} catch (EpsilonCOMException e) {
							e.printStackTrace();
						}
					}
				}
			}

			private void showErrorMsg(String localizedMessage) {
				int style = SWT.ICON_ERROR;
				MessageBox messageBox = new MessageBox(parent.getShell(), style);
				messageBox.setText("Error");
			    messageBox.setMessage(localizedMessage);
			    messageBox.open();
			}
		});
		
		selectedElementNameAndTypeLabel = new Label(groupContent, SWT.NONE);
		selectedElementNameAndTypeLabel.setText("");
		
		selectedElementNameAndTypeTextLabel = new Label(groupContent, SWT.NONE);
		selectedElementNameAndTypeTextLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		selectedElementNameAndTypeTextLabel.setEnabled(true);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.common.dt.launching.dialogs.AbstractCachedModelConfigurationDialog#createGroups(org.eclipse.swt.widgets.Composite)
	 */
	protected void createGroups(Composite control) {
		super.createGroups(control);
		createLoadStoreOptionsGroup(control);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.common.dt.launching.dialogs.AbstractModelConfigurationDialog#createNameAliasGroup(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createNameAliasGroup(final Composite parent) {
		// FIXME All labels can be local
		final Composite groupContent = createGroupContainer(parent, "Identification", 3);		
		GridData oneCol = new GridData(GridData.FILL_HORIZONTAL);
		twoCol = new GridData(GridData.FILL_HORIZONTAL);
		twoCol.horizontalSpan = 2;
		nameLabel = new Label(groupContent, SWT.NONE);
		nameLabel.setText("Name: ");
		nameText = new Text(groupContent, SWT.BORDER);
		nameText.setLayoutData(twoCol);
		
		aliasesLabel = new Label(groupContent, SWT.NONE);
		aliasesLabel.setText("Aliases: ");
		aliasesText = new Text(groupContent, SWT.BORDER);
		aliasesText.setLayoutData(twoCol);
		
		referenceLabel = new Label(groupContent, SWT.NONE);
		referenceLabel.setText("Reference: ");
		referenceLabel.setToolTipText("This is the title/id of the model in the PTC IM repository");
		
		referenceText = new Text(groupContent, SWT.BORDER);
		referenceText.setLayoutData(oneCol);
		
		Button openButton = new Button(groupContent, SWT.NONE);
		openButton.setText("Open Model");
		openButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		openButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				JawinFileDialog diag;
				try {
					diag = factory.getFileDialogManager();
				} catch (EpsilonCOMException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return;
				}
				String ref = null;
				try {
					ref = diag.openDialog();
				} catch (EpsilonCOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				if (ref != null)
					modelReferenceToFields(ref);
			}
		});
		
		serverLabel = new Label(groupContent, SWT.NONE);
		serverLabel.setText("Server: ");
		serverLabel.setToolTipText("Leave blank to open the model by title.");
		
		serverText = new Text(groupContent, SWT.BORDER);
		serverText.setLayoutData(twoCol);
		
		repositoryLabel = new Label(groupContent, SWT.NONE);
		repositoryLabel.setText("Repository: ");
		repositoryLabel.setToolTipText("Leave blank to open the model by title.");
		
		repositoryText = new Text(groupContent, SWT.BORDER);
		repositoryText.setLayoutData(twoCol);
		
		versionLabel = new Label(groupContent, SWT.NONE);
		versionLabel.setText("Version: ");
		versionLabel.setToolTipText("Leave blank to open the latest version of the model. This option can oly be used to open models by refernce(id)");
		
		versionText = new Text(groupContent, SWT.BORDER);
		versionText.setLayoutData(twoCol);
		
		fromSelectionLabel = new Label(groupContent, SWT.NONE);
		fromSelectionLabel.setText("Select element as root: ");

		fromSelectionCheckbox = new Button(groupContent, SWT.CHECK);
		fromSelectionCheckbox.setLayoutData(twoCol);
		fromSelectionCheckbox.setToolTipText("If checked, Epsilon scripts will be run using the selected element as root. "
				+ "The selected element should prerably be a package. "
				+ "If not, performance might be affected.");
		fromSelectionCheckbox.setSelection(false);
		fromSelectionCheckbox.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				 if (fromSelectionCheckbox.getSelection())
			            enableElementId(true);
			        else
			        	enableElementId(false);
			}
		});
		
		createdSelectedElement(parent, groupContent);
		
		GridData fromSelectionButtonData = new GridData();
		fromSelectionButtonData.horizontalSpan = 2;
		fromSelectionCheckbox.setLayoutData(fromSelectionButtonData);
		
		groupContent.layout();
		groupContent.pack();
	}
	
	private void enableElementId(boolean enabled) {
		selectedElementIdText.setEnabled(enabled);
		selectedElementFindIdButton.setEnabled(enabled);
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.common.dt.launching.dialogs.AbstractModelConfigurationDialog#getModelName()
	 */
	protected String getModelName() {
		return "PTC Integrity Modeler Model";
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.common.dt.launching.dialogs.AbstractModelConfigurationDialog#getModelType()
	 */
	protected String getModelType() {
		return MODEL_TYPE;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.common.dt.launching.dialogs.AbstractCachedModelConfigurationDialog#loadProperties()
	 */
	protected void loadProperties(){
		super.loadProperties();
		if (properties == null) return;
		referenceText.setText(properties.getProperty(PtcimModel.PROPERTY_MODEL_REFERENCE));
		serverText.setText(properties.getProperty(PtcimModel.PROPERTY_SERVER_NAME));
		repositoryText.setText(properties.getProperty(PtcimModel.PROPERTY_REPOSITORY_NAME));
		versionText.setText(properties.getProperty(PtcimModel.PROPERTY_VERSION_NUMBER));
		boolean fromSelection = properties.getBooleanProperty(PtcimModel.PROPERTY_FROM_SELECTION, false);
		fromSelectionCheckbox.setSelection(fromSelection);
		enableElementId(fromSelection);
		selectedElementIdText.setText(properties.getProperty(PtcimModel.PROPERTY_ELEMENT_ID));
		selectedElementNameAndTypeTextLabel.setText(properties.getProperty(PtcimModel.PROPERTY_ELEMENT_NAME_AND_TYPE));
	}

	private String setProjectPropertiesText(JawinObject ap) {
		// Get current project information
		String ref = null;
		try {
			ref = (String) ap.getAttribute("Property", "Reference");
		} catch (EpsilonCOMException e) {
			return "Failed to get the current project information. The information in"
					+ " the form may not match the current project properties.";
		}
		if (ref != null) {
			modelReferenceToFields(ref);
		}
		return "";
	}
	
	private void modelReferenceToFields(String ref) {
		String[] info = ref.split("\\\\");
		serverText.setText(info[3]);
		repositoryText.setText(info[4]);
		referenceText.setText(info[5]);
		versionText.setText(info[6]);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.common.dt.launching.dialogs.AbstractCachedModelConfigurationDialog#storeProperties()
	 */
	protected void storeProperties() {
		super.storeProperties();
		properties.put(PtcimModel.PROPERTY_MODEL_REFERENCE, referenceText.getText());
		properties.put(PtcimModel.PROPERTY_SERVER_NAME, serverText.getText());
		properties.put(PtcimModel.PROPERTY_REPOSITORY_NAME, repositoryText.getText());
		properties.put(PtcimModel.PROPERTY_VERSION_NUMBER, versionText.getText());
		String fromSelection = Boolean.toString(fromSelectionCheckbox.getSelection());
		properties.put(PtcimModel.PROPERTY_FROM_SELECTION, fromSelection);
		properties.put(PtcimModel.PROPERTY_ELEMENT_ID, selectedElementIdText.getText());
		properties.put(PtcimModel.PROPERTY_ELEMENT_NAME_AND_TYPE, selectedElementNameAndTypeTextLabel.getText());
	}
}
