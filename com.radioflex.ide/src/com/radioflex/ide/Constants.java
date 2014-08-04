package com.radioflex.ide;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.RGB;

/**
 * Class which holds several constants.
 * 
 * @author YMLiang
 * 
 */
public class Constants {

	// Important IDs
	/**
	 * The Plug-in ID.
	 */
	public static final String PLUGIN_ID = "org.radioflex.ide";

	/**
	 * The IDE perspective ID.
	 */
	public static final String IDE_PERSPECTIVE_ID = PLUGIN_ID
			+ ".perspective.rcp";

	/**
	 * The ASM page ID.
	 */
	public static final String PREFERENCES_ASM_ID = "org.radioflex.ide.page.asm";

	/**
	 * The Nature ID.
	 */
	public static final String NATURE_ID = "org.radioflex.ide.nature";

	/**
	 * The Build ID.
	 */
	public static final String BUILDER_ID = "org.radioflex.ide.builder";

	/**
	 * Preference key for string text style preference keys.
	 */
	public static final String PREFERENCES_TEXTCOLOR_STRING = "preferences.textcolor.string"; //$NON-NLS-1$

	/**
	 * Preference key for comment text style preference keys.
	 */
	public static final String PREFERENCES_TEXTCOLOR_COMMENT = "preferences.textcolor.comment"; //$NON-NLS-1$

	/**
	 * Preference key for instruction text style preference keys.
	 */
	public static final String PREFERENCES_TEXTCOLOR_INSTRUCTION = "preferences.textcolor.instruction"; //$NON-NLS-1$

	/**
	 * Preference key for segment text style preference keys.
	 */
	public static final String PREFERENCES_TEXTCOLOR_SEGMENT = "preferences.textcolor.segment"; //$NON-NLS-1$

	/**
	 * Perference key for macros text style preferense keys.
	 */
	public static final String PREFERENCES_TEXTCOLOR_MACROS = "perferences.textcolor.macros"; //$NON-NLS-1$

	/**
	 * Perference key for derivatives text style preferense keys.
	 */
	public static final String PREFERENCES_TEXTCOLOR_DERIVATIVES = "perferences.textcolor.derivatives"; //$NON-NLS-1$

	/**
	 * Perference key for register text style preferense keys.
	 */
	public static final String PREFERENCES_TEXTCOLOR_REGISTER = "perferences.textcolor.register"; //$NON-NLS-1$

	/**
	 * Perference key for keywords have changed.
	 */
	public static final String PERFERENCE_KEYWORD_CHANGE = "perferences.keyword.change"; //$NON-NLS-1$

	/**
	 * Name for the string partition.
	 */
	public static final String PARTITION_STRING = "partion.string";

	/**
	 * Name for the single line comment partition.
	 */
	public static final String PARTITION_COMMENT = "partion.comment";

	/**
	 * Icon for the new wizard.
	 */
	public static final ImageDescriptor WIZARD_NEW = Activator
			.getImageDescriptor("icons/newWizard.gif");

	
	//Added by YidanWang:
	//TreeModel.java:
//	public static final int TREEMODEL_TYPE_NULL = 0;
//	public static final int TREEMODEL_TYPE_ROOT_PROCEDURE = 1;
//	public static final int TREEMODEL_TYPE_ROOT_MACRO = 2;
//	public static final int TREEMODEL_TYPE_ROOT_LABEL = 3;
//	public static final int TREEMODEL_TYPE_ROOT_SEGMENT = 4;
//	public static final int TREEMODEL_TYPE_PROCEDURE = 5;
//	public static final int TREEMODEL_TYPE_MACRO = 6;
//	public static final int TREEMODEL_TYPE_LABEL = 7;
//	public static final int TREEMODEL_TYPE_SEGMENT = 8;

}
