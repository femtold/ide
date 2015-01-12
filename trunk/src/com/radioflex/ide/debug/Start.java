package com.radioflex.ide.debug;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.cdt.utils.pty.PTY;
import org.eclipse.cdt.utils.spawner.ProcessFactory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.internal.core.StreamsProxy;
import org.eclipse.ui.PlatformUI;

import com.radioflex.ide.Constants;
import com.radioflex.ide.view.XMLView;

public class Start extends AbstractHandler {
	PTY pty;
	static Process process;
	private File workingDirectory;
	private XMLView xmlView;
	private StreamsProxy streamsProxy;
	private final static String encoding = "UTF-8";
	private static OutputStream terminalToRemoteStream;

	/*public Start() {
		super();
	}*/
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			pty = new PTY(false);
			String[] string = new String[] { "/bin/bash", "-i" };
			ProcessFactory factory = ProcessFactory.getFactory(); //singleton pattern
			process = factory.exec(string, Platform.environment(),
					workingDirectory, pty);
			streamsProxy = new StreamsProxy(process, encoding);
			terminalToRemoteStream = new BufferedOutputStream(
					new TerminalOutputStream(streamsProxy, encoding), 1024);
			
			addListener(streamsProxy.getOutputStreamMonitor(),
					new TerminalOutputListener());

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			String str = "python /data/users/zwqu/Desktop/rfsim/rfsim.py\n";
			sendString(str);
			xmlView = (XMLView) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.findView(Constants.VIEW_XML);
			xmlView.setAllValue("0");
			DebugState.setState(2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void sendString(String strArray) throws IOException {
		byte[] bytes = strArray.getBytes(encoding);
		terminalToRemoteStream.write(bytes);
		terminalToRemoteStream.flush();
	}

	private void addListener(IStreamMonitor monitor, IStreamListener listener) {
		monitor.addListener(listener);
		listener.streamAppended(monitor.getContents(), monitor);
	}

	static Process systemProcess() {
		return process;
	}

	@Override
	public boolean isEnabled() {
		if (DebugState.getState() == 1)
			return true;
		else
			return false;
	}
}
