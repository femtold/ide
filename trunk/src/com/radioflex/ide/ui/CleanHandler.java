package com.radioflex.ide.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.ide.actions.BuildUtilities;
import org.eclipse.ui.internal.ide.dialogs.CleanDialog;

/**
 * Discards any problems and previously built state. 
 *
 */
@SuppressWarnings("restriction")
public class CleanHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (window != null) {
			IProject[] selected = BuildUtilities.findSelectedProjects(window);
			
			//invoke the eclipse clean dialog
			new CleanDialog(window,selected).open();
		}
		return null;
	}
}
