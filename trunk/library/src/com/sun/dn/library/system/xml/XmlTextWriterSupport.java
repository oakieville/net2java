/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Xml;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;


public class XmlTextWriterSupport {
	private Document document;
	private Node currentNode;
	private String filename;
	Transformer transformer;

	
	public XmlTextWriterSupport () {
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			document = db.newDocument();
			currentNode = document;
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException(t.getMessage());
		}
	}

	public XmlTextWriterSupport (String filename, String encoding) {
		this();
		this.filename = filename;
		transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
	}

	public void writeStartDocument() {
	}

	public void writeEndDocument() {
	}

	public void writeEndElement() {
		Node parent = this.currentNode.getParentNode();
		currentNode = parent;
	}

	public void close() {
		try {
			Source source = new DOMSource(this.document);
			Result result = new StreamResult(new File(filename));
			transformer.transform(source, result);
		} catch (Throwable t) {
			throw new RuntimeException(t.getMessage());
		}
	}

	public void setFormatting(int i) {
		if (i == 0) {
			this.transformer.setOutputProperty(OutputKeys.INDENT,"yes");
		}
	}


	public void writeStartElement(String text) {
		Element element = document.createElement(text);
		currentNode.appendChild(element);
		currentNode = element;
	}

	public void writeElementString(String text, String namespace) {
		Element element = document.createElement(text);
		currentNode.appendChild(element);
		Text textNode = this.document.createTextNode(namespace);
		element.appendChild(textNode);
	}


	

}
