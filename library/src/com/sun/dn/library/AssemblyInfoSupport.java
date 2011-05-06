/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library;

import com.sun.dn.util.AssemblyInformation;

	/** Support class for parsing VB project
	** information
	** @author danny.coward@sun.com
	**/

public class AssemblyInfoSupport {
	private AssemblyInformation ai;

	public AssemblyInfoSupport() {
		this.ai = AssemblyInformation.getInstance();
	}

	public String getValueFor(String propertyName) {
		return ai.getValueFor(propertyName);
	}

}
