
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

import com.sun.dn.Library;
import com.sun.dn.library.LibraryData;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.util.*;

import com.sun.dn.parser.DNType;
import com.sun.dn.parser.DNEvent;
import com.sun.dn.parser.UnknownType;
import com.sun.dn.parser.DNVariable;
import com.sun.dn.parser.Signature;
import com.sun.dn.util.Debug;

	/** Data structure for a .NET library translation file.
         * Aliasing works by slipping the EntryParser for the
         * aliased type under the covers !! Neat...
	@author danny.coward@sun.com
	*/

 public class Entry {
	public static String CLASS_TYPE = "class";
	public static String EVENT_TYPE = "event";
	public static String DELEGATE_TYPE = "delegate";
	public static String INTERFACE_TYPE = "interface";
	public static String ENUMERATION_TYPE = "enumeration";
        public static String STRUCTURE_TYPE = "structure";
	
	private EntryParser ep;
        private Entry aliasEntry;
	private DNType dnType;
        private boolean isAliased = false;
	
	public Entry(File f, LibraryData library) {
		ep = new EntryParser(f);
                if (ep.aliasName != null) {
                    isAliased = true;
                }
                if (ep.dnTypename != null) {
			this.dnType = library.createLibraryDNType(ep.dnTypename, this);
		}
	}
        
        public static Entry createDefaultEntry() {
            Entry e = new Entry();
            e.ep = EntryParser.createDefaultEntryParser();
            e.ep.myFile = new File((new File("NewFile1.xml").getAbsolutePath()));
            return e;
        }
        
        public static Entry createDefaultEventEntry() {
            Entry e = new Entry();
            e.ep = EntryParser.createDefaultEventEntryParser();
            e.ep.myFile = new File((new File("NewFile1.xml").getAbsolutePath()));
            return e;
        }
        
        private Entry() {}
        
        /** Data structure property name -> property type.*/
        public Map getPropertyTypes() {
            return this.ep.propertyTypes;
        }
        
         /** Name of the alias class, or null.*/
        public String getAliasName() {
            return ep.aliasName;
        }
         /** Set the name of the alias class.*/
        public void setAliasName(String aliasName) {
            ep.aliasName = aliasName;
        }
        
        /** List of PropertyTranslation objects.*/
        public List getPropertyTranslations() {
            return this.ep.propertyTranslations;
        }
        
         /** Map of Signature to JavaExpression objects.*/
        public Map getMethodSigToJavaExpressionMap() {
            return ep.getMethodSigToJavaExpressionMap();
        }

	public String toString() {
		return "Entry: " + ep.myFile.getName();
	}
        
        /** Return the file I came from */
        public java.io.File getFile() {
            return ep.myFile;
            
        }
        /** Set the file I came from */
        public void setFile(java.io.File f) {
            this.ep.myFile = f;
        }
        /** Return associated veent object, if there is one. */
	public DNEvent getEvent() {
		return ep.dnEvent;
	}
        
        public void setEventName(String name) {
            ep.dnEvent = new DNEvent(name);
        }
        

	public String getDelegateTypename() {
		return ep.delegateType;
	}
        
        public void setDelegateTypename(String name) {
            ep.delegateType = name;
        }

	public boolean isRuntime() {
		return ep.isRuntime;
	}
        
        public void setRuntime(boolean isRuntime) {
            ep.isRuntime = isRuntime;
        }
        
        public boolean isClass() {
            return this.ep.getType().equals(CLASS_TYPE);
        }
        
        public boolean isEnumeration() {
            return this.ep.getType().equals(ENUMERATION_TYPE);
        }
        
        public boolean isInterface() {
            return this.ep.getType().equals(INTERFACE_TYPE);
        }
        
        public boolean isStructure() {
            return this.ep.getType().equals(STRUCTURE_TYPE);
        }
         
          public boolean isDelegate() {
            return this.ep.getType().equals(DELEGATE_TYPE);
        }
          
           public boolean isEvent() {
            return this.ep.getType().equals(EVENT_TYPE);
        }
          
          public boolean isAliased() {
              return this.isAliased;
          }
          
          public DNType getAliasType() {
              return this.aliasEntry.getDNType();
          }
          

          public void resolveAlias(LibraryData l) {
            if (this.isAliased) {
                Debug.logn("Resolve Aliases on " + this, this);
                DNType aliasType = l.getLibraryClass(ep.aliasName);
                if (aliasType == null) {
                    throw new RuntimeException("Can't find alias type for " + ep.aliasName);
                }
                aliasEntry = l.getEntryFor(aliasType);
                this.ep = aliasEntry.ep;
              }
              
          }
          
	public void resolveSuperclasses(LibraryData l) {
		DNType c = null;
		if (this.ep.inheritsClassname != null) {
			c = l.getLibraryClass(this.ep.inheritsClassname);
			if (c != null) {
				//System.out.println("OK " + vbClass.getName() + ": inherits " + c.getName() + "\n\t"+ ep.myFile);
				this.dnType.setSuperClass(c);
			} else {
				System.out.println("WARNING: " + dnType.getName() + ": inherits " + this.ep.inheritsClassname + " but its not in the library\n\t"+ ep.myFile);
			}
		} else { 
			//System.out.println(vbClass.getName() + ": does not define its inherits\n\t"+ ep.myFile);
		}
		
	}

	public void resolveInterfacesImplemented(LibraryData l) {
		DNType c = null;
                
		for (Iterator itr = this.ep.implementsList.iterator(); itr.hasNext();) {
			String nextInterface = (String) itr.next();
                        //System.out.println(nextInterface);
			c = l.getLibraryClass(nextInterface);
			if (c != null) {
				//System.out.println("OK " + vbClass.getName() + ": implements " + c.getName() + "\n\t"+ ep.myFile);
				this.dnType.addInterfaceImplemented(c);
			} else {
				System.out.println(dnType.getName() + ": implements " + nextInterface  + " but its not in the library\n\t"+ ep.myFile);
			}	
		}
	}

	public String getType() {
		return ep.getType();
	}
        
        public void setType(String type) {
            this.ep.type = type;
        }

	public String getDNTypename() {
		return ep.dnTypename;
	}
        
        public void setDNTypename(String newTypename) {
            ep.dnTypename = newTypename;
        }
        
        public String getDNInheritsTypename() {
            return ep.inheritsClassname;
        }
        
         public void setDNInheritsTypename(String newInheritsTypename) {
            ep.inheritsClassname = newInheritsTypename;
        }
        
        public void addImplementsType(String type) {
            ep.implementsList.add(type);
        }
        
         public void removeImplementsType(String type) {
            ep.implementsList.remove(type);
        }
        
        public List getImplementsList() {
            return ep.implementsList;
        }

	public Map getListenerAddMethods() {
            return ep.listenerAddMethods;
	}
        
        public Map getListenerJavaInterfaces() {
            return ep.listenerJavaInterfaces;
        }
        
        public String getJavaListenerFor(DNEvent ev) {
            return ep.getJavaListenerFor(ev);
        }

	public Map getListenMethods() {
		return ep.listenerMethods;
	}

	public DNType getDNType() {
		return this.dnType;
	}

	public String getJavaClassname() {
		String s = this.ep.javaClassname;
		if (s == null || "".equals(s)) {
			return null;
		} else {
			return s;
		}
	}
        
        public void setJavaClassname(String javaClassname) {
            this.ep.javaClassname = javaClassname;
        }


	// does this class equal my type or is one its parent types equal to my type ?
	public boolean doTypesMatch(DNType c) { 
		return this.getDNType().isEqualOrIsSuperType(c);
	}
        
        public JavaExpression getTranslationForInvocationExpression(String methodName, List args, Library library) {
		Debug.logn("Get Translation for " + methodName + " " + args, this);	
		JavaExpression je = ep.findJavaExpressionForInvocation(methodName, args, library);
		if (je != null) {
			Debug.logn("got " + je, this);
			return je;
		}
		Debug.logn("..ie..not found on " + this, this);
		return null;
	}

        
        public String getDNTypeForMemberAccessExpression(String memberName) {
		String c = ep.getDNTypeForMemberAccessExpression(memberName);
		if (c != null) {
			Debug.logn("got " + c, this);
			return c;
		}
		return null;
	}
        
        public JavaExpression getTranslationForAssignment(String memberName) {
		Debug.logn("Get Translation for " + memberName, this);
		JavaExpression je = this.ep.findSetJavaExpressionForMember(memberName);
		if (je != null) {
			Debug.logn(" found " + je, this);
			return je;
		}
		return null;
	}
        
        public JavaExpression getTranslationForMemberAccessExpression(String variableName) {
		return this.ep.findGetJavaExpressionForMember(variableName);
	}

	public Signature getSignatureForInvocation(String methodName, List args, Library library) {
		return this.ep.getSignatureForInvocationExpression(methodName, args, library);
	}
        
         public void write(OutputStream os) throws Exception {
             this.ep.write(os);
         }


	


	




}

 