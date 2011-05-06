
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
package com.sun.dn.java;

import java.util.*;
import com.sun.dn.parser.*;
import com.sun.dn.*;
import com.sun.dn.util.*;
import com.sun.dn.library.LibraryData;

	/** Parser created class for wiring up event generators to
	** event listeners.
	@author danny.coward@sun.com
	*/

public abstract class EventSupport extends JavaClass {
	protected JavaClass targetType;		// e.g. Form1
	protected Signature methodSig;		// Button1_Click
	protected String senderJavaType;	// JButton
	protected DNVariable vbSender;
	protected Library library;
	protected DNEvent vbEvent ;
	protected static String SENDER = "sender";
	protected static String TARGET = "target";

	public EventSupport(DNVariable vbSender, 
				DNEvent vbEvent , 
				String listenerInterfaceName, 
				JavaClass targetType, 
				Signature methodSig, 
				Library library) {
		super(targetType.getName() + "_" + Util.replaceString(vbSender.getName(), ".", "_") + "_" + methodSig.getName() + "_Support", 
			new ArrayList(),
			null,
			new ArrayList(),
			false);
		super.addModifier(JavaKeywords.J_PUBLIC);		
		super.interfacesImplemented.add(listenerInterfaceName);
		this.targetType = targetType;
		this.methodSig = methodSig;
		this.vbSender = vbSender;
		this.library = library;
		this.vbEvent = vbEvent;

		// now the event callback methods
		List listenerInvokeMethods = JavaEvents.getJavaMethodsFor(listenerInterfaceName, library);
		String methodName = library.getJavaListenMethodNameForDNEvent(vbSender.getType(), vbEvent);
                boolean matched = false;
                
		for (Iterator itr = listenerInvokeMethods.iterator(); itr.hasNext();) { 
			JavaMethod jm = (JavaMethod) itr.next();
			if (jm.getName().equals(methodName)) {
				this.implementMethod(jm);
				matched = true;
			}
			super.addMethod(jm);	
		}
		if (!matched) {
			throw new RuntimeException("Error matching listen method (" + methodName + ") on interface (" + listenerInterfaceName + ") for the event " + vbEvent.getName() + " on " + vbSender.getType());
		}
	}

	public abstract void implementMethod(JavaMethod jm);

	public String getEventJavaType() {
		return this.library.getJavaTypeFor(vbEvent.getDNType().getName());
	}

	public DNVariable getVBSender() {
		return this.vbSender;
	}

	public String getSenderJavaType() {
		return library.getJavaTypeFor(vbSender.getType());
	}

}
 