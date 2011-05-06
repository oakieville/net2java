
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


import com.sun.dn.Interpreter;
import com.sun.dn.parser.UnknownType;
import com.sun.dn.util.AssemblyInformation;

import java.util.*;
import java.io.*;
import com.sun.dn.util.Util;
import com.sun.dn.util.Debug;
import com.sun.dn.parser.DNEvent;
import com.sun.dn.parser.DNType;

	/** The .NET Library manages all the .NET to Java
	** conversions and all the program types.
	**/

public class LibraryData {
        public static String PATH_IN_LIB = "com/sun/dn/library";
	private NamespaceListParser csPrimitivesNamespaces;
        private NamespaceListParser appTypeExcludeNamespaces;
	private String language;
	List availableNamespaces = new ArrayList();
	private File libraryDir;
	private Map dnTypeNameToEntryMap = new HashMap();
	private List eventEntries = new ArrayList();
        private List runtimeEntries = new ArrayList();
	private List programDefinedDNTypes = new ArrayList();
	private AssemblyInformation ai; // should really be on the parse tree,
	private boolean allNamespaces = false;					  // but performancewise that would suck
        
	
		// ha !
	public LibraryData(File libraryRoot, String programType, String language, boolean allNamespaces) {
            this.allNamespaces = allNamespaces;
            //System.out.println("Library root = " + libraryRoot);
                if (language.equals(Interpreter.VB_LANGUAGE) || language.equals(Interpreter.CS_LANGUAGE)) {
			this.language = language;
		} else {
			throw new RuntimeException("Unknown language: "  + this.language);
		}
                if (!libraryRoot.exists()) {
                    libraryRoot = new File("");
                } else {
                    this.libraryDir = new File(libraryRoot, PATH_IN_LIB);
                    this.initLibraryNamespaces(programType, language);
                    this.init();
                }
		
		
	}
        
        public Map getDNTypeNameToEntryMap() {
            return dnTypeNameToEntryMap;
        }
        
        public String getLanguage() {
            return this.language;
        }
        
        public List getEventEntries() {
            return this.eventEntries;
        
        }
        
        public List getRuntimeEntries() {
            return this.runtimeEntries;
        }

	public void setAssemblyInformation(AssemblyInformation ai) {
		this.ai = ai;
	}


	public void addNamespace(String namespace) {
		this.availableNamespaces.add(namespace);
	}

	public List getNamespaces() {
		return this.availableNamespaces;
	}
        
        public List getProgramDefinedDNTypes() {
            return this.programDefinedDNTypes;
        }
        
        public void addProgramDefinedDNType(DNType cl) {
            this.programDefinedDNTypes.add(cl);
        }

	private void initLibraryNamespaces(String programType, String language) {
		csPrimitivesNamespaces = new NamespaceListParser();
                appTypeExcludeNamespaces = new NamespaceListParser();
                File f = new File(libraryDir, "../" + programType + ".xml");
		appTypeExcludeNamespaces.addFromFile(f);

		if (language.equals(Interpreter.CS_LANGUAGE)) {
			f = new File(libraryDir, "../csprimitives.xml");
			csPrimitivesNamespaces.addFromFile(f);
		}
		
	}

