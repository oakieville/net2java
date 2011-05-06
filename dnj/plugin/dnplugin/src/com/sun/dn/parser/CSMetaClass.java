
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
	
//import com.sun.corba.se.spi.legacy.interceptor.UnknownType;
import java.util.*;
import com.sun.dn.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.parser.statement.*;
import com.sun.dn.util.*;

	/** The abstract superclass for VB modules interfaces and classes.
	* @author danny.coward@sun.com
	*/

public abstract class CSMetaClass extends MetaClass implements InterpretationContext {
	
        protected abstract String myKeyword();
        protected abstract void registerType();
        
        public void parseBody() {
            String cd = super.getOriginalCode();
            String classBody = cd.substring(cd.indexOf("{") + 1, cd.lastIndexOf("}") -1 ).trim();
            this.parseBody(classBody);
        }
        
        public void addMetaClass(MetaClass mc) {
            
            if (this.superClassname.equals(DEFAULT_SUPERCLASS_NAME)) {
                this.superClassname = mc.superClassname;
            }
            //System.out.println("--" + this.superClassname);
            this.interfacesImplemented.addAll(mc.interfacesImplemented);
            //System.out.println(super.getOriginalCode());
            //System.out.println(Util.getInsideCurlies(mc.getOriginalCode()));
            String newBody = addMetaClassBodyTo(super.getOriginalCode(), Util.getInsideCurlies(mc.getOriginalCode()));
            //System.out.println(newBody);
            super.setCode(newBody);
            
            super.resetDNType();
            
        }
        
       
        private static String addMetaClassBodyTo(String targetClassCode, String body) {
            String s = targetClassCode.substring(0, targetClassCode.lastIndexOf("}"));
            s = s + "\n";
            s = s + body;
            s = s + "\n";
            s = s + "}";
            return s;
        }    
            
            
        
        
        public CSMetaClass(String code, InterpretationContext context) {
		super(code, context);
		//Debug.logn("Parse " + code, this);
		String declaration = code.substring(0, code.indexOf("{"));
		this.parseDeclaration(declaration);
                
		Debug.logn("Parsed " + this, this);
	}
       
        
  
