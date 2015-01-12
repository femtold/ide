package com.radioflex.ide.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Parse XML file, get the root element. 
 *
 */
public class ParseXML {
	Element rootElement;
	
	public ParseXML(File file){
		parse(file);
	}
	
	private void parse(File file){
		try{
			InputStream inputStream = new FileInputStream(file);
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(inputStream);
			rootElement = document.getRootElement();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Element getRootElement(){
		return rootElement;
	}
	
}
