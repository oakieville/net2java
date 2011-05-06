
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
package com.sun.dn.parser;


import com.sun.dn.*;
import com.sun.dn.library.Entry;
import com.sun.dn.Library;
import com.sun.dn.library.LibraryData;
import java.util.*;
import com.sun.dn.util.*;

    /** A .NET type. A type can be a Class an Enumeration a Structure or a Delegate.
     *
     * @author danny.coward@sun.com
     */

public class DNType {
	String name;
	String shortName;
	DNType superClass;
	Entry entry;
        LibraryData library;
        private DNType myArrayType;
	private boolean isEnum = false;
	private boolean isDelegate = false;
	private boolean isEvent = false;
	private boolean isInterface = false;
	private List interfacesImplemented = new ArrayList();

	public DNType(String name, LibraryData library) {
		this.name = name;
                this.library = library;
	}
        
        public DNType createArrayDNType(String language, LibraryData library) {
            
            if (language.equals(Interpreter.VB_LANGUAGE)) {
                this.myArrayType = new DNType(name + "()", library);
            } else if (language.equals(Interpreter.CS_LANGUAGE)) {
                this.myArrayType = new DNType(name + "[]", library);
            } else {
                throw new RuntimeException("Wrong language: " + language);
            }
            return this.myArrayType;
        }
        
        public DNType getArrayDNType() {
            if (this.myArrayType == null) {
                throw new RuntimeException("I should have been initialised with my array type, but I haven't been. " + this);
            }
            return this.myArrayType;
        }
        
        public DNType getPointDNType(LibraryData l) {
            //System.out.println("---Get point type for " + this);
            if (this.isPoint()) {
                //System.out.println("------got " + this);
                return this;
            } else {
                DNType pointType = (new Library(l)).getProgramDefinedOrLibraryDNTypeFor(this.getOneDimName(this.name));
                //System.out.println("------*-got " + pointType);
                return pointType;
            }
        }
        
        public boolean isUnknown() {
            return this instanceof UnknownType;
        }
        
        public boolean isNull() {
            //return this.getName().equals(NullType.NULLTYPE);
            return this instanceof NullType;
        }

	public boolean isInterface() {
		return this.isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}
	
	// ie is not an array
	public boolean isPoint() {
            return !this.name.endsWith("()") && !this.name.endsWith("[]");
	}

	public boolean isEnum() {
		return this.isEnum;
	}

	public void setEnum(boolean isEnum) {
		this.isEnum = isEnum;
	}

	public void setDelegate(boolean isDelegate) {
		this.isDelegate = isDelegate;
	}

	public boolean isDelegate() {
		return this.isDelegate;
	}

	public void setEvent(boolean isEvent) {
		this.isEvent = isEvent;
	}

	public boolean isEvent() {
		return this.isEvent;
	}

	public static boolean isArray(String n) {
		return n.endsWith("()") || n.endsWith("[]");
	}

	public static String getOneDimName(String n) {
		if (isArray(n)) {
			return n.substring(0, n.length() -2);
		} else {
			return n;
		}
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public Entry getEntry() {
		return this.entry;
	}

	public void setSuperClass(DNType superClass) {
		this.superClass = superClass;
	}

	public void addInterfaceImplemented(DNType interfaceClass) {
		this.interfacesImplemented.add(interfaceClass);
	}

	public List getInterfacesImplemented() {
		return this.interfacesImplemented;
	}

	public List getAllInterfacesImplemented() {
		List l = new ArrayList();
		l.addAll(this.getInterfacesImplemented());
		for (Iterator itr = this.getSuperClasses().iterator(); itr.hasNext();) {
			DNType c = (DNType) itr.next();
			l.addAll(c.getInterfacesImplemented());
		}
		return l;
	}

	public String getName() {
            return this.name;
	}

	public String getShortName() {
            if (shortName == null) {
                shortName = getShortNameFor(this.name);
            }
            return shortName;
	}
        
        public static String getShortNameFor(String name) {
            List l = Util.tokenizeIgnoringEnclosers(name, ".");
            return (String) l.get(l.size() -1);
        }

	public boolean shortNameEquals(DNType otherClass) {
		String name1 = this.name;
		String name2 = otherClass.getName();
		return shortNameEquals(name1, name2);
	}

	public static boolean shortNameEquivalent(String longName, String shortName) {
		return longName.equals(shortName) || longName.endsWith("." + shortName);
	}

	public static boolean shortNameEquals(String name1, String name2) {
		List l = Util.tokenizeIgnoringEnclosers(name1, ".");
		List ll = Util.tokenizeIgnoringEnclosers(name2, ".");
		String s1 = (String) l.get(l.size() -1);
		String s2 = (String) ll.get(ll.size() -1);
		s1 = DNPrimitives.convertToDNType(s1);
		s2 = DNPrimitives.convertToDNType(s2);
		return s1.equals(s2);
	}

	public String toString() {
		return "DNType: " + name;// + " s: " + this.getSuperClasses();
	}
        
        public boolean isAliased() {
            return this.getEntry() != null && this.getEntry().isAliased();
        }
        
        public DNType getAliasedType() {
            if (this.isAliased()) {
                return this.getEntry().getAliasType();
            } else {
                return null;
            }
        }

	// am I equals to this type, or is one of this type's super types, 
       // or implemented interfaces equal to me ?
        // or aliased to me
	public boolean isEqualOrIsSuperType(DNType c) {
            if (c == null) throw new RuntimeException("class passed is null");
            //System.out.println("--cf " + this + " -- " + c);
            if (this.isPoint() != c.isPoint()) {
                //System.out.println("--different dimensions");
                return false;
            }
            
            DNType objectType = c;
            objectType = objectType.getPointDNType(this.library);
            if (objectType.isAliased()) {
                objectType = objectType.getAliasedType();
            } 
            DNType subjectType = this;
            subjectType = subjectType.getPointDNType(this.library);
             if (subjectType.isAliased()) {
                subjectType = subjectType.getAliasedType();
            } 
            //System.out.println("--cf(aliased part done) " + subjectType + " -- " + objectType);
            
            
            
            if (subjectType.shortNameEquals(objectType)) {
            	return true;
            }
            if (subjectType.isEnum() && objectType.shortNameEquals( this.library.getLibraryClass("Integer") ) ) {
                return true;
            }
                    
            for (Iterator itr = objectType.getSuperClasses().iterator(); itr.hasNext();) {
            	DNType nextC = (DNType) itr.next();
            	if (subjectType.shortNameEquals(nextC)) {
                    return true;
		}
            }
            for (Iterator itr = objectType.getAllInterfacesImplemented().iterator(); itr.hasNext();) {
		DNType nextC = (DNType) itr.next();
		if (subjectType.shortNameEquals(nextC)) {
                    return true;
		}
            }
            return false;
	}

	public List getSuperClasses() {
            List l = new ArrayList();
            l.add(this.getSuperClass());
            if (this.getSuperClass() instanceof UnknownType ||
		this.getSuperClass().getName().equals("System.Object")) {
		// then don't add all the super classes of those. it ends there
            } else {
		l.addAll(this.getSuperClass().getSuperClasses());
            }
            return l;	
	}

	public List getThisAndSuperClasses() {
		List l = new ArrayList();
		l.add(this);
		l.addAll(this.getSuperClasses());
		return l;
	}

	public DNType getSuperClass() {
		if (superClass != null) {
			return superClass;
		} else {
			return new UnknownType(library);
		}
	}
} 
 