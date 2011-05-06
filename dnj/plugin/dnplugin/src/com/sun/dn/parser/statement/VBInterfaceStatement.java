
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
import com.sun.dn.parser.InterpretationContext;
import com.sun.dn.parser.VBMetaClass;
import com.sun.dn.parser.VBKeywords;
import com.sun.dn.util.*;

	/** A VB interface. Defined as  <br>
	[ <attrlist> ] [ Public | Private | Protected | Friend |  <br>
	Protected Friend ] _ [ Shadows ] Interface name  <br>
	[ Inherits interfacename[, interfacename ]]  <br>
	[ [ Default ] Property proname ] [ Function memberame ]  <br>
	[ Sub memberame ] [ Event memberame ]  <br>
	End Interface <br>
	@author danny.coward@sun.com
	*/

public class VBInterfaceStatement extends VBMetaClass {

	public static boolean isInterfaceStatement(String statement) {
		return Util.codeContains(statement , VBKeywords.VB_Interface);
	}

	public VBInterfaceStatement(String firstLine, Iterator itr, InterpretationContext context) {
		super(firstLine, context);
		super.parseFromList(this.getStatements(firstLine, itr));
	}


	protected boolean isEnd(String s) {
		return s.startsWith(VBKeywords.VB_End + " " + VBKeywords.VB_Interface);
	}
	
	public void parse(String rawCode) {
		if (true) throw new RuntimeException("shouldn't be here");
	
	}

	public String toString() {
		return "Interface declaration";
	}

}
 