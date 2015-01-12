package com.radioflex.ide.ui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import com.radioflex.ide.Constants;
import com.radioflex.ide.Messages;

/**
 * Customize the new project wizard page.
 *
 */
public class NewProjectPage extends WizardNewProjectCreationPage {

	private Composite container;
	private Composite browseComposite;
	private Label includeLabel;
	private Text include_text;
	private Button browse1;
	private Label toolchainLabel;
	private Text toolchainText;
	private Button browse2;
	private Label aflLabel;
	private Text aflText;
	private Button browse3;
	private Label hcLabel;
	private Text hcText;
	private Button addButton;
	private File filterPath = null;
	private String[] extensions = null;
	private Button hardwareBtn;
	private Button simulatorBtn;

	
	/**
	 * The constructor.
	 */
	public NewProjectPage() {
		super(Messages.WIZARD_NEW_PROJECT_TITLE);
		setTitle(Messages.WIZARD_NEW_PROJECT_PAGE1_TITLE);
		setDescription(Messages.WIZARD_NEW_PROJECT_DESCRIPTION);
		setImageDescriptor(Constants.WIZARD_NEW);
	}
	
	/**
	 * Create all visible controls. 
	 */
	public void createControl(Composite parent) {
		
		//extends WizardNewProjectCreationPage controls
		super.createControl(parent);

		container = (Composite) getControl();
		GridLayout layout = new GridLayout();
		container.setLayout(layout);

		//add browse composite
		browseComposite = new Composite(container, SWT.NONE);
		browseComposite.setLayout(new GridLayout(3, false));

		//add the include directory field
		includeLabel = new Label(browseComposite, SWT.NONE);
		includeLabel.setText(Messages.INCLUDE_DIRECTORIES);

		include_text = new Text(browseComposite, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_text.widthHint = 323;
		include_text.setLayoutData(gd_text);

		browse1 = new Button(browseComposite, SWT.NONE);
		browse1.setText(Messages.BROWSE);
		GridData gd_button = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_button.widthHint = 108;
		browse1.setLayoutData(gd_button);
		browse1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (changePressed(1) == null)
					return;
				else
					include_text.setText(changePressed(1));

			}
		});

		//add tool chain directory field
		toolchainLabel = new Label(browseComposite, SWT.NONE);
		toolchainLabel.setText(Messages.TOOLCHAIN_DIRECTORIES);

		toolchainText = new Text(browseComposite, SWT.BORDER);
		toolchainText.setLayoutData(gd_text);

		browse2 = new Button(browseComposite, SWT.NONE);
		browse2.setText(Messages.BROWSE);

		browse2.setLayoutData(gd_button);
		browse2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (changePressed(1) == null)
					return;
				else
					toolchainText.setText(changePressed(2));
			}
		});

		//add the architecture file location browse field
		aflLabel = new Label(browseComposite, SWT.NONE);
		aflLabel.setText(Messages.ARCHITECUTRE_FILR_LOCATION);

		aflText = new Text(browseComposite, SWT.BORDER);
		aflText.setLayoutData(gd_text);

		browse3 = new Button(browseComposite, SWT.NONE);
		browse3.setText(Messages.BROWSE);

		browse3.setLayoutData(gd_button);
		browse3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (changePressed(1) == null)
					return;
				else
					aflText.setText(changePressed(3));
			}
		});

		//add the hardware configuration field
		hcLabel = new Label(browseComposite, SWT.NONE);
		hcLabel.setText(Messages.HARDWARE_CONFIGURATION);

		hcText = new Text(browseComposite, SWT.BORDER);
		hcText.setLayoutData(gd_text);

		addButton = new Button(browseComposite, SWT.NONE);
		addButton.setText(Messages.ADD_BUTTON);

		addButton.setLayoutData(gd_button);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (changePressed(1) == null)
					return;
				else
					hcText.setText(changePressed(4));
			}
		});

		//add the radio button about hardware and simulator
		hardwareBtn = new Button(browseComposite, SWT.RADIO);
		hardwareBtn.setText(Messages.HW_RADIO_BUTTON);

		simulatorBtn = new Button(browseComposite, SWT.RADIO);
		simulatorBtn.setText(Messages.SIM_RADIO_BUTTON);

		setControl(container);
	}

	private String changePressed(int index) {
		File f = new File(getTextControl(index).getText());
		if (!f.exists()) {
			f = null;
		}
		File d = getFile(f);
		if (d == null) {
			return null;
		}

		return d.getAbsolutePath();
	}

	private Text getTextControl(int i) {
		switch (i) {
		case 1:
			return include_text;
		case 2:
			return toolchainText;
		case 3:
			return aflText;
		default:
			return hcText;
		}
	}

	private File getFile(File startingDirectory) {

		FileDialog dialog = new FileDialog(getShell(), SWT.OPEN | SWT.SHEET);
		if (startingDirectory != null) {
			dialog.setFileName(startingDirectory.getPath());
		} else if (filterPath != null) {
			dialog.setFilterPath(filterPath.getPath());
		}
		if (extensions != null) {
			dialog.setFilterExtensions(extensions);
		}
		String file = dialog.open();
		if (file != null) {
			file = file.trim();
			if (file.length() > 0) {
				return new File(file);
			}
		}
		return null;
	}

}
