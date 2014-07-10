package com.radioflex.ide.ui.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizard;
import org.eclipse.ui.wizards.datatransfer.WizardExternalProjectImportPage;

public class OpenProjectWizard extends ExternalProjectImportWizard {

	private IWizardPage page;

	
	
	public OpenProjectWizard() {
		super();
		// TODO Auto-generated constructor stub
	}



	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		page = new WizardExternalProjectImportPage();
		addPage(page);
	}
	
	
	

}
