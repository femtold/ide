package com.radioflex.ide.preferences;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.radioflex.ide.Activator;
import com.radioflex.ide.Constants;
import com.radioflex.ide.Messages;
import com.radioflex.ide.editor.ASMInstructionSet;

/**
 * Preferences Page for Keys change.
 *
 */
public class PreferenceKeysChange extends PreferencePage implements
		IWorkbenchPreferencePage {

	public static final String PREFERENCES_KEYWORD_CHANGE = "preferences.keyword.change";
	
	// the keys tree.
	private Tree tree;

	// the Array of key and description.
	private String[][] InstructionArray = null;
	private String[][] SegmentArray = null;
	private String[][] MacrosArray = null;
	private String[][] DerivativeArray = null;
	private String[][] RegisterArray = null;

	private Text descriptionText;
	private Text instructionSetText;

	private TreeItem instructionRoot;
	private TreeItem segmetRoot;
	private TreeItem macrosRoot;
	private TreeItem derivativesRoot;
	private TreeItem registerRoot;

	private static int index = 1;
	private ArrayList<String> KList;
	private ArrayList<String> DList;

	private Combo KeywordsCombo;

	/**
	 * The constructor
	 */
	public PreferenceKeysChange() {
		noDefaultAndApplyButton();
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {
		this.setDescription(Messages.KEYWORD_DESCRIPTION + "\n");
	}

	@Override
	protected Control createContents(Composite parent) {

		Composite contents = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;

		contents.setLayout(layout);

		initialize();

		createKeyWordPage(contents);

		Dialog.applyDialogFont(contents);

		return contents;
	}

	/**
	 * Get the keys and description from XML file through ASMInstruction.
	 */
	private void initialize() {
		InstructionArray = ASMInstructionSet.getSortedInstructionArray();
		SegmentArray = ASMInstructionSet.getSortedSegmentArray();
		MacrosArray = ASMInstructionSet.getSortedMacrosArray();
		DerivativeArray = ASMInstructionSet.getSortedDerivativeArray();
		RegisterArray = ASMInstructionSet.getSortedRegisterArray();
	}

	private void createKeyWordPage(Composite parent) {

		Label label = new Label(parent, SWT.FILL);
		label.setText(Messages.KEYWORD_TEXT + ":");

		makeTree(parent); // the keys tree

		Label wizardLabel = new Label(parent, SWT.LEFT);
		wizardLabel.setText(Messages.KEYWORD_LABEL_TEXT + ":");

		Composite showComposite = new Composite(parent, SWT.NONE);
		showComposite.setSize(600, 800);
		GridLayout layout = new GridLayout(2, true);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		showComposite.setLayout(layout);

		Label instructionLabel = new Label(showComposite, SWT.LEFT);
		instructionLabel.setText(Messages.KEYWORD_TEXT + ":");

		KeywordsCombo = new Combo(showComposite, SWT.ARROW);
		makeCombo(); // the keys combo box

		Label instructionSetLabel = new Label(showComposite, SWT.LEFT);
		instructionSetLabel.setText(Messages.INSTRUCTIN_SET);
		instructionSetText = new Text(showComposite, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_text.widthHint = 118;
		instructionSetText.setLayoutData(gd_text);

		Label descriptionLabel = new Label(showComposite, SWT.LEFT);
		descriptionLabel.setText(Messages.DESCRIPTION_TEXT + ":");
		descriptionText = new Text(parent, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		descriptionText.setLayoutData(gd);

		KeywordsCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String keys = KeywordsCombo.getText();
				String des = searchKey(keys);
				if (des.substring(des.indexOf("Key"), des.indexOf("Key") + 4)
						.equals("Key1"))
					instructionSetText.setText("Instruction" + "("
							+ des.substring(0, des.indexOf("Key")) + ")");
				else if (des.substring(des.indexOf("Key"),
						des.indexOf("Key") + 4).equals("Key2"))
					instructionSetText.setText("Segment" + "("
							+ des.substring(0, des.indexOf("Key")) + ")");
				else if (des.substring(des.indexOf("Key"),
						des.indexOf("Key") + 4).equals("Key3"))
					instructionSetText.setText("Macros" + "("
							+ des.substring(0, des.indexOf("Key")) + ")");
				else if (des.substring(des.indexOf("Key"),
						des.indexOf("Key") + 4).equals("Key4"))
					instructionSetText.setText("Derivative" + "("
							+ des.substring(0, des.indexOf("Key")) + ")");
				else if (des.substring(des.indexOf("Key"),
						des.indexOf("Key") + 4).equals("Key5"))
					instructionSetText.setText("Register" + "("
							+ des.substring(0, des.indexOf("Key")) + ")");

				descriptionText.setText(des.substring(des.indexOf("Key") + 4,
						des.length()));
			}
		});

		Composite editComposite = new Composite(parent, SWT.NONE);
		GridLayout editLayout = new GridLayout(3, true);
		editComposite.setLayout(editLayout);

		Button editButton = new Button(editComposite, SWT.PUSH);
		editButton.setText(Messages.EDIT_BUTTON_TEXT);
		editButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_BEGINNING));
		editButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (!MessageDialog.openQuestion(null, Messages.CONFIRM,
						Messages.CONFIRM_QUESTION))
					return;
				else {
					if (index == 0)
						index = 1;
					else
						index = 0;
					// edit the keyword
					if (instructionSetText.getText().startsWith("Instruction")) {
						int i = Integer
								.parseInt(instructionSetText.getText()
										.substring(
												12,
												instructionSetText.getText()
														.length() - 1));
						InstructionArray[i][0] = KeywordsCombo.getText();
						InstructionArray[i][1] = KeywordsCombo.getText()
								.toLowerCase();
						InstructionArray[i][2] = descriptionText.getText();
					} else if (instructionSetText.getText().startsWith(
							"Segment")) {
						int i = Integer
								.parseInt(instructionSetText.getText()
										.substring(
												8,
												instructionSetText.getText()
														.length() - 1));
						SegmentArray[i][0] = KeywordsCombo.getText();
						SegmentArray[i][1] = KeywordsCombo.getText()
								.toLowerCase();
						SegmentArray[i][2] = descriptionText.getText();
					} else if (instructionSetText.getText()
							.startsWith("Macros")) {
						int i = Integer
								.parseInt(instructionSetText.getText()
										.substring(
												7,
												instructionSetText.getText()
														.length() - 1));
						MacrosArray[i][0] = KeywordsCombo.getText();
						MacrosArray[i][1] = KeywordsCombo.getText()
								.toLowerCase();
						MacrosArray[i][2] = descriptionText.getText();
					} else if (instructionSetText.getText().startsWith(
							"Derivative")) {
						int i = Integer
								.parseInt(instructionSetText.getText()
										.substring(
												11,
												instructionSetText.getText()
														.length() - 1));
						DerivativeArray[i][0] = KeywordsCombo.getText();
						DerivativeArray[i][1] = KeywordsCombo.getText()
								.toLowerCase();
						DerivativeArray[i][2] = descriptionText.getText();
					} else if (instructionSetText.getText().startsWith(
							"Register")) {
						int i = Integer
								.parseInt(instructionSetText.getText()
										.substring(
												9,
												instructionSetText.getText()
														.length() - 1));
						RegisterArray[i][0] = KeywordsCombo.getText();
						RegisterArray[i][1] = KeywordsCombo.getText()
								.toLowerCase();
						RegisterArray[i][2] = descriptionText.getText();
					}
					tree.removeAll();
					refreshTree(tree);
					makeCombo();
				}
			}
		});

		// add the keyword
		Button addButton = new Button(editComposite, SWT.PUSH);
		addButton.setText(Messages.ADD_BUTTON_TEXT);
		addButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_BEGINNING));
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				if (!MessageDialog.openQuestion(null, Messages.CONFIRM,
						Messages.CONFIRM_QUESTION))
					return;
				else {
					if (index == 0)
						index = 1;
					else
						index = 0;
					// add the keyword
					KList = new ArrayList<String>();
					DList = new ArrayList<String>();

					if (instructionSetText.getText().startsWith("Instruction")) {
						for (int i = 0; i < InstructionArray.length; i++) {
							KList.add(InstructionArray[i][0]);
							DList.add(InstructionArray[i][2]);
						}
						KList.add(KeywordsCombo.getText().toUpperCase());
						DList.add(descriptionText.getText());
						InstructionArray = new String[KList.size()][3];
						for (int i = 0; i < KList.size(); i++) {
							InstructionArray[i][0] = KList.get(i);
							InstructionArray[i][1] = KList.get(i).toLowerCase();
							InstructionArray[i][2] = DList.get(i);
						}
						KList = null;
						DList = null;
					} else if (instructionSetText.getText().startsWith(
							"Segment")) {
						for (int i = 0; i < SegmentArray.length; i++) {
							KList.add(SegmentArray[i][0]);
							DList.add(SegmentArray[i][2]);
						}
						KList.add(KeywordsCombo.getText().toUpperCase());
						DList.add(descriptionText.getText());
						SegmentArray = new String[KList.size()][3];
						for (int i = 0; i < KList.size(); i++) {
							SegmentArray[i][0] = KList.get(i);
							SegmentArray[i][1] = KList.get(i).toLowerCase();
							SegmentArray[i][2] = DList.get(i);
						}
						KList = null;
						DList = null;
					} else if (instructionSetText.getText()
							.startsWith("Macros")) {
						for (int i = 0; i < MacrosArray.length; i++) {
							KList.add(MacrosArray[i][0]);
							DList.add(MacrosArray[i][2]);
						}
						KList.add(KeywordsCombo.getText().toUpperCase());
						DList.add(descriptionText.getText());
						MacrosArray = new String[KList.size()][3];
						for (int i = 0; i < KList.size(); i++) {
							MacrosArray[i][0] = KList.get(i);
							MacrosArray[i][1] = KList.get(i).toLowerCase();
							MacrosArray[i][2] = DList.get(i);
						}
						KList = null;
						DList = null;
					} else if (instructionSetText.getText().startsWith(
							"Derivative")) {
						for (int i = 0; i < DerivativeArray.length; i++) {
							KList.add(DerivativeArray[i][0]);
							DList.add(DerivativeArray[i][2]);
						}
						KList.add(KeywordsCombo.getText().toUpperCase());
						DList.add(descriptionText.getText());
						DerivativeArray = new String[KList.size()][3];
						for (int i = 0; i < KList.size(); i++) {
							DerivativeArray[i][0] = KList.get(i);
							DerivativeArray[i][1] = KList.get(i).toLowerCase();
							DerivativeArray[i][2] = DList.get(i);
						}
						KList = null;
						DList = null;
					} else if (instructionSetText.getText().startsWith(
							"Register")) {
						for (int i = 0; i < RegisterArray.length; i++) {
							KList.add(RegisterArray[i][0]);
							DList.add(RegisterArray[i][2]);
						}
						KList.add(KeywordsCombo.getText().toUpperCase());
						DList.add(descriptionText.getText());
						RegisterArray = new String[KList.size()][3];
						for (int i = 0; i < KList.size(); i++) {
							RegisterArray[i][0] = KList.get(i);
							RegisterArray[i][1] = KList.get(i).toLowerCase();
							RegisterArray[i][2] = DList.get(i);
						}
						KList = null;
						DList = null;
					}
					makeCombo();
					tree.removeAll();
					refreshTree(tree);
				}

			}
		});

		// remove the keyword
		Button removeButton = new Button(editComposite, SWT.PUSH);
		removeButton.setText(Messages.REMOVE_BUTTON_TEXT);
		removeButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_BEGINNING));
		removeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				if (!MessageDialog.openQuestion(null, Messages.CONFIRM,
						Messages.CONFIRM_QUESTION))
					return;
				else {
					if (index == 0)
						index = 1;
					else
						index = 0;
					// add the keyword
					KList = new ArrayList<String>();
					DList = new ArrayList<String>();

					if (instructionSetText.getText().startsWith("Instruction")) {
						for (int i = 0; i < InstructionArray.length; i++) {

							KList.add(InstructionArray[i][0]);
							DList.add(InstructionArray[i][2]);
						}
						int j = Integer
								.parseInt(instructionSetText.getText()
										.substring(
												12,
												instructionSetText.getText()
														.length() - 1));
						KList.remove(j);
						DList.remove(j);
						instructionSetText.setText("");
						descriptionText.setText("");
						InstructionArray = new String[KList.size()][3];
						for (int i = 0; i < KList.size(); i++) {
							InstructionArray[i][0] = KList.get(i);
							InstructionArray[i][1] = KList.get(i).toLowerCase();
							InstructionArray[i][2] = DList.get(i);
						}
						KList = null;
						DList = null;
					} else if (instructionSetText.getText().startsWith(
							"Segment")) {
						for (int i = 0; i < SegmentArray.length; i++) {
							KList.add(SegmentArray[i][0]);
							DList.add(SegmentArray[i][2]);
						}
						int j = Integer
								.parseInt(instructionSetText.getText()
										.substring(
												8,
												instructionSetText.getText()
														.length() - 1));
						KList.remove(j);
						DList.remove(j);
						instructionSetText.setText("");
						descriptionText.setText("");
						SegmentArray = new String[KList.size()][3];
						for (int i = 0; i < KList.size(); i++) {
							SegmentArray[i][0] = KList.get(i);
							SegmentArray[i][1] = KList.get(i).toLowerCase();
							SegmentArray[i][2] = DList.get(i);
						}
						KList = null;
						DList = null;
					} else if (instructionSetText.getText()
							.startsWith("Macros")) {
						for (int i = 0; i < MacrosArray.length; i++) {
							KList.add(MacrosArray[i][0]);
							DList.add(MacrosArray[i][2]);
						}
						int j = Integer
								.parseInt(instructionSetText.getText()
										.substring(
												7,
												instructionSetText.getText()
														.length() - 1));
						KList.remove(j);
						DList.remove(j);
						instructionSetText.setText("");
						descriptionText.setText("");
						MacrosArray = new String[KList.size()][3];
						for (int i = 0; i < KList.size(); i++) {
							MacrosArray[i][0] = KList.get(i);
							MacrosArray[i][1] = KList.get(i).toLowerCase();
							MacrosArray[i][2] = DList.get(i);
						}
						KList = null;
						DList = null;
					} else if (instructionSetText.getText().startsWith(
							"Derivative")) {
						for (int i = 0; i < DerivativeArray.length; i++) {
							KList.add(DerivativeArray[i][0]);
							DList.add(DerivativeArray[i][2]);
						}
						int j = Integer
								.parseInt(instructionSetText.getText()
										.substring(
												11,
												instructionSetText.getText()
														.length() - 1));
						KList.remove(j);
						DList.remove(j);
						instructionSetText.setText("");
						descriptionText.setText("");
						DerivativeArray = new String[KList.size()][3];
						for (int i = 0; i < KList.size(); i++) {
							DerivativeArray[i][0] = KList.get(i);
							DerivativeArray[i][1] = KList.get(i).toLowerCase();
							DerivativeArray[i][2] = DList.get(i);
						}
						KList = null;
						DList = null;
					} else if (instructionSetText.getText().startsWith(
							"Register")) {
						for (int i = 0; i < RegisterArray.length; i++) {
							KList.add(RegisterArray[i][0]);
							DList.add(RegisterArray[i][2]);
						}
						int j = Integer
								.parseInt(instructionSetText.getText()
										.substring(
												9,
												instructionSetText.getText()
														.length() - 1));
						KList.remove(j);
						DList.remove(j);
						instructionSetText.setText("");
						descriptionText.setText("");
						RegisterArray = new String[KList.size()][3];
						for (int i = 0; i < KList.size(); i++) {
							RegisterArray[i][0] = KList.get(i);
							RegisterArray[i][1] = KList.get(i).toLowerCase();
							RegisterArray[i][2] = DList.get(i);
						}
						KList = null;
						DList = null;
					}
					makeCombo();
					tree.removeAll();
					refreshTree(tree);
				}

			}
		});

	}

	private String searchKey(String keys) {
		String des = "";
		for (int i = 0; i < InstructionArray.length; i++) {
			if (keys.equals(InstructionArray[i][0])) {
				des = InstructionArray[i][2];
				String i_s = i + "";
				return i_s + "Key1" + des;
			}
		}

		for (int i = 0; i < SegmentArray.length; i++) {
			if (keys.equals(SegmentArray[i][0])) {
				des = SegmentArray[i][2];
				String i_s = i + "";
				return i_s + "Key2" + des;
			}
		}

		for (int i = 0; i < MacrosArray.length; i++) {
			if (keys.equals(MacrosArray[i][0])) {
				des = MacrosArray[i][2];
				String i_s = i + "";
				return i_s + "Key3" + des;
			}
		}

		for (int i = 0; i < DerivativeArray.length; i++) {
			if (keys.equals(DerivativeArray[i][0])) {
				des = DerivativeArray[i][2];
				String i_s = i + "";
				return i_s + "Key4" + des;
			}
		}

		for (int i = 0; i < RegisterArray.length; i++) {
			if (keys.equals(RegisterArray[i][0])) {
				des = RegisterArray[i][2];
				String i_s = i + "";
				return i_s + "Key5" + des;
			}
		}
		return "";
	}

	private void makeTree(Composite parent) {

		Composite treeComposite = new Composite(parent, SWT.NONE);
		treeComposite.setSize(600, 800);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		treeComposite.setLayout(layout);
		GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);

		tree = new Tree(treeComposite, SWT.NONE);
		tree.setLayoutData(gd);

		TreeColumn keywordColumn = new TreeColumn(tree, SWT.NONE, 0);
		keywordColumn.setWidth(100);
		keywordColumn.setText(Messages.KEYWORD_TITLE);
		TreeColumn uppercaseColumn = new TreeColumn(tree, SWT.NONE, 1);
		uppercaseColumn.setWidth(85);
		uppercaseColumn.setText(Messages.UPPERCASE_TEXT);
		TreeColumn descriptionColumn = new TreeColumn(tree, SWT.NONE, 2);
		descriptionColumn.setWidth(420);
		descriptionColumn.setText(Messages.DESCRIPTION_TEXT);

		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);

		refreshTree(tree);
	}

	private void makeCombo() {

		KeywordsCombo.removeAll();
		for (int i = 0; i < SegmentArray.length; i++) {
			KeywordsCombo.add(SegmentArray[i][0]);
		}

		for (int i = 0; i < MacrosArray.length; i++) {
			KeywordsCombo.add(MacrosArray[i][0]);
		}

		for (int i = 0; i < DerivativeArray.length; i++) {
			KeywordsCombo.add(DerivativeArray[i][0]);
		}

		for (int i = 0; i < RegisterArray.length; i++) {
			KeywordsCombo.add(RegisterArray[i][0]);
		}
		for (int i = 0; i < InstructionArray.length; i++) {
			KeywordsCombo.add(InstructionArray[i][0]);
		}
	}

	private void refreshTree(Tree tree) {
		instructionRoot = new TreeItem(tree, SWT.NULL);
		instructionRoot.setText(Messages.TEXTCOLOR_INSTRUCTION_NAME);
		for (int i = 0; i < InstructionArray.length; i++)
			createTreeItem(instructionRoot, InstructionArray, i);
		instructionRoot.setExpanded(false);

		segmetRoot = new TreeItem(tree, SWT.NULL);
		segmetRoot.setText(Messages.TEXTCOLOR_SEGMENT_NAME);
		for (int i = 0; i < SegmentArray.length; i++)
			createTreeItem(segmetRoot, SegmentArray, i);
		segmetRoot.setExpanded(false);

		macrosRoot = new TreeItem(tree, SWT.NULL);
		macrosRoot.setText(Messages.TEXTCOLOR_MACROS_NAME);
		for (int i = 0; i < MacrosArray.length; i++)
			createTreeItem(macrosRoot, MacrosArray, i);
		macrosRoot.setExpanded(false);

		derivativesRoot = new TreeItem(tree, SWT.NULL);
		derivativesRoot.setText(Messages.TEXTCOLOR_DERIVATIVE_NAME);
		for (int i = 0; i < DerivativeArray.length; i++)
			createTreeItem(derivativesRoot, DerivativeArray, i);
		derivativesRoot.setExpanded(false);

		registerRoot = new TreeItem(tree, SWT.NULL);
		registerRoot.setText(Messages.TEXTCOLOR_REGISTER_NAME);
		for (int i = 0; i < RegisterArray.length; i++)
			createTreeItem(registerRoot, RegisterArray, i);
		registerRoot.setExpanded(false);

	}

	private void createTreeItem(TreeItem parentItem, String[][] array, int i) {
		TreeItem item = new TreeItem(parentItem, SWT.NONE);
		item.setText(0, String.valueOf(i));
		item.setText(1, array[i][0]);
		item.setText(2, array[i][2]);
	}

	public boolean performOk() {
		String root = Platform.getInstanceLocation().getURL().getPath();
		File mySyntax = new File(root + "syntax_keywords_changed.xml");
		try {
			mySyntax.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileWriter fw = null;

		try {
			fw = new FileWriter(mySyntax);
			fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<sets>\n");
			for (int i = 0; i < InstructionArray.length; i++)
				fw.write("  <instruction command = \"" + InstructionArray[i][0]
						+ "\" description = \"" + InstructionArray[i][2]
						+ "\" />\n");
			for (int i = 0; i < SegmentArray.length; i++)
				fw.write("  <segment field = \"" + SegmentArray[i][0]
						+ "\" description = \"" + SegmentArray[i][2]
						+ "\" />\n");
			for (int i = 0; i < MacrosArray.length; i++)
				fw.write("  <macros command = \"" + MacrosArray[i][0]
						+ "\" description = \"" + MacrosArray[i][2] + "\" />\n");
			for (int i = 0; i < DerivativeArray.length; i++)
				fw.write("  <derivatives command = \"" + DerivativeArray[i][0]
						+ "\" description = \"" + DerivativeArray[i][2]
						+ "\" />\n");
			for (int i = 0; i < RegisterArray.length; i++)
				fw.write("  <register command = \"" + RegisterArray[i][0]
						+ "\" description = \"" + RegisterArray[i][2]
						+ "\" />\n");
			fw.write("</sets>");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null)
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setValue(Constants.PREFERENCES_KEYWORD_CHANGE, index);
		return super.performOk();
	}
}
