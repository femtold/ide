package com.radioflex.ide.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.handlers.HandlerUtil;

public class SaveAsHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		
		SaveAsDialog sv= new SaveAsDialog(window.getShell());
		sv.open();
		return null;
	}

}
