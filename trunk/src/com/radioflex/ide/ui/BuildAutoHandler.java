package com.radioflex.ide.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.radioflex.ide.Activator;

/**
 * Set whether build automatically or not. 
 *
 */
public class BuildAutoHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		//this is not contributing //????
		Command command = event.getCommand();
		boolean oldvalue = HandlerUtil.toggleCommandState(command);
		
		IWorkspaceDescription wsdescrip = ResourcesPlugin.getWorkspace().getDescription();
		wsdescrip.setAutoBuilding(!oldvalue);
		try {
			ResourcesPlugin.getWorkspace().setDescription(wsdescrip);
		} catch (CoreException e) {
			Activator.getDefault().getLog().log(e.getStatus());
		}		
		
		return null;
	}

}
