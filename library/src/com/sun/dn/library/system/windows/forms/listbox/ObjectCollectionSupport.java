/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/


package com.sun.dn.library.System.Windows.Forms.ListBox;

import javax.swing.*;

public class ObjectCollectionSupport {
    private DefaultListModel model;
   
    /** Creates a new instance of ListBoxObjectCollectionSupport */
    public ObjectCollectionSupport(DefaultListModel model) {
        this.model = model;
    }
    
    public void clear() {
        model.removeAllElements();
    }
    
    public void add(Object o) {
        //System.out.println("Adding object  " + o + " to " + this.model);
        this.model.addElement(o);
    }
    
    public void addArray(Object[] o) {
        for (int i = 0; i < o.length; i++) {
            this.add(o[i]);
        }
    }
    
    public String toString() {
        return model.toString();
    }
    
}

