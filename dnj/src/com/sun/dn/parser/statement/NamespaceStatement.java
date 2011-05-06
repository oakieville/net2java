
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
import com.sun.dn.library.LibraryData;

	/** A .NET namespace statement. Examples <br>
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


public class NamespaceStatement extends StatementAdapter  implements InterpretationContext {
	List topLevelStatementObjects;
	String shortName;

	public String getShortName() {
		return this.shortName;
	}
       
        
        public String getFQName() {
            NamespaceStatement ns = Util.getParentNamespace(this);
            if (ns == null) {
                return this.getShortName();
            } else {
                return ns.getFQName() + "." + this.getShortName();
            } 
        }

	public List getAllNonNamespaceStatementObjects() {
		List allList = new ArrayList();
		for (Iterator itr = this.topLevelStatementObjects.iterator(); itr.hasNext();) {
			Object o = itr.next();
			if (o instanceof NamespaceStatement) {
				NamespaceStatement nso = (NamespaceStatement) o;
				allList.addAll(nso.getAllNonNamespaceStatementObjects());
			} else {
				allList.add(o);
			}
		}
		
		return allList;
	}

	protected NamespaceStatement(String code, InterpretationContext context) {
            super(code, context);
        }

	protected String getParentNamespace() {
		NamespaceStatement parentNs = Util.getParentNamespace(this);
		if (parentNs == null) {
			return "";
		} else {
			return parentNs.getShortName();
		}		
	}
	
	public DNVariable getVariable(String name) {
		for (Iterator itr = this.getVariables().iterator(); itr.hasNext();) {
			DNVariable var = (DNVariable) itr.next();
			if (var.getName().equals(name)) {
				return var;
			}
		}
		return null;
	}

	public List getVariables() {
		return this.context.getVariables();
	}

	public Library getLibrary() {
		Debug.logn("accessing library on " + this, this);
		return this.context.getLibrary();
	}

	public InterpretationContext getParent() {
		return this.context;
	}

	public MetaClass getMetaClass() {
		return this.context.getMetaClass();
	}



}

 