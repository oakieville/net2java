/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System;

public class DelegateSupport {
	private String targetJavaExp;
	private String methodName;

	public DelegateSupport(String targetJavaExp, String methodName) {
		this.targetJavaExp = targetJavaExp;
		this.methodName = methodName;
	}
	
}
