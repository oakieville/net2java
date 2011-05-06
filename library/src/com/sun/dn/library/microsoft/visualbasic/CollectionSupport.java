/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/

package com.sun.dn.library.Microsoft.VisualBasic;

import com.sun.dn.*;
import java.util.*;

public class CollectionSupport {

	public static Object getItem(LinkedHashMap lhm, int index) {
		int i = 1;
		for (Iterator itr = lhm.keySet().iterator(); itr.hasNext();) {
			Object o = itr.next();
			
			if ( i == index ) {
				return lhm.get(o);
			}
			i++;
		}
		return null;
	}
        
        public static void remove(LinkedHashMap lhm, int index) {
            lhm.remove(getItem(lhm, index));
            
        }
       
}
