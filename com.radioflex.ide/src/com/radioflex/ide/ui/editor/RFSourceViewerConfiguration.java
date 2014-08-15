package com.radioflex.ide.ui.editor;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import com.radioflex.ide.ui.preference.*;
import com.radioflex.ide.ui.*;

public class RFSourceViewerConfiguration extends SourceViewerConfiguration {
	private RadioFlexEditor editor;
	private RFCodeScanner recodescanner = null;
	private PropertyChangeRuleBaseScanner[] scanner = new PropertyChangeRuleBaseScanner[2];

	public RFSourceViewerConfiguration(RadioFlexEditor editor) {
		this.editor = editor;
	}

	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		recodescanner = new RFCodeScanner(editor);
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(recodescanner);
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		scanner[0] = new PropertyChangeRuleBaseScanner(editor,
				PreferenceInitializer.PREF_TEXTATTR_COMMENT);
		dr = new DefaultDamagerRepairer(scanner[0]);
		reconciler.setDamager(dr, RFPartitionScanner.PARTITION_COMMENT);
		reconciler.setRepairer(dr, RFPartitionScanner.PARTITION_COMMENT);

		scanner[1] = new PropertyChangeRuleBaseScanner(editor,
				PreferenceInitializer.PREF_TEXTATTR_STRING);
		dr = new DefaultDamagerRepairer(scanner[1]);
		reconciler.setDamager(dr, RFPartitionScanner.PARTITION_STRING);
		reconciler.setRepairer(dr, RFPartitionScanner.PARTITION_STRING);

		return reconciler;
	}

	public void dispose() {
		if (recodescanner != null) {
			recodescanner.dispose();
		}

		for (int i = 0; i < scanner.length; i++) {
			if (scanner[i] != null) {
				scanner[i].dispose();
			}
		}
	}

	// ------------------------------------------------------------
	// ----Added by Terry Li-------------------------------------

	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType) {
		return new RFTextHover(sourceViewer);
	}

	// ------------------------------------------------------------
	// ----Added by Yidan Wang-------------------------------------
	/**
	 * {@inheritDoc}
	 */
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		IReconcilingStrategy reconcilingStrategy = new RFReconcilingStategy(
				editor);

		MonoReconciler reconciler = new MonoReconciler(reconcilingStrategy,
				false);
		reconciler.setProgressMonitor(new NullProgressMonitor());
		reconciler.setDelay(500);

		return reconciler;
	}
	// ----end-----------------------------------------------------
	// ------------------------------------------------------------

}
