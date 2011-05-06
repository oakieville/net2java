
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

import java.io.*;
import java.util.*;
import com.sun.dn.*;
import com.sun.dn.parser.statement.*;
import com.sun.dn.util.*;
import com.sun.dn.Library;

	/**
	* Class responsible for parsing C# source code. 
        * The output is a ParseTree.
	* @author danny.coward@sun.com
	*/


public class CSParser extends Parser {
    

	CSParser (List pathnames, Library library, TranslationPolicy policy) {
		super(pathnames, library, policy);
	}


	public List getTopLevelStatements(String code, List comments, InterpretationContext context) {
		List codeChunks = Util.tokenizeSemiColonChunksAndPanhandles(code);
                Debug.logn("done chunks", this);
		List statements = this.getTopLevelCSStatementsFromList(codeChunks, comments, context);
		return statements;
	}
        
        void parseNamespaceStatement(NamespaceStatement ns, ParseTree tree) throws Exception {
            List allContained = ns.getAllNonNamespaceStatementObjects();
            for (Iterator itrr = allContained.iterator(); itrr.hasNext();) {
                Object oo = itrr.next();
		if (oo instanceof MetaClass) {
                    tree.addMetaClass((MetaClass) oo);
                } else if (oo instanceof DelegateStatement) { 
                    tree.addDelegate((DelegateStatement) oo); 
                } else if (oo instanceof UntranslatedStatement) {
                    tree.addUntranslatedStatement((UntranslatedStatement)oo);
                } else {
                    System.out.println("CSParser: skipping " + oo);
		}
            }
        }
        
        void parseFile(String pathname, ParseTree tree) throws Exception {
            parseFile(pathname, new ArrayList(), tree);
            
        }

	void parseFile(String pathname, List floatingComments, ParseTree tree) throws Exception {
            String code = Util.getString(pathname);
            Debug.logn("Start parsing statements in " + pathname, this);
            List statements = this.getTopLevelStatements(code, floatingComments, tree);
            Debug.logn("here " + pathname, this);
            // dannyc note this is the same but different as the method on VBParser
            StatementAdapter statementCreated = null;
            List comments = new ArrayList();
            comments.addAll(floatingComments);
            for (Iterator itr = statements.iterator(); itr.hasNext();) {
                Object o = itr.next();  
                
		if (o instanceof MetaClass) {
                    statementCreated = (StatementAdapter)o;
                    tree.addMetaClass((MetaClass ) o);
		} else if (o instanceof NamespaceStatement) {
                    statementCreated = (StatementAdapter)o;
                    this.parseNamespaceStatement((NamespaceStatement)o, tree);
		} else if (o instanceof ImportsStatement) {
                    statementCreated = (StatementAdapter)o;
                    tree.addImportsStatement((ImportsStatement) o);
                } else if (o instanceof Comment) {
                    comments.add(o);
                } else if (o instanceof AssemblyInformation) {
                    tree.setAssemblyInformation((AssemblyInformation) o);
                } else if (o instanceof UntranslatedStatement) {
                    statementCreated = (StatementAdapter)o;
                    tree.addUntranslatedStatement((UntranslatedStatement)o);
                } else {
                    throw new RuntimeException("CSParser: Unknown statement type " + o);
		}
                if (statementCreated != null) {
                    statementCreated.addConstructedPreStatements(comments);
                    comments = new ArrayList();
                    statementCreated = null;
                    
                }
            }
	}
        
        private List breakOutComments(List allStatementsWithComments, InterpretationContext context) {
            List allStatements = new ArrayList();
            for (Iterator itr = allStatementsWithComments.iterator(); itr.hasNext();) {
                String nextString = (String) itr.next();
                //System.out.println("---" + nextString);
                if ( CSComment.isComment(nextString, context) ) {
                    String commentString = CSComment.getCommentString(nextString);
                    //System.out.println("---COMMENT" + commentString);
                    allStatements.add(commentString);
                    String followingOne = CSComment.getRemainderAfterComment(nextString);
                    //System.out.println("---AFTER COMMENT" + followingOne);
                    if (!followingOne.trim().equals("")) {
                        allStatements.add(followingOne);
                    }
                    
                    
                } else {
                    allStatements.add(nextString);
                }
                
                
            }
            return allStatements;
        }

