
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
package com.sun.dn.parser.expression;

import com.sun.dn.library.JavaExpression;
import java.util.*;
import com.sun.dn.java.*;
import com.sun.dn.parser.*;
import com.sun.dn.*;
import com.sun.dn.util.*;
import com.sun.dn.Library;

	/**
	** A VB expression denoting the creation of a new object
	NewExpression ::=
	ObjectCreationExpression |
	ArrayCreationExpression |
	DelegateCreationExpression
	@author danny.coward@sun.com
	**/

public class ObjectCreationExpression extends NewExpression {
	public String classname;
	public List args = new ArrayList();	
        private String newKeyword;

	public ObjectCreationExpression(String dnCode, String classname, List args, String newKeyword, InterpretationContext context) {
		super(dnCode, context);
		this.args = args;
		this.classname= classname;
                this.newKeyword = newKeyword;
	}

	public List getArgs() {
		return this.args;
	}
        
        public String getNewKeyword() {
            return this.newKeyword;
        }

	public DNType getDNType() {
            DNType c = this.getLibrary().getProgramDefinedOrLibraryDNTypeFor(classname);
            return c;
	}

	public String getTypeName() {
		return this.getDNType().getName();
	}

	private Library getLibrary() {
            return context.getLibrary();
	}

	public boolean matchesSignature(Signature signature) {
            return signature.matchesSignature(this.newKeyword, this.args, this.getLibrary());
	}
        
        private String getJavaForThisClassName() {
            return  JavaKeywords.J_NEW + " " + this.getDNType().getName() + "(" + writeArgs(this.args) + ")";
        }

	public String tryAsJava() {

            Debug.logn("write Java for " + this, this);
            ParseTree pt = ParseTree.getParseTree(this.context);
            // 1) Every class has a no arg constructor!
            if (pt.findMetaClassFor(this.getDNType()) != null && this.getArgs().isEmpty()) {
		return this.getJavaForThisClassName();
            }
           
            
            Signature sig = pt.resolveMethod(this.getDNType(), this.newKeyword, this.getArgs());
            if (sig != null) {
                Debug.logn("program defined constructor method found " + this, this);
                
		return this.getJavaForThisClassName();
            }
            
            JavaExpression je = this.getLibrary().getTranslationForNew(this);
            if (je != null) {
                Debug.logn("library defined constructor method found " + je, this);
                JavaExpressionMatcher jem = new JavaExpressionMatcher(je, this.getLibrary());
		String s =  jem.getJavaForNew(this);
                Debug.logn("Add imports " + je.getImportStrings(), this);
		this.context.getMetaClass().addJavaImports(je.getImportStrings());
		return s;
            } else {
                Debug.logn("in the twilight zone here - best to try something" + this, this);
		return this.getJavaForThisClassName();
            }
	}

	public String toString() {
		return "ObjectCreation cl=" + classname + " args=" + args;
	}

}
 