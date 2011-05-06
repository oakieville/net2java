
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
package com.sun.dn.library;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import com.sun.dn.*;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import com.sun.dn.parser.DNEvent;
import com.sun.dn.parser.Signature;
import com.sun.dn.library.JavaExpression;
import com.sun.dn.util.Debug;
import com.sun.dn.util.Util;


class EntryParser {
        public static String COPYRIGHT_NOTICE = "\nCopyright 2006 Sun Microsystems, Inc. All rights reserved.\nUse is subject to license terms.\n";
        public static String ALIAS = "alias";
	public static String TRANSLATION = "translation";
	public static String PROPERTIES = "properties";
	public static String PROPERTY = "property";
	public static String NAME = "name";
	public static String GET = "get";
	public static String SET = "set";
	public static String METHODS = "methods";
	public static String METHOD = "method";
	public static String JAVA = "java";
        public static String JAVA_EVENT_LISTENER = "java-listener-ifc";
	public static String NET_CLASSNAME = "net-classname";
	public static String NET_TYPE = "net-type";
        public static String VB_SIGNATURE = "vb-signature";
	public static String CLASS = "class";
	public static String ENUMERATION = "enumeration";
	public static String INTERFACE = "interface";
	public static String DELEGATE = "delegate";
	public static String IMPORT = "import";
	public static String INHERITS = "inherits";
	public static String IMPLEMENTS = "implements";
	public static String EVENT_DEFN = "event-defn";
	public static String EVENT = "event";
        public static String EVENTS = "events";
	public static String ADD_LISTENER = "add-listener";
	public static String LISTENER_METHOD = "listener-method";
	public static String RUNTIME = "runtime";
        public static String STRUCTURE = "structure";
        public static String EVENT_TYPE = "type";

	String dnTypename = null;
	String javaClassname = "";
	String classImport;
	String inheritsClassname;
	List implementsList = new ArrayList();
	Map listenerAddMethods = new HashMap();
	Map listenerMethods = new HashMap();
        Map listenerJavaInterfaces = new HashMap();
	Map propertyTypes = new HashMap();
	boolean isRuntime = false;
        String aliasName;

	String type;

	DNEvent dnEvent;
	String delegateType;
	//String javaEventListener = "";
	//String eventImport = "";

	File myFile;

	private Map methodSigToJavaExpressionMap = new HashMap();
        List propertyTranslations = new ArrayList();

	boolean printImportDebug = false;

