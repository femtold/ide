package com.radioflex.ide.ui.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.radioflex.ide.ui.Activator;
import com.radioflex.ide.ui.Constants;


public class PreferencesInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {

		Display display = Display.getCurrent();
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String textAttribute = TextAttributeConverter
				.textAttributesToPreferenceData(new Color(display, 0, 0, 255),
						false, false);
		store.setDefault(Constants.PREFERENCES_TEXTCOLOR_STRING, textAttribute);

		textAttribute = TextAttributeConverter.textAttributesToPreferenceData(
				new Color(display, 0, 128, 0), false, true);
		store.setDefault(Constants.PREFERENCES_TEXTCOLOR_COMMENT, textAttribute);

		textAttribute = TextAttributeConverter.textAttributesToPreferenceData(
				new Color(display, 0, 0, 128), true, false);
		store.setDefault(Constants.PREFERENCES_TEXTCOLOR_INSTRUCTION,
				textAttribute);

		textAttribute = TextAttributeConverter.textAttributesToPreferenceData(
				new Color(display, 128, 64, 0), true, false);
		store.setDefault(Constants.PREFERENCES_TEXTCOLOR_SEGMENT, textAttribute);
		PreferenceManager pm = PlatformUI.getWorkbench().getPreferenceManager();
		IPreferenceNode[] arr = pm.getRootSubNodes();
		for (IPreferenceNode pn : arr) {
			System.out.println("Label:" + pn.getLabelText() + " ID:"
					+ pn.getId());
		}
	}

}
