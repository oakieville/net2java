
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
import com.sun.dn.*;
import com.sun.dn.util.*;
import java.util.*;

         /** Factory class for creating objects representing Visual Basic statements
     * from Visual Basic code.
     * @author danny.coward@sun.com
     */

public class VBStatementFactory implements StatementFactory { 

	public Statement getStatement(String rawStatementString, ListIterator allStatements, InterpretationContext context) {
            Debug.logn("Get statement for ." + rawStatementString + ".", this);
		if ( VBComment.isComment(rawStatementString, context) ) {
			return new VBComment(rawStatementString, context);
                } else if (VBComment.isCodeThenComment(rawStatementString, context)) {
                        VBComment comment = VBComment.getCommentFrom(rawStatementString);
                        String cd = VBComment.getCodeFrom(rawStatementString);
                        StatementAdapter s = (StatementAdapter) getStatement(cd, allStatements, context);
                        s.addConstructedPreStatement(comment); 
                        return s;
                 } else if (ExitStatement.isExitStatement(rawStatementString, context)) {
                        return new ExitStatement(rawStatementString, context);
                } else if (VBAddRemoveHandlerStatement.isVBAddRemoveHandlerStatement(rawStatementString, context)) {
                        return VBAddRemoveHandlerStatement.createVBAddRemoveHandlerStatement(rawStatementString, context);
                } else if (VBRaiseEventStatement.isVBRaiseEventStatement(rawStatementString, context)) {
                        return VBRaiseEventStatement.createVBRaiseEventStatement(rawStatementString, context);        
                 } else if (OnErrorStatement.isOnErrorStatement(rawStatementString, context)) {
                        return new OnErrorStatement(rawStatementString, context);
                 } else if (UsingBlock.isVBUsingBlock(rawStatementString, context)) {
                        String usingCode = UsingBlock.getVBUsingBlockCode(rawStatementString, allStatements, context);
                        return UsingBlock.createVBUsingBlock(usingCode, context);
		} else if ( LocalVariableDeclaration.isVBDimStatement(rawStatementString, context) ) {
			return LocalVariableDeclaration.createVBLocalVariableDeclaration(rawStatementString, context);
                } else if ( ThrowExceptionStatement.isVBThrowExceptionStatement(rawStatementString, context)) {
                        return  ThrowExceptionStatement.createVBThrowExceptionStatement(rawStatementString, context);
		} else if ( ReturnStatement.isVBReturnStatement(rawStatementString, context) ) {
			return ReturnStatement.createVBReturnStatement(rawStatementString, context);
		} else if ( IfThenElseStatement.isVBIfThenElseStatement(rawStatementString, context) ) {
			String ifThenElseCode = IfThenElseStatement.parseVBIfThenElseBlock(rawStatementString, allStatements);
			return IfThenElseStatement.createVBIfThenElseStatement(ifThenElseCode, context);
		} else if ( ForEachNextStatement.isVBForEachNextStatement(rawStatementString.trim(), context) ) {
			String forEachNextCode = ForEachNextStatement.parseVBForEachLoop(rawStatementString, allStatements);
			return ForEachNextStatement.createVBForEachNextStatement(forEachNextCode, context); 
		} else if ( ForNextStatement.isVBForNextStatement(rawStatementString, context) ) {
			String forNextCode = ForNextStatement.parseVBForLoop(rawStatementString, allStatements);
			return ForNextStatement.createVBForNextStatement(forNextCode, context);
		} else if ( WhileStatement.isVBWhileStatement(rawStatementString, context) ) {
			String whileCode = WhileStatement.parseVBWhileLoop(rawStatementString, allStatements);
			return WhileStatement.createVBWhileStatement(whileCode, context);
		} else if ( DoStatement.isVBDoStatement(rawStatementString, context) ) {
			String doCode = DoStatement.parseVBDoLoop(rawStatementString, allStatements);
			return DoStatement.createVBDoStatement(doCode, context);
		} else if ( SelectStatement.isVBSelectStatement(rawStatementString, context) ) {
			String selectCode = SelectStatement.parseVBSelectLoop(rawStatementString, allStatements);
			return SelectStatement.createVBSelectStatement(selectCode, context);
		} else if ( TryCatchFinallyStatement.isVBTryCatchFinallyStatement(rawStatementString, context) ) {
			String tcfCode = TryCatchFinallyStatement.parseVBTryCatchFinallyStatement(rawStatementString, allStatements);
			return TryCatchFinallyStatement.createVBTryCatchFinallyStatement(tcfCode, context);
		} else if ( WithStatement.isWithStatement(rawStatementString, context) ) {
			String withCode = WithStatement.parseWithLoop(rawStatementString, allStatements);
			return new WithStatement(withCode, context);
		} else if ( Assignment.isVBAssignment(rawStatementString, context) ) {
			return Assignment.createVBAssignment(rawStatementString, context);
                } else if ( StopStatement.isVBStopStatement(rawStatementString, context)) {
                        return new StopStatement(rawStatementString, context);
                } else if ( EraseStatement.isVBEraseStatement(rawStatementString, context)) {
                        return new EraseStatement(rawStatementString, context);
                } else if ( JumpStatement.isVBJumpStatement(rawStatementString, context)) {
                        return JumpStatement.createVBJumpStatement(rawStatementString, context);
                } else if ( LineLabel.isVBLineLabel(rawStatementString, context)) {
                        return LineLabel.createVBLineLabel(rawStatementString, context);
                } else if ( LockStatement.isVBLockStatement(rawStatementString, context)) {
                        String lockCode = LockStatement.getVBLockStatementLoop(rawStatementString, allStatements, context);
                        return LockStatement.createVBLockStatement(lockCode, context);
                } else if ( DelegateInvocation.isVBDelegateInvocation(rawStatementString, context)) {
                        return DelegateInvocation.createVBDelegateInvocation(rawStatementString, context);
                //} else if ( ExpressionStatement.isVBExpressionStatement(rawStatementString, context)) {
                //        return ExpressionStatement.createVBExpressionStatement(rawStatementString, context);
		} else if ( CallExpression.isCallExpression(rawStatementString) ) {
			return CallExpression.createVBCallExpression(rawStatementString, context);
		} else {			
			Debug.clogn("I don't know what to do with " + rawStatementString, Statement.class);
			throw new RuntimeException("I don't know what to do with " + rawStatementString);
		}				
	}


}
 