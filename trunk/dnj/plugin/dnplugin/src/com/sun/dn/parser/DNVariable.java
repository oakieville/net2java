
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

import java.util.*;
import com.sun.dn.*;
import com.sun.dn.java.*;
import com.sun.dn.util.*;
import com.sun.dn.Library;


	/** A variable in .NET 
	** @author danny.coward@sun.com
	*/

public class DNVariable {
	protected String name;
	protected String type;
	protected String jType;
	protected String initStatement;
	protected List modifiers = new ArrayList();
        protected boolean isRef = false;
	// is it a member variable of a class ?
	protected boolean isMember = false;
	protected List dimension; // of Integers
	
	protected DNVariable() {}

	public String getJType() {
		return this.jType;
	}
        
        public void setRef(boolean isRef) {
            this.isRef = isRef;
        }
        
        public boolean isRef() {
            return this.isRef;
        }

	public void setJType(String jType) {
		//System.out.println("*************boom " + jType);
		//if (true) throw new RuntimeException("stop");
		this.jType = jType;
	}

	public String getName() {
		return this.name;
	}

	public List getDimension() {
		return this.dimension;
	}
        
        public void resetDimension(List dimension) {
            this.dimension = dimension;
        }

	public static String parseVBName(String nameDim) {
		if (-1 == nameDim.indexOf('(')) {
			return nameDim;
		} else {
			return nameDim.substring(0, nameDim.indexOf('('));
		}
	}

	public static List parseVBDimension(String nameDim) {
		if (-1 == nameDim.indexOf('(')) {
			return null;
		} else {
			List l = new ArrayList();
			String dimString = nameDim.substring(nameDim.indexOf('(') + 1, nameDim.indexOf(')'));
			StringTokenizer st = new StringTokenizer(dimString, ",");
			while(st.hasMoreTokens()) {
				String nextToken = st.nextToken().trim();
				l.add(new Integer(nextToken));
			}
			return l;
		}
	}

	public static String parseCSType(String typeString) {
		boolean parsing = true;
		String s = typeString;
		while (parsing) {
			if (s.endsWith("[]")) {
				s = s.substring(0, s.length() - 2);
			} else {
				return s;
			}
		}
		return s;
	}

	public static DNVariable createCSVariable(String nameString, String typeString) {
                Debug.clogn("Create CS Variable of name ." + nameString + ". and type ." + typeString + ".", DNVariable.class);
		DNVariable variable = new DNVariable();
		variable.name = nameString;
		if (typeString.endsWith("[]")) {
                        Debug.clogn("Its multi dimensional", DNVariable.class);
			variable.dimension = new ArrayList();
			variable.type = typeString.substring(0, typeString.length() -2);
		} else {
			variable.type = typeString;
		}
                 Debug.clogn("Is point ?" + variable.isPoint(), DNVariable.class);
		return variable;
	}

	public static DNVariable createVBVariable(String nameDim, String typeString) {
		DNVariable v = new DNVariable();
		v.name = parseVBName(nameDim.trim());
		v.dimension = parseVBDimension(nameDim.trim());
		v.parseVBType(typeString);
		return v;
	}

	public static DNVariable createUnknownVariable(String name) {
		DNVariable v = new DNVariable();
		v.name = name.trim();
		v.type = UnknownType.UNKNOWN;
		return v;
	}

	private void parseVBType(String ts) {
            String typeString = ts.trim();
            if (typeString == null) {
                throw new RuntimeException("Can't make a VBVariable with null type");
            }
            if (typeString.endsWith("()")) {
                //System.out.println("VBVar- " + this + " is a dimensionless array");
		// its an array of undetermined dimension
		if (this.dimension != null) {
                    throw new RuntimeException("This shouldn't happen: dimension is both assigned in the name and in the type");
                }
		dimension = new ArrayList();
		this.type = typeString.substring(0, typeString.indexOf("()"));
			//System.out.println("** So the type is " + type);
            } else {
                this.type = typeString;
            }
	}

	public void setMember(boolean isMember) {
		this.isMember = isMember;
	}

	public boolean isMember() {
		return isMember;
	}

	public String getType() {
		return type;
	}

	public String getVBFullType() {
		if (this.isPoint()) {
			return this.getType();
		} else {
			return this.getType() + "()";
		}
	}
        
        public String getCSFullType() {
		if (this.isPoint()) {
			return this.getType();
		} else {
			return this.getType() + "[]";
		}
	}

	// is an ordinary variable that is not an array
	public boolean isPoint() {
		return isPoint(this.dimension);
	}

	public boolean isDimensionlessArray() {
		return isDimensionlessArray(this.dimension);
	}

	private static boolean isDimensionlessArray(List l) {
		return (l != null) && (l.size() == 0);
	}

	private static boolean isPoint(List l) {
		return (l == null) || 
				((l.size() == 1) && ((Integer) l.get(0)).equals(new Integer(1)));
	}

	public String toString() {
		return "VBVariable: name-" + name + " type-" + type + 
				" dim: " + dimension + " mem: " + isMember;
	}

	public String asJavaDeclaration(Library library, boolean typeNeedsDeclaring) {
		//System.out.println("as Java decl " + this);
		String javaType = library.getJavaTypeFor(this.getType());
		if (javaType == null) {

			throw new RuntimeException("null Java type for " + this);
		}
		//System.out.println(" java type is " + javaType);
		return writeJavaFor(this.getName(), javaType, typeNeedsDeclaring, this.getDimension());
	}

        public static String writeJavaFor(String name, String javaType, List dimension) {
            return writeJavaFor(name, javaType, true, dimension);
        }
        
	public static String writeJavaFor(String name, String javaType, boolean typeNeedsDeclaring, List dimension) {
		//System.out.println("Write Java for name:" + name + " type:" + javaType + " dimemsion:" + dimension);
	
		if (isPoint(dimension)) {
                        if (typeNeedsDeclaring) {
                            return javaType + " " + name;
                        } else {
                            return name;
                        }
		} else {
			String s = javaType;
			if (isDimensionlessArray(dimension)) {
				s = s + "[]";
			} else {
				for (int i = 0; i < dimension.size(); i++) {
					s = s + "[]";
				}
			}
                        if (typeNeedsDeclaring) {
                            s = s + " " + name;
                        } else {
                            s = name;
                        }
			
			if (isDimensionlessArray(dimension)) {
				//s = s + " = " + JavaKeywords.J_NULL;
			} else {
				s = s + " = " + JavaKeywords.J_NEW + " " + javaType;
				for (int i = 0; i < dimension.size(); i++) {
					s = s + "[";
					Integer next = (Integer) dimension.get(i);
					s = s + next.intValue() + "]";
				}
			}
			return s;
		}
	}


}
 