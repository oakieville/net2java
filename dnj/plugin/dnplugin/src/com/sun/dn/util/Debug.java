
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
	
	/** Debugging class.
	@author danny.coward@sun.com
	*/

public class Debug {
	static Debug debug;
	static DebugWindow dw;
	List classNames = new ArrayList();
	List entries = new ArrayList();
	int limit = 20;
	static boolean isOn = true;
	long lastTime = (new java.util.Date()).getTime();
	static int printLength = 3000;
	static boolean libDebug = false;
	static boolean javaDebug = false;

	public static void init(String classes) {
		debug = new Debug(classes);
		//dw = new DebugWindow();
	}

	public static void flush() {
		debug.entries = new ArrayList();
	}

	public static void stop(Object o) {
		if (o instanceof Class) {
			throw new Stop((Class) o);
		} else {
			throw new Stop(o.getClass());
		}
	}

	public static boolean isJavaDebug() {
		return javaDebug;
	}

	public static long getTime() {
		return (new Date()).getTime();
	}	

	Debug(String classes) {
		StringTokenizer st = new StringTokenizer(classes, ",");
		while (st.hasMoreTokens()) {
			String nextClass = st.nextToken();
			classNames.add(nextClass);
		}
	}

	public static void logn(Object s, Object sender) {
		debug.l(s.toString(), sender.getClass(), true);	
	}

	public static void liblogn(Object s, Object sender) {
		if (libDebug) {
			System.out.println("[" + sender + "] " + s);
		}

	}

	public static void setOn(boolean b) {
		isOn = b;
	}


	public static void clogn(Object s, Class claxx) {
		debug.l(s.toString(), claxx, true);	
	}

	void l(String s, Class claxx, boolean newLine) {
		long l = (new java.util.Date()).getTime();
		long timeSinceLast = l - lastTime;
		lastTime = l; 
	
		String shortName = this.getShortName(claxx);
		String d = "[" + shortName  + "] " + s;
		this.addEntry(d);
		//System.out.println(classNames);
		if (isOn && classNames.contains(shortName)) { // list turns off the debug	
			if (newLine) {
				//dw.addText(d);
				if (d.length() > printLength ) {
					d = d.substring(0, printLength ) + "-cut";
				}
				System.out.println(d);	
			} else {
				System.out.print(d);
			}
		}
		if(timeSinceLast  > 10000) {
			//dumpEntries();
			//throw new Stop(Debug.class);		
		}
	}

	private void addEntry(String d) {
		this.entries.add(d);
		if (this.entries.size() > limit) {
			this.entries.remove(0);
		}
	}

	public static void dumpEntries() {
		for (Iterator itr = debug.entries.iterator(); itr.hasNext();) {
			System.out.println("." + itr.next());
		}
		System.out.println("Entries dumped");
	}

	private String getShortName(Class claxx) {
		String name = claxx.getName();
		return name.substring(claxx.getPackage().getName().length() + 1, name.length());
	}
}
 