package com.radioflex.ide.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Element;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

import com.radioflex.ide.Activator;
import com.radioflex.ide.debug.Start;

/**
 * The XML view.
 *
 */
public class XMLView extends ViewPart {

	private TreeViewer viewer;
	private Tree tree;
	private static Element e1;
	// private List<Element> leaves = new ArrayList<Element>();
	private Set<Element> set = new HashSet<Element>();

	@Override
	public void createPartControl(Composite parent) {
		tree = new Tree(parent, SWT.FULL_SELECTION);
		tree.setHeaderVisible(true);
		viewer = new TreeViewer(tree);

		TreeViewerColumn column1 = new TreeViewerColumn(viewer, SWT.LEFT);
		tree.setLinesVisible(true);
		column1.getColumn().setAlignment(SWT.LEFT);
		column1.getColumn().setText("ID");
		column1.getColumn().setWidth(150);

		TreeViewerColumn column2 = new TreeViewerColumn(viewer, SWT.RIGHT);
		column2.getColumn().setAlignment(SWT.LEFT);
		column2.getColumn().setText("Value");
		column2.getColumn().setWidth(150);
		column2.setEditingSupport(new EnableEdit(viewer));

		String path = Activator.getFilePathFromPlugin("example.xml");
		ParseXML p = new ParseXML(new File(path));
		List<Element> list = new ArrayList<Element>();
		e1 = p.getRootElement();
		list.add(e1);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(list);
		viewer.expandAll();
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private class EnableEdit extends EditingSupport {

		public EnableEdit(ColumnViewer viewer) {
			super(viewer);
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor(tree);
		}

		@Override
		protected boolean canEdit(Object element) {
			if (element instanceof Element) {
				Element e = (Element) element;
				if (!e.elementIterator().hasNext())
					return true;
				return false;
			}
			return false;
		}

		@Override
		protected Object getValue(Object element) {
			if (element instanceof Element) {
				Element e = (Element) element;
				return e.getText();
			}
			return null;
		}

		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof Element) {
				Element e = (Element) element;
				e.setText((String) value);
				viewer.update(element, null);
				try {
					Start.sendString("rfdbg.setcycles("+(String)value+")\n");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private class ViewContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ArrayList<?>)
				return ((List<?>) inputElement).toArray();
			return null;

		}

		@Override
		public Object[] getChildren(Object parentElement) {

			if (parentElement instanceof Element)
				return ((Element) parentElement).elements().toArray();
			return null;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (((Element) element).elementIterator().hasNext())
				return true;
			return false;
		}

	}

	private class ViewLabelProvider implements ITableLabelProvider {

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return ((Element) element).attributeValue("id");
			case 1:
				return ((Element) element).getText().trim();
			}
			return null;
		}

	}

	public TreeViewer getViewer() {
		// TODO Auto-generated method stub
		return viewer;
	}

	public void setAllValue(String str) {
		getLeaves(getChildren(e1));
		for (Iterator<?> it = set.iterator(); it.hasNext();) {
			Element e = (Element) it.next();
			e.setText(str);
			viewer.update(e, null);
		}
	}

	// it is like that we can specify the key with a value;
	public void setKeyValue(String str, String str1) {
		// put all leaves into the set:
		getLeaves(getChildren(e1));
		// get all the leaves:
		for (Iterator<?> it = set.iterator(); it.hasNext();) {
			Element e = (Element) it.next();
			if (str.equals(e.attributeValue("id").trim())) {
				e.setText(str1);
				viewer.update(e, null);
				// System.out.println("set the "+str+"  successfully!");
			}
		}
	}

	// if the key is not special we can set it's value by index;
	/*
	 * public void setValue(String str, int index){
	 * 
	 * getLeaves(getChildren(e1));
	 * 
	 * if(str != null){ Element e = leaves.get(index); e.setText(str);
	 * viewer.update(e, null); } }
	 */

	private boolean hasChildren(Element ele) {
		if (ele.elementIterator().hasNext()) {
			return true;
		}
		return false;
	}

	private List<?> getChildren(Element ele) {
		if (hasChildren(ele)) {
			return ele.elements();
		}
		List<Element> list = new ArrayList<Element>();
		list.add(ele);
		return list;
	}

	private void getLeaves(List<?> list) {
		for (int i = 0; i < list.size(); i++) {
			Element e = (Element) list.get(i);
			if (hasChildren(e)) {
				getLeaves(getChildren(e));
			} else if (!hasChildren(e)) {
				// System.out.println(e.getTextTrim());
				set.add(e);
				// leaves.add(e);
				// System.out.println(set.size());
			}
		}
	}
}