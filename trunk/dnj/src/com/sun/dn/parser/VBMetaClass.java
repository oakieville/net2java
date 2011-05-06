
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
import com.sun.dn.parser.expression.*;
import com.sun.dn.parser.statement.*;
import com.sun.dn.util.*;

	/** The abstract superclass for VB modules interfaces and classes.
	* @author danny.coward@sun.com
	*/

public abstract class VBMetaClass extends MetaClass implements InterpretationContext {
    private List bodyStatementsToParse;
    protected abstract boolean isEnd(String s);
       
        
        public void parseBody() {
           boolean thisIsInterface = (this instanceof VBInterfaceStatement);  
           this.parseBodyStatements(bodyStatementsToParse, thisIsInterface, super.context); 
           bodyStatementsToParse = null;
        }
        
        public void addMetaClass(MetaClass mc) {
            //System.out.println("add meta class: " + mc);
            //System.out.println("my superclass is : " + this.superClassname);
            if (this.superClassname.equals(DEFAULT_SUPERCLASS_NAME)) {
                this.superClassname = mc.superClassname;
            }
            this.interfacesImplemented.addAll(mc.interfacesImplemented);
            this.bodyStatementsToParse.addAll(((VBMetaClass) mc).bodyStatementsToParse);
            
            this.resetDNType();
        }
        
        protected VBMetaClass(String code, InterpretationContext context) {
            super(code, context);
        }
        
	private void parseDeclaration(String declaration) {
		Debug.logn("Parse decl " + declaration, this);
		StringTokenizer st;
		String metaClassKeyword = "";
		if (-1 != declaration.indexOf(VBKeywords.VB_Class)) {
			metaClassKeyword = VBKeywords.VB_Class;
		} else if (-1 != declaration.indexOf(VBKeywords.VB_Module)) {
			metaClassKeyword = VBKeywords.VB_Module;
		} else if (-1 != declaration.indexOf(VBKeywords.VB_Interface)) {
			metaClassKeyword = VBKeywords.VB_Interface;
                } else if (-1 != declaration.indexOf(VBKeywords.VB_Structure)) {
                        metaClassKeyword = VBKeywords.VB_Structure;    
		} else {
			throw new RuntimeException("can't find Class or Module or Interface or Structure in " + declaration);
		}
		String modifierString = declaration.substring(0, declaration.indexOf(metaClassKeyword));
		String restString = declaration.substring(declaration.indexOf(metaClassKeyword) + metaClassKeyword.length(), declaration.length()).trim();
		st = new StringTokenizer(modifierString);

		while (st.hasMoreTokens()) {
			String next = st.nextToken();
                        if (isVBAttribute(next)) {
                            VBComment comment = new VBComment("'Translator: Uses metadata: " + next, this);
                            super.constructedPreStatements.add(comment);
                        } else if (next.equals(VBKeywords.VB_Partial)) {
                            super.isPartial = true;
                        } else {
                            this.modifiers.add(next);
                            if (next.equals(VBKeywords.VB_Shadows)) {
                                super.hidesBase = true;
                            }
                        }
		}

		Debug.logn("Modifiers " + this.modifiers, this);
		st = new StringTokenizer(restString);
		name = st.nextToken();
		Debug.logn("name " + name, this);

		while (st.hasMoreTokens()) {
                    String nextToken = st.nextToken().trim();
                    //System.out.println("NT: " + nextToken);
                    if (nextToken.equals(VBKeywords.VB_Inherits)) {
			superClassname = st.nextToken();
                    } else if (nextToken.equals(VBKeywords.VB_Implements)) {
                        this.parseImplementsList(declaration);
                        break;
                        			
                    } 		
		}
		Debug.logn("superclass " + superClassname, this);
		Debug.logn("interfaces implemented " + interfacesImplemented, this);
		this.getDNType();
	}
        
        private void parseImplementsList(String declaration) {
            String implementsString = declaration.substring(declaration.indexOf(VBKeywords.VB_Implements) + VBKeywords.VB_Implements.length() + 1, declaration.length());
            //System.out.println(implementsString);
            StringTokenizer stt = new StringTokenizer(implementsString, ",");
            while(stt.hasMoreTokens()) {
                String nextInterfacename = stt.nextToken().trim();
                //System.out.println(nextInterfacename);
                interfacesImplemented.add(nextInterfacename);
            }	
        }
        
       

	public void parseFromList(List statements) {
		String firstLine = (String) statements.get(0);
		String declaration = firstLine;
		boolean keepReading = true;
		int lineNumberRead = 0;
		while (keepReading) {
			lineNumberRead++;
			String nextLine = ((String) statements.get(lineNumberRead)).trim();
			if (nextLine.startsWith(VBKeywords.VB_Inherits) || 
				nextLine.startsWith(VBKeywords.VB_Implements)) {
				declaration = declaration + " " + nextLine;
			} else if (VBComment.isComment(nextLine, this)) {
                            this.constructedPreStatements.add(VBComment.getCommentFrom(nextLine));
                        } else {
				keepReading = false;
			}
		}
		this.parseDeclaration(declaration);
		// first line is the declaration
		List bodyStatements = new ArrayList();
		for (int i = lineNumberRead; i < statements.size() -2; i++) {
			String statement = (String) statements.get(i);
			if (!"".equals(statement)) {
				bodyStatements.add(statement);
			}			
		}
                this.bodyStatementsToParse = bodyStatements;
	}

