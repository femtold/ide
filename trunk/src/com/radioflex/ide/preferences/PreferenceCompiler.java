package com.radioflex.ide.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.radioflex.ide.Activator;
import com.radioflex.ide.Constants;
import com.radioflex.ide.Messages;

/**
 * Preferences Page for compiler.
 *
 */
public class PreferenceCompiler extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	/**
	 * The constructor.
	 */
	public PreferenceCompiler() {
		super(FieldEditorPreferencePage.GRID);  //use GRID-Layout

		noDefaultAndApplyButton();  //disable Default and Apply

		setPreferenceStore(Activator.getDefault().getPreferenceStore());  //set
	}

	/**
	 * {@inheritDoc}}
	 */
	public void init(IWorkbench workbench) {

	}

	/**
	 * {@inheritDoc}}
	 */
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();

		//Field for Compiler-Executable
		FileFieldEditor compiler = new FileFieldEditor(
				Constants.PREFERENCES_COMPILER_NAME, Messages.COMPILER_NAME
						+ ": ", true, parent);

		addField(compiler);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;

		Label label = new Label(parent, SWT.LEFT);
		label.setText(Messages.PARAMS_TEMPLATE_COMPILER + "\n ");
		label.setLayoutData(gd);

		//Field for parameters to use Compiler-Executable
		MultiLineStringFieldEditor params1 = new MultiLineStringFieldEditor(
				Constants.PREFERENCES_COMPILER_PARAMS_1, Messages.PARAMS_NAME_1
						+ ": ", parent);
		addField(params1);
		
		MultiLineStringFieldEditor params2 = new MultiLineStringFieldEditor(
				Constants.PREFERENCES_COMPILER_PARAMS_2, Messages.PARAMS_NAME_2
						+ ": ", parent);
		addField(params2);
	}

}
