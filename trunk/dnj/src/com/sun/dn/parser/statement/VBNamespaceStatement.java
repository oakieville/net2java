
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
import com.sun.dn.*;
import com.sun.dn.parser.*;
import com.sun.dn.util.*;

	/** A VB namespace statement. Examples <br>
	Imports N1.N2 <br>
	Namespace N1.N2 <br> <br>
   
   	Class A <br>
   	End Class <br>
	End Namespace  <br>
	Namespace N3 <br> <br>
   
   	Class B <br>
      Inherits A <br>
   	End Class <br>
	End Namespace <br>

	sigh Namespace *statements* can nest. how dumb is that !! <br>
	@author danny.coward@sun.com
	**/


public class VBNamespaceStatement extends NamespaceStatement {
	
	public VBNamespaceStatement(String firstLine, Iterator itr,  InterpretationContext context, VBParser parser) {
		super(firstLine, context);
		List statements = this.getStatements(firstLine, itr);
		Debug.logn("Parse " + statements, this);
		String declaration = (String) statements.get(0);
		this.parseDeclaration(declaration);
		List innerStatements = new ArrayList();
		
		for (int i = 1; i < statements.size() -1; i++) {
			innerStatements.add(statements.get(i));
		}
		this.topLevelStatementObjects = parser.getTopLevelVBStatementsFromList(innerStatements, this);
		Debug.logn("--"+this.shortName+"---" + topLevelStatementObjects , this);

	}


	private void parseDeclaration(String declaration) {
		
		//String parentNamespace = this.getParentNamespace();
		StringTokenizer st = new StringTokenizer(declaration);
		String namespaceKw = st.nextToken();
		String lastParticle = st.nextToken();
		this.shortName = lastParticle;
		Debug.logn("my shortname is " + this.shortName , this);

	}

	private static List getStatements(String firstLine, Iterator itr) {
		List statements = new ArrayList();
		statements.add(firstLine);
		String next = firstLine;
		int nestCount = 1;
		while (itr.hasNext()) {
			next = (String) itr.next();
			//Debug.clogn("next " + next, NamespaceStatement.class);
			if (isNamespaceStatement(next)) {
				nestCount++;
			}
			if (isEnd(next)) {
				nestCount--;
			}
			statements.add(next);
			if (isEnd(next) && nestCount == 0) {
				break;
			}
		}
		return statements;
	}

	public static boolean isNamespaceStatement(String statement) {
		return statement.trim().startsWith(VBKeywords.VB_Namespace) && !isEnd(statement);
	}

	private static boolean isEnd(String statement) {
		return statement.trim().endsWith(VBKeywords.VB_End + " " + VBKeywords.VB_Namespace);
	}

	


}

 