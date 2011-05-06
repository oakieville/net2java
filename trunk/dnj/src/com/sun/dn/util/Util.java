
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
package com.sun.dn.util;

import java.util.*;
import com.sun.dn.parser.statement.*;
import com.sun.dn.parser.*;
import java.awt.Component;

import java.io.*;
import org.w3c.dom.*;

	/** Contains some useful utility methods for the
	** .NET parser.
         * @author danny.coward@sun.com
	*/

public class Util {
    public static java.awt.Dimension BORDER = new java.awt.Dimension(10, 10);
   
        public static String createLegalFilename(String s) {
            return Util.replaceString(s, ">", "_");
        }
    
        public static void normaliseComponents(Vector labels) {
            int maxWidth = 0;
            for (Enumeration e = labels.elements(); e.hasMoreElements();) {
                Component c = (Component) e.nextElement();
                int i = c.getPreferredSize().width;
                if ( i > maxWidth) {
                    maxWidth = i;
                }
            }
            //System.out.println(maxWidth);
            for (Enumeration e = labels.elements(); e.hasMoreElements();) {
                Component c = (Component) e.nextElement();
                java.awt.Dimension d = new java.awt.Dimension(maxWidth+5, c.getHeight());
                
                //c.setSize(d);
                c.setMinimumSize(d);
                c.setPreferredSize(d);
            }
        }
    
        /** Does the supplied code contain the keyword. Avoids the trap
         * of returning true e.g when the keyword is 'As' and the code is
         * Dim IsAsthmatic As Boolean
         */
    
        public static boolean codeContains(String myCode, String keyword) {	
		if (myCode.startsWith(keyword + " ")) {
			return true;
		}
		if (myCode.endsWith(" " + keyword)) {
			return true;
		}
		if (myCode.equals(keyword)) {
			return true;
		}
		List l = Util.tokenizeIgnoringEnclosers(myCode, " " + keyword + " ");
		return (l.size() > 1);
	}

        /** return the string up to the first { */
	public static String getUpToOpeningCurlyBracket(String containsOpen) {
		return containsOpen.substring(0, containsOpen.indexOf("{"));
	}
        
        public static String getInsideCurlies(String code) {
            return code.substring(code.indexOf("{") + 1, code.lastIndexOf("}"));
        }

	private static String getEitherUpToFirstSCOrFirstPanhandle(String s) {
            
		Debug.clogn("getEitherUpToFirstSCOrFirstPanhandle " + compactify(s, 50, 50), Util.class);
		//System.out.println("getEitherUpToFirstSCOrFirstPanhandle " + compactify(s, 50, 50));
                
		int curlyOpen = getIndexOfIgnoringClosures(s, '{');
		int semiColon = getIndexOfIgnoringClosures(s, ';');
//Debug.clogn("done " + semiColon + " " + curlyOpen, Util.class);
		if (curlyOpen == -1 && semiColon == -1) {
			return s;
		}
		if (curlyOpen == -1) {
			return s.substring(0, semiColon);
		}
		// now we can assume that curlyOpen is not equal to zero
		if (curlyOpen != -1 && (semiColon != -1 && semiColon < curlyOpen)) {
			return s.substring(0, semiColon);
		} else {
			// this is the last case
			return Util.getUpToClosingCurlyBracket(s);
		}
	}

	private static int getIndexOfIgnoringClosures(String master, char c) {
            Debug.clogn("getIndexOfIgnoringClosures ", Util.class);
		int bracketCount = 0;
                boolean inComment = false;
		for (int i = 0; i < master.length(); i++) {
			char cc = master.charAt(i);
                        if (cc == '/' && i < master.length() -1 && master.charAt(i+1) == '*') {
                            inComment = true;
                        }
                        if (cc == '*' && i < master.length() -1 && master.charAt(i+1) == '/') {
                            inComment = false;
                        }
			if (!inComment && cc == '(') bracketCount++;
			if (!inComment && cc == ')') bracketCount--;
			if (!inComment && cc == c && bracketCount == 0) {
				return i;
			}
		}
		return -1;
	}

