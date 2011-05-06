
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
import com.sun.dn.util.*;
import java.util.*;

    /** Factory class for creating objects representing C# statements
     * from C# code.
     * @author danny.coward@sun.com
     */

public class CSStatementFactory implements StatementFactory {
    
        public Statement getStatement(String code, ListIterator allStatements, InterpretationContext context) {
            Debug.clogn("Get statement from ." + code + ".", CSStatementFactory.class);
         
            Statement s = getStatementPrivate(code, allStatements, context);
            Debug.clogn("Got " + s, CSStatementFactory.class);
            
            return s;
        }


	private Statement getStatementPrivate(String code, ListIterator allStatements, InterpretationContext context) {
		if (CSComment.isComment(code, context)) {
			CSComment comment = new CSComment(code, context);
			return comment;
		} else if (JumpStatement.isCSJumpStatement(code, context)) {
			return JumpStatement.createCSJumpStatement(code, context);
                } else if (ThrowExceptionStatement.isCSThrowExceptionStatement(code, context)) {
                    return ThrowExceptionStatement.createCSThrowExceptionStatement(code, context);
                } else if (ReturnStatement.isCSReturnStatement( code, context )) {
                    return ReturnStatement.createCSReturnStatement(code, context);
                } else if (UsingBlock.isCSUsingBlock( code, context )) {
                    return UsingBlock.createCSUsingBlock(code, context);
                } else if (CheckedBlock.isCheckedBlock( code, context )) {
                    return CheckedBlock.createCheckedBlock(code, context);
                } else if (UnsafeBlock.isUnsafeBlock( code, context )) {
                    return UnsafeBlock.createUnsafeBlock(code, context);
                } else if (LockStatement.isCSLockStatement( code, context )) {
                    return LockStatement.createCSLockStatement(code, context);
		} else if (TryCatchFinallyStatement.isCSTryCatchFinallyStatement(code, context)) {
			String allCode = TryCatchFinallyStatement.parseCSTryCatchFinallyStatement(code, allStatements);
			TryCatchFinallyStatement tcfs = TryCatchFinallyStatement.createCSTryCatchFinallyStatement(allCode, context);
			return tcfs;
		} else if (WhileStatement.isCSWhileStatement(code, context)) {
			WhileStatement ws = WhileStatement.createCSWhileStatement(code, context);
			return ws;
                } else if (DoStatement.isCSDoStatement(code, context)) {
                        String doCode = DoStatement.parseCSDoLoop(code, allStatements);
                        return DoStatement.createCSDoStatement(doCode, context);
		} else if (SelectStatement.isCSSelectStatement(code, context)) {
			SelectStatement ss = SelectStatement.createCSSelectStatement(code, context);
			return ss;
		} else if (LocalVariableDeclaration.isCSLocalVariableDeclaration(code, context)) {
			return LocalVariableDeclaration.createCSLocalVariableDeclaration(code, context);
		} else if (Assignment.isCSAssignment(code, context)) {
			return Assignment.createCSAssignment(code, context);
		} else if (ForNextStatement.isCSForNextStatement(code, context)) {
			ForNextStatement fns = ForNextStatement.createCSForNextStatement(code, context);
			return fns;
                } else if (ForEachNextStatement.isCSForEachNextStatement(code, context)) {
                        String allCode = ForEachNextStatement.parseCSForEachLoop(code, allStatements);
			ForEachNextStatement fens = ForEachNextStatement.createCSForEachNextStatement(allCode, context);
			return fens;
		} else if (IfThenElseStatement.isCSIfThenElseStatement(code, context)) {
			Debug.clogn("its an IF statement", CSStatementFactory.class);
			String allCode = IfThenElseStatement.parseCSIfThenElseBlock(code, allStatements);
			IfThenElseStatement ites = IfThenElseStatement.createCSIfThenElseStatement(allCode, context);
			return ites;
                } else if (DelegateInvocation.isCSDelegateInvocation(code, context)) {
			Debug.clogn("its a delegate invocation statement", CSStatementFactory.class);
			DelegateInvocation di = DelegateInvocation.createCSDelegateInvocation(code, context);
			return di;
                 } else if ( ExpressionStatement.isCSExpressionStatement(code, context)) {
                        Debug.clogn("its an expression statement", CSStatementFactory.class);
                        return ExpressionStatement.createCSExpressionStatement(code, context);
		} else if (CallExpression.isCSCallExpression(code)) {
			Debug.clogn("it a CallExpression", CSStatementFactory.class);
			return CallExpression.createCSCallExpression(code, context);
		}
		return null;

	}


}
 