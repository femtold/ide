package com.radioflex.ide.debug;

import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.PlatformUI;

import com.radioflex.ide.Constants;
import com.radioflex.ide.ui.ConsoleHandler;
import com.radioflex.ide.view.XMLView;

public class Stop extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ConsoleHandler.info("Simulator has finished running");
		ArrayList<String> stringList = TerminalOutputListener.getString();
		XMLView xmlView = (XMLView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView(Constants.VIEW_XML);
		DebugState.setState(1);
		return null;
	}

	@Override
	public boolean isEnabled() {
		if(DebugState.getState() != 1)
			return true;
		else 
			return false;
	}

}
