
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
import com.sun.dn.parser.expression.*;
import com.sun.dn.util.*;
import com.sun.dn.java.*;
import com.sun.dn.*;
import java.util.*;

        /** Jump statements give explicit direction as to where
         * next to evaluate statements in running code. For example, 
         * 'break' is a C# jump statement.
         * @author danny.coward@sun.com
         */

public class UnsafeBlock extends StatementAdapter  {
      
        private CodeBlock codeBlock;
       
	
	public static boolean isUnsafeBlock(String code, InterpretationContext context) {
            if ( code.trim().startsWith(CSKeywords.CS_Unsafe)) {
                    String rest = code.trim().substring(CSKeywords.CS_Unsafe.length(), code.trim().length());
                    if (rest.trim().startsWith("{")) {
                        return true;
                    }
                } 
                return false;
	}

	
	private UnsafeBlock(String code, InterpretationContext context) {
		super(code, context);
                String rest = code.trim().substring(CSKeywords.CS_Unsafe.length(), code.trim().length()).trim();
                String codeBlockS = rest.substring(rest.indexOf("{")+1, rest.length()-1).trim();
                
                ParseTree pt = ParseTree.getParseTree(context);
                TranslationWarning warning = new TranslationWarning(code, "Unsafe blocks cannot be translated correctly.");
                pt.getTranslationReport().addTranslationWarning(warning);
                this.codeBlock = new CodeBlock(super.context);
                List stmnts = CSMetaClass.tokenizeToClassStatements(codeBlockS);
                this.codeBlock.parseCS(stmnts);
	}

	public static UnsafeBlock createUnsafeBlock(String code, InterpretationContext context) {
		return new UnsafeBlock(code, context);
	}

	public List tryGetJava() {
		List l = new ArrayList();
                l.add("// Translator: a C# 'unsafe' block: start block ");
		List codeBlockJava = this.codeBlock.getJava();
                l.addAll(codeBlockJava);
                l.add("// Translator: a C# 'unsafe' block: end block ");
                
		return l;
	}
        
       

}

 