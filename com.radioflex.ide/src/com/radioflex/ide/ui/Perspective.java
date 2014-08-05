package com.radioflex.ide.ui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		layout.setFixed(true);
		layout.addStandaloneView("example.view", true /* show title */,
				IPageLayout.LEFT, 0.2f, layout.getEditorArea());
		layout.addView("org.eclipse.ui.views.ContentOutline", IPageLayout.RIGHT,
				0.77f, layout.getEditorArea());
//		layout.addFastView("org.eclipse.ui.views.ContentOutline");
	}
}
