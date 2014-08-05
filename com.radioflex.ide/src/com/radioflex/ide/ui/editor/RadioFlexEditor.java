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
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.radioflex.ide.ui.Constants;
import com.radioflex.ide.ui.editor.REPartitionScanner;

public class RadioFlexEditor extends TextEditor {
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

	public void refreshSourceViewer() {
		ISourceViewer isv = getSourceViewer();
		if (isv instanceof SourceViewer) {
			((SourceViewer) getSourceViewer()).refresh();
		}

	}

	public void refreshSourceViewerConfiguration() {
		this.setSourceViewerConfiguration(getSourceViewerConfiguration());
	}

	public void dispose() {
		super.dispose();

		SourceViewerConfiguration svc = getSourceViewerConfiguration();
		if (svc instanceof RESourceViewerConfiguration) {
			((RESourceViewerConfiguration) svc).dispose();
		}
	}


	// ------------------------------------------------------------
	// ----Added by Yidan Wang-------------------------------------
	/** field: outlinePage for RadioFlexEditor */
	private REOutlinePage outlinePage;

	// For test: print class call trace
	private void print(String s) {
		System.out.println(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getAdapter(Class adapter) {

		print("start in RadioFlexEditor getAdapter");
		print(adapter.getName());

		if (adapter != null && IContentOutlinePage.class.equals(adapter)) {
			// create and initialize an IContentOutlinePage
			// object(REOutlinePage) for the particular text
			// editor(RadioFlexEditor) first
			if (outlinePage == null) {
				outlinePage = new REOutlinePage(this);
				IEditorInput input = getEditorInput();
				if (input != null) {
					outlinePage.setInput(input);
				}
			}

			print("end in RadioFlexEditor getAdapter: return contentOutlinePage");
			return outlinePage;
		}

		print("end in RadioFlexEditor getAdapter: return super.getAdapter(adapter)");
		return super.getAdapter(adapter);
	}

	/**
	 * Updates the view of the outlinepage.
	 */
	public void updateOutlinePage() {

		print("start in RadioFlexEditor updateContentOutlinePage");

		if (outlinePage != null) {
			IEditorInput input = getEditorInput();
			if (input != null) {

				// test
				print(" in RadioFlexEditor updateContentOutlinePage:"
						+ "outlinePage.setInput(input)");

				outlinePage.setInput(input);
			}
		}

		print("end in RadioFlexEditor updateContentOutlinePage");

	}
	// ----end-----------------------------------------------------
	// ------------------------------------------------------------
}
