/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Xml;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.w3c.dom.traversal.*;
import java.io.*;

public class XmlTextReaderSupport {
	private String filename;
	private Document document;
	private boolean doneReading = false;
	int currentNodeIndex = -1;
 
	public XmlTextReaderSupport () {
	}

	private void parse() {
		if (this.document != null) {
			return;
		}
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			this.document = db.parse(filename);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException(t.getMessage());
		}
	}

	public XmlTextReaderSupport(String filename) {
		this();
		this.filename = filename;
	}

	private Node getCurrentNode() {
		if (this.currentNodeIndex == -1) {
			return this.document;
		} else {
			NodeList nl = document.getElementsByTagName("*");
			if (this.currentNodeIndex > nl.getLength() - 1) {
				return null;
			} else {

				return nl.item(this.currentNodeIndex);
			}
		}
	}

	public int moveToContent() {
		this.currentNodeIndex++;
		//System.out.println("CurrentNode is now " + this.getCurrentNode());
		return this.getType(this.getCurrentNode());
	}

	private int getType(Node n) {
		if (n instanceof Element) {
			return 0;
		} else if (n instanceof Text) {
			return 1;
		} else if (n == null) {
			return -1;
		} else {
			throw new RuntimeException("Unimplemented type for :" + n.getClass());
		}
	}

	public String getName() {
		return this.getCurrentNode().getNodeName();
	}

	public String readElementString() {
		//this.moveToContent();
		Element e = (Element) this.getCurrentNode();
		if (e == null) {
			return "";
		}
		NodeList nl = e.getChildNodes();
		if (nl.getLength() > 0) {
			Text t = (Text) nl.item(0);
			return t.getData().trim();		
		} else {
			return "";
		}
	}

	public boolean read() {
		return !this.isEOF();
	}

	public boolean isEOF() {
		this.parse();
		return this.getCurrentNode() == null;
	}
	

}

class NodeFilterImpl implements NodeFilter {
	public short acceptNode(Node n) {
		return NodeFilter.FILTER_ACCEPT;
	}
}	
