
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
package com.sun.dn.java;

import java.io.*;
import java.util.*;
import com.sun.dn.*;
import com.sun.dn.parser.DNVariable;
import com.sun.dn.library.LibraryData;

	/** Object representing a Java variable. Could be used
	** in a Java method declaration, or as an instance variable
	** on a Java class.
	** @author danny.coward@sun.com
	*/

public class DelegateHelper {
    
    
    public static String getCreationArgString(String targetJava, String methodName) {
        return targetJava + ", \"" + methodName + "\"";
    }
    
    public static Constructor getJavaConstructorFor(JavaClass jc, Library library) {
        List modifiers = new ArrayList();
        modifiers.add(JavaKeywords.J_PUBLIC);
        List args = new ArrayList();
        args.addAll(getJavaVariables());
        Constructor c =  new Constructor(modifiers, jc, args, library);
        c.addCodeLine("this.target = target;");
        c.addCodeLine("this.methodName = methodName;");
        return c;
    }
    
    public static List getJavaVariables() {
        List l = new ArrayList();
        l.add(new JavaVariable("target", "Object"));
        l.add(new JavaVariable("methodName", "String"));
        return l;
    }
    
    public static JavaMethod getInvokeMethodFor(List jArgs, String returnType, Library library) {
        List modifiers = new ArrayList();
        modifiers.add(JavaKeywords.J_PUBLIC);
    
        JavaMethod jm = new JavaMethod(modifiers, getInvokeMethodNameFor(), returnType, jArgs, library);
        jm.addCodeLine("try {");
        jm.addCodeLine("\tObject[] params = new Object[" + jArgs.size() + "];");
        for (int i = 0; i < jArgs.size(); i++) {
            JavaVariable jv = (JavaVariable) jArgs.get(i);
            String jvName = JavaPrimitives.primitiveToClass(jv.getName(), jv.getType());
            jm.addCodeLine("\tparams[" + i + "] = " + jvName + ";");
        }
        jm.addCodeLine("\t Method[] methods = target.getClass().getDeclaredMethods();");
        jm.addCodeLine("\tfor (int _i = 0; _i < methods.length; _i++) {");
        jm.addCodeLine("\t\tMethod m = methods[_i];");
        jm.addCodeLine("\t\tif (m.getName().equals(methodName)) {");
        jm.addCodeLine("\t\t\tm.invoke(target, params);");
        jm.addCodeLine("\t\t}");
        jm.addCodeLine("\t}");
        jm.addCodeLine("} catch (Throwable t) {");
        jm.addCodeLine("\tt.printStackTrace();");
        jm.addCodeLine("}");
        
        
        return jm;
    }
    
    
    
     
     
     
  
    public static String getInvokeMethodNameFor() {
        return "dnInvokeDelegate";
    }
    
}
 