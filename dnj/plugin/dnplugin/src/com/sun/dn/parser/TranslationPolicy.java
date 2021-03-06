
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


	/** The policy object determines the way in which the parser
	** deals with errors during interpretation of a .NET program.
	*@author danny.coward@sun.com
	*/

public class TranslationPolicy {
	/** The parser will fail to convert anything it doesn't like. */
	public static String STRICT = "strict";
	/** The parser will keep going as best it can to convert anything it doesn't like, by putting in
	placeholers for things it cannot resolve how to translate. */
	public static String GENTLE = "gentle";

	private String type;

	public TranslationPolicy(String type) {
		this.type = type;
	}

	public boolean isOfType(String type) {
		return this.type.equals(type);
	}

	public void handleTypeResolveException(TypeResolveException tre) {
		System.out.println("Error trying to resolve [ " + tre.getContainingStatement() + " ]");
		System.out.println(" Reason: " + tre.getMessage() + " code snippet: [ " + tre.getCode() + " ]");
		tre.printStackTrace();
		System.out.println("Usually this kind of failure means there is no type entry for an expression within the containing statement");
	}

	
}
 