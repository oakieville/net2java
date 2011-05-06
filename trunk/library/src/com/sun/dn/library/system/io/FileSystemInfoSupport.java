package com.sun.dn.library.System.IO;

import java.io.File;
import java.util.*;

public class FileSystemInfoSupport extends File {

	public FileSystemInfoSupport(String pathname) {
		super(pathname);
	}

	public String getFullName() {
		return super.toString();
	}

	public String getNameString() {
		return super.getName();
	}

	public String getExtension() {
		String ext = "";
		if (this.getNameString().indexOf(".") != -1) {
			ext = this.getNameString().substring(this.getNameString().indexOf("."), 
									this.getNameString().length());
		}
		return ext;
	}

	public Date getLastAccessTime() {
		return new Date(this.lastModified());
	}

	public Date getLastWriteTime() {
		return new Date(this.lastModified());
	}

	public Date getCreationTime() {
		return new Date();
	}

	public FileAttributesSupport getAttributes() {
		return new FileAttributesSupport(this);
	}




	
}
