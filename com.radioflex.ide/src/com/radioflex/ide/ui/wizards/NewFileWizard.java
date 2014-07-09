package com.radioflex.ide.ui.wizards;

import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewFileResourceWizard;

public class NewFileWizard extends BasicNewFileResourceWizard {
	
	 private WizardNewFileCreationPage page;
	/**
	 * 
	 */
	public NewFileWizard() {
		super();
		// TODO Auto-generated constructor stub
	}



	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		super.addPages();
		/*page = new WizardNewFileCreationPage("New File", getSelection());
		addPage(page);*/
	}

}
