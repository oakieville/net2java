/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Xml;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import java.util.*;



public class XmlDocumentSupport extends XmlNodeSupport {


	public XmlDocumentSupport() {
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        super.node = db.newDocument();
                        //System.out.println(super.node.getOwnerDocument());
                } catch  (Exception e) {
			throw new RuntimeException(e.getMessage());
                    
                }
	}
        
        public XmlNodeSupport getFirstChild() {
            return new XmlNodeSupport(this.node.getFirstChild());
        }
        
        public XmlNodeSupport createElement(String tagName) {
            //System.out.println("Create Element for tag " + tagName);
            //System.out.println(super.node.getOwnerDocument());
            Element e;
            if (super.node instanceof Document) {
                e = ((Document) node).createElement(tagName);
            } else {
                 e = super.node.getOwnerDocument().createElement(tagName);
            }
            return new XmlNodeSupport(e);
        }

	public void load(String filename) {
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			File f = new File(filename);
			super.node = db.parse(f);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public XmlNodeSupport selectSingleNode(String xPath) {
		// (dannyc)
                StringTokenizer str = new StringTokenizer(xPath, "/");
                String lastPart = "";
                while (str.hasMoreTokens()) {
                    lastPart = str.nextToken();
                }
		
		//System.out.println("search for  " + lastPart  + " in " + node);
		NodeList nl = ((Document) node).getElementsByTagName(lastPart);
		Node n = nl.item(0);
		//System.out.println("found " + n);

		return new XmlNodeSupport(n);
	}


	public void save(String filename) {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Source source = new DOMSource(this.node);
			Result result = new StreamResult(new File(filename));
			transformer.transform(source, result);
		} catch (Throwable t) {
			throw new RuntimeException(t.getMessage());
		}
	}
        
        public String toString() {
            return "XmlDocumentSupport: " + super.node + " "  + super.node.getChildNodes().getLength();
        }



}
