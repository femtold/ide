package com.radioflex.ide.editor;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.link.LinkedPosition;
import org.eclipse.jface.text.source.ISourceViewer;


/**
 * 
 * The implementation for text hover.
 *
 */
public class ASMTextHover implements ITextHover {

	private static String[][] InstructionArray = null;
	private static String[][] SegmentArray = null;
	private static String[][] MacrosArray = null;
	private static String[][] DerivativeArray = null;
	private static String[][] RegisterArray = null;

	public ASMTextHover(ISourceViewer sourceViewer) {
		Assert.isNotNull(sourceViewer);
	}

	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		LinkedPosition p = new LinkedPosition(textViewer.getDocument(),
				hoverRegion.getOffset(), hoverRegion.getLength());
		String text = null;
		try {
			text = p.getContent();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		InstructionArray = ASMInstructionSet.getSortedInstructionArray();
		SegmentArray = ASMInstructionSet.getSortedSegmentArray();
		MacrosArray = ASMInstructionSet.getSortedMacrosArray();
		DerivativeArray = ASMInstructionSet.getSortedDerivativeArray();
		RegisterArray = ASMInstructionSet.getSortedRegisterArray();

		for (int i = 0; i < InstructionArray.length; i++)
			if (text.equals(InstructionArray[i][0])
					|| text.equals(InstructionArray[i][1]))
				return InstructionArray[i][2];
		for (int i = 0; i < SegmentArray.length; i++)
			if (text.equals(SegmentArray[i][0])
					|| text.equals(SegmentArray[i][1]))
				return SegmentArray[i][2];
		for (int i = 0; i < MacrosArray.length; i++)
			if (text.equals(MacrosArray[i][0])
					|| text.equals(MacrosArray[i][1]))
				return MacrosArray[i][2];
		for (int i = 0; i < DerivativeArray.length; i++)
			if (text.equals(DerivativeArray[i][0])
					|| text.equals(DerivativeArray[i][1]))
				return DerivativeArray[i][2];
		for (int i = 0; i < RegisterArray.length; i++)
			if (text.equals(RegisterArray[i][0])
					|| text.equals(RegisterArray[i][1]))
				return RegisterArray[i][2];
		return null;
	}

	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		return findWord(textViewer.getDocument(), offset);
	}

	public IRegion findWord(IDocument document, int offset) {
		int start = -2;
		int end = -1;

		try {
			int pos = offset;
			char c;

			while (pos >= 0) {
				c = document.getChar(pos);
				if (isDelimiter(c))
					break;
				--pos;
			}

			start = pos;
			pos = offset;
			int length = document.getLength();

			while (pos < length) {
				c = document.getChar(pos);
				if (isDelimiter(c))
					break;
				++pos;
			}

			end = pos;
		} catch (BadLocationException e) {
			e.printStackTrace();
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
	
	private boolean isDelimiter(char c){
		if(Character.isWhitespace(c) || c == ',' || c == '#')
			return true;
		else return false;
	}

}
