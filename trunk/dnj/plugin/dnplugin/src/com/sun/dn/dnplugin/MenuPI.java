/*
 * MenuPI.java
 *
 * Created on July 18, 2005, 8:36 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.sun.dn.DNPlugin;

import org.openide.util.actions.*;
import org.netbeans.api.project.ui.*;
import org.netbeans.api.project.*;
import com.sun.dn.gui.*;
import javax.swing.*;
import org.openide.filesystems.FileObject;
import java.io.File;



/**
 *
 * @author Danny  Coward
 */
public class MenuPI extends CallableSystemAction {
    
    /** Creates a new instance of MenuPI */
    public MenuPI() {
    }

    public void performAction() {
        
        ProjectSelector ps = new ProjectSelector();
        ps.setBounds(50,50,300,450);
        ps.setVisible(true);
        
        Project p = ps.getProject();
        if (p != null) {
            FileObject fo = p.getProjectDirectory();
            String foPath = fo.getPath();
            System.out.println(foPath);
            String sourcePath = foPath + File.separator + "src";
            ProjectInformation pi = ProjectUtils.getInformation(p);
            String projectName = pi.getDisplayName();
            JFrame f = new InterpreterWindow("src/com/sun/dn/library", sourcePath, projectName);
            f.setBounds(50,50,500,450);
            f.setVisible(true);
        }
    }

    public String getName() {
        return "Import Project";
    }

    public org.openide.util.HelpCtx getHelpCtx() {
        return null;
    }
    
}