package com.radioflex.ide.ui.editor;

import java.util.ArrayList;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

import com.radioflex.ide.ui.Constants;

public class REPartitionScanner extends RuleBasedPartitionScanner {
	
	public REPartitionScanner() {

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
