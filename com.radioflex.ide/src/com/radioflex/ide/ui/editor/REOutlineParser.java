package com.radioflex.ide.ui.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.editors.text.TextEditor;

import com.radioflex.ide.*;

public class REOutlineParser {

	public enum NodeType {
		SECTION(0), LEBAL(1), MACRO(2), REGISTER(3);

		private final int index;

		NodeType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}
	}

	public class NodeData {
		// protected NodeType type;
		protected String text;

		// constructors
		public NodeData() {
		}

		public NodeData(String text) {
			this.text = text;
		}

		// public NodeData(NodeType type) {
		// this.type = type;
		// }
		// public NodeData(NodeType type, String text) {
		// this.type = type;
		// this.text = text;
		// }
		//
		// // getters and setters
		// public NodeType getType() {
		// return type;
		// }
		//
		// public void setType(NodeType type) {
		// this.type = type;
		// }

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		/** return text for display */
		public String toString() {
			return this.text;
		}

	}

	public class LocatedData extends NodeData {
		private Position position;

		// constructors
		public LocatedData() {
		}

		public LocatedData(String text) {
			super(text);
		}

		// public LocatedData(NodeType type) {
		// super(type);
		// }
		//
		// public LocatedData(NodeType type, String text) {
		// super(type, text);
		// }

		public Position getPosition() {
			return position;
		}

		public void setPosition(Position position) {
			this.position = position;
		}

	}

	// public class MultiLocatedData extends NodeData{
	// }

	private ArrayList<TreeNode> outlineTree = new ArrayList<TreeNode>();
	private TreeNode sections = new TreeNode(new NodeData("Sections"));

	public REOutlineParser() {
		outlineTree.add(sections);
	}

	public ArrayList<TreeNode> getOutlineTree() {
		return outlineTree;
	}

	public TreeNode[] getOutlineTreeArray() {
		return outlineTree.toArray(new TreeNode[0]);
	}

	/**
	 * Parse the new input and build up the tree.
	 */
	public void parseDocument(IDocument document) {

		sections.setChildren(null);

		if (document != null) {
			int lines = document.getNumberOfLines();
			int lineOffset, pos, linelen, matchStart, matchEnd, startOffset, length;
			String name = "";
			String stringLine = "";
			String stringLineLower = "";
			StringBuffer filterBuffer = new StringBuffer();
			Pattern pattern = null;
			Matcher matcher = null;
			TreeNode child = null;

			lineOffset = 0;
			pos = 0;
			linelen = 0;
			matchStart = 0;
			matchEnd = 0;
			startOffset = 0;
			length = 0;

			FT.print("\nbefore parse>>>>>>>>>>>>>>>>");

			for (int line = 0; line < lines; line++) {
				try {
					lineOffset = document.getLineOffset(line);
					linelen = document.getLineLength(line);
					stringLine = document.get(lineOffset, linelen);
					filterBuffer.setLength(0);

					for (pos = 0; pos < linelen; pos++) {
						if (document.getPartition(lineOffset + pos).getType()
								.equals(IDocument.DEFAULT_CONTENT_TYPE)) {
							filterBuffer.append(stringLine.substring(pos,
									pos + 1));
						} else {
							filterBuffer.append(" ");
						}
					}

					stringLine = filterBuffer.toString();
					stringLineLower = stringLine.toLowerCase();

					FT.print("lineOffset:" + lineOffset + " linelen:" + linelen);
					System.out.printf("%s", stringLine);

					// ---------------------------------------

					if (stringLineLower.indexOf(".") > -1) {
						pattern = Pattern.compile("(\\A|\\W)\\.(\\w+[:]*)");
						matcher = pattern.matcher(stringLineLower);

						if (matcher.find()) {
							matchStart = matcher.start(2);// group2:(\\w+[:]*)
							matchEnd = matcher.end(2);
							startOffset = lineOffset + matchStart;
							length = lineOffset + matchEnd - startOffset;

							name = stringLine.substring(matchStart, matchEnd);

							if (name.codePointAt(name.length() - 1) != ':') {

								// LocatedData data =new LocatedData(name);
								// for Test!!!!!!!!!!!!
								LocatedData data = new LocatedData(name
										+ " startOffset:" + startOffset
										+ " length:" + length);
								data.setPosition(new Position(startOffset,
										length));
								child = new TreeNode(data);

								sections.addChild(child);
							}
						}
					}
				} catch (BadLocationException e) {
					Activator
							.getDefault()
							.getLog()
							.log(new Status(Status.ERROR, Constants.PLUGIN_ID,
									Status.OK, Messages.BADLOCATION_ERROR, e));
				}
			}

			FT.print("<<<<<<<<<<<<<<<<<<after parse\n");

		}
	}
}
