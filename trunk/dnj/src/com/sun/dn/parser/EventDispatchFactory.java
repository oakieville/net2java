
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
import com.sun.dn.Library;

    /** This factory creates EventDispatch objects suitable for a given language
     * and translation library.
     * @author danny.coward@sun.com
     */

public class EventDispatchFactory {
    private static JavaMethod gjm;

	public static EventDispatch createEventDispatch(String language, Library library) {
		if (language.equals(Interpreter.VB_LANGUAGE)) {
			return new VBEventDispatch(library);
		} else if (language.equals(Interpreter.CS_LANGUAGE)) {
			return new CSEventDispatch(library);
		} else {
			throw new RuntimeException("Language " + language + " not supported.");
		}
	}
        
        private static JavaMethod getGenericAddRemoveListenerMethodFor(Library library, String jEventType, String jEventListenersVarName, String addRemoveMN, String javaAddRemove) {
            List modifiers = new ArrayList();
            modifiers.add(JavaKeywords.J_PUBLIC);
            List args = new ArrayList();
            JavaVariable jv = new JavaVariable("event", jEventType);
            args.add(jv);
            JavaMethod jm =  new JavaMethod(modifiers,
                addRemoveMN, 
                JavaKeywords.J_VOID, 
                args, 
                library);
            jm.addCodeLine("this." + jEventListenersVarName + "." + javaAddRemove + "(event);");
            return jm;
        }
        
        public static JavaMethod getGenericAddListenerMethodFor(Library library, String jEventType, String jEventListenersVarName) {
            return getGenericAddRemoveListenerMethodFor(library,
                    jEventType,
                    jEventListenersVarName,
                    EventDispatchFactory.getGenericAddListenerMethodname(),
                    "add");
        }
        
        public static JavaMethod getGenericRemoveListenerMethodFor(Library library, String jEventType, String jEventListenersVarName) {
            return getGenericAddRemoveListenerMethodFor(library,
                    jEventType,
                    jEventListenersVarName,
                    EventDispatchFactory.getGenericRemoveListenerMethodname(),
                    "remove");
        }
        
        public static String getGenericAddListenerMethodname() {
            return "addEventListener";
        }
	
        public static String getGenericRemoveListenerMethodname() {
            return "removeEventListener";
        }

}

 