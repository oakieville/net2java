
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
package com.sun.dn;

import com.sun.dn.parser.*;
import com.sun.dn.java.*;
import java.util.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.util.*;
import com.sun.dn.java.*;
import com.sun.dn.Library;

	/** The Web Translator holds all the behaviors needed to translate
	** an ASP application into a WebProgram.
	** @author danny.coward@sun.com
	**/

public class WebTranslator {
	public static String INT_CONTEXT_METHOD_NAME = "initJavaContext";
	
	public static void addJavaComponentInitialisation(JavaClass jc, Library library) {

		List args = new ArrayList();
		args.add(new JavaVariable("component", "javax.faces.component.UIComponent"));

		JavaMethod initMethod = new JavaMethod(new ArrayList(), 
						INT_CONTEXT_METHOD_NAME, 
						null, 
						args, 
						library);

		List vars = jc.getMemberVariables();
		for (Iterator itr = vars.iterator(); itr.hasNext();) {
			JavaVariable jv = (JavaVariable) itr.next();
			if (jv.getType().startsWith("javax.faces.component")) {
				addInitFor(jv.getName(), jv.getType(), initMethod);
			}
			
		}
		String pageLoadString = "this.Page_Load(null, null);";
		initMethod.addCodeLine(pageLoadString);

		//addInitFor("TextBox1", "javax.faces.component.html.HtmlInputText", initMethod);

		jc.addMethod(initMethod);
	}

	private static void addInitFor(String componentName, String componentType, JavaMethod initMethod) {
		String s = "if (" + componentName + " == null) {";
		initMethod.addCodeLine(s);
		s = "	"+componentName+" = ("+componentType+") component.findComponent(\""+componentName+"\");";
		initMethod.addCodeLine(s);
		s = "System.out.println(\""+componentName+"= \" + " + componentName + ");";
		initMethod.addCodeLine(s);
		s = "}";
		initMethod.addCodeLine(s); 
	}

}
 