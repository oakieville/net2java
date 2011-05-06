
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

package com.sun.dn;

import com.sun.dn.java.JavaKeywords;
import com.sun.dn.library.Entry;
import com.sun.dn.library.EventNameType;
import com.sun.dn.library.JavaExpression;
import com.sun.dn.library.LibraryData;
import com.sun.dn.parser.CSKeywords;
import com.sun.dn.parser.DNEvent;
import com.sun.dn.parser.DNType;
import com.sun.dn.parser.Expression;
import com.sun.dn.parser.MetaClass;
import com.sun.dn.parser.NullType;
import com.sun.dn.parser.Signature;
import com.sun.dn.parser.TypeResolveException;
import com.sun.dn.parser.UnknownType;
import com.sun.dn.parser.VBKeywords;
import com.sun.dn.parser.expression.InvocationExpression;
import com.sun.dn.parser.expression.MemberAccessExpression;
import com.sun.dn.parser.expression.ObjectCreationExpression;
import com.sun.dn.parser.expression.VBMeExpression;
import com.sun.dn.parser.statement.Assignment;
import com.sun.dn.parser.statement.UntranslatedStatement;
import com.sun.dn.parser.statement.VBEnumStatement;
import com.sun.dn.util.Debug;
import com.sun.dn.util.Stop;
import com.sun.dn.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Danny Coward
 */
public class Library {
    private static String ARRAY_PSEUDOTYPE = "Array";
    LibraryData libraryData;
    
    /** Creates a new instance of LibraryAssistant */
    public Library(LibraryData library) {
        this.libraryData = library;
    }
    
    public LibraryData getLibraryData() {
        return libraryData;
    }
    
    public String getJavaAddListenerMethodNameFor(String dnSenderType, DNEvent DNEvent) {
               
		DNType c = this.getProgramDefinedOrLibraryDNTypeFor(dnSenderType);
		List entries = getAllEntriesFor(c);
		for (Iterator eIterator = entries.iterator(); eIterator.hasNext();) {
			Entry e = (Entry) eIterator.next();
			if (e == null) {
				throw new RuntimeException("Can't find entry for " + dnSenderType);
			} else {
				for (Iterator itr = e.getListenerAddMethods().keySet().iterator(); itr.hasNext();) {
					EventNameType eNameType = (EventNameType) itr.next();
					if (eNameType.getType().equals(DNEvent.getName())) {
						return (String) e.getListenerAddMethods().get(eNameType);
					}
				}
			}	
		}
		throw new RuntimeException("Can't find add method for " + dnSenderType + " with event " + DNEvent);
	}

	public String getJavaListenMethodNameForDNEvent(String senderType, DNEvent dnEvent) {
              //System.out.println(senderType);
              //System.out.println(dnEvent);
		DNType c = this.getProgramDefinedOrLibraryDNTypeFor(senderType);
		List entries = getAllEntriesFor(c);
		for (Iterator eIterator = entries.iterator(); eIterator.hasNext();) {
			Entry e = (Entry) eIterator.next();
			for (Iterator itr = e.getListenMethods().keySet().iterator(); itr.hasNext();) {
				EventNameType eNameType = (EventNameType ) itr.next();
				if (eNameType.getType().equals(dnEvent.getName())) {
					return (String) e.getListenMethods().get(eNameType);
				}
			}
		}
		throw new RuntimeException("Can't find listen method for " + dnEvent + " on sender " + senderType);
	}


	public JavaExpression getTranslationForInvocation(InvocationExpression ie) {
		Debug.logn("------------------Get Translation for ie " + ie, this);
                
		Expression eS = ie.getTarget();
		DNType c = eS.getDNType();
		Debug.logn("Target type is " + c, this);
		List entriesToSearch = this.getAllEntriesFor(c);
                Debug.logn("Look on " + entriesToSearch, this);
		if (eS instanceof VBMeExpression) {
			entriesToSearch.addAll(libraryData.getRuntimeEntries());
		}
		for (Iterator itr = entriesToSearch.iterator(); itr.hasNext();) {
			Entry e = (Entry) itr.next();
			JavaExpression je = e.getTranslationForInvocationExpression(ie.getMethodName(), ie.getArgs(), this);
			if (je != null) {
				return je;
			}
		}
		return null;
	}

	public JavaExpression getTranslationForNew(ObjectCreationExpression oce) {
            Debug.logn("Get Translation for New " + oce, this);
		DNType c = oce.getDNType();
		//System.out.println(c);
		for (Iterator itr = this.getAllEntriesFor(c).iterator(); itr.hasNext();) {
			Entry e = (Entry) itr.next();
                        Debug.logn("try on entry " + e, this);
			JavaExpression je = e.getTranslationForInvocationExpression(oce.getNewKeyword(), oce.getArgs(), this);
			if (je != null) {
				return je;
			}
		}
		return null;
	}

