package com.radioflex.ide.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.radioflex.ide.Activator;

/** Initialize the preference values. */
public class PreferencesInitializer extends AbstractPreferenceInitializer {

	/** Default Syntax Highlighting TextAttributes */
	// (r,g,b), bold, italic
	public static final String PREF_TEXTATTR_MACRO_DEFAULT = TextAttributeConverter
			.textAttributesToPreferenceData(new Color(Display.getCurrent(),
					50, 120, 0), true, false);
	public static final String PREF_TEXTATTR_INSTRUCTION_DEFAULT = TextAttributeConverter
			.textAttributesToPreferenceData(new Color(Display.getCurrent(), 125,
					48, 80), false, false);
	public static final String PREF_TEXTATTR_SEGMENT_DEFAULT = TextAttributeConverter
			.textAttributesToPreferenceData(new Color(Display.getCurrent(),
					140, 105, 20), true, false);
	public static final String PREF_TEXTATTR_DERIVATIVE_DEFAULT = TextAttributeConverter
			.textAttributesToPreferenceData(new Color(Display.getCurrent(),
					170, 50, 90), false, true);
	public static final String PREF_TEXTATTR_STRING_DEFAULT = TextAttributeConverter
			.textAttributesToPreferenceData(new Color(Display.getCurrent(),
					255, 0, 155), false, false);
	public static final String PREF_TEXTATTR_COMMENT_DEFAULT = TextAttributeConverter
			.textAttributesToPreferenceData(new Color(Display.getCurrent(),
					10, 80, 10), false, true);
	public static final String PREF_TEXTATTR_REGISTER_DEFAULT = TextAttributeConverter
			.textAttributesToPreferenceData(new Color(Display.getCurrent(),
					89, 85, 55), false, false);
	public static final String PREF_TEXTATTR_NUMERIC_DEFAULT = TextAttributeConverter
			.textAttributesToPreferenceData(new Color(Display.getCurrent(),
					0, 0, 128), false, false);

	@Override
	public void initializeDefaultPreferences() {

		IEclipsePreferences node = DefaultScope.INSTANCE
				.getNode(Activator.PLUGIN_ID);
		node.put(PreferenceHighlighting.PREF_TEXTATTR_MACRO,
				PREF_TEXTATTR_MACRO_DEFAULT);
		node.put(PreferenceHighlighting.PREF_TEXTATTR_INSTRUCTION,
				PREF_TEXTATTR_INSTRUCTION_DEFAULT);
		node.put(PreferenceHighlighting.PREF_TEXTATTR_SEGMENT,
				PREF_TEXTATTR_SEGMENT_DEFAULT);
		node.put(PreferenceHighlighting.PREF_TEXTATTR_DERIVATIVE,
				PREF_TEXTATTR_DERIVATIVE_DEFAULT);
		node.put(PreferenceHighlighting.PREF_TEXTATTR_STRING,
				PREF_TEXTATTR_STRING_DEFAULT);
		node.put(PreferenceHighlighting.PREF_TEXTATTR_COMMENT,
				PREF_TEXTATTR_COMMENT_DEFAULT);
		node.put(PreferenceHighlighting.PREF_TEXTATTR_REGISTER,
				PREF_TEXTATTR_REGISTER_DEFAULT);
		node.put(PreferenceHighlighting.PREF_TEXTATTR_NUMERIC,
				PREF_TEXTATTR_NUMERIC_DEFAULT);

		node.putInt(PreferenceKeysChange.PREFERENCES_KEYWORD_CHANGE, 1);

	}
}
