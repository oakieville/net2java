
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
import com.sun.dn.library.LibraryData;

	/** The superclass for NET modules interfaces and classes.
	* @author danny.coward@sun.com
	*/

public abstract class MetaClass extends StatementAdapter implements InterpretationContext {
        static String DEFAULT_SUPERCLASS_NAME = "Object";
	protected String name;
        protected boolean isPartial = false;
	protected List members = new ArrayList();
        protected List delegates = new ArrayList();
	protected List javaImportStrings = new ArrayList();
	protected List modifiers = new ArrayList();
	protected String superClassname = DEFAULT_SUPERCLASS_NAME;
	protected List interfacesImplemented = new ArrayList();
	protected List variableMembers = new ArrayList();
        protected List events = new ArrayList();
	protected List enumerations = new ArrayList();
	protected List assigmentsOfEventGenerators = new ArrayList();
        protected List propertyStatements = new ArrayList();
        private DNType myType = null;
        protected boolean hidesBase = false; 
        protected List untranslatedStatements = new ArrayList();
	private List memberStatementsToMakePublic = new ArrayList();
       

        protected MetaClass(String code, InterpretationContext context) {
            super(code, context);
        }
        
        public abstract void parseBody();
        
        public abstract void addMetaClass(MetaClass mc);
            
        
	public void addMemberStatementToMakePublic(MemberStatement ms) {
		this.memberStatementsToMakePublic.add(ms);
	}

	public List getMemberStatementsToMakePublic() {
		return memberStatementsToMakePublic;
	}
        
        public boolean isPartial() {
            return this.isPartial;
        }
        
        public void setPartial(boolean isPartial) {
            this.isPartial = isPartial;
            
        }
        
	public String getName() {
		return this.name;
	}
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getFQName() {
            NamespaceStatement ns = Util.getParentNamespace(this);
            if (ns == null) {
                return this.getName();
            } else {
                return ns.getFQName() + "." + this.getName();
            }
        }

	public List getMembers() {
		return this.members;
	}
        
        public List getDelegates() {
            return this.delegates;
        }
        
        public List getEvents() {
            return this.events;
        }
        
        public boolean isEventName(String name) {
            return getEventStatementFor(name) != null;
        }
        
        public EventStatement getEventStatementFor(String name) {
            for (Iterator itr = this.getEvents().iterator(); itr.hasNext();) {
                EventStatement es = (EventStatement) itr.next();
                if (es.getVariable().getName().equals(name)) {
                    return es;
                }
            }
            return null;
        }
        
         public List getOperatorStatements() {
            List l = new ArrayList();
            for (Iterator itr = this.members.iterator(); itr.hasNext();) {
                Object o = itr.next();
                if (o instanceof OperatorStatement) {
                    l.add(o);
                }
            }
            return l;
        }

	public List getModifiers() {
		return this.modifiers;
	}

	public String getSuperClassname() {
		return this.superClassname;
	}	

	public List getInterfacesImplemented() {
		return this.interfacesImplemented;
	}
        
        public List getUntranslatedStatements() {
            return this.untranslatedStatements;
        }
        
         public List getPropertyStatements() {
            return this.propertyStatements;
        }

	public List getEnumerations() {
		return this.enumerations;
	}

	public boolean isEnumeration(DNType c) {
		return this.getEnumeration(c) != null;
	}

	public EnumStatement getEnumeration(DNType c) {
		for (Iterator itr = this.getEnumerations().iterator(); itr.hasNext();) {
			EnumStatement es = (EnumStatement) itr.next();
                        //System.out.println(es.getName() + " " + c.getName());
			if ( es.getFQName().equals(c.getName())) {
				return es;
			}
		}
		return null;
	}
        
        public boolean hasNoArgConstructor() {
            //System.out.println(this.name + " " + this.members);
            for (Iterator itr = this.members.iterator(); itr.hasNext();) {
                MemberStatement jm = (MemberStatement) itr.next();
                //System.out.println(jm.getName() + jm.getArgs().size() + " " + jm.isConstructor());
                if (jm.isConstructor() && jm.getArgs().size() == 0) {
                    return true;
                }
            }
            return false;
        }

