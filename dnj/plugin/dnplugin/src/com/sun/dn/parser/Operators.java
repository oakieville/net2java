
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
import com.sun.dn.parser.expression.*;

    /** Implemetors of this interface hold langiage syntax-specific information
     * about the operators in that language.
     * @author danny.coward@sun.com
     */

public interface Operators {
        
            /** Return all the comparison operators.*/
	public List getRelationalOperators();
            /** Translate the given comparison operator into Java */
	public String translateComparison(String s);
            /** Return all the operators that assign values.*/
	public List getAssignmentOperators();
            /** Translate the given assignment operator into Java. */
	public String translateAssignmentOperator(String assignmentString, String operator, String valueString);
            /** Test if this is an assignment to a delegate. */
	public boolean isDelegateAssignmentOperator(String op);
        
        public String translateLogical(String logicalOperator);
        
        public List getLogicals();
}

 