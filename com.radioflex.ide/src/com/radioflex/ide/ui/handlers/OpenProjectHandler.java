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

import com.radioflex.ide.ui.wizards.OpenProjectWizard;

public class OpenProjectHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		
		OpenProjectWizard opw = new OpenProjectWizard();
		opw.init(PlatformUI.getWorkbench(), new StructuredSelection());
		
		WizardDialog dialog = new WizardDialog(window.getShell(), opw);
		dialog.open();
		return null;
	}

}
