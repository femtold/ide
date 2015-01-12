package com.radioflex.ide.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.model.WorkbenchViewerComparator;

import com.radioflex.ide.Activator;
import com.radioflex.ide.Messages;

/** Preferences Page for Syntax Highlight. */
public class PreferenceHighlighting extends PreferencePage implements
		IWorkbenchPreferencePage {

	/** Color list content provider. */
	private class ColorListContentProvider implements
			IStructuredContentProvider {

		/** {@inheritDoc} */
		public void dispose() {
		}

		/** {@inheritDoc} */
		public Object[] getElements(Object inputElement) {
			return ((java.util.List<?>) inputElement).toArray();
		}

		/** {@inheritDoc} */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	/** Color list label provider. */
	private class ColorListLabelProvider extends LabelProvider implements
			IColorProvider, IFontProvider {

		/** {@inheritDoc} */
		public Color getBackground(Object element) {
			return null;
		}

		/** {@inheritDoc} */
		public Color getForeground(Object element) {
			return ((HighlightingColorListItem) element).getfItemColor();
		}

		/** {@inheritDoc} */
		public String getText(Object element) {
			return ((HighlightingColorListItem) element).getfDisplayName();
		}

		/** @return font to preview style in the table viewer */
		@Override
		public Font getFont(Object element) {
			HighlightingColorListItem item = (HighlightingColorListItem) element;
			int style;
			style = item.getfItemBold() ? SWT.BOLD : SWT.NORMAL;
			if (item.getfItemItalic()) {
				style |= SWT.ITALIC;
			}

			IPreferenceStore store = Activator.getDefault()
					.getPreferenceStore();
			FontData fd = PreferenceConverter.getFontData(store,
					JFaceResources.TEXT_FONT);
			fd.setStyle(style);
			return new Font(Display.getDefault(), fd);
		}
	}

	/** Item in the highlighting color list. */
	private static class HighlightingColorListItem {

		/** Display name */
		private String fDisplayName;

		/** Color preference key */
		private String fItemKey;

		/** Item color */
		private Color fItemColor;

		/** Item bold */
		private boolean fItemBold;

		/** Item italic */
		private boolean fItemItalic;

		/** Item value changes */
		private boolean change = false;

		/** Initialize the item with the given values.
		 * 
		 * @param displayName
		 *            the display name
		 * @param itemKey
		 *            the color preference key
		 * @param itemColor
		 *            the item color
		 * @param itemBold
		 *            the item bold
		 * @param itemItalic
		 *            the item italic */
		public HighlightingColorListItem(String displayName, String itemKey,
				Color itemColor, boolean itemBold, boolean itemItalic) {
			fDisplayName = displayName;
			fItemKey = itemKey;
			fItemColor = itemColor;
			fItemBold = itemBold;
			fItemItalic = itemItalic;
		}

		public String getfDisplayName() {
			return fDisplayName;
		}

		public Color getfItemColor() {
			return fItemColor;
		}

		public String getfItemKey() {
			return fItemKey;
		}

		/** @return boolean: whether the value has been changed (different from
		 *         default) */
		public boolean hasChanged() {
			return change;
		}

		public boolean getfItemBold() {
			return fItemBold;
		}

		public boolean getfItemItalic() {
			return fItemItalic;
		}

		public void setfItemColor(Color color) {
			fItemColor = color;
			change = true;
		}

		public void setfItemColor(RGB rgb) {
			this.fItemColor = new Color(Display.getCurrent(), rgb);
			change = true;
		}

		public void setfItemBold(boolean fItemBold) {
			this.fItemBold = fItemBold;
			change = true;
		}

		public void setfItemItalic(boolean fItemItalic) {
			this.fItemItalic = fItemItalic;
			change = true;
		}

	}

	/** TextAttribute Preference Constants */
	public static final String PREF_TEXTATTR_MACRO = "preferences.textattribute.macro"; //$NON-NLS-1$
	public static final String PREF_TEXTATTR_INSTRUCTION = "preferences.textattribute.instruction"; //$NON-NLS-1$
	public static final String PREF_TEXTATTR_SEGMENT = "preferences.textattribute.segment"; //$NON-NLS-1$
	public static final String PREF_TEXTATTR_DERIVATIVE = "preferences.textattribute.derivative"; //$NON-NLS-1$
	public static final String PREF_TEXTATTR_REGISTER = "preferences.textattribute.register"; //$NON-NLS-1$
	public static final String PREF_TEXTATTR_STRING = "preferences.textattribute.string"; //$NON-NLS-1$
	public static final String PREF_TEXTATTR_COMMENT = "preferences.textattribute.comment"; //$NON-NLS-1$
	public static final String PREF_TEXTATTR_NUMERIC = "preferences.textattribute.numeric"; //$NON-NLS-1$

	/** The keys of the overlay store. */
	private final String[][] fSyntaxColorListModel = new String[][] {
			{ Messages.PREF_TEXTCOLOR_COMMENT_NAME, PREF_TEXTATTR_COMMENT },
			{ Messages.PREF_TEXTCOLOR_MACRO_NAME, PREF_TEXTATTR_MACRO },
			{ Messages.PREF_TEXTCOLOR_SEGMENT_NAME, PREF_TEXTATTR_SEGMENT },
			{ Messages.PREF_TEXTCOLOR_INSTRUCTION_NAME, PREF_TEXTATTR_INSTRUCTION },
			{ Messages.PREF_TEXTCOLOR_DERIVATIVE_NAME, PREF_TEXTATTR_DERIVATIVE },
			{ Messages.PREF_TEXTCOLOR_REGISTER_NAME, PREF_TEXTATTR_REGISTER },
			{ Messages.PREF_TEXTCOLOR_NUMERIC_NAME, PREF_TEXTATTR_NUMERIC },
			{ Messages.PREF_TEXTCOLOR_STRING_NAME, PREF_TEXTATTR_STRING } };

	/** Color selector for foreground color. */
	private ColorSelector fSyntaxForegroundColorSelector;

	/** Check box for bold preference. */
	private Button fBoldCheckBox;

	/** Check box for italic preference. */
	private Button fItalicCheckBox;

	/** List for text attribute items. */
	private final List<HighlightingColorListItem> fHighlightingColorList = new ArrayList<HighlightingColorListItem>();

	/** Highlighting color list viewer */
	private TableViewer fHighlightingColorListViewer;

	/** Creates a new preference page. */
	public PreferenceHighlighting() {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	/** {@inheritDoc} */
	protected Control createContents(Composite parent) {
		Composite contents = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		contents.setLayout(layout);
		contents.setLayoutData(new GridData(GridData.FILL_BOTH));

		createSyntaxPage(contents);
		initialize();
		Dialog.applyDialogFont(contents);
		return contents;
	}

	/** Create all visual controls.
	 * 
	 * @param parent
	 *            The parent object.
	 * @return A control object. */
	private Control createSyntaxPage(Composite parent) {
		/* !!!!!!!!!!!!!!!! unsolved: text wrapping */
		// first line of explanation
		Label explanLabel = new Label(parent, SWT.LEFT);
		explanLabel.setText(Messages.PREF_SYNTAXHIGHLIGHT_TITLE);
		// Second line of explanation
		explanLabel = new Label(parent, SWT.LEFT);
		explanLabel.setText("Default Colors and Font can be configured on the");

		{
			// Third line of explanation
			Composite cComposite = new Composite(parent, SWT.NONE);
			RowLayout clayout = new RowLayout();
			clayout.marginHeight = 0;
			clayout.marginWidth = 0;
			clayout.spacing = 0;
			cComposite.setLayout(clayout);

			// link to "Text Editors" preference page
			Link linkTextEditors = new Link(cComposite, SWT.NONE);
			String linkLabel = String.format("<a href=\"null\">%s</a>",
					Messages.PREF_LINK_TEXTEDITORS);
			linkTextEditors.setText(linkLabel);
			linkTextEditors.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					String pid = "org.eclipse.ui.preferencePages.GeneralTextEditor";
					PreferenceDialog dialog = PreferencesUtil
							.createPreferenceDialogOn(Display.getDefault()
									.getActiveShell(), pid, null, null);
					dialog.open();
				}
			});
			explanLabel = new Label(cComposite, SWT.NONE);
			explanLabel.setText(" and ");
			// link to "Colors and Fonts" preference page
			Link linkColorsAndFonts = new Link(cComposite, SWT.NONE);
			linkLabel = String.format("<a href=\"null\">%s</a>",
					Messages.PREF_LINK_COLORSANDFONTS);
			linkColorsAndFonts.setText(linkLabel);
			linkColorsAndFonts.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					String pid = "org.eclipse.ui.preferencePages.ColorsAndFonts";
					PreferenceDialog dialog = PreferencesUtil
							.createPreferenceDialogOn(Display.getDefault()
									.getActiveShell(), pid, null, null);
					dialog.open();
				}
			});
			explanLabel = new Label(cComposite, SWT.NONE);
			explanLabel.setText(" pages.\n ");
		}

		// editorComposite = table viewer(left) + selection boxes(right)
		Composite editorComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		editorComposite.setLayout(layout);
		GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gd.horizontalSpan = 2;
		editorComposite.setLayoutData(gd);

		// (1,1)label: Element
		Label label = new Label(editorComposite, SWT.NULL);
		label.setText(Messages.PREF_ELEMENT_LABEL + ":");

		// (1,2)Empty label: unused grid
		label = new Label(editorComposite, SWT.NULL);
		label.setText(" ");

		{
			// (2,1)viewer
			fHighlightingColorListViewer = new TableViewer(editorComposite,
					SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
			fHighlightingColorListViewer
					.setLabelProvider(new ColorListLabelProvider());
			fHighlightingColorListViewer
					.setContentProvider(new ColorListContentProvider());
			fHighlightingColorListViewer
					.setComparator(new WorkbenchViewerComparator());

			gd = new GridData(SWT.BEGINNING, SWT.FILL, false, true);
			gd.heightHint = convertHeightInCharsToPixels(10);
			gd.widthHint = convertHeightInCharsToPixels(6);
			fHighlightingColorListViewer.getControl().setLayoutData(gd);

			fHighlightingColorListViewer
					.addSelectionChangedListener(new ISelectionChangedListener() {
						public void selectionChanged(SelectionChangedEvent event) {
							handleSyntaxColorListSelection();
						}
					});
		}

		// (2,2) stylesComposite: color and styles
		Composite stylesComposite = new Composite(editorComposite, SWT.NONE);
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		stylesComposite.setLayout(layout);
		stylesComposite.setLayoutData(new GridData(SWT.BEGINNING,
				SWT.BEGINNING, false, false));

		// (2,2)(1,1)foreground color label
		label = new Label(stylesComposite, SWT.LEFT);
		label.setText(Messages.PREF_FOREGROUNDCOLOR_LABEL + ":");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent = 20;
		label.setLayoutData(gd);

		{
			// (2,2)(1,2)foreground color selector
			fSyntaxForegroundColorSelector = new ColorSelector(stylesComposite);
			Button foregroundColorButton = fSyntaxForegroundColorSelector
					.getButton();
			gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
			foregroundColorButton.setLayoutData(gd);

			foregroundColorButton.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					HighlightingColorListItem item = getHighlightingColorListItem();
					item.setfItemColor(fSyntaxForegroundColorSelector
							.getColorValue());
					fHighlightingColorListViewer
							.setInput(fHighlightingColorList);
				}
			});
		}
		{
			// (2,2)(2,1)(1,1)bold check box
			fBoldCheckBox = new Button(stylesComposite, SWT.CHECK);
			fBoldCheckBox.setText(Messages.PREF_BOLD_LABEL);
			gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
			gd.horizontalIndent = 20;
			gd.horizontalSpan = 2;
			fBoldCheckBox.setLayoutData(gd);

			fBoldCheckBox.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					HighlightingColorListItem item = getHighlightingColorListItem();
					item.setfItemBold(fBoldCheckBox.getSelection());
					fHighlightingColorListViewer
							.setInput(fHighlightingColorList);
				}
			});
		}
		{
			// (2,2)(2,1)(2,1)italic check box
			fItalicCheckBox = new Button(stylesComposite, SWT.CHECK);
			fItalicCheckBox.setText(Messages.PREF_ITALIC_LABEL);
			gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
			gd.horizontalIndent = 20;
			// gd.horizontalSpan = 2;
			fItalicCheckBox.setLayoutData(gd);

			fItalicCheckBox.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					HighlightingColorListItem item = getHighlightingColorListItem();
					item.setfItemItalic(fItalicCheckBox.getSelection());
					fHighlightingColorListViewer
							.setInput(fHighlightingColorList);
				}
			});
		}
		parent.layout();
		return parent;
	}

	/** Returns the current highlighting color list item.
	 * 
	 * @return the current highlighting color list item */
	private HighlightingColorListItem getHighlightingColorListItem() {
		IStructuredSelection selection = (IStructuredSelection) fHighlightingColorListViewer
				.getSelection();
		return (HighlightingColorListItem) selection.getFirstElement();
	}

	/** Update controls after item select. */
	private void handleSyntaxColorListSelection() {
		HighlightingColorListItem item = getHighlightingColorListItem();

		Color color = null;
		boolean bold = false;
		boolean italic = false;

		// check for previous changes
		if (!item.hasChanged()) {
			// item.hasChanged()=false -> item didn't change -> remain current
			String data = Activator.getDefault().getPreferenceStore()
					.getString(item.getfItemKey());
			color = TextAttributeConverter.preferenceDataToColorAttribute(data);
			bold = TextAttributeConverter.preferenceDataToBoldAttribute(data);
			italic = TextAttributeConverter
					.preferenceDataToItalicAttribute(data);
		} else {
			// item.hasChanged()=true -> item has changed -> set to new values
			color = item.getfItemColor();
			bold = item.getfItemBold();
			italic = item.getfItemItalic();
		}

		fSyntaxForegroundColorSelector.setColorValue(color.getRGB());
		fBoldCheckBox.setSelection(bold);
		fItalicCheckBox.setSelection(italic);

		fSyntaxForegroundColorSelector.getButton().setEnabled(true);
		fBoldCheckBox.setEnabled(true);
		fItalicCheckBox.setEnabled(true);

	}

	/** {@inheritDoc} */
	public void init(IWorkbench workbench) {
	}

	/** Setup the text attribute list. */
	private void initialize() {
		String data = "";
		Color color = null;
		boolean bold = false;
		boolean italic = false;

		for (int i = 0, n = fSyntaxColorListModel.length; i < n; i++) {
			data = Activator.getDefault().getPreferenceStore()
					.getString(fSyntaxColorListModel[i][1]);
			color = TextAttributeConverter.preferenceDataToColorAttribute(data);
			bold = TextAttributeConverter.preferenceDataToBoldAttribute(data);
			italic = TextAttributeConverter
					.preferenceDataToItalicAttribute(data);
			fHighlightingColorList.add(new HighlightingColorListItem(
					fSyntaxColorListModel[i][0], fSyntaxColorListModel[i][1],
					color, bold, italic));
		}
		fHighlightingColorListViewer.setInput(fHighlightingColorList);
		fHighlightingColorListViewer.setSelection(new StructuredSelection(
				fHighlightingColorListViewer.getElementAt(0)));
	}

	/** {@inheritDoc} */
	@Override
	public void performApply() {
		saveChanges();
		super.performApply();
	}

	/** {@inheritDoc} */
	@Override
	public void performDefaults() {
		HighlightingColorListItem item;
		String itemKey;
		String prefData = "";
		boolean defaultFind = true;
		Color color = null;
		boolean bold = false;
		boolean italic = false;

		for (int i = 0, n = fSyntaxColorListModel.length; i < n; i++) {
			item = fHighlightingColorList.get(i);
			itemKey = item.fItemKey;
			if (itemKey.equals(PREF_TEXTATTR_COMMENT)) {
				prefData = PreferencesInitializer.PREF_TEXTATTR_COMMENT_DEFAULT;
			} else if (itemKey.equals(PREF_TEXTATTR_INSTRUCTION)) {
				prefData = PreferencesInitializer.PREF_TEXTATTR_INSTRUCTION_DEFAULT;
			} else if (itemKey.equals(PREF_TEXTATTR_REGISTER)) {
				prefData = PreferencesInitializer.PREF_TEXTATTR_REGISTER_DEFAULT;
			} else if (itemKey.equals(PREF_TEXTATTR_NUMERIC)) {
				prefData = PreferencesInitializer.PREF_TEXTATTR_NUMERIC_DEFAULT;
			} else if (itemKey.equals(PREF_TEXTATTR_SEGMENT)) {
				prefData = PreferencesInitializer.PREF_TEXTATTR_SEGMENT_DEFAULT;
			} else if (itemKey.equals(PREF_TEXTATTR_DERIVATIVE)) {
				prefData = PreferencesInitializer.PREF_TEXTATTR_DERIVATIVE_DEFAULT;
			} else if (itemKey.equals(PREF_TEXTATTR_MACRO)) {
				prefData = PreferencesInitializer.PREF_TEXTATTR_MACRO_DEFAULT;
			} else if (itemKey.equals(PREF_TEXTATTR_STRING)) {
				prefData = PreferencesInitializer.PREF_TEXTATTR_STRING_DEFAULT;
			} else {
				defaultFind = false;
			}

			if (defaultFind) {
				color = TextAttributeConverter
						.preferenceDataToColorAttribute(prefData);
				bold = TextAttributeConverter
						.preferenceDataToBoldAttribute(prefData);
				italic = TextAttributeConverter
						.preferenceDataToItalicAttribute(prefData);
				item.setfItemColor(color);
				item.setfItemBold(bold);
				item.setfItemItalic(italic);
			}
		}
		fHighlightingColorListViewer.setInput(fHighlightingColorList);

		item = getHighlightingColorListItem();
		fSyntaxForegroundColorSelector.setColorValue(item.getfItemColor()
				.getRGB());
		fBoldCheckBox.setSelection(item.getfItemBold());
		fItalicCheckBox.setSelection(item.getfItemItalic());

		// add performApply() to make changes valid (flushed into preference
		// store & visible in sourceViewer) immediately
		// performApply();
	}

	/** {@inheritDoc} */
	public boolean performOk() {
		saveChanges();
		return super.performOk();
	}

	/** Save all changes to preferencesStore. */
	private void saveChanges() {
		String data = "";
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		boolean changes = false;
		for (HighlightingColorListItem hcli : fHighlightingColorList) {
			if (hcli.hasChanged()) {
				data = TextAttributeConverter.textAttributesToPreferenceData(
						hcli.getfItemColor(), hcli.getfItemBold(),
						hcli.getfItemItalic());
				store.setValue(hcli.getfItemKey(), data);
				changes = true;
			}
		}
		if (changes) {
			try {
				ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID)
						.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
