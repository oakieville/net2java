/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System;

public class EventHandlerSupport extends DelegateSupport {
	
	public EventHandlerSupport(Object jTargetName, String methodName) {
		super(jTargetName.toString(), methodName);
	}
}
