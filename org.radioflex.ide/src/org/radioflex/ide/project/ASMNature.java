package org.radioflex.ide.project;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.radioflex.ide.Constants;

public class ASMNature implements IProjectNature {

	private IProject project;

	public void configure() throws CoreException {
		// TODO Auto-generated method stub
		// Add nature-specific information
		// for the project, such as adding a builder
		// to a project's build spec
		IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();
		boolean found = false;

		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(Constants.BUILDER_ID)) {
				found = true;
				break;
			}
		}
		if (!found) {
			// add builder to project
			ICommand command = desc.newCommand();
			command.setBuilderName(Constants.BUILDER_ID);
			ICommand[] newCommands = new ICommand[commands.length + 1];

			// Add it before other builders.
			System.arraycopy(commands, 0, newCommands, 1, commands.length);
			newCommands[0] = command;
			desc.setBuildSpec(newCommands);
			project.setDescription(desc, null);
		}
	}

	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub
		// Remove the nature-specific information here

	}

	public IProject getProject() {
		// TODO Auto-generated method stub
		return project;
	}

	public void setProject(IProject value) {
		// TODO Auto-generated method stub
		project = value;

	}

}