	protected void parseBody(String bodyCode) {
            boolean isInterface = this instanceof CSInterfaceStatement;
            List bodyStatements = this.tokenizeToClassStatements(bodyCode);
            //System.out.print("----------------." + bodyStatements + ".");
            Debug.logn("Here ", this );
            Map codeListToStatementMap = new HashMap();
            StatementAdapter statementCreated = null;
            List comments = new ArrayList();
            for (Iterator itr = bodyStatements.iterator(); itr.hasNext();) {
            	String nextStatementString = (String) itr.next();
                try {
                    Debug.logn("Parse next statement in class " + nextStatementString, this );
                    if (CSComment.isComment(nextStatementString, super.context)) {
                        Comment comment = new CSComment(nextStatementString, context);
                        Debug.logn("Its a comment", this );
                        comments.add(comment);
                        statementCreated = null;
                    } else if (CSOperatorStatement.isOperatorStatement(nextStatementString, context)) {
                        Debug.logn("Its an operator statement", this );
                        List oBodyStatements = new ArrayList();
			statementCreated = new CSOperatorStatement(nextStatementString, isInterface, this, oBodyStatements );
			super.members.add(statementCreated);
                        codeListToStatementMap.put(oBodyStatements, statementCreated);
                    } else if (DelegateStatementImpl.isCSDelegateStatement(nextStatementString, context)) {
                        Debug.logn("Its a delegate declaration", this );
                        statementCreated = DelegateStatementImpl.createCSDelegateStatement(nextStatementString, context);
                        super.delegates.add(statementCreated);
                    } else if (CSEnumStatement.isCSEnumStatement(nextStatementString, context)) {
                        Debug.logn("Its a delegate declaration", this );
                        statementCreated = new CSEnumStatement(nextStatementString, context);
                        ((CSEnumStatement) statementCreated).parseBody();
                        super.enumerations.add(statementCreated);
                    } else if (CSMethodStatement.isMethodStatement(nextStatementString, this)) {
                        Debug.logn("Its a method", this );
			List mBodyStatements = new ArrayList();
			statementCreated = new CSMethodStatement(nextStatementString, isInterface, this, mBodyStatements );
			super.members.add(statementCreated);
			codeListToStatementMap.put(mBodyStatements, statementCreated);
                    } else if (EventStatement.isCSEventStatement(nextStatementString , context)) {
                        Debug.logn("Its an event", this );
                        statementCreated = EventStatement.createCSEventStatement(nextStatementString, context);
                        super.events.add(statementCreated); 
                    } else if (VariableMemberDeclaration.isCSVariableMember(nextStatementString , this)) {
                        Debug.logn("Its a class or instance variable", this );
			statementCreated = VariableMemberDeclaration.createCSVariableMemberDeclaration(nextStatementString, this);
			super.variableMembers.add(statementCreated);
                    } else if (PropertyStatement.isCSPropertyStatement(nextStatementString , context)) {
                        Debug.logn("Its a property", this );
                        List pStatements = PropertyStatement.parseCSPropertyLoop(nextStatementString); 
			statementCreated = PropertyStatement.createCSPropertyStatement(nextStatementString, this);
                        codeListToStatementMap.put(pStatements, statementCreated);
                        super.propertyStatements.add(statementCreated);
                    } else {
			System.out.println("statement unknown ." + nextStatementString + ".");
			throw new Stop(this.getClass());
                    }
                    //Debug.logn("here?", this );
                } catch (Throwable pe) {
                   //System.out.println("Here");
                   statementCreated = (StatementAdapter) Parser.handleTopLevelParseError(nextStatementString, pe, context);
                   this.untranslatedStatements.add(statementCreated);
               }
                if (statementCreated != null) {
                    statementCreated.addConstructedPreStatements(comments);
                    comments = new ArrayList();
                }
                
            }
            if (!isInterface) {
                for (Iterator itr = codeListToStatementMap.keySet().iterator(); itr.hasNext();) {
                    List body = (List) itr.next();
                    Object np = codeListToStatementMap.get(body);
                    if (np instanceof CSMethodStatement) {
                        try {
                            ((CSMethodStatement) np).parseBody(body); 
                        } catch (Throwable t) {
                            Parser.handleTopLevelParseError(body.toString(), t, context);
                            CSComment c = new CSComment("'" + body.toString(), context);
                            ((CSMethodStatement) np).addConstructedPreStatement(c);
                        }
                    } else if (np instanceof PropertyStatement) {
                        try {
                            ((PropertyStatement) np).parseCS(body);
                        } catch (Throwable t) {
                            Parser.handleTopLevelParseError(body.toString(), t, context);
                            CSComment c = new CSComment("'" + body.toString(), context);
                            ((PropertyStatement) np).addConstructedPreStatement(c);
                        }
                    }
                }
            }
        }
       
         public static List tokenizeToClassStatements(String code) {
		Debug.clogn("make into class statements " + Util.compactify(code, 30, 30), CSMetaClass.class); 
		List tokens = new ArrayList();
		List bodyStatements = Util.tokenizeSemiColonChunksAndPanhandles(code);
		Debug.clogn("scph chunks = " + bodyStatements , CSClassStatement.class);
		Debug.clogn("Raw statements count1 = " + bodyStatements.size(), CSMetaClass.class);
		bodyStatements = removeRegions(bodyStatements); 
		Debug.clogn("Raw statements count2 = " + bodyStatements.size(), CSMetaClass.class);
		
		// now fish out the regions and comments
		for (Iterator itr = bodyStatements.iterator(); itr.hasNext();) { 
			String next = ((String) itr.next()).trim();
			//System.out.println("next=" + next);
			if (next.startsWith("//")) { 
				StringTokenizer st = new StringTokenizer(next, "\n"); 
				boolean done = false; 
				while(st.hasMoreTokens() && !done) { 
					String nextComment = st.nextToken().trim();
					
					if (nextComment.trim().startsWith("//")) {
						//System.out.println("Add **" + nextComment + "**");
						
                                                addIfNotEmpty(tokens, nextComment);
					} else {
						
						String toAdd = Util.toString(nextComment, st, "\n");
						//System.out.println("Add **" + toAdd  + "**");
						
                                                addIfNotEmpty(tokens, toAdd);
						done = true;
					}
				}
			} else {
				//System.out.println("Add **" + next + "**");
				addIfNotEmpty(tokens, next);
			}
		}
		//System.out.println(tokens);
		return tokens;

	}
         