            /** Return a list of strings that are parsed from the given string by
             *looking for ; as separators, or things of the shape <br>
             * 'text text { lots more text including ;'s }' i.e. a panhandle
             */
	public static List tokenizeSemiColonChunksAndPanhandles(String s) {
		List l = new ArrayList();
		boolean done = false;
		String current = s;
		while (!done) {
			String nextRaw = getEitherUpToFirstSCOrFirstPanhandle(current);
			//System.out.println("current = ." + current + ".");
			//System.out.println("NEXT = ." + nextRaw + ".");
			String next = nextRaw.trim();
			next = trimRightSemiColon(next);
			if (!"".equals(next) && !"}".equals(next)) {
				//System.out.println("Adding " + next);
				l.add(next);
			} else {
				//System.out.println("nextRaw = ." + nextRaw + ". so is trivial. exiting");
				done = true;
                                //System.out.println("Return from tokenizeSemiColonChunksAndPanhandles");
                                //return l;
			}
			if (nextRaw.length() >= current.length()) {
				//System.out.println("next longer than current");
				done = true;
			} else {
				current = current.substring(nextRaw.length(), current.length());
				if (current.startsWith(";")) {
					current = current.substring(1, current.length());
				}
			}
		}
                //System.out.println("Return from tokenizeSemiColonChunksAndPanhandles");
		return l;		 
	}

            /** take of the trailing ;*/
	public static String trimRightSemiColon(String s) {
		if ("".equals(s)) {
			return s;
		} else if (s.charAt(s.length() -1) == ';') {
			return s.substring(0, s.length() -1);
		} else {
			return s;
		}
	}

           /** Return the string up to the closing }. This skips }'s that
            * are inside "", () and further {}. Supplied string is assumed to contain
            an opening {.*/
	public static String getUpToClosingCurlyBracket(String containsOpen) {
                Debug.clogn("getUpToClosingCurlyBracket " + compactify(containsOpen, 50, 50), Util.class);
                StringBuffer sbTokens = new StringBuffer();
   
		int curlyBracketDepth = 0;
		boolean inQuote = false;
		int bracketDepth= 0;
		boolean justMadeToken = false;
		boolean mayExit = false;
		String token = "";
		for (int i = 0; i < containsOpen.length(); i++) {
			char c = containsOpen.charAt(i);
			justMadeToken = false;
			if (c == '"') inQuote = !inQuote;
			if (!inQuote) {
				if (c == '(') bracketDepth++;
				if (c == ')') bracketDepth--;

				if (c == '{' && bracketDepth == 0) {
					curlyBracketDepth++;
					mayExit = true;
				}
				if (c == '}' && bracketDepth == 0) curlyBracketDepth--;
				if (curlyBracketDepth == 0 && mayExit) {
                                        //Debug.clogn("returning ", Util.class);
					return new String(sbTokens) + "}";
				} 
			}
			sbTokens.append(c);
                        //Debug.clogn("index at " + i + " of " + containsOpen.length(), Util.class);
			
		}
		 //Debug.clogn("returning ", Util.class);
		return new String(sbTokens);
		
		//throw new RuntimeException("i.e. Odd C# syntax ");
	}
        /** Return 'text more text' from 'random text { text more text }' ignoring
         * "", () and further {}.*/
	public static String getInsideFirstCurlyBrackets(String containsOpen) {
		String chopEnd = getUpToClosingCurlyBracket(containsOpen);
		String toReturn = chopEnd.substring(chopEnd.indexOf("{") + 1, chopEnd.length() -1).trim();
		return toReturn;

	}

            /** Make a long string from the initial plus the remaining
             *items in the tokeniser, joining adjacent pieces with a separator
             */
	public static String toString(String initial, StringTokenizer st, String sep) {
		String s = initial;
		while(st.hasMoreTokens()) {
			s = s + sep + st.nextToken();
		}
		return s;
	}
        /** String replacement 101. */
	public static String replaceString(String master, String toReplace, String replacement) {
            try {
		if (toReplace.equals("") || replacement.equals("")) {
			return master;
		}
		if (master.indexOf(toReplace) != -1) {
			String start = master.substring(0, master.indexOf(toReplace));
			String end = master.substring(master.indexOf(toReplace) + toReplace.length(), master.length());
			if (end.indexOf(toReplace) != -1) {
				end = replaceString(end, toReplace, replacement);
			}
			return start + replacement + end;
		} else {
			return master;
		}
            } catch (Throwable t) {
                Debug.stop("stop");
            }
            return null;
	}

