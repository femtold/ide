package org.radioflex.ide.navigetor;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;



public class CustomContentProvider implements ITreeContentProvider, IResourceChangeListener {

	public CustomContentProvider() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this,IResourceChangeEvent.POST_CHANGE);
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		System.out.println("CustomContentProvider.dispose is called");
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		System.out.println("CustomContentProvider.inputChanged is called");

	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		System.out.println("CustomContentProvider.getElements is called!");
		System.out.println("input Elements are: " + inputElement);
		return this.getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		System.out.println("CustomContentProvider.getChildren is called!");
		System.out.println("parent Elements are: " + parentElement);
		// TODO Auto-generated method stub
		
		
		Object[] objs = new Object[0];
		
		if(parentElement instanceof IWorkspaceRoot) {
			objs = ((IWorkspaceRoot) parentElement).getProjects();
		}
		
		
		if(parentElement instanceof IProject) {
			IProject p = ((IProject)parentElement);
			if(p.isOpen()) {
				try {		
					objs = ((IProject) parentElement).members();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		if(parentElement instanceof IFolder) {
		
			try {
				objs = ((IFolder) parentElement).members();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return objs;
		
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		System.out.println("CustomContentProvider.getParent is called!");
		System.out.println("element is: " + element);
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		System.out.println("CustomContentProvider.hasChildren is called!");
		int len = this.getChildren(element).length;
		System.out.println("hasChildren is finished!");
		return len>0;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		// TODO Auto-generated method stub
		System.out.println("resourceChanged");
		System.out.println(event);
		
		
	}

}
