
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
package com.sun.dn.parser.statement;

import java.util.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.parser.*;
import com.sun.dn.*;
import com.sun.dn.util.*;

	/** Abstract implementation class for .NET statemnts. 
         *  This could be, for example,
	**  a class declaration,
	** or a return statement.
	** @author danny.coward@sun.com
	*/

public abstract class StatementAdapter implements Statement {
	private String code;
	protected InterpretationContext context;
	protected List constructedPreStatements = new ArrayList();
        
        
        protected StatementAdapter(String code, InterpretationContext context) {
            this.setCode(code);
            this.context = context;
        }
        

        
        protected void setCode(String code) {
            this.code = code;
        }

	protected List tryGetJava() {
		throw new RuntimeException("Subclass may implement this.");
	}

	public List getConstructedPreStatements() {
		return this.constructedPreStatements;
	}
        
        public void addConstructedPreStatements(List comments ) {
            this.constructedPreStatements.addAll(comments);
        }
        
        public void addConstructedPreStatement(Statement s) {
            this.constructedPreStatements.add(s);
        }

	protected void addConstructedPreStatementsFrom(Expression e) {
		if (e instanceof HasConstructedStatements) {
			constructedPreStatements.addAll(((HasConstructedStatements) e).getConstructedPreStatements());
		}
	}
        
        public static boolean isVBAttribute(String code) {
            return code.startsWith("<") && code.endsWith(">");
        }
        
        public static boolean isCSAttribute(String s) {
           
            boolean b = s.trim().startsWith("[") && s.trim().endsWith("]");
           
            return b;
        }
        
        public static String getAttributeString(String code) {
            return code.substring(1, code.length()-1);
        }

	public String getOriginalCode() {
		if (this.code == null) {
			throw new RuntimeException("Development Exception: The 'code' variable has not been used in this class: " + this.getClass());
		} else {
			return this.code;
		}

	}

	public List getJava() {
		List java = null;
		try {
			java = this.tryGetJava();
		} catch (Throwable tre) {
			ParseTree tree = ParseTree.getParseTree(context);
                        if (tre instanceof TypeResolveException) {
                            ((TypeResolveException) tre).setContainingStatement(this.getOriginalCode());
                        }
			ParseTree.handleTypeResolveException(tree, this.getOriginalCode(), tre, false); // might exit here

			java = new ArrayList();
			java.add( UntranslatedStatement.getJavaFor(Util.replaceString(this.getOriginalCode(), "\n", " ")) );
			return java;

		}
		return java;
	}
	
}

 