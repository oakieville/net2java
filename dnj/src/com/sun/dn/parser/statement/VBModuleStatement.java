
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

import java.util.*;
	
import java.util.*;
import com.sun.dn.parser.*;
import com.sun.dn.util.*;

	/** A VB Module. Defined as <br>
	[ <attrilist> ] [ Public | Friend ] Module name  <br>
	[ statements ]  <br>
	End Module <br>
	@author danny.coward@sun.com
	*/


public class VBModuleStatement extends VBMetaClass {

	public VBModuleStatement(String firstLine, Iterator itr, InterpretationContext context) {
		super(firstLine, context);
		super.parseFromList(this.getStatements(firstLine, itr));
	}

	public static boolean isModuleStatement(String statement) {
		return Util.codeContains(statement, VBKeywords.VB_Module);
	}


	public boolean isMain() {
		for (Iterator itr = members.iterator(); itr.hasNext();) {
			MemberStatement ms = (MemberStatement) itr.next();
			if (ms.isMain()) {
				return true;
			}
		}
		return false;
	}

	protected boolean isEnd(String s) {
		return s.startsWith(VBKeywords.VB_End + " " + VBKeywords.VB_Module);
	}

	public void parse(String code) {
		if (true) throw new RuntimeException("no longer called");
	}

	public String toString() {
		String s = "ModuleStatement " + name;
		return s;
	

	}
	

}

 