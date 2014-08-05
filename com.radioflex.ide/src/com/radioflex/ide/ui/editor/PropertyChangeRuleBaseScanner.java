package com.radioflex.ide.ui.editor;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.radioflex.ide.ui.Activator;
import com.radioflex.ide.ui.preference.TextAttributeConverter;


public class PropertyChangeRuleBaseScanner extends RuleBasedScanner implements IPropertyChangeListener{

	private Token defToken;
	private RadioFlexEditor editor;
	private String preferencesKey;


	public PropertyChangeRuleBaseScanner(final RadioFlexEditor editor,
			final String preferencesKey) {
		this.editor = editor;
		this.preferencesKey = preferencesKey;

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		defToken = new Token(
				TextAttributeConverter.preferenceDataToTextAttribute(store
						.getString(preferencesKey)));

		super.setDefaultReturnToken(defToken);

		Activator.getDefault().getPreferenceStore()
				.addPropertyChangeListener(this);
	}

	public void dispose() {
		Activator.getDefault().getPreferenceStore()
				.removePropertyChangeListener(this);
	}


	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(preferencesKey)) {
			defToken.setData(TextAttributeConverter
					.preferenceDataToTextAttribute((String) event.getNewValue()));
		}

		editor.refreshSourceViewer();
	}
}
