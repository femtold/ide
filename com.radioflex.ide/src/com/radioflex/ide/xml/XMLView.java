package com.radioflex.ide.xml;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.eclipse.jface.viewers.CellEditor;
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
import org.eclipse.ui.part.ViewPart;

import com.radioflex.ide.ui.Activator;


public class XMLView extends ViewPart {
	private TreeViewer viewer;
	
	public void createPartControl(Composite parent) {
		viewer= new TreeViewer(parent,SWT.MULTI | SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		viewer.getTree().setHeaderVisible(true);
		viewer.getTree().setLinesVisible(true);
		
		
		TreeViewerColumn column1 = new TreeViewerColumn(viewer, SWT.NONE);
		column1.getColumn().setText("ID");
		column1.getColumn().setWidth(130);
		
		TreeViewerColumn column2 = new TreeViewerColumn(viewer, SWT.NONE);
		column2.getColumn().setText("Value");
		column2.getColumn().setWidth(100);
		
		column2.setEditingSupport(new EditingSupport(viewer) {
			
			@Override
			protected void setValue(Object element, Object value) {
				// TODO Auto-generated method stub
				if(element instanceof Element){
					Element e =(Element)element;
					e.setText((String) value);
					viewer.update(element, null);
				}
			}
			
			@Override
			protected Object getValue(Object element) {
				// TODO Auto-generated method stub
				if(element instanceof Element){
					Element e = (Element)element;
					return e.getText();
				}
				return null;
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				// TODO Auto-generated method stub
				return new TextCellEditor(viewer.getTree());
			}
			
			@Override
			protected boolean canEdit(Object element) {
				// TODO Auto-generated method stub
			if(element instanceof Element){
					Element e = (Element) element;
					if(!e.elementIterator().hasNext()){
						return true;}
					return false;
				}
				return false;
			}
		});
		

		// set input here
		String path = Activator.getFilePathFromPlugin("example.xml");
		ParseXML p = new ParseXML(new File(path));
		List<Element> list  = new ArrayList<>();
		list.add(p.rootElement);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(list);
		
		

	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}
	

	/**
	 * The ViewContentProvider implements ITreeContentProvider is used to display the tree structure;
	 * First, method inputChanged is called;
	 * The second method is getElements; 
	 * @author kitkat
	 *
	 */
	class ViewContentProvider implements ITreeContentProvider {

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
			// TODO Auto-generated method stub
			if(inputElement instanceof ArrayList<?>)
				return ((List<?>)inputElement).toArray();
			return null;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			// TODO Auto-generated method stub
			if(parentElement instanceof Element)
				return ((Element)parentElement).elements().toArray();
			return null;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		//if true there is a triangle at the first;false there is no;
		@Override
		public boolean hasChildren(Object element) {
			// TODO Auto-generated method stub
			if(((Element) element).elementIterator().hasNext())
					return true;
			return false;
		}

	}

	class ViewLabelProvider implements ITableLabelProvider {


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
			// TODO Auto-generated method stub
				switch(columnIndex){
					case 0:
						return ((Element) element).attributeValue("id");
					case 1:
						return ((Element) element).getText();
				}
				return null;
		}
	}
}

