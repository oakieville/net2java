
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
import com.sun.dn.*;
import com.sun.dn.parser.*;
import com.sun.dn.parser.statement.*;
import com.sun.dn.java.*;
import com.sun.dn.container.gui.*;
import com.sun.dn.container.web.*;
import com.sun.dn.util.*;

    /** EventDispatch objects exist in order to be able to
     * wire up event senders to event consumers. The understand the semantics
     * of the .NET event mechanism, and know how to produce equivalent behavior
     * for the translated Java Program.
     * @author dannyc.coward@sun.com
     */

public interface EventDispatch {
	
	public void connectEventSupports(JavaProgram jp, List vbEventSupports, MetaClass cms);
	public List createEventSupports(Map javaMethodMemberStatementMap, JavaClass jc, String projectType, String namespaceName);
	public void makeEventHandlerMethodsAccessible(List eventHandlerMemberStatements, Map javaMethodMemberStatementMap);
        public void connectProgramDefinedDelegates(Map javaMethodMemberStatementMap);
}

 