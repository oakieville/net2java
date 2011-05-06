/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/
package com.sun.dn.library.System.Text;

public class StringBuilderSupport {

	public StringBuilderSupport(String s) {
		System.out.println("StringBuilder Support constrcutor woith string");
	}

	public StringBuilderSupport insert(int i, String s) {
		System.out.println("StringBuidler insert int string");
		return this;
	}

	public StringBuilderSupport remove(int i, int j) {
		System.out.println("StringBuidler remove int int");
		return this;
	}

	public StringBuilderSupport replace(String s, String t) {
		System.out.println("StringBuidler repalce string string");
		return this;
	}

	public StringBuilderSupport appendFormat(String s, Object o, Object oo) {
		System.out.println("StringBuidler appendFormat string object object");
		return this;
	}





	

}
