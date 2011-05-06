/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.ComponentModel;

public class ContainerSupport implements IContainerSupport {


	public void dispose() {
		System.out.println("Library ContainerSupport doesn't implement dispose()");
	}

	public void Dispose(boolean b) {
		System.out.println("Library ContainerSupport doesn't implement Dispose(boolean b)");

	}

}

