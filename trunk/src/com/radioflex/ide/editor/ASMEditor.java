package com.radioflex.ide.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;

import com.radioflex.ide.Constants;

/**
 * The editor class.
 * 
 */
public class ASMEditor extends TextEditor {

	public static final String RADIOFLEX_EDITOR_ID = "com.radioflex.ide.editor";

	/**
	 * {@inheritDoc}
	 */
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);

		IDocument document = getDocumentProvider()
				.getDocument(getEditorInput());

		FastPartitioner partitioner = new FastPartitioner(
				new ASMPartitonScanner(),
				new String[] { Constants.PARTITION_STRING,
						Constants.PARTITION_COMMENT });

		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void initializeEditor() {
		super.initializeEditor();
		setSourceViewerConfiguration(new ASMSourceViewerConfiguration(this));
	}

	/**
	 * Refresh the editor.
	 */
	public void refreshSourceViewer() {
		ISourceViewer isv = getSourceViewer();
		if (isv instanceof SourceViewer) {
			((SourceViewer) getSourceViewer()).refresh();
		}
	}

	public void refreshSourceViewerConfiguration() {
		this.setSourceViewerConfiguration(getSourceViewerConfiguration());
	}

	/**
	 * {@inheritDoc}
	 */
	public void dispose() {
		super.dispose();

		SourceViewerConfiguration svc = getSourceViewerConfiguration();
		if (svc instanceof ASMSourceViewerConfiguration)
			((ASMSourceViewerConfiguration) svc).dispose();
	}
}
