package com.radioflex.ide.ui.wizards;

import org.eclipse.ui.wizards.newresource.BasicNewFileResourceWizard;

public class NewFileWizard extends BasicNewFileResourceWizard {

	private NewFileWizardPage page;
	
	

	public NewFileWizard() {
		super();
		System.out.println("new NewFileWizard");
		// TODO Auto-generated constructor stub
	}



	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		page = new NewFileWizardPage("new file", selection);
		addPage(page);
	}
	
}