	public JavaExpression getTranslationForAssignment(Assignment as) {
		Debug.logn("Get Translation for assignment " + as, this);
		long l = Debug.getTime();
		//System.out.println("--Get Translation for assignment " + as);
		long ll = -1;
		long lll = -1;
		DNType c = null;
		List allEntries = null;
		String sss = null;
		boolean forSetters = as.isMemberAssignment();
		if (forSetters) {
			MemberAccessExpression mae = as.getMemberAccessExpression();
			Expression ee = mae.getExpression();
			sss = ee.getClass().getName();
			lll = Debug.getTime();
			c = ee.getDNType();
			
			allEntries = this.getAllEntriesFor(c);
			for (Iterator itr = allEntries.iterator(); itr.hasNext();) {
				Entry en = (Entry) itr.next();
                                String memberName = mae.getVariableMemberName();
				JavaExpression je = en.getTranslationForAssignment(memberName);
				if (je != null) {
					ll = (new Date()).getTime();
					
					Debug.logn("--Found in " + (ll-l) + "ms", this);
					return je;
				}
			}
		}
		ll = Debug.getTime();
		if ((ll-l) > 1000) {
			System.out.println("SLOW(" + (ll-l) + "ms) : " + as + "\n" + c + "\n" + (ll-lll) + "\n" + sss);
		}

		Debug.logn("--Not Found " + (Debug.getTime()-l) + "ms", this);
		return null;
	}	

	public JavaExpression getTranslationForMAE(MemberAccessExpression mae) {
		Debug.logn("Get Translation for mae " + mae, this);
		long l = (new Date()).getTime();

		Expression e = mae.getExpression(); // this.TabControl
		if (e == null) return null;
		DNType typeClass = this.getDNType(e.getTypeName()); // TabControl
		
		for (Iterator itr = this.getAllEntriesFor(typeClass).iterator(); itr.hasNext();) {
			Entry en = (Entry) itr.next();
			JavaExpression je = en.getTranslationForMemberAccessExpression(mae.getVariable().getName());
			if (je != null) {
				Debug.logn("--Found in " + (Debug.getTime()-l) + "ms", this);
				if ((Debug.getTime() - l) > 1000) {
					System.out.println("SLOW (" + (Debug.getTime() - l) + "ms) " + mae);
				}

				return je;
			}
		}
		Debug.logn("--Not Found, in " + (Debug.getTime()-l) + "ms", this);
		if ((Debug.getTime() - l) > 1000) {
			System.out.println("SLOW (" + (Debug.getTime() - l) + "ms) " + mae);
		}
		return null;
	}
    
    public boolean hasDNEvent(String eventName) {
		return this.getEventPrivate(eventName) != null;

	}

	public String getJavaListenerTypeFor(String dnSenderType, DNEvent evnt) {
            //System.out.println("Library: Find java listener for " + evnt + " on " + dnSenderType);
            DNType c = this.getProgramDefinedOrLibraryDNTypeFor(dnSenderType);
            List entries = getAllEntriesFor(c);
            //System.out.println(entries);
            for (Iterator itr = entries.iterator(); itr.hasNext();) {
		Entry e = (Entry) itr.next();
		
                String listener =  e.getJavaListenerFor(evnt);
                if (listener != null) {
                    return listener;
                }
		
            }
            throw new RuntimeException("can't find listener for " + evnt + " on " + dnSenderType);
	}
    
     // this deals with DNTypes that are arrays
	private List getAllEntriesFor(DNType c) {
            Debug.logn("Finding entries for " + c, this);
            DNType type = c;
            if (c.isAliased()) {
                type = c.getAliasedType();
            }

            List l = new ArrayList();
            Debug.logn("Heierarchy " + type.getThisAndSuperClasses(), this);
            for (Iterator itr = type.getThisAndSuperClasses().iterator(); itr.hasNext();) {
            	DNType next = (DNType) itr.next();
		if (!(next instanceof UnknownType) && next.getEntry() != null) {
                    l.add(next.getEntry());
		}
            }
            if (!type.isPoint()) {
                DNType aTy = this.getDNType(ARRAY_PSEUDOTYPE);
                l.addAll(this.getAllEntriesFor(aTy));
            }
            Debug.logn("And obtained: " + l, this);
            return l;
	}
    
    	public DNEvent getDNEvent(String eventName) {
		DNEvent e = this.getEventPrivate(eventName);
		if (e == null) {
			throw new RuntimeException("Cannot find event for " + eventName);
		} else {
			return e;
		}
    }

