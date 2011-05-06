
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

import com.sun.dn.parser.*;
import com.sun.dn.util.*;


	/**
	Option statements establish ground rules for subsequent code, 
	helping prevent syntax and logic errors. The Option Explicit statement 
	ensures that all variables are declared and spelled correctly, which cuts 
	back on time spent debugging later. The Option Strict statement helps 
	prevent logic errors and data loss that can occur when you work between 
	variables of different types. The Option Compare statement specifies the 
	way strings are compared to each other, either by their Binary or Text 
	arrangement.
	@author danny.coward@sun.com
	*/


public class VBOptionStatement extends StatementAdapter  {
	private String statement;

	public static boolean isOptionStatement(String statement) {
		return Util.codeContains(statement, VBKeywords.VB_Option);
	}

	public VBOptionStatement(String code, InterpretationContext context) {
            super(code, context);
            this.parse(code);
	}

	private void parse(String code) {
		this.statement = code.trim();
	}

	public String asComment() {
		return "VB: " + this.statement;
	}

}

 