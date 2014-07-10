package com.radioflex.ide.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.radioflex.ide.ui.wizards.NewProjectWizard;

public class NewProjectHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		
		NewProjectWizard npw = new NewProjectWizard();
		npw.init(PlatformUI.getWorkbench(), new StructuredSelection());
		
		WizardDialog dialog = new WizardDialog(window.getShell(), npw);
		dialog.open();
		return null;
	}

}
