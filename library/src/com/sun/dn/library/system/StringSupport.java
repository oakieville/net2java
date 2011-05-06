/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System;

import com.sun.dn.util.*;

public class StringSupport {
    
    private static String replaceString(String master, String toReplace, String replacement) {
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
	} 

	public static int compare(String a, String b) {
		// (dannyc)
		return 0;
	}

	public static int compare(String a, String b, boolean ignoreCase) {
		System.out.println("StringS.compare(stra, strb, ignoreCaseBool)");
		return 0;
	}

	public static int compareOrdinal(String a, String b) {
		System.out.println("String . compare ordinal stra, strb");
		return 0;
	}

	public static String concat(String s1, String s2, String s3) {
		return s1 + s2 + s3;
	}

	public static boolean endsWith(String s, String end) {
		return s.endsWith(end);

	}

	public static String format(String s, Object o) {
		//System.out.println("Format " + s + " with " + o);
		if (o == null) {
			return s;
		} else {
			return replaceString(s, "{0}", o.toString());
		}
	}

	public static String format(String s, Object o, Object oo) {
		if (oo == null) {
			return format(s, o);
		} else {
			return replaceString(format(s, o), "{1}", oo.toString());
		}
	}

	public static String format(String s, Object o, Object oo, Object ooo) {
		if (ooo == null) {
			return format(s, o, oo);
		} else {
			return replaceString(format(s, o, oo), "{2}", ooo.toString());
		}
	}


	public static String join(String a, String[] b) {
		System.out.println("String . join ordinal stra, strarrayb");
		return "no result";
	}

	public static int lastIndexOf(String str, String s) {
		if (s.equals("")) {
			return 0;
		}
		return str.lastIndexOf(s);
	}

	public static int lastIndexOf(String s, String t, int i, int count) {
		String testString = s.substring(i, i+count);
		return lastIndexOf(testString, t, i) + i;
	}

	public static int lastIndexOf(String str, String s, int i) {
		if (s.equals("")) {
			return 0;
		}
		return str.lastIndexOf(s, i);


	}

	public static int lastIndexOfAny(String s, char[] chars) {
		System.out.println("String . lastIndexOfAny stra, ch[]ar");
		return 0;
	}

	public static int lastIndexOfAny(String s, char[] chars, int i, int j) {
		System.out.println("String . lastIndexOfAny stra, ch[]ar");
		return 0;
	}

	public static int lastIndexOfAny(String s, char[] chars, int i) {
		System.out.println("String . lastIndexOfAny stra, ch[]ar");
		return 0;
	}

	public static int indexOfAny(String s, char[] chars, int k, int l) {
		int j = s.length() + 1;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			int ind = indexOf(s, "" + c, k, l);
			if (ind != -1) {
				if (ind < j) {
					j = ind;
				}
			}
		}
		if (j == s.length() + 1) {
			return -1;
		} else {
			return j;
		}
	}

	public static int indexOfAny(String s, char[] chars, int k) {
		int j = s.length() + 1;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			int in = indexOf(s, "" + c, k);
			if (in != -1) {
				if (in < j) {
					j = in;
				}
			}
		}
		if (j == s.length() + 1) {
			return -1;
		} else {
			return j;
		}


	}

	public static int indexOfAny(String s, char[] chars) {
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			int j = indexOf(s, "" + c);
			if (j != -1) {
				return j;
			}
		}
		return -1;
	}


	public static String[] split(String str, char[] separator, int count) {
		System.out.println("String . split char array int");
		return null;
	}

	public static String[] split(String str, char[] separator) {
		System.out.println("String . split str char array");
		return null;
	}

	public static boolean startsWith(String s, String str) {
		return s.startsWith(str);
	}

	

	public static String remove(String s, int start, int count) {
		String startS = s.substring(0, start);
		String endS = s.substring(start + count, s.length());
		return startS + endS;
	}

	public static int indexOf(String str, String s, int i) {
		if (s.equals("")) {
			return 0;
		}
		return str.indexOf(s, i);

	}

	public static int indexOf(String str, String s) {
		if (s.equals("")) {
			return 0;
		}
		return str.indexOf(s);
	}

	public static int indexOf(String s, String t, int i, int count) {
		String testString = s.substring(0, i+count);
		return indexOf(testString, t, i);
	}

	public static String insert(String s, int i, String t) {
		String startS = s.substring(0, i);
		String endS = s.substring(i, s.length());
		return startS + t + endS;
	}

	public static String padRight(String s, int i) {
		return padRightC(s, i, ' ');

	}

	public static String padRightC(String s, int i, char c) {
		String ss = s;
		for (int j = 0; j < i; j++) {
			ss = ss + c;
		}
		//System.out.println("String padLeft");
		return ss;

	}


	public static String padLeft(String s, int i) {
		return padLeftC(s, i, ' ');
	}

	public static String padLeftC(String s, int i, char c) {
		String ss = s;
		for (int j = 0; j < i; j++) {
			ss = c + ss;
		}
		//System.out.println("String padLeft");
		return ss;
	}

	public static String padLeft(String s, int i, char[] chars) {
		System.out.println("String padLeft");
		return "no result";
	}

	public static String toUpper(String s) {
		//System.out.println("to upper " + s);
		return s.toUpperCase();

	}

	public static String trim(String s) {
		return s.trim();
	}

	public static String trim(String s, char[] chars) {
		String looper = s;
		for (int i = 0; i < chars.length; i++) {
			looper = trim(looper, chars[i]);
		}
		return looper;
	}

	private static String trim(String s, char c) {
		String left = trimEndC(s, c);
		return trimStartC(left, c);

	}

	public static String trimEnd(String s) {
		return trimEndC(s, ' ');
	}

	private static String trimEndC(String str, char bc) {
		boolean isBad = true;
		String s = "";
		try {
		for (int i = str.length()-1; i >=0; i--) {
			char c = str.charAt(i);
			if (c != bc) { // not equal to the bad char
				isBad = false;
			} else {
				
			}
			if (!isBad) {
				s = c + s;
			}

		}
		} catch (Throwable t) {
			t.printStackTrace();
			return "error in method string timend";
		}
		return s;

	}
	
	public static String trimEnd(String s, char[] chars) {
		String looper = s;
		for (int i = chars.length-1; i >= 0; i--) {
			looper = trimEndC(looper, chars[i]);
		}
		return looper;

	}

	public static String trimStart(String toTrim) {
		//System.out.println("String . trimStart str");
		return trimStartC(toTrim, ' ');
	}

	public static String trimStart(String s, char[] chars) {
		String looper = s;
		for (int i = 0; i < chars.length; i++) {
			looper = trimStartC(looper, chars[i]);
		}
		return looper;

	}

	private static String trimStartC(String str, char bc) {
		boolean isBad = true;
		String s = "";
		try {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c != bc) {
				isBad = false;
			} else {
				
			}
			if (!isBad) {
				s = s + c;
			}
		}
		} catch (Throwable t) {
			t.printStackTrace();
			return "error in method string timend";
		}
		return s;
	}

	public static String replace(String master, String replacee, String replacer) {
		//System.out.println("Replace in ." + master + ". the string ." + replacee + ". with ." + replacer + ".");
		String res = replaceString(master, replacee, replacer);
		//System.out.println("Result ." + res + ".");
		return res;
	}

}
