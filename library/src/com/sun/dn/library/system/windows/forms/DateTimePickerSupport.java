/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/


package com.sun.dn.library.System.Windows.Forms;


import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;

public class DateTimePickerSupport extends JPanel {
    JComboBox monthComboBox = new JComboBox();
    JComboBox dayComboBox = new JComboBox();
    private Vector als = new Vector();
    
    /** Creates a new instance of DateTimePickerSupport */
    public DateTimePickerSupport() {
       this.monthComboBox.setModel(this.getMonthModel());
       this.dayComboBox.setModel(this.getDayModel());
       this.add(monthComboBox);
       this.add(dayComboBox);
       ActionListener del = new ActionListener() {
           public void actionPerformed(ActionEvent ae) {
               notifyListeners();
           }
       };
       this.monthComboBox.addActionListener(del);
       this.dayComboBox.addActionListener(del);
    }
    
    public void addActionListener(ActionListener al) {
        als.add(al);
    }
    
     void notifyListeners() {
        for (Enumeration e = als.elements(); e.hasMoreElements();) {
            ActionListener al = (ActionListener) e.nextElement();
            ActionEvent ae = new ActionEvent(this, 0, "no command");
            al.actionPerformed(ae);
        }
    }
    
    public void setSize(Dimension r) {
        super.setSize(new Dimension(r.width, r.height + 10));
    }
    
    private ComboBoxModel getDayModel() {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for (int i = 1; i < 32; i++) {
            m.addElement("" + i);
        }
        return m;
    }
    
    private ComboBoxModel getMonthModel() {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        m.addElement("January");
        m.addElement("February");
        m.addElement("March");
        m.addElement("April");
        m.addElement("May");
        m.addElement("June");
        m.addElement("July");
        m.addElement("August");
        m.addElement("September");
        m.addElement("October");
        m.addElement("November");
        m.addElement("December");
        return m;
    }
    
    public Date getValue() {
        int month = monthComboBox.getSelectedIndex();
        int day = dayComboBox.getSelectedIndex();
        Date d = new Date();
        d.setDate(day+1);
        d.setMonth(month);
        return d;
    }
    
    public void setValue(Date d) {
        int month = d.getMonth();
        monthComboBox.setSelectedIndex(month);
        int day = d.getDate();
        dayComboBox.setSelectedIndex(day-1);
        
    }
    
}

