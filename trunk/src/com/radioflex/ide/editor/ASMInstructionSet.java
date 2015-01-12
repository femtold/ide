package com.radioflex.ide.editor;

import java.io.File;
import java.util.Collections;
//import java.util.Collections;
import java.util.HashMap;
//import java.util.Vector;

import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.radioflex.ide.Activator;
import com.radioflex.ide.Constants;
import com.radioflex.ide.Messages;

/**
 * Loads an manages the instruction, which are uses in the editor.
 *
 */
public final class ASMInstructionSet {
	
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

	/**
	 * Must not be instantiated.
	 */
	private ASMInstructionSet() {
	}

	/**
	 * Returns all instructions.
	 * @return The instructions.
	 */
	public static HashMap<String, String> getInstructionMap() {

	    instructionMap = null;
			loadXMLData();
		return instructionMap;
	}

	/**
	 * Returns all segment.
	 * @return The segments.
	 */
	public static HashMap<String, String> getSegmentMap() {

		segmentMap = null;
			loadXMLData();
		return segmentMap;
	}

	/**
	 * Returns all instructions.
	 * @return The instructions.
	 */
	public static HashMap<String, String> getMacrosMap() {

		macrosMap = null;
			loadXMLData();
		return macrosMap;
	}

	/**
	 * Returns all derivatives.
	 * @return The derivatives.
	 */
	public static HashMap<String, String> getDerivativeMap() {

		derivativeMap = null;
			loadXMLData();
		return derivativeMap;
	}

	/**
	 * Returns all registers.
	 * @return The register.
	 */
	public static HashMap<String, String> getRegisterMap() {

		registerMap = null;
			loadXMLData();
		return registerMap;
	}

	/**
	 * Return the Array with the instructions.
	 * @return The instructions.
	 */
	public static String[][] getSortedInstructionArray() {

		if (sortedInstructionArray == null)
			loadXMLData();
		return sortedInstructionArray;
	}

	/**
	 * Return the Array with the segments.
	 * @return The segments.
	 */
	public static String[][] getSortedSegmentArray() {

		if (sortedSegmentArray == null)
			loadXMLData();
		return sortedSegmentArray;
	}

	/**
	 * Return the Array with the macros.
	 * @return The macros.
	 */
	public static String[][] getSortedMacrosArray() {

		if (sortedMacrosArray == null)
			loadXMLData();
		return sortedMacrosArray;
	}

	/**
	 * Return the Array with the derivative.
	 * @return The derivatives.
	 */
	public static String[][] getSortedDerivativeArray() {

		if (sortedDerivativeArray == null)
			loadXMLData();
		return sortedDerivativeArray;
	}

	/**
	 * Return the Array with the register.
	 * @return The registers.
	 */
	public static String[][] getSortedRegisterArray() {

		if (sortedRegisterArray == null)
			loadXMLData();
		return sortedRegisterArray;
	}

	/**
	 * Load the instructions.
	 */
	private static void loadXMLData() {
		if (instructionMap == null)
			instructionMap = new HashMap<String, String>();
		else
			instructionMap.clear();
		if (segmentMap == null)
			segmentMap = new HashMap<String, String>();
		else
			segmentMap.clear();
		if (macrosMap == null)
			macrosMap = new HashMap<String, String>();
		else
			macrosMap.clear();
		if (derivativeMap == null)
			derivativeMap = new HashMap<String, String>();
		else
			derivativeMap.clear();
		if (registerMap == null)
			registerMap = new HashMap<String, String>();
		else
			registerMap.clear();
		
		String root = Platform.getInstanceLocation().getURL().getPath();
		File mySyntax = new File(root + "syntax_keywords_changed.xml");
		
		if(!mySyntax.exists())	
			xmlfile = Activator.getFilePathFromPlugin("syntax_keywords.xml");
		else 
			xmlfile = root + "syntax_keywords_changed.xml";

		try {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(new File(xmlfile), new DefaultHandler() {
				public void startElement(String url, String localNmae,
						String qName, Attributes attributes)
						throws SAXException {
					if (qName.equals("instruction"))
						instructionMap.put(attributes.getValue("command"),
								attributes.getValue("description"));
					else if (qName.equals("segment"))
						segmentMap.put(attributes.getValue("field"),
								attributes.getValue("description"));
					else if (qName.equals("macros"))
						macrosMap.put(attributes.getValue("command"),
								attributes.getValue("description"));
					else if (qName.equals("derivatives"))
						derivativeMap.put(attributes.getValue("command"),
								attributes.getValue("description"));
					else if (qName.equals("register"))
						registerMap.put(attributes.getValue("command"),
								attributes.getValue("description"));
				}
			});
		} catch (Exception e) {
			Activator
					.getDefault()
					.getLog()
					.log(new Status(Status.ERROR, Constants.PLUGIN_ID,
							Status.OK, Messages.LOAD_ASMISET_ERROR, e));
		}
		
		sortedInstructionArray = new String[instructionMap.size()][3];
		sortedSegmentArray = new String[segmentMap.size()][3];
		sortedMacrosArray = new String[macrosMap.size()][3];
		sortedDerivativeArray = new String[derivativeMap.size()][3];
		sortedRegisterArray = new String[registerMap.size()][3];
		
		Vector<String> sortVector = new Vector<String>(instructionMap.keySet());
		Collections.sort(sortVector);
		int pos = 0;
		
		for(String element : sortVector){
			sortedInstructionArray[pos][0] = new String(element);
			sortedInstructionArray[pos][1] = new String(element.toLowerCase());
			sortedInstructionArray[pos][2] = new String((String)instructionMap.get(element));
			pos++;
		}
		
	    sortVector = new Vector<String>(segmentMap.keySet());
		Collections.sort(sortVector);
		pos = 0;
		
		for(String element : sortVector){
			sortedSegmentArray[pos][0] = new String(element);
			sortedSegmentArray[pos][1] = new String(element.toLowerCase());
			sortedSegmentArray[pos][2] = new String((String)segmentMap.get(element));
			pos++;
		}
		
	    sortVector = new Vector<String>(macrosMap.keySet());
		Collections.sort(sortVector);
	    pos = 0;
		
		for(String element : sortVector){
			sortedMacrosArray[pos][0] = new String(element);
			sortedMacrosArray[pos][1] = new String(element.toLowerCase());
			sortedMacrosArray[pos][2] = new String((String)macrosMap.get(element));
			pos++;
		}
		
		sortVector = new Vector<String>(derivativeMap.keySet());
		Collections.sort(sortVector);
		pos = 0;
		
		for(String element : sortVector){
			sortedDerivativeArray[pos][0] = new String(element);
			sortedDerivativeArray[pos][1] = new String(element.toLowerCase());
			sortedDerivativeArray[pos][2] = new String((String)derivativeMap.get(element));
			pos++;
		}
		
		sortVector = new Vector<String>(registerMap.keySet());
		Collections.sort(sortVector);
		pos = 0;
		
		for(String element : sortVector){
			sortedRegisterArray[pos][0] = new String(element);
			sortedRegisterArray[pos][1] = new String(element.toLowerCase());
			sortedRegisterArray[pos][2] = new String((String)registerMap.get(element));
			pos++;
		}
		
	}

}
