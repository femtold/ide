package com.radioflex.ide.ui.wizards;

import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

public class NewProjectWizard extends BasicNewProjectResourceWizard {
	private WizardNewProjectCreationPage page;
	
	public NewProjectWizard() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		page = new WizardNewProjectCreationPage("new project");
		addPage(page);
		
	}
	
	
}
