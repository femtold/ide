package org.radioflex.ide.project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.radioflex.ide.Constants;


public class ASMVistor implements IResourceProxyVisitor, IResourceDeltaVisitor {
	public boolean visit(IResourceProxy proxy) throws CoreException {
		String name = proxy.getName();
		if (name != null && name.endsWith(".asm")) {
			// found a source file
		    System.out.println("Processing " + name);
			processResource(proxy.requestResource());

		}
		return true;
	}

	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		if (resource.getName().endsWith(".asm")) {
			processResource(resource);
		}
		return true;

	}
	
	private void processResource(IResource resource) throws CoreException {
		if (resource instanceof IFile) {
			try {
				IFile file = (IFile) resource;
				String masmName = file.getName().replace(".asm", ".masm");
				IContainer container = file.getParent();
				IFile masmFile = container.getFile(new Path(masmName));

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				InputStream in = file.getContents();
				ASMTranslator.convert(new InputStreamReader(in),
						new OutputStreamWriter(baos));
				ByteArrayInputStream contents =
						new ByteArrayInputStream(baos.toByteArray());
				if (masmFile.exists()) {
					masmFile.setContents(contents, true, false, null);
					} else {
					masmFile.create(contents, true, null);
					}
				masmFile.setDerived(true,null);


			} catch (IOException e) {
				throw new CoreException(new Status(Status.ERROR,
						Constants.PLUGIN_ID, "Failed to generate resource", e));
			}
		}
	}
}
