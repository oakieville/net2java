/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
/*
 * ControlControlCollectionSupport.java
 *
 * Created on January 17, 2006, 12:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.sun.dn.library.System.Windows.Forms.Control;

import  java.awt.Container;
import  java.awt.Component;
import  javax.swing.*;
import com.sun.dn.library.System.Windows.Forms.*;

/**
 *
 * @author localuser
 */
public class ControlCollectionSupport {
    Container component;
    
    
    /** Creates a new instance of ControlControlCollectionSupport */
    public ControlCollectionSupport(Container c) {
        this.component = c;
    }
   
    public void add(Component c) {
      if (c instanceof NeedsScrollbars) {
          JScrollPane jsp = new JScrollPane(c);
          jsp.setBounds(c.getBounds());
          component.add(jsp);
      } else {
          component.add(c);
      }
            
        
    }
    
}