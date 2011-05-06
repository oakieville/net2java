
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
import com.sun.dn.*;
import com.sun.dn.parser.*;
import com.sun.dn.parser.statement.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.java.*;
import com.sun.dn.container.gui.*;
import com.sun.dn.container.web.*;
import com.sun.dn.util.*;
import com.sun.dn.Library;

class CSEventDispatch implements EventDispatch {
	private Library library;
	private Map assignmentEventSupportMap = new HashMap();

	public CSEventDispatch(Library library) {
		this.library = library;
	}

	public void makeEventHandlerMethodsAccessible(List memberStatementsToMakePublic, Map javaMethodMemberStatementMap) {
		for (Iterator mss = memberStatementsToMakePublic.iterator(); mss.hasNext();) {
			MemberStatement ms = (MemberStatement) mss.next();
			for (Iterator itrr = javaMethodMemberStatementMap.keySet().iterator(); itrr.hasNext();) {
				JavaMethod jm = (JavaMethod) itrr.next();
				if (javaMethodMemberStatementMap.get(jm).equals(ms)) {
					jm.disallowPrivate();
				}
			}				
		}
	}
        
        public void connectProgramDefinedDelegates(Map javaMethodMemberStatementMap) {
            for (Iterator itr = javaMethodMemberStatementMap.keySet().iterator(); itr.hasNext();) {
                JavaMethod jm = (JavaMethod) itr.next();
		MemberStatement ms = (MemberStatement) javaMethodMemberStatementMap.get(jm);
		for (Iterator itrr = ms.getStatements().iterator(); itrr.hasNext();) {
                    Statement nextStatement = (Statement) itrr.next();
                    try {
                        if (nextStatement instanceof Assignment) {
                            Assignment assignment = (Assignment) nextStatement;
                            if (assignment.isAddingProgramDefinedDelegate()) {
                            //System.out.println(assignment);
                                String hookupCode = "";
                                if (CSOperators.isDelegateAddOperator(assignment.getAssignmentOperator())) {
                                    hookupCode = EventDispatchFactory.getGenericAddListenerMethodname();
                                } else {
                                    hookupCode = EventDispatchFactory.getGenericRemoveListenerMethodname();
                                }
                                Expression e = assignment.getRightExpression();
                                hookupCode = hookupCode + "(" + e.asJava() + ");";
                                assignment.addJavaSupplement(hookupCode);
                            }
                        }
                    } catch (Throwable t) {
                        ParseTree pt = ParseTree.getParseTree(ms);
                        ParseTree.handleTypeResolveException(pt, "<C# event hookup code>", t, false);
                    }
                }
            }
        }
        
        

	public void connectEventSupports(JavaProgram jp, List dnEventSupports, MetaClass cms) {
		Debug.logn("Hook up " + dnEventSupports + " in " + cms, this);
		for (Iterator itr = dnEventSupports.iterator(); itr.hasNext();) {
                    try {
			GuiEventSupport ges = (GuiEventSupport ) itr.next();
			Assignment assignment = (Assignment) assignmentEventSupportMap.get(ges);
			assignment.addJavaSupplement(ges.getHookupCode());
                    } catch (Throwable t) {
                        ParseTree pt = ParseTree.getParseTree(cms);
                        ParseTree.handleTypeResolveException(pt, "<C# event hookup code>", t, false);
                
                    }
		}

		assignmentEventSupportMap = new HashMap();
	}


	public List createEventSupports(Map javaMethodMemberStatementMap, 
					JavaClass jc, 
					String projectType,
                                        String namespaceName) {
            Debug.logn("Create Event Supports for " + jc, this);
            List eventSupports = new ArrayList();
	
		// I think we have to look through every MemberStatement for Assignments that
		// are of the shape evnt += delegate
            for (Iterator itr = javaMethodMemberStatementMap.keySet().iterator(); itr.hasNext();) {
                JavaMethod jm = (JavaMethod) itr.next();
		MemberStatement ms = (MemberStatement) javaMethodMemberStatementMap.get(jm);
		for (Iterator itrr = ms.getStatements().iterator(); itrr.hasNext();) {
                    Statement nextStatement = (Statement) itrr.next();
                    try {
                        if (nextStatement instanceof Assignment) {
                            Debug.logn("Test assignment " + nextStatement, this);
                            Assignment assignment = (Assignment) nextStatement;
                            if (assignment.isAddingCSEventListener()) {
                                if (assignment.isAddingProgramDefinedDelegate()) {
                                // these get added elsewhere
                                } else {
                                
                                    EventSupport es = this.getCSEventSupportFor(assignment, jc);
                                    es.setPackageName(namespaceName);
                                    eventSupports.add(es);
                                    assignmentEventSupportMap.put(es, assignment);
                                    Debug.logn("Yeap ", this);
                               
                                }
                            } else {
                                Debug.logn("Nope ", this);

                            }
                        }
                    } catch (Throwable t) {
                        ParseTree pt = ParseTree.getParseTree(ms);
                        ParseTree.handleTypeResolveException(pt, "<C# event hookup code>", t, false);
                    }
                }
                
            }
            Debug.logn("Got " + eventSupports, this);

            return eventSupports;
	}

	private EventSupport getCSEventSupportFor(Assignment assignment, JavaClass jc) {
                
                MemberAccessExpression assignee = (MemberAccessExpression) assignment.getAssignee();
		DNEvent event = this.library.getDNEvent(assignee.getTypeName());
                
                String javaListenerInterface = library.getJavaListenerTypeFor(assignee.getExpression().getTypeName(), event);
             
                String senderType = assignee.getExpression().getTypeName();
                String senderVarName = assignee.getExpression().asJava();
                DNVariable senderAsVariable = DNVariable.createCSVariable(senderVarName, senderType);
		DelegateCreationExpression dce = (DelegateCreationExpression) assignment.getRightExpression();

		Signature invokeeMethodSignature = dce.resolveMethodName();

		GuiEventSupport es = new GuiEventSupport(senderAsVariable ,
						event,
						javaListenerInterface, 
						jc, 
						invokeeMethodSignature, 
						library);
                
		return es;
	}
        
   



}

 