/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/

package com.sun.dn.library.System.IO;

import java.io.*;

public class StreamReaderSupport {
        BufferedReader reader = null;

	public StreamReaderSupport(StreamSupport stream) {
                this.reader = new BufferedReader(new InputStreamReader(stream.is));
	}
        
        public StreamReaderSupport(FileStreamSupport fss) {
            try {
                this.reader = new BufferedReader(new FileReader(fss.filename));
            } catch (IOException ioe) {
                throw new RuntimeException(ioe.getMessage());
            }
        }
       
        
        public String readLine() {
            try {
                return this.reader.readLine();
            } catch (Throwable t) {
                System.out.println(t);
                throw new RuntimeException(t.getMessage());
            }
        }
        
        public void close() {
            try {
                this.reader.close();
            } catch (Throwable t) {
                System.out.println(t);
                throw new RuntimeException(t.getMessage());
            }          
        }
}
