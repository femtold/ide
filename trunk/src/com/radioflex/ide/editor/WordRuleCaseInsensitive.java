package com.radioflex.ide.editor;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

/**
 * A special WordRule, which ignores the case of a given word.
 * 
 *
 */
public class WordRuleCaseInsensitive extends WordRule {

	/**
	 * Buffer used for pattern detection.
	 */
	private StringBuffer fBuffer = new StringBuffer();

	/**
	 * The constructor.
	 */
	public WordRuleCaseInsensitive() {
		this(Token.UNDEFINED);
	}

	/**
	 * Create a rule which, If no token has been associated, the specified
	 * default token will be returned.
	 * 
	 * @param defaultToken
	 *            the default token to be returned on success if nothing else is
	 *            specified, may not be null.
	 */
	public WordRuleCaseInsensitive(IToken defaultToken) {
		super(new IWordDetector() {
			@Override
			public boolean isWordStart(char c) {
				return false;
			}

			@Override
			public boolean isWordPart(char c) {
				return false;
			}
		}, defaultToken);
		fDetector = new MyWordDecetor();
	}

	/**
	 * {@inheritDoc}}
	 */
	public IToken evaluate(ICharacterScanner scanner) {
		int c = 0;

		if (scanner.getColumn() > 0) {
			scanner.unread();
			c = scanner.read();
			if (!Character.isWhitespace((char) c) && (char) c != ','
					&& (char) c != '#')
				return fDefaultToken;
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
				if (token != null)
					return token;
				if (fDefaultToken.isUndefined())
					unreadBuffer(scanner);
				return fDefaultToken;
			}
		}
		scanner.unread();
		return Token.UNDEFINED;
	}

	/**
	 * {@inheritDoc}}
	 */
	public void addWord(String word, IToken token) {
		super.addWord(word.toLowerCase(), token);
	}

	/**
	 * Returns the characters in the buffer to the scanner.
	 * @param scanner the scanner to be used.
	 */
	protected void unreadBuffer(ICharacterScanner scanner) {
		for (int i = fBuffer.length() - 1; i > -1; i--)
			scanner.unread();
	}

	/**
	 * A wordDetector, which recognizes all type able characters.
	 *
	 */
	private class MyWordDecetor implements IWordDetector {

		@Override
		public boolean isWordStart(char c) {
			return c >= ' ' && c <= '~';
		}

		@Override
		public boolean isWordPart(char c) {
			return isTrue(c);
		}

	}

	private boolean isTrue(char c) {
		if (c == '.' || (c >= 48 && c <= 57) || (c >= 65 && c <= 90)
				|| (c >= 97 && c <= 122))
			return true;
		else
			return false;
	}

}
