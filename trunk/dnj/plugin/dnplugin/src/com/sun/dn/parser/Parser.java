
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
		Abstract Class responsible for parsing .NET source code. The output is a ParseTree.
		@author danny.coward@sun.com
	*/


public abstract class Parser {
	List pathnames;
	private Library library;
	private TranslationPolicy policy;

	Parser(List pathnames, Library library, TranslationPolicy policy) {
		this.pathnames = pathnames;
		this.library = library;
		this.policy = policy;
	}
        
        public static Statement handleTopLevelParseError(String statement, Throwable rte, InterpretationContext context) {
             ParseTree pt = ParseTree.getParseTree(context);
             if (pt.getPolicy().isOfType(TranslationPolicy.GENTLE)) {
                UntranslatedStatement ut = new UntranslatedStatement(statement, context);
 
                TypeResolveException tre = new TypeResolveException(statement, "Error understanding top level statement " + context);
                pt.getTranslationReport().addTypeResolveException(tre);
                return ut;
              } else {
                rte.printStackTrace();
                throw new RuntimeException("Parse Error below:");
              }
        }

	public static Parser createParser(String language, List pathnames, Library library, TranslationPolicy policy) {
		if (language.equals(Interpreter.VB_LANGUAGE)) {
			return new VBParser(pathnames, library, policy);
		} else if (language.equals(Interpreter.CS_LANGUAGE)) {
			return new CSParser(pathnames, library, policy);
		} else {
			throw new RuntimeException("Language: " + language + " not supported.");
		}
	}
        
        public static void parseStatementStrings(List rawStatementStrings, InterpretationContextWithStatements statements, StatementFactory factory) {
            //Debug.clogn("Parse " + rawStatementStrings, Statement.class);
            String rawStatementString = null;
		
            for (ListIterator itr = rawStatementStrings.listIterator(); itr.hasNext();) {
                try {
                    rawStatementString = (String) itr.next();
                    Debug.clogn("Parse " + rawStatementString, Parser.class);
                    //System.out.println("Parse " + rawStatementString);
                    long l = (new Date()).getTime();
                    if (factory instanceof VBStatementFactory) {
                        // may need to parse these out too
                    } else {
                        if (LineLabel.hasCSLineLabel(rawStatementString, statements)) {
                            statements.addStatement(LineLabel.createCSLineLabel(rawStatementString, statements));
                            rawStatementString = LineLabel.getRemainderAfterLabel(rawStatementString); 
                        }
                    }
                    if (!rawStatementString.trim().equals("")) {
                                   
                        Statement statement = factory.getStatement(rawStatementString, itr, statements);
                        if (statement != null) {
                            statements.addStatement(statement);
                        }
                    
                    }
                 } catch (Throwable th) {
                    ParseTree tree = ParseTree.getParseTree(statements);        
                    ParseTree.handleTypeResolveException(tree, rawStatementString, th, true); // might exit here

                    UntranslatedStatement us = new UntranslatedStatement(rawStatementString, statements);
                    statements.addStatement(us);			
                }
            }
            Debug.clogn("Done parsing statements", Statement.class);
	}
        
	List getTopLevelStatements(String code, InterpretationContext context) {
		if (true) throw new RuntimeException("not implemented");
		return null;
	}
	abstract void parseFile(String pathname, ParseTree tree) throws Exception;

	public ParseTree parse() throws Exception {
		//System.out.println("Making sense of " + pathname);
		ParseTree tree = new ParseTree(this.library, this.policy);
		for (Iterator itr = pathnames.iterator(); itr.hasNext();) {
			String pathname = (String) itr.next();
			Debug.logn("Parse " + pathname, this);
			this.parseFile(pathname, tree);
			Debug.logn("Done parsing " + pathname, this);
                        
		}
                tree.resolvePartialMetaClassAndNamingConflicts();
               List l = tree.getMetaClasses();
               for (Iterator itr = l.iterator(); itr.hasNext();) {
                    MetaClass mc = (MetaClass) itr.next();
                    mc.parseBody();
               }
		return tree;
	}


	
}
 