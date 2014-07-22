package org.radioflex.ide.ui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PerspectiveIDE implements IPerspectiveFactory {
		
		
	    public void createInitialLayout(IPageLayout layout) {
	    	
	    	layout.addView("example.view", IPageLayout.LEFT, 0.3f, IPageLayout.ID_EDITOR_AREA);
	    	layout.setFixed(true);
	    }
	}

