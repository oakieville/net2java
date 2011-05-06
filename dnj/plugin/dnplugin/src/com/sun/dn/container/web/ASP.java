
 /* 
 * Copyright (c) 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *  
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *  
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *  
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *  
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package com.sun.dn.container.web;

import javax.xml.parsers.*;
import java.io.*;
import org.w3c.dom.*;
import java.util.*;

import com.sun.dn.parser.*;
import com.sun.dn.*;
import com.sun.dn.util.*;

import javax.xml.transform.*; 
import javax.xml.transform.dom.*; 
import javax.xml.transform.stream.*; 

	/** Class that parses ASPs and can write equivalent JSPs.
	** @author danny.coward@sun.com
	**/

public class ASP {
	private String PAGE_DECL = "<%@ Page"; 
	// Need to parse this guy: <%@ Page Language="vb" AutoEventWireup="false" Codebehind="WebForm1.aspx.vb" Inherits="WebApplicationX.WebForm1"%>
	private String DOC_TYPE = "<!DOCTYPE HTML PUBLIC";
	private String META = "<META";
	private static String JAVA_TAGS = "<%@ page contentType=" + '"' + "text/html" + '"' + " language=" + '"' + "java" + '"' +   " session=" + '"' + "true" + '"' +    "autoFlush=" + '"' + "false" + '"' +   " %>";
	private static String HTML_TAGS = "<%@ taglib uri=" + '"' + "http://java.sun.com/jsf/html" + '"' + " prefix=" + '"' + "h" + '"' + " %>";
	private static String JSF_TAGS = "<%@ taglib uri=" + '"' + "http://java.sun.com/jsf/core" + '"' + " prefix=" + '"' + "f" + '"' + " %>";
	private Document d;
	private Map componentMap = new HashMap();
	private String name;
	private String libraryDir;

	public ASP(String data, String name, String libraryDir) throws Exception {
		this.name = name;
		this.libraryDir = libraryDir;
		String xmlData = this.coerceIntoXML(data);
		Debug.logn("Parse " + xmlData, this);
		ByteArrayInputStream bais = new ByteArrayInputStream(xmlData.getBytes());
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		d = db.parse(bais);	
	}

	public String getName() {
		return this.name;
	}

	public Map getComponentMap() {
		return componentMap;
	}

	public DNVariable getComponent(String name) {
		for (Iterator itr = this.componentMap.keySet().iterator(); itr.hasNext();) {
			DNVariable next = (DNVariable) itr.next();
			if (next.getName().equals(name)) {
				return next;
			}
		}
		return null;
	}

	public void addActionListenerFor(DNVariable v, String listenerClassname) {
		Debug.logn(" add action Listener for " + v, this);
		Element e = (Element) componentMap.get(v);
	
		if (e.getTagName().equals("h:commandButton")) {
			Element listenerElt = d.createElement("f:actionListener");
			listenerElt.setAttribute("type", listenerClassname );
			e.appendChild(listenerElt);
			Debug.logn(" ASP: add action Listener for " + v + " DONE", this);
		} else if (e.getTagName().equals("h:selectOneRadio")) {
			Element listenerElt = d.createElement("f:valueChangeListener");
			listenerElt.setAttribute("type", listenerClassname );
			e.appendChild(listenerElt);
			Debug.logn(" ASP: add action Listener for " + v + " DONE", this);
		} else {
			throw new RuntimeException("Don't know how to add action listener for " + v);
		}

	}

