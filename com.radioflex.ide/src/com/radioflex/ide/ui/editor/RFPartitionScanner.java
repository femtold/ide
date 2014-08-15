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

public class RFPartitionScanner extends RuleBasedPartitionScanner {
	
	public static final String PARTITION_STRING = "partition.string";
	public static final String PARTITION_COMMENT = "partition.comment";
	
	public RFPartitionScanner() {

		IToken string = new Token(PARTITION_STRING);
		IToken comment = new Token(PARTITION_COMMENT);

		ArrayList<IRule> rules = new ArrayList<IRule>();
		rules.add(new EndOfLineRule(";", comment));
		rules.add(new SingleLineRule("\"", "\"", string));
		rules.add(new SingleLineRule("'", "'", string));

		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
}