            /** Apply trim() to every item in a list of strings.*/
	public static List trimStrings(List l) {
		List ll = new ArrayList();
		for (Iterator itr = l.iterator(); itr.hasNext();) {
			String next = (String) itr.next();
			if (!next.trim().equals("")) {
				ll.add(next.trim());
			}
		}
		return ll;
	}

            /** Replaces new lines with colons.*/
	public static String toSingleLine(String vbCode) {
            if (vbCode == null) {
                return "null";
                
            } else {
		return replaceString(vbCode, "\n", ":");
            }
	}

            /** List the Element children of this node.*/
	public static List getElts(Node n) {
		List l = new ArrayList();
		NodeList nl = n.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node nextNode = (Node) nl.item(i);
			if (nextNode instanceof Element) {
				l.add(nextNode);
			}
		}
		return l;
	}

            /** Pull the text out of the first Text child of this element.*/
	public static String getText(Element e) {
		if (e == null) {
			return "";
		}
		NodeList nl = e.getChildNodes();
		Text t = (Text) nl.item(0);
		return t.getData().trim();		
	}

            /** Recursively get all the files under a directory, filtering.*/
	public static List getAllEntryFilesIn(File dir, String filter ) {
		return getAllEntryFilesIn(dir, filter, new ArrayList());
	}
        
            /** Recursively get all the files under a directory, filtering.*/
	public static List getAllEntryFilesIn(File dir, String filter, List bannedFilenames ) {
		List l = new ArrayList();
		String[] ss = dir.list();
		for (int i = 0; i < ss.length; i++) {
			String next = ss[i];
			File nextFile = new File(dir, next);
			if (nextFile.isDirectory()) {
				l.addAll(getAllEntryFilesIn(nextFile, filter, bannedFilenames));
			}
			if (filter != null && !filter.equals("")) {
				if (next.endsWith(filter) && !bannedFilenames.contains(next)) {
					l.add(nextFile);
				}
			} else {
				l.add(nextFile);
			}			
		}
		return l;
	}
 
        /** Is return the first element in common.*/
	public static Object listContains(List a, List master) {
		for (Iterator itr = master.iterator(); itr.hasNext();) {
			Object next = itr.next();
			if (a.contains(next)) {
				return next;
			}
		}
		return null;
	}

            /** tab multiplied by i */
	public static String getIndent(int i) {
		String s = "";
		for (int j = 0; j < i; j++) {
			s = s + "\t";
		}
		return s;
	}

            /** Read the data from the input stream and write it to file.*/
	public static void read(InputStream is, String prefix) throws IOException {
		int i = 0;
		String s = "";
		String newline = "\n";
		File f = new File("jvbout.txt");
		FileOutputStream fos = new FileOutputStream(f);
		//writeln("["+prefix+"-start] ", fos, true);
		writeln("["+prefix+"] ", System.out, true);
		while (i != -1) {
			i = is.read(); // but it could block here
			if (i != -1) {		
				char c = (char) i;
				//writeln("" + c, fos, false);
                                writeln("" + c, System.out, false);
				char[] n = {c};
				if (newline.equals(new String(n))) {
					//writeln("["+prefix+"] ", fos, false);
                                        writeln("["+prefix+"] ", System.out, false);
				}
			} else {

			}
		}
		//writeln("\n["+prefix+"-done]", fos, true);
                writeln("\n["+prefix+"-done]", System.out, true);
		fos.close();
	}

            /** Read all the data in the file out.*/
	public static String getString(String pathname) throws Exception {
		String code = "";
		FileInputStream fis = new FileInputStream(pathname);
		return new String(readTextBytes(fis));
	}

	static byte[] readTextBytes(InputStream is) throws IOException {
		byte[] result = null; 
		byte[] buf = new byte[1024]; 
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		int numBytesRead; 
		while ((numBytesRead = is.read(buf)) != -1) { 
			bout.write(buf, 0, numBytesRead); 
		} 
		result = bout.toByteArray(); 
		return result; 
    } 
        /** Read the filepaths from a ; sep list.*/
	public static List getFilepaths(String filePaths, String filter) {
		List l = new ArrayList();
		StringTokenizer st = new StringTokenizer(filePaths, ";");
		while(st.hasMoreTokens()) {
                    String nextFilepath = st.nextToken();
                    if (nextFilepath.endsWith("*")) {
                        String directoryPath = nextFilepath.substring(0, nextFilepath.length() -1);
                        
                        List files = getAllEntryFilesIn(new File(directoryPath), filter);
                        for (Iterator itr = files.iterator(); itr.hasNext(); ) {
                            l.add(((File) itr.next()).toString());
                        }
                    } else {
                        l.add(nextFilepath);
                    }
		}
		return l;
	}

            /** Get the parent namespace of this context. */
	public static NamespaceStatement getParentNamespace(InterpretationContext context) {
		boolean keepGoingUp = true;
		InterpretationContext ic = context;
                //if (ic instanceof NamespaceStatement) {
                //    return (NamespaceStatement) ic;
                //}
		while (keepGoingUp) {
                        ic = ic.getParent();
			if (ic == null) {
				return null;
			} else {
				if (ic instanceof NamespaceStatement) {
					return (NamespaceStatement) ic;
				}
			}
                        
		}
		return null;
	}

            /** Make a list from a string assuming whitespace sparators.*/
	public static List toTokens(String fragment) {
		List l = new ArrayList();
		StringTokenizer st = new StringTokenizer(fragment);
		while (st.hasMoreTokens()) {
			l.add(st.nextToken());
		}
		return l;
	}

            /** Make a filename from a package name.*/
	public static String convertPackageToDir(String packageName) {
		return packageName.replace('.', java.io.File.separatorChar);
	}

	/** return a list of tokens derived from s using sep as
	* the determining seperator, but NEITHER chopping anything
	* that is inside braces ( ), NOR anything inside quotes ""
        * NOR anything inside more {} pairs.
         */ 

	public static List tokenizeIgnoringEnclosers(String s, char sep) {
		return tokenizeIgnoringEnclosers(s, "" + sep);
	}
        
        public static List tokenizeForWhitespace(String s) {
            return tokenizeIgnoringEnclosers(s, " ", "\t");
        }
        
        public static List tokenizeIgnoringEnclosers(String s, String sep1, String sep2) {
            List toReturn = new ArrayList();
            
                
            List l1 = tokenizeIgnoringEnclosers(s, sep1);
            for (Iterator itr = l1.iterator(); itr.hasNext();) {
                String nextString = (String) itr.next();
                List l2 = tokenizeIgnoringEnclosers(nextString, sep2);
                for (Iterator itr2 = l2.iterator(); itr2.hasNext();) {
                    String nextString2 = (String) itr2.next();
                    if (!nextString2.trim().equals("")) {
                        toReturn.add(nextString2);
                    }
                }
            }    
            return toReturn;
            
        }

            /** return a list of tokens derived from s using sep as
	* the determining seperator, but NEITHER chopping anything
	* that is inside braces ( ), NOR anything inside quotes ""
        * NOR anything inside more {} pairs.
         */ 
	public static List tokenizeIgnoringEnclosers(String s, String sep) {
            //Debug.clogn("tokenize " + s, Util.class);
            List tokens = new ArrayList();
            boolean incrementSq = true;
            if (sep.equals("[") || sep.equals("]")) {
                incrementSq = false;
            }

            if (s.indexOf(sep) == -1) { // optimisation - can safely remove
                if (!"".equals(s)) {
                    tokens.add(s);
		}
		return tokens;
            }
            int insideSomethingCounter = 0;
            int bracketDepth = 0;
            int curlyBracketDepth = 0;
            int sqBracketDepth = 0;
            boolean inQuote = false;
            boolean justMadeToken = false;
            
            StringBuffer tokenBuffer = new StringBuffer();
            for (int i = 0; i < s.length(); i++) {
		char c = s.charAt(i);
		justMadeToken = false;
                
		if (c == '"' && (i == 0 || ((i > 0) && s.charAt(i-1) != (char) '\\' ))) inQuote = !inQuote;
		if (!inQuote) {
                    if (c == '(') bracketDepth++;
                    if (c == '{') curlyBracketDepth++;
                    if (c == '}') curlyBracketDepth--;
                    if (c == ')') bracketDepth--;
                    if (c == ']' && incrementSq) sqBracketDepth++;
                    if (c == '[' && incrementSq) sqBracketDepth--;
                    if (sep.startsWith(""+c) && (!inQuote && bracketDepth == 0 && curlyBracketDepth==0 & sqBracketDepth==0)) {
			if (i + sep.length() < s.length()) {
                            String readingForward = s.substring(i, i + sep.length());
                            if (readingForward.equals(sep)) {
				tokens.add(new String(tokenBuffer));
				tokenBuffer = new StringBuffer();
				justMadeToken = true;
				i = i + sep.length() -1;
                            }
			}
                    }
		}
		if (!justMadeToken ) {
                    tokenBuffer.append(c);
		}
            }
            tokens.add(new String(tokenBuffer));
            return tokens;
	}

            /** Throw out the last element.*/
	public static List shrinkListByOne(List l) {
		List newL = new ArrayList();
		if (l.size() == 1) {
			return newL;
		}
		for (Iterator itr = l.iterator(); itr.hasNext();) {
			Object o = itr.next();
			if (itr.hasNext()) {
				newL.add(o);
			}
		}
		return newL;
	}
        
         public static String stripCurlies(String s) {
             boolean shrinking = true;
             String oldS = s;
             String newS;
             while(shrinking) {
                 newS = stripCurliesP(oldS);
                 if (newS.equals(oldS)) {
                     return newS;
                 }
                 oldS = newS;
             }
             throw new RuntimeException("Weird");
         }
        
        private static String stripCurliesP(String s) {
      
            if (s.startsWith("(")) {
                return s.substring(1, s.length());
            }
            if (s.endsWith(")")) {
                return s.substring(0, s.length()-1);
            }
            return s;
        }
        
        public static String stripQuotes(String s) {
            String toReturn = s;
            if (s.startsWith("\"")) {
                toReturn = s.substring(1, s.length());
            }
            if (toReturn.endsWith("\"")) {
                toReturn = toReturn.substring(0, toReturn.length()-1);
            }
            return toReturn;
        }

            /** Strip the () off it. <br>
             * don't do things like '(a) And (b)' -> 'a) And (b' <br>
             * but only things like '(a And b)' -> a And b
             */
	public static String stripBrackets(String str) {
		String s = str.trim();
		if (s.startsWith("(")) {

			boolean strip = false;
			int bracketCounter = 0;
			boolean getsClosed = false;
			for (int i = 0; i < s.length() - 1; i++) {
				char c = s.charAt(i);
				if (c == '(') {
					bracketCounter++;
				}
				if (c == ')') {
					bracketCounter--;
				}
				if (bracketCounter == 0) {
					getsClosed = true;
				}
			}
			//System.out.println(bracketCounter);
			if ( s.charAt(s.length() - 1) == ')' && !getsClosed) {
				return s.substring(1, s.length() -1);
			}
		}
		return s;
	}

            /** Make it into a string using \n to conjoin elements.*/
	public static String toString(List l) {
		String s = "";
		for (Iterator itr = l.iterator(); itr.hasNext();) {
			String n = (String) itr.next();
			s = s + "\n" + n;
		}
		return s;
	}
              /** Make it into a string using one whitespace to conjoin elements.*/
	public static String toSpacedString(List l) {
		String s = "";
		for (Iterator itr = l.iterator(); itr.hasNext();) {
			String n = (String) itr.next();
			s = s + " " + n;
		}
		return s.trim();

	}

            /** Make the string guaranteed to be less that (start+finish),
             * by making sure that the first start chars show and the last
             * finish chars show.*/
	public static String compactify(String s, int start, int finish) {
		if (s.length() > start + finish) {
			return s.substring(0, start) + "...(snip)..." + s.substring(s.length() - finish, s.length());
		}
		return s;
	}

            /** Make all the strings in a list shorter than start+finish
             */
	public static List compactifyStrings(List l, int start, int finish) {
		List ls = new ArrayList();
		for (Iterator itr = l.iterator(); itr.hasNext();) {
			String next = (String) itr.next();
			next = compactify(next, start, finish);
			ls.add(next);
		}
		return ls;
	}

	static void writeln(String s, OutputStream os, boolean line) throws IOException {
		if (line) {
			//System.out.println(s);
			os.write( (s + "\n").getBytes() );
		} else {
			//System.out.print(s);
			os.write( (s).getBytes() );
		}
	}

}
 