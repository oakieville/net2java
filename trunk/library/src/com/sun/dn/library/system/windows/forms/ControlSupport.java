/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
/*
 * ControlSupport.java
 *
 * Created on January 17, 2006, 1:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.sun.dn.library.System.Windows.Forms;

import com.sun.dn.library.System.Windows.Forms.Control.ControlCollectionSupport;
import java.awt.Container;

public class ControlSupport {
    
    /** Creates a new instance of ControlSupport */
    public ControlSupport() {
    }
    
    public static ControlCollectionSupport getControls(Container c) {  
        return new ControlCollectionSupport(c); 
    } 
}

