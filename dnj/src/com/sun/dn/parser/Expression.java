
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

	/** Interface representing any .NET expression. An expression is a snippet
	** of .NET code that is embedded within another expression
	** or within a statement. For example, in Visual Basic, expressions are 
         * defined as <br>
	**
	** Expression ::= <br>
	** SimpleExpression | <br>
	** InvocationExpression | <br>
	** MemberAccessExpression | <br>
	** IndexExpression | <br>
	** NewExpression | <br>
	** CastExpression | <br>
	** OperatorExpression <br>
	** @author danny.coward@sun.com
	**/

public interface Expression {
        /** The Java String equivalent to this expression.*/
	public String asJava(); 
        /** The name of the .NET type resulting from evaluating this expression.*/
	public String getTypeName();
        /** The formal .NET type resulting from evaluating this expression.*/
	public DNType getDNType();
        /** The original code that was parsed to create this expression.*/
	public String getOriginalCode();
}
 