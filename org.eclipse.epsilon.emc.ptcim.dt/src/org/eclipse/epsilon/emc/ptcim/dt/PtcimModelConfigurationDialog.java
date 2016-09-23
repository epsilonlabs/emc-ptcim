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
import org.eclipse.epsilon.emc.ptcim.ole.IPtcCollection;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcFileDialog;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcFrameworkFactory;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcModelManager;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcObject;
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
	
	/** The Constant MODEL_TYPE. */
	private static final String MODEL_TYPE = "PTC IM Model";
	
	/** The Constant PTCIM_OLE_EP_ID. */
	// The Extension Point ID
	public static final String PTCIM_OLE_EP_ID = "org.eclipse.epsilon.emc.ptcim.ole";
	
	/** The Constant ATT_CLASS. */
	public static final String ATT_CLASS = "class";

	/** The factory. */
	private IPtcFrameworkFactory factory;
	
	/** The file text label. */
	protected Label fileTextLabel;
	
	/** The file text. */
	protected Text fileText;

	/** The profile directories label. */
	protected Label profileDirectoriesLabel;

	/** The profile directories text. */
	protected Text profileDirectoriesText;
	
	/** The profile workspace directories label. */
	protected Label profileWorkspaceDirectoriesLabel;
	
	/** The profile workspace directories text. */
	protected Text profileWorkspaceDirectoriesText;
	
	/** The browse model file. */
	protected Button browseModelFile;
	
	/** The ignore argo uml profiles. */
	protected Button ignoreArgoUmlProfiles;
	
	/** The reference label. */
	protected Label referenceLabel;
	
	/** The reference text. */
	protected Text referenceText;
	
	/** The server text. */
	private Text serverText;
	
	/** The server label. */
	private Label serverLabel;
	
	/** The repository text. */
	private Text repositoryText;
	
	/** The repository label. */
	private Label repositoryLabel;
	
	/** The version label. */
	private Label versionLabel;
	
	/** The version text. */
	private Text versionText;
	
	/** The from selection checkbox. */
	private Button fromSelectionCheckbox;

	/** The from selection label. */
	private Label fromSelectionLabel;

	/** The selected element id label. */
	private Label selectedElementIdLabel;

	/** The selected element find id button. */
	private Button selectedElementFindIdButton;

	/** The selected element id text. */
	private Text selectedElementIdText;
	
	/** The manager. */
	IPtcModelManager<? extends IPtcObject, ? extends IPtcCollection<? extends IPtcObject>> manager = null;

	/** The two col. */
	private GridData twoCol;
	
	/**
	 * Instantiates a new ptcim model configuration dialog.
	 */
	public PtcimModelConfigurationDialog() {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint(PTCIM_OLE_EP_ID);
		IExtension[] extensions = ep.getExtensions();
		// There should only be one contributor
		IExtension ext = extensions[0];
		IConfigurationElement[] ce = ext.getConfigurationElements();
    	try {
    		factory = (IPtcFrameworkFactory) ce[0].createExecutableExtension(ATT_CLASS);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.close();
	}
	

	/**
	 * Created selected element.
	 *
	 * @param parent the parent
	 * @param groupContent the group content
	 */
	private void createdSelectedElement(final Composite parent, final Composite groupContent) {
		selectedElementIdLabel = new Label(groupContent, SWT.NONE);
		selectedElementIdLabel.setText("Id of the root element:");
		
		selectedElementIdText = new Text(groupContent, SWT.BORDER);
		selectedElementIdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		selectedElementIdText.setToolTipText("Provide a known element ID or use the button below to get the ID of the"
				+ " current selected element");
		selectedElementIdText.setEnabled(false);
		
		selectedElementFindIdButton = new Button(groupContent, SWT.NONE);
		selectedElementFindIdButton.setText("Find ID");
		selectedElementFindIdButton.setToolTipText("Opens the current (or last) opened model and locates the current"
				+ " (or last) selected element. The model information would be updated to match the project.");
		selectedElementFindIdButton.setEnabled(false);
		selectedElementFindIdButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				selectedElementIdText.setText("");
				if (manager != null) {
					IPtcObject ap = null;
					try {
						ap = manager.getActiveProjet();
					} catch (EpsilonCOMException e) {
						showErrorMsg("Failed get active project.");
					}
					if (ap != null) {
//						System.out.println(ap);
//						try {
//							System.out.println(ap.getAttribute("Property", "Name"));
//						} catch (EpsilonCOMException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
						IPtcCollection<? extends IPtcObject> selection = null;
						try {
							selection = manager.getActiveItems();
						} catch (EpsilonCOMException e1) {
							showErrorMsg("Failed to connect to the PTC IM Modeler.");
						}
						if (selection != null) {
							Iterator<? extends IPtcObject> it = selection.iterator();
							boolean seletionExists = true;
							if (!it.hasNext()) {
								showErrorMsg("There is no element selected.");
								seletionExists = false;
							}
							if (seletionExists) {
								IPtcObject current = it.next();
								Object id = null;
								try {
									id = current.getAttribute("Property", "Id");
								} catch (EpsilonCOMException e) {
									showErrorMsg("Failed to get the current selected element ID.");
								}
								if (id != null) {
									selectedElementIdText.setText((String) id);
									String res = setProjectPropertiesText(ap);
									if (res.length() > 0) {
										showErrorMsg(res);
									}
								}
								try {
									current.disconnect();
								} catch (EpsilonCOMException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							try {
								selection.disconnect();
							} catch (EpsilonCOMException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						try {
							ap.disconnect();
						} catch (EpsilonCOMException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}

			private void showErrorMsg(String localizedMessage) {
				//Display display = new Display();
			    //Shell shell = new Shell(display);
				int style = SWT.ICON_ERROR;
				MessageBox messageBox = new MessageBox(parent.getShell(), style);
				messageBox.setText("Error");
			    messageBox.setMessage(localizedMessage);
			    messageBox.open();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.common.dt.launching.dialogs.AbstractCachedModelConfigurationDialog#createGroups(org.eclipse.swt.widgets.Composite)
	 */
	protected void createGroups(Composite control) {
		super.createGroups(control);
		//createFilesGroup(control);
		//createProfilesGroup(control);
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
		openButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				IPtcFileDialog<? extends IPtcObject> diag;
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
	
	/**
	 * Enable element id.
	 *
	 * @param enabled the enabled
	 */
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
	}
	
	/**
	 * Sets the project properties text.
	 *
	 * @param ap the ap
	 * @return the string
	 */
	private String setProjectPropertiesText(IPtcObject ap) {
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
	
	/**
	 * Model reference to fields.
	 *
	 * @param ref the ref
	 */
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
	protected void storeProperties(){
		super.storeProperties();
		properties.put(PtcimModel.PROPERTY_MODEL_REFERENCE, referenceText.getText());
		properties.put(PtcimModel.PROPERTY_SERVER_NAME, serverText.getText());
		properties.put(PtcimModel.PROPERTY_REPOSITORY_NAME, repositoryText.getText());
		properties.put(PtcimModel.PROPERTY_VERSION_NUMBER, versionText.getText());
		String fromSelection = Boolean.toString(fromSelectionCheckbox.getSelection());
		properties.put(PtcimModel.PROPERTY_FROM_SELECTION, fromSelection);
		properties.put(PtcimModel.PROPERTY_ELEMENT_ID, selectedElementIdText.getText());
	}
	
}
