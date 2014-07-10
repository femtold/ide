package com.radioflex.ide.ui.editor;

import org.eclipse.ui.editors.text.TextEditor;

public class RadioflexEditor extends TextEditor{
	public static final String ID = "com.radioflex.ide.ui.radioflexeditor";

	protected void initializeEditor() {
		super.initializeEditor();
		setSourceViewerConfiguration(new RESourceViewerConfiguration());
	}
}
