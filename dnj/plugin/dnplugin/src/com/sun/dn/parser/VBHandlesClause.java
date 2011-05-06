
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
import com.sun.dn.util.*;

	/**
 * A VBHandlesClause is a list of VB event handlers attached to a method
 * signature in order to flag its intention to handle events. Defined as: <br>
 * VBHandlesClause ::= [ Handles EventHandlesList ] <br>
 * EventHandlesList ::=  <br>
 * EventMemberSpecifier | <br>
 * EventHandlesList , EventMemberSpecifier <br>
 * EventMemberSpecifier ::= <br>
 * Identifier . Identifier <br>
 * MyBase . Identifier <br>
 * 
 * @author danny.coward@sun.com
 */

public class VBHandlesClause {
	List throwerEventList = new ArrayList();
	private InterpretationContext context;

	public VBHandlesClause(String clause, InterpretationContext context) {
		this.context = context;
		//a comma separated list of 2 things separated by a dot
		Debug.logn("Parse " + clause, this);
		StringTokenizer st = new StringTokenizer(clause, ",");
		while(st.hasMoreTokens()) {
			String nextToken = st.nextToken().trim();
			List l = Util.tokenizeIgnoringEnclosers(nextToken, ".");
			String sender = (String) l.get(0);
			String eventName = (String) l.get(1);

			DNVariable variable = context.getVariable(sender);
			if (variable == null) {
				throw new RuntimeException("Cannt find variable " + sender);
			}
			DNEvent e = this.context.getLibrary().getDNEvent(eventName);
                        
			if (e == null) {
				throw new RuntimeException("cannt find event class for " + eventName );
			}
			this.throwerEventList.add(new VariableEventPair(variable, e));
		}

	}

	public List getVariables() {
		List l = new ArrayList();
		for (Iterator itr = this.throwerEventList.iterator(); itr.hasNext();) {
			VariableEventPair next = (VariableEventPair) itr.next();
			l.add(next.variable);
		}
		return l;
	}

	public DNEvent getEventFor(DNVariable v) {
		for (Iterator itr = this.throwerEventList.iterator(); itr.hasNext();) {
			VariableEventPair next = (VariableEventPair) itr.next();
			if (next.variable.equals(v)) {
				return next.e;
			}
		}
		return null;
	}

	public String toString() {
		String s = "HandlCls: ";
		for (Iterator itr = throwerEventList.iterator(); itr.hasNext();) {
			VariableEventPair vep = (VariableEventPair) itr.next();
			s = s + vep.variable + "-" + vep.e;
			if (itr.hasNext()) {
				s = s + " ,";
			}
		}
		return s;
	}	
}

class VariableEventPair{
	DNVariable variable;
	DNEvent e;
		
	VariableEventPair(DNVariable variable, DNEvent e) {
		this.variable = variable;
		this.e = e;
	}

}


 