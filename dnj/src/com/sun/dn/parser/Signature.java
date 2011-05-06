
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

import com.sun.dn.util.Debug;
import com.sun.dn.util.Util;
import com.sun.dn.Library;
import com.sun.dn.parser.statement.CSComment;
import com.sun.dn.parser.statement.VBComment;
import com.sun.dn.parser.statement.StatementAdapter;
import java.util.*;


	/** The method signature of a .NET  method.
	** @author danny.coward@sun.com
	*/

public class Signature {
	private String name;
	private List args = new ArrayList();
	private String returnType;
	private List modifiers = new ArrayList();
	private String methodImplements = ""; // may not be there
	private String attributesList; 	// may not be there
	private String handlesClause;		// may not be there
        boolean isAbstract = false;
        boolean isUnsafe = false;
        boolean isExternal = false;
        private String originalCode;
	public static String MAIN = "Main";
        private List comments = new ArrayList();
        private boolean hidesBase = false;
        private boolean canBeHidden = false;
        
		// [STAThread] static void Main(string[] args)

	public static Signature parseCS(String code) {
		Signature signature = new Signature();
                signature.originalCode = code.trim();
                Debug.clogn("Parse " + code, Signature.class);
		List tokens = Util.tokenizeIgnoringEnclosers(code, " ");
		for (Iterator itr = tokens.iterator(); itr.hasNext();) {
			String next = ((String) itr.next()).trim();
			if (CSKeywords.getMemberModifiers().contains(next)) {
				signature.modifiers.add(next);
			} else if (StatementAdapter.isCSAttribute(next)) {
                            //System.out.println("Adding new comment " + next);
                                signature.comments.add(new CSComment("// Translator: Was marked: " + next, null)); 
			} else if (next.equals(CSKeywords.CS_Static)) {
                                signature.modifiers.add(next);
                        } else if (next.equals(CSKeywords.CS_Extern)) {
                                signature.comments.add(new CSComment("// Translator: This is an external C# method, so its implementation is in the dll provided.", null));
                                signature.comments.add(new CSComment("// Translator: Therefore, I've marked it abstract.", null));
                                signature.isExternal = true;
                        } else if (next.equals(CSKeywords.CS_Unsafe)) {
                               signature.isUnsafe = true;
                        } else if (next.equals(CSKeywords.CS_Abstract)) {
                            signature.modifiers.add(next);
                            signature.isAbstract = true;
                        } else if (next.equals(CSKeywords.CS_Delegate)) {
                            // this is really ok since it means I'm
                            // being used by a CSDelegateDeclaration
                        } else if (next.equals(CSKeywords.CS_New)) {
                                signature.hidesBase = true;
                        } else if (next.equals(CSKeywords.CS_Override)) {
                                signature.hidesBase = false;
                        } else if (next.equals(CSKeywords.CS_Virtual)) {
                                signature.canBeHidden = true;
			} else if (next.indexOf("(") != -1) {
				// then this is the name and the args
				signature.name = next.substring(0, next.indexOf("("));
				//System.out.println("Sig name " + signature.name);
				String argsString = next.substring(next.indexOf("("), next.length());
				argsString = Util.stripBrackets(argsString);
                                argsString = Util.stripCurlies(argsString); 
				//System.out.println(" arg string " + argsString );

                                    /// ugly: return type is one before the identifier
                                int i = tokens.indexOf(next);
                                String oneBeforeName = ((String) tokens.get(i-1)).trim();
                                if (oneBeforeName.equals(CSKeywords.CS_Operator)) {
                                    // that's ok, we'll just skip it
                                    String beforeThat = ((String) tokens.get(i-2)).trim();
                                    if (beforeThat.equals(CSKeywords.CS_Implicit) || beforeThat.equals(CSKeywords.CS_Explicit)) {
                                        signature.returnType = signature.name;
                                    } else {
                                        signature.returnType = beforeThat;
                                    }
                                } else { 
                                    signature.returnType = oneBeforeName;
                                }
                                //System.out.println(code);
                                //System.out.println("Name " + signature.name);
                                //System.out.println("ReturnType " + signature.returnType);
				parseCSSigArgs(signature, argsString);
				
			} else {
                            //System.out.println("Next = " + next);
                           // throw new TypeResolveException("I don't understand keyword: " + next, code);
                        }
		}
                      
		
		Debug.logn("Parsed sig from " + code, Signature.class);
		//if (true) { throw new Stop(Signature.class); }
		return signature;
	}
        
