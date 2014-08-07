package com.radioflex.ide.ui.editor;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultTextHover;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.link.LinkedPosition;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.ISelectionListener;

public class RTextHover implements ITextHover {

	private String[][] instructionArray;
	private String[][] segmentArray;
	private String[][] macrosArray;
	private String[][] derivativesArray;
	private String[][] registerArray;
	private ISourceViewer fSourceViewer;
 
	public RTextHover(ISourceViewer sourceViewer) {
		Assert.isNotNull(sourceViewer);
		fSourceViewer = sourceViewer;
	}

	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		return findWord(textViewer.getDocument(), offset);
	}

	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		LinkedPosition p = new LinkedPosition(textViewer.getDocument(),
				hoverRegion.getOffset(), hoverRegion.getLength());
		String text = null;
		try {
			text = p.getContent();
			System.out.println("get the hover text is "+text);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		// IDocument doc = textViewer.getDocument();
		instructionArray = SyntaxKeywords.getInstructionArray();
		segmentArray = SyntaxKeywords.getSegmentArray();
		macrosArray = SyntaxKeywords.getMacrosArray();
		derivativesArray = SyntaxKeywords.getDerivativeArray();
		registerArray = SyntaxKeywords.getRegisterArray();

		String info = new String();

		for (int i = 0; i < instructionArray.length; i++) {
			if (text.equals(instructionArray[i][0])
					|| text.equals(instructionArray[i][1])) {
				info = instructionArray[i][2];
			}
		}
		for (int i = 0; i < segmentArray.length; i++) {
			if (text.equals(segmentArray[i][0])
					|| text.equals(segmentArray[i][1])) {
				info = segmentArray[i][2];
			}
		}
		for (int i = 0; i < macrosArray.length; i++) {
			if (text.equals(macrosArray[i][0])
					|| text.equals(macrosArray[i][1])) {
				info = macrosArray[i][2];
			}
		}
		for (int i = 0; i < derivativesArray.length; i++) {
			if (text.equals(derivativesArray[i][0])
					|| text.equals(derivativesArray[i][1])) {
				info = derivativesArray[i][2];
			}
		}
		for (int i = 0; i < registerArray.length; i++) {
			if (text.equals(registerArray[i][0])
					|| text.equals(registerArray[i][1])) {
				info = registerArray[i][2];
			}
		}
		return info;
	}

	private IRegion findWord(IDocument document, int offset) {
		int start = -2;
		int end = -1;

		try {

			int pos = offset;
			char c;

			while (pos >= 0) {
				c = document.getChar(pos);
				// if (!Character.isUnicodeIdentifierPart(c))
				if (Character.isWhitespace(c))
					break;
				--pos;
			}

			start = pos;

			pos = offset;
			int length = document.getLength();

			while (pos < length) {
				c = document.getChar(pos);
				// if (!Character.isUnicodeIdentifierPart(c))
				if (Character.isWhitespace(c))
					break;
				++pos;
			}

			end = pos;

		} catch (BadLocationException x) {
		}

		if (start >= -1 && end > -1) {
			if (start == offset && end == offset)
				return new Region(offset, 0);
			else if (start == offset)
				return new Region(start, end - start);
			else
				return new Region(start + 1, end - start - 1);
		}

		return null;
	}

}