	public void registerAssignmentForEventGenerator(Assignment a) {
		for (Iterator itr = this.getVariableMembersWithEvents().iterator(); itr.hasNext();) {
			VariableMemberDeclaration vmd = (VariableMemberDeclaration) itr.next();
			List vars = vmd.getVariables();
			for (Iterator itrr = vars.iterator(); itrr.hasNext();) {
				DNVariable v = (DNVariable) itrr.next();
				if (isAssignmentOf(a, v)) {
					this.assigmentsOfEventGenerators.add(a);
				}
			}
		}
	}

	public List getAllAssignmentsOf(DNVariable v) {
		Debug.logn("Look for all assignments of " + v + " ___ in " + this.getName(), this);
		List l = new ArrayList();
		for (Iterator itr = this.assigmentsOfEventGenerators.iterator(); itr.hasNext();) {
			Assignment a = (Assignment) itr.next();
			if (this.isAssignmentOf(a, v)) {
				l.add(a);
			}
		}
		return l;
	}

	private boolean isAssignmentOf(Assignment a, DNVariable v) {
		Expression assignee = a.getAssignee();
		if (assignee instanceof MemberAccessExpression) {
			MemberAccessExpression mae  = (MemberAccessExpression) assignee;
			if (mae.getVariableMemberName().equals(v.getName())) {
				Debug.logn("***********" + a, this);
				if (a.getExpression() != null
					&& a.getExpression() instanceof ObjectCreationExpression) {
					Debug.logn("GOT ITTT!!!!", this);
					return true;
				}
			}
	
		}
		return false;
	}

	public String getFQNamespaceName() {
		NamespaceStatement ns = Util.getParentNamespace(this);
		if (ns == null) {
                    return "";
		} else {
                    return ns.getFQName();
		}
	}

	public MemberStatement getConstructor() {
		for (Iterator itr = this.members.iterator(); itr.hasNext();) {
			Object ms = itr.next();
			if (ms instanceof MemberStatement && ((MemberStatement)  ms).isConstructor()) {
				return (MemberStatement ) ms;
			}
		}
		return null;
	}
        
        public void addMember(MemberStatement ms) {
            this.members.add(ms);
        }

	public MemberStatement getMainMethod() {
		for (Iterator itr = this.members.iterator(); itr.hasNext();) {
			Object ms = itr.next();
			if (ms instanceof MemberStatement && ((MemberStatement)  ms).isMain()) {
				return (MemberStatement ) ms;
			}
		}
		return null;
	}


	public List getVariableMembers() {
		return this.variableMembers;
	}

	public List getVariableMembersWithEvents() {
		List l = new ArrayList();
		for (Iterator itr = this.variableMembers.iterator(); itr.hasNext();) {
			VariableMemberDeclaration vmd = (VariableMemberDeclaration) itr.next();
			if (vmd.isWithEvents()) {
				l.add(vmd);
			}
		}
		return l;
	}

	public List findMemberStatementsByName(String methodName) {
		List methods = new ArrayList();
		for (Iterator itr = this.members.iterator(); itr.hasNext();) {
			MemberStatement ms = (MemberStatement) itr.next();
			Signature sig = ms.getSignature();
			if (sig.getName().equals(methodName)) {
				methods.add(ms);
			}
		}
		return methods;
	}
        
        public PropertyStatement findPropertyStatementOnMe(String variableName) {
            Debug.logn("Find Property statement for " + variableName, this);
            //System.out.println("Find Property statement for " + variableName);
            for (Iterator itr = this.propertyStatements.iterator(); itr.hasNext();) {
                PropertyStatement ps = (PropertyStatement) itr.next();
                if ( ps.getPropertyName().equals(variableName) ) {
                    return ps;
                }
            }
            Debug.logn("THERE IS NO Property statement for " + variableName, this);
            //System.out.println("THERE IS NO Property statement for " + variableName);
            return null;
        }
        
