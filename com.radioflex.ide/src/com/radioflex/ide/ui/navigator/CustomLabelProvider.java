package com.radioflex.ide.ui.navigator;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class CustomLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		//System.out.println("CustomLabelProvider.addListener is called");

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		//System.out.println("CustomLabelProvider.dispose is called");
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		//System.out.println("CustomLabelProvider.isLabelProperty is called");
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		//System.out.println("CustomLabelProvider.removeListener is called");

	}

	@Override
	public Image getImage(Object element) {
		// TODO Auto-generated method stub
		//System.out.println("CustomLabelProvider.getImage is called");
		/*
		 * return icon according to the type of element: 
		 * 		IProject, 
		 * 		IFolder, 
		 * 		IFile, 
		 * 		as well as File type
		 * */
		return null;
	}

	@Override
	public String getText(Object element) {
		// TODO Auto-generated method stub
		//System.out.println("CustomLabelProvider.getText is called: " + element.toString());
		
		if(element instanceof IResource) {
			return ((IResource)element).getName();
		}
		return element.toString();
	}

}
