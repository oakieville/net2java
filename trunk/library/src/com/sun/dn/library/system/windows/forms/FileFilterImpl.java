/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/


package com.sun.dn.library.System.Windows.Forms;

import javax.swing.filechooser.FileFilter;
import java.util.*;
import java.io.*;

public class FileFilterImpl extends FileFilter {
    List descriptions = new ArrayList();
    List filePatterns = new ArrayList();
    
    /** txt files (*.xml)|*.xml|All files (*.*)|*.* */
    public FileFilterImpl(String dnFilterString) {
        StringTokenizer stk = new StringTokenizer(dnFilterString, "|");
        while(stk.hasMoreTokens()) {
            String next = stk.nextToken();
            descriptions.add(next);
            if (stk.hasMoreTokens()) {
                String nextPattern = stk.nextToken().trim();
                //System.out.println(nextPattern);
                String fileending = nextPattern.substring(nextPattern.indexOf("."), nextPattern.length());
                filePatterns.add(fileending);
            }
        }
    }
    
    public boolean accept(File f){
        if (f.isDirectory()) {
            return true;
        }
        String filename = f.toString();
        for (Iterator itr = filePatterns.iterator(); itr.hasNext();) {
            String next = (String) itr.next();
            if (filename.endsWith(next)) {
                return true;
            }
            
        }
        return false;
    }
    
    public String getDescription() {
        String description = "";
       for (Iterator itr = descriptions.iterator(); itr.hasNext();) {
           String next = (String) itr.next();
           description = description + next;
           if (itr.hasNext()) {
               description = description + ", ";
           }
       }
       return description;
    }
    
    
    
}

