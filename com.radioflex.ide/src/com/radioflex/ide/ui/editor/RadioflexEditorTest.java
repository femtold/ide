package com.radioflex.ide.ui.editor;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;

public class RadioflexEditorTest {
	private RColorProvider colorprovider;
	private RESourceViewerConfiguration sourceviewerconfiguration;
	private ISourceViewer sourceviewer;
	private IPresentationReconciler rc;
	
	@Before
	public void setUp() throws Exception {
		colorprovider = new RColorProvider();
		sourceviewerconfiguration = new RESourceViewerConfiguration();
	}

	// Test class ColorProvider
	@Test
	public void disposeTest() {
		colorprovider.dispose();
	}

	@Test
	public void getcolorTest() {
		Color macros = colorprovider.getColor(RColorProvider.MACROS);
		Color derivative = colorprovider.getColor(RColorProvider.DERIVATIVE);
		Color register = colorprovider.getColor(RColorProvider.REGISTER);
		assertEquals(
				new Color(Display.getCurrent(), RColorProvider.MACROS),
				macros);
		assertEquals(new Color(Display.getCurrent(), RColorProvider.DERIVATIVE),
				derivative);
		assertEquals(
				new Color(Display.getCurrent(), RColorProvider.REGISTER),
				register);
	}

	//Test class RESourceViewerConfiguration
	
	@Test
	public void getPresentationReconciler(){
		rc = sourceviewerconfiguration.getPresentationReconciler(sourceviewer);
	}
}
