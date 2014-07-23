package com.radioflex.ide.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import com.radioflex.ide.ui.Constants;


public class RESourceViewerConfiguration extends SourceViewerConfiguration {
	private RadioflexEditor editor;
	private RCodeScanner recodescanner = null;
	private PropertyChangeRuleBaseScanner[] scanner = new PropertyChangeRuleBaseScanner[2];
	
	public RESourceViewerConfiguration(RadioflexEditor editor) {
		this.editor = editor;
	}

	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		recodescanner = new RCodeScanner(editor);
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(recodescanner);
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		scanner[0] = new PropertyChangeRuleBaseScanner(editor,
				Constants.PREFERENCES_TEXTCOLOR_COMMENT);
		dr = new DefaultDamagerRepairer(scanner[0]);
		reconciler.setDamager(dr, Constants.PARTITION_COMMENT);
		reconciler.setRepairer(dr, Constants.PARTITION_COMMENT);

		scanner[1] = new PropertyChangeRuleBaseScanner(editor,
				Constants.PREFERENCES_TEXTCOLOR_STRING);
		dr = new DefaultDamagerRepairer(scanner[1]);
		reconciler.setDamager(dr, Constants.PARTITION_STRING);
		reconciler.setRepairer(dr, Constants.PARTITION_STRING);

		return reconciler;
	}

}
