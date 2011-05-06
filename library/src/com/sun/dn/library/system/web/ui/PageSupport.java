/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Web.UI;

import java.util.*;

public abstract class PageSupport {
	static List instances = new ArrayList();

	protected PageSupport() {
		instances.add(this);
	}

	public static PageSupport getInstance(String classname) {
		for (Iterator itr = instances.iterator(); itr.hasNext();) {
			PageSupport ps = (PageSupport) itr.next();
			if (ps.getClass().getName().equals(classname)) {
				System.out.println("Found existing class for classname " + ps);
				initPageSupport(ps);
				return ps;
			}
		}
		try {
			System.out.println("Creating new Page Support for classname " + classname);
			PageSupport ps = (PageSupport) Class.forName(classname).newInstance();
			System.out.println("Created new page support " + ps);
			
			initPageSupport(ps);
			return ps;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException("Error creating bean " + classname);
		}
	
	}

	private static void initPageSupport(PageSupport ps) {
		//ps.initJavaContext();
		//ps.Page_Load(null, null);
	}

	public abstract void Page_Load(Object sender, com.sun.dn.library.System.EventArgsSupport e);

}
