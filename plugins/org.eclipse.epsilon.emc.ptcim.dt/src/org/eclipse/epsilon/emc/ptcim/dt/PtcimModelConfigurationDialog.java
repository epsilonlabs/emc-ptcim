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

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ForkJoinPool;

import javax.swing.JOptionPane;

import org.eclipse.epsilon.common.dt.launching.dialogs.AbstractCachedModelConfigurationDialog;
import org.eclipse.epsilon.emc.ptcim.ClassFactory;
import org.eclipse.epsilon.emc.ptcim.IAutomationCaseObject;
import org.eclipse.epsilon.emc.ptcim.PtcimCollection;
import org.eclipse.epsilon.emc.ptcim.PtcimFileDialog;
import org.eclipse.epsilon.emc.ptcim.PtcimFrameworkFactory;
import org.eclipse.epsilon.emc.ptcim.PtcimModel;
import org.eclipse.epsilon.emc.ptcim.PtcimModelManager;
import org.eclipse.epsilon.emc.ptcim.PtcimObject;
import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

/**
 * The Class PtcimModelConfigurationDialog.
 */
public class PtcimModelConfigurationDialog extends AbstractCachedModelConfigurationDialog implements Observer {

	private static final String MODEL_TYPE = "PTC IM Model";

	public static final String PTCIM_OLE_EP_ID = "org.eclipse.epsilon.emc.ptcim.ole";

	public static final String ATT_CLASS = "class";

	private PtcimFrameworkFactory factory;

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
	private Button propertiesAttributesCacheEnabledCheckbox;
	private Label propertiesAttributesCacheEnabledLabel;
	private Button propertiesValuesCacheEnabledCheckbox;
	private Label propertiesValuesCacheEnabledLabel;

	PtcimModelManager manager = null;