        private static void addIfNotEmpty(List l, String s) {
            if (!"".equals(s.trim())) {
                l.add(s);
            }
        }
         
        protected static List getDeclarationAsTokens(String declaration) {
            List l = new ArrayList();
            List tokens = Util.tokenizeIgnoringEnclosers(declaration, " ");
            for (Iterator itr = tokens.iterator(); itr.hasNext();) {
                String next = ((String) itr.next()).trim();
                if (next.endsWith(":")) {
                    l.add(next.substring(0, next.length()-1));
                    l.add(":");
                } else {
                    l.add(next);
                }
            }
            List ll = new ArrayList();
            for (Iterator itr = l.iterator(); itr.hasNext();) {
                String next = (String) itr.next();
                ll.addAll(Util.tokenizeIgnoringEnclosers(next, "\n"));
                
            }
            ll = Util.trimStrings(ll);
            return ll;
        }
        
       
        
        protected void parseDeclaration(String declaration) { 
            //StringTokenizer st = new StringTokenizer(declaration);
            List tokens = this.getDeclarationAsTokens(declaration);
            Iterator itr = tokens.iterator();
            Debug.logn("Parse declaration " + declaration, this);
            boolean reading = true;
            while (reading && itr.hasNext()) {
		String token = ((String) itr.next()).trim();
                Debug.logn("Next Token " + token, this);
                if (CSKeywords.getMemberModifiers().contains(token)) {
                    modifiers.add(token);
                    Debug.logn("Added modifier " + token, this);
                } else if (isCSAttribute(token)) {
                    Debug.logn("Found attribute " + token, this);
                    CSComment comment = new CSComment("'Translator: Uses metadata: " + token, this);
                    super.constructedPreStatements.add(comment);
                } else if (token.equals(CSKeywords.CS_Abstract)) {
                    modifiers.add(token);
                } else if (token.equals(CSKeywords.CS_Partial)) {
                    super.isPartial = true;
		} else if (token.equals(myKeyword())) {
                    String nS = ((String) itr.next()).trim();
                    Debug.logn("Set name " + nS, this);
                    super.name = nS;
		} else if (token.equals(":")) {
                    
                    this.parseSuperTypesString(declaration);
                    break;
		} else if (token.equals("")) {
                            
                } else {
                    throw new RuntimeException("Unknown token: " + token);
                }
            }
            this.registerType();
	}
        
        private void parseSuperTypesString(String declaration) throws TypeResolveException {
            String superTypesString = declaration.substring(declaration.indexOf(":") + 1, declaration.length());
            
            StringTokenizer stt = new StringTokenizer(superTypesString, ",");
            while (stt.hasMoreTokens()) {
                String nextSType = stt.nextToken().trim();
                //System.out.println(nextSType);
                Debug.logn("Set superclass " + nextSType, this);
                try {
                    DNType type = this.getLibrary().getProgramDefinedOrLibraryDNTypeFor(nextSType);
                    
                    if (type.isInterface()) {
                        //System.out.println("Interface");
                        this.interfacesImplemented.add(nextSType);
                    } else {
                        //System.out.println("Class");
                        superClassname = nextSType;
                    }
                } catch (TypeResolveException tre) {
                         this.interfacesImplemented.add(nextSType);
                        TranslationWarning trw = new TranslationWarning(nextSType, "Could not resolve supertypename " + nextSType + " in " + name);
                        ParseTree.getParseTree(context).getTranslationReport().addTranslationWarning(trw);
                }
             }
        }
        
        
        private static String removeRegion(String toStrip) {
            String s = toStrip.trim();
            if (s.startsWith("#region") || s.startsWith("#endregion")) {
                StringTokenizer st = new StringTokenizer(s, "\n");
                String nextToken = st.nextToken();
                String stripped = Util.toString("", st, "\n");
                return removeRegion(stripped);
            } else {
                return s;
            }
        }

        protected static List removeRegions(List l) {
		List ll = new ArrayList();
		for (Iterator itr = l.iterator(); itr.hasNext();) {
			String next = ((String) itr.next());
			ll.add(removeRegion(next));
		}	
		return ll;
	}
        
        public List getVariables() {
		List variables = super.getVariables();
		DNVariable thisVar = DNVariable.createCSVariable(CSKeywords.CS_This, this.getDNType().getName());
		variables.add(thisVar);
		return variables;
	}
}

 