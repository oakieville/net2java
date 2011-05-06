
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
package com.sun.dn.java;


import java.io.*;
import java.util.*;
import com.sun.dn.parser.DNVariable;
import com.sun.dn.*;
import com.sun.dn.util.*;

	/** A Java class.
	@author danny.coward@sun.com
	*/

public class JavaClass {
	protected static String OBJECT = "Object";
	protected String name;
	protected List methods = new ArrayList(); 
	protected List imports = new ArrayList();
	protected List comments = new ArrayList();
        protected List postDeclarationComments = new ArrayList();
	protected List modifiers = new ArrayList();
	protected String superClassname;
	protected List interfacesImplemented = new ArrayList();
	protected List memberVariables = new ArrayList();
	protected List innerClasses = new ArrayList();
	protected boolean isInterface;
	protected String packageName = "";

	public JavaClass(String name, List modifiers, String superClassname, 
				List interfacesImplemented, boolean isInterface) {

		this.name = name;
		
		this.superClassname = superClassname;
		this.interfacesImplemented = interfacesImplemented;
		this.isInterface = isInterface;
		// dannyc
		this.imports.add("java.util.*");
                for (Iterator itr = modifiers.iterator(); itr.hasNext();) {
                    String modifier = (String) itr.next();
                    this.addModifier(modifier);
                }
	}
        
        public static String getShortName(String name) {
            if (name.indexOf(".") != -1) {
                return name.substring(name.lastIndexOf(".") + 1, name.length());
            }
            return name;
        }
        
        
        
        public static String getPackageName(String name) {
            if (name.indexOf(".") != -1) {
                return name.substring(0, name.lastIndexOf("."));
            }
            return "";
        }

	public JavaClass(DNVariable variable) {
		if (variable.getJType() != null) {
			this.name = variable.getJType();
		} else {
			this.name = variable.getType();
		}
	}
        
        public void makePublic() {
            this.addModifier(JavaKeywords.J_PUBLIC);
            this.modifiers.remove(JavaKeywords.J_PROTECTED);
            this.modifiers.remove(JavaKeywords.J_PRIVATE);
        }
        
        public void makeAbstract() {
            
             this.addModifier(JavaKeywords.J_ABSTRACT);
            
        }

	public void addModifier(String modifier) {
            if (!modifiers.contains(modifier)) {
                modifiers.add(modifier);
            }
	}

	public void addInnerClass(JavaClass jClass) {
		this.innerClasses.add(jClass);
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public List findMethodsByName(String name) {
		List l = new ArrayList();
		for (Iterator itr = this.methods.iterator(); itr.hasNext();) {
			JavaMethod jm = (JavaMethod) itr.next();
			if (jm.getName().equals(name)) {
				l.add(jm);
			}
		}
		return l;
	}


	public void addImports(List importStrings) {
		//System.out.println("Adding " + importStrings + " to " + this);
		for (Iterator itr = importStrings.iterator(); itr.hasNext();) {
			String nextImport = (String) itr.next();
			if (!this.imports.contains(nextImport)) {
				this.imports.add(nextImport);
			}
		}
	}

	public void addMemberVariable(JavaVariable jv) {
		this.memberVariables.add(jv);
	}

	public List getMemberVariables() {
		return this.memberVariables;
	}

	// probably need this for creating the Java structure
	public JavaClass(Class clazz) {

	}

	public void addComment(String comment) {
		this.comments.add(comment);
	}
        
        public void addPostDeclarationComment(String comment) {
            this.postDeclarationComments.add(comment);
        }

	public String getName() {
		return this.name;
	}
        
        public String getFQName() {
            if (this.packageName.equals("")) {
                return this.getName();
            } else {
                return this.packageName + "." + this.getName();
            }
        }

	public void addMethod(JavaMethod method) {
		this.methods.add(method);
	}
        
        protected String getCommentCode() {
            return this.getCommentCode(this.comments);
        }
        
        protected String getCommentCode(List cmnts) {
            StringBuffer sb = new StringBuffer();
		
		if (cmnts.size() > 0) {
			sb.append("\t/**\n");
			for (Iterator itr = cmnts.iterator(); itr.hasNext();) {
				String next = (String) itr.next();
				sb.append("\t* " + next);
                                if (itr.hasNext()) {
                                    sb.append("\n");
                                }
			}
			sb.append("\n\t**/\n\n");
		}
		return new String(sb);
	}
        
        
       
        protected String getImportsCode() {
            String s = "";
            for (Iterator itr = imports.iterator(); itr.hasNext();) {
                String nextImport = (String) itr.next();
                s = s + JavaKeywords.J_IMPORT + " " + nextImport + ";\n";
            }
            s = s + "\n";
            return s;
        }
        
	private String asCode(boolean writeImports) {
		StringBuffer sb = new StringBuffer();
		if (!this.packageName.equals("")) {
			sb.append(JavaKeywords.J_PACKAGE + " " + this.packageName + ";\n\n");
		} 

		if (writeImports) {
			sb.append(this.getImportsCode());
		}

		sb.append(this.getCommentCode(this.comments));
		
		for (Iterator itr = this.modifiers.iterator(); itr.hasNext();) {
			String next = (String) itr.next();
			if (!"".equals(next)) {
				sb.append(next + " ");
			}
		}

		String classOrInterface = "";
		if (this.isInterface) {
			classOrInterface = JavaKeywords.J_INTERFACE;
		} else {
			classOrInterface = JavaKeywords.J_CLASS;
		}

		sb.append(classOrInterface + " " + name + " ");
		if (this.superClassname != null && !this.superClassname.equals(OBJECT)) {
			sb.append(JavaKeywords.J_EXTENDS + " " + this.superClassname + " ");
		}

		if (this.interfacesImplemented.size() > 0) {
			sb.append(JavaKeywords.J_IMPLEMENTS + " ");
			for (Iterator itr = this.interfacesImplemented.iterator(); itr.hasNext();) {
				sb.append(itr.next());
                                if (itr.hasNext()) {
                                    sb.append(", ");
                                } else {
                                    sb.append(" ");
                                }
			}
		}
		sb.append("{\n");
		int indent = 1;
                
                sb.append(getCommentCode(this.postDeclarationComments));
                
		for (Iterator itr = this.innerClasses.iterator(); itr.hasNext();) {
			JavaClass jc = (JavaClass) itr.next();
			sb.append("\t" + jc.asCode(false) + "\n");
		}

		for (Iterator itr = memberVariables.iterator(); itr.hasNext();) {
			JavaVariable next = (JavaVariable) itr.next();
                        if (!next.getComments().isEmpty()) {
                            sb.append(Util.getIndent(1) + "/** ");
                            for (Iterator itrr = next.getComments().iterator(); itrr.hasNext();) {
                                String nextComment = (String) itrr.next();
                                sb.append(Util.getIndent(1) + nextComment + "\n");
                            }
                            sb.append(Util.getIndent(1) + " **/\n");
                        }
			sb.append(Util.getIndent(1) + next.asJava() + ";" + "\n");
		}
		for (Iterator itr = methods.iterator(); itr.hasNext();) {
			JavaMethod jm = (JavaMethod) itr.next();
			sb.append("\n");
			sb.append(jm.asCode(indent));
			sb.append("\n");
		}	
		sb.append("}");
		return new String(sb);

	}

	public String asCode() {
		return this.asCode(true);
	}
	

}
 