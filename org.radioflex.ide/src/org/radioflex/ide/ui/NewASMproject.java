package org.radioflex.ide.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.radioflex.ide.Activator;
import org.radioflex.ide.Constants;
import org.radioflex.ide.Messages;


/**
 * The new ASM project wizard
 * 
 * @author YMLiang
 * 
 */
public class NewASMproject extends Wizard implements INewWizard {

	/**
	 * The first page of the wizard.
	 */
	private WizardNewProjectCreationPage page1;

	/**
	 * The constructor.
	 */
	public NewASMproject() {
		super();
		setWindowTitle(Messages.WIZARD_NEW_PROJECT_TITLE);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addPages() {
		super.addPages();

		page1 = new WizardNewProjectCreationPage(
				Messages.WIZARD_NEW_FILE_PAGE1_TITLE);
		page1.setTitle(Messages.WIZARD_NEW_FILE_PAGE1_TITLE);
		page1.setImageDescriptor(Constants.WIZARD_NEW);
		page1.setDescription(Messages.WIZARD_NEW_PROJECT_DESCRIPTION);

		addPage(page1);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean performFinish() {
		try {
			IProject project = page1.getProjectHandle();

			project.create(null);
			project.open(null);

			// assign a nature to a project by updating the
			// project's description to include the nature

			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = Constants.NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);

		} catch (CoreException e) {
			Activator.getDefault().getLog().log(e.getStatus());
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

}
