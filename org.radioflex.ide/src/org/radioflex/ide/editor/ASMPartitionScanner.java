package org.radioflex.ide.editor;

import java.util.ArrayList;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.radioflex.ide.Constants;


/**
 * A partition scanner for the ASM editor.
 * 
 * @author YMLiang
 * 
 */
public class ASMPartitionScanner extends RuleBasedPartitionScanner {

	/**
	 * Creates the partitioner and set up the appropriate rules
	 */
	public ASMPartitionScanner() {

		IToken string = new Token(Constants.PARTITION_STRING);
		IToken comment = new Token(Constants.PARTITION_COMMENT);

		ArrayList<IRule> rules = new ArrayList<IRule>();

		// Add rule for single line comments
		rules.add(new EndOfLineRule(";", comment));

		// Add rule for strings and character constants
		rules.add(new SingleLineRule("\"", "\"", string));
		rules.add(new SingleLineRule("'", "'", string));

		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
}