	private DNEvent getEventPrivate(String eventName) {
		for (Iterator itr = libraryData.getEventEntries().iterator(); itr.hasNext();) {
                    Entry e = (Entry) itr.next();
                    DNEvent ev = e.getEvent();
                    if (ev.getName().equals(eventName)) {
			return ev;
                    } else {
                        List l = Util.tokenizeIgnoringEnclosers(ev.getName(), ".");
                        if (eventName.equals( l.get(l.size()-1) )) {
                            return ev;
			} 
                    }
                }
                DNType t = this.getProgramDefinedDelegate(eventName);
                if (t != null) {
                    DNEvent ev = new DNEvent(eventName);
                    ev.setDNType(t);
                    return ev;
                }
		return null;
	}
    
     public DNType getProgramDefinedDelegate(String delegateName) {
            for (Iterator itr = libraryData.getProgramDefinedDNTypes().iterator(); itr.hasNext();) {
                DNType type = (DNType) itr.next();
                if (type.getName().equals(delegateName) && type.isDelegate()) {
                    return type;
                }        
            }
            return null;
        }
    
    	public DNType createEnumerationDNType(String name, VBEnumStatement es) {
		DNType c = this.createProgramDefinedDNType(name, null);
		c.setSuperClass(libraryData.getLibraryClass(es.getType()));
		c.setEnum(true);
		//System.out.println("Added Enum of type " + es.getType());
		return c;
	}

	public boolean containsProgramDefinedDNType(String name) {
		return this.getProgramDefinedDNType(name) != null;
	}
    
    	// but don't create a new one
	public DNType getProgramDefinedOrLibraryDNTypeFor(String type) {
            DNType c = libraryData.getLibraryClass(type);
            if (c != null) {
            	return c;
            } else {
		c = this.getProgramDefinedDNType(type);
            }
            if (c == null) {
		throw new TypeResolveException(type, "Type for " + type + " not found");
            }
            return c;
	}

	public DNType getDNType(String name) {
		return this.createProgramDefinedDNType(name, null);
	}
    
    public DNType getProgramDefinedDNType(String name) {
            Debug.logn("Get Program defined type for " + name, this);
            boolean isArrayName = DNType.isArray(name);
            String trueName = "";
            if (isArrayName) {
                trueName = DNType.getOneDimName(name);
            } else {
                trueName = name;
            }
            for (Iterator itr = libraryData.getProgramDefinedDNTypes().iterator(); itr.hasNext();) {
                DNType c = (DNType) itr.next();
                //Debug.logn("try " + c, this);
		if (DNType.shortNameEquivalent(c.getName(), trueName)) {
                    if (isArrayName) {
                        return c.createArrayDNType(libraryData.getLanguage(), libraryData);
                    } else {
                        return c;
                    }
		}
            }
            return null;
	}

    
    public DNType createProgramDefinedDNType(String name, MetaClass mc) {
            DNType c = libraryData.getLibraryClass(name);
            if (c == null) { // no lib class
                c = this.getProgramDefinedDNType(name);
		if (c != null) {
                    return c;
		} else { // no lib class and no predefined class either
                    DNType cl = new DNType(name, libraryData);
                    libraryData.addProgramDefinedDNType(cl);
                    return cl;
		}
            } else { // there is a library class
		return c;
            }
	}
    
    
    	public String getJavaTypeFor(String typeString) {
            if (typeString.equals(NullType.NULLTYPE)) {
                return JavaKeywords.J_VOID;
            }
		if (libraryData.getLanguage().equals(Interpreter.VB_LANGUAGE)) {
			return this.getJavaTypeForVBType(typeString);
		} else if (libraryData.getLanguage().equals(Interpreter.CS_LANGUAGE)) { 
			return this.getJavaTypeForCSType(typeString);
		} 
		throw new Stop(this.getClass());
	}

	private String getJavaTypeForCSType(String csTypeString) {
		Debug.logn("Get Java type for CS class " + csTypeString, this);
		if (csTypeString == null || csTypeString.equals(CSKeywords.CS_Void)) {
			return JavaKeywords.J_VOID;
		}
		

		// first try the things in the library
		String s = this.getJavaClassnameFor(csTypeString);
		if (s != null) {
			//Debug.logn("got ." + s+".", this);
			return s;
		}

		// now try the EXISTING program defined types that were set during the parse
		DNType c = this.getProgramDefinedDNType(csTypeString);
                
		if (c != null) {
			//Debug.logn("got ." + s+".", this);
			if (c.isEnum()) {
				return "int";
			} else {
				return c.getName();
			}
		}		

		//String unk = "<Unknown:" +  csTypeString + ">";
		String ret = "/* " + UntranslatedStatement.TRANSLATEME + " " + csTypeString + " */";
		if (csTypeString.equals("string")) {
			ret = "String";
		} else {
			//throw new TypeResolveException(csTypeString, "Cannot find jtype for cs class ["+csTypeString+"].");
		}
		//Debug.logn("got ." + ret +".", this);
		return ret;
	}
	

