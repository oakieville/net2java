/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.IO;

import java.io.*;
import java.util.*;

public class StreamSupport {
        InputStream is;
        OutputStream os;
        
        public StreamSupport(InputStream is) {
            this.is = is;
        }
        
        public StreamSupport(OutputStream os) {
            this.os = os;
        }

	public void close() {
            
            if (true) throw new RuntimeException("StreamSupport.close");
            
        }	
}
