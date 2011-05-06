
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

/**
 * GUI for the .NET Interpreter.
 * @author Danny  Coward
 */
public class InterpreterWindow extends JFrame {
	JFileChooser chooser = new JFileChooser(); 
	JButton browseButton = new JButton("Browse...");
	JButton translateButton = new JButton("Translate");
	JTextField dnProjDir = new JTextField();
	JList vbFilesList = null;
	JScrollPane flsp;
	JRadioButton cmdRB = new JRadioButton("Command Line");
	JRadioButton guiRB = new JRadioButton("GUI");
	JRadioButton webRB = new JRadioButton("Web");
        JRadioButton vbRB = new JRadioButton("Visual Basic");
	JRadioButton csRB = new JRadioButton("C#");
	JComboBox mainClassnameCB = new JComboBox();
	JCheckBox policyCB = new JCheckBox("Translator is forgiving of unknown .NET APIs");
	JList javaFilesList = new JList();
	JTextArea reportTA = new JTextArea();

	JPanel dnProjectPanel = null;
	JPanel resultsPanel = null;
	JPanel currentPanel = null;

	File dnProjDirFile = null;
	java.util.List vbFiles = new ArrayList();
	String projectType = JavaProgram.CMDLINE_TYPE;
	boolean useStrict = false;
	String mainClassname = null;	
	

	static Dimension BORDER = new Dimension(10, 10);

	String libraryPath = "";
	String writeDirectory = "tmp";
	String projectName = "Unknown Project";

		

	public InterpreterWindow(String libraryPath, String writeDirectory, String projectName) {
		this.addWindowListener(new ExitListener());
		if (projectName == null) {
			this.setTitle("Translate .NET Project, destination " + writeDirectory);
		} else {
			this.setTitle("Import .NET Project into " + projectName);
		}
		this.libraryPath = libraryPath;
		this.writeDirectory = writeDirectory;
		this.projectName = projectName;
		this.init();
		
		Debug.init("");	
    }

	public void setCurrentPanel(JPanel p) {
		if (currentPanel != null) {
			this.getContentPane().remove(currentPanel);
		}
		this.getContentPane().add(p);
		currentPanel = p;
		this.repaint();
		//this.setVisible(false);
		this.setVisible(true);

		this.refreshGuiComponents();
		this.getContentPane().doLayout();
		
	}

