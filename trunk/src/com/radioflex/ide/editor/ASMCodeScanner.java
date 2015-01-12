package com.radioflex.ide.editor;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Device;

import com.radioflex.ide.Activator;
import com.radioflex.ide.Constants;
import com.radioflex.ide.preferences.TextAttributeConverter;

/**
 * RuleBasedScanner for the ASMEditor
 *
 *
 */
public class ASMCodeScanner extends RuleBasedScanner implements
		IPropertyChangeListener {

	private Token instructionToken;
	private Token segmentToken;
	private Token macrosToken;
	private Token derivativeToken;
	private Token registerToken;
	private Token integerToken; // testing
	private ArrayList<IRule> rules;
	private ASMEditor editor;

	/**
	 * The constructor.
	 * 
	 * @param editor
	 *            The underlying ASMEditor for the CodeScanner.
	 */
	public ASMCodeScanner(final ASMEditor editor) {
		this.editor = editor;

		rules = new ArrayList<IRule>();
		createTokens(editor.getSite().getShell().getDisplay());

		Activator.getDefault().getPreferenceStore()
				.addPropertyChangeListener(this);

		addWordRules();
		addIntegerRules();

		setRules(rules.toArray(new IRule[] {}));
	}

	/**
	 * Disposes the PropertyChangeListener from the PreferenceStore.
	 */
	public void dispose() {
		Activator.getDefault().getPreferenceStore()
				.removePropertyChangeListener(this);
	}

	/**
	 * Create all Tokens.
	 * 
	 * @param device
	 *            The device is needed for the color of the Tokens.
	 */
	private void createTokens(Device device) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		instructionToken = new Token(
				TextAttributeConverter.preferenceDataToTextAttribute(store
						.getString(Constants.PREFERENCES_TEXTCOLOR_INSTRUCTION)));
		segmentToken = new Token(
				TextAttributeConverter.preferenceDataToTextAttribute(store
						.getString(Constants.PREFERENCES_TEXTCOLOR_SEGMENT)));
		macrosToken = new Token(
				TextAttributeConverter.preferenceDataToTextAttribute(store
						.getString(Constants.PREFERENCES_TEXTCOLOR_MACROS)));
		derivativeToken = new Token(
				TextAttributeConverter.preferenceDataToTextAttribute(store
						.getString(Constants.PREFERENCES_TEXTCOLOR_DERIVATIVES)));
		registerToken = new Token(
				TextAttributeConverter.preferenceDataToTextAttribute(store
						.getString(Constants.PREFERENCES_TEXTCOLOR_REGISTER)));
		//testing
		integerToken = new Token(
				TextAttributeConverter.preferenceDataToTextAttribute(store
						.getString(Constants.PREFERENCES_TEXTCOLOR_NUMERIC)));

	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(
				Constants.PREFERENCES_TEXTCOLOR_INSTRUCTION))
			instructionToken
					.setData(TextAttributeConverter
							.preferenceDataToTextAttribute((String) event
									.getNewValue()));
		else if (event.getProperty().equals(
				Constants.PREFERENCES_TEXTCOLOR_SEGMENT))
			segmentToken
					.setData(TextAttributeConverter
							.preferenceDataToTextAttribute((String) event
									.getNewValue()));
		else if (event.getProperty().equals(
				Constants.PREFERENCES_TEXTCOLOR_MACROS))
			macrosToken
					.setData(TextAttributeConverter
							.preferenceDataToTextAttribute((String) event
									.getNewValue()));
		else if (event.getProperty().equals(
				Constants.PREFERENCES_TEXTCOLOR_DERIVATIVES))
			derivativeToken
					.setData(TextAttributeConverter
							.preferenceDataToTextAttribute((String) event
									.getNewValue()));
		else if (event.getProperty().equals(
				Constants.PREFERENCES_TEXTCOLOR_REGISTER))
			registerToken
					.setData(TextAttributeConverter
							.preferenceDataToTextAttribute((String) event
									.getNewValue()));
		else if (event.getProperty().equals(
				Constants.PREFERENCES_KEYWORD_CHANGE)) {
			rules.removeAll(rules);
			addWordRules();
			setRules(rules.toArray(new IRule[] {}));
			editor.refreshSourceViewerConfiguration();
		}
		editor.refreshSourceViewer();

	}

	/**
	 * Add the word rule for CodeScanner.
	 */
	private void addWordRules() {
		WordRuleCaseInsensitive wordRule = new WordRuleCaseInsensitive();
		HashMap<String, String> instructions = ASMInstructionSet
				.getInstructionMap();
		if (instructions != null)
			for (String instruction : instructions.keySet())
				wordRule.addWord(instruction, instructionToken);
		rules.add(wordRule);

		wordRule = new WordRuleCaseInsensitive();
		HashMap<String, String> segments = ASMInstructionSet.getSegmentMap();
		if (segments != null)
			for (String segment : segments.keySet())
				wordRule.addWord(segment, segmentToken);
		rules.add(wordRule);

		wordRule = new WordRuleCaseInsensitive();
		HashMap<String, String> macroses = ASMInstructionSet.getMacrosMap();
		if (macroses != null)
			for (String macros : macroses.keySet())
				wordRule.addWord(macros, macrosToken);
		rules.add(wordRule);

		wordRule = new WordRuleCaseInsensitive();
		HashMap<String, String> derivatives = ASMInstructionSet
				.getDerivativeMap();
		if (instructions != null)
			for (String derivative : derivatives.keySet())
				wordRule.addWord(derivative, derivativeToken);
		rules.add(wordRule);

		wordRule = new WordRuleCaseInsensitive();
		HashMap<String, String> registers = ASMInstructionSet.getRegisterMap();
		if (instructions != null)
			for (String register : registers.keySet())
				wordRule.addWord(register, registerToken);
		rules.add(wordRule);
	}

	private void addIntegerRules() {
		IWordDetector wordDetector = new IWordDetector() {
			@Override
			public boolean isWordStart(char c) {
				// precluding impossible cases here for integers. testing.
				return c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c >= 'A'
						&& c <= 'Z';
			}

			@Override
			public boolean isWordPart(char c) {

				return c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c >= 'A'
						&& c <= 'Z';
			}
		};
		WordRegexRule integerRule = new WordRegexRule(wordDetector);
		integerRule.addRegex("([1-9]?[0-9]*|0)", integerToken);// decimal
		integerRule.addRegex("(0[0-9]+)", integerToken);// Octal
		integerRule.addRegex("([a-f0-9]+h)", integerToken);// Hexadecimal

		rules.add(integerRule);
	}
}
