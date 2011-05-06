
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
		Class responsible for parsing VB source code. The output is a ParseTree.
		@author danny.coward@sun.com
	*/


public class VBParser extends Parser {

	VBParser(List pathnames, Library library, TranslationPolicy policy) {
		super(pathnames, library, policy);
	}

	List getTopLevelStatements(String code, ParseTree tree, String pathname) {
            try {
		List allStatements = getVBStatements(code);
		List tls = this.getTopLevelVBStatementsFromList(allStatements, tree);
		return tls;
            } catch (RuntimeException rte) {
                if (tree.getPolicy().isOfType(TranslationPolicy.STRICT)) {
                    ParseTree.reportRuntimeException(rte, Util.toSingleLine(code), pathname);
                } else {
                    String niceCode = Util.compactify(Util.toSingleLine(code), 0, 60);
                    Statement ut = super.handleTopLevelParseError(niceCode, rte, tree);
                    TranslationWarning tw = new TranslationWarning(niceCode, "Serious Warning: Code in " + pathname + " has not been translated");
                    tree.getTranslationReport().addTranslationWarning(tw);
                }
            }
            return new ArrayList();
	}

	public List getTopLevelVBStatementsFromList(List allStatements, InterpretationContext context) {
            List topLevelList = new ArrayList();
            //Debug.clogn("All statements " + allStatements, VBParser.class );
            List comments = new ArrayList();
            for (Iterator itr = allStatements.iterator(); itr.hasNext();) {
		String statement = (String) itr.next();
		//Debug.clogn("test next statement for module or class " + Util.compactify(statement, 0, 30), VBParser.class );
                StatementAdapter statementCreated = null;
                        
                try {
                    if (AssemblyInformation.isVBAssemblyInformation(statement)) {
                	// is we encounter any assembly info, then
			// we are going to read it into one AssemblyInformation
			// and return a list with that in it
			AssemblyInformation ai = AssemblyInformation.createVBAssemblyInformation(statement, itr); 
			topLevelList = new ArrayList(); 
			topLevelList.add(ai);
			return topLevelList;
                    } else if (VBComment.isComment(statement, null)) {
			comments.add(VBComment.getCommentFrom(statement));                
                    } else if (VBStructureStatement.isVBStructureStatement(statement)) {
                        statementCreated = new VBStructureStatement(statement, itr, context);
                        topLevelList.add(statementCreated);	
                    } else if (VBModuleStatement.isModuleStatement(statement)) {
			Debug.clogn("its a module", VBParser.class);
			statementCreated = new VBModuleStatement(statement , itr, context);
			topLevelList.add(statementCreated);	
                    } else if (VBClassStatement.isVBClassStatement(statement)) {
                        statementCreated  = new VBClassStatement(statement , itr, context);
			topLevelList.add(statementCreated);	
                    } else if (VBInterfaceStatement.isInterfaceStatement(statement)) {
			statementCreated  = new VBInterfaceStatement(statement , itr, context);
			topLevelList.add(statementCreated);	
                    } else if (VBOptionStatement.isOptionStatement(statement)) {
			statementCreated = new VBOptionStatement(statement, context);
			topLevelList.add(statementCreated);
                    } else if (VBImportsStatement.isImportsStatement(statement)) {
                        statementCreated = new VBImportsStatement(statement, context);
			topLevelList.add(statementCreated);
                    } else if (VBEnumStatement.isEnumStatement(statement)) {
			statementCreated = new VBEnumStatement(statement, itr, context);
			topLevelList.add(statementCreated);
                    } else if (DelegateStatementImpl.isVBDelegateStatement(statement, context)) {                               
                        statementCreated = DelegateStatementImpl.createVBDelegateStatement(statement, context);
                        topLevelList.add(statementCreated);
                    } else if (VBNamespaceStatement.isNamespaceStatement(statement)) {
                        statementCreated  = new VBNamespaceStatement(statement , itr, context, this);
			topLevelList.add(statementCreated);
                    } else {
				// shouldn't get here so
			throw new RuntimeException("Shouldn't get here");
                    }
                } catch (RuntimeException rte) {
                    Statement ut = super.handleTopLevelParseError(statement, rte, context);
                    topLevelList.add(ut);   
                }
                if (statementCreated != null) {
                    statementCreated.addConstructedPreStatements(comments);
                    comments = new ArrayList();
                }
            }
           
            
            return topLevelList;
	}
        
