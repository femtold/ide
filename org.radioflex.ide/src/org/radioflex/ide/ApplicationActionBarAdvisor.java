package org.radioflex.ide;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	private IWorkbenchAction New, Import, Export, Exit, saveAction,
			saveAllAction;
	private IWorkbenchAction Redo, Undo, Copy, Paste, Cut, Find;
	private IWorkbenchAction openPerspective,showQC,preferences;
	private IWorkbenchAction properties;
	private IWorkbenchAction HelpContents;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(IWorkbenchWindow window) {

		New = ActionFactory.NEW.create(window);
		New.setText("New");
		register(New);

		Import = ActionFactory.IMPORT.create(window);
		register(Import);

		Export = ActionFactory.EXPORT.create(window);
		register(Export);

		Exit = ActionFactory.QUIT.create(window);
		register(Exit);

		saveAction = ActionFactory.SAVE.create(window);
		register(saveAction);

		saveAllAction = ActionFactory.SAVE_ALL.create(window);
		register(saveAllAction);

		Redo = ActionFactory.REDO.create(window);
		register(Redo);

		Undo = ActionFactory.UNDO.create(window);
		register(Undo);

		Copy = ActionFactory.COPY.create(window);
		register(Copy);

		Paste = ActionFactory.PASTE.create(window);
		register(Paste);

		Cut = ActionFactory.CUT.create(window);
		register(Cut);

		Find = ActionFactory.FIND.create(window);
		register(Find);

		properties = ActionFactory.PROPERTIES.create(window);
		register(properties);
		
		openPerspective = ActionFactory.OPEN_PERSPECTIVE_DIALOG.create(window);
		register(openPerspective);
		
		showQC = ActionFactory.SHOW_QUICK_ACCESS.create(window);
		register(showQC);
		
		preferences = ActionFactory.PREFERENCES.create(window);
		register(preferences);

		HelpContents = ActionFactory.HELP_CONTENTS.create(window);
		register(HelpContents);

	}

	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("&File",
				IWorkbenchActionConstants.M_FILE);
		MenuManager editMenu = new MenuManager("&Edit",
				IWorkbenchActionConstants.M_EDIT);
		MenuManager debugMeun = new MenuManager("Debug");
		MenuManager projectMenu = new MenuManager("Project",
				IWorkbenchActionConstants.M_PROJECT);
		MenuManager windowMenu = new MenuManager("Window",
				IWorkbenchActionConstants.M_WINDOW);
		MenuManager helpMenu = new MenuManager("&Help",
				IWorkbenchActionConstants.M_HELP);

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(debugMeun);
		menuBar.add(projectMenu);
		menuBar.add(windowMenu);
		menuBar.add(helpMenu);

		fileMenu.add(New);
		fileMenu.add(Import);
		fileMenu.add(Export);
		fileMenu.add(saveAction);
		fileMenu.add(saveAllAction);
		fileMenu.add(Exit);

		editMenu.add(Redo);
		editMenu.add(Undo);
		editMenu.add(Copy);
		editMenu.add(Paste);
		editMenu.add(Cut);
		editMenu.add(Find);

		projectMenu.add(properties);
		
		windowMenu.add(openPerspective);
		windowMenu.add(showQC);
		windowMenu.add(preferences);

		helpMenu.add(HelpContents);

	}

	protected void fillCoolBar(ICoolBarManager coolBar) {
	}
}
