package com.radioflex.ide.ui.wizards;

import org.eclipse.ui.wizards.newresource.BasicNewFileResourceWizard;

public class NewFileWizard extends BasicNewFileResourceWizard {


/*	private IStructuredSelection selection;
	private IWorkbench workbench;
	private NewFileWizardPage page;*/

	private NewFileWizardPage page;

	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		page = new NewFileWizardPage("new file", selection);
		addPage(page);
	}
	
/*	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		this.workbench = workbench;
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return true;
	}*/
	

}
