package org.radioflex.ide.navigetor;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.navigator.CommonNavigator;



public class CustomNavigator extends CommonNavigator {

	static int index = 0;
	
	protected IAdaptable getInitialInput() {
		this.getCommonViewer().refresh();	
		return ResourcesPlugin.getWorkspace().getRoot();
	}

}
