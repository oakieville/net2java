/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Web.UI.WebControls;

import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.component.UISelectItem;
import java.util.*;

public class ListBoxSupport {
	
	public static void setSelectedIndex(HtmlSelectOneListbox lb, int i) {
		List l = lb.getChildren();
		for (Iterator itr = l.iterator(); itr.hasNext();) {
			System.out.println(itr.next());
		}
		UISelectItem o = (UISelectItem) l.get(i);
		System.out.println("Select: " + o.getItemValue());
		lb.setValue(o.getItemValue());
	}

	public static String getSelectedValue(HtmlSelectOneListbox lb) {
		return (String) lb.getValue();
	}

	public static void removeListItemAt(List l, int i) {
		l.remove(i-1);
	}

	public static void addListItem(List c, Object o) {
		UISelectItem se = new UISelectItem();
		se.setItemValue(o);
		se.setItemLabel(o.toString());
		c.add(se);
	}	

}
