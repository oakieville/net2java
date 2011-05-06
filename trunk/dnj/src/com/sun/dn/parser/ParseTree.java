
 /* 
 * Copyright (c) 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *  
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *  
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *  
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *  
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package com.sun.dn.parser;

import java.util.*;
import com.sun.dn.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.parser.statement.*;
import com.sun.dn.util.*;
import com.sun.dn.Library;

	/** Data structure containing a .NET program
	*@author danny.coward@sun.com
	*/	

public class ParseTree implements InterpretationContext {
	List optionStatements = new ArrayList();
	Object mainProcedure;
	List namespaceStatements;
	List metaClasses = new ArrayList();
	List enumerations = new ArrayList();
        List delegates = new ArrayList();
        List untranslatedStatements = new ArrayList();
	Library library;
	AssemblyInformation ai;
        List lineLabels = new ArrayList();
	TranslationPolicy policy;
	TranslationReport report = new TranslationReport();

	public ParseTree(Library library, TranslationPolicy policy) {
		this.library = library;
		this.policy = policy;
	}

	public TranslationPolicy getPolicy() {
		return this.policy;
	}

	public TranslationReport getTranslationReport() {
		return this.report;
	}
        
        public void addLineLabel(String lineLabel) {
            this.lineLabels.add(lineLabel);
        }
        
        public List getLineLabels() {
            return this.lineLabels;
        }

	public void addEnumeration(EnumStatement eStatement) {
		this.enumerations.add(eStatement);
	}
        
        public void addDelegate(DelegateStatement ds) {
            
            this.delegates.add(ds);
        }
        
        public DelegateStatement getDelegateFor(String name) {
           
            for (Iterator itr = this.getDelegates().iterator(); itr.hasNext();) {
                DelegateStatement ds = (DelegateStatement) itr.next();
                if (ds.getName().equals(name)) {
                    return ds;
                }
            }
            return null;
        }
        
        public List getDelegates() {
            List l = new ArrayList();
            l.addAll(this.delegates);
            for (Iterator itr = this.metaClasses.iterator(); itr.hasNext();) {
                MetaClass mc = (MetaClass) itr.next();
                List dels = mc.getDelegates();
                l.addAll(dels);
            }
            return l;
        }
        
        public void addUntranslatedStatement(UntranslatedStatement us) {
            this.untranslatedStatements.add(us);
        }
        
        public List getUntranslatedStatements() {
            return this.untranslatedStatements;
        }

	public void setAssemblyInformation(AssemblyInformation ai) {
		this.library.getLibraryData().setAssemblyInformation(ai);
	}

	public List getEnumerations() {
		return this.enumerations;
	}
 
        
        public List getMetaClassesForName(String name) {
            List l = new ArrayList();
            for (Iterator itr = this.metaClasses.iterator(); itr.hasNext();) {
                MetaClass mc = (MetaClass) itr.next();
                if (mc.getFQName().equals(name)) {
                    l.add(mc);
                }
            }
            return l;
        }
        
        public void resolvePartialMetaClassAndNamingConflicts() {
            this.doMergePartialClasses();
            this.doRenamesForNamingConflicts();
        }
        
        private void doMergePartialClasses() {
            List newMetaClasses = new ArrayList();
            List alreadyAggregated = new ArrayList();
            for (Iterator itr = this.metaClasses.iterator(); itr.hasNext();) {
                MetaClass mc = (MetaClass) itr.next();
                //System.out.println("Checking: " + mc.getName() + " " + mc.isPartial());
                if (!alreadyAggregated.contains(mc)) {
                    List partials = this.getMetaClassesForName(mc.getFQName());
                    alreadyAggregated.addAll(partials);
                    //System.out.println("Partials: " + partials);
                    for (Iterator itrr = partials.iterator(); itrr.hasNext();) {
                        MetaClass nextPartial = (MetaClass) itrr.next();
                        if (nextPartial != mc) {
                            mc.addMetaClass(nextPartial);
                        }
                    }
                    mc.setPartial(false);
                   newMetaClasses.add(mc); 
                }
                
            }
            this.metaClasses = newMetaClasses;
        }
        
        private void doRenamesForNamingConflicts() {
            for (Iterator itr = this.metaClasses.iterator(); itr.hasNext();) {
                MetaClass mc = (MetaClass) itr.next();
                List allByName = this.getMetaClassesForName(mc.getFQName());
                if (allByName.size() > 1) {
                    allByName.remove(mc);
                    for (Iterator itrr = allByName.iterator(); itrr.hasNext();) {
                        MetaClass nextMc = (MetaClass) itrr.next();
                        String oldName = nextMc.getFQName();
                        String newName = this.createNewMetaClassName(nextMc);
                        nextMc.setName(newName);
                        TranslationWarning trw = new TranslationWarning(oldName, "Type: " + oldName + " was renamed to " + newName + " due to a naming conflict.");
                        this.getParseTree(this).getTranslationReport().addTranslationWarning(trw);
                    }
                }
            }
        }

