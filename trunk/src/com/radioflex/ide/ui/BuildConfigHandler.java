package com.radioflex.ide.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.RadioState;

/**
 * 
 * Configure build mode.
 *
 */
public class BuildConfigHandler extends AbstractHandler implements IHandler {
	
    static int i = 0;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (HandlerUtil.matchesRadioState(event))
			return null;
		
		//Get the radio state
		String currentState = event.getParameter(RadioState.PARAMETER_ID);
		if(currentState.equals("Simulator"))
			i = 0;
			
		else if(currentState.equals("FPGA"))
			i = 1;
			
		HandlerUtil.updateRadioState(event.getCommand(), currentState);
		return null;
	}
	
	public static int activeConfig(){
		return i;
	}

}
