/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Web.UI.WebControls;

import javax.faces.component.html.HtmlSelectOneRadio;

public class RadioButtonSupport {
	public static String CHECKED = "checked";
	public static String NOT_CHECKED = "not.checked";


	public static boolean getChecked(HtmlSelectOneRadio sor) {
		return CHECKED.equals(sor.getValue());
	}

	public static void setChecked(HtmlSelectOneRadio sor, boolean b) {
		String value = "";
		if (b) {
			value = CHECKED;
		} else {
			value = NOT_CHECKED;
		}
		sor.setValue(value);
	}	

}
