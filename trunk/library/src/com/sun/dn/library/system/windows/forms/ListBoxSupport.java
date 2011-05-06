/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/


package com.sun.dn.library.System.Windows.Forms;

import com.sun.dn.library.System.Windows.Forms.ListBox.ObjectCollectionSupport;
import javax.swing.*;

public class ListBoxSupport extends JList implements NeedsScrollbars {
    
    /** Creates a new instance of ListBoxSupport */
    public ListBoxSupport() {
        
    }
    
    
    public static ObjectCollectionSupport getItems(JList list) {
        if ( !(list.getModel() instanceof DefaultListModel) ){
            list.setModel(new DefaultListModel());
        }
        return new ObjectCollectionSupport( (DefaultListModel) list.getModel());
    }
    
}

