package com.radioflex.ide.preferences;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * A multi line StringFieldEditor.
 * 
 */
@SuppressWarnings("deprecation")
public class MultiLineStringFieldEditor extends StringFieldEditor {

	private Text textFiledML = null;

	private int validateStrategyML = VALIDATE_ON_KEY_STROKE;

	private int textLimitML = UNLIMITED;

	/**
	 * Create a multi-line string field editor. Use the method
	 * <code>setTextLimit</code> to limit the text.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on.
	 * @param labelText
	 *            the label text of the field editor.
	 * @param width
	 *            the width of the text input field in characters, or UNLIMITED
	 *            for no limit.
	 * @param strategy
	 *            either <code>VALIDATE_ON_KEY_STROKE</code> to perform on the
	 *            fly checking(the default), or
	 *            <code>VALIDATE_ON_FOCUS_LOST<code> to 
	 *    perform validation only after the text has been typed in.
	 * @param parent
	 *            the parent of the field editor's control.
	 */
	public MultiLineStringFieldEditor(String name, String labelText, int width,
			int strategy, Composite parent) {
		super(name, labelText, width, strategy, parent);
		setValidateStrategy(strategy);
	}

	/**
	 * Create a multi-line string field editor. Use the method
	 * <code>setTextLimit</code> to limit the text.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param width
	 *            the width of text input field in characters, or
	 *            <code>UNLIMITED</code> for no limit
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public MultiLineStringFieldEditor(String name, String labelText, int width,
			Composite parent) {
		this(name, labelText, width, VALIDATE_ON_KEY_STROKE, parent);
	}

	/**
	 * 
	 * Create a multi-line string field editor. Use the method
	 * <code>setTextLimit</code> to limit the text.
	 * 
	 * @param name the name of the preference this field editor works on
	 * @param labelText the label text of the field editor
	 * @param parent the parent of the field editor's control
	 */
	public MultiLineStringFieldEditor(String name, String labelText,
			Composite parent) {
		this(name, labelText, UNLIMITED, parent);
	}

	/**
	 * {@inheritDoc}}
	 */
	public void setValidateStrategy(int value) {
		super.setValidateStrategy(value);
		Assert.isTrue(value == VALIDATE_ON_KEY_STROKE
				|| value == VALIDATE_ON_FOCUS_LOST);
		validateStrategyML = value;
	}

	/**
	 * {@inheritDoc}}
	 */
	public void setTextLimit(int limit) {
		super.setTextLimit(limit);
		textLimitML = limit;
	}

	/**
	 * {@inheritDoc}}
	 */
	public Text getTextControl(Composite parent) {
		textFiledML = super.getTextControl();
		if (textFiledML == null) {
			textFiledML = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP
					| SWT.LEFT | SWT.V_SCROLL);
			textFiledML.setFont(parent.getFont());
			switch (validateStrategyML) {
			case VALIDATE_ON_FOCUS_LOST:
				textFiledML.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						clearErrorMessage();
					}
				});
				textFiledML.addFocusListener(new FocusAdapter() {
					public void focusGained(FocusEvent e) {
						refreshValidState();
					}

					public void focusLost(FocusEvent e) {
						valueChanged();
						clearErrorMessage();
					}
				});
				break;
			case VALIDATE_ON_KEY_STROKE:
				textFiledML.addKeyListener(new KeyAdapter() {
					public void keyReleased(KeyEvent e) {
						valueChanged();
					}
				});
				break;
			default:
				Assert.isTrue(false, "Unknown validate strategy");
			}

			textFiledML.addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent e) {
					textFiledML = null;
				}

			});

			if (textLimitML > 0)
				textFiledML.setTextLimit(textLimitML);
		} else {
			checkParent(textFiledML, parent);
		}

		return textFiledML;
	}

	/**
	 * {@inheritDoc}}
	 */
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		super.doFillIntoGrid(parent, numColumns);

		textFiledML = super.getTextControl();
		GridData gd = (GridData) textFiledML.getLayoutData();
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		textFiledML.setLayoutData(gd);

		Label label = getLabelControl(parent);
		gd = new GridData();
		gd.verticalAlignment = SWT.TOP;
		label.setLayoutData(gd);
	}

}
