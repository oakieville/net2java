
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

public class VBAddRemoveHandlerStatement extends StatementAdapter { 
	private String type;
        private AddressOfExpression aoe;
        private String eventType;
        private Expression expressionNeedingAddOrRemove;

	protected List tryGetJava() {
            List l = new ArrayList();
            String s = expressionNeedingAddOrRemove.asJava();
            if (type.equals(VBKeywords.VB_AddHandler)) {
                s = s + "." + EventDispatchFactory.getGenericAddListenerMethodname();
            } else {
                s = s + "." + EventDispatchFactory.getGenericRemoveListenerMethodname();
            }
            s = s + "(new " + EventStatement.getVBEventDelegateTypeName(eventType);
            s = s + "(this, \"" + this.aoe.getMethodName() + "\") );";
            
            l.add(s);
            return l;
	}
        

        
        private VBAddRemoveHandlerStatement(String code, InterpretationContext context) {
           super(code, context);
        }

	public static boolean isVBAddRemoveHandlerStatement(String code, InterpretationContext context) {
            return code.trim().startsWith(VBKeywords.VB_AddHandler) ||
                    code.trim().startsWith(VBKeywords.VB_RemoveHandler);
        }
        
        public static VBAddRemoveHandlerStatement createVBAddRemoveHandlerStatement(String code, InterpretationContext context) {
            VBAddRemoveHandlerStatement arhs = new VBAddRemoveHandlerStatement(code, context); 
            StringTokenizer stt = new StringTokenizer(code, ",");
            String first = stt.nextToken().trim();
            if (first.startsWith(VBKeywords.VB_AddHandler)) {
                arhs.type = VBKeywords.VB_AddHandler;
            } else {
                arhs.type = VBKeywords.VB_RemoveHandler;
            }
            String eventTargetDotType = first.substring(arhs.type.length() + 1, first.length());
            List elts = Util.tokenizeIgnoringEnclosers(eventTargetDotType, ".");
            if (elts.size() > 1) {
                String expressionString = (String) elts.get(0);
                arhs.expressionNeedingAddOrRemove = (new VBExpressionFactory()).getExpression(expressionString, context);
            } else {
                arhs.expressionNeedingAddOrRemove = new VBMeExpression("<No Code>", context);
            }
            
            arhs.eventType = (String) elts.get(elts.size()-1);
            String second = stt.nextToken().trim();
            arhs.aoe = AddressOfExpression.createAddressOfExpression(second, context);
            return arhs;
        }
        
        
}

 