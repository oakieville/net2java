
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
import com.sun.dn.parser.*;
import com.sun.dn.util.*;

/** @author danny.coward@sun.com */

public class LineLabel extends Comment {
    
    private LineLabel(String code, InterpretationContext context) {
        super(code, context);
    }
	
	public static boolean hasCSLineLabel(String code, InterpretationContext context) {
                if (CSComment.isComment(code, context)) {
                    return false;
                }
                if (code.trim().equals("")) {
                    return false;
                }
               
		List tokensBySpace = Util.tokenizeIgnoringEnclosers(code, "\n");
                String first = ((String) tokensBySpace.get(0)).trim();
                first = ((String) Util.tokenizeIgnoringEnclosers(first, " ").get(0)).trim();
                
                return first.endsWith(":");
	}
        
        public static boolean isVBLineLabel(String code, InterpretationContext context) {
            if (VBComment.isComment(code.trim(), context)) {
                return false;
            }
            ParseTree pt = ParseTree.getParseTree(context);
           
            return pt.getLineLabels().contains(code.trim());
        }
        
        public static String getRemainderAfterLabel(String code) {
            
            String c = code.substring(code.indexOf(":") + 1, code.length());
          
            return c;
        }
        
        public static LineLabel createVBLineLabel(String code, InterpretationContext context) {
           
            LineLabel ll = new LineLabel(code, context);
            ll.setComment( code.trim() );
            TranslationWarning tw = new TranslationWarning(ll.getComment(), "Line labels have no translation.");
            ParseTree.getParseTree(context).getTranslationReport().addTranslationWarning(tw);
            return ll;
        }
        
        public static LineLabel createCSLineLabel(String code, InterpretationContext context) {
            LineLabel ll = new LineLabel(code, context);
            ll.setComment(code.substring(0, code.indexOf(":")));
            TranslationWarning tw = new TranslationWarning(ll.getComment(), "Linex labels have no translation.");
            ParseTree.getParseTree(context).getTranslationReport().addTranslationWarning(tw);
            return ll;
        }
        
        protected List tryGetJava() {
		List l = new ArrayList();
		l.add("// Translator: NET Line Label: " + super.getComment());
		return l;
	}

	public String toString() {
		return "aCSLineLabel";
	}	

}

 