
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
import javax.swing.filechooser.*;
import java.util.*;
import java.io.*;
import com.sun.dn.library.LibraryData;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.beans.*;
/**
 * Gui for editing Library entries, aka .NET class to Java translations.
 * @author Danny  Coward
 */
 class LibraryTreePanel extends JPanel {
    java.util.List allEntries = new ArrayList();
    private JTree tree;
    private String defaultDirectory;
    private File selectedFile = null;
    private JButton newFolderB = new JButton("New Folder...");
    private JButton newEntryB = new JButton ("New Entry...");
    private JButton removeB = new JButton("Remove");
    private DefaultMutableTreeNode treeNodeToSelect = null;
    
		
    public LibraryTreePanel (String defaultDirectory) {
        this.defaultDirectory = defaultDirectory;

	this.initGui();
        hookupGui();
        this.refreshGui();
    }
    
    public JTree getTree() {
        return this.tree;
    }
    
    public File getSelectedFile() {
        if (tree.getSelectionPath() != null) {
            //System.out.println("here");
            //System.out.println(tree.getSelectionPath().getLastPathComponent() + " " + tree.getSelectionPath().getLastPathComponent().getClass());
            DefaultMutableTreeNode tn =  (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            //System.out.println("heddddddddddre");
            //System.out.println(tn.getUserObject() + " " + tn.getUserObject().getClass());
            selectedFile = (File) tn.getUserObject();
        } else {
            selectedFile = null;
        }
        return selectedFile;
    }
    
    public void setSelectedFile(File f) {
         this.selectedFile = f;
         this.refreshGui();
    }
    
    
    
    public void updateGuiButtons() {
        File f = this.getSelectedFile();
        boolean b = (f != null && f.isDirectory());
        this.newFolderB.setEnabled(b);
        this.newEntryB.setEnabled(b);
        this.removeB.setEnabled(f != null);
    }
    
    private void initGui() {
        tree = new JTree();
        tree.setCellRenderer(new LibraryFileCellRenderer());
        this.setLayout(new BorderLayout());
        JPanel lblp = new JPanel();
        lblp.add(new JLabel("Library Entries"));
        JPanel lblcp = new JPanel();
        lblcp.setLayout(new BorderLayout());
        
        lblcp.add(lblp, BorderLayout.WEST);
        
        this.add(lblcp, BorderLayout.NORTH);
        JScrollPane jsp = new JScrollPane(tree);
        this.add(jsp, BorderLayout.CENTER);
        
        JPanel btnsPanel = new JPanel();
        btnsPanel.add(this.newFolderB);
        btnsPanel.add(this.newEntryB);
        btnsPanel.add(this.removeB);
        this.add(btnsPanel, BorderLayout.SOUTH);
    }
    
    private void hookupGui() {
        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse) {
                
                updateGuiButtons();
            }
        });
        this.removeB.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent ae) {
               doRemove();
           } 
        });
        this.newEntryB.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent ae) {
               doAddNewEntry();
           } 
        });
        this.newFolderB.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent ae) {
               doAddFolder();
           } 
        });
    }
    
    private void refreshGui() {
        DefaultMutableTreeNode root = this.getTreeNodeFor(new File(defaultDirectory, LibraryData.PATH_IN_LIB));
        DefaultTreeModel dtm = new DefaultTreeModel(root);
        this.tree.setModel(dtm);
         
        TreePath tp = getTreePathForSelectedFile();
        if (tp != null) {
            tree.setSelectionPath(tp);
        }
    }
    
    private TreePath getTreePathForSelectedFile() {
        if (this.treeNodeToSelect != null) {
            return new TreePath(this.treeNodeToSelect.getPath());
            
            
        }
        return null;
    }
  
    
   
    
    public void doRemove() {
        File f = this.getSelectedFile();
        int confirm = JOptionPane.showConfirmDialog(this, "Really delete " + this.getSelectedFile().getName() + " ?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            f.delete();
            this.refreshGui();
        }
                             
        
        
    }
    
    public void doAddNewEntry() {
        File f = this.getSelectedFile();
        //String value = JOptionPane.showInputDialog( this, "Enter the short .NET Type name (e.g. 'Object')", "Add New Entry", JOptionPane.PLAIN_MESSAGE);
        
        NewEntryDialog ned = new NewEntryDialog((JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this));
        ned.setVisible(true);
        String value = ned.getName();
        if (value != null && !"".equals(value)) {
            File current = this.getSelectedFile();
            this.writeDefaultEntryFile(value, current, ned.isEvent());                 
        }
        
    }
    
    private void writeDefaultEntryFile(String name, File directory, boolean isEvent) {
        Entry e = null;
        if (isEvent) {
            e = Entry.createDefaultEventEntry();
        } else {
            e = Entry.createDefaultEntry();
        }
        String pathToRemove = this.defaultDirectory + LibraryData.PATH_IN_LIB;
        String namespaceFilepath = directory.toString().substring(pathToRemove.length(), directory.toString().length());
        String namespaceName = Util.replaceString(namespaceFilepath, File.separator, ".");
        if (namespaceName.startsWith(".")) {
            namespaceName = namespaceName.substring(1, namespaceName.length());
        }
        String dnTypename = null;
        if (!namespaceName.equals("")) {
            dnTypename = namespaceName + "." + name;
        } else {
            dnTypename = name;
        }
        
        if (e.isEvent()) {
            e.setEventName(dnTypename);
        } else {
            e.setDNTypename(dnTypename);
        }
        
        File ff = new File(directory, name + ".xml");
        try {
            FileOutputStream fos = new FileOutputStream(ff);
                
            e.write(fos);
            fos.close();
            this.selectedFile = ff;
            this.refreshGui();    
         } catch (Throwable t) {
             t.printStackTrace();
             JOptionPane.showMessageDialog(this, "Writing " + ff + " failed.");
         }
     }
    
    public void doAddFolder() {
        String value = JOptionPane.showInputDialog( this, "Enter the folder name", "Add Namespace", JOptionPane.PLAIN_MESSAGE);
        if (value != null && !"".equals(value)) {
            File current = this.getSelectedFile();
            File newFile = new File(current, value);
            try {
                newFile.mkdir();
                this.selectedFile = newFile;
                this.refreshGui();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            
        }
        
        
    }
    
    private DefaultMutableTreeNode getTreeNodeFor(File f) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(f);
        MyFileFilter mff = new MyFileFilter();
        //mff.addExtension(".java");
        mff.addExtension(".xml");
        java.util.List files = this.getOrderedFiles(f, mff);
       
        for (int i = 0; i < files.size(); i++ ) {
            
            String next = (String) files.get(i);
            if (!next.equals("CVS")) {
                File nextFile = new File(f, next);
            
                DefaultMutableTreeNode nextChild = this.getTreeNodeFor(nextFile);
                
                node.add(nextChild);
                if (nextFile.equals(this.selectedFile)) {
                    this.treeNodeToSelect = nextChild;
                }
            }
        }
        return node;
    }
    
    private java.util.List getOrderedFiles(File f, FilenameFilter ff) {
        java.util.Set subDirs = new TreeSet();
        java.util.Set subFiles = new TreeSet();
        String[] files = f.list(ff);
        java.util.List all = new ArrayList();
        if (files == null) {
            return all;
        }
        for (int i = 0; i < files.length; i++) {
            String next = files[i];
            File nextFile = new File(f, next);
            if (nextFile.isDirectory()) {
                subDirs.add(next);
            } else {
                subFiles.add(next);
            }
        }
        all.addAll(subDirs);
        all.addAll(subFiles);
        return all;
    }
}



class LibraryFileCellRenderer extends DefaultTreeCellRenderer {
    
    public Component getTreeCellRendererComponent(JTree tree,
                                        Object value,
                                        boolean sel,
                                        boolean expanded,
                                        boolean leaf,
                                        int row,
                                        boolean hasFocus) {
        
        
        Component c =  super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        //System.out.println(c.getClass());
        //System.out.println(value.getClass());
        if (value instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode dtn = (DefaultMutableTreeNode) value;
            Object data = dtn.getUserObject();
            //System.out.println(data.getClass());
            if (data instanceof File) {
                DefaultTreeCellRenderer dtcr = (DefaultTreeCellRenderer) c;
                File f = (File) data;
                dtcr.setText(f.getName());
                if (f.isDirectory() && f.list().length == 0) {
                    dtcr.setIcon(dtcr.getClosedIcon());
                }
            }
        }
        return c;
    }

}

 