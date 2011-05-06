/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Xml;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import com.sun.dn.util.*;

public class XmlNodeSupport {
	Node node;
        
        protected  XmlNodeSupport() {
        
        }

	public XmlNodeSupport(Node node) {
            //System.out.println("Creating node support with node = " + node);
            this.node = node;
	}
        
        public XmlNodeListSupport getChildNodes() {
            return new XmlNodeListSupport(node.getChildNodes());
        }
        
  
        
        public XmlNodeSupport appendChild(XmlNodeSupport nodeSupport) {
            this.node.appendChild(nodeSupport.node);
            return nodeSupport;
        }

	public XmlNodeSupport selectSingleNode(String xPath) {
		if (true) throw new RuntimeException("not implemented");
		return null;
	}
      
	
	public String getInnerText() {
		if (this.node == null) {
			return "";
		}
		NodeList nl = this.node.getChildNodes();
                if ( nl.item(0) != null) {
                    Text t = (Text) nl.item(0);
                    return t.getData().trim();	
                } 
                return "";
	}

	public void setInnerText(String innerText) {
		if (this.node == null) {
			return;
		}
		NodeList nl = this.node.getChildNodes();
                if (nl.item(0) instanceof Text) {
                    Text t = (Text) nl.item(0);
                    t.setData(innerText);	
                } else {
                    Text t = this.node.getOwnerDocument().createTextNode(innerText);
                   this.node.appendChild(t);
                }
                
		
	}
        
        public String toString() {
            return "XmlNodeSupport: " + node;
        }

}
