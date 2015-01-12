package com.radioflex.ide.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;

import com.radioflex.ide.Activator;
import com.radioflex.ide.Constants;
import com.radioflex.ide.Messages;
import com.radioflex.ide.ui.BuildConfigHandler;
import com.radioflex.ide.ui.ConsoleFactory;
import com.radioflex.ide.ui.ConsoleHandler;

/**
 * Builder for ASM-Files.
 *
 */
public class ASMBuilder extends IncrementalProjectBuilder {

	public static final String RADIOFLEX_BUILDER_ID = "com.radioflex.ide.builder";
	
	private IProject project;

	/**
	 * {@inheritDoc}
	 */
	protected IProject[] build(int kind, Map<String, String> args,
			IProgressMonitor monitor) throws CoreException {

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		String compiler = store.getString(Constants.PREFERENCES_COMPILER_NAME)
				.trim();

		ConsoleFactory.showConsole();

		if (compiler.length() < 1) {
			ConsoleHandler.error(Messages.NO_COMPILER);
			return null;
		}

		ConsoleHandler.info(Messages.START_BUILDING);
		if (kind == IncrementalProjectBuilder.FULL_BUILD) {
			fullBuild(monitor);
		} else if (kind == IncrementalProjectBuilder.INCREMENTAL_BUILD) {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		} else if (kind == IncrementalProjectBuilder.CLEAN_BUILD) {
			clean(monitor);
		}

		return null;
	}

	/**
	 * Builds the hole project.
	 * @param monitor A monitor.
	 * @throws CoreException
	 */
	private void fullBuild(IProgressMonitor monitor) throws CoreException {
		try {
			project = getProject();
			monitor.beginTask(Messages.BUILDING_TITLE, 100);
			monitor.subTask(Messages.BUILDING_TEXT_COMPILE);
			project.accept(new MyFullBuildVistor());
			monitor.done();
		} catch (CoreException e) {
			Activator.getDefault().getLog().log(e.getStatus());
		}

	}

	/**
	 * Builds only the project delta.
	 * @param delta The given delta.
	 * @param monitor A monitor.
	 * @throws CoreException
	 */
	private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor)
			throws CoreException {
		try {
			project = getProject();
			monitor.beginTask(Messages.BUILDING_TITLE, 100);
			monitor.subTask(Messages.BUILDING_TEXT_COMPILE);
			delta.accept(new MyIncrementalBuildVistor());
			monitor.done();
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			Activator.getDefault().getLog().log(e.getStatus());
		}
	}

	/**
	 * Clean the project build and discards any problems and previously built state.
	 * @param monitor A monitor.
	 */
	protected void clean(IProgressMonitor monitor) throws CoreException {
		ConsoleFactory.showConsole();
		ConsoleHandler.info("clean");
		try {
			IProject project = getProject();
			project.getProjectRelativePath();
			project.accept(new MyCleanVistor());
		} catch (CoreException e) {
			Activator.getDefault().getLog().log(e.getStatus());
		}
	}

	/**
	 * Class for visiting all resources when a full build is initiated.
	 *
	 */
	private class MyFullBuildVistor implements IResourceVisitor {

		@Override
		public boolean visit(IResource resource) throws CoreException {
			int resourceType = resource.getType();

			if ((resourceType == IResource.FOLDER)
					|| (resourceType == IResource.PROJECT))
				return true;
			else if (resourceType == IResource.FILE) {
				String extension = resource.getFileExtension();
				if ((extension != null) && extension.equalsIgnoreCase("asm")) {
					compileFile((IFile) resource);
				}

			}
			return false;
		}

	}

	/**
	 * Class for visiting all resources when a incremental build is initiated.
	 *
	 */
	private class MyIncrementalBuildVistor implements IResourceDeltaVisitor {

		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			int deltaKind = delta.getKind();

			if ((deltaKind == IResourceDelta.ADDED)
					|| (deltaKind == IResourceDelta.CHANGED)) {
				IResource resource = delta.getResource();
				int resourceType = resource.getType();

				if ((resourceType == IResource.FOLDER)
						|| (resourceType == IResource.PROJECT))
					return true;
				else if (resourceType == IResource.FILE) {
					String extension = resource.getFileExtension();
					if ((extension != null)
							&& extension.equalsIgnoreCase("asm")) {
						compileFile((IFile) resource);
					}
				}
			}
			return false;
		}

	}

	/**
	 * Class for visiting all resources when a clean is initiated.
	 *
	 */
	private class MyCleanVistor implements IResourceVisitor {

		public boolean visit(IResource resource) throws CoreException {
			int resourceType = resource.getType();

			if (resourceType == IResource.PROJECT)
				return true;
			else if (resourceType == IResource.FOLDER) {

				resource.delete(true, null);
			}
			return false;
		}

	}

	/**
	 * Compiles a given file.
	 * @param file The file to be compiled.
	 */
	private void compileFile(IFile file) {

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		String compiler = store.getString(Constants.PREFERENCES_COMPILER_NAME)
				.trim();
		String params1 = store.getString(
				Constants.PREFERENCES_COMPILER_PARAMS_1).trim();
		String params2 = store.getString(
				Constants.PREFERENCES_COMPILER_PARAMS_2).trim();

		if (params1 == null)
			params1 = "";
		if (params2 == null)
			params2 = "";
		String dir = file.getLocation().toOSString();

		int pos = dir.lastIndexOf(System.getProperty("file.separator"));

		if (pos > 0)
			dir = dir.substring(0, pos);

		String dir_sim = "";
		String dir_fpga = "";
		dir_sim = dir + System.getProperty("file.separator")
				+ Constants.BUILD_DIRECTORY_SIM;
		dir_fpga = dir + System.getProperty("file.separator")
				+ Constants.BUILD_DIRECTORY_FPGA;

		boolean success = true;

		int currentState = BuildConfigHandler.activeConfig();

		// Path p_dir = null;

		if ((!(new File(dir_sim)).exists()) && currentState == 0) {
			success = (new File(dir_sim)).mkdir();
			// p_dir = new Path(dir_sim);
		}

		if ((!(new File(dir_fpga)).exists()) && currentState == 1) {
			success = (new File(dir_fpga)).mkdir();
			// p_dir = new Path(dir_fpga);
		}

		if (success) {

			String dest = "";

			if (currentState == 0)
				dest = dir_sim + System.getProperty("file.separator")
						+ file.getName();
			else
				dest = dir_fpga + System.getProperty("file.separator")
						+ file.getName();
			File fdest = new File(dest);

			copyFile(new File(file.getLocation().toOSString()), fdest, true);

			String output = "";

			if (currentState == 0)
				output = ProgramExecuter.exec(compiler, params1, dest, dir_sim,
						true, true, true);
			else if (currentState == 1)
				output = ProgramExecuter.exec(compiler, params2, dest,
						dir_fpga, true, true, true);
			ConsoleHandler.error(output);

			if (fdest.exists())
				fdest.delete();
		}

	}

	/**
	 * Copy a file from source to destination.
	 * @param src Source file
	 * @param dst Destination File
	 * @param force If destination exists then delete it.
	 * @return True if copy successful.
	 */
	private boolean copyFile(File src, File dst, boolean force) {
		if (!src.exists())
			return false;

		if (dst.exists())
			dst.delete();

		try {
			FileInputStream fis = new FileInputStream(src);
			FileOutputStream fos = new FileOutputStream(dst);

			byte[] buf = new byte[1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1)
				fos.write(buf, 0, i);
			fis.close();
			fos.close();
		} catch (Exception e) {
			return false;
		}

		if (dst.exists())
			return true;

		return false;

	}
}
