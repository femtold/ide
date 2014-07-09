package com.radioflex.ide.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.radioflex.ide.ui.wizards.NewFileWizard;

public class NewFileHandler extends AbstractHandler implements IHandler {

	private Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getShell();

	// private NewFilePage page = new NewFilePage("New File",selection);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		/*IWorkbenchWizard */NewFileWizard wizard = new NewFileWizard();
		WizardDialog dialog = new WizardDialog(shell, wizard);
		if(dialog.open()==Window.OK){
			System.out.println("Dialog Open");
		}
		return null;
	}

}
