
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

import com.sun.dn.parser.*;
import com.sun.dn.parser.expression.*;
    /** @author danny.coward@sun.com */

public class OperatorStatementHelper {
    
   public static boolean matchesBinaryOperatorExpression(OperatorStatement os, BinaryOperatorExpression boe, InterpretationContext context) {
            
            if (os.getName().equals(boe.getOperator()) && os.isBinary()) {
                // types now have to match
                DNVariable leftArg = (DNVariable) os.getArgs().get(0);
                DNVariable rightArg = (DNVariable) os.getArgs().get(1);
                DNType leftArgT = context.getLibrary().getProgramDefinedOrLibraryDNTypeFor(leftArg.getType());
                DNType rightArgT = context.getLibrary().getProgramDefinedOrLibraryDNTypeFor(rightArg.getType());
                
                if (leftArgT.isEqualOrIsSuperType(boe.getLeftExpression().getDNType()) &&
                        rightArgT.isEqualOrIsSuperType(boe.getRightExpression().getDNType())) {
                    return true;
                }
                
            }
            return false;
        }
    
}

 