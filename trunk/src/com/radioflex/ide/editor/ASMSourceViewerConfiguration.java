package com.radioflex.ide.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import com.radioflex.ide.Constants;

/**
 * Configurations for the ASMEditor. Provides syntax highlighting and text hover. 
 * 
 *
 */
public class ASMSourceViewerConfiguration extends SourceViewerConfiguration {
	
	/**
	 * The underlying ASM Editor.
	 */
	private ASMEditor editor;
	
	/**
	 * Rule scanner for default ASM code.
	 */
	private ASMCodeScanner asmcodescanner = null;
	
	/**
	 * Rule scanner for comment and string parts.
	 */
	private PropertyChangeRuleBaseScanner[] scanner = new PropertyChangeRuleBaseScanner[2];
	
	/**
	 * The constructor.
	 * @param editor The underlying ASM Editor.
	 */
	public ASMSourceViewerConfiguration(ASMEditor editor){
		this.editor = editor;
	}

	/**
	 * {@inheritDoc}}
	 */
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer){
		PresentationReconciler reconciler = new PresentationReconciler();
		
		asmcodescanner = new ASMCodeScanner(editor);
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(asmcodescanner);
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		scanner[0] = new PropertyChangeRuleBaseScanner(editor,Constants.PREFERENCES_TEXTCOLOR_COMMENT);
		dr = new DefaultDamagerRepairer(scanner[0]);
		reconciler.setDamager(dr, Constants.PARTITION_COMMENT);
		reconciler.setRepairer(dr,  Constants.PARTITION_COMMENT);
		
		scanner[1] = new PropertyChangeRuleBaseScanner(editor,Constants.PREFERENCES_TEXTCOLOR_STRING);
		dr = new DefaultDamagerRepairer(scanner[1]);
		reconciler.setDamager(dr, Constants.PARTITION_STRING);
		reconciler.setRepairer(dr,  Constants.PARTITION_STRING);
		
		return reconciler;
	}
	
	/**
	 * {@inheritDoc}}
	 */
	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType){
		return new ASMTextHover(sourceViewer);
	}
	
	public void dispose(){
		if(asmcodescanner != null)
			asmcodescanner.dispose();
		for(int i = 0; i < scanner.length; i++)
			if(scanner[i] != null)
				scanner[i].dispose();
	}
}
