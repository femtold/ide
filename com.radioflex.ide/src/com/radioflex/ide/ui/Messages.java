package com.radioflex.ide.ui;

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
	private static final String BUNDLE_NAME = "com.radioflex.ide.ui.messages";

	
	public static String WORKBENCH_WINDOW_TITLE;
	
	
	public static String BADLOCATION_ERROR;

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
	 * Name of the macros text style.
	 */
	public static String TEXTCOLOR_MACROS_NAME;
	
	/**
	 * Name of the derivative text style.
	 */
	public static String TEXTCOLOR_DERIVATIVE_NAME;
	
	/**
	 * Name of the register text style.
	 */
	public static String TEXTCOLOR_REGISTER_NAME;
	

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
	
	/**
	 * The title for the preference page Keyword.
	 */
	public static String KEYWORD_TITLE;
	
	/**
	 * The discription for the preference page Keyword.
	 */
	public static String KEYWORD_DISCRIPTION;
	
	/**
	 * instruction-text
	 */
	public static String INSTRUCTION_TEXT;
	
	/**
	 * Text for edit button
	 */
	public static String EDIT_BUTTON_TEXT;
	
	/**
	 * Text for add button
	 */
	public static String ADD_BUTTON_TEXT;
	
	/**
	 * Text for remove button
	 */
	public static String REMOVE_BUTTON_TEXT;
	
	/**
	 * Text for uppercase
	 */
	public static String UPPERCASE_TEXT;
	
	/**
	 * Text for lowercase
	 */
	public static String LOWERCASE_TEXT;
	
	/**
	 * Text for description
	 */
	public static String DESCRIPTION_TEXT;
	
	/**
	 * Label for keyword text
	 */
	public static String KEYWORD_LABEL_TEXT;
	// Initialize the constants.
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

}
