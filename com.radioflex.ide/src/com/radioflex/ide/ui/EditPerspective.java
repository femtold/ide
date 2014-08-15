package com.radioflex.ide.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class EditPerspective implements IPerspectiveFactory {

	private static final String FOLDER_LEFT_ID = "perspective.edit.folder.left";

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		layout.setFixed(true);

		IFolderLayout leftFolder = layout.createFolder(FOLDER_LEFT_ID,
				IPageLayout.LEFT, 0.2f, layout.getEditorArea());
		leftFolder.addView(IDConstants.OUTLINE_ID);
		leftFolder.addView(IDConstants.XMLView_ID);
		layout.addView(IDConstants.PROJECT_EXPLORER_ID, IPageLayout.TOP, 0.45f,
				FOLDER_LEFT_ID);		
	}
}
