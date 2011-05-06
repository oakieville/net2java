
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
import com.sun.dn.util.*;
import java.io.*;

    /** Data structure holding all the information
     * about a translation attempt on a .NET program.
     *@author danny.coward@sun.com
     */

public class TranslationReport {
        private List typeResolveExceptions = new ArrayList();
        private List translationWarnings = new ArrayList();
	private File reportFile = new File("vb-translation-errors.txt");
        private boolean warningsSignificant = true;
        private int reportLimit = 100;

	public boolean hasTypeResolveErrors() {
		return !(typeResolveExceptions.isEmpty());
	}
        
        public List getTypeResolveExceptions() {
            return typeResolveExceptions;
        }
        
        public void addTypeResolveException(TypeResolveException tre) {
            this.typeResolveExceptions.add(tre);
        }
        
        public void addTranslationWarning(TranslationWarning warning) {
            this.translationWarnings.add(warning);
        }
        
        public boolean hasTranslationWarnings() {
            return this.translationWarnings.size() > 0;
        }
        
        public List getTranslationWarnings() {
            return this.translationWarnings;
        }

	public void doReport(List tres, List warns) {
		try {	
			this.doReport(tres, warns, System.out);
			//FileOutputStream fos = new FileOutputStream(reportFile);
			//this.doReport(tres, warns, fos);
			//fos.close();
			//System.out.println("Errors written to " + reportFile);
		} catch (IOException ioe) {
			//System.out.println("Failure writing to error file: " + reportFile);
			ioe.printStackTrace();
			//throw new Stop(this.getClass());
		}
	}

	public void doReport(List tres, List warns, OutputStream os) throws IOException {
		String s = "There are " + tres.size() + " resolution errors and " + translationWarnings.size() + " warnings. Code snippets prefixed by .NET:\n\n";
		os.write(s.getBytes());
                int numberWritten = 0;
		for (Iterator itr = tres.iterator(); itr.hasNext();) {
			TypeResolveException tre = (TypeResolveException) itr.next();
			s = " .NET [ " + Util.toSingleLine(tre.getContainingStatement()) + " ]\n";
			os.write(s.getBytes());
			s = "  The reason was: " + tre.getMessage() + " @ [ " + tre.getCode() + " ]\n";
			os.write(s.getBytes());
                        numberWritten++;
                        if (numberWritten > this.reportLimit) {
                            os.write(("There are more errors, I'm just giving you the first " + reportLimit + ".\n").getBytes());
                            break;
                        }
                        
		}
                if (tres.size() > 0) {
                    s = "\n\n";
                    os.write(s.getBytes());
                }
                numberWritten = 0;
                for (Iterator itr = warns.iterator(); itr.hasNext();) {
                    TranslationWarning trw = (TranslationWarning) itr.next();
                    s = " .NET [ " + Util.toSingleLine(trw.getCode()) + " ]\n";
                    os.write(s.getBytes());
                    s = "\nWarning: " + trw.getMessage() + "\n";
                    os.write(s.getBytes());
                    numberWritten++;
                        if (numberWritten > this.reportLimit) {
                            os.write(("There are more warnings, I'm just giving you the first " + reportLimit + ".\n").getBytes());
                            break;
                        }
                }
		s = "\n(" +  tres.size()+ " errors and " + translationWarnings.size() + " warnings.)\n\n";
		os.write(s.getBytes());
	}


}
 