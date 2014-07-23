package com.radioflex.ide.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;

import com.radioflex.ide.ui.Constants;
import com.radioflex.ide.ui.editor.REPartitionScanner;

public class RadioflexEditor extends TextEditor{
	public static final String ID = "com.radioflex.ide.ui.radioflexeditor";
	
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);

		IDocument document = getDocumentProvider()
				.getDocument(getEditorInput());

		FastPartitioner partitioner = new FastPartitioner(
				new REPartitionScanner(),
				new String[] { Constants.PARTITION_STRING,
						Constants.PARTITION_COMMENT });
		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);
	}

	protected void initializeEditor() {
		super.initializeEditor();
		setSourceViewerConfiguration(new RESourceViewerConfiguration(this));
	}
	
	public void refreshSourceViewer(){
		ISourceViewer isv = getSourceViewer();
		if (isv instanceof SourceViewer) {
			((SourceViewer) getSourceViewer()).refresh();
		}
	}
	
	public void dispose() {
		super.dispose();

		SourceViewerConfiguration svc = getSourceViewerConfiguration();
		if (svc instanceof RESourceViewerConfiguration) {
			((RESourceViewerConfiguration) svc).dispose();
		}
	}
}