	private JPanel getResultsPanel() {
		if (resultsPanel != null) {
			return resultsPanel;
		}
		resultsPanel = new JPanel();
		
		JPanel pp = new JPanel();
		JPanel p = null;

		pp.setLayout(new BoxLayout(pp , BoxLayout.Y_AXIS));
		JScrollPane jsp = null;

		// ________ File List
		p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		jsp = new JScrollPane(javaFilesList);
		p.add(new JLabel("Java files created"));
		p.add(jsp);
		pp.add(Box.createRigidArea(BORDER));
		pp.add(p);
	
		// _________ Error report
		p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(new JLabel("Translation report"));
		jsp = new JScrollPane(reportTA);
		p.add(jsp);
		pp.add(Box.createRigidArea(BORDER));
		pp.add(p);

		// _______ Go Back
		p = new JPanel();
		p.setLayout(new FlowLayout());
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				goBack();
			}
		});
		JButton doneButton = new JButton("Done");
		doneButton .addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});

		p.add(backButton);
		p.add(doneButton);

		//pp.add(Box.createRigidArea(BORDER));
		pp.add(p);

		resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.X_AXIS));
		resultsPanel.add(Box.createRigidArea(BORDER));
		resultsPanel.add(pp);
		resultsPanel.add(Box.createRigidArea(BORDER));

		return resultsPanel;
	}

	private JPanel getVBProjectPanel() {
		if (dnProjectPanel != null) {
			return dnProjectPanel;
		}
		String[] dummy = {"[no source files]"};
		vbFilesList = new JList(dummy );
		JPanel p = null;
		JPanel pp = null;
		dnProjectPanel = new JPanel();
		
		dnProjectPanel.setLayout(new BoxLayout(dnProjectPanel, BoxLayout.X_AXIS));
		dnProjectPanel.add(Box.createRigidArea(BORDER));
		

		JPanel cp = new JPanel();
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
		dnProjectPanel.add(cp);
		dnProjectPanel.add(Box.createRigidArea(BORDER));


		// ____________Directory Chooser
		p = new JPanel();
		JLabel lbl = new JLabel("Directory of .NET Project");
		p.setLayout(new BorderLayout());
		p.add(lbl, BorderLayout.NORTH);
		p.add(dnProjDir, BorderLayout.CENTER);
		p.add(browseButton, BorderLayout.EAST);
		dnProjDir.setEditable(false);
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
                		doBrowse();
            	}
                });
		cp.add(Box.createRigidArea(BORDER));
		cp.add(p);


		// _______________Source File List
		p = new JPanel();
		p.setLayout(new BorderLayout());
		lbl = new JLabel("Source files");
		flsp = new JScrollPane(vbFilesList);
		p.add(lbl, BorderLayout.NORTH);
		p.add(flsp, BorderLayout.CENTER);
		cp.add(Box.createRigidArea(BORDER));
		cp.add(p);

                
                // _______________Language Type Radio Buttons
		p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		pp = new JPanel();
		ButtonGroup bg = new ButtonGroup();
		bg.add(vbRB);
		bg.add(csRB);
		pp.setLayout(new BoxLayout(pp, BoxLayout.Y_AXIS));
		pp.add(new JLabel("Language"));
		pp.add(vbRB);
		pp.add(csRB);
                vbRB.setSelected(true);
                pp.add(Box.createHorizontalGlue());
                p.add(pp);
                cp.add(Box.createRigidArea(BORDER));
                cp.add(p);

		// _______________Project Type Radio Buttons
		p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		pp = new JPanel();
		bg = new ButtonGroup();
		bg.add(cmdRB);
		bg.add(guiRB);
		bg.add(webRB);
		pp.setLayout(new BoxLayout(pp, BoxLayout.Y_AXIS));
		pp.add(new JLabel("Project Type"));
		pp.add(cmdRB);
		pp.add(guiRB);
		pp.add(webRB);
	
		// ok this is cheating
		JLabel jl = new JLabel("Main class");
		pp.add(Box.createRigidArea(BORDER));
		pp.add(jl);

		ActionListener rbListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				changeProjectType();
			}
		};
		cmdRB.addActionListener(rbListener);
		guiRB.addActionListener(rbListener);
		webRB.addActionListener(rbListener);
		
		
		p.add(pp);
		p.add(Box.createHorizontalGlue());
		cp.add(Box.createRigidArea(BORDER));
		cp.add(p);

		// ________________Main class name chooser
		p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		pp = new JPanel();
		pp.setLayout(new BoxLayout(pp, BoxLayout.Y_AXIS));
		
		//pp.add(jl);
		pp.add(mainClassnameCB);
		
		mainClassnameCB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					changeMainClassname();
				}
		});
		p.add(pp);
		p.add(Box.createHorizontalGlue());
		//cp.add(Box.createRigidArea(BORDER));
		cp.add(p);


		// ____________Policy Button
		p = new JPanel();
		policyCB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					changePolicy();
				}
			});


		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(policyCB);
		p.add(Box.createHorizontalGlue());
		cp.add(Box.createRigidArea(BORDER));
		cp.add(p);


		// ________________Translate Button panel
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(translateButton);
		translateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
                		doTranslate();
            	}
       	});
		cp.add(Box.createRigidArea(BORDER));
		cp.add(Box.createRigidArea(BORDER));
		cp.add(p);
		cp.add(Box.createRigidArea(BORDER));
		return dnProjectPanel;

	}

	private void init() {
		this.setCurrentPanel(this.getVBProjectPanel());
	}

	public void refreshGuiComponents() {
		translateButton.setEnabled(dnProjDirFile != null);
		cmdRB.setEnabled(dnProjDirFile != null);
		guiRB.setEnabled(dnProjDirFile != null);
		webRB.setEnabled(dnProjDirFile != null);
                vbRB.setEnabled(dnProjDirFile != null);
                csRB.setEnabled(dnProjDirFile != null);
		policyCB.setEnabled(dnProjDirFile != null);

		if (dnProjDirFile != null) {
			this.dnProjDir.setText(dnProjDirFile.toString());
		} else {
			this.dnProjDir.setText("[Choose directory...]");
		}
		policyCB.setSelected(!useStrict);
		this.refreshFilesList();
		this.refreshProjectTypeComponents();
	}

	public void refreshProjectTypeComponents() {
		cmdRB.setSelected(projectType.equals(JavaProgram.CMDLINE_TYPE));
		guiRB.setSelected(projectType.equals(JavaProgram.GUI_TYPE));
		webRB.setSelected(projectType.equals(JavaProgram.WEB_TYPE));

	}
    
    	public void doBrowse() {
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (dnProjDirFile != null) {
			chooser.setSelectedFile(dnProjDirFile);
		}
		int returnVal = chooser.showOpenDialog(this); 
		if(returnVal == JFileChooser.APPROVE_OPTION) { 
			dnProjDirFile = chooser.getSelectedFile();
			vbFiles = Util.getAllEntryFilesIn(dnProjDirFile, ".vb");
                        vbFiles.addAll(Util.getAllEntryFilesIn(dnProjDirFile, ".cs"));
		} 
		this.refreshGuiComponents();

    	}

	public void changeProjectType() {
		if (cmdRB.isSelected()) {
			projectType = JavaProgram.CMDLINE_TYPE;
		} else if (guiRB.isSelected()) {
			projectType = JavaProgram.GUI_TYPE;
		} else if (webRB.isSelected()) {
			projectType = JavaProgram.WEB_TYPE;
		}  
		
	}

	public void changePolicy() {
		useStrict = !policyCB.isSelected();
		//System.out.println("US = " + useStrict);
	}

	public void changeMainClassname() {
		mainClassname = (String) mainClassnameCB.getSelectedItem();
		//System.out.println("MC = " + mainClassname);
	}

	private java.util.List getSourceFilenames() {
		java.util.List l = new ArrayList();
		for (Iterator itr = vbFiles.iterator(); itr.hasNext();) {
			File next = (File) itr.next();
			l.add(next.toString());
		}
		return l;
	}

	private java.util.List getClassnames() {
		java.util.List l = new ArrayList();
		for (Iterator itr = vbFiles.iterator(); itr.hasNext();) {
			File next = (File) itr.next();
			String fn = next.getName();
			if ((fn.endsWith(".vb") || fn.endsWith(".cs")) && !fn.startsWith("AssemblyInfo")) {
				l.add(fn.substring(0, fn.length()-3));
			}
		}
		return l;

	}
        
        private String getLanguage() {
            if (vbRB.isSelected()) {
                return Interpreter.VB_LANGUAGE;
            } else if (csRB.isSelected()) {
                return Interpreter.CS_LANGUAGE;
            } else {
                throw new RuntimeException("Incoherent state");   
            }
        }

	public void doTranslate() {
		try {
			Interpreter.setGUI(true);
			Interpreter interpreter = new Interpreter(System.out, this.getLanguage());
			String policyType = null;
			if (useStrict) {
				policyType = TranslationPolicy.STRICT;
			} else {
				policyType = TranslationPolicy.GENTLE;
			}

			//System.out.println(this.projectType);
			//System.out.println(policyType);
			//System.out.println(this.mainClassname);
			


			JavaProgram jp = interpreter.createJavaProgram(this.getSourceFilenames(),
										this.libraryPath ,
										this.mainClassname,
										this.projectType, 
										policyType);
			int value = interpreter.writeJavaProgram(jp, this.writeDirectory);
			DefaultListModel dlm = new DefaultListModel();
			for (Iterator itr = jp.getFiles().iterator(); itr.hasNext();) {
				File next = (File) itr.next();
				dlm.addElement(next.getName());
			}
			this.javaFilesList.setModel(dlm);
		

			this.setCurrentPanel(this.getResultsPanel());
			TranslationReport report = jp.getTranslationReport();
			if (report.hasTypeResolveErrors() || report.hasTranslationWarnings()) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				java.util.List errors = new ArrayList();
				errors.addAll(report.getTypeResolveExceptions());
				report.doReport(errors, report.getTranslationWarnings(), baos );
				String reportS = new String(baos.toByteArray());
				reportTA.setText(reportS);
			} else {
				reportTA.setText("There were no unknown APIs");
			}
		} catch (Throwable t) {
			System.out.println("Catching " + t);
			DefaultListModel dlm = new DefaultListModel();
			dlm.addElement("[No files were written]");
			this.javaFilesList.setModel(dlm);
			String errorText = "Unrecoverable error. \n";
			if (t instanceof TypeResolveException) {
				TypeResolveException tre = (TypeResolveException) t;
				errorText = errorText + "Error: " + tre.getMessage() + "\n";
				errorText = errorText + "Code: " + tre.getCode() + "\n";
				errorText = errorText + "Containing Statement: " + tre.getContainingStatement() + "\n";
			} else {
				errorText = errorText + t.getMessage();
			}
			reportTA.setText(errorText);
			t.printStackTrace();
		}

	}

	public void goBack() {
		this.setCurrentPanel(this.getVBProjectPanel());
	}

	public void refreshFilesList() {
		if (dnProjDirFile == null) {
			flsp.setEnabled(false);
			mainClassnameCB.removeAllItems();
			mainClassnameCB.setEnabled(false);
		} else {
			flsp.setEnabled(true);
			DefaultListModel dlm = new DefaultListModel();
			for (Iterator itr = vbFiles.iterator(); itr.hasNext();) {
				File next = (File) itr.next();
				String fn = next.toString().substring(dnProjDirFile.toString().length(), next.toString().length());
				dlm.addElement(fn);
			}	
			vbFilesList.setModel(dlm);
			mainClassnameCB.removeAllItems();
			mainClassnameCB.setEnabled(true);
			for (Iterator itr = this.getClassnames().iterator(); itr.hasNext();) {
				String next = (String) itr.next();
				mainClassnameCB.addItem(next);
			}
			mainClassnameCB.setSelectedItem(mainClassname);
		}
		
	}
    
    
}

class ExitListener extends WindowAdapter {
  	public void windowClosing(WindowEvent event) {
		event.getWindow().setVisible(false);
    		System.exit(0);
  	}
}

 