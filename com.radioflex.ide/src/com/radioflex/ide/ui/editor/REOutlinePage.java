package com.radioflex.ide.ui.editor;

import java.util.ArrayList;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import com.radioflex.ide.ui.editor.REOutlineParser.LocatedData;

public class REOutlinePage extends ContentOutlinePage {

	private RadioFlexEditor editor;

	private IEditorInput input;

	public REOutlinePage(RadioFlexEditor editor) {
		this.editor = editor;

		FT.print("in REOutlinePage Constructor");

	}

	/**
	 * Sets the input for the outlinepage.
	 * 
	 * @param input
	 *            The new input.
	 */
	// ??????????????????????????????????????????????????
	public void setInput(IEditorInput input) {
		FT.print("start in REOutlinePage setInput");

		this.input = input;

		// The tree viewer creation is inherited from ContentOutlinePage.
		// final local variables could be used and won't get changed in local
		// (anonymous) inner classes
		final TreeViewer viewer = getTreeViewer();

		if ((viewer != null) && (viewer.getContentProvider() != null)) {
			// add runnable to the ui thread?????????????????????
			// Causes the run() method of the runnable to be invoked by the
			// user-interface thread at the next reasonable opportunity
			editor.getSite().getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {

					FT.print(" in REOutlinePage setInput: new Runnable innerclass");

					Control control = viewer.getControl();
					control.setRedraw(false);

					ITextSelection textselect = (ITextSelection) editor
							.getSelectionProvider().getSelection();

					TreeNode treenode = getSelectedTreeModelObject(viewer);
					viewer.setInput(REOutlinePage.this.input);
					viewer.refresh();
					viewer.expandAll();
					selectTreeModelObject(viewer, treenode);

					editor.getSelectionProvider().setSelection(textselect);

					control.setRedraw(true);
				}
			});
		}

		FT.print("end in REOutlinePage setInput");
	}

	/**
	 * Returns the selected element in the tree viewer.
	 * 
	 * @param viewer
	 *            The tree viewer.
	 * 
	 * @return The selected element.
	 */
	private TreeNode getSelectedTreeModelObject(TreeViewer viewer) {

		FT.print("start in REOutlinePage getSelectedTreeObject");

		ISelection selection = viewer.getSelection();

		if (!selection.isEmpty()) {
			if (selection instanceof IStructuredSelection) {
				Object object = ((IStructuredSelection) selection)
						.getFirstElement();

				if (object instanceof TreeNode) {

					FT.print("end in REOutlinePage getSelectedTreeObject 1"
							+ "return (TreeNode) object;");

					return (TreeNode) object;
				}
			}
		}

		FT.print("end in REOutlinePage getSelectedTreeObject 2"
				+ "return null;");

		return null;
	}

	/**
	 * Select a given TreeNode in the given TreeViewer.
	 * 
	 * @param viewer
	 *            The given TreeViewer.
	 * @param treenode
	 *            The given TreeNode.
	 */
	private void selectTreeModelObject(TreeViewer viewer, TreeNode treenode) {

		FT.print("start in REOutlinePage selectTreeObject");

		if (treenode == null) {
			return;
		}

		IContentProvider icp = viewer.getContentProvider();
		if (icp instanceof REOutlineContentProvider) {
			REOutlineContentProvider cp = (REOutlineContentProvider) icp;
			TreeNode fto = cp.findEqualTreeObject(treenode);

			if (fto != null) {
				ArrayList<Object> newSelection = new ArrayList<Object>();
				newSelection.add(fto);
				viewer.setSelection(new StructuredSelection(newSelection));
			}
		}

		FT.print("end in REOutlinePage selectTreeObject");

	}

	/**
	 * {@inheritDoc}
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);

		FT.print("start (after super.createControl) in REOutlinePage createControl");

		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new REOutlineContentProvider());
		// viewer.setLabelProvider(new REOutlineLabelProvider());
		viewer.addSelectionChangedListener(this);
		viewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

		if (input != null) {
			viewer.setInput(input);
		}

		FT.print("end in REOutlinePage createControl");
	}

	/**
	 * {@inheritDoc}
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		super.selectionChanged(event);

		FT.print("start (after super.selectionChanged) in REOutlinePage selectionChanged");

		ISelection selection = event.getSelection();

		if (selection.isEmpty()) {
			editor.resetHighlightRange();
		} else {
			if (selection instanceof IStructuredSelection) {
				Object object = ((IStructuredSelection) selection)
						.getFirstElement();

				if (object instanceof TreeNode) {
					try {
						Position position = null;
						Object data = ((TreeNode) object).getData();
						if (data instanceof LocatedData){
							position = ((LocatedData)data).getPosition();	
						}
						
						if (position != null) {
							editor.setHighlightRange(position.offset,
									position.length, true);
							editor.getSelectionProvider().setSelection(
									new TextSelection(position.offset,
											position.length));
						}
					} catch (IllegalArgumentException x) {
						editor.resetHighlightRange();
					}
				}
			}
		}

		FT.print("end in REOutlinePage selectionChanged");

	}

	/**
	 * Inner Class: content provider
	 */
	private class REOutlineContentProvider implements ITreeContentProvider {
		private IEditorInput input;
		REOutlineParser parser;

		public REOutlineContentProvider() {
			parser = new REOutlineParser();
		}

		/**
		 * {@inheritDoc}
		 */
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof TreeNode) {
				return ((TreeNode) parentElement).getChildren();
			}
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public Object getParent(Object element) {
			if (element instanceof TreeNode) {
				return ((TreeNode) element).getParent();
			}
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean hasChildren(Object element) {
			if (element instanceof TreeNode) {
				return (((TreeNode) element).getChildren().length > 0);
			}
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		public Object[] getElements(Object inputElement) {
			if (inputElement == this.input) {
				return (Object[]) parser.getOutlineTreeArray();
			}
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public void dispose() {
			this.input = null;
		}

		/**
		 * {@inheritDoc}
		 */
		// ?????????????????????????????????????
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			FT.print("start in REOutlineContentProvider inputChanged");

			// ???????????????????????????????????????????????
			if (oldInput instanceof IEditorInput) {
				this.input = null;
			}

			if (newInput instanceof IEditorInput) {
				this.input = (IEditorInput) newInput;

				IDocument document = REOutlinePage.this.editor
						.getDocumentProvider().getDocument(this.input);

				parser.parseDocument(document);

			}

			FT.print("end in REOutlineContentProvider inputChanged");
		}

		/**
		 * Finds an existing TreeNode in the tree model.
		 * 
		 * @param treenode
		 *            The given TreeNode.
		 * 
		 * @return The TreeNode, if found. Or null if not found.
		 */
		TreeNode findEqualTreeObject(TreeNode treenode) {
			if (treenode == null) {
				return null;
			}

			TreeNode existingNode = null;
			ArrayList<TreeNode> outlineTree = parser.getOutlineTree();
			int n = outlineTree.size();
			for (int i = 0; i < n; i++) {
				existingNode = outlineTree.get(i);
				if (existingNode.equals(existingNode)) {
					return existingNode;
				}
				TreeNode[] children = existingNode.getChildren();
				for (TreeNode child : children) {
					if(existingNode.equals(child))
						return child;
				}
			}
			return null;
		}
	}
}