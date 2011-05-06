/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Net;

import java.io.*;
import java.util.*;
import com.sun.dn.library.System.IO.StreamSupport;
import com.sun.dn.library.System.IO.StreamReaderSupport;
import java.net.*;

public class WebClientSupport {

	public WebClientSupport() {
                System.out.println("Hello from here");
		//if (true) throw new RuntimeException("new web client");
	}
        
        public StreamSupport openRead(String s) { 
            
            try {
                URL url = new URL(s);
                InputStream is = url.openStream();
                System.out.println("Opened stream on " + s);
                StreamSupport ss = new StreamSupport(is);
                return ss;
            } catch (Throwable t) {
                System.out.println(t);
                throw new RuntimeException(t.getMessage());
            }
            
        }
        
        // ok, I know
        public StreamSupport openWrite(String address, String method) {  
            try {
                if (method.equals("PUT")) {
                    File f = new File(address);
                    FileOutputStream fos = new FileOutputStream(f);
                    return new StreamSupport(fos); 
                } else {
                    System.out.println("System.Net.WebClient.openWrite() Implementation Issue");
                    throw new RuntimeException("");
                }
                
            } catch (Throwable t) {
                System.out.println(t);
                throw new RuntimeException(t.getMessage());
            }
        }

}
