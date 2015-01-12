package com.radioflex.ide.project;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

import com.radioflex.ide.Constants;

/**
 * The nature of an ASM project. Set a builder to the underlying project.
 *
 */
public class ASMNature implements IProjectNature {
	
	public static final String RADIOFLEX_NATURE_ID = "com.radioflex.ide.nature";

	/**
	 * The underlying project.
	 */
	private IProject project;

	/**
	 * {@inheritDoc}}
	 */
	public void configure() throws CoreException {
		IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();
		boolean found = false;

		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(Constants.RADIOFLEX_BUILDER_ID)) {
				found = true;
				break;
			}
		}
		if (!found) {
			//add builder to project
			ICommand command = desc.newCommand();
			command.setBuilderName(Constants.RADIOFLEX_BUILDER_ID);
			ICommand[] newCommands = new ICommand[commands.length+1];
			
			//add it before other builders.
			System.arraycopy(commands, 0, newCommands, 1, commands.length);
			newCommands[0] = command;
			desc.setBuildSpec(newCommands);
			project.setDescription(desc, null);
		}
	}

	@Override
	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public IProject getProject() {
		// TODO Auto-generated method stub
		return project;
	}

	@Override
	public void setProject(IProject value) {
		// TODO Auto-generated method stub
		project = value;

	}

}