        private static void parseCSSigArgs(Signature signature, String argsString) {
            StringTokenizer st = new StringTokenizer(argsString, ",");
            //System.out.println(argsString);
            while(st.hasMoreTokens()) {
                String nextArgPair = st.nextToken();
                StringTokenizer argT = new StringTokenizer(nextArgPair);
                String first = argT.nextToken();
                boolean isRef = false;
                String typeS = "";
                String nameS = "";
                if (first.equals(CSKeywords.CS_Out) || first.equals(CSKeywords.CS_Ref)) {
                    if (first.equals(CSKeywords.CS_Ref)) {
                        isRef = true;
                    }
                    typeS = argT.nextToken();
                    nameS = argT.nextToken();
                } else {
                    typeS = first;
                    nameS = argT.nextToken();
                }
                DNVariable v = DNVariable.createCSVariable(nameS, typeS);
                v.setRef(isRef);
                signature.args.add(v);
                //System.out.println("Adding + " + v);
            }
        }

	private Signature() {
	}
        
        public String getOriginalCode() {
            return this.originalCode;
        }
        
        public List getComments() {
            return this.comments;
        }

	public void makeCSPublic() {
		this.makePublic(CSKeywords.CS_Public, CSKeywords.getMemberModifiers());
	}
        
        public void makeVBPublic() {
		this.makePublic(VBKeywords.VB_Public, VBKeywords.getAccessKeywords());
	}
        
        private void makePublic(String kw, List memMods) {
            List accessModifiers = CSKeywords.getMemberModifiers();
            List mods = new ArrayList();
            mods.addAll(this.modifiers);
            for (Iterator itr = mods.iterator(); itr.hasNext();) {
		String next = (String) itr.next();
		if (memMods.contains(next)) {
            	this.modifiers.remove(next);
		}
            }
            this.modifiers.add(kw);
        }

	public String getName() { 
		return name;
	}

	public String getReturnType() {
            if (this.returnType == null) {
                return NullType.NULLTYPE;
            }
            return returnType;
		
	}

	private static boolean isNew(String s) {
		return s.equals(VBKeywords.VB_New) || s.equals(CSKeywords.CS_New);
	}
        
        public boolean isAbstract() {
            return this.isAbstract;
        }
        
        public boolean isExternal() {
            return this.isExternal;
        }
        
        public boolean isUnsafe() {
            return this.isUnsafe;
        }

	public String getHandlesClause() {
		return this.handlesClause;
	}
        
        public boolean matchesSignature(String mName, List mArgs, Library library) {
            boolean namesStrictMatch = mName.equals(this.getName());
            boolean namesAreNewKeywords = isNew(this.name) && isNew(mName);
            boolean namesMatch = namesStrictMatch || namesAreNewKeywords;
            //System.out.println("Matches Signature " + mName + " " + mArgs + " " + this);
            //System.out.println("Names " + namesMatch);
            //System.out.println("Args " + this.doArgsMatch(mArgs, library));
            //System.out.println("Names " + this.getName() + " " + mName);
            if (namesMatch && this.doArgsMatch(mArgs, library, false)) { 
                return true;

            }
            return false;
        }
        
         public boolean doArgsMatch(List myArgs, Library library) {     
             return doArgsMatch(myArgs, library, false);
         }
         
        private boolean doArgsMatch(List myArgs, Library library, boolean b) {
            //boolean b =  false;//this.getName().equals("New");// && sig.getName().equals("test");
		//boolean b = this.getArgs().size() == 1;
            if (b) System.out.println("DO THESE ARGS MATCH: " + this.getArgs() + "---" + myArgs);
            if (this.getArgs().size() != myArgs.size()) {
                if (b) System.out.println("Different args size");
		return false;
            }
            for (int i = 0; i < this.getArgs().size(); i++) {
		DNVariable sigArgVariable = (DNVariable) this.getArgs().get(i); // ith sig arg variable
		DNType sigArgClass = library.getDNType(sigArgVariable.getVBFullType());
		if (sigArgClass == null) throw new RuntimeException("Cannot continue - was counting on finding the sig types in the library");
			
		if (b) System.out.println("SigVariable= " + sigArgVariable);
			
		DNType myArgClass = null;
		if (b) System.out.println("myV= " + myArgs.get(i));

		if (myArgs.get(i) instanceof DNVariable) {
                    DNVariable myArgVariable = (DNVariable) myArgs.get(i); // ith of my args value
                    if (sigArgVariable.isPoint() != myArgVariable.isPoint()) {
			return false;
                    }
                    myArgClass = library.getDNType(myArgVariable.getVBFullType());
                    if (b) System.out.println("my arg type = " + myArgVariable.getVBFullType());

                    if (myArgClass == null) throw new RuntimeException("Cannot continue - was counting on finding the sig types in the library");
		} else {
                    Expression myArgExpression = (Expression) myArgs.get(i);
                    if (b) System.out.println("my arg type = " + myArgExpression.getTypeName());
                    myArgClass = library.getDNType(myArgExpression.getTypeName());
                    //if (sigArgVariable.isPoint() != myArgExpression.isPoint()) {
                    //	return false;
                    //}

                    if (myArgClass == null) throw new RuntimeException("Cannot continue - was counting on finding the sig types in the library");
                    // but how do you figure out the dimension of this ?
                 }

                 if (b) System.out.println("compare (sigarg)" + sigArgClass + " with (myArg)" + myArgClass);
		 boolean myArgIsNull = myArgClass.getName().equals(NullType.NULLTYPE);
                // System.out.println("My arg os null " + myArgIsNull);
                 if (!myArgIsNull && !sigArgClass.isEqualOrIsSuperType(myArgClass)) {
                    if (b) System.out.println("NO THEY DO NOT");
                    return false;
		}
            }
            if (b) System.out.println("YES THEY DO");

            return true;
	}

