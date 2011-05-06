/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.IO;

import java.io.*;

public class StreamWriterSupport {
        private BufferedWriter writer;
        
	public StreamWriterSupport(StreamSupport stream) {
		writer = new BufferedWriter(new OutputStreamWriter(stream.os));
	}
        
        public StreamWriterSupport(FileStreamSupport fss) { 
            try {
                writer = new BufferedWriter(new FileWriter(fss.filename));
            } catch (IOException ioe) {
                throw new RuntimeException(ioe.getMessage());
            }
        }
        
	public void write(String toWrite) {
            try {
		this.writer.write(toWrite, 0, toWrite.length());
            } catch (Throwable t) {
                System.out.println(t.getMessage());
                throw new RuntimeException(t.getMessage());
            }
	}
        
        public void writeLine(String toWrite) {
            this.write(toWrite + "\n");
	}
        
        public void writeLineInt(int i) {
            this.write(i + "\n");
	}
        
        public void close() {
            try {
  		this.writer.close(); 
            } catch (Throwable t) {
                System.out.println(t.getMessage());
                throw new RuntimeException(t.getMessage());
            }
        }
}
