
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

	/** A VB code comment.
	@author danny.coward@sun.com
	**/

public class VBComment extends Comment {
	
	public static boolean isComment(String code, InterpretationContext context) {
		return code.indexOf(VBKeywords.VB_Comment) == 0
                        || code.trim().startsWith(VBKeywords.VB_REM + " ");
        }
        
        public static boolean isCodeThenComment(String code, InterpretationContext context) {
            String ccode = code.trim();
            if (!isComment(ccode, context)) {
                return getOffendingListFrom(ccode).size() > 1;
            }
            return false;
        }  
        
        private static List getOffendingListFrom(String code) {
            List l = Util.tokenizeIgnoringEnclosers(code.trim(), "'");
            if (l.size() == 1) {
                l = Util.tokenizeIgnoringEnclosers(code.trim(), VBKeywords.VB_REM);
            }
            return l;
        }
        
        public static VBComment getCommentFrom(String code) {
            List l = getOffendingListFrom(code);
            return new VBComment("'" + l.get(1), null);
        }
        
        public static String getCodeFrom(String code) {
            List l = getOffendingListFrom(code); 
            return (String) l.get(0);
        }

	public VBComment(String code, InterpretationContext context) {
            super(code, context);
            String codeToParse = code.trim();
            if (code.indexOf(VBKeywords.VB_Comment) == 0) {
                setComment( codeToParse.substring(1, codeToParse.length()) );
            } else if (code.trim().startsWith(VBKeywords.VB_REM + " ")) {
                setComment( codeToParse.substring(4, codeToParse.length()) );
            } else {
                throw new RuntimeException("Error parsing comment");
            }
                
	}

}

 