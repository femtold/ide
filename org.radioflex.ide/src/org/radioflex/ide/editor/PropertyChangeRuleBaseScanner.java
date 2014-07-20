package org.radioflex.ide.editor;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.radioflex.ide.Activator;
import org.radioflex.ide.ui.TextAttributeConverter;

/**
 * Default RuleBaseScanner.
 * 
 * @author YMLiang
 * 
 */
public class PropertyChangeRuleBaseScanner extends RuleBasedScanner implements
		IPropertyChangeListener {

	/** Default Token for the text attributes * */
	private Token defToken;

	/** Editor need for refresh * */
	private ASMEditor editor;

	/** Key for preference store * */
	private String preferencesKey;

	/**
	 * Constructor of PropertyChangeRuleBaseScanner
	 * 
	 * @param editor
	 *            The given Editor.
	 * @param preferencesKey
	 *            The preference key to be listen on.
	 */
	public PropertyChangeRuleBaseScanner(final ASMEditor editor,
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

	/**
	 * Remove rule scanner from property listener.
	 */
	public void dispose() {
		Activator.getDefault().getPreferenceStore()
				.removePropertyChangeListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(preferencesKey)) {
			defToken.setData(TextAttributeConverter
					.preferenceDataToTextAttribute((String) event.getNewValue()));
		}

		editor.refreshSourceViewer();
	}
}
