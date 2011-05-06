
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
package com.sun.dn.container.gui;

import java.util.*;
import com.sun.dn.parser.*;
import com.sun.dn.*;
import com.sun.dn.java.*;
import com.sun.dn.util.*;
import com.sun.dn.Library;

	/** Specialised class for wiring up .NET gui events.
	** @author danny.coward@sun.com
	*/

public class GuiEventSupport extends EventSupport {

	public String toString() {
		return "GuiEventSupport for snd: " + vbSender + " evnt: " + vbEvent;
	}

	// signtaure is the sig of the method to be invoked when the event is sent
      

	public GuiEventSupport (DNVariable vbSender, 
					DNEvent vbEvent , 
					String listenerInterfaceName, 
					JavaClass targetType, 
					Signature methodSig, 
					Library library) {
		super(vbSender, vbEvent, listenerInterfaceName, targetType, methodSig, library);
		
		// now add the constructor
		super.addMethod(this.createArgConstructor());

		// now the instance variables
		this.addInstanceVariables();
	}

	public void implementMethod(JavaMethod jm) {
		String s = "//auto generated";
		jm.addCodeLine(s);
		//s = "System.out.println(\" event handler \");";
		//jm.addCodeLine(s);
		//s = "System.out.println(\"Event Handler calling: "+methodSig.getName()+"\");";
		//jm.addCodeLine(s);
		s = TARGET + "." + methodSig.getName() + "(" + SENDER + 
			", new " + getEventJavaType() + "(" + JavaEvents.EVENT_PARAM_NAME + "));";
		jm.addCodeLine(s);
	}

	private Constructor createArgConstructor() {
		List args = new ArrayList();
		JavaVariable jv = new JavaVariable(TARGET, targetType.getName());
		args.add(jv);
		jv = new JavaVariable(SENDER, getSenderJavaType());
		args.add(jv);
		Constructor c =  new Constructor(new ArrayList(), this, args, library);
		c.addCodeLine("this." + SENDER + " = " + SENDER + ";");
		c.addCodeLine("this." + TARGET + " = " + TARGET + ";");
		return c;
	}

	private void addInstanceVariables() {
		this.addMemberVariable(new JavaVariable(TARGET, targetType.getName()));
		this.addMemberVariable(new JavaVariable(SENDER, getSenderJavaType()));
	}

	public String getHookupCode() { // sender.addListenerMethod( new action class );
		String jSender = "";
		if (vbSender.getName().equals(VBKeywords.VB_MyBase)) {
			jSender = JavaKeywords.J_THIS;
		} else {
			jSender = vbSender.getName();
		}
		String s = jSender + ".";
		String methodName = library.getJavaAddListenerMethodNameFor(vbSender.getType(), vbEvent);
		s = s + methodName + "(new " + this.getName() + "(this, " + jSender + "))";
		return s;
	}






}
 