
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


import com.sun.dn.Interpreter;
import com.sun.dn.java.JavaProgram;
import com.sun.dn.util.Debug;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;
import java.util.*;
import java.io.*;
import java.beans.*;
import javax.swing.tree.*;
import javax.swing.event.*;
/**
 * Gui for editing Library entries, aka .NET class to Java translations.
 * @author Danny  Coward
 */
public class LibraryEntryFrame extends JFrame implements LibraryChangeListener  {
        String defaultDirectory;
        LibraryTreePanel ltp;
        LibraryEntryEditor leep;
        AliasedEntryPanel rep;
        FolderPanel lfe;
        SupportClassEditorPanel sce;
        JPanel currentEditorPanel;
		
	public LibraryEntryFrame (String defaultDirectory) {
            this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    //System.exit(0);
                }
            });
            this.defaultDirectory = defaultDirectory;
            //this.setIconImage(new IconImage());
            Debug.init("");
            LibraryData library = new LibraryData(new File(defaultDirectory), JavaProgram.CMDLINE_TYPE, Interpreter.VB_LANGUAGE, true);
            this.initGuiComponents(library);
            this.hookupGui();
            
            
            
        }
        
        private void saveEverything(LibraryData library) {
            java.util.List l = library.getLibraryEntries();
            for (Iterator itr = l.iterator(); itr.hasNext();) {
                Entry e = (Entry) itr.next();
                 try {
                    File f = e.getFile();
                
                    FileOutputStream fos = new FileOutputStream(f);
                    e.write(fos);
                    fos.close();
                    System.out.println("Copyright done for: " + f);
                } catch (Throwable t) {
                    t.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error saving " + e);
                }
            }
        }
        
        public LibraryData getLibrary() {
            return leep.library;
            
        }
        
        public void setSupportClassEditor(SupportClassEditor editor) {
            this.sce.setEditor(editor); 
        }
        
        public void hookupGui() {
            JTree tree = ltp.getTree();
            tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() { 
                 public void valueChanged(TreeSelectionEvent e) {
                     doChangeFile();
                 }
            });
        }
        
        public void initGuiComponents(LibraryData library) {
            this.setTitle("Edit Library: " + defaultDirectory);
            leep = new LibraryEntryEditor(this, defaultDirectory, library);
            this.getContentPane().setLayout(new BorderLayout());
            ltp = new LibraryTreePanel(defaultDirectory);
            lfe = new FolderPanel(this); 
            sce = new SupportClassEditorPanel(this);
            rep = new AliasedEntryPanel(this);
            resetEditorPanel(lfe);
        }
        
        private void resetPanels() {
            this.getContentPane().removeAll();
            
            this.getContentPane().add(ltp,  BorderLayout.WEST);
            this.getContentPane().add(currentEditorPanel, BorderLayout.CENTER);
            currentEditorPanel.setVisible(false);
            currentEditorPanel.setVisible(true);
        }
        
        
        private void resetEditorPanel(JPanel newOne) {
            this.currentEditorPanel = newOne;
            resetPanels(); 
        }
        
        public void filesChanged(File fileThatChanged) {
            ltp.setSelectedFile(fileThatChanged); 
        }
        
        public void doChangeFile() {
            File f = ltp.getSelectedFile();
            if (f == null) {
                return;
            }
            if (!leep.canOpenNewEntry()) {
                return;
            }
            try {
                if (f.isDirectory()) {
                    this.resetEditorPanel(this.lfe);
                    this.lfe.setFile(f);
                } else {
                    if (f.getName().endsWith(".java")) {
                        this.resetEditorPanel(sce);
                        sce.setFile(f);
                    } else if (f.getName().endsWith(".xml")) {
                        Entry e = new Entry(f, leep.library);
                        if (e.isAliased()) {
                            this.resetEditorPanel(rep);
                            rep.setEntry(e);
                        } else {
                            this.resetEditorPanel(leep);
                            leep.setEntry(e);
                        }
                    } else {
                        throw new RuntimeException("Wierdness");
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
                this.resetEditorPanel(new ErrorPanel(this, f));
            }
        }
        
         public static void main(String[] args) {
            JFrame f = new LibraryEntryFrame(args[0]); 
            f.setBounds(10,10,850,480);
            f.setVisible(true);
        }
        
}

class ErrorPanel extends JPanel {
    File f;
    LibraryChangeListener lcl;
    
    ErrorPanel(LibraryChangeListener lcl, File f) {
        this.lcl = lcl;
        this.f = f;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JLabel("Error Parsing Library entry file: " + f.getName()));
        JButton jb = new JButton("Delete");
        jb.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent ae) {
               deleteMe();
           } 
        });
        this.add(jb);
    }
    
    public void deleteMe() {
        int ret = JOptionPane.showConfirmDialog(this, "Really delete " + f + " ?", "Delete Library Entry", JOptionPane.YES_NO_OPTION);
        if (ret == JOptionPane.YES_OPTION) {
            f.delete();
            lcl.filesChanged(null);
        }
    }
}

 