package com.radioflex.ide.ui.editor;

import java.util.ArrayList;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

public class RCodeScanner extends RuleBasedScanner {
	private static String[] rfmacros = { "#include", "#define", "#if", "#else",
			"#end" };
	private static String[] rfregister = { "r", "a", "spar", "cpar", "cr" };

	private static String[] rfderivatives = { ".org", ".align", ".skip",
			".code", ".text", ".data", ".w", ".dw" };

	private static String[] rfinstructions = { "abs", "add", "addc", "addi",
			"sub", "subc", "div", "cmp", "cpi", "and", "or", "xor", "not",
			"ash", "ashi", "lsh", "lshi", "ror", "rol", "clz", "nop", "break",
			"jmp", "call", "ret", "idle", "wait", "loop", "loopi", "move",
			"setlo", "sethi", "load", "store", "in", "inr", "out", "outr" };

	public RCodeScanner(RColorProvider cp) {
		super();

		IToken macros = new Token(new TextAttribute(
				cp.getColor(RColorProvider.MACROS)));
		IToken derivative = new Token(new TextAttribute(
				cp.getColor(RColorProvider.DERIVATIVE)));
		IToken register = new Token(new TextAttribute(
				cp.getColor(RColorProvider.REGISTER)));
		IToken instructions = new Token(new TextAttribute(
				cp.getColor(RColorProvider.INSTRUCTIONS)));
		IToken string = new Token(new TextAttribute(
				cp.getColor(RColorProvider.STRING)));
		IToken comment = new Token(new TextAttribute(
				cp.getColor(RColorProvider.SINGLE_LINE_COMMENT)));
		IToken other = new Token(new TextAttribute(
				cp.getColor(RColorProvider.DEFAULT)));

		ArrayList<IRule> rules = new ArrayList<IRule>();

		rules.add(new EndOfLineRule(";", comment));
		rules.add(new SingleLineRule("\"", "\"", string));
		rules.add(new SingleLineRule("'", "'", string));
		rules.add(new WhitespaceRule(new RWhitespaceDetector()));

		WordRule wordrule = new WordRule(new RWordDetector(), other);
		for (int i = 0; i < rfmacros.length; i++) {
			wordrule.addWord(rfmacros[i], macros);
			wordrule.addWord(rfmacros[i].toUpperCase(), macros);
		}
		for (int i = 0; i < rfregister.length; i++) {
			wordrule.addWord(rfregister[i], register);
			wordrule.addWord(rfregister[i].toUpperCase(), register);
		}
		for (int i = 0; i < rfderivatives.length; i++) {
			wordrule.addWord(rfderivatives[i], derivative);
			wordrule.addWord(rfderivatives[i].toUpperCase(), derivative);
		}
		for (int i = 0; i < rfinstructions.length; i++) {
			wordrule.addWord(rfinstructions[i], instructions);
			wordrule.addWord(rfinstructions[i].toUpperCase(), instructions);
		}
		rules.add(wordrule);

		IRule[] results = new IRule[rules.size()];
		rules.toArray(results);
		setRules(results);
	}
}
