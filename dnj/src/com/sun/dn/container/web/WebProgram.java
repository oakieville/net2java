
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
import com.sun.dn.java.*;
import com.sun.dn.*;
import com.sun.dn.util.*;

	/** Object containing all the structural elements of an ASP
	** application. 
	* @author danny.coward@sun.com
	*/

public class WebProgram {
	private List asps;
	private JavaProgram jp;

	public WebProgram(List asps, JavaProgram jp) {
		this.asps = asps;
		this.jp = jp;
	}

	public JavaProgram getJavaProgram() {
		return this.jp;
	}

	public List getWebFormNames() {
		List l = new ArrayList();
		for (Iterator itr = this.asps.iterator(); itr.hasNext();) {
			ASP asp = (ASP) itr.next();
			String name = asp.getName();
			l.add(name.substring(0, name.length() -5)); // drop the .aspx
		}
		return l;
	}

	public List getWebForms() {
		List l = new ArrayList();
		List wfNames = this.getWebFormNames();
		for (Iterator itr = this.jp.getClasses().iterator(); itr.hasNext();) {
			JavaClass jc = (JavaClass ) itr.next();
			if (wfNames.contains(jc.getName())) {
				l.add(jc);
			}
		}
		return l;
	}

	public List getASPs() {
		return this.asps;
	}

}
 