	public boolean typeEquals(String t1, String t2) {
		//System.out.println(t1 + " type equals " + t2 + " " + VBClass.shortNameEquals(t1, t2));
		return DNType.shortNameEquals(t1, t2);
	}

	public Signature(String s) {
		this.parseVB(s);
	}

		/**
		<name>(<arg1Name> As <Type1>, <arg2> As <Type2>, ...) As <returnType>
		getTime(i As Integer) As Date
		*/
	public void parseVB(String s) {
                
		Debug.logn("Parse Signature from ." + s + ".", this);
		String trimmed = s.trim();
                this.originalCode = trimmed;
		if (trimmed.startsWith("<")) {
			this.attributesList = trimmed.substring(1, trimmed.indexOf(">"));
			Debug.logn("att list " + attributesList, this);
			trimmed = trimmed.substring(trimmed.indexOf(">")+1, trimmed.length());
                        VBComment comment = new VBComment("'Translator: Uses metadata: " + attributesList, null);
                        this.comments.add(comment);
		}
                
		StringTokenizer st = new StringTokenizer(trimmed);
		boolean readingModifiers = true;
		String firstPartOfName = "";
		String next = "";
                String lastWordBeforeName = "";
                String wordAfterName = "(";
		while (readingModifiers && st.hasMoreTokens() ) {
                        //System.out.println(next);
			next = st.nextToken().trim();
                        //System.out.println(next);
			if (getVBMemberModifierKeywords().contains(next)) {
				modifiers.add(next);
                        } else if (next.equals(VBKeywords.VB_Operator)) {
                            // skip it, VBFunctions take care of this
                            lastWordBeforeName = next;
                            //System.out.println("skipped operator kw");
                        } else if (next.equals(VBKeywords.VB_Declare)) {
                                
                                this.comments.add(new VBComment("' Translator: This is an external VB method, so its implementation is in the dll provided.", null));
                                this.comments.add(new VBComment("' Translator: Therefore, I've marked it abstract.", null));
                                this.isExternal = true;
                        } else if (next.equals(VBKeywords.VB_Lib)) {
                            String dllName = st.nextToken().trim();
                            wordAfterName = VBKeywords.VB_Lib;
                            this.comments.add(new VBComment("' Translator: Uses dll: " + dllName, null));
                        } else if (next.equals(VBKeywords.VB_Alias)) {
                            String aliasName = st.nextToken().trim();
                            lastWordBeforeName = VBKeywords.VB_Alias;
                            wordAfterName = "(";
                            this.comments.add(new VBComment("' Translator: Alised as: " + aliasName, null));
                        } else if (next.equals(VBKeywords.VB_Delegate)) {
                            // this is ok because we must be in a delegate statement parse
			} else if (next.equals(VBKeywords.VB_Sub) || next.equals(VBKeywords.VB_Function)) {
				lastWordBeforeName = next;
			} else {
                                //readingModifiers = false;
                                //System.out.println("Done");
				//System.out.println(trimmed);
				//throw new RuntimeException("Error parsing signature ." + trimmed + ".");
			}
		} 
		Debug.logn("Modifiers " + modifiers, this);
			
		name = trimmed.substring(trimmed.indexOf(lastWordBeforeName) + lastWordBeforeName.length(), trimmed.indexOf(wordAfterName)).trim();
                name = Util.stripQuotes(name);
                Debug.logn("Name is " + name, this);
		String containsArgs = trimmed.substring(trimmed.indexOf('('), trimmed.length());
		String argsString = null;
		String rest = null;
		if (containsArgs.equals("()")) {
			argsString = "";
			rest = trimmed.substring(trimmed.indexOf(')') + 1, trimmed.length());
		} else {
			List l = Util.tokenizeIgnoringEnclosers(containsArgs + " ", ")");
			String leadingBracketArgs = (String) l.get(0);
			argsString = leadingBracketArgs.substring(1, leadingBracketArgs.length());
			if (l.size() > 1) {
				rest = containsArgs.substring(containsArgs.indexOf(argsString) + argsString.length() + 1, containsArgs.length());;
				if (rest.startsWith(")")) {
					rest = rest.substring(1, rest.length());
				}
			} else {
				rest = "";
			}
			//System.out.println("----------" + l.size());
		}
	
		Debug.logn("args string = " + argsString , this);
		Debug.logn("rest string = " + rest, this);
		
		args = parseVBArgs(argsString);
		
		StringTokenizer stt = new StringTokenizer(rest);
		
		if (stt.hasMoreTokens()) {
			String nextW = stt.nextToken();
			if (nextW.equals(VBKeywords.VB_As)) {
				returnType = this.parseReturnTypeString(stt.nextToken());
			} else if (nextW.equals(VBKeywords.VB_Handles)) {
				// this is ok because only Subroutines can be event handler methods
				//System.out.println(this + " is event handler");
				String hc = "";
				while(stt.hasMoreTokens()) {
					hc = hc + stt.nextToken();
				}
				this.handlesClause = hc;
				//if (true) throw new Stop(this.getClass());

			}
			this.methodImplements = this.makeString(stt);
			Debug.logn("and the method implements bit is " + this.methodImplements , this);
		}
               
		Debug.logn(""+this, this);
	}

