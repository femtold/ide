package org.radioflex.ide.project;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;


public class ASMBuilder extends IncrementalProjectBuilder {

	public ASMBuilder() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IProject[] build(int kind, Map<String, String> args,
			IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		// add your build logic here
		if (kind == FULL_BUILD) {
			fullBuild(getProject(), monitor);
		} else {
			incrementalBuild(getProject(), monitor, getDelta(getProject()));
		}
		return null;
	}

	private void incrementalBuild(IProject project, IProgressMonitor monitor,
			IResourceDelta delta) throws CoreException {
		if (delta == null) {
			fullBuild(project, monitor);
		} else {
			System.out.println("Doing an incremental build");
			delta.accept(new ASMVistor());
		}
	}

	private void fullBuild(IProject project, IProgressMonitor monitor)
			throws CoreException {
		System.out.println("Doing a full build");
		project.accept(new ASMVistor(),IResource.NONE);
	}

	protected void startupOnInitialize() {
		// add builder logic here
	}

	protected void clean(IProgressMonitor monitor) {
		// add build clean logic here
	}

}
