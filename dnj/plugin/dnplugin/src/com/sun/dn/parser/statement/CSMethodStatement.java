
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

	/** Class representing a C# Method member.
	** @author danny.coward@sun.com
	*/


public class CSMethodStatement extends MemberStatement {
	private String threadingModel;
	public static String STATHREAD = "[STAThread]";

	public CSMethodStatement(String code, boolean isInInterface, InterpretationContext context, List bodyStatements) {
		super(code, context);
		Debug.logn("Parse " + code, this);
                super.isInterfaceMemberOrAbstract = isInInterface;
		//String declaration = code.substring(0, code.indexOf(")")+1).trim();
                List declL = Util.tokenizeIgnoringEnclosers(code, ")");
                String declaration = ((String) declL.get(0)) + ")";

		declaration = Util.replaceString(declaration, "\n", " ");
		List l = Util.tokenizeIgnoringEnclosers(declaration, " ");
		l = Util.trimStrings(l); // dannyc  there has to be a cleaner way..however

		String signatureString = Util.toSpacedString(l);
		super.sig = Signature.parseCS(signatureString);

                super.constructedPreStatements.addAll(super.sig.getComments());
                
                if (!super.sig.isAbstract() && !super.isInterfaceMemberOrAbstract && !super.sig.isExternal()) {
                    
                    String rest = code.substring(code.indexOf("{") + 1, code.lastIndexOf("}")-1).trim();
                   
                    Debug.logn("Method Body as String = " + rest, this);
                    List restList = CSClassStatement.tokenizeToClassStatements(rest);
                    Debug.logn("Method Body as statements = " + restList, this);
                    restList = Util.trimStrings(restList );
                    bodyStatements.addAll(restList);
                }
                
                if (super.sig.isUnsafe()) {
                    Comment c = new CSComment("  Translator: This method was marked 'unsafe'", this);
                    ParseTree pt = ParseTree.getParseTree(context);
                    TranslationWarning warning = new TranslationWarning(code, "Unsafe members cannot be translated correctly.");
                    pt.getTranslationReport().addTranslationWarning(warning);
                    super.constructedPreStatements.add(c);
                }

		
		
	}
        
        public String toString() {
            return "aCSMethod " + this.sig.getName();
        }

	public void parseBody(List restList) {
		Parser.parseStatementStrings(restList, this, new CSStatementFactory());	
	}

	private void parseMethodDeclaration(List tokens) {
		
		
		if (true) throw new Stop(this.getClass());
	}

	public static boolean isMethodStatement(String code, InterpretationContext context) {
            Debug.clogn("a method statement ? " + code, CSMethodStatement.class);
		boolean is = code.indexOf("(") != -1 && !PropertyStatement.isCSPropertyStatement(code, context)
                && !VariableMemberDeclaration.isCSVariableMember(code,context);
                Debug.clogn("" + PropertyStatement.isCSPropertyStatement(code, context), CSMethodStatement.class);
                Debug.clogn("" + VariableMemberDeclaration.isCSVariableMember(code,context), CSMethodStatement.class);
             Debug.clogn(" it is ?: " + is, CSMethodStatement.class);   
             return is;
	}

	public boolean isConstructor() {
		return super.getName().equals(this.context.getMetaClass().getName());
	}


	public void parse(List stmts) {

	}


}

 