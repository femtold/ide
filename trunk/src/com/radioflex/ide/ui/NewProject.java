package com.radioflex.ide.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.radioflex.ide.Activator;
import com.radioflex.ide.Constants;
import com.radioflex.ide.Messages;
import com.radioflex.ide.project.ASMNature;

/**
 * The new ASM project wizard.
 *
 */
public class NewProject extends Wizard implements INewWizard {

	/**
	 * The page of the wizard.
	 */
	protected NewProjectPage npp;

	/**
	 * The constructor.
	 */
	public NewProject() {
		super();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}}
	 */
	public String getWindowTitle() {
		return Messages.WIZARD_NEW_PROJECT_TITLE;
	}

	/**
	 * {@inheritDoc}}
	 */
	public void addPages() {
		super.addPages();

		npp = new NewProjectPage();
		addPage(npp);
	}

	/**
	 * {@inheritDoc}}
	 */
	public boolean performFinish() {
		try {
			IProject project = npp.getProjectHandle();

			project.create(null);
			project.open(null);

			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = ASMNature.RADIOFLEX_NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);

		} catch (CoreException e) {
			Activator.getDefault().getLog().log(e.getStatus());
		}
		return true;
	}

}
