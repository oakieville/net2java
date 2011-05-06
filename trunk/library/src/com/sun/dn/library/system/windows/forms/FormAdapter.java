/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Windows.Forms;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public abstract class FormAdapter extends JFrame implements WindowListener {
	private FormAdapter owner;
        private JDialog dialog;
        private boolean isDialog;
        private Thread dialogThread;

	public FormAdapter() {
		this.addWindowListener(this);		
		

	}
        
        public void InitializeComponent() {
            
        }

	public int showDialog(Object o) {
            this.isDialog = true;
            dialog = new JDialog((Frame)o, true);
            dialog.setTitle(this.getTitle());
            dialog.setBounds(this.getBounds());
            dialog.getContentPane().add(this.getContentPane());
            dialog.setVisible(true);
            
            return 0;
	}
        
        public void setVisible(boolean b) {
            if (isDialog) {
                this.dialog.setVisible(b);
            } else {
                super.setVisible(b);
            }
            
        }

	public int disposeForm() {
		//System.out.println("Form dispose");
		return 0;

	}

	public void setMenu(MainMenuSupport mms) {
		//System.out.println("FormAdapter setMenu");
	}

	public void close() {
		//System.out.println("FormAdapter close");
	}

	public MainMenuSupport getMenu() {
		//System.out.println("Form Adapter getMenu()");
		return null;
	}


	public void setAcceptButton(JButton j) {
		//System.out.println("FormAdapter noop");
	}

	public void setCancelButton(JButton j) {
		//System.out.println("FormAdapter noop");
	}

	public void setAutoScaleBaseSize(Point d) {
		//System.out.println("Library: FormAdapter does not implement setAutoScaleBaseSize(Point d)");
	}

	public void resumeLayout(boolean b) {
		this.getContentPane().doLayout();
	}

	public void suspendLayout() {
		this.getContentPane().setLayout(new AbsoluteLayout());
	}

	protected void Dispose(boolean b) {
		//System.out.println("Library: FormAdapter does not implement Dispose(boolean b)");

	}

	public void setFormOwner(FormAdapter owner) {
		this.owner = owner;
	}

	public void setIcon(Object ii) {
		//System.out.println("no op in Fprm adapter setting icon");
	}

	public Image getIcon() {
		//System.out.println("no op in Form adapter get Icon");
		return null;
	}

	public void setMinimumSize(Dimension d) {
		//System.out.println("no op in Fprm adapter setMinimumSize");
	}


	public FormAdapter getFormOwner() {
		return owner;
	}

	public void windowClosing(WindowEvent e) {
		//System.out.println("Closing");
		System.exit(0);	
	}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

}
