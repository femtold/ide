package com.radioflex.ide.ui.editor;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class RWhitespaceDetector implements IWhitespaceDetector{

	@Override
	public boolean isWhitespace(char c) {
		return Character.isWhitespace(c);
	}

}
