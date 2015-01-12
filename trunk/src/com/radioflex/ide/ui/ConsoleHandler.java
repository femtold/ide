package com.radioflex.ide.ui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * 
 * Connected a new message stream to  the output console
 *
 */
public class ConsoleHandler {
	
	private static MessageConsoleStream consoleStream;
	
	//Output the right message
	public static void info(final String _message){
		Display.getDefault().asyncExec(new Runnable(){
			public void run(){
				consoleStream = ConsoleFactory.getConsole().newMessageStream();
				consoleStream.print(_message + "\n");
			}
		});
	}
	
	//Output the error message with red foreground color
	public static void error(final String _message){
		Display.getDefault().asyncExec(new Runnable(){
			public void run(){
				consoleStream = ConsoleFactory.getConsole().newMessageStream();
				consoleStream.setColor(new Color(null,255,0,0));
				consoleStream.println(_message + "\n");
			}
		});
	}
}