	public EntryParser(File f) {
		myFile = f;
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document d = db.parse(f);
                        //db.
			this.parseFromNode(EntryParser.getFirstElement(d));
			if (dnTypename == null && dnEvent == null) {
				throw new RuntimeException("" + f + " is missing a class or event-defn decl");
			}
			Debug.logn("OK - " + f, this);
		

		} catch (Throwable t) {
                        System.out.println("Error in file " + f);
			t.printStackTrace();
			throw new RuntimeException("Stop");
		}
	}
        
        private EntryParser() {}
        
        public static Element getFirstElement(Document d) {
            NodeList nl = d.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i) instanceof Element) {
                    return (Element) nl.item(i);
                }
            }
            return null;
        }
        
        public static EntryParser createDefaultEntryParser() {
            EntryParser ep = new EntryParser();
            ep.dnTypename = "dnTypename";
            ep.javaClassname = "java.lang.Object";
            ep.inheritsClassname = "System.Object";
            ep.type = Entry.CLASS_TYPE;
            return ep;
        }
        
        public static EntryParser createDefaultEventEntryParser() {
            EntryParser ep = new EntryParser();
            ep.type = Entry.EVENT_TYPE;
            ep.dnEvent = new DNEvent("NewName");
            ep.dnTypename = "New .NET class name";
            ep.delegateType = "System.Object";
            return ep;
        }
        
       Map getMethodSigToJavaExpressionMap() {
            return methodSigToJavaExpressionMap;
        }

	public String toString() {
		return "EP: " + myFile.getName();
	}

	public String getType() {
		return this.type;
	}

	

		/**
		<translation: properties+, methods+>
		<properties: property*>
		<methods: method*>
		*/

		// the <translation> node
	private void parseFromNode(Element n) {
		if (!n.getTagName().equals(TRANSLATION)) {
			throw new RuntimeException("Expecting translation, got : " + n.getNodeValue());
		}
		NodeList nl = n.getChildNodes();
		
		for (Iterator itr = Util.getElts(n).iterator(); itr.hasNext();) {
			Element e = (Element) itr.next();
			if (e.getTagName().equals(PROPERTIES)) {
				this.parseProperties(e);
			} else if (e.getTagName().equals(METHODS)) {
				this.parseMethods(e);
                        } else if (e.getTagName().equals(EVENTS)) {
                                this.parseEventListeners(e);
			} else if (e.getTagName().equals(CLASS)) {
				this.type = Entry.CLASS_TYPE;
				this.parseClassOrDelegateOrInterface(e);
			} else if (e.getTagName().equals(EVENT_DEFN)) {
				this.type = Entry.EVENT_TYPE;
				this.parseEvent(e);
			} else if (e.getTagName().equals(ENUMERATION)) {
				this.type = Entry.ENUMERATION_TYPE;
				this.parseClassOrDelegateOrInterface(e);
			} else if (e.getTagName().equals(DELEGATE)) {
                            
				this.type = Entry.DELEGATE_TYPE;
				this.parseClassOrDelegateOrInterface(e);
			} else if (e.getTagName().equals(INTERFACE)) {
				this.type = Entry.INTERFACE_TYPE;
				this.parseClassOrDelegateOrInterface(e);
                        } else if (e.getTagName().equals(STRUCTURE)) {
                                this.type = Entry.STRUCTURE_TYPE;
				this.parseClassOrDelegateOrInterface(e);
			} else {
				throw new RuntimeException("Don't know how to parse " + e.getNodeValue());
			}
		}
	}

	private void parseEvent(Element n) {
		List l = Util.getElts(n);
		//System.out.println(l);
		for (int i = 0; i < l.size(); i++) {
                    Element nextElt = (Element) l.get(i);
                    if (nextElt.getTagName().equals(NET_CLASSNAME)) {
			dnEvent = new DNEvent(this.getText(nextElt ).trim());
                   //} else if (nextElt.getTagName().equals(IMPORT)) {
			//eventImport = this.getText(nextElt ).trim();
                    } else if (nextElt.getTagName().equals(NET_TYPE)) {
			delegateType = this.getText(nextElt ).trim();
                   //} else if (nextElt.getTagName().equals(JAVA)) {
                    // javaEventListener = this.getText(nextElt ).trim();
                    
                        
                    } else {
                        System.out.println("Can't parse event in " + this.myFile);
			//throw new RuntimeException("Can't parse event in " + this.myFile);
                    }
		}
	}

	private void parseClassOrDelegateOrInterface(Element n) {
		List l = Util.getElts(n);
		Element vbElt = (Element) l.get(0);
                if (!vbElt.getTagName().equals(NET_CLASSNAME)) {
                    throw new RuntimeException("Expecting tag " + NET_CLASSNAME + " not " + vbElt.getTagName() + " in " + myFile);
                }
		dnTypename = this.getText(vbElt).trim();
		for (int i = 1; i < l.size(); i++) {
			Element nextElt = (Element) l.get(i);
			if (nextElt.getTagName().equals(JAVA)) {
				String jc = this.getText(nextElt ).trim();
				if (!"".equals(jc)) {
					javaClassname = jc;
				}
			} else if (nextElt.getTagName().equals(IMPORT)) {
				classImport = this.getText(nextElt ).trim();
			} else if (nextElt.getTagName().equals(INHERITS)) {
				inheritsClassname = this.getText(nextElt ).trim();
			} else if (nextElt.getTagName().equals(IMPLEMENTS)) {
                            String implementsText = this.getText(nextElt ).trim();
                                //System.out.println("IL " + fromString(implementsText));  
				implementsList.addAll(fromString(implementsText));
			} else if (nextElt.getTagName().equals(EVENT)) {
                                throw new RuntimeException("Event error " + myFile);
			} else if (nextElt.getTagName().equals(RUNTIME)) {
				String isRuntimeS = this.getText(nextElt).trim();
				this.isRuntime = isRuntimeS.equals("true");
                        } else if (nextElt.getTagName().equals(ALIAS)) {
                                this.aliasName = this.getText(nextElt ).trim();
			} else {
				throw new RuntimeException("Can't parse " + nextElt);
			}
		}
		Debug.logn("Tran: " + dnTypename + " " + javaClassname, this );
	}
        
        private void parseEventListeners(Element e) {
            List l = Util.getElts(e);
            for (Iterator itr = l.iterator(); itr.hasNext();) {
                Element nextElt = (Element) itr.next();
                this.parseListenerAddMethods(nextElt);
            }
        }

	private void parseListenerAddMethods(Element e) {
		List l = Util.getElts(e);
                String eName = "";
                String eType = "";
                String addMethod = "";
                String lMethod = "";
                String javaListenerInterfaceName = "";
                 
                for (Iterator itr = l.iterator(); itr.hasNext();) {
                    Element nextElement = (Element) itr.next();
                    if (nextElement.getTagName().equals(NAME)) {
                        eName = this.getText(nextElement).trim();
                    } else if (nextElement.getTagName().equals(NET_CLASSNAME)) {
                        eType = this.getText(nextElement).trim();
                    } else if (nextElement.getTagName().equals(ADD_LISTENER)) {
                        addMethod = this.getText(nextElement).trim();
                    } else if (nextElement.getTagName().equals(LISTENER_METHOD)) {
                        lMethod = this.getText(nextElement).trim();
                    } else if (nextElement.getTagName().equals(JAVA_EVENT_LISTENER)) {
                        javaListenerInterfaceName = this.getText(nextElement).trim();
                    } else {
                        System.out.println("Unknown tag " + nextElement.getTagName() + " in " + this.myFile);
                    }
                }
                EventNameType eNameType = new EventNameType(eName, eType);
                listenerAddMethods.put(eNameType, addMethod);
                if (!"".equals(lMethod)) {
                    listenerMethods.put(eNameType, lMethod);
                }
                if (!"".equals(javaListenerInterfaceName)) {
                    listenerJavaInterfaces.put(eNameType, javaListenerInterfaceName);
                } else {
                    System.out.println("Missing interface name in " + this.myFile);
                }
		
	}

	private String getText(Element e) {
		return Util.getText(e);	
	}

	/**
	<property: name, get+, set+>
		<name: $String>
		<get: java>
		<set: java>
	*/

	private void parseProperties(Element n) {
		for (Iterator itr = Util.getElts(n).iterator(); itr.hasNext();) {
			Element e = (Element) itr.next();
			List l = Util.getElts(e);


			Element nameE = (Element) l.get(0);
			boolean hasType = false;
			for (int i = 1; i < l.size(); i++) {
				Element propDefnElt = (Element) l.get(i);
				if (propDefnElt.getTagName().equals(NET_TYPE)) {
					hasType = true;
					String typeS = this.getText(propDefnElt);
					this.propertyTypes.put(this.getText(nameE).trim(), typeS);
				} else {
					List ll = Util.getElts(propDefnElt);
					Element javaE = (Element) ll.get(0);
					Element imports = null;
					if (ll.size() > 1	) {
						imports = (Element) ll.get(1);
					}

					String importS = this.getText(imports );
					if (printImportDebug) {
						if (!importS.equals("")) {
							importS = importS  + this;
						}
					}
					PropertyTranslation pt = new PropertyTranslation(this.getText(nameE ), propDefnElt.getTagName(), this.getText(javaE), importS);
					this.propertyTranslations.add(pt);
				}
				if (!hasType) {
					System.out.println("Library entry missing type " + dnTypename + " @ " + this.getText(nameE ));
					throw new RuntimeException("Stop");
				}
			}
		}

	}

	/**
	 * 	<method: vbSig, java>
	 * 		<java: $String>
	 * 		<vbSig: $String>
	 * 
	 */	
	private void parseMethods(Element n) {
		for (Iterator itr = Util.getElts(n).iterator(); itr.hasNext();) {
			Element e = (Element) itr.next();
			MethodTranslation mt = this.getMethodTranslation(e);
			Debug.logn("*" + mt, this);				
		}
	}

		// parse the <method> node
	public MethodTranslation getMethodTranslation(Element e) {
		List l = Util.getElts(e);
		Element vbE = (Element) l.get(0);
                if (!vbE.getTagName().equals(VB_SIGNATURE)) {
                    
                       throw new RuntimeException("Expecting tag " + VB_SIGNATURE + " not " + vbE.getTagName() + " in " + myFile);                 
                }
		Element javaE = (Element) l.get(1);
		Element imports = null;
		if (l.size() > 2) {
			imports = (Element) l.get(2);
		}
		String importS = this.getText(imports );
		if (printImportDebug) {
			importS = importS  + this;
		}
		MethodTranslation mt = new MethodTranslation(this.getText(vbE), this.getText(javaE), importS);
		
		Signature s = new Signature(mt.vbSig);
		JavaExpression je = new JavaExpression(mt.java, mt.getImports(), s);
		methodSigToJavaExpressionMap.put(s, je);
		return mt;
	}

	public JavaExpression findJavaExpressionForInvocation(String methodName, List args, Library library) {
		boolean b = false;//ie.getMethodName().equals("Add"); //this.vbClassname.equals("System.String");
		if (b) System.out.println("--------------------------Find sub for " + methodName + " on " + this);

		for (Iterator itr = this.methodSigToJavaExpressionMap.keySet().iterator(); itr.hasNext();) {
			Signature sg = (Signature) itr.next();
			if (sg.matchesSignature(methodName, args, library)) {
				JavaExpression je = (JavaExpression) methodSigToJavaExpressionMap.get(sg);
				if (b) System.out.println("--Found");
				return je;
			}
		}
		if (b) System.out.println("--Not Found");
		return null;
	}
        
         public String getJavaListenerFor(DNEvent ev) {
             Debug.logn("Entry Parser: Find java listener for " + ev + " on " + this.myFile, this);
             for (Iterator itr = this.listenerJavaInterfaces.keySet().iterator(); itr.hasNext();) {
                 EventNameType ent = (EventNameType) itr.next();
                 Debug.logn("next " + ent.name + " vs " + ev.getName(), this);
                 if (ent.type.equals(ev.getName())) {
                     return (String) this.listenerJavaInterfaces.get(ent);
                 }
             }
             return null;
         }

	

	public JavaExpression findSetJavaExpressionForMember(String memberName) {
		for (Iterator itr = this.propertyTranslations.iterator(); itr.hasNext();) {
			PropertyTranslation pt = (PropertyTranslation) itr.next();
			if (pt.name.equals(memberName) && pt.isSet()) {
				return new JavaExpression(pt.java, pt.getImports(), null);
			}
		}
		return null;
	}

	public JavaExpression findGetJavaExpressionForMember(String memberName) {
            boolean b = memberName.equals("Controls");
             
		for (Iterator itr = this.propertyTranslations.iterator(); itr.hasNext();) {
			PropertyTranslation pt = (PropertyTranslation) itr.next();
			if (pt.name.equals(memberName) && pt.isGet()) {
				return new JavaExpression(pt.java, pt.getImports(), null);
			}
		}
		return null;
	}
        
        Signature getSignatureForInvocationExpression(String name, List args, Library library) {
            Debug.logn("Get sig for " + name + " " + args, this);
		for (Iterator itr = this.methodSigToJavaExpressionMap.keySet().iterator(); itr.hasNext();) {
			Signature sg = (Signature) itr.next();
                        Debug.logn("Try "+ sg, this);
			if (sg.matchesSignature(name, args, library)) {
				return sg;
			}
		}
		return null;
	}

	
        
        String getDNTypeForMemberAccessExpression(String memberName) {
		for (Iterator itr = propertyTypes.keySet().iterator(); itr.hasNext();) {
			String nextPropertyName = (String) itr.next();
			if (nextPropertyName.equals(memberName)) {
				return (String) propertyTypes.get(nextPropertyName);
			}
		}
		for (Iterator itr = this.listenerAddMethods.keySet().iterator(); itr.hasNext();) {
			EventNameType ent = (EventNameType) itr.next();
			if (memberName.equals(ent.getName())) {
				return ent.getType();
			}
		}	
		return null;
	}
        
        private Document appendNodesForEvent(Document document, Element rootE, Element typeNode) {
            rootE.appendChild(typeNode);
            if (!("".equals(this.dnEvent.getName())) && !(this.dnEvent.getName() == null)) {
                Element evDnTypenameE = createElt(document, NET_CLASSNAME, this.dnEvent.getName());
                typeNode.appendChild(evDnTypenameE);
             }
            if (!("".equals(this.delegateType)) && !(this.delegateType == null)) {
                Element delegateTypenameE = createElt(document, NET_TYPE, this.delegateType);
                typeNode.appendChild(delegateTypenameE);
            }
            /**
            if (!("".equals(this.javaEventListener)) && !(this.javaEventListener == null)) {
                Element javaListenerE = createElt(document, JAVA, this.javaEventListener);
                typeNode.appendChild(javaListenerE);
            }
            if (!("".equals(this.eventImport)) && !(this.eventImport == null)) {
                Element importE = createElt(document, IMPORT, this.eventImport);
                typeNode.appendChild(importE);
            }
            */
            return document;
        }
        
        private Document getDocument() throws Exception {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = db.newDocument();
            Comment copyrightComment = document.createComment(COPYRIGHT_NOTICE);
            
            document.appendChild(copyrightComment);
            Element translationE = document.createElement(TRANSLATION);
            document.appendChild(translationE);
            Element typeNode = null;
            if (this.type == Entry.CLASS_TYPE) {
                typeNode = document.createElement(CLASS);
            } else if (this.type == Entry.STRUCTURE_TYPE) {
                typeNode = document.createElement(STRUCTURE);
            } else if (this.type == Entry.INTERFACE_TYPE) {
                typeNode = document.createElement(INTERFACE);
            } else if (this.type == Entry.DELEGATE_TYPE) {
                typeNode = document.createElement(DELEGATE);
            } else if (this.type == Entry.ENUMERATION_TYPE) {
                typeNode = document.createElement(ENUMERATION);  
            } else if (this.type == Entry.EVENT_TYPE) {
                typeNode = document.createElement(EVENT_DEFN); 
                return appendNodesForEvent(document, translationE, typeNode);
            } else {
                throw new RuntimeException("Can't serialize entries of type " + this.type);
            }
            translationE.appendChild(typeNode);
            /* "<net-classname> AssemblyInfo </net-classname>
		<inherits> System.Object </inherits>
		<java> com.sun.dn.library.AssemblyInfoSupport </java>"
             */
            Element dnClassnameE = createElt(document, NET_CLASSNAME, dnTypename);
            typeNode.appendChild(dnClassnameE);
            if (!("".equals(aliasName)) && !(aliasName == null)) {
                Element aliasE = createElt(document, ALIAS, aliasName);
                typeNode.appendChild(aliasE);
            }
            
            if (!("".equals(inheritsClassname)) && !(inheritsClassname == null)) {
                Element inheritE = createElt(document, INHERITS, inheritsClassname);
                typeNode.appendChild(inheritE);
            }
            String implementsS = asString(implementsList);
            if (!"".equals(implementsS)) {
                 Element implementsE = createElt(document, IMPLEMENTS, asString(implementsList));
                typeNode.appendChild(implementsE);
            }
           
            if (this.isRuntime) {
                String isRuntimeS = "true";
                Element isRuntimeE = createElt(document, RUNTIME, isRuntimeS);
                typeNode.appendChild(isRuntimeE);
            } 
            if (!(this.javaClassname == null) && !this.javaClassname.equals("")) {
                Element javaE = createElt(document, JAVA, javaClassname);
                typeNode.appendChild(javaE);
            }
            
            if (!this.listenerAddMethods.isEmpty()) {
                 Element eventsE = document.createElement(EVENTS);
                 translationE.appendChild(eventsE);
                 for (Iterator itr = this.listenerAddMethods.keySet().iterator(); itr.hasNext();) {
                    EventNameType ent = (EventNameType) itr.next();
                    Element eventE = document.createElement(EVENT);
                    Element eventNameE = createElt(document, NAME, ent.getName());
                    eventE.appendChild(eventNameE);
                    Element eventTypeE = createElt(document, NET_CLASSNAME, ent.getType());
                    eventE.appendChild(eventTypeE);
                    String addListenMName = (String) this.listenerAddMethods.get(ent);
                    Element almnE = createElt(document, ADD_LISTENER, addListenMName);
                    eventE.appendChild(almnE);
                    if (this.listenerMethods.get(ent) != null && !(this.listenerMethods.get(ent).equals(""))) {
                        String lm = (String) this.listenerMethods.get(ent);
                        Element lmE = createElt(document, LISTENER_METHOD, lm);
                        eventE.appendChild(lmE);
                    }
                    if (this.listenerJavaInterfaces.get(ent) != null && !(this.listenerJavaInterfaces.get(ent).equals(""))) {
                        String lji = (String) this.listenerJavaInterfaces.get(ent);
                        Element ljiE = createElt(document, JAVA_EVENT_LISTENER, lji);
                        eventE.appendChild(ljiE);
                    }
                    eventsE.appendChild(eventE);
                 }
            }
            
            if (!this.propertyTypes.isEmpty()) {
                Element propertiesE = document.createElement(PROPERTIES);
                translationE.appendChild(propertiesE);
                for (Iterator itr = this.propertyTypes.keySet().iterator(); itr.hasNext();) {
                    String pName = (String) itr.next();
                    String pType = (String) propertyTypes.get(pName);
                    Element propertyE = document.createElement(PROPERTY);
                    propertiesE.appendChild(propertyE);
                    Element nameE = this.createElt(document, NAME, pName);
                    propertyE.appendChild(nameE);
                    Element typeE = this.createElt(document, NET_TYPE, pType);
                    propertyE.appendChild(typeE);
                    List getSets = this.getPropertyTranslations(pName);
                    for (Iterator gss = getSets.iterator(); gss.hasNext();) {
                        PropertyTranslation pt = (PropertyTranslation) gss.next();
                        String gs = null;
                        if (pt.isGet()) {
                            gs = GET;
                        } else {
                            gs = SET;
                        }
                        Element getSetE = document.createElement(gs);
                        propertyE.appendChild(getSetE);
                        Element javaPE = this.createElt(document, JAVA, pt.getJava());
                        getSetE.appendChild(javaPE);
                        if (!pt.getImports().isEmpty()) {
                            Element importPE = this.createElt(document, IMPORT, asString(pt.getImports()));
                            getSetE.appendChild(importPE);
                        }
                    }
                    
                }
            }
            
            if (!this.methodSigToJavaExpressionMap.isEmpty()) {
                Element methodsE = document.createElement(METHODS);
                translationE.appendChild(methodsE);
                for (Iterator itr = this.methodSigToJavaExpressionMap.keySet().iterator(); itr.hasNext();) {
                    Signature sig = (Signature) itr.next();
                    Element methodE = document.createElement(METHOD);
                    methodsE.appendChild(methodE);
                    Element sigE = createElt(document, VB_SIGNATURE, sig.getOriginalCode());
                    methodE.appendChild(sigE);
                    JavaExpression jee = (JavaExpression) this.methodSigToJavaExpressionMap.get(sig); 
                    Element javaME = createElt(document, JAVA, jee.getExpression());
                    methodE.appendChild(javaME);
                    if (!jee.getImportStrings().isEmpty()) {
                        Element importME = createElt(document, IMPORT, asString(jee.getImportStrings()));
                        methodE.appendChild(importME);
                    }
                }
            }
            
            return document;
        }
        
        private List getPropertyTranslations(String pName) {
            List l = new ArrayList();
            for (Iterator itr = this.propertyTranslations.iterator(); itr.hasNext();) {
                PropertyTranslation pt = (PropertyTranslation) itr.next();
                if (pt.getName().equals(pName)) {
                    l.add(pt);
                }
            }
            return l;
        }
        
        private List fromString(String s) {
            List l = new ArrayList();
            StringTokenizer stok = new StringTokenizer(s, ",");
            while(stok.hasMoreTokens()) {
                String next = stok.nextToken().trim();
                l.add(next);
            }
            return l;
            
        }
        
        private String asString(List l) {
            String s = "";
            for (Iterator itr = l.iterator(); itr.hasNext();) {
                Object next = itr.next();
                s = s + next.toString();
                if (itr.hasNext()) {
                    s = s + ",";
                }
            }
            return s;
        }
        
        private Element createElt(Document d, String tagName, String text) {
            Element e = d.createElement(tagName);
            if (!"".equals(text)) {
                Text t = d.createTextNode(text);
                e.appendChild(t);
            }
            return e;
        }
        
        public void write(OutputStream os) throws Exception {
            Document document = this.getDocument();
            StreamSource src = new StreamSource("./src/Whitespace.xsl");
            Transformer transformer = TransformerFactory.newInstance().newTransformer(src);
	    Source source = new DOMSource(document);
	    Result result = new StreamResult(os);
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
	    transformer.transform(source, result);
        }

}

class VarName {
	String varName;

	VarName(String varName) {
		this.varName = varName;
	}

	public boolean equals(Object o) {
		return o == this;
	}

	public String toString() {
		return "VarName:("+varName+")";
	}

}

class MethodTranslation {
	String vbSig;
	String java;
	String imports;

	MethodTranslation(String vbSig, String java, String imports) {
		this.vbSig = vbSig.trim();
		this.java = java.trim();
		this.imports = imports.trim();
	}

	List getImports() {
		List l = new ArrayList();
		StringTokenizer st = new StringTokenizer(imports, ",");
		while(st.hasMoreTokens()) {
			l.add(st.nextToken().trim());
		}
		return l;
	}

	public String toString() {
		return "MT: " + vbSig + ":" + java + ":" + imports;
	}
}

 