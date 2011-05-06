
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

/** @author danny.coward@sun.com */
public class DelegateStatementImpl extends MemberStatement implements DelegateStatement {
	private DNType type;
        
        public static boolean isCSDelegateStatement(String code, InterpretationContext context) {
            List l = Util.tokenizeIgnoringEnclosers(code, " ");
            for (Iterator itr = l.iterator(); itr.hasNext();) {
                String next = ((String) itr.next()).trim();
                if (next.trim().equals(CSKeywords.CS_Delegate)) {
                    return true;
                }
            }
            return false;
        }
        
        public static boolean isVBDelegateStatement(String code, InterpretationContext context) {
            return Util.codeContains(code, VBKeywords.VB_Delegate);
        }
        
        protected DelegateStatementImpl(String code, InterpretationContext context) {
            super(code, context);
        }
        
        public List getModifiers() {
            return super.sig.getModifiers();
        }
        
        public String getNamespaceName() {
            
            NamespaceStatement ns = null;
            if (this.context instanceof NamespaceStatement) {
                ns = (NamespaceStatement) this.context;
            } else {
                ns = Util.getParentNamespace(this.context);
            }
            if (ns == null) {
                return "";
            } else {
                return ns.getFQName();
            }
        }
        
        public String getName() {
            return this.getDNType().getName();
        }
        
        public String getReturnType() {
            return super.sig.getReturnType();
        }
        
        
        public List getArgs() {
            return this.sig.getArgs();
        }
        
        public DNType getDNType() {
            return this.type;
        }
        
        public static DelegateStatementImpl createCSDelegateStatement(String code, InterpretationContext context) {
            DelegateStatementImpl ds = new DelegateStatementImpl(code, context);
            ds.sig = Signature.parseCS(code);
            String name = ds.sig.getName();
            ds.type = context.getLibrary().createProgramDefinedDNType(name, null);
            ds.type.setSuperClass(context.getLibrary().getProgramDefinedOrLibraryDNTypeFor("Object"));
            ds.type.setDelegate(true);
            return ds;
        }
        
        public static DelegateStatementImpl createVBDelegateStatement(String code, InterpretationContext context) {
            DelegateStatementImpl ds = new DelegateStatementImpl(code, context);
            ds.sig = new Signature(code);
            ds.constructedPreStatements.addAll(ds.sig.getComments());
            String name = ds.sig.getName();
            ds.type = context.getLibrary().createProgramDefinedDNType(name, null);
            ds.type.setSuperClass(context.getLibrary().getProgramDefinedOrLibraryDNTypeFor("Object"));
            ds.type.setDelegate(true);
            //Debug.stop(Object.class);
            return ds;
        }

	public void parse(List stmts) {
            
        }

	

}

 