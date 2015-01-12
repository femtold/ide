package com.radioflex.ide.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import com.radioflex.ide.Constants;

/**
 * The edit model perspective.
 *  
 */
public class EditPerspective implements IPerspectiveFactory {
	
	
	private static final String FOLDER_LEFT_ID = "perspective.edit.folder.left";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		
		//show the editor area
		layout.setEditorAreaVisible(true);
		layout.setFixed(true);
		
		//show the project explorer view and XML view
		IFolderLayout leftFolder = layout.createFolder(FOLDER_LEFT_ID, IPageLayout.LEFT, 0.2f, layout.getEditorArea());
		leftFolder.addView(Constants.VIEW_XML);
		layout.addView(Constants.VIEW_PROJECT_EXPLORER,IPageLayout.TOP, 0.5f,FOLDER_LEFT_ID);
		
		//show the console view
		ConsoleFactory cf = new ConsoleFactory();
		layout.addView(IConsoleConstants.ID_CONSOLE_VIEW, IPageLayout.BOTTOM, 0.70f, editorArea);
		cf.openConsole();
	}
}
