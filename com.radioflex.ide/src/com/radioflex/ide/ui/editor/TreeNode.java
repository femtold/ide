package com.radioflex.ide.ui.editor;

import java.util.ArrayList;

/**
 * instance of a simple tree node.
 */
public class TreeNode {

	/** data object associated with this node */
	private Object data = null;
	/** parent of this node (default: this) */
	private TreeNode parent = this;
	/** list of children of this node */
	private ArrayList<TreeNode> children = new ArrayList<TreeNode>();

	public static boolean isRoot(TreeNode node) {
		return node.equals(node.getParent());
	}
//	public static void link(TreeNode parent, TreeNode child){
//		child.setParent(parent);
//		parent.addChild(child);
//	}

	public TreeNode() {
	}

	public TreeNode(TreeNode parent) {
		this.parent = parent;
	}

	public TreeNode(Object data) {
		this.data = data;
	}

	public TreeNode(TreeNode parent, Object data) {
		this.parent = parent;
		this.data = data;
	}

	public TreeNode getRoot() {
		TreeNode p = this;
		while (!isRoot(p)) {
			p = p.getParent();
		}
		return p;
	}

	public TreeNode getParent() {
		return this.parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public TreeNode[] getChildren() {
		return children.toArray(new TreeNode[0]);
	}

	public void setChildren(TreeNode[] children) {
		this.children.clear();

		if (children != null) {
			for (TreeNode child : children) {
				child.setParent(this);
				this.children.add(child);
			}
		}
	}

	public TreeNode getChild(int position) {
		return children.get(position);
	}

	public void addChild(TreeNode child) {
		child.setParent(this);
		children.add(child);
	}

	public void removeChild(TreeNode child) {
		children.remove(child);
	}

	public void removeChild(int position) {
		children.remove(position);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	//????????????????????????????????????
	// public boolean equals(TreeNode treeNodeObj) {
	// if (!(treeNodeObj instanceof TreeNode)) {
	// return false;
	// }
	// return this.equals(treeNodeObj);
	// }

	public String toString() {		
		return this.getData().toString();

	}
}
