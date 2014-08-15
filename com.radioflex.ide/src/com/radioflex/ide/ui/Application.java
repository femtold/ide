package com.radioflex.ide.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.ide.ChooseWorkspaceData;
import org.eclipse.ui.internal.ide.ChooseWorkspaceDialog;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.StatusUtil;
import org.eclipse.ui.internal.ide.application.DelayedEventsProcessor;
import org.eclipse.ui.internal.ide.application.IDEApplication;
import org.eclipse.ui.internal.ide.application.IDEWorkbenchAdvisor;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;


/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication , IExecutableExtension{

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public static final String METADATA_FOLDER = ".metadata"; //$NON-NLS-1$

    private static final String VERSION_FILENAME = "version.ini"; //$NON-NLS-1$

    // Use the branding plug-in of the platform feature since this is most likely
    // to change on an update of the IDE.
    private static final String WORKSPACE_CHECK_REFERENCE_BUNDLE_NAME = "org.eclipse.platform"; //$NON-NLS-1$
    private static final Version WORKSPACE_CHECK_REFERENCE_BUNDLE_VERSION;
    static {
        Bundle bundle = Platform.getBundle(WORKSPACE_CHECK_REFERENCE_BUNDLE_NAME);
        WORKSPACE_CHECK_REFERENCE_BUNDLE_VERSION = bundle != null ? bundle.getVersion() : null/*not installed*/;
    }

    private static final String WORKSPACE_CHECK_REFERENCE_BUNDLE_NAME_LEGACY = "org.eclipse.core.runtime"; //$NON-NLS-1$
    private static final String WORKSPACE_CHECK_LEGACY_VERSION_INCREMENTED = "2"; //$NON-NLS-1$   legacy version=1

    private static final String PROP_EXIT_CODE = "eclipse.exitcode"; //$NON-NLS-1$

    /**
     * A special return code that will be recognized by the launcher and used to
     * restart the workbench.
     */
    private static final Integer EXIT_RELAUNCH = new Integer(24);

    /**
     * A special return code that will be recognized by the PDE launcher and used to
     * show an error dialog if the workspace is locked.
     */
    private static final Integer EXIT_WORKSPACE_LOCKED = new Integer(15);
    
    /**
     * The ID of the application plug-in
     */
	public static final String PLUGIN_ID = "org.eclipse.ui.ide.application"; //$NON-NLS-1$

    /**
     * Creates a new IDE application.
     */
    public Application() {
        // There is nothing to do for IDEApplication
    }

    /* (non-Javadoc)
     * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext context)
     */
    public Object start(IApplicationContext appContext) throws Exception {
        Display display = createDisplay();
        // processor must be created before we start event loop
        DelayedEventsProcessor processor = new DelayedEventsProcessor(display);

        try {

        	// look and see if there's a splash shell we can parent off of
        	Shell shell = WorkbenchPlugin.getSplashShell(display);
        	if (shell != null) {
        		// should should set the icon and message for this shell to be the 
        		// same as the chooser dialog - this will be the guy that lives in
        		// the task bar and without these calls you'd have the default icon 
        		// with no message.
        		shell.setText(ChooseWorkspaceDialog.getWindowTitle());
        		shell.setImages(Window.getDefaultImages());
        	}
           
            Object instanceLocationCheck = checkInstanceLocation(shell, appContext.getArguments());
			if (instanceLocationCheck != null) {
            	WorkbenchPlugin.unsetSplashShell(display);
                appContext.applicationRunning();
                return instanceLocationCheck;
            }

            // create the workbench with this advisor and run it until it exits
            // N.B. createWorkbench remembers the advisor, and also registers
            // the workbench globally so that all UI plug-ins can find it using
            // PlatformUI.getWorkbench() or AbstractUIPlugin.getWorkbench()
            int returnCode = PlatformUI.createAndRunWorkbench(display,
            		new ApplicationWorkbenchAdvisor());

            // the workbench doesn't support relaunch yet (bug 61809) so
            // for now restart is used, and exit data properties are checked
            // here to substitute in the relaunch return code if needed
            if (returnCode != PlatformUI.RETURN_RESTART) {
				return EXIT_OK;
			}

            // if the exit code property has been set to the relaunch code, then
            // return that code now, otherwise this is a normal restart
            return EXIT_RELAUNCH.equals(Integer.getInteger(PROP_EXIT_CODE)) ? EXIT_RELAUNCH
                    : EXIT_RESTART;
        } finally {
            if (display != null) {
				display.dispose();
			}
            Location instanceLoc = Platform.getInstanceLocation();
            if (instanceLoc != null)
            	instanceLoc.release();
        }
    }

    /**
     * Creates the display used by the application.
     * 
     * @return the display used by the application
     */
    protected Display createDisplay() {
        return PlatformUI.createDisplay();
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
     */
    public void setInitializationData(IConfigurationElement config,
            String propertyName, Object data) {
        // There is nothing to do for IDEApplication
    }

  
    private Object checkInstanceLocation(Shell shell, Map applicationArguments) {
        // -data @none was specified but an ide requires workspace
        Location instanceLoc = Platform.getInstanceLocation();
        if (instanceLoc == null) {
            MessageDialog
                    .openError(
                            shell,
                            IDEWorkbenchMessages.IDEApplication_workspaceMandatoryTitle,
                            IDEWorkbenchMessages.IDEApplication_workspaceMandatoryMessage);
            return EXIT_OK;
        }

        // -data "/valid/path", workspace already set
        if (instanceLoc.isSet()) {
            // make sure the meta data version is compatible (or the user has
            // chosen to overwrite it).
            if (!checkValidWorkspace(shell, instanceLoc.getURL())) {
				return EXIT_OK;
			}

            // at this point its valid, so try to lock it and update the
            // metadata version information if successful
            try {
                if (instanceLoc.lock()) {
//                    writeWorkspaceVersion();
                    return null;
                }
                
                // we failed to create the directory.  
                // Two possibilities:
                // 1. directory is already in use
                // 2. directory could not be created
                File workspaceDirectory = new File(instanceLoc.getURL().getFile());
                if (workspaceDirectory.exists()) {
                	if (isDevLaunchMode(applicationArguments)) {
                		return EXIT_WORKSPACE_LOCKED;
                	}
	                MessageDialog.openError(
	                        shell,
	                        IDEWorkbenchMessages.IDEApplication_workspaceCannotLockTitle,
	                        NLS.bind(IDEWorkbenchMessages.IDEApplication_workspaceCannotLockMessage, workspaceDirectory.getAbsolutePath()));
                } else {
                	MessageDialog.openError(
                			shell, 
                			IDEWorkbenchMessages.IDEApplication_workspaceCannotBeSetTitle,
                			IDEWorkbenchMessages.IDEApplication_workspaceCannotBeSetMessage);
                }
            } catch (IOException e) {
                IDEWorkbenchPlugin.log("Could not obtain lock for workspace location", //$NON-NLS-1$
                        e);            	
                MessageDialog
                .openError(
                        shell,
                        IDEWorkbenchMessages.InternalError,
                        e.getMessage());                
            }            
            return EXIT_OK;
        }

        // -data @noDefault or -data not specified, prompt and set
        ChooseWorkspaceData launchData = new ChooseWorkspaceData(instanceLoc
                .getDefault());

        boolean force = false;
        while (true) {
            URL workspaceUrl = promptForWorkspace(shell, launchData, force);
            if (workspaceUrl == null) {
				return EXIT_OK;
			}

            // if there is an error with the first selection, then force the
            // dialog to open to give the user a chance to correct
            force = true;

            try {
                // the operation will fail if the url is not a valid
                // instance data area, so other checking is unneeded
                if (instanceLoc.set(workspaceUrl, true)) {
                    launchData.writePersistedData();
                    return null;
                }
            } catch (IllegalStateException e) {
                MessageDialog
                        .openError(
                                shell,
                                IDEWorkbenchMessages.IDEApplication_workspaceCannotBeSetTitle,
                                IDEWorkbenchMessages.IDEApplication_workspaceCannotBeSetMessage);
                return EXIT_OK;
            } catch (IOException e) {
            	  MessageDialog
                  .openError(
                          shell,
                          IDEWorkbenchMessages.IDEApplication_workspaceCannotBeSetTitle,
                          IDEWorkbenchMessages.IDEApplication_workspaceCannotBeSetMessage);
			}

            // by this point it has been determined that the workspace is
            // already in use -- force the user to choose again
            MessageDialog.openError(shell, IDEWorkbenchMessages.IDEApplication_workspaceInUseTitle, 
                    NLS.bind(IDEWorkbenchMessages.IDEApplication_workspaceInUseMessage, workspaceUrl.getFile()));
        }
    }

	private static boolean isDevLaunchMode(Map args) {
		// see org.eclipse.pde.internal.core.PluginPathFinder.isDevLaunchMode()
		if (Boolean.getBoolean("eclipse.pde.launch")) //$NON-NLS-1$
			return true;
		return args.containsKey("-pdelaunch"); //$NON-NLS-1$
	}
	

    private URL promptForWorkspace(Shell shell, ChooseWorkspaceData launchData,
			boolean force) {
        URL url = null;
        do {
        	// okay to use the shell now - this is the splash shell
            new ChooseWorkspaceDialog(shell, launchData, false, true).prompt(force);
            String instancePath = launchData.getSelection();
            if (instancePath == null) {
				return null;
			}

            force = true;

            // 70576: don't accept empty input
            if (instancePath.length() <= 0) {
                MessageDialog
                .openError(
                        shell,
                        IDEWorkbenchMessages.IDEApplication_workspaceEmptyTitle,
                        IDEWorkbenchMessages.IDEApplication_workspaceEmptyMessage);
                continue;
            }

            // create the workspace if it does not already exist
            File workspace = new File(instancePath);
            if (!workspace.exists()) {
				workspace.mkdir();
			}

            try {
                // Don't use File.toURL() since it adds a leading slash that Platform does not
                // handle properly.  See bug 54081 for more details.  
                String path = workspace.getAbsolutePath().replace(
                        File.separatorChar, '/');
                url = new URL("file", null, path); //$NON-NLS-1$
            } catch (MalformedURLException e) {
                MessageDialog
                        .openError(
                                shell,
                                IDEWorkbenchMessages.IDEApplication_workspaceInvalidTitle,
                                IDEWorkbenchMessages.IDEApplication_workspaceInvalidMessage);
                continue;
            }
        } while (!checkValidWorkspace(shell, url));

        return url;
    }

   
    private boolean checkValidWorkspace(Shell shell, URL url) {
        // a null url is not a valid workspace
        if (url == null) {
			return false;
		}

        if (WORKSPACE_CHECK_REFERENCE_BUNDLE_VERSION == null) {
            // no reference bundle installed, no check possible
            return true;
        }

		int severity;
		String title;
		String message;
		
		severity = MessageDialog.INFORMATION;
		title = IDEWorkbenchMessages.IDEApplication_versionTitle_olderWorkspace;
		message = NLS.bind(IDEWorkbenchMessages.IDEApplication_versionMessage_olderWorkspace, url.getFile());
		MessageDialog dialog = new MessageDialog(shell, title, null, message, severity,
				new String[] {IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL}, 0);
		return dialog.open() == Window.OK;
    }


    /* (non-Javadoc)
     * @see org.eclipse.equinox.app.IApplication#stop()
     */
	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
			return;
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}