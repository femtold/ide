package com.radioflex.ide.ui.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.editors.text.TextEditor; 

public class RadioflexEditor extends TextEditor{
	public static final String ID = "com.radioflex.ide.ui.radioflexeditor";

	protected void initializeEditor() {
		super.initializeEditor();
		setSourceViewerConfiguration(new RESourceViewerConfiguration());
	}
}
