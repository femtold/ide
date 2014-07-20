package org.radioflex.ide.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.radioflex.ide.Constants;
import org.radioflex.ide.Messages;

/**
 * The new ASM file wizard.
 * @author YMLiang
 *
 */
public class NewASMfile extends Wizard implements INewWizard {
	/**
	 * The first page of the wizard.
	 */
	private WizardNewFileCreationPage page1;

	private IStructuredSelection selection;

	/**
	 * The constructor.
	 */
	public NewASMfile() {
		super();
		setWindowTitle(Messages.WIZARD_NEW_FILE_TITLE);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addPages() {
		super.addPages();

		page1 = new WizardNewFileCreationPage(
				Messages.WIZARD_NEW_FILE_PAGE1_TITLE, selection) {
			protected boolean validatePage() {
				if (!getFileName().toLowerCase().endsWith(".asm")) {
					setErrorMessage(Messages.WIZARD_NEW_FILE_PAGE1_INVALID_FILE);
					return false;
				}
				return super.validatePage();
			}
		};
		page1.setTitle(Messages.WIZARD_NEW_FILE_PAGE1_TITLE);
		page1.setImageDescriptor(Constants.WIZARD_NEW);
		page1.setDescription(Messages.WIZARD_NEW_PROJECT_DESCRIPTION);

		addPage(page1);

	}

	/**
	 * {@inheritDoc}
	 */
	public boolean performFinish() {

		IFile file = page1.createNewFile();

		if (file == null) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}
