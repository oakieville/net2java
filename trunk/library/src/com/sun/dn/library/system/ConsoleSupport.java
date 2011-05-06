/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/


package com.sun.dn.library.System;

import java.io.*;

/**
 *
 * @author localuser
 */
public class ConsoleSupport {
    
    public static int read() {
        try {
            int i = System.in.read();
            return i;
            
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }
    }
    
     public static String readLine() {
         try {
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(isr);
            String line = null;
            while ((line = reader.readLine()) != null) {
                return line;
            }
            return "";
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }
       
    }
}

