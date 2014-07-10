package com.radioflex.ide.ui.editor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class RColorProvider {
	public static final RGB MACROS = new RGB(0, 155, 255);
	public static final RGB SINGLE_LINE_COMMENT = new RGB(130, 0, 255);
	public static final RGB REGISTER = new RGB(255, 125, 0);
	public static final RGB DERIVATIVE = new RGB(20, 89, 200);
	public static final RGB INSTRUCTIONS = new RGB(15, 148, 15);
	public static final RGB STRING = new RGB(148, 50, 50);
	public static final RGB DEFAULT = new RGB(0, 0, 0);

	protected Map<RGB, Color> fColorTable = new HashMap<RGB, Color>(7);

	public void dispose() {
		Iterator<Color> e = fColorTable.values().iterator();
		while (e.hasNext())
			((Color) e.next()).dispose();
	}

	public Color getColor(RGB rgb) {
		Color color = (Color) fColorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}
}
