package com.radioflex.ide.ui.editor;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

public class WordRuleCaseInsensitive extends WordRule {
	private StringBuffer fBuffer = new StringBuffer();

	public WordRuleCaseInsensitive() {
		this(Token.UNDEFINED);
	}

	/**
	 * Creates a rule which. If no token has been associated, the specified
	 * default token will be returned.
	 * 
	 */
	public WordRuleCaseInsensitive(IToken defaultToken) {
		super(new IWordDetector() { // A dummy. WordDetector will be
					// replaced a
					// few rows below.
					public boolean isWordPart(char c) {
						return false;
					}

					public boolean isWordStart(char c) {
						return false;
					}
				}, defaultToken);

		fDetector = new MyWordDetector();
	}

	public IToken evaluate(ICharacterScanner scanner) {
		int c = 0;

		if (scanner.getColumn() > 0) {
			scanner.unread();
			c = scanner.read();
			if (!Character.isWhitespace((char) c)) {
				return fDefaultToken;
			}
		}

		c = scanner.read();
		c = Character.toLowerCase((char) c);
		if (fDetector.isWordStart((char) c)) {
			if (fColumn == UNDEFINED || (fColumn == scanner.getColumn() - 1)) {

				fBuffer.setLength(0);
				do {
					fBuffer.append((char) c);
					c = scanner.read();
					c = Character.toLowerCase((char) c);
				} while (c != ICharacterScanner.EOF
						&& fDetector.isWordPart((char) c));
				scanner.unread();

				IToken token = (IToken) fWords.get(fBuffer.toString());
				if (token != null) {
					return token;
				}

				if (fDefaultToken.isUndefined()) {
					unreadBuffer(scanner);
				}

				return fDefaultToken;
			}
		}

		scanner.unread();
		return Token.UNDEFINED;
	}

	public void addWord(String word, IToken token) {
		super.addWord(word.toLowerCase(), token);
	}

	protected void unreadBuffer(ICharacterScanner scanner) {
		for (int i = fBuffer.length() - 1; i > -1; i--) {
			scanner.unread();
		}
	}

	private class MyWordDetector implements IWordDetector {
		public boolean isWordStart(char c) {
			return ((c > ' ') && (c <= '~'));
		}

		public boolean isWordPart(char c) {
			return ((c > ' ') && (c <= '~'));
		}
	}
}