	private void init() {
		List l = Util.getAllEntryFilesIn(libraryDir, ".xml");
		for (Iterator itr = l.iterator(); itr.hasNext();) {
			File f = (File) itr.next();
                        boolean appTypeExcluded = appTypeExcludeNamespaces.contains(f, libraryDir);
                        boolean languageExcluded = language.equals(Interpreter.VB_LANGUAGE) && csPrimitivesNamespaces.contains(f, libraryDir);
			if (!allNamespaces && (appTypeExcluded || languageExcluded) ) {
                            Debug.logn("Skip entry: outside library namespaces " + f, this);
                           // System.out.println("Excluding " + f + " apptypeexcluded " + appTypeExcluded + " langExcluded " + languageExcluded);
                        } else {
                            
				Debug.logn("Parse and add Entry for " + f, this);
				Entry e = new Entry(f, this);
                                
				if (e.getType().equals(Entry.CLASS_TYPE) 
					|| e.getType().equals(Entry.DELEGATE_TYPE)
					|| e.getType().equals(Entry.INTERFACE_TYPE)
					|| e.getType().equals(Entry.ENUMERATION_TYPE) 
                                        || e.getType().equals(Entry.STRUCTURE_TYPE) ) {
                     
					this.dnTypeNameToEntryMap.put(e.getDNTypename(), e);
				} else if (e.getType().equals(Entry.EVENT_TYPE)) {
					this.eventEntries.add(e);
				} else {
					throw new RuntimeException("Unknown Type");
				}
			} 
		}
		// insert superclasses and interfaces implemented
		for (Iterator itr = this.dnTypeNameToEntryMap.keySet().iterator(); itr.hasNext();) {
			String key = (String) itr.next();
			Entry nextE = (Entry) this.dnTypeNameToEntryMap.get(key);
			nextE.resolveSuperclasses(this);
			nextE.resolveInterfacesImplemented(this);
		}
                
                for (Iterator itr = this.dnTypeNameToEntryMap.keySet().iterator(); itr.hasNext();) {
			String key = (String) itr.next();
			Entry nextE = (Entry) this.dnTypeNameToEntryMap.get(key);
			nextE.resolveAlias(this);
		}
		

		// resolve the classes on the events
		for (Iterator itr = this.eventEntries.iterator(); itr.hasNext();) {
			Entry nextE = (Entry) itr.next();
			DNEvent event = nextE.getEvent();
			DNType c = this.getLibraryClass(nextE.getDelegateTypename());
			if (c == null) {
                            System.out.println("WARNING: Can't resolve class for event " + event);
			}
			event.setDNType(c);
		}

		// stick the entry into the class, and fill out the runtime list
		for (Iterator itr = this.dnTypeNameToEntryMap.keySet().iterator(); itr.hasNext();) {
			String key = (String) itr.next();
			Entry nextE = (Entry) this.dnTypeNameToEntryMap.get(key);
			DNType cc = nextE.getDNType();
			cc.setEntry(nextE);
			if (nextE.isRuntime()) {
				//System.out.println("Adding to runtime classes: entry for " + cc);
				this.runtimeEntries.add(nextE);
			}
		}
	}

	private List getLibraryClasses() {
		List l = new ArrayList();
		for (Iterator itr = this.getLibraryEntries().iterator(); itr.hasNext();) {
			Entry en = (Entry) itr.next();
			l.add(en.getDNType());
		}
		return l;
	}

	public List getLibraryEntries() {
            return new ArrayList(this.dnTypeNameToEntryMap.values());
	}

	

            // has to stay
	public DNType getLibraryClass(String name) {
		if (name == null) { 
                    return null;
                } else if (name.equals(UnknownType.UNKNOWN)) {
                    return new UnknownType(this);
                }
                boolean isArrayName = DNType.isArray(name);
                String trueName = "";
                if (isArrayName) {
                    trueName = DNType.getOneDimName(name);
                } else {
                    trueName = name;
                }
		Entry e = this.getEntry(trueName);
		if (e == null) {
                    return null;
                } 
                if (isArrayName) {
                    return e.getDNType().createArrayDNType(this.language, this);
		} else {
                    return e.getDNType();
                }
	}

	// this can stay
	public DNType createLibraryDNType(String name, Entry e) {
		DNType c = this.getLibraryClass(name);
		if (c != null) {
			return c;
		}
		DNType newC = new DNType(name, this);
		newC.setDelegate(e.getType().equals(Entry.DELEGATE_TYPE));
		boolean isEvent = e.getType().equals(Entry.EVENT_TYPE);
		boolean isInterface = e.getType().equals(Entry.INTERFACE_TYPE);
		boolean isEnumeration = e.getType().equals(Entry.ENUMERATION_TYPE);
		newC.setEvent(isEvent);
		newC.setInterface(isInterface);
		newC.setEnum(isEnumeration);
		return newC;
	}
        
        
        private Entry getEntry(String name) {
		if (name == null) {
			throw new RuntimeException("null name");
		}
		for (Iterator itr = this.getDNTypeNameToEntryMap().keySet().iterator(); itr.hasNext();) {
			String vbClassname = (String) itr.next();
			if (DNType.shortNameEquivalent(vbClassname, name)) {
				Entry en  = (Entry) this.getDNTypeNameToEntryMap().get(vbClassname);
				return en;
			}
		}
		return null;
	}
       

        // this can stay
	public Entry getEntryFor(DNType c) {
		for (Iterator itr = this.getLibraryEntries().iterator(); itr.hasNext();) {
			Entry e = (Entry) itr.next();
			if (e.getDNType().equals(c)) {
				return e;
			}
		}
		return null;
	}



	

    


} 
 