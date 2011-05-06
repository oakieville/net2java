
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

package com.sun.dn.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import com.sun.dn.util.*;
import com.sun.dn.*;
import com.sun.dn.java.*;
import com.sun.dn.parser.*;
import java.beans.*;
import java.io.*;

/**
 * GUI for the .NET Interpreter.
 * @author Danny  Coward
 */
public class ImportPanel extends JPanel {
    private String language = "vb";
    private ImportProgressDialog progressD;
    private JTextField locationTF = new JTextField("");
    private JButton locationB = new JButton("Browse...");
    private JTextField destinationTF = new JTextField("");
    private JButton destinationB = new JButton("Browse...");
    private JCheckBox cmdCB = new JCheckBox("Console");
    private JCheckBox guiCB = new JCheckBox("Windows");
    private JCheckBox webCB = new JCheckBox("ASP.NET");
    private JComboBox mainClassnameCB = new JComboBox();
    private static JFileChooser locationFC = new JFileChooser(); 
    private static JFileChooser destinationFC = new JFileChooser(); 
    
    private String libraryPath;
    
    static Dimension BORDER = new Dimension(10, 10);
   
    
    public ImportPanel(String libraryPath) {
        this.libraryPath = libraryPath;
        this.initUI();
        this.initHookups();
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public void setDefaultDestinationDirectory(String defaultDirName) {
        if (defaultDirName != null) {
            destinationTF.setText(defaultDirName);
        } else {
            File cur = new File("");
            destinationTF.setText(cur.getAbsolutePath());
        }
    }
    
    private void initHookups() {
        locationB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                doBrowseProjectLocation();
            }
        });
        destinationB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                doBrowseDestinationLocation();
            }
        });
        ActionListener cbActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                doWebOptionUpdate();
            }
        };
        webCB.addActionListener(cbActionListener);
        guiCB.addActionListener(cbActionListener);
        cmdCB.addActionListener(cbActionListener);
        locationTF.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                doUpdateForLocationChange();
            }
        });
        destinationTF.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                doUpdateForDestinationChange();
            }
        });
    }
    
    private void initUI() {
        
        JPanel p = null;
	JPanel pp = null;
		
	this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	this.add(Box.createRigidArea(BORDER));
		

	JPanel cp = new JPanel();
	cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
	this.add(cp);
	this.add(Box.createRigidArea(BORDER));


	// ____________Directory Chooser
	p = new JPanel();
	JLabel lbl = new JLabel("Project Directory to Import: ");
	p.setLayout(new BorderLayout());
	p.add(lbl, BorderLayout.WEST);
     
	p.add(locationTF, BorderLayout.CENTER);
        locationTF.setText( System.getProperty("user.home") );
        //locationTF.setText( "d:/jvb/projects/guiProjects/FileUI");
	p.add(locationB, BorderLayout.EAST);
	locationTF.setEditable(false);
	
	cp.add(Box.createRigidArea(BORDER));
	cp.add(p);
        
        // _________Destination Chooser
	p = new JPanel();
	lbl = new JLabel("Destination for Java output: ");
	p.setLayout(new BorderLayout());
	p.add(lbl, BorderLayout.WEST);
        destinationTF.setEditable(false);
        destinationTF.setText( (new File("")).toString());
	p.add(destinationTF, BorderLayout.CENTER);
	p.add(destinationB, BorderLayout.EAST);
	
	
	cp.add(Box.createRigidArea(BORDER));
	cp.add(p);
        
        // _________Project Type Chooser
	p = new JPanel();
	lbl = new JLabel("Select the Application Type: ");
	p.setLayout(new BorderLayout());
	p.add(lbl, BorderLayout.WEST);
        pp = new JPanel();
        pp.setLayout(new BoxLayout(pp, BoxLayout.Y_AXIS));
        pp.add(cmdCB);
        pp.add(guiCB);
        pp.add(webCB);
        guiCB.setSelected(true);
        ButtonGroup bg = new ButtonGroup();
        bg.add(cmdCB);
        bg.add(guiCB);
        bg.add(webCB);
	p.add(pp, BorderLayout.CENTER);
        JButton falseButton = new JButton("Browse...");
        falseButton.setVisible(false);
	p.add(falseButton, BorderLayout.EAST);
	locationTF.setEditable(false);
	cp.add(Box.createRigidArea(BORDER));
	cp.add(p);
        
        // _________Destination Chooser
	p = new JPanel();
	lbl = new JLabel("Select the Main class: ");
	p.setLayout(new BorderLayout());
	p.add(lbl, BorderLayout.WEST);
     
	p.add(mainClassnameCB, BorderLayout.CENTER);
        falseButton = new JButton("Browse...");
        falseButton.setVisible(false);
	p.add(falseButton, BorderLayout.EAST);
	
	cp.add(Box.createRigidArea(BORDER));
	cp.add(p);
    }
    
    public void doBrowseProjectLocation() {
        locationFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
       // locationFC.setCurrentDirectory(new File("d:/dnj/projects/demos"));
	int returnVal = locationFC.showOpenDialog(this); 
	if(returnVal == JFileChooser.APPROVE_OPTION) { 
            File f = locationFC.getSelectedFile();
            locationTF.setText(f.toString());
            this.doUpdateMainClassnames();
	} 
    }
    
     public void doBrowseDestinationLocation() {
        destinationFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	int returnVal = destinationFC.showOpenDialog(this); 
	if(returnVal == JFileChooser.APPROVE_OPTION) { 
            File f = destinationFC.getSelectedFile();
            destinationTF.setText(f.toString());  
	}
    }
     
     public void doUpdateMainClassnames() {
         DefaultComboBoxModel cbm = new DefaultComboBoxModel();
         java.util.List l = this.getSourceFilenames();
         for (int i = 0; i < l.size(); i++) {
             File f = new File((String) l.get(i));
             String name = f.getName();
             name = name.substring(0, name.indexOf("."));
             if (!name.equals("AssemblyInfo")) {
                 cbm.addElement(name);
             }
             
         }
         mainClassnameCB.setModel(cbm);
     }
     
     public void doWebOptionUpdate() {
         mainClassnameCB.setEnabled(!webCB.isSelected());
    }
     
     public void doUpdateForLocationChange() {
         //System.out.println("Location Changed");
     }
     
     public void doUpdateForDestinationChange() {
         //System.out.println("Destination Changed");
     }
     
     public ImportProgressDialog getImportProgressDialog() {
         if (progressD == null) {
             JFrame frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this);
             progressD = new ImportProgressDialog(frame);
         }
         return progressD;
     }
     
     public java.util.List getSourceFilenames() {
         File f = new File(this.locationTF.getText());
         java.util.List ll = new ArrayList();
         java.util.List l = new ArrayList();
         if (language.equals("vb")) {
             java.util.List vbCruftFilenames = new ArrayList();
             vbCruftFilenames.add("Application.Designer.vb");
             vbCruftFilenames.add("AssemblyInfo.vb");
             vbCruftFilenames.add("Resources.Designer.vb");
             vbCruftFilenames.add("Settings.Designer.vb");
             l.addAll(Util.getAllEntryFilesIn(f, ".vb", vbCruftFilenames));
         } else {
             java.util.List csCruftFilenames = new ArrayList();
             csCruftFilenames.add("Application.Designer.cs");
             csCruftFilenames.add("AssemblyInfo.cs");
             csCruftFilenames.add("Resources.Designer.cs");
             csCruftFilenames.add("Settings.Designer.cs");
             l.addAll(Util.getAllEntryFilesIn(f, ".cs", csCruftFilenames));
         }
         if (webCB.isSelected()) {
             l.addAll(Util.getAllEntryFilesIn(f, ".asp"));
         }
         for (int i = 0; i < l.size(); i++) {
             ll.add(l.get(i).toString());
         }
         if (ll.isEmpty()) {
                this.getImportProgressDialog().finished();
                //JOptionPane.showMessageDialog(this, "There are no ." + language + " source files found in: " + locationTF.getText());
                int ret = JOptionPane.showConfirmDialog(this, "There are no ." + language + " source files found in: " + locationTF.getText() + "\nBrowse again ?");
                if (ret == JOptionPane.YES_OPTION) {
                    doBrowseProjectLocation();
                }
            }
         //System.out.println(ll);
         return ll;
     }
     
     public String getMainClassname() {
         return (String) this.mainClassnameCB.getSelectedItem();
     }
     
     public String getProjectType() {
         if (cmdCB.isSelected()) {
            return JavaProgram.CMDLINE_TYPE;
         } else if (webCB.isSelected()) {
             return JavaProgram.WEB_TYPE;
         } 
         return JavaProgram.GUI_TYPE;
     }
     
     public String getWriteDirectory() {
         return (new File(destinationTF.getText())).toString();
     }
     
     public void startTranslation() {
         Thread t = new Thread() {
             public void run() {
                 doTranslate();
             }
         };
         try {
             t.start();
             
         } catch (Throwable tt) {
             
         }
     }
     
     public void translationIsDone() {
         
     }
     
     public void doTranslate() {
         
         try {
            this.getImportProgressDialog().started();
            System.out.println("Translate");
            java.util.List files = this.getSourceFilenames();
            
            Debug.init("");
            Interpreter.setGUI(true);
            Interpreter interpreter = new Interpreter(System.out, this.language);
            JavaProgram jp = interpreter.createJavaProgram(files,
				this.libraryPath ,
				this.getMainClassname(),
				this.getProjectType(), 
				TranslationPolicy.GENTLE);
            this.getImportProgressDialog().parsed(); 
            int value = interpreter.writeJavaProgram(jp, this.getWriteDirectory());
            this.getImportProgressDialog().finished(); 
            TranslationReport report = jp.getTranslationReport();
            if (report.hasTypeResolveErrors() || report.hasTranslationWarnings()) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
		report.doReport(report.getTypeResolveExceptions(), report.getTranslationWarnings(),  baos );
		String reportS = new String(baos.toByteArray());
		showReport(reportS);
            } 
        } catch (Throwable t) { 
            t.printStackTrace();
            this.getImportProgressDialog().finished();
            int ret = JOptionPane.showConfirmDialog(this, "Error parsing project in: " + locationTF.getText() + "\n Show stack ?");
            if (ret == JOptionPane.YES_OPTION) {
                JFrame jf = new JFrame();
                jf.setTitle("Stack Trace");
                jf.setBounds(50, 50, 500, 500);
                JTextArea jta = new JTextArea();
                jf.getContentPane().add(new JScrollPane(jta));
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                t.printStackTrace(pw);
                jta.setText(sw.toString());
                jf.setVisible(true);
            }
        }
        this.getImportProgressDialog().finished(); 
     }
     
     private void showReport(String s) {
         ReportPanel rp = new ReportPanel();
         rp.setText(s);
         rp.setVisible(true);
     }
    
    
    
     public static void main(String args[]) {
        JFrame j = new JFrame();
        j.setBounds(5, 5, 500, 260);
        
        final ImportPanel ip = new ImportPanel(System.getProperty("user.home") + "/dnj/");
        ip.language = args[0];
        ip.setDefaultDestinationDirectory(System.getProperty("user.home") + "/dnj_temp");
        j.getContentPane().setLayout(new BorderLayout());
        j.getContentPane().add(ip, BorderLayout.CENTER);
        j.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        JButton jb = new JButton("Translate");
        j.getContentPane().add(jb, BorderLayout.SOUTH);
        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                ip.startTranslation();
            }
        });
        j.setVisible(true);
        
        
    }
}

 