	private String parseReturnTypeString(String s) {
		//if (s.endsWith("()")) {
		//	return s.substring(0, s.length() -2);
		//}
		return s;
	}

	private String makeString(StringTokenizer st) {
		String s = "";
		while(st.hasMoreElements()) {
			s = s + " " + st.nextToken();
		}
		return s;
	}

	public static List parseVBArgs(String argString) {
                List arguments = new ArrayList();
		boolean b = false; //this.name.equals("Trim");
		Debug.clogn("Parsing args " + argString, Signature.class);
		if (b) System.out.println("Parsing args " + argString);
		StringTokenizer st = new StringTokenizer(argString, ",");
		while(st.hasMoreTokens()) {
			String nextArg = st.nextToken();
                        boolean isRef = false;
			if (b) System.out.println(" next arg args " + nextArg );

			StringTokenizer argT = new StringTokenizer(nextArg);
			if (!argT.hasMoreTokens()) {
				throw new RuntimeException("shouldn't be here " + argString);
			}
			String nextT = argT.nextToken();
			if (nextT.equals(VBKeywords.VB_ByVal)) {
                            isRef = false;
                        } else if (nextT.equals(VBKeywords.VB_ByRef)) {
                            isRef = true;  
			} else if (nextT.equals(VBKeywords.VB_Optional)) {
				// read on one more
				if (!argT.hasMoreTokens()) {
					throw new RuntimeException("shouldn't be here " + argString);
				}
				String byVal = argT.nextToken();
				if (!byVal.equals(VBKeywords.VB_ByVal)) {
					throw new RuntimeException("Error 1 parsing args " + argString);
				}
			} else {
				throw new RuntimeException("Error 2 parsing args " + argString);
			}

			if (!argT.hasMoreTokens()) {
				throw new RuntimeException("shouldn't be here " + argString);
			}

			String name = "";
			String nameOrParamArray = argT.nextToken();
			if (nameOrParamArray.equals("ParamArray")) {
				name = argT.nextToken();
			} else {
				name = nameOrParamArray;
			}
			if (!argT.hasMoreTokens()) {
				throw new RuntimeException("shouldn't be here " + argString);
			}

			String asK = argT.nextToken();
			if (!argT.hasMoreTokens()) {
				throw new RuntimeException("shouldn't be here " + argString);
			}

			String type = argT.nextToken();
			DNVariable variable = DNVariable.createVBVariable(name, type);
                        variable.setRef(isRef);
			Debug.clogn("Add param " + variable, Signature.class);
			if (b) System.out.println("Add param " + variable);
			arguments.add(variable);
		}
                return arguments;

	}

	public static List getVBMemberModifierKeywords() {
		List l = new ArrayList();
		l.addAll(VBKeywords.getAccessKeywords());
		l.addAll(VBKeywords.getSharedKeywords());
		l.addAll(VBKeywords.getShadowKeywords());
		return l;	
	}

	public boolean isMain() {
		return this.name.equals(MAIN);
	}
        
        public boolean isShared() {
            return this.modifiers.contains(VBKeywords.VB_Shared) || this.modifiers.contains(CSKeywords.CS_Static); 
        }

	public List getArgs() {
		return this.args;
	}

	public List getModifiers() {
		return this.modifiers;
	}	

	public String toString() {
		//if (true) throw new RuntimeException("stop");
		String s = "[Sig: nm: " + name;
		//if (returnType != null) {
			s = s + " ret: " + returnType;
		//}
		s = s + " mods: " + modifiers;
		s = s + " args: " + args; 
		return s + "]";
	}
}


 