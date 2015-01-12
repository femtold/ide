package com.radioflex.ide.debug;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

public class Run extends AbstractHandler implements IHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String[] strArray = { "rfdbg.run()\n", "rfdbg.status()\n" };
		try {
			for (int i = 0; i < strArray.length; i++) {
				Start.sendString(strArray[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		DebugState.setState(3);
		return null;
	}

	public boolean isEnabled() {
		if (DebugState.getState() != 1) {
			return true;
		} else
			return false;
	}
}