        private List mergePartialMetaClasses(List statementList) {
            for (Iterator itr = statementList.iterator(); itr.hasNext();) {
                Statement s = (Statement) itr.next();
                if (s instanceof MetaClass) {
                    MetaClass mc = (MetaClass) s;
                    if (mc.isPartial()) {
                        System.out.println(mc.getName() + " is a Partial class");
                    }
                }
                
            }
            return statementList;
        }
        
        private void putStatementIntoTree(Object o, ParseTree tree) {
            if (o instanceof MetaClass) {
                tree.addMetaClass((MetaClass ) o);
            } else if (o instanceof VBOptionStatement) {
                tree.addOptionStatement((VBOptionStatement) o);
            } else if (o instanceof ImportsStatement) {
                tree.addImportsStatement((ImportsStatement) o);
            } else if (o instanceof NamespaceStatement) {
                List allContained = ((NamespaceStatement)o).getAllNonNamespaceStatementObjects();
                for (Iterator itrr = allContained.iterator(); itrr.hasNext();) {
                    Object oo = itrr.next();
                    this.putStatementIntoTree(oo, tree);    
                }
            } else if (o instanceof VBEnumStatement) {
                tree.addEnumeration((VBEnumStatement) o);
            } else if (o instanceof AssemblyInformation) {
                tree.setAssemblyInformation((AssemblyInformation) o);
            } else if (o instanceof DelegateStatementImpl) {
                tree.addDelegate((DelegateStatementImpl) o);
            } else if (o instanceof UntranslatedStatement) {
                tree.addUntranslatedStatement((UntranslatedStatement)o);
            } else {
                throw new RuntimeException("VBParser: Unknown statement type " + o.getClass());
            }
        }


	void parseFile(String pathname, ParseTree tree) throws Exception {
            String code = Util.getString(pathname);
            Debug.logn("Start parsing statements in " + pathname, this);
            List classModuleDeclarations = this.getTopLevelStatements(code, tree, pathname);
            Debug.logn("Finished parsing statements ", this);
            for (Iterator itr = classModuleDeclarations.iterator(); itr.hasNext();) {
                Object o = itr.next();
                this.putStatementIntoTree(o, tree);
            }
	}
        
        private static String createVBLineForLineBreaks(String nextLine, StringTokenizer st) {
            String line = nextLine.trim();
            if (line.endsWith("_")) {
                if (line.length() <= 2) {
                    return "";
                }
                //System.out.println("." + line + ".");
		line = line.substring(0, line.length() -2);
		
		if (st.hasMoreTokens()) {
                    String n = st.nextToken();
		
                    line = line + " " + createVBLineForLineBreaks(n, st);
		} else {
			// this would be invalid VB syntaix to get here
                    throw new RuntimeException("Error reassembling lines after " + nextLine);
		}
			//System.out.println("New line " + line);
            }
            return line;
	}

	
	public static List getVBStatements(String codeBlock) {
            List l = new ArrayList();
            StringTokenizer st = new StringTokenizer(codeBlock, "\n");
            while (st.hasMoreElements()) { // oops watch out for Date and String literals
		String nextLine = st.nextToken();
		String s = createVBLineForLineBreaks(nextLine, st);
                if (!"".equals(s)) {
                    nextLine = s;
			
                    List lines = toLines(nextLine);			
                    Iterator itr = lines.iterator();
                    while(itr.hasNext()) {
                        String nextL = (String) itr.next();
                        if (!"".equals(nextL)) {
                            l.add(nextL);
                        }
                    }
                }
            }  
            return l;
	}
        
        private static List toLines(String codeLine) {
		// reserved characters are " and :
		//Debug.clogn("**" + codeLine.trim(), Statement.class);
		List lines = new ArrayList();
		String line = "";
		boolean inQuote = false; // for string literals
		boolean inHash = false; // for date literals
		int bracketDepth = 0;
		for (int i = 0; i < codeLine.trim().length(); i++) {
			char c = codeLine.trim().charAt(i);
			if (c == '"') {
				inQuote = !inQuote;
			}
			if (c == '#') {
				inHash = !inHash;
			}
			if (c == '<') {
				bracketDepth++;
			}
			if (c == '>') {
				bracketDepth--;
			}

			if ((c == ':' || new Character(c).toString().equals("'") ) && !inHash && !inQuote && (bracketDepth==0) && !codeLine.trim().startsWith("'") ) {
				if (c == ':') {
					//chop and don't include char
					lines.add(line);
					line = "";
				} else {
					// chop and include the comment character '
					lines.add(line);
					line = "'";
				}
			} else {
				line = line + c;
			}
		}
		lines.add(line.trim());
		//Debug.clogn("**" + lines, Statement.class);
		return lines;

	}


	
}
 