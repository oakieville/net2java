
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
import com.sun.dn.java.*;
import com.sun.dn.container.gui.*;
import com.sun.dn.container.web.*;
import com.sun.dn.util.*;
import com.sun.dn.Library;

class VBEventDispatch implements EventDispatch {
	private Library library;

	public VBEventDispatch(Library library) {
		this.library = library;
	}

	public void makeEventHandlerMethodsAccessible(List eventHandlerMemberStatements, Map javaMethodMemberStatementMap) {
	}
        
        public void connectProgramDefinedDelegates(Map javaMethodMemberStatementMap) {
            //Debug.stop(this);
        }

	public void connectEventSupports(JavaProgram jp, List DNEventSupports, MetaClass cms) {
		if (jp.getType().equals(JavaProgram.GUI_TYPE)) {
			Debug.logn(" Translate event handlers ", this);
			this.doVBGuiHookupEventHandlers(DNEventSupports, cms);
			Debug.logn(" done event handlers ", this);
		} else {
			//System.out.println("Translator has not hooked up GUI event listeners for this program type " + jp.getType());
			//System.out.println("This is normal for web programs");
		}
	}

	public List createEventSupports(Map javaMethodMemberStatementMap, JavaClass jc, 
					String projectType, String namespaceName) {
            List dnes = new ArrayList();
            for (Iterator itrr = javaMethodMemberStatementMap.keySet().iterator(); itrr.hasNext();) {
		JavaMethod jm = (JavaMethod) itrr.next();
		MemberStatement memS = (MemberStatement) javaMethodMemberStatementMap.get(jm);
		if (memS instanceof VBSubroutineStatement && 
                    ((VBSubroutineStatement) memS).isEventHandler()) {
                    jm.disallowPrivate();
                    try {
                        List thisMembersEventSupports = this.getDNEventSupports( (VBSubroutineStatement) memS, jc, projectType, namespaceName);
                        dnes.addAll(thisMembersEventSupports); // for later
                    } catch (Throwable t) {
                        ParseTree pt = ParseTree.getParseTree(memS);
                        ParseTree.handleTypeResolveException(pt, "<VB event hookup code>", t, false);
                    }
		}
            }
            return dnes;
	}


	private List getDNEventSupports(VBSubroutineStatement ss, JavaClass targetType, String projectType, String namespaceName) {
            List l = new ArrayList();
            VBHandlesClause hc = ss.getHandlesClause();
		//System.out.println("Translator getEventSupports()");
		//System.out.println(ss.getName());
		//System.out.println(hc.getVariables());

            for (Iterator itr = hc.getVariables().iterator(); itr.hasNext();) {
		DNVariable vbVariable = (DNVariable) itr.next();
		
		DNEvent dnEvent = hc.getEventFor(vbVariable);
		String javaListenerInterface = library.getJavaListenerTypeFor(vbVariable.getType(), dnEvent);
		Signature methodSig = ss.getSignature();
		EventSupport es = null;
		if (projectType.equals(JavaProgram.GUI_TYPE)) {
                    es = new GuiEventSupport(vbVariable, 
                                            dnEvent,
                                            javaListenerInterface, 
                                            targetType, 
                                            methodSig, 
                                            library);
		} else if (projectType.equals(JavaProgram.WEB_TYPE)) {
				es = new WebEventSupport(vbVariable, 
					dnEvent,
					javaListenerInterface, 
					targetType, 
					methodSig, 
					library);
		} else {
                    
                    //throw new RuntimeException("unknown project type " + projectType);
                }
                    es.setPackageName(namespaceName);
                    l.add(es);
		}
		//System.out.println(l);
		return l;
	}




	private void doVBGuiHookupEventHandlers(List eventSupports, MetaClass cms) {
            for (Iterator itr = eventSupports.iterator(); itr.hasNext();) {
		GuiEventSupport ev = (GuiEventSupport) itr.next();
		Debug.logn("  Translate event support " + ev, this);
//System.out.println("  Translate event support " + ev);
		DNVariable v = ev.getVBSender();
					
		// here is the check for whether this is a form hookup
		// is the variable class a (sub)type of the MetaClass ??
		if (! library.getDNType(v.getType()).isEqualOrIsSuperType(cms.getDNType() )) { 
                    List assignments = cms.getAllAssignmentsOf(v);
                    Debug.logn("  got assigments " + assignments.size(), this);
                    boolean hookedUp = false;
                    for (Iterator itrr = assignments.iterator(); itrr.hasNext();) {
                        Assignment a = (Assignment) itrr.next();
			a.addJavaSupplement(ev.getHookupCode());
			hookedUp = true;
                    }
                    if (!hookedUp) {
			System.out.println("WARNING: Hookup point for " + v + " not found. (Translator)");
                    }
		} else { // form events are handled differently
			// find the form constructor
			// insert it into the implementation
			//System.out.println("Here ?");
                    VBSubroutineStatement methodForInsert = (VBSubroutineStatement) cms.getConstructor();
				
                    if (methodForInsert == null) {
                        //methodForInsert = VBSubroutineStatement.createConstructor(cms);
                        //cms.addMember(methodForInsert);
                        methodForInsert = (VBSubroutineStatement) cms.findMemberStatementsByName(JavaGui.INITIALIZE_METHOD_NAME).get(0);
                         if (methodForInsert == null) {
                            throw new RuntimeException("Form has no constructor (says the Translator)");
                         }
                    }
                    
                    methodForInsert.addJavaSupplement(ev.getHookupCode());
                    //throw new Stop(this.getClass());
		}
		Debug.logn("  Translated event support ", this);

            }
	}
        
        

}

 