	List getTopLevelCSStatementsFromList(List allStatementsWithComments, List floatingComments, InterpretationContext context) {
		List topLevelList = new ArrayList();
		//Debug.clogn("All statements " + allStatementsWithComments, CSParser.class );
                StatementAdapter statementCreated = null;
                
                List allStatements = this.breakOutComments(allStatementsWithComments, context);
                //Debug.clogn("All statements without comments " + allStatements, CSParser.class );
                List comments = new ArrayList();
                
                comments.addAll(floatingComments);
                
                
		for (Iterator itr = allStatements.iterator(); itr.hasNext();) {
			String statement = ((String) itr.next()).trim();
			//Debug.logn("Parser parse ." + statement + ".", this);
                        
                        try {
                            if (AssemblyInformation.isCSAssemblyInformation(statement)) {
				// is we encounter any assembly info, then
				// we are going to read it into one AssemblyInformation
				// and return a list with that in it
				AssemblyInformation ai = AssemblyInformation.createCSAssemblyInformation(statement, itr);
				topLevelList = new ArrayList();
				topLevelList.add(ai);
				return topLevelList;
                            } else if (CSUsingDirective.isUsingDirective(statement)) {
				statementCreated = new CSUsingDirective(statement, context);
				topLevelList.add(statementCreated);
                            } else if (CSNamespaceStatement.isNamespaceStatement(statement)) {
				statementCreated = new CSNamespaceStatement(statement, context, this);
				topLevelList.add(statementCreated);
                            } else if (CSComment.isComment(statement, context)) {
				String commentCode = CSComment.getCommentString(statement);
				String rest = CSComment.getRemainderAfterComment(statement);
                                comments.add(new CSComment(commentCode.trim(), context));
				topLevelList.addAll(this.getTopLevelStatements(rest, comments, context));
                            } else if (CSClassStatement.isCSClassStatement(statement, context)) {
				statementCreated = new CSClassStatement(statement, context);
				topLevelList.add(statementCreated);
                            } else if (CSStructStatement.isCSStructStatement(statement, context)) {
				statementCreated = new CSStructStatement(statement, context);
				topLevelList.add(statementCreated);
                            } else if (CSEnumStatement.isCSEnumStatement(statement, context)) {
				statementCreated = new CSEnumStatement(statement, context);
				topLevelList.add(statementCreated);
                            } else if (CSInterfaceStatement.isCSInterfaceStatement(statement, context)) {
				statementCreated = new CSInterfaceStatement(statement, context);
				topLevelList.add(statementCreated);
                            } else if (DelegateStatementImpl.isCSDelegateStatement(statement, context)) {
				statementCreated = DelegateStatementImpl.createCSDelegateStatement(statement, context);
				topLevelList.add(statementCreated);
                            } else {
				//topLevelList.add( new UntranslatedStatement(statement, context) );
				String errorS = null;
				if (statement.length() > 50) {
					errorS = statement.substring(0,50) + " (snip)";
				} else {
					errorS = statement;
				}
				throw new RuntimeException("I don't know what this is: ." + errorS + ".");
                            }
                            
                            //System.out.println("STATMENT " + statementCreated + " COMMENTS: " + comments);
                            if (statementCreated instanceof CSMetaClass) {
                                statementCreated.addConstructedPreStatements(comments);
                                comments = new ArrayList();
                                //System.out.println("Comments added to statement.");
                            }
                            
                        } catch (Throwable rte) {
                            
                            Statement ut = super.handleTopLevelParseError(statement, rte, context);
                            topLevelList.add(ut);   
                        }
                
                }
		return topLevelList;
	}

	
}
 