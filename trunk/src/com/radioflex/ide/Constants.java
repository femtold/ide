package com.radioflex.ide;

import org.eclipse.jface.resource.ImageDescriptor;

import com.radioflex.ide.editor.ASMEditor;
import com.radioflex.ide.preferences.PreferenceHighlighting;
import com.radioflex.ide.preferences.PreferenceKeysChange;
import com.radioflex.ide.project.ASMBuilder;
import com.radioflex.ide.project.ASMNature;

/** Summary of all constants */
public class Constants {

	// ------------------ID Constants------------------
	/** The plugin ID. */
	public static final String PLUGIN_ID = Activator.PLUGIN_ID;
	/** The nature ID. */
	public static final String RADIOFLEX_NATURE_ID = ASMNature.RADIOFLEX_NATURE_ID;
	/** The builder ID. */
	public static final String RADIOFLEX_BUILDER_ID = ASMBuilder.RADIOFLEX_BUILDER_ID;
	/** The project explorer view ID. */
	public static final String VIEW_PROJECT_EXPLORER = "com.radioflex.ide.view.project";
	/** The edit mode perspective ID. */
	public static final String PERSPECTIVE_EDIT_ID = PerspectiveEdit.PERSPECTIVE_EDIT_ID;
	/** The debug mode perspective ID. */
	public static final String PERSPECTIVE_DEBUG_ID = PerspectiveDebug.PERSPECTIVE_DEBUG_ID;
	/** The editor ID. */
	public static final String RADIOFLEX_EDITOR_ID = ASMEditor.RADIOFLEX_EDITOR_ID;
	/** The xml view ID. */
	public static final String VIEW_XML = "com.radioflex.ide.viewxml";

	// ------------------Editor Constants------------------
	public static final String PARTITION_STRING = "partition.string";
	public static final String PARTITION_COMMENT = "partition.comment";

	// ------------------Build Constants------------------
	public static final String BUILD_DIRECTORY_SIM = "simulator";
	public static final String BUILD_DIRECTORY_FPGA = "fpga";

	// ------------------Preferences Constants------------------
	// syntax highlighting(root page):
	public static final String PREFERENCES_TEXTCOLOR_COMMENT = PreferenceHighlighting.PREF_TEXTATTR_COMMENT;
	public static final String PREFERENCES_TEXTCOLOR_STRING = PreferenceHighlighting.PREF_TEXTATTR_STRING;
	public static final String PREFERENCES_TEXTCOLOR_INSTRUCTION = PreferenceHighlighting.PREF_TEXTATTR_INSTRUCTION;
	public static final String PREFERENCES_TEXTCOLOR_SEGMENT = PreferenceHighlighting.PREF_TEXTATTR_SEGMENT;
	public static final String PREFERENCES_TEXTCOLOR_MACROS = PreferenceHighlighting.PREF_TEXTATTR_MACRO;
	public static final String PREFERENCES_TEXTCOLOR_DERIVATIVES = PreferenceHighlighting.PREF_TEXTATTR_DERIVATIVE;
	public static final String PREFERENCES_TEXTCOLOR_REGISTER = PreferenceHighlighting.PREF_TEXTATTR_REGISTER;
	public static final String PREFERENCES_TEXTCOLOR_NUMERIC = PreferenceHighlighting.PREF_TEXTATTR_NUMERIC;
	// changing keywords(sub 1):
	public static final String PREFERENCES_KEYWORD_CHANGE = PreferenceKeysChange.PREFERENCES_KEYWORD_CHANGE;
	// build&complier(sub 2):
	public static final String PREFERENCES_COMPILER_NAME = "preferences.compiler.name";
	public static final String PREFERENCES_COMPILER_PARAMS_1 = "preferences.compiler.params1";
	public static final String PREFERENCES_COMPILER_PARAMS_2 = "preferences.compiler.params2";

	// ------------------Other------------------
	public static final ImageDescriptor WIZARD_NEW = Activator
			.getImageDescriptor("icons/alt_winsow16.gif");

}
