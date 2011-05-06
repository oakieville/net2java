
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

	/** A C# code comment.


		comment: 
			single-line-comment
			delimited-comment 

		single-line-comment: 
			// input-characters opt
 
		input-characters: 
		input-character
		input-characters input-character 

		input-character: 
		Any Unicode character except a new-line-character 
			new-line-character: 
			Carriage return character (U+000D)
			Line feed character (U+000A)
			Line separator character (U+2028)
			Paragraph separator character (U+2029) 

			delimited-comment: 
			slash star delimited-comment-charactersopt star slash 
			delimited-comment-characters: 
			delimited-comment-character
			delimited-comment-characters delimited-comment-character 
			delimited-comment-character: 
			not-asterisk
			star not-slash 
			not-asterisk: 
			Any Unicode character except star 
			not-slash: 
			Any Unicode character except slash
			Comments do not nest. The character sequences (slash star) and (star slash) have no special meaning within a // comment, and the character sequences // and /* have no special meaning within a delimited comment.
			Comments are not processed within character and string literals.

			@author danny.coward@sun.com
	**/

public class CSComment extends Comment {
	
	public static boolean isComment(String code, InterpretationContext context) {
		return code.trim().indexOf("//") == 0 || code.trim().indexOf("/*") == 0;
	}

        
	public static String getCommentString(String code) {
            Debug.clogn("Get comment out of " + code, CSComment.class);
            
            if (code.trim().startsWith("/*")) {
                if (code.trim().indexOf("*/") != -1) {
                    String ret = code.trim().substring(0, code.trim().indexOf("*/") + 2);
                    Debug.clogn("GOT " + ret + "END GOT", CSComment.class);
                    return ret;
                } else {
                    return code.trim();
                    
                }
            } else {
                StringTokenizer str = new StringTokenizer(code, "\n");
                String ret = str.nextToken();
                Debug.clogn("GOT() " + ret, CSComment.class);
		return ret;
            }
	}
        
        public static String stripComments(String code, InterpretationContext context) {
            if (CSComment.isComment(code, context)) {
                StringTokenizer s = new StringTokenizer(code, "\n");
                String next = s.nextToken();
                String rest = Util.toString("", s, "\n");
                return stripComments(rest, context);
            } else {
                return code;
            }
        }
        
       

	public static String getRemainderAfterComment(String code) {
           
		String comment = getCommentString(code);
		String remr = code.substring(comment.length(), code.length()).trim();

		return remr ;
	}

	public CSComment(String code, InterpretationContext context) {
            super(code, context);

            String c = code.substring(2, code.length());
            c = Util.replaceString(c, "/", " ");
            super.setComment( c );
	}

	public String toString() {
		return "aCSComment " + super.getComment();
	}	

}


 