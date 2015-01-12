package com.radioflex.ide;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.ide.IDE;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	//private static final String PERSPECTIVE_ID = "com.radioflex.ide.perspective"; //$NON-NLS-1$

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}
	/*
	 * MODIFY ADD
	 * (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#getInitialWindowPerspectiveId()
	 */
	public String getInitialWindowPerspectiveId() {
		return Constants.PERSPECTIVE_EDIT_ID;   //return the edit model perspective
	}
	/*	ADD CHECK
	 * (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#getDefaultPageInput()
	 */

	//Returns the default input for newly created workbench pages 
	public IAdaptable getDefaultPageInput() {
		
		//the workspace root will be return
		return ResourcesPlugin.getWorkspace().getRoot(); 
	}
	/*
	 * ADD CHECK
	 * (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#initialize(org.eclipse.ui.application.IWorkbenchConfigurer)
	 */
	public void initialize(IWorkbenchConfigurer configurer) {
		
		//This is necessary to enable certain types of content in the explorers. 
		IDE.registerAdapters();

		//add the  ASM project image
		getWorkbenchConfigurer().declareImage(IDE.SharedImages.IMG_OBJ_PROJECT,
				Activator.getImageDescriptor("icons/alt_window16.gif"), true);
	}
	/*
	 * ADD CHECK
	 * (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#preShutdown()
	 */
	
	public boolean preShutdown(){
		
		//save the workspace preShutdown
		try{
			ResourcesPlugin.getWorkspace().save(true, null);
		}catch(CoreException e){
			e.printStackTrace();
		}
		return true;
	}
}
