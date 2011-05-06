
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
import com.sun.dn.util.*;
import java.lang.reflect.Method;
import com.sun.dn.parser.*;
import com.sun.dn.*;
import com.sun.dn.Library;

	/** Class that creates all the Java Methods on an event listener.
	* @author danny.coward@sun.com
	*/

public class JavaEvents {
	public static String EVENT_PARAM_NAME = "_event";

	public static List getJavaMethodsFor(String javaInterfaceName, Library library) {
		List lst = new ArrayList();
		try {
			Class c = Class.forName(javaInterfaceName);
			Method[] methods = c.getMethods();
			for (int i = 0; i < methods.length; i++) {
				String mName = methods[i].getName();
				Class[] mParams = methods[i].getParameterTypes();
				if (mParams.length > 1) {
					throw new RuntimeException("Too many parameters in this listener");
				}
				List mods = new ArrayList();
				mods.add(JavaKeywords.J_PUBLIC);
		
				List args = new ArrayList();
				args.add(new JavaVariable(EVENT_PARAM_NAME, mParams[0].getName() ) );

				JavaMethod jm = 
					new JavaMethod(mods, mName, JavaKeywords.J_VOID, args, library);
				lst.add(jm);
			}
		} catch (Throwable t) {
			System.out.println("Error getting methods on " + javaInterfaceName);
			t.printStackTrace();
			throw new Stop(JavaEvents.class);
		}
                
               
		return lst;
	}

}
 