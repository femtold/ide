/*
 * Copyright (c) 2012 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.radioflex.ide.debug;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.radioflex.ide.Constants;
import com.radioflex.ide.ui.ConsoleHandler;
import com.radioflex.ide.view.XMLView;

/**
 * @author alruiz@google.com (Alex Ruiz)
 */
class TerminalOutputListener implements IStreamListener {
	private static ArrayList<String> stringList = new ArrayList<String>();

	private static final int ANSISTATE_INITIAL = 0;
	private int ansiState = ANSISTATE_INITIAL;
	private static final int ANSISTATE_ESCAPE = 1;
	private static final int ANSISTATE_EXPECTING_PARAMETER_OR_COMMAND = 2;
	private static final int ANSISTATE_EXPECTING_OS_COMMAND = 3;
	private final StringBuffer ansiOsCommand = new StringBuffer(128);

	private XMLView xmlView = (XMLView) PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getActivePage()
			.findView(Constants.VIEW_XML);

	@Override
	public void streamAppended(String text, IStreamMonitor monitor) {
		processText(text);
	}

	private void processText(final String text) {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				StringBuffer buffer = new StringBuffer();
				char[] charText = text.toCharArray();
				int charTextLength = charText.length;
				int index = 0;

				if (charTextLength == 0) {
					return;
				}
				while (index < charTextLength) {
					char character = charText[index];
					index++;
					switch (ansiState) {
					case ANSISTATE_INITIAL:
						switch (character) {
						case '\u0000':
							break;
						case '\u0007':
							break;
						case '\b':
							break;
						case '\t':
							break;
						case '\n':
							break;
						case '\r':
							buffer.append('\r');
							break;
						case '\u001b':
							ansiState = ANSISTATE_ESCAPE; // Escape.
							break;
						default:
							buffer.append(character);
							break;
						}
						break;
					case ANSISTATE_ESCAPE:
						switch (character) {
						case '[':
							ansiState = ANSISTATE_EXPECTING_PARAMETER_OR_COMMAND;
							break;
						case ']':
							ansiState = ANSISTATE_EXPECTING_OS_COMMAND;
							ansiOsCommand.delete(0, ansiOsCommand.length());
							break;
						case '7':
							ansiState = ANSISTATE_INITIAL;
							break;
						case '8':
							ansiState = ANSISTATE_INITIAL;
							break;
						case 'c':
							ansiState = ANSISTATE_INITIAL;
							break;
						default:
							ansiState = ANSISTATE_INITIAL;
							break;
						}
						break;
					case ANSISTATE_EXPECTING_PARAMETER_OR_COMMAND:
						if (character == '@'
								|| (character >= 'A' && character <= 'Z')
								|| (character >= 'a' && character <= 'z')) {
							ansiState = ANSISTATE_INITIAL;
							// processAnsiCommandCharacter(character);
						} else {
						}
						break;
					case ANSISTATE_EXPECTING_OS_COMMAND:
						if (character == '\u0007') {
							ansiState = ANSISTATE_INITIAL;
						} else {
							ansiOsCommand.append(character);
						}
						break;
					default:
						ansiState = ANSISTATE_INITIAL;
						break;
					}
				}
				ConsoleHandler.info(buffer.toString());
				// System.out.print(buffer.toString());

				String s = buffer.toString();
				String regex = "Info: Total cycle = \\d+. Current result = \\d+.";
				regex = "[a-z]+ = \\d+";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(s);
				while (m.find()) {
					String str = m.group();
					System.out.println(str);
					
					Pattern p1 = Pattern.compile("[a-z]+");
					Matcher m1 = p1.matcher(str);
					while(m1.find()){
						String str1 = m1.group();
						Pattern p2 = Pattern.compile("\\d+");
						Matcher m2 = p2.matcher(str);
						while (m2.find()){
							String str2 = m2.group();
							xmlView.setKeyValue(str1,str2);
						}
					}
				}
				/*
				 * String[] str = s.split("\r"); for (int i = 0; i < str.length;
				 * i++) { stringList.add(str[i]); }
				 */

				/*
				 * char[] charArray = s.toCharArray(); int a =
				 * Integer.valueOf(charArray[29]); System.out.println(a);
				 */
			}
		});
	}

	private void processAnsiCommandCharacter(char character) {

	}

	public static ArrayList<String> getString() {
		return stringList;
	}

}
