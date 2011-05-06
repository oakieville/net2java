/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
/*
 * XmlNodeListSupport.java
 *
 * Created on February 13, 2006, 7:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.sun.dn.library.System.Xml;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import com.sun.dn.util.*;

public class XmlNodeListSupport {
    NodeList nl;
    
    /** Creates a new instance of XmlNodeListSupport */
    public XmlNodeListSupport(NodeList nl) {
        this.nl = nl;
    }
    
    public int getCount() { 
        return this.nl.getLength();
    }
    
    public XmlNodeSupport item(int i) {
        if (this.nl.item(i) == null) {
            return null;
        }
        return new XmlNodeSupport(this.nl.item(i));
        
    }
    
}