	public void translate() {
		//NodeList nl = d.getElementsByTagName("HTML");
		//Node htmlNode = nl.item(0);
		//htmlNode.appendChild(this.getDoPostBackNode());
		NodeList nl = d.getElementsByTagName("form");
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			this.translateForm((Element) n);
		}
	}

	private Node getDoPostBackNode() {
		try {
			String script = Util.getString(this.libraryDir + File.separator + "VBJUtil" + File.separator + "DoPostBack.javascript");
			Debug.logn(script, this);
			ByteArrayInputStream bais = new ByteArrayInputStream(script.getBytes());
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document scriptDoc = db.parse(bais);
			Node scriptNode = scriptDoc.getElementsByTagName("script").item(0);
			Node importedNode = d.importNode(scriptNode, true);
			return importedNode;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new Stop(this.getClass());
		}
	}

	private void translateForm(Element aspFormElt) {
		Node parent = aspFormElt.getParentNode();
		//parent.appendChild(this.getDoPostBackNode());

		Element e = d.createElement("h2");
		Node formTitleNode = d.createTextNode("A Form !");
		//e.appendChild(formTitleNode);
		parent.replaceChild(e, aspFormElt);
		
		Element facesViewElt = d.createElement("f:view");
		parent.appendChild(facesViewElt );


		Element jspFormElt = d.createElement("h:form");

		jspFormElt .setAttribute("id", aspFormElt.getAttribute("id") );
		facesViewElt.appendChild(jspFormElt);

		this.translateAspComponents(aspFormElt, jspFormElt);
		e = d.createElement("h2");
		//e.appendChild(d.createTextNode("End Form"));
		parent.appendChild(e);
	}

	private void translateAspComponents(Node formNode, Node parent) {
		NodeList nl = formNode.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			Node translatedComponent = null;
			if (n.getNodeName().toLowerCase().equals("asp:button")) {
				translatedComponent  = this.translateButton((Element) n);
			} else if (n.getNodeName().toLowerCase().equals("asp:textbox")) {
				translatedComponent  = this.translateTextField((Element) n);
			} else if (n.getNodeName().toLowerCase().equals("asp:image")) {
				translatedComponent  = this.translateImage((Element) n);
			} else if (n.getNodeName().toLowerCase().equals("asp:label")) {
				translatedComponent  = this.translateLabel((Element) n);
			} else if (n.getNodeName().toLowerCase().equals("asp:radiobutton")) {
				translatedComponent  = this.translateRadioButton((Element) n);
			} else if (n.getNodeName().toLowerCase().equals("asp:imagebutton")) {
				translatedComponent  = this.translateImageButton((Element) n);
			} else if (n.getNodeName().toLowerCase().equals("asp:listbox")) {
				translatedComponent  = this.translateListBox((Element) n);
			} else if (n.getNodeName().toLowerCase().equals("asp:panel")) {
				translatedComponent  = this.translatePanel((Element) n);


			} else if (n.getNodeName().toLowerCase().equals("#text")) {

			} else {
				throw new RuntimeException("Cannot translate node: " + n.getNodeName());
			}
			if (translatedComponent != null) {
				parent.appendChild(translatedComponent );
			}
		}
	}

	private Node translatePanel(Element pNode) {
		Element item = d.createElement("h:panelGrid");
		item.setAttribute("id",  pNode.getAttribute("id") );
		item.setAttribute("bgcolor",  pNode.getAttribute("BackColor") );
		item.setAttribute("width",  pNode.getAttribute("Width") );
		item.setAttribute("columns", "2" );
		this.translateAspComponents(pNode, item);
		
		DNVariable v = DNVariable.createVBVariable(pNode.getAttribute("id"), "System.Web.UI.WebControls.Panel");
		componentMap.put(v, item );
		return item;
	}


	private Node translateRadioButton(Element rbNode) {
		Element item = d.createElement("f:selectItem");
		item.setAttribute("itemValue", "checked");
		item.setAttribute("itemLabel", rbNode.getAttribute("Text") );
		Element e = d.createElement("h:selectOneRadio");
		e.setAttribute("id", rbNode.getAttribute("id") );

		if ("True".equals(rbNode.getAttribute("AutoPostBack"))) {
			this.addAutoPostBackTo(e, rbNode.getAttribute("id") );
		}
		DNVariable v = DNVariable.createVBVariable(rbNode.getAttribute("id"), "System.Web.UI.WebControls.RadioButton");
		componentMap.put(v, e);
		e.appendChild(item);
		return e;
	}

	private void addAutoPostBackTo(Element en, String senderName) {
		en.setAttribute("onclick", "doPostBack('"+"Form1:RadioButton1"+"','')");
	}

	private Node translateLabel(Element labelNode) {
		Element e = d.createElement("h:outputLabel");
		e.setAttribute("id", labelNode.getAttribute("id") );
		e.setAttribute("value", Util.getText(labelNode) );
		DNVariable v = DNVariable.createVBVariable(labelNode.getAttribute("id"), "System.Web.UI.WebControls.Label");
		componentMap.put(v, e);
		return e;
	}

	private Node translateListBox(Element lbNode) {
		Element e = d.createElement("h:selectOneListbox");
		e.setAttribute("id", lbNode.getAttribute("id") );
		NodeList nl = lbNode.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n =  nl.item(i);
			if (n instanceof Element && ((Element)n).getTagName().equals("asp:ListItem")) {
				Element item = d.createElement("f:selectItem");
				item.setAttribute("itemLabel", ((Element)n).getAttribute("Value"));
				item.setAttribute("itemValue", ((Element)n).getAttribute("Value"));

				e.appendChild(item);
			}
		}
		DNVariable v = DNVariable.createVBVariable(lbNode.getAttribute("id"), "System.Web.UI.WebControls.ListBox");
		componentMap.put(v, e);
		return e;
	}



	private Node translateImage(Element imageNode) {
		Element e = d.createElement("h:graphicImage");
		e.setAttribute("id", imageNode.getAttribute("id") );
		e.setAttribute("url", imageNode.getAttribute("ImageUrl") );
		e.setAttribute("width", imageNode.getAttribute("Width") );
		e.setAttribute("height", imageNode.getAttribute("Height") );

		//e.setAttribute("immediate", "true" );
		DNVariable v = DNVariable.createVBVariable(imageNode.getAttribute("id"), "System.Web.UI.WebControls.Image");
		componentMap.put(v, e);
		return e;


	}

	private Node translateImageButton(Element aspButtonNode) {
		Element e = d.createElement("h:commandButton");
		e.setAttribute("id", aspButtonNode.getAttribute("id") );
		e.setAttribute("image", aspButtonNode.getAttribute("ImageUrl") );
		//e.setAttribute("width", aspButtonNode.getAttribute("Width") );
		//e.setAttribute("height", aspButtonNode.getAttribute("Height") );

		DNVariable v = DNVariable.createVBVariable(aspButtonNode.getAttribute("id"), "System.Web.UI.WebControls.ImageButton");
		componentMap.put(v, e);
		return e;
	}


	private Node translateButton(Element aspButtonNode) {
		Element e = d.createElement("h:commandButton");
		e.setAttribute("id", aspButtonNode.getAttribute("id") );
		e.setAttribute("value", aspButtonNode.getAttribute("Text") );
		//e.setAttribute("width", aspButtonNode.getAttribute("Width") );
		//e.setAttribute("height", aspButtonNode.getAttribute("Height") );

		DNVariable v = DNVariable.createVBVariable(aspButtonNode.getAttribute("id"), "System.Web.UI.WebControls.Button");
		componentMap.put(v, e);
		return e;
	}

	private Node translateTextField(Element aspTextBoxNode) {
		Element e = d.createElement("h:inputText");
		e.setAttribute("id", aspTextBoxNode.getAttribute("id") );
		DNVariable v = DNVariable.createVBVariable(aspTextBoxNode.getAttribute("id"), "System.Web.UI.WebControls.TextBox");
		componentMap.put(v, e);
		return e;
	}


	private String coerceIntoXML(String data) {
		StringTokenizer st = new StringTokenizer(data, "\n");
		String s = "<?xml version=" + '"' + "1.0" + '"' + "?>";
		while (st.hasMoreElements()) {
			String nextLine = st.nextToken();
			if (nextLine.startsWith(PAGE_DECL) || 
				nextLine.toUpperCase().startsWith(DOC_TYPE) ||
				nextLine.toUpperCase().trim().startsWith(META)) {
				Debug.logn("Skipping: " + nextLine, this);
			} else {
				s = s + "\n" + nextLine;
			}
		}
		return s;
	}

	private String insertJSPAndTagDirectives(String xmlDoc) {
		String jsp = "";
		StringTokenizer st = new StringTokenizer(xmlDoc, "\n");
		while(st.hasMoreTokens()) {
			String nextLine = st.nextToken();
			jsp = jsp + "\n" + nextLine;
			if (nextLine.toUpperCase().trim().startsWith("<HEAD>")) {
				jsp = jsp + "\n" + JAVA_TAGS;
				jsp = jsp + "\n" + HTML_TAGS;
				jsp = jsp + "\n" + JSF_TAGS;
			}
		}
		return jsp;
	}

	public void write(OutputStream os) throws Exception {
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		writeXML(boas);
		String xml = new String(boas.toByteArray());
		String jsp = insertJSPAndTagDirectives(xml);
		os.write(jsp.getBytes());
	}

	public void writeXML(OutputStream os) throws Exception {
		TransformerFactory tFactory = TransformerFactory.newInstance(); 
		Transformer transformer = tFactory.newTransformer(); 
		DOMSource source = new DOMSource(d); 
		StreamResult result = new StreamResult(os); 
		transformer.transform(source, result); 
	}


}
 