	public void addMetaClass(MetaClass mc) {
            String oldName = mc.getFQName();
            
            
	    this.metaClasses.add(mc);
	}

	public void addOptionStatement(VBOptionStatement os) {
		this.optionStatements.add(os);
	}

	public List getMetaClasses() {
		return metaClasses;
	}
        
       

	public void addImportsStatement(ImportsStatement is) {
		this.library.getLibraryData().addNamespace(is.getNamespace());
	}

	public List getImportedNamespaces() {
		return this.library.getLibraryData().getNamespaces();
	}

	public List getOptionStatements() {
		return optionStatements;
	}

	public List getVariables() {
		return new ArrayList();
	}

	public DNVariable getVariable(String name) {
		return null;
	}

	public Library getLibrary() {
		return this.library;
	}

	public InterpretationContext getParent() {
		return null;
	}
        
        public Signature resolveMethod() {
            return null;
        }
        
        
        public String createNewMetaClassName(MetaClass mc) {
            boolean notOk = true;
            int i = 0;
            while(notOk) {
                String proposedName = mc.getFQName() + i;
                if (this.containsMetaClassName(proposedName)) {
                    i++;
                } else {
                    notOk = false;
                    return mc.getName() + i;
                }
            }
            return mc.getName();
        }
        
        
        private boolean containsMetaClassName(String name) {
            for (Iterator itr = this.metaClasses.iterator(); itr.hasNext();) {
                MetaClass nextMc = (MetaClass) itr.next();
                if (nextMc.getFQName().equals(name)) {
                    return true;
                }
            }
            return false;
        }
        
        public Signature resolveMethod(DNType calleeType, String mName, List mArgs) {
            //System.out.println("resolveMethod on " + calleeType + " of name " + mName);
            for (Iterator itr = this.metaClasses.iterator(); itr.hasNext();) {
		MetaClass mc = (MetaClass) itr.next();
		Signature sig = mc.resolveMethod(calleeType, mName, mArgs);
		if (sig != null) {
                    //System.out.println("found it on  " + mc + " sig " + sig);
                    return sig;
		}
            }
            //System.out.println("not found");
            return null;
	}
        
        public List findVariablesFor(DNType type, InterpretationContext context) {
            List l = new ArrayList();
            MetaClass mc = this.findMetaClassFor(type);
            if (mc != null) {
                l.addAll(mc.getVariables());
            } else {
                //System.out.println("Meta class for " + type + " was null");
            }
            // dannyc: now you should look in the Library for superclasses with properties
            return l;
        }
        
        public MetaClass findMetaClassFor(DNType type) {
            for (Iterator itr = this.metaClasses.iterator(); itr.hasNext();) {
		MetaClass mc = (MetaClass) itr.next();
                if (mc.getDNType().equals(type)) {
                    return mc;
                }
            }
            return null;
        }

	public MetaClass getMetaClass() {
		if (true) {
			throw new RuntimeException("I have no meta class");
		} else {
			return null;
		}
	}
        
        public static void reportRuntimeException(RuntimeException re, String rawStatementString, String pathname) {
		System.out.println(" ____ Parser error____");
		System.out.println("  Hi. I was parsing [ " + rawStatementString + " ] in " + pathname + "\n");
		System.out.println("  If this code is syntactically correct, then unfortuntately");
		System.out.println("  its likely that this is my mistake or a syntax case I don't"); 
		System.out.println("  support yet.");
		System.out.println("\n Here is the stack trace: ");
		re.printStackTrace();
		if (!Interpreter.isGUI()) {				
                    System.exit(-1);
		} else {
                    throw re;
		}
	}
        
        public static void handleTypeResolveException(ParseTree tree, String offendingCode, Throwable th, boolean parseTime) {
                TypeResolveException tre;
                if (th instanceof TypeResolveException) {
                    tre = (TypeResolveException) th;
                            
                } else {
                    tre = new TypeResolveException(offendingCode, th.getMessage());            
                }
		if (tree.getPolicy().isOfType(TranslationPolicy.GENTLE)) {
                    tree.getTranslationReport().addTypeResolveException(tre);	
		} else {
                        th.printStackTrace();
			tree.getPolicy().handleTypeResolveException(tre);
			if (!Interpreter.isGUI()) {				
				System.exit(-1);
			} else {
				throw tre;
			}
		}
	}
        
        public static ParseTree getParseTree(InterpretationContext ic) {
		boolean found = false;
		InterpretationContext c = ic;
		while (!found) {
			//System.out.println(c);
			if (c instanceof ParseTree) {
				return (ParseTree) c;
			}
			c = c.getParent();
		}
		throw new RuntimeException("Statement not in tree");
	}



}
 