	private boolean isVariableMemberDeclaration(String nextLine, InterpretationContext context) {
		return !nextLine.trim().startsWith("'") &&
			!Util.codeContains(nextLine , VBKeywords.VB_Sub) &&
			!Util.codeContains(nextLine , VBKeywords.VB_Function) &&
			!VBEnumStatement.isEnumStatement(nextLine.trim()) &&
			!RegionStatement.isRegionBoundary(nextLine, context);		
	}

	private void parseBodyStatements(List bodyStatements, boolean thisIsInterface, InterpretationContext context) {
            Map codeToStatementMap = new HashMap();
            List comments = new ArrayList();
            for (Iterator itr = bodyStatements.iterator(); itr.hasNext();) {
            	String nextLine = (String) itr.next();
                StatementAdapter statementCreated = null;
                try {
                    if (VBComment.isComment(nextLine, context)) {
                        Comment comment = VBComment.getCommentFrom(nextLine);
                        comments.add(comment);
                    } else if (DelegateStatementImpl.isVBDelegateStatement(nextLine, context)) {
                        statementCreated = DelegateStatementImpl.createVBDelegateStatement(nextLine, context);
                        super.delegates.add(statementCreated);
                    } else if (VBSubroutineStatement.isVBSubroutineStatement(nextLine ,context)) {
                        List subStatements = VBSubroutineStatement.getStatements(nextLine, itr, thisIsInterface);
                        statementCreated = new VBSubroutineStatement(nextLine, this);
                        codeToStatementMap.put(subStatements, statementCreated);
                        this.members.add(statementCreated);
                    } else if (VBFunctionStatement.isVBFunctionStatement(nextLine, context)) {
                        statementCreated = new VBFunctionStatement(nextLine, this);
                        List funStatements = VBFunctionStatement.getStatements(nextLine, itr, thisIsInterface, (VBFunctionStatement) statementCreated );
                        codeToStatementMap.put(funStatements, statementCreated);
                        this.members.add(statementCreated);
                    } else if (VBOperatorStatement.isOperatorStatement(nextLine, context)) {
                        statementCreated = new VBOperatorStatement(nextLine, this);
                        List funStatements = VBOperatorStatement.getStatements(nextLine, itr, thisIsInterface, (VBOperatorStatement) statementCreated );
                        codeToStatementMap.put(funStatements, statementCreated);
                        this.members.add(statementCreated);
                    } else if (VBEnumStatement.isEnumStatement(nextLine.trim())) {
                        statementCreated = new VBEnumStatement(nextLine, itr, this.context);
                        this.enumerations.add(statementCreated);
                    } else if (EventStatement.isVBEventStatement(nextLine.trim(), context)) {
                        statementCreated = EventStatement.createVBEventStatement(nextLine.trim(), this.context);
                        this.events.add(statementCreated);
                    } else if (RegionStatement.isRegionBoundary(nextLine, context)) {
                        Debug.logn("Ignoring Region boundary", this);
                    } else if (PropertyStatement.isVBPropertyStatement(nextLine, context)) {
                        List pStatements = PropertyStatement.parseVBPropertyLoop(nextLine, itr);
                        statementCreated = PropertyStatement.createVBPropertyStatement(nextLine, this);
                        codeToStatementMap.put(pStatements, statementCreated);   
                        propertyStatements.add(statementCreated);
                        
                    } else { 
                        // it must be a variable member
                        //System.out.println("Must be a variable " + nextLine);
                        statementCreated =  VariableMemberDeclaration.createVBVariableMemberDeclaration(nextLine, this);
                        this.variableMembers.add(statementCreated);
                    }
            
                } catch (Throwable pe) {
                   
                   statementCreated = (StatementAdapter) Parser.handleTopLevelParseError(nextLine, pe, context);
                   this.untranslatedStatements.add(statementCreated);
               }
                
                if (statementCreated != null) {
                   statementCreated.addConstructedPreStatements(comments);
                   comments = new ArrayList();
                }
            }
                
            for (Iterator itr = codeToStatementMap.keySet().iterator(); itr.hasNext();) {
		List stmts = (List) itr.next();
                Object np = codeToStatementMap.get(stmts);
                if (np instanceof MemberStatement) {
                    MemberStatement ms = (MemberStatement) np;
                    try {
                        ms.parse(stmts); // we can do this now the member variables are ready
                    } catch (Throwable t) {
                        Parser.handleTopLevelParseError(stmts.toString(), t, context);
                        VBComment c = new VBComment("'" + stmts.toString(), context);
                        ms.addConstructedPreStatement(c);
                    }
                } else if (np instanceof PropertyStatement) {
                    PropertyStatement ps = (PropertyStatement) np;
                    try {
                        ps.parseVB(stmts); // we can do this now the member variables are ready
                    } catch (Throwable t) {
                        Parser.handleTopLevelParseError(stmts.toString(), t, context);
                        VBComment c = new VBComment("'" + stmts.toString(), context);
                        ps.addConstructedPreStatement(c);
                    }
                }
            }
	}

	public List getStatements(String firstLine, Iterator itr) {
		List statements = new ArrayList();
		statements.add(firstLine);
		String next = firstLine;
		while (!isEnd(next) && itr.hasNext()) {
			next = (String) itr.next();
			//System.out.println("Add " + next);
			statements.add(next);
		}
		statements.add(next); // add the end decl
		return statements;
	}

	public List getVariables() {
		List variables = super.getVariables();
		DNVariable myBase = DNVariable.createVBVariable(VBKeywords.VB_MyBase, this.getDNType().getSuperClass().getName());
		variables.add(myBase);
                //System.out.println("My Variables are " + variables);
		return variables;
	}



}

 