	private GridData twoCol;
	private IAutomationCaseObject projects;
	boolean isConnected = false;
	public PtcimModelConfigurationDialog() {
		factory = new PtcimFrameworkFactory();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					projects = ClassFactory.createCCaseProjects();
					if (PtcimFileDialog.dialog == null) {
						PtcimFileDialog.dialog = ClassFactory.createArtisanModelFileDialog();
					}
					isConnected = true;
					try {
						manager = factory.getModelManager(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
					manager.setProjects(ClassFactory.createCCaseProjects());
				} catch (Exception e1) {
					Object[] options = {"Close", "Read more..."};
					int n = JOptionPane.showOptionDialog(null,
					    "Running the driver from 64-bit Java needs some registry changes. Click the read more button to read more.",
					    "64-bit compatibility [CCaseObject]",
					    JOptionPane.YES_NO_OPTION,
					    JOptionPane.INFORMATION_MESSAGE,
					    null,     //do not use a custom Icon
					    options,  //the titles of buttons
					    options[0]); //default button title
					if (n == 1) {
						try {
							Desktop.getDesktop().browse(new URL("https://github.com/epsilonlabs/emc-ptcim/blob/master/README.md#running-from-64-bit-java-environments").toURI());
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			}
		}).start();
		// We need to know if the whole process was triggered by the UI or not. This is done by passing an attribute to the model manager. 
		// Here we know that this is accessed if and only if the UI was used so we pass the 'true' value for the fromUI parameter.
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#close()
	 */
	@Override
	public boolean close() {
		//manager.disconnect();
		factory.shutdown();
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
		
		selectedElementNameAndTypeLabel = new Label(groupContent, SWT.NONE);
		selectedElementNameAndTypeLabel.setText("");

		selectedElementNameAndTypeTextLabel = new Label(groupContent, SWT.NONE);
		selectedElementNameAndTypeTextLabel.setLayoutData(twoCol);
		selectedElementNameAndTypeTextLabel.setEnabled(true);
		
		selectedElementFindIdButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				selectedElementIdText.setText("");
				new Thread(new Runnable() {
					@Override
					public void run() {
						if (manager != null) {
							PtcimObject ap = null;
							try {
								ap = manager.getActiveProject();
							} catch (EolInternalException e) {
								showErrorMsg("Failed to get active project. Make sure the PTC IM Molder is opened and the"
										+ " desired model loaded, and that an element is selected.");
							}
							if (ap != null) {
								PtcimCollection selection = null;
								try {
									selection = manager.getActiveItems();
								} catch (EolInternalException e1) {
									showErrorMsg("Failed to connect to the PTC IM Modeler.");
								}
								if (selection != null) {
									Iterator<PtcimObject> it = selection.iterator();
									boolean seletionExists = true;
									if (!it.hasNext()) {
										showErrorMsg("There is no element selected.");
										seletionExists = false;
									}
									if (seletionExists) {
										PtcimObject current = it.next();
										final String id = current.property("Id", null).toString();
										final String name = current.property("Name", null).toString();
										final String type = current.property("Type", null).toString();
										if ((id != null) && (name != null) && (type != null)) {
											Display.getDefault().asyncExec(new Runnable() {
											    public void run() {
											    	selectedElementIdText.setText((String) id);
											    	String displayedNameAndText = (String) name + " (" + (String) type + ")";
											    	selectedElementNameAndTypeTextLabel.setText(displayedNameAndText);
											    	
											    }
											});
											String res = setProjectPropertiesText(ap);
									    	if (res.length() > 0) {
									    		showErrorMsg(res);
									    	}
										}
									}
									selection.disconnect();
								}
							}
						}
				    }
				}).start();
			}

			private void showErrorMsg(String localizedMessage) {
				int style = SWT.ICON_ERROR;
				MessageBox messageBox = new MessageBox(parent.getShell(), style);
				messageBox.setText("Error");
				messageBox.setMessage(localizedMessage);
				messageBox.open();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.epsilon.common.dt.launching.dialogs.
	 * AbstractCachedModelConfigurationDialog#createGroups(org.eclipse.swt.
	 * widgets.Composite)
	 */
	protected void createGroups(Composite control) {
		super.createGroups(control);
		createLoadStoreOptionsGroup(control);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.epsilon.common.dt.launching.dialogs.
	 * AbstractModelConfigurationDialog#createNameAliasGroup(org.eclipse.swt.
	 * widgets.Composite)
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
		
		final Observer o = this;	

		openButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				PtcimFileDialog diag;
				try {
					diag = factory.getFileDialogManager(o);
				} catch (EolInternalException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return;
				}
				String ref = null;
				try {
					ref = diag.openDialog();
				} catch (EolInternalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				/*
				 * if (ref != null) modelReferenceToFields(ref);
				 */
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
		versionLabel.setToolTipText(
				"Leave blank to open the latest version of the model. This option can oly be used to open models by refernce(id)");

		versionText = new Text(groupContent, SWT.BORDER);
		versionText.setLayoutData(twoCol);

		propertiesAttributesCacheEnabledLabel = new Label(groupContent, SWT.NONE);
		propertiesAttributesCacheEnabledLabel.setText("Enable properties' attributes cache: ");

		propertiesAttributesCacheEnabledCheckbox = new Button(groupContent, SWT.CHECK);
		propertiesAttributesCacheEnabledCheckbox.setLayoutData(twoCol);
		propertiesAttributesCacheEnabledCheckbox.setToolTipText(
				"If checked, Epsilon scripts will create and populate a cache with the attributes retrieved for each property.");
		propertiesAttributesCacheEnabledCheckbox.setSelection(false);

		propertiesValuesCacheEnabledLabel = new Label(groupContent, SWT.NONE);
		propertiesValuesCacheEnabledLabel.setText("Enable properties' values cache: ");

		propertiesValuesCacheEnabledCheckbox = new Button(groupContent, SWT.CHECK);
		propertiesValuesCacheEnabledCheckbox.setLayoutData(twoCol);
		propertiesValuesCacheEnabledCheckbox.setToolTipText(
				"If checked, Epsilon scripts will create and populate a cache with the values retrieved for each property.");
		propertiesValuesCacheEnabledCheckbox.setSelection(false);

		fromSelectionLabel = new Label(groupContent, SWT.NONE);
		fromSelectionLabel.setText("Select element as root: ");

		fromSelectionCheckbox = new Button(groupContent, SWT.CHECK);
		fromSelectionCheckbox.setLayoutData(twoCol);
		fromSelectionCheckbox
				.setToolTipText("If checked, Epsilon scripts will be run using the selected element as root. "
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

		groupContent.layout(true, true);
		groupContent.pack(true);
	}

	private void enableElementId(boolean enabled) {
		selectedElementIdText.setEnabled(enabled);
		selectedElementFindIdButton.setEnabled(enabled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.epsilon.common.dt.launching.dialogs.
	 * AbstractModelConfigurationDialog#getModelName()
	 */
	protected String getModelName() {
		return "PTC Integrity Modeler Model";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.epsilon.common.dt.launching.dialogs.
	 * AbstractModelConfigurationDialog#getModelType()
	 */
	protected String getModelType() {
		return MODEL_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.epsilon.common.dt.launching.dialogs.
	 * AbstractCachedModelConfigurationDialog#loadProperties()
	 */
	protected void loadProperties() {
		super.loadProperties();
		if (properties == null)
			return;
		referenceText.setText(properties.getProperty(PtcimModel.PROPERTY_MODEL_REFERENCE));
		serverText.setText(properties.getProperty(PtcimModel.PROPERTY_SERVER_NAME));
		repositoryText.setText(properties.getProperty(PtcimModel.PROPERTY_REPOSITORY_NAME));
		versionText.setText(properties.getProperty(PtcimModel.PROPERTY_VERSION_NUMBER));
		boolean fromSelection = properties.getBooleanProperty(PtcimModel.PROPERTY_FROM_SELECTION, false);
		fromSelectionCheckbox.setSelection(fromSelection);
		enableElementId(fromSelection);
		selectedElementIdText.setText(properties.getProperty(PtcimModel.PROPERTY_ELEMENT_ID));
		selectedElementNameAndTypeTextLabel
				.setText(properties.getProperty(PtcimModel.PROPERTY_ELEMENT_NAME_AND_TYPE));
		boolean propertiesAttributesCacheEnabledSelection = properties
				.getBooleanProperty(PtcimModel.PROPERTY_PROPERTIES_ATTRIBUTES_CACHE_ENABLED, false);
		propertiesAttributesCacheEnabledCheckbox.setSelection(propertiesAttributesCacheEnabledSelection);
		boolean propertiesValuesCacheEnabledSelection = properties
				.getBooleanProperty(PtcimModel.PROPERTY_PROPERTIES_VALUES_CACHE_ENABLED, false);
		propertiesValuesCacheEnabledCheckbox.setSelection(propertiesValuesCacheEnabledSelection);
	}

	private String setProjectPropertiesText(PtcimObject ap) {
		// Get current project information
		final String ref = (String) ap.property("Reference", null);
		Display.getDefault().asyncExec(new Runnable() {
		    public void run() {
				if (ref != null) {
					modelReferenceToFields(ref);
				}
		    }
		});
		return "";
	}

	public void modelReferenceToFields(String ref) {
		String[] info = ref.split("\\\\");
		serverText.setText(info[3]);
		repositoryText.setText(info[4]);
		referenceText.setText(info[5]);
		versionText.setText(info[6]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.epsilon.common.dt.launching.dialogs.
	 * AbstractCachedModelConfigurationDialog#storeProperties()
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
		String propertiesAttributesCacheEnabledSelection = Boolean
				.toString(propertiesAttributesCacheEnabledCheckbox.getSelection());
		properties.put(PtcimModel.PROPERTY_PROPERTIES_ATTRIBUTES_CACHE_ENABLED,
				propertiesAttributesCacheEnabledSelection);
		String propertiesValuesCacheEnabledSelection = Boolean
				.toString(propertiesValuesCacheEnabledCheckbox.getSelection());
		properties.put(PtcimModel.PROPERTY_PROPERTIES_VALUES_CACHE_ENABLED, propertiesValuesCacheEnabledSelection);
	}

	// When the user has selected the model from the PTC IM model explorer, the observable object notifies this observer and the update method is called.
	// We need to run the population of the UI from the default display thread because this is the only thread that can update Java SWT UIs.
	@Override
	public void update(Observable o, Object arg) {
		final String theReference = (String) arg;
		Display.getDefault().asyncExec(new Runnable() {
		    public void run() {
				modelReferenceToFields((theReference));
		    }
		});
	}
}