        public VariableMemberDeclaration findVariableMemberDeclarationOnMe(String name) {
            
            //System.out.println("Find Variable member decl on me " + name);
            
            //System.out.println("My superclass = " + type.getSuperClass());
            for (Iterator itr = this.variableMembers.iterator(); itr.hasNext();) {
                VariableMemberDeclaration vmd = (VariableMemberDeclaration) itr.next();
                if ( vmd.definesVariableOfName(name) ) {
                    return vmd;
                }
            }
            DNType superType = this.getDNType().getSuperClass();
            
            MetaClass mc = ParseTree.getParseTree(this.context).findMetaClassFor(superType);
            if (mc != null) {
                return mc.findVariableMemberDeclarationOnMe(name); 
            }
            //System.out.println("Nope: " + name);
            return null;
        }

	public Signature resolveMethodOnMe(InvocationExpression ie) {
		if (ie.getTarget() instanceof VBMeExpression) {
			for (Iterator itr = this.members.iterator(); itr.hasNext();) {
				MemberStatement ms = (MemberStatement) itr.next();
				Signature sig = ms.getSignature();
				if (sig.matchesSignature(ie.getMethodName(), ie.getArgs(), this.getLibrary())) {
					return sig;
				}
			}
		}
		return null;
	}
        
        public Signature resolveMethod(DNType calleeType, String mName, List mArgs) {
            //System.out.println("Resolve on " + this + " " + ie);
            //System.out.println("This mc type is " + this.getDNType());
            //System.out.println("Target type is " + ie.getTarget().getDNType());
            
            if (this.getDNType().isEqualOrIsSuperType(calleeType)) {
                //System.out.println("TYPES MATCH");
                //System.out.println("This members " + this.members);
		for (Iterator itr = this.members.iterator(); itr.hasNext();) {
                    MemberStatement ms = (MemberStatement) itr.next();
                    //System.out.println(ms);
                    Signature sig = ms.getSignature();
                    if (sig.matchesSignature(mName, mArgs, this.getLibrary())) {
                        //System.out.println("Yes");
                        return sig;
                    }
		}
            }
            //System.out.println("No");
            return null;
	}

	public void addJavaImports(List javaImportStrings) {
		this.javaImportStrings.addAll(javaImportStrings);
	}
        
        public void addJavaImport(String javaImportString) {
            this.javaImportStrings.add(javaImportString);
        }

	public List getJavaImports() {
		return this.javaImportStrings;
	}

	public List getVariables() {
            //System.out.println("Get vars in " + this.getName());
		List variables = new ArrayList();
		for (Iterator itr = variableMembers.iterator(); itr.hasNext();) {
			VariableMemberDeclaration vmd = (VariableMemberDeclaration) itr.next();
			variables.addAll(vmd.getVariables());
		}
                for (Iterator itr = this.events.iterator(); itr.hasNext();) {
                    EventStatement es = (EventStatement) itr.next();
                    variables.add(es.getVariable());
                }
                
                List superClasses = this.getDNType().getSuperClasses();
                // get variables from superclasses
                for (Iterator itr = superClasses.iterator(); itr.hasNext();) {
                    DNType type = (DNType) itr.next();
                    // System.out.println("Supertype " + type);
                    ParseTree pt = ParseTree.getParseTree(this);
                    List vars = pt.findVariablesFor(type, this);
                    //System.out.println("vars " + vars);
                    variables.addAll(vars);
                }
                //System.out.println("Got " + variables + " in " + this);
		return variables;
	}

	public DNVariable getVariable(String name) {
		for (Iterator itr = this.getVariables().iterator(); itr.hasNext();) {
			DNVariable var = (DNVariable) itr.next();
			if (var.getName().equals(name)) {
				return var;
			}
		}
		return null;
	}



	public Library getLibrary() {
		return this.context.getLibrary();
	}

	public InterpretationContext getParent() {
		return this.context;
	}

	public MetaClass getMetaClass() {
		return this;
	}
        
        protected void resetDNType() {
            myType = null;
            this.getDNType();
        }
	
	public DNType getDNType() {
            if (myType == null) {
		DNType superClass = this.context.getLibrary().getDNType(this.superClassname);
		myType = this.context.getLibrary().createProgramDefinedDNType(this.getFQName(), this);
		myType.setSuperClass(superClass);
		Debug.logn("My class is " + myType, this);
            }
            return myType;
                
	}


}

 