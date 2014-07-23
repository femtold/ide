package com.radioflex.ide.ui.editor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.Status;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.radioflex.ide.ui.Constants;
import com.radioflex.ide.ui.Messages;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.radioflex.ide.ui.Activator;

public final class SyntaxKeywords {
	private static HashMap<String, String> instructionMap = null;
	private static HashMap<String, String> segmentMap = null;
	private static String[][] sortedInstructionArray = null;
	private static String[][] sortedSegmentArray = null;


	private SyntaxKeywords() {
	}

	public static HashMap<String, String> getInstructions() {
		if (instructionMap == null) {
			loadXMLData();
		}
		return instructionMap;
	}

	public static HashMap<String, String> getSegments() {
		if (segmentMap == null) {
			loadXMLData();
		}
		return segmentMap;
	}

	public static String[][] getInstructionArray() {
		if (sortedInstructionArray == null) {
			loadXMLData();
		}
		return sortedInstructionArray;
	}

	public static String[][] getSegmentArray() {
		if (sortedSegmentArray == null) {
			loadXMLData();
		}
		return sortedSegmentArray;
	}

	private static void loadXMLData() {
		if (instructionMap == null) {
			instructionMap = new HashMap<String, String>();
		} else {
			instructionMap.clear();
		}

		if (segmentMap == null) {
			segmentMap = new HashMap<String, String>();
		} else {
			segmentMap.clear();
		}

		String xmlfile = Activator
				.getFilePathFromPlugin("syntax_keywords.xml");

		try {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(new File(xmlfile), new DefaultHandler() {
				public void startElement(String uri, String localName,
						String qName, Attributes attributes)
						throws SAXException {
					if (qName.equals("instruction")) {
						instructionMap.put(attributes.getValue("command"),
								attributes.getValue("description"));
					} else if (qName.equals("segment")) {
						segmentMap.put(attributes.getValue("field"),
								attributes.getValue("description"));
					}
				}
			});
		} catch (Exception e) {
			Activator
					.getDefault()
					.getLog()
					.log(new Status(Status.ERROR, Constants.PLUGIN_ID,
							Status.OK,
							Messages.LOAD_ASMISET_ERROR, e));
		}

		sortedInstructionArray = new String[instructionMap.size()][3];
		sortedSegmentArray = new String[segmentMap.size()][3];

		Vector<String> sortVector = new Vector<String>(instructionMap.keySet());
		Collections.sort(sortVector);
		int pos = 0;

		for (String element : sortVector) {
			sortedInstructionArray[pos][0] = new String(element);
			sortedInstructionArray[pos][1] = new String(element.toLowerCase());
			sortedInstructionArray[pos][2] = new String(
					(String) instructionMap.get(element));
			pos++;
		}

		sortVector = new Vector<String>(segmentMap.keySet());
		Collections.sort(sortVector);
		pos = 0;

		for (String element : sortVector) {
			sortedSegmentArray[pos][0] = new String(element);
			sortedSegmentArray[pos][1] = new String(element.toLowerCase());
			sortedSegmentArray[pos][2] = new String(
					(String) segmentMap.get(element));
			pos++;
		}
	}

}
