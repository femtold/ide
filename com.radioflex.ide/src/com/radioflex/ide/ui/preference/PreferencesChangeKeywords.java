package com.radioflex.ide.ui.preference;

import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;

import com.radioflex.ide.ui.Activator;
import com.radioflex.ide.ui.Constants;
import com.radioflex.ide.ui.Messages;
import com.radioflex.ide.ui.editor.RadioFlexEditor;
import com.radioflex.ide.ui.editor.SyntaxKeywords;

/**
 * 
 * @author terry
 *
 */
public class PreferencesChangeKeywords extends PreferencePage implements
		IWorkbenchPreferencePage {
	private Tree ttree;
	private String[][] instructionArray;
	private String[][] segmentArray;
	private String[][] registerArray;
	private String[][] macrosArray;
	private String[][] derivativesArray;
	private Text keywordtext;
	private Text descriptiontext;
	private TreeItem instructionroot;
	private TreeItem segmentroot;
	private TreeItem macrosroot;
	private TreeItem derivativesroot;
	private TreeItem registerroot;
	private int index;
	private TreeItem[] selectionitem;
	private String ktext;
	private String dtext;
	private TreeItem parentitem;
	private ArrayList<String> kintermediateList;
	private ArrayList<String> dintermediateList;
	

	public PreferencesChangeKeywords() {
	}

	public PreferencesChangeKeywords(String title) {
		super(title);
	}

	public PreferencesChangeKeywords(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	public void init(IWorkbench workbench) {
		this.setTitle(Messages.KEYWORD_TITLE);
		this.setDescription(Messages.KEYWORD_DISCRIPTION + "\n");
		this.noDefaultAndApplyButton();
	}

	@Override
	protected Control createContents(Composite parent) {
		GridLayout layout = new GridLayout(1,false);
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		parent.setLayout(layout);
		

		createKeywordPage(parent);

		Dialog.applyDialogFont(parent);
		return parent;
	}

	private void createKeywordPage(Composite parent) {
		//get keyword array
		instructionArray = SyntaxKeywords.getInstructionArray();
		segmentArray = SyntaxKeywords.getSegmentArray();
		registerArray = SyntaxKeywords.getRegisterArray();
		macrosArray = SyntaxKeywords.getMacrosArray();
		derivativesArray = SyntaxKeywords.getDerivativeArray();
		
		Label label = new Label(parent, SWT.FILL);
		label.setText(Messages.INSTRUCTION_TEXT + ":");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		
		ttree = new Tree(parent, SWT.SINGLE | SWT.V_SCROLL |SWT.H_SCROLL|SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		ttree.setLayoutData(gd);
		makeTree(parent);
		
		
		ttree.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectionitem = ttree.getSelection();
				keywordtext.setText(selectionitem[0].getText(1));
				descriptiontext.setText(selectionitem[0].getText(3));
			}

		});

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.horizontalSpan = 2;
		
		Label keywordlabel = new Label(parent, SWT.LEFT);
		keywordlabel.setText(Messages.KEYWORD_LABEL_TEXT);
		
		keywordtext = new Text(parent, SWT.BORDER);
		keywordtext.setLayoutData(new GridData());

		Label descriptionlabel = new Label(parent, SWT.LEFT);
		descriptionlabel.setText(Messages.DESCRIPTION_TEXT);
		
		descriptiontext = new Text(parent, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		descriptiontext.setLayoutData(gd);
		
		Button editbutton = new Button(parent, SWT.PUSH);
		editbutton.setText(Messages.EDIT_BUTTON_TEXT + "...");
		editbutton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_BEGINNING));
		editbutton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ktext = keywordtext.getText();
				dtext = descriptiontext.getText();
				selectionitem = ttree.getSelection();
				parentitem =  selectionitem[0].getParentItem();
				index = Integer.parseInt(selectionitem[0].getText());
				
				//edit the keyword
				if(parentitem == instructionroot){
					instructionArray[index][0] = ktext;
					instructionArray[index][1] = ktext.toLowerCase();
					instructionArray[index][2] = dtext;
				}
				else if(parentitem == segmentroot){
					segmentArray[index][0] = ktext;
					segmentArray[index][1] = ktext.toLowerCase();
					segmentArray[index][2] = dtext;
				}
				else if(parentitem == macrosroot){
					macrosArray[index][0] = ktext;
					macrosArray[index][1] = ktext.toLowerCase();
					macrosArray[index][2] = dtext;
				}
				else if(parentitem == derivativesroot){
					derivativesArray[index][0] = ktext;
					derivativesArray[index][1] = ktext.toLowerCase();
					derivativesArray[index][2] = dtext;
				}
				else if(parentitem == registerroot){
					registerArray[index][0] = ktext;
					registerArray[index][1] = ktext.toLowerCase();
					registerArray[index][2] = dtext;
				}
				ttree.removeAll();
				refreshTree(ttree);
			}

		});


		Button addbutton = new Button(parent, SWT.PUSH);
		addbutton.setText(Messages.ADD_BUTTON_TEXT);
		addbutton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_BEGINNING));
		addbutton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				kintermediateList = new ArrayList<String>();
				dintermediateList = new ArrayList<String>();
				//先变成ArrayList<> 添加元素 在变成数组类型
				if(ttree.getSelection()[0].getParentItem() == instructionroot){
					for(int i = 0;i < instructionArray.length;i++){
						kintermediateList.add(instructionArray[i][0]);
						dintermediateList.add(instructionArray[i][2]);
					}
					kintermediateList.add(keywordtext.getText());
					dintermediateList.add(descriptiontext.getText());
					instructionArray = new String[kintermediateList.size()][3];
					for(int i = 0;i < kintermediateList.size();i++){
						instructionArray[i][0] = kintermediateList.get(i);
						instructionArray[i][1] = kintermediateList.get(i).toLowerCase();
						instructionArray[i][2] = dintermediateList.get(i);
					}
				}
				else if(ttree.getSelection()[0].getParentItem() == segmentroot){
					for(int i = 0;i < segmentArray.length;i++){
						kintermediateList.add(segmentArray[i][0]);
						dintermediateList.add(segmentArray[i][2]);
					}
					kintermediateList.add(keywordtext.getText());
					dintermediateList.add(descriptiontext.getText());
					segmentArray = new String[kintermediateList.size()][3];
					for(int i = 0;i < kintermediateList.size();i++){
						segmentArray[i][0] = kintermediateList.get(i);
						segmentArray[i][1] = kintermediateList.get(i).toLowerCase();
						segmentArray[i][2] = dintermediateList.get(i);
					}
				}
				else if(ttree.getSelection()[0].getParentItem() == macrosroot){
					for(int i = 0;i < macrosArray.length;i++){
						kintermediateList.add(macrosArray[i][0]);
						dintermediateList.add(macrosArray[i][2]);
					}
					kintermediateList.add(keywordtext.getText());
					dintermediateList.add(descriptiontext.getText());
					macrosArray = new String[kintermediateList.size()][3];
					for(int i = 0;i < kintermediateList.size();i++){
						macrosArray[i][0] = kintermediateList.get(i);
						macrosArray[i][1] = kintermediateList.get(i).toLowerCase();
						macrosArray[i][2] = dintermediateList.get(i);
					}
				}
				else if(ttree.getSelection()[0].getParentItem() == derivativesroot){
					for(int i = 0;i < derivativesArray.length;i++){
						kintermediateList.add(derivativesArray[i][0]);
						dintermediateList.add(derivativesArray[i][2]);
					}
					kintermediateList.add(keywordtext.getText());
					dintermediateList.add(descriptiontext.getText());
					derivativesArray = new String[kintermediateList.size()][3];
					for(int i = 0;i < kintermediateList.size();i++){
						derivativesArray[i][0] = kintermediateList.get(i);
						derivativesArray[i][1] = kintermediateList.get(i).toLowerCase();
						derivativesArray[i][2] = dintermediateList.get(i);
					}
				}
				else if(ttree.getSelection()[0].getParentItem() == registerroot){
					for(int i = 0;i < registerArray.length;i++){
						kintermediateList.add(registerArray[i][0]);
						dintermediateList.add(registerArray[i][2]);
					}
					kintermediateList.add(keywordtext.getText());
					dintermediateList.add(descriptiontext.getText());
					registerArray = new String[kintermediateList.size()][3];
					for(int i = 0;i < kintermediateList.size();i++){
						registerArray[i][0] = kintermediateList.get(i);
						registerArray[i][1] = kintermediateList.get(i).toLowerCase();
						registerArray[i][2] = dintermediateList.get(i);
					}
				}
				ttree.removeAll();
				refreshTree(ttree);	
			}
		});

		Button removebutton = new Button(parent, SWT.PUSH);
		removebutton.setText(Messages.REMOVE_BUTTON_TEXT);
		removebutton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_BEGINNING));
		removebutton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				kintermediateList = new ArrayList<String>();
				dintermediateList = new ArrayList<String>();
				index = Integer.parseInt(selectionitem[0].getText());
				//先变成ArrayList<> 删除元素 在变成数组类型
				if(ttree.getSelection()[0].getParentItem() == instructionroot){
					for(int i = 0;i < instructionArray.length;i++){
						kintermediateList.add(instructionArray[i][0]);
						dintermediateList.add(instructionArray[i][2]);
					}
					kintermediateList.remove(index);
					dintermediateList.remove(index);
					instructionArray = new String[kintermediateList.size()][3];
					for(int i = 0;i < kintermediateList.size();i++){
						instructionArray[i][0] = kintermediateList.get(i);
						instructionArray[i][1] = kintermediateList.get(i).toLowerCase();
						instructionArray[i][2] = dintermediateList.get(i);
					}
				}
				else if(ttree.getSelection()[0].getParentItem() == segmentroot){
					for(int i = 0;i < segmentArray.length;i++){
						kintermediateList.add(segmentArray[i][2]);
						dintermediateList.add(segmentArray[i][2]);
					}
					kintermediateList.remove(index);
					dintermediateList.remove(index);
					segmentArray = new String[kintermediateList.size()][3];
					for(int i = 0;i < kintermediateList.size();i++){
						segmentArray[i][0] = kintermediateList.get(i);
						segmentArray[i][1] = kintermediateList.get(i).toLowerCase();
						segmentArray[i][2] = dintermediateList.get(i);
					}
				}
				else if(ttree.getSelection()[0].getParentItem() == macrosroot){
					for(int i = 0;i < macrosArray.length;i++){
						kintermediateList.add(macrosArray[i][0]);
						dintermediateList.add(macrosArray[i][2]);
					}
					kintermediateList.remove(index);
					dintermediateList.remove(index);
					macrosArray = new String[kintermediateList.size()][3];
					for(int i = 0;i < kintermediateList.size();i++){
						macrosArray[i][0] = kintermediateList.get(i);
						macrosArray[i][1] = kintermediateList.get(i).toLowerCase();
						macrosArray[i][2] = dintermediateList.get(i);
					}
				}
				else if(ttree.getSelection()[0].getParentItem() == derivativesroot){
					for(int i = 0;i < derivativesArray.length;i++){
						kintermediateList.add(derivativesArray[i][0]);
						dintermediateList.add(derivativesArray[i][2]);
					}
					kintermediateList.remove(index);
					dintermediateList.remove(index);
					derivativesArray = new String[kintermediateList.size()][3];
					for(int i = 0;i < kintermediateList.size();i++){
						derivativesArray[i][0] = kintermediateList.get(i);
						derivativesArray[i][1] = kintermediateList.get(i).toLowerCase();
						derivativesArray[i][2] = dintermediateList.get(i);
					}
				}
				else if(ttree.getSelection()[0].getParentItem() == registerroot){
					for(int i = 0;i < registerArray.length;i++){
						kintermediateList.add(registerArray[i][0]);
						dintermediateList.add(registerArray[i][2]);
					}
					kintermediateList.remove(index);
					dintermediateList.remove(index);
					registerArray = new String[kintermediateList.size()][3];
					for(int i = 0;i < kintermediateList.size();i++){
						registerArray[i][0] = kintermediateList.get(i);
						registerArray[i][1] = kintermediateList.get(i).toLowerCase();
						registerArray[i][2] = dintermediateList.get(i);
					}
				}
				ttree.removeAll();
				refreshTree(ttree);	
			}

		});
	}

	private void makeTree(Composite parent) {
		TreeColumn keywordcolumn = new TreeColumn(ttree, SWT.NONE,0);
		keywordcolumn.setWidth(140);
		keywordcolumn.setText(Messages.KEYWORD_TITLE);
		TreeColumn uppercasecolumn = new TreeColumn(ttree, SWT.NONE, 1);
		uppercasecolumn.setWidth(85);
		uppercasecolumn.setText(Messages.UPPERCASE_TEXT);
		TreeColumn lowercasecolumn = new TreeColumn(ttree, SWT.NONE, 2);
		lowercasecolumn.setWidth(85);
		lowercasecolumn.setText(Messages.LOWERCASE_TEXT);
		TreeColumn descriptioncolumn = new TreeColumn(ttree, SWT.NONE, 3);
		descriptioncolumn.pack();
		descriptioncolumn.setText(Messages.DESCRIPTION_TEXT);
		
		ttree.setLinesVisible(true);
		ttree.setHeaderVisible(true);
					
		refreshTree(ttree);
	}

	private TreeItem createTreeItem(TreeItem parentItem, String[][] string,
			int i) {
		TreeItem item = new TreeItem(parentItem, SWT.NONE);
		item.setText(0,String.valueOf(i));
		item.setText(1, string[i][0]);
		item.setText(2, string[i][1]);
		item.setText(3, string[i][2]);
		return item;
	}
	
	private void refreshTree(Tree ttree){
		instructionroot = new TreeItem(ttree, SWT.NULL);
		instructionroot.setText(Messages.TEXTCOLOR_INSTRUCTION_NAME);
		for (int i = 0;i < instructionArray.length;i++)
			createTreeItem(instructionroot, instructionArray, i);
		instructionroot.setExpanded(false);//false 为默认的树是收起的状态，默认为false
		
		segmentroot = new TreeItem(ttree, SWT.NULL);
		segmentroot.setText(Messages.TEXTCOLOR_SEGMENT_NAME);
		for (int i = 0;i < segmentArray.length;i++)
			createTreeItem(segmentroot, segmentArray, i);
		
		macrosroot = new TreeItem(ttree, SWT.NULL);
		macrosroot.setText(Messages.TEXTCOLOR_MACROS_NAME);
		for (int i = 0;i < macrosArray.length;i++)
			createTreeItem(macrosroot, macrosArray, i);
		
		derivativesroot = new TreeItem(ttree, SWT.NULL);
		derivativesroot.setText(Messages.TEXTCOLOR_DERIVATIVE_NAME);
		for (int i = 0;i < derivativesArray.length;i++)
			createTreeItem(derivativesroot, derivativesArray, i);
		
		registerroot = new TreeItem(ttree, SWT.NULL);
		registerroot.setText(Messages.TEXTCOLOR_REGISTER_NAME);
		for (int i = 0;i < registerArray.length;i++)
			createTreeItem(registerroot, registerArray, i);

	}
	
	@Override
	public boolean performOk() {
		String root = Platform.getInstanceLocation().getURL().getPath();
		File mysyntax = new File(root + "syntax_keywords2.xml");
		try {
			mysyntax.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(mysyntax);
			fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+"<sets>\n");
			for(int i = 0;i < instructionArray.length;i++)
				fw.write("  <instruction command=\""+instructionArray[i][0]+"\" description=\""+instructionArray[i][2]+"\" />\n");
			for(int i = 0;i < segmentArray.length;i++)
				fw.write("  <segment field=\""+segmentArray[i][0]+"\" description=\""+segmentArray[i][2]+"\" />\n");
			for(int i = 0;i < macrosArray.length;i++)
				fw.write("  <macros command=\""+macrosArray[i][0]+"\" description=\""+macrosArray[i][2]+"\" />\n");
			for(int i = 0;i < derivativesArray.length;i++)
				fw.write("  <derivatives command=\""+derivativesArray[i][0]+"\" description=\""+derivativesArray[i][2]+"\" />\n");
			for(int i = 0;i < registerArray.length;i++)
				fw.write("  <register command=\""+registerArray[i][0]+"\" description=\""+registerArray[i][2]+"\" />\n");
			fw.write("</sets>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Display display = Display.getCurrent();
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		int i = PreferencesInitializer.gethasChange();
		store.setValue(Constants.PERFERENCE_KEYWORD_CHANGE,i*i-1);
		return super.performOk();
	}
}
