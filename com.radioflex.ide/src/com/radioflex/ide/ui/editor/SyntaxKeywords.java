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

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.part.ResourceTransfer;

import com.radioflex.ide.ui.Constants;
import com.radioflex.ide.ui.Messages;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.radioflex.ide.ui.Activator;

public final class SyntaxKeywords {
	private static HashMap<String, String> instructionMap = null;
	private static HashMap<String, String> segmentMap = null;
	private static HashMap<String, String> macrosMap = null;
	private static HashMap<String, String> derivativeMap = null;
	private static HashMap<String, String> registerMap = null;
	private static String[][] sortedInstructionArray = null;
	private static String[][] sortedSegmentArray = null;
	private static String[][] sortedMacrosArray = null;
	private static String[][] sortedDerivativeArray = null;
	private static String[][] sortedRegisterArray = null;
	private static String xmlfile;

	private SyntaxKeywords() {
	}

	public static HashMap<String, String> getInstructions() {
		instructionMap = null;
		loadXMLData();
		return instructionMap;
	}

	public static HashMap<String, String> getSegments() {
		segmentMap = null;
		loadXMLData();
		
		return segmentMap;
	}
	
	public static HashMap<String, String> getMacros() {
		macrosMap = null;
		loadXMLData();
		
		return macrosMap;
	}
	
	public static HashMap<String, String> getDerivative() {
		derivativeMap = null;
		loadXMLData();
		return derivativeMap;
	}
	
	public static HashMap<String, String> getRegister() {
		registerMap = null;
		loadXMLData();
		return registerMap;
	}
	
	public static String[][] getInstructionArray() {
		sortedInstructionArray = null;
		loadXMLData();
		
		return sortedInstructionArray;
	}

	public static String[][] getSegmentArray() {
		sortedSegmentArray = null;
		loadXMLData();
		
		return sortedSegmentArray;
	}
	
	public static String[][] getMacrosArray() {
		sortedMacrosArray = null;
		loadXMLData();
		return sortedMacrosArray;
	}
	
	public static String[][] getDerivativeArray() {
		sortedDerivativeArray = null;
		loadXMLData();
		return sortedDerivativeArray;
	}
	
	public static String[][] getRegisterArray() {
		sortedRegisterArray = null;
		loadXMLData();
		
		return sortedRegisterArray;
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
		
		if (macrosMap == null) {
			macrosMap = new HashMap<String, String>();
		} else {
			macrosMap.clear();
		}
		
		if (derivativeMap == null) {
			derivativeMap = new HashMap<String, String>();
		} else {
			derivativeMap.clear();
		}
		
		if (registerMap == null) {
			registerMap = new HashMap<String, String>();
		} else {
			registerMap.clear();
		}
		
		//选择默认配置还是用户定义的配置
		String root = Platform.getInstanceLocation().getURL().getPath();
		File mysyntax = new File(root + "syntax_keywords2.xml");
		String xmlfile = new String();

		if(mysyntax.exists()){
			xmlfile = Platform.getInstanceLocation().getURL().getPath()+"syntax_keywords2.xml";}
		else{
			xmlfile = Activator.getFilePathFromPlugin("syntax_keywords.xml");}
		try{
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
					}else if (qName.equals("macros")) {
						macrosMap.put(attributes.getValue("command"),
								attributes.getValue("description"));
					}else if (qName.equals("derivatives")) {
						derivativeMap.put(attributes.getValue("command"),
								attributes.getValue("description"));
					}else if (qName.equals("register")) {
						registerMap.put(attributes.getValue("command"),
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
		sortedMacrosArray = new String[macrosMap.size()][3];
		sortedDerivativeArray = new String[derivativeMap.size()][3];
		sortedRegisterArray = new String[registerMap.size()][3];
		
		//instruction
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
		
		//segment
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
		
		//macros
		sortVector = new Vector<String>(macrosMap.keySet());
		Collections.sort(sortVector);
		pos = 0;

		for (String element : sortVector) {
			sortedMacrosArray[pos][0] = new String(element);
			sortedMacrosArray[pos][1] = new String(element.toLowerCase());
			sortedMacrosArray[pos][2] = new String(
					(String) macrosMap.get(element));
			pos++;
		}
		
		//derivative
		sortVector = new Vector<String>(derivativeMap.keySet());
		Collections.sort(sortVector);
		pos = 0;

		for (String element : sortVector) {
			sortedDerivativeArray[pos][0] = new String(element);
			sortedDerivativeArray[pos][1] = new String(element.toLowerCase());
			sortedDerivativeArray[pos][2] = new String(
					(String) derivativeMap.get(element));
			pos++;
		}
		
		//register
		sortVector = new Vector<String>(registerMap.keySet());
		Collections.sort(sortVector);
		pos = 0;

		for (String element : sortVector) {
			sortedRegisterArray[pos][0] = new String(element);
			sortedRegisterArray[pos][1] = new String(element.toLowerCase());
			sortedRegisterArray[pos][2] = new String(
					(String) registerMap.get(element));
			pos++;
		}
	}

}
