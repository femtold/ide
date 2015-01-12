package com.radioflex.ide.editor;

import java.util.ArrayList;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

import com.radioflex.ide.Constants;

/**
 * A partition scanner for the ASM editor.
 *
 */
public class ASMPartitonScanner extends RuleBasedPartitionScanner {
	
	/**
	 * Partition scanner for ASMEditor.
	 */
	public ASMPartitonScanner() {
		IToken string = new Token(Constants.PARTITION_STRING);
		IToken comment = new Token(Constants.PARTITION_COMMENT);

		ArrayList<IRule> rules = new ArrayList<IRule>();

		rules.add(new EndOfLineRule(";", comment));

		rules.add(new SingleLineRule("\"", "\"", string));
		rules.add(new SingleLineRule("'", "'", string));

		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
}
