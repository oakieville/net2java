
 /* 
 * Copyright (c) 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *  
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *  
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *  
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *  
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package com.sun.dn.library;

import com.sun.dn.util.Util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.beans.*;
import java.io.*;


 class NewEntryDialog extends JDialog {
    JTextField nameF = new JTextField();
    JRadioButton eventRB = new JRadioButton("Event Definition");
    JRadioButton typeRB = new JRadioButton("Class, Interface, Delegate or Structure");
    
    int pHeight = 150;
    int pWidth = 450;
    
    public NewEntryDialog(JFrame f) {
        super(f, true);
        super.getContentPane().setLayout(new BorderLayout());
        super.getContentPane().add(Box.createRigidArea(Util.BORDER), BorderLayout.NORTH);
        super.getContentPane().add(Box.createRigidArea(Util.BORDER), BorderLayout.SOUTH);
        super.getContentPane().add(Box.createRigidArea(Util.BORDER), BorderLayout.WEST);
        super.getContentPane().add(Box.createRigidArea(Util.BORDER), BorderLayout.EAST);
        JPanel p = this.initMainPanel();
        super.getContentPane().add(p, BorderLayout.CENTER);
        int x = f.getBounds().x + ( f.getBounds().width / 2 ) - (pWidth / 2);
        int y = f.getBounds().y + ( f.getBounds().height / 2 ) - (pHeight / 2);
        this.setBounds(x, y, pWidth, pHeight);
    }
    
    private JPanel initMainPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        Vector labels = new Vector();
        
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BorderLayout());
        JLabel nameLbl = new JLabel("Enter the .NET Typename: ");
        labels.add(nameLbl);
        namePanel.add(nameLbl, BorderLayout.WEST);
        namePanel.add(nameF, BorderLayout.CENTER);
        p.add(namePanel);
        
        JPanel typePanel = new JPanel();
        typePanel.setLayout(new BorderLayout());
        JLabel typeLbl = new JLabel("Choose the type: ");
         labels.add(typeLbl);
        typePanel.add(typeLbl, BorderLayout.WEST);
        JPanel bgPanel = new JPanel();
        bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(this.typeRB);
        bg.add(this.eventRB);
        bgPanel.add(this.typeRB);
        bgPanel.add(this.eventRB);
        typeRB.setSelected(true);
        
        typePanel.add(bgPanel);
        p.add(typePanel);
        JPanel okCancelPanel = new JPanel();
        
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");
        okCancelPanel.add(okBtn);
        okCancelPanel.add(cancelBtn);
        p.add(okCancelPanel);
        Util.normaliseComponents(labels);
        okBtn.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent ae) {
               setVisible(false);
           } 
        });
        cancelBtn.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent ae) {
               nameF.setText("");
               setVisible(false);
           } 
        });
        
        nameF.setText("e.g. 'Object' or 'Click'");
        nameF.setSelectionStart(0);
        nameF.setSelectionEnd(nameF.getText().length());
        return p;
        
    }
    
    public boolean isEvent() {
        return this.eventRB.isSelected();
    }

	public void setName(String name) {
		nameF.setText(name);
                nameF.setSelectionStart(0);
                nameF.setSelectionStart(name.length());
        }
    
    public String getName() {
        return nameF.getText().trim();
    }
}
 