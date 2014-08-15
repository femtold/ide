package com.radioflex.ide.ui.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.radioflex.ide.ui.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public static final String PREF_TEXTATTR_MACRO = "preferences.textattribute.macro"; //$NON-NLS-1$
	public static final String PREF_TEXTATTR_INSTRUCTION = "preferences.textattribute.instruction"; //$NON-NLS-1$
	public static final String PREF_TEXTATTR_SECTION = "preferences.textattribute.section"; //$NON-NLS-1$
	public static final String PREF_TEXTATTR_DERIVATIVE = "preferences.textattribute.derivative"; //$NON-NLS-1$
	public static final String PREF_TEXTATTR_REGISTER = "preferences.textattribute.register"; //$NON-NLS-1$
	public static final String PREF_TEXTATTR_STRING = "preferences.textattribute.string"; //$NON-NLS-1$
	public static final String PREF_TEXTATTR_COMMENT = "preferences.textattribute.comment"; //$NON-NLS-1$

	private static int index;

	@Override
	public void initializeDefaultPreferences() {

		Display display = Display.getCurrent();
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String textAttribute = null;

		textAttribute = TextAttributeConverter.textAttributesToPreferenceData(
				new Color(display, 0, 199, 140), true, false);
		store.setDefault(PREF_TEXTATTR_MACRO, textAttribute);

		textAttribute = TextAttributeConverter.textAttributesToPreferenceData(
				new Color(display, 128, 64, 0), true, false);
		store.setDefault(PREF_TEXTATTR_SECTION, textAttribute);

		textAttribute = TextAttributeConverter.textAttributesToPreferenceData(
				new Color(display, 0, 0, 255), true, false);
		store.setDefault(PREF_TEXTATTR_INSTRUCTION, textAttribute);
	
		textAttribute = TextAttributeConverter.textAttributesToPreferenceData(
				new Color(display, 30, 150, 255), true, false);
		store.setDefault(PREF_TEXTATTR_DERIVATIVE, textAttribute);

		textAttribute = TextAttributeConverter.textAttributesToPreferenceData(
				new Color(display, 20, 89, 200), true, false);
		store.setDefault(PREF_TEXTATTR_REGISTER, textAttribute);

		textAttribute = TextAttributeConverter.textAttributesToPreferenceData(
				new Color(display, 255, 0, 255), false, false);
		store.setDefault(PREF_TEXTATTR_STRING, textAttribute);

		textAttribute = TextAttributeConverter.textAttributesToPreferenceData(
				new Color(display, 35, 140, 35), false, true);
		store.setDefault(PREF_TEXTATTR_COMMENT, textAttribute);

		index = 1;
		store.setDefault(PreferenceChangeKeywords.PERFERENCE_KEYWORD_CHANGE,
				index);
	}
	
	public static void printPreferenceNames(){
		PreferenceManager pm = PlatformUI.getWorkbench().getPreferenceManager();
		IPreferenceNode[] arr = pm.getRootSubNodes();
		for (IPreferenceNode pn : arr) {
			System.out.println("Label:" + pn.getLabelText() + " ID:"
					+ pn.getId());
		}
	}

	public static int gethasChange() {
		return index;
	}

}
