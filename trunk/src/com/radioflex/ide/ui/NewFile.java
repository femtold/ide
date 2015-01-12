package com.radioflex.ide.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.part.FileEditorInput;

import com.radioflex.ide.Activator;
import com.radioflex.ide.Constants;
import com.radioflex.ide.Messages;
import com.radioflex.ide.editor.ASMEditor;

/** The new ASM file wizard. */
public class NewFile extends Wizard implements INewWizard {

	private WizardNewFileCreationPage page1;

	private IStructuredSelection selection;

	public NewFile() {
		super();
		setWindowTitle(Messages.WIZARD_NEW_FILE_TITLE);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	public void addPages() {
		super.addPages();

		page1 = new WizardNewFileCreationPage(
				Messages.WIZARD_NEW_FILE_PAGE1_TITLE, selection) {
			protected boolean validatePage() {
				// validate the file extension must be .asm
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

	@Override
	public boolean performFinish() {

		IFile file = page1.createNewFile();
		// open RadioFlex Editor rightly after a new asm file is created
		if (file != null && file.exists() && !file.isPhantom()) {
			try {
				PlatformUI
						.getWorkbench()
						.getActiveWorkbenchWindow()
						.getActivePage()
						.openEditor(new FileEditorInput(file),
								ASMEditor.RADIOFLEX_EDITOR_ID);
			} catch (PartInitException e) {
				Activator.getDefault().getLog().log(e.getStatus());
			}
			return true;
		}
		return false;
	}
}
