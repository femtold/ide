package org.radioflex.ide;

import org.eclipse.osgi.util.NLS;

/**
 * Class which holds the message constants.
 * 
 * @author YMLiang
 * 
 */
public class Messages extends NLS{
	/**
	 * Name of the message bundle.
	 */
	private static final String BUNDLE_NAME = "org.radioflex.ide.messages";

	/**
	 * Name of the string text style.
	 */
	public static String TEXTCOLOR_STRING_NAME;

	/**
	 * Name of the comment text style.
	 */
	public static String TEXTCOLOR_COMMENT_NAME;

	/**
	 * Name of the instruction text style.
	 */
	public static String TEXTCOLOR_INSTRUCTION_NAME;

	/**
	 * Name of the segment text style.
	 */
	public static String TEXTCOLOR_SEGMENT_NAME;

	/**
	 * Syntax Highlight Title Text.
	 */
	public static String SYNTAXHIGHLIGHT_TITLE;

	/**
	 * Foreground-Color-Text.
	 */
	public static String FOREGROUNDCOLOR_TEXT;

	/**
	 * Color-Text.
	 */
	public static String COLOR_TEXT;

	/**
	 * Bold-Text.
	 */
	public static String BOLD_TEXT;

	/**
	 * Italic-Text.
	 */
	public static String ITALIC_TEXT;

	/**
	 * The title for the new ASM project wizard.
	 */
	public static String WIZARD_NEW_PROJECT_TITLE;

	/**
	 * The title for the first page in the new ASM projet wizard.
	 */
	public static String WIZARD_NEW_PROJECT_PAGE1_TITLE;

	/**
	 * The title for the new ASM file wizard.
	 */
	public static String WIZARD_NEW_FILE_TITLE;

	/**
	 * The title for the first page in the new ASM file wizard.
	 */
	public static String WIZARD_NEW_FILE_PAGE1_TITLE;

	/**
	 * Error Message for an invalid file.
	 */
	public static String WIZARD_NEW_FILE_PAGE1_INVALID_FILE;

	/**
	 * The description for the first page in the new ASM project wizard.
	 */
	public static String WIZARD_NEW_PROJECT_DESCRIPTION;
	
	/**
	   * Text for error for loading ASM instruction set.
	   */
	  public static String LOAD_ASMISET_ERROR;

	// Initialize the constants.
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

}
