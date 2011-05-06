
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

import com.sun.dn.parser.*;
import com.sun.dn.util.*;


public class OfExpression  extends ExpressionAdapter {
    private Expression typeExpression;
    
    private OfExpression(String code, InterpretationContext context) {
        super(code, context);
    }
    
    public static boolean isVBOfExpression(String code, InterpretationContext context) {
        if (code.trim().startsWith(VBKeywords.VB_Of) && Util.tokenizeIgnoringEnclosers(code, VBKeywords.VB_Of).size() > 1) {
            return true;
        } else {
            return false;
        }
    }
    
    public static OfExpression createVBOfExpression(String code, InterpretationContext context) {
        OfExpression oe = new OfExpression(code, context);
        String typeString = code.trim().substring(VBKeywords.VB_Of.length(), code.trim().length()).trim();
        oe.typeExpression = (new VBExpressionFactory()).getExpression(typeString, context);
        return oe;
    }
    
    public DNType getDNType() {
        return null;
    }
    
    public String getTypeName() {
        return null;
    }
    
    public String tryAsJava() {
        return "";
    }
    
}

 