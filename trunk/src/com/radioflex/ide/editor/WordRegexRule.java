package com.radioflex.ide.editor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;

public class WordRegexRule implements IRule {

	/** Internal setting for the un-initialized column constraint. */
	protected static final int UNDEFINED = -1;
	/** The column constraint. */
	protected int fColumn = UNDEFINED;
	/** The table of predefined words and token for this rule. */
	protected Map<String, IToken> fRegexps = new HashMap<String, IToken>();
	/** Buffer used for pattern detection. */
	private StringBuffer fBuffer = new StringBuffer();
	/** Tells whether this rule is case sensitive. default value is true. */
	private boolean fIgnoreCase = true;

	/** The word detector used by this rule. */
	protected IWordDetector fDetector;

	public WordRegexRule(IWordDetector detector) {
		Assert.isNotNull(detector);
		fDetector = detector;
	}

	public void addRegex(String wordregex, IToken token) {
		Assert.isNotNull(wordregex);
		Assert.isNotNull(token);
		// If case-insensitive, convert to lower case before adding to the map
		if (fIgnoreCase)
			wordregex = wordregex.toLowerCase();
		fRegexps.put(wordregex, token);
	}

	/**
	 * Sets a column constraint for this rule. If set, the rule's token will
	 * only be returned if the pattern is detected starting at the specified
	 * column. If the column is smaller then 0, the column constraint is
	 * considered removed.
	 *
	 * @param column
	 *            the column in which the pattern starts
	 */
	public void setColumnConstraint(int column) {
		if (column < 0)
			column = UNDEFINED;
		fColumn = column;
	}

	/*
	 * @see IRule#evaluate(ICharacterScanner)
	 */
	public IToken evaluate(ICharacterScanner scanner) {
		int c = scanner.read();
		if (c != ICharacterScanner.EOF && fDetector.isWordStart((char) c)) {
			if (fColumn == UNDEFINED || (fColumn == scanner.getColumn() - 1)) {

				fBuffer.setLength(0);
				do {
					fBuffer.append((char) c);
					c = scanner.read();
				} while (c != ICharacterScanner.EOF
						&& fDetector.isWordPart((char) c));
				scanner.unread();

				System.out.println("\"word\": <"+fBuffer+'>');
				
				
				String buffer = fBuffer.toString();
				if (fIgnoreCase)
					buffer = buffer.toLowerCase();
				
				IToken token = null;
				for (String regex : fRegexps.keySet()) {
					if (buffer.matches(regex)) {
						token = fRegexps.get(regex);
						break;
					}
				}

				if (token != null && !token.isUndefined())
					return token;
			}
		}

		scanner.unread();
		return Token.UNDEFINED;
	}

	/**
	 * Returns the characters in the buffer to the scanner.
	 *
	 * @param scanner
	 *            the scanner to be used
	 */
	protected void unreadBuffer(ICharacterScanner scanner) {
		for (int i = fBuffer.length() - 1; i >= 0; i--)
			scanner.unread();
	}
}
