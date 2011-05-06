
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
package com.sun.dn.container.web;

import java.util.*;
import com.sun.dn.parser.*;
import com.sun.dn.*;
import com.sun.dn.java.*;
import com.sun.dn.util.*;
import com.sun.dn.library.LibraryData;

	/** Specialised class for creating web event hookups.
	** @author danny.coward@sun.com
	*/

public class WebEventSupport extends EventSupport {

	public String toString() {
		return "WebEventSupport for snd: " + vbSender + " evnt: " + vbEvent;
	}

	public WebEventSupport (DNVariable vbSender, 
					DNEvent vbEvent , 
					String listenerInterfaceName, 
					JavaClass targetType, 
					Signature methodSig, 
					Library library) {
		super(vbSender, vbEvent, listenerInterfaceName,targetType, methodSig, library);

		

		// for web
		super.addMethod(this.createNoArgConstructor());
	}

	public void implementMethod(JavaMethod jm) {

		String eventArgName = ((JavaVariable) jm.getArgs().get(0)).getName();
		String s = "//auto generated";
		jm.addCodeLine(s);
		s = "System.out.println(\"Event Handler calling: "+methodSig.getName()+"\");";
		jm.addCodeLine(s);
		s = "javax.faces.component.UIComponent component = " + eventArgName  + ".getComponent();";
		jm.addCodeLine(s);

		s = targetType.getName() + " page = (" + targetType.getName() + ") com.sun.dn.library.System.Web.UI.PageSupport.getInstance(\"" + targetType.getName() + "\");";
		jm.addCodeLine(s);

		s = "page." + WebTranslator.INT_CONTEXT_METHOD_NAME + "(component);";
		jm.addCodeLine(s);
		s = "page." + methodSig.getName() + "(null, null);";
		jm.addCodeLine(s);
		//s = TARGET + "." + methodSig.getName() + "(" + SENDER + 
		//	", new " + getEventJavaType() + "(" + JavaEvents.EVENT_PARAM_NAME + "));";
		//jm.addCodeLine(s);
		s = "System.out.println(\"Event Handler called: "+methodSig.getName()+"\");";
		jm.addCodeLine(s);


	}


	
	private Constructor createNoArgConstructor() {
		List args = new ArrayList();
		Constructor c =  new Constructor(new ArrayList(), this, args, library);
		return c;
	}


}
 