	private String getJavaTypeForVBType(String vbTypeString) {
            Debug.logn("Get Java Type for " + vbTypeString, this);
                
            if (vbTypeString == null || vbTypeString.equals(VBKeywords.VB_Nothing)) {
                Debug.logn("got void", this);
                return JavaKeywords.J_VOID;
            }
            String vbType, jArray = null;
            if (DNType.isArray(vbTypeString)) {
		vbType = DNType.getOneDimName(vbTypeString);
		jArray = "[]";
            } else {
		vbType = vbTypeString;
		jArray = "";
            }

		// first try the things in the library
            String s = this.getJavaClassnameFor(vbType);
               // System.out.println("lib " + vbTypeString + " : " + s.getClass());
            if (s != null) {
                Debug.logn("got ." + s+".", this);
                return s + jArray;
            }

            // now try the EXISTING program defined types that were set during the parse
            DNType c = this.getProgramDefinedDNType(vbType);
                
            if (c != null) {
                    //Debug.logn("got ." + s+".", this);
            if (c.isEnum()) {
                Debug.logn(" got int" + jArray, this);
                return "int" + jArray;
            } else {
                Debug.logn(" got " + c.getName() + jArray, this);
                return c.getName() + jArray;
            }
        }		
                //System.out.println("trying for " + vbTypeString);
	//String unk = "<Unknown:" +  vbTypeString + ">";
        String ret = "/* " + UntranslatedStatement.TRANSLATEME + " " + vbTypeString + " */";
	if (vbType.equals("Char")) {
            ret = "char";
	} else if (vbType.equals("Date")) {
            ret = "java.util.Date";
	} else if (vbType.equals("Integer")) {
            ret = "int";
	} else if (vbType.equals("Double")) {
            ret = "double";
	} else if (vbType.equals("Float")) {
            ret = "double";
	} else if (vbType.equals("Single")) {
            ret = "float";
	} else if (vbType.equals("Boolean")) {
            ret = "boolean";
	} else if (vbType.equals("Byte")) {
            ret = "byte";
	} else {
            //throw new TypeResolveException(vbTypeString, "Cannot find jtype for vb class ["+vbTypeString+"].");
        }
	Debug.logn("got ." + ret +".", this);
	return ret + jArray;
    }
        
        private String getJavaClassnameFor(String typeString) {
            Debug.logn("get Java substitute for clas " + typeString, this);
            String classname = null;
            String jArray = null;
            if (DNType.isArray(typeString)) {
		classname = DNType.getOneDimName(typeString);
		jArray = "[]";
            } else {
		classname = typeString;
		jArray = "";
            }
           // Debug.logn("here", this);
            for (Iterator itr = libraryData.getLibraryEntries().iterator(); itr.hasNext();) {
                Entry e = (Entry ) itr.next();
                //System.out.println("jcname " + typeString + " : " + e);
               // Debug.logn("here " + e.getDNType(), this);
		if (e.getDNType().getName().equals(classname) ||
			e.getDNType().getShortName().equals(classname)	 ) {
			Debug.logn("got " + e.getJavaClassname() + jArray, this);
                        if (e.getJavaClassname() != null) {
                            return e.getJavaClassname() + jArray;
                        } else {
                            return null;
                        }
		}
            }
            return null;
            //throw new TypeResolveException(typeString, "Cannot find Java class for " + typeString);
	}
        
        	// looks through the library entry files for this guy.
	public String getTypeFor(MemberAccessExpression mae) {
		Debug.logn("Seek type for MemberAccessExpression " + mae, this);
                
		DNType type = mae.getExpression().getDNType();
                Debug.logn("MAE target type is " + type, this);
                List entries = this.getAllEntriesFor(type);
                entries.addAll(libraryData.getRuntimeEntries());
                Debug.logn("Look on entries " + type, this);
		for (Iterator itr = entries.iterator(); itr.hasNext();) {
			Entry e = (Entry) itr.next();
			String c = e.getDNTypeForMemberAccessExpression(mae.getVariableMemberName());
			if (c != null) {
				//System.out.println(" got " + c);
				return c;
			}
		}
		return null;

	}
        
        public Signature getSignatureFor(InvocationExpression ie) {
            Debug.logn("Get signature for ie: " + ie, this);
            List entries = this.getAllEntriesFor(ie.getTarget().getDNType());
            entries.addAll(libraryData.getRuntimeEntries());
            for (Iterator itr = entries.iterator(); itr.hasNext();) {
		Entry e = (Entry) itr.next();
                Debug.logn("check next entry: " + e, this);
                
		Signature sig = e.getSignatureForInvocation(ie.getMethodName(), ie.getArgs(), this);
		if (sig != null) {
                    Debug.logn("yes: " + sig, this);
                    return sig;
		}
                Debug.logn("no: " + e, this);
            }
            return null;
	}
        
        
        

    
}

 