
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
package com.sun.dn;

import com.sun.dn.parser.*;
import com.sun.dn.java.*;
import java.util.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.util.*;
import com.sun.dn.java.*;
import com.sun.dn.container.web.*;
import com.sun.dn.container.gui.*;
import com.sun.dn.parser.statement.*;
import com.sun.dn.Library;

/** The Translator contains all the behaviors needed to translate a Parse Tree into
 * a JavaProgram.
 * @author danny.coward@sun.com
 **/

public class Translator {
    private String homeDir;
    private Library library;
    private String language;
    
    public Translator(String language) {
        this.language = language;
    }
    
    
    
    public JavaProgram createJavaProgram(ParseTree tree, String mainClassname, String projectType) throws Exception {
        
        List metaClasses = tree.getMetaClasses();
        tree.getMetaClasses(); // but there are other things..
        Debug.logn("Translator classes to translate: " + metaClasses, this);
        String vbImportsAsComment = "vb imports: " + tree.getImportedNamespaces().toString();
        JavaProgram jp = new JavaProgram(projectType, tree.getLibrary());
        
        EventDispatch eventDispatch = EventDispatchFactory.createEventDispatch(this.language, getLibrary(tree));
        
        this.addEnumerationsToProgram(tree.getEnumerations(), tree, jp);
        
        // delegates
        for (Iterator itr = tree.getDelegates().iterator(); itr.hasNext();) {
            DelegateStatement ds = (DelegateStatement) itr.next();
            try {
                JavaClass classForDelegate = this.translateDelegate(ds, tree);
                jp.addClass(classForDelegate);
            } catch (Throwable t) {
                ParseTree.handleTypeResolveException(tree, ds.getOriginalCode(), t, false);
                UntranslatedStatement ut = new UntranslatedStatement(ds.getOriginalCode(), tree);
                tree.addUntranslatedStatement(ut);
            }
        }
        Debug.logn("Done delegates", this);
        
        // meta classes
        for (Iterator itr = metaClasses.iterator(); itr.hasNext();) {
            MetaClass cms = (MetaClass) itr.next();
            Debug.logn("Translate metaclass " + cms.getName(), this);
            
            boolean isInstance = false;
            boolean isInterface = false;
            if (cms instanceof VBClassStatement 
                    || cms instanceof CSClassStatement
                    || cms instanceof CSStructStatement
                    || cms instanceof VBInterfaceStatement
                    || cms instanceof CSInterfaceStatement) {
                isInstance  = true;
            }
            if (cms instanceof VBInterfaceStatement
                    || cms instanceof CSInterfaceStatement) {
                isInterface = true;
            }
            
            // get Java Class for this metaclass
            JavaClass jc = this.createJavaClassStubForMetaClass(cms, isInstance, isInterface, tree);
            Debug.logn("Created java class for" + cms, this);
            
            // now add the main class
            if (cms.getDNType().getShortName().equals(mainClassname)) {
                JavaClass jmc = null;
                jc.makePublic();
                String mainArgsType = JavaMain.MAIN_NO_ARGS;
                MemberStatement mainMethod = cms.getMainMethod();
               
                
                
                if (mainMethod != null) {
                    mainMethod.makePublic(); 
                    if (!mainMethod.getArgs().isEmpty()) {
                        mainArgsType = JavaMain.MAIN_STRING_ARGS;
                    }
                }
                
                String classImport = "";
                if (jc.getFQName().indexOf(".") != -1) {
                    classImport = jc.getFQName();
                }
                
                if (projectType.equals(JavaProgram.CMDLINE_TYPE)) {
                    jmc = new JavaMain(jc.getName(), null, classImport, mainArgsType);
                } else if (projectType.equals(JavaProgram.GUI_TYPE)) {
                    boolean initializedInConstructor = cms.hasNoArgConstructor();
                    jmc = new JavaGui(jc.getName(), null, classImport, initializedInConstructor);
                } else {
                    jmc = null;
                    //throw new RuntimeException("Unknown project type " + projectType);
                }
               // jmc.addComment(vbImportsAsComment);
                jp.setMainClass(jmc);
                
                for (Iterator itrr = tree.getOptionStatements().iterator(); itrr.hasNext();) {
                    VBOptionStatement nextO = (VBOptionStatement) itrr.next();
                    if (jmc != null) {
                        jmc.addComment(nextO.asComment());
                    }
                }
                
            }
            Debug.logn(" Added the main class for" + cms, this);
            
            // translate the program defined events into
            // a variable for the event listeners
            // and the add and remove event methods.
            this.addVariablesAndMethodsForEvents(cms.getEvents(), tree, jc);
            
            // translate and add the enumerations
            this.addEnumerationsToProgram(cms.getEnumerations(), tree, jp);
            
            // do the member variables
            this.addMemberVariablesToJavaClass(cms.getVariableMembers(), tree, jc);
            
            // translate the method stubs
            Map javaMethodMemberStatementMap = this.addMemberMethodStubsToJavaClass(cms.getMembers(), isInstance, tree, jc);
            
            // Add the method stubs for the properties. 
            Map javaMethodToPropertyMap = new HashMap();
            javaMethodToPropertyMap = this.addPropertyMethodStubsToJavaClass(cms.getPropertyStatements(), isInstance, tree, jc);
           
            Debug.logn(" Added members for" + cms, this);
            // create and add the Event Support Classes
            List vbEventSupports = eventDispatch.createEventSupports(javaMethodMemberStatementMap, jc, projectType, cms.getFQNamespaceName());
            jp.addAllClasses(vbEventSupports);
            
            // make the right methods public
            List memberStatementsToMakePublic = cms.getMemberStatementsToMakePublic();
            eventDispatch.makeEventHandlerMethodsAccessible(memberStatementsToMakePublic, javaMethodMemberStatementMap);
            
            // connect the event handlers
            eventDispatch.connectEventSupports(jp, vbEventSupports, cms);
            eventDispatch.connectProgramDefinedDelegates(javaMethodMemberStatementMap); 
            
            
            // now implement the methods
            this.implementJavaMethods(javaMethodMemberStatementMap);
            this.implementJavaMethods(javaMethodToPropertyMap);
            Debug.logn(" Implemented methods for" + cms, this);
            
            // by now all the asJava() and getJava() calls have been made
            // so lets see what the library needs the Java class to import.
            jc.addImports(cms.getJavaImports());
            
            
            // now add all the untranslated comments
            for (Iterator utr = cms.getUntranslatedStatements().iterator(); utr.hasNext();) {
                UntranslatedStatement us = (UntranslatedStatement) utr.next();
                for (Iterator utrr = us.getJava().iterator(); utrr.hasNext();) {
                    jc.addPostDeclarationComment((String) utrr.next()); 
                }
            }
            
            // now make sure VB Gui initialization can work
            this.rescopeMethodsForGuiInitialization(jc, cms);
            Debug.logn("Done translating metaclass " + cms.getName(), this);
             
            jp.addClass(jc);  
             
        }
        
        if (jp.getMainClass() == null) {
            TranslationWarning trw = new TranslationWarning(mainClassname, "The classname for the main class you supplied (" + mainClassname + ") could not be found.");
            tree.getTranslationReport().addTranslationWarning(trw);
        }
        
        if (!tree.getUntranslatedStatements().isEmpty()) {
            System.out.println("There were untranslated top level statements");
            for (Iterator itr = tree.getUntranslatedStatements().iterator(); itr.hasNext();) {
                UntranslatedStatement ut = (UntranslatedStatement) itr.next();
                System.out.println(ut.getJava());
            } 
            
        }
        jp.setTranslationReport(tree.getTranslationReport());
        return jp;
    }
    
    private void rescopeMethodsForGuiInitialization(JavaClass jc, MetaClass cms) {
        //if (this.language.equals(Interpreter.VB_LANGUAGE)) {
            DNType wfType = cms.getLibrary().getDNType("System.Windows.Form");
            //System.out.println("wfType:" + wfType);
            //System.out.println("cms.getDNType()" + cms.getDNType());
            //System.out.println(cms.getDNType().isEqualOrIsSuperType(wfType));
            //System.out.println();
            if (wfType != null && wfType.isEqualOrIsSuperType(cms.getDNType())) {
                //System.out.println("Here");
                for ( Iterator itr = jc.findMethodsByName(JavaGui.INITIALIZE_METHOD_NAME).iterator(); itr.hasNext();){
                    JavaMethod jm = (JavaMethod) itr.next();
                    jm.makePublic();
                    
                }
            }
            
        //}
        
    }
    
    private void addVariablesAndMethodsForEvents(List eventStatements, InterpretationContext context, JavaClass jc) {
        List imports = new ArrayList();
        imports.add("java.util.*");
        jc.addImports(imports);
        for (Iterator itr = eventStatements.iterator(); itr.hasNext();) {
            EventStatement es = (EventStatement) itr.next();
            List modifiers = new ArrayList();
            modifiers.add(JavaKeywords.J_PRIVATE);
            JavaVariable jv = new JavaVariable(es.getJavaVariableName(), "List", modifiers, "new ArrayList()", null);
            for (Iterator itrr = es.getConstructedPreStatements().iterator(); itrr.hasNext();) {
                Object o = itrr.next();
                if (o instanceof Comment) {
                    jv.addComment( ((Comment)o).getComment() );
                }
            }
            jc.addMemberVariable(jv);
            
            //JavaMethod jm = EventDispatchFactory.getGenericJavaListenMethod(library);
            
            //jc.addMethod(jm);
            JavaMethod jm = null;
            jm = EventDispatchFactory.getGenericAddListenerMethodFor(library, es.getVariable().getType(), es.getJavaVariableName());
            jc.addMethod(jm);
            jm = EventDispatchFactory.getGenericRemoveListenerMethodFor(library, es.getVariable().getType(), es.getJavaVariableName());
            jc.addMethod(jm);
        }
    }
    
    private JavaClass translateDelegate(DelegateStatement ds, ParseTree tree) {
        List modifiers = this.translateModifiers( ds.getModifiers() );
        
        String translatedsuperClassName = getLibrary(tree).getJavaTypeFor(ds.getDNType().getSuperClass().getName());
        boolean isInterface = false;
        List jInterfaces = new ArrayList();
        
        JavaClass jc = new JavaClass(ds.getName(), modifiers, translatedsuperClassName, jInterfaces, isInterface );
        jc.setPackageName(ds.getNamespaceName());
        jc.addComment("Class converted from .NET code");
        jc.addMethod(DelegateHelper.getJavaConstructorFor(jc, getLibrary(tree)));
        for (Iterator itr = DelegateHelper.getJavaVariables().iterator(); itr.hasNext();) {
            JavaVariable jv = (JavaVariable) itr.next();
            jc.addMemberVariable(jv);
        }
         for (Iterator itr = ds.getConstructedPreStatements().iterator(); itr.hasNext();) {
            Object o = itr.next();
            if (o instanceof Comment) {
                jc.addPostDeclarationComment(((Comment)o).getComment());
            }
        }
        
        List imports = new ArrayList();
        imports.add("java.lang.reflect.*");
        jc.addImports(imports);
        
        List invokeJArgs = this.translateArgs(ds.getArgs(), tree); 
        String jReturnType = tree.getLibrary().getJavaTypeFor( ds.getReturnType());
        jc.addMethod(DelegateHelper.getInvokeMethodFor(invokeJArgs, jReturnType, library));
        return jc;
    }
    
    private JavaClass createJavaClassStubForMetaClass(MetaClass cms, boolean isInstance, boolean isInterface, ParseTree tree) {
        List modifiers = this.translateModifiers( cms.getModifiers() );
        modifiers.remove(JavaKeywords.J_STATIC);
        String superClassname = "";
        String translatedsuperClassName = getLibrary(tree).getJavaTypeFor(cms.getSuperClassname());
        if (translatedsuperClassName != null) {
            superClassname = translatedsuperClassName;
        } else {
            superClassname = cms.getSuperClassname();
           // System.out.println("SCN: " + "superClassname");
        }
        
        
        List interfacesImplemented = cms.getInterfacesImplemented();
        List jInterfaces = new ArrayList();
        for (Iterator itr = interfacesImplemented.iterator(); itr.hasNext();) {
            String nextInterface = (String) itr.next();
            String jInterfaceName = getLibrary(tree).getJavaTypeFor(nextInterface);
            jInterfaces.add(jInterfaceName);
        }
        JavaClass jc = new JavaClass(cms.getName(), modifiers, superClassname, jInterfaces, isInterface );
        for (Iterator itr = cms.getConstructedPreStatements().iterator(); itr.hasNext();) {
            Object o = itr.next();
            if (o instanceof Comment) {
                jc.addComment(((Comment)o).getComment());
            }
        }
        jc.setPackageName(cms.getFQNamespaceName());
        jc.addComment("Class converted from .NET code");
        return jc;
    }
    
    private void addEnumerationsToProgram(List enumerations, ParseTree tree, JavaProgram jp) {
        for (Iterator itr = enumerations.iterator(); itr.hasNext();) {
            EnumStatement es = (EnumStatement) itr.next();
            Debug.logn("Translate enum " + es, this);
            JavaClass jc = this.createJavaClassFrom(es, tree);
            Debug.logn("done", this);
            jp.addClass(jc);
        }
    }
    
    private void addMemberVariablesToJavaClass(List memberVariables, ParseTree tree, JavaClass jc) {
        for (Iterator itrr = memberVariables.iterator(); itrr.hasNext();) {
            VariableMemberDeclaration vmd = (VariableMemberDeclaration) itrr.next();
            //Debug.logn(" Translate variable " + vmd .getName(), this);
            
            List javaVariables = new ArrayList();
            try {
                javaVariables = translateVariableMember(vmd, tree);
            } catch (Throwable t) {
                ParseTree.handleTypeResolveException(tree, vmd.getOriginalCode(), t, false);
                String comment = UntranslatedStatement.getUncommentedJavaFor(vmd.getOriginalCode());
                jc.addPostDeclarationComment(comment);
            }
            //Debug.logn(" done translating " + vmd .getName(), this);
            for (Iterator jvs = javaVariables.iterator(); jvs.hasNext();) {
                JavaVariable jv = (JavaVariable) jvs.next();
                jc.addMemberVariable(jv);
            }
        }
    }
    
    private Map addPropertyMethodStubsToJavaClass(List propertyStatements,
            boolean isInstance,
            ParseTree tree,
            JavaClass jc) {
        
        Map javaMethodPropertyStatementMap = new HashMap();
        for (Iterator itrr = propertyStatements.iterator(); itrr.hasNext();) {
            PropertyStatement propS = (PropertyStatement) itrr.next();
            //Debug.logn(" Translate method " + memS .getName(), this);
            
            List goss = propS.getGetOrSetStatements();
            for (Iterator gossI = goss.iterator(); gossI.hasNext();) {
                GetOrSetStatement getS = (GetOrSetStatement) gossI.next();
                
                try {
                    JavaMethod jm = this.createJavaMethodForGetStatement(jc, getS, !isInstance, tree );
                    jc.addMethod( jm );
                    javaMethodPropertyStatementMap.put(jm, getS);
                } catch (Throwable t) {
                    ParseTree.handleTypeResolveException(tree, getS.getOriginalCode(), t, false);
                    String comment = UntranslatedStatement.getUncommentedJavaFor(getS.getOriginalCode());
                    jc.addPostDeclarationComment(comment);
            }
                
                
                
            }
        }
        return javaMethodPropertyStatementMap;
    }
    
    private Map addMemberMethodStubsToJavaClass(List members, boolean isInstance, ParseTree tree, JavaClass jc) {
        Map javaMethodMemberStatementMap = new HashMap();
        for (Iterator itrr = members.iterator(); itrr.hasNext();) {
            MemberStatement memS = (MemberStatement) itrr.next();
            //Debug.logn(" Translate method " + memS .getName(), this);
            try {
                JavaMethod jm = this.createJavaMethod(jc, memS, !isInstance, tree );
                jc.addMethod( jm );
                javaMethodMemberStatementMap.put(jm, memS);
            } catch (Throwable t) {
                ParseTree.handleTypeResolveException(tree, memS.getOriginalCode(), t, false);
                String comment = UntranslatedStatement.getUncommentedJavaFor(memS.getOriginalCode());
                jc.addPostDeclarationComment(comment);
            } 
        }
        return javaMethodMemberStatementMap;
    }
    
    
    
    private void implementJavaMethods(Map javaMethodStatementWithStatementMap) {
        // the methods have not yet been implemented...
        // now we can write the method implementations
        for (Iterator itrr = javaMethodStatementWithStatementMap.keySet().iterator(); itrr.hasNext();) {
            JavaMethod jm = (JavaMethod) itrr.next();
            Debug.logn(" implement method " + jm.getName(), this);
            StatementWithStatements ms = (StatementWithStatements) javaMethodStatementWithStatementMap.get(jm);
            this.doImplementJavaMethod(jm, ms);
            Debug.logn(" implemented method ", this);
        }
    }
    
    
    
    private JavaClass createJavaClassFrom(EnumStatement es, InterpretationContext context) {
        List javaClassForEnumModifiers = this.translateModifiers( es.getModifiers() );
        javaClassForEnumModifiers.remove(JavaKeywords.J_PRIVATE);
        JavaClass jc = new JavaClass(JavaClass.getShortName(es.getFQName()),
                javaClassForEnumModifiers,
                null,
                new ArrayList(),
                false);
        jc.setPackageName(JavaClass.getPackageName(es.getFQName()));
        int i = 0;
        for (Iterator itr = es.getVariableToExpressionMap().keySet().iterator(); itr.hasNext();) {
            DNVariable dnv = (DNVariable) itr.next();
            //System.out.println("df" + es.getVariableToExpressionMap());
            Expression e = (Expression) es.getVariableToExpressionMap().get(dnv);
            //System.out.println(" -- " + e);
            List varModifiers = new ArrayList();
            varModifiers.add(JavaKeywords.J_STATIC); // inner classes can't have statics
            JavaVariable jv = new JavaVariable(dnv.getName(),
                    getLibrary(context).getJavaTypeFor(es.getType()), 
                    varModifiers,
                    e.asJava(),
                    null);
            jc.addMemberVariable(jv);
            i++;
            
        }
        return jc;
    }
    
    private List translateVariableMember(VariableMemberDeclaration vmd, InterpretationContext context) {
        List javaVariables = new ArrayList();
        
        Debug.logn("Translate Variable Member " + vmd, this);
        List modifiers = translateModifiers(vmd.getModifiers());
        String initialValue = null;
        if (vmd.getExpression() != null) {
            initialValue = vmd.getExpression().asJava();
        }
        for (Iterator itr = vmd.getVariables().iterator(); itr.hasNext();) {
            DNVariable vbVar = (DNVariable) itr.next();
            String jName = vbVar.getName();
            String jType = "UnknownJavaType";
            
            jType = getLibrary(context).getJavaTypeFor(vbVar.getType());
            //System.out.println("JTYPE =" + jType);
            List dimension = vbVar.getDimension();
            JavaVariable jv = new JavaVariable(jName, jType, modifiers, initialValue, dimension);
            for (Iterator itrr = vmd.getConstructedPreStatements().iterator(); itrr.hasNext();) {
                Object o = itrr.next();
                if (o instanceof Comment) {
                    jv.addComment(((Comment) o).asJava());
                }
            }
            if (vmd.isConstant()) { 
                jv.makeStatic();
            }
            javaVariables.add(jv);
        }
        return javaVariables;
    }
    
    private JavaMethod createJavaMethodForGetStatement(JavaClass jClass, GetOrSetStatement getS, boolean inModule, InterpretationContext context) {
        boolean isInstanceMethod = true;
        boolean isInInterface = false;
        Debug.logn("Create Java Method for property getter: " + getS, this);
        List javaArgs = this.translateArgs(getS.getArgs(), context);
        List javaModifiers = this.translateModifiers(getS.getModifiers());
        Debug.logn("Getter type name is: " + getS.getReturnTypeName(), this);
        String jReturnType = context.getLibrary().getJavaTypeFor(getS.getReturnTypeName());
        String jName = getS.getJavaName();
        JavaMethod jm = new JavaMethod(javaModifiers , jName, jReturnType,
                javaArgs, isInstanceMethod, isInInterface, context.getLibrary());
        
        return jm;
    }
    
    private JavaMethod createJavaMethod(JavaClass jClass, MemberStatement ms, boolean inModule, InterpretationContext context) {
        boolean isInstanceMethod;
        boolean isBodiless = ms.isInterfaceMember() || ms.isAbstract() || ms.isExternal(); 
        if (inModule) {
            isInstanceMethod = false;
        } else {
            if (ms.isMain()) {
                isInstanceMethod = false;
            } else {
                isInstanceMethod = true;
            }
        }
        JavaMethod jm = null;
        List jvs = this.translateArgs(ms.getArgs(), context);
        List javaModifiers = this.translateModifiers(ms.getModifiers());
        if (ms.isConstructor()) {
            jm = new Constructor(javaModifiers , jClass, jvs, ms.getLibrary());
        } else {
            String jReturnType = ms.getLibrary().getJavaTypeFor(ms.getReturnType());
            jm = new JavaMethod(javaModifiers , ms.getJavaName(), jReturnType,
                    jvs, isInstanceMethod, isBodiless, ms.getLibrary());
        }
        for (Iterator itr = ms.getComments().iterator(); itr.hasNext();) {
            Comment c = (Comment) itr.next();
            jm.addCommentString(c.getComment());
        }
        if (ms.isExternal()) {
            jm.makeAbstract();
            jClass.makeAbstract(); 
        }
        return jm;
    }
    
    private void doImplementJavaMethod(JavaMethod jm, StatementWithStatements sws) {
        List statements = sws.getStatements();
        List lines = null;
        for (Iterator itr = statements.iterator(); itr.hasNext();) {
            Object o = itr.next();
            //System.out.println("Casting " + o + " to Statement");
            Statement s = (Statement) o;
            lines = this.translateStatement(s, sws);
            for (Iterator itrr = lines.iterator(); itrr.hasNext();) {
                jm.addCodeLine((String) itrr.next());
            }
            
            
        }
        lines = sws.getJavaSupplements();
        for (Iterator itrr = lines.iterator(); itrr.hasNext();) {
            jm.addCodeLine((String) itrr.next() + ";"); // dannyc
        }
    }
    
    private List translateModifiers(List modifiers) {
        if (this.language.equals(Interpreter.VB_LANGUAGE)) {
            return this.translateModifiersVB(modifiers);
        } else {
            return this.translateModifiersCS(modifiers);
        }
        
    }
    
    private List translateModifiersCS(List csModifiers) {
        List l = new ArrayList();
        for (Iterator itr = csModifiers.iterator(); itr.hasNext();) {
            String next = (String) itr.next();
            if (next.equals(CSKeywords.CS_Static)) {
                l.add(JavaKeywords.J_STATIC);
            } else if (next.equals(CSKeywords.CS_Abstract)) {
                l.add(JavaKeywords.J_ABSTRACT);
            } else if (next.equals(CSKeywords.CS_Sealed)) {
                l.add(JavaKeywords.J_FINAL);
            } else if (next.equals(CSKeywords.CS_Readonly) ||
                        next.equals(CSKeywords.CS_Const)) {
                // there is no translation. should add a comment: pls file a bug
            } else if (next.equals(CSKeywords.CS_Volatile)) {
                l.add(JavaKeywords.J_VOLATILE);
            } else if (CSKeywords.getMemberModifiers().contains(next)) {
                l.add(this.translateCSAccessKeyword(next));
            } else {
                l.add(JavaProgram.UNTRANSLATED_KEYWORD);
                //throw new RuntimeException("Can't translate modifier " + next);
                
            }
        }
        return l;
    }
    
    
    private List translateModifiersVB(List vbModifiers) {
        List l = new ArrayList();
        for (Iterator itr = vbModifiers.iterator(); itr.hasNext();) {
            String next = (String) itr.next();
            if (VBKeywords.getAccessKeywords().contains(next)) {
                l.add(this.translateVBAccessKeyword(next));
            } else if (VBKeywords.getSharedKeywords().contains(next)) {
                l.add(this.translateVBSharedKeyword(next));
            } else if (VBKeywords.getShadowKeywords().contains(next)) {
                l.add(this.translateShadowKeyword(next));
            } else if (VBKeywords.getInheritanceKeywords().contains(next)) {
                l.add(this.translateVBInheritanceKeyword(next));
            } else if (next.equals(VBKeywords.VB_ReadOnly)) {
                //l.add("<"+next+">");
            } else if (next.equals(VBKeywords.VB_WithEvents)) {
                //if (true) throw new RuntimeException("WIthEvents");
                //System.out.println("WARNING: IGNORING WithEvents keyword (Translator)");
                
            } else {
                l.add(JavaProgram.UNTRANSLATED_KEYWORD);
                //throw new RuntimeException("Can't translate modifier " + next);
            }
        }
        return l;
    }
    
    private List translateArgs(List vbArgs, InterpretationContext context) {
        List args = new ArrayList();
        for (Iterator itr = vbArgs.iterator(); itr.hasNext();) {
            DNVariable variable = (DNVariable) itr.next();
            JavaVariable jv = new JavaVariable(variable.getName(), getLibrary(context).getJavaTypeFor(variable.getType()));
            jv.setDimension(variable.getDimension());
            args.add(jv);
        }
        return args;
    }
    
    private List translateStatement(Statement statement, InterpretationContext context) {
        // going to get into trouble with multiline statements..
        Debug.logn("    Translate " + statement, this);
        long l = Debug.getTime();
        List strings = new ArrayList();
        for (Iterator itr = statement.getConstructedPreStatements().iterator(); itr.hasNext();) {
            Statement nextStatement = (Statement) itr.next();
            strings.addAll(translateStatement(nextStatement, context));
        }
        if (statement instanceof Assignment) {
            strings.addAll(this.translateAssignment((Assignment) statement));
        } else if (statement instanceof CallExpression) {
            CallExpression ce = (CallExpression) statement;
            strings.addAll( ((CallExpression) statement).getJava() );
        } else if (statement instanceof Comment) {
            strings.add(this.translateComment((Comment) statement));
        } else if (statement instanceof IfThenElseStatement ||
                statement instanceof ForNextStatement ||
                statement instanceof ForEachNextStatement ||
                statement instanceof DoStatement ||
                statement instanceof TryCatchFinallyStatement ||
                statement instanceof SelectStatement ||
                statement instanceof WhileStatement ||
                statement instanceof WithStatement ||
                statement instanceof UntranslatedStatement ||
                statement instanceof ReturnStatement ||
                statement instanceof StopStatement ||
                statement instanceof EraseStatement ||
                statement instanceof UnsafeBlock ||
                statement instanceof LockStatement ||
                statement instanceof CheckedBlock ||
                statement instanceof JumpStatement ||
                statement instanceof DelegateInvocation ||
                statement instanceof ExitStatement ||
                statement instanceof OnErrorStatement ||
                statement instanceof VBAddRemoveHandlerStatement ||
                statement instanceof VBRaiseEventStatement ||
                statement instanceof LocalVariableDeclaration ||
                statement instanceof ExpressionStatement ||
                statement instanceof UsingBlock) {
            strings.addAll(statement.getJava());
        } else {
            //System.out.println("Skipping " + statement + " can't translate it.");
            strings.add("//Translator couldnt: " + statement.toString());
        }
        Debug.logn("    Done " + (Debug.getTime() - l), this);
        return strings;
    }
    
    private List translateAssignment(Assignment assignment) {
        List l = new ArrayList();
        for (Iterator itr = assignment.getJava().iterator(); itr.hasNext();) {
            String next = (String) itr.next();
            l.add(next);
        }
        return l;
    }
    
    private String translateComment(Comment comment) {
        return (String) comment.getJava().get(0);
    }
    
    // dannyc ....
    private static String translateVBAccessKeyword(String kw) {
        if (kw.equals(VBKeywords.VB_Public)) {
            return JavaKeywords.J_PUBLIC;
        } else if (kw.equals(VBKeywords.VB_Protected)) {
            return JavaKeywords.J_PROTECTED;
        } else if (kw.equals(VBKeywords.VB_Friend)) {
            return "";
        } else if (kw.equals(VBKeywords.VB_Private)) {
            return JavaKeywords.J_PRIVATE;
        } else {
            throw new RuntimeException("this isn't an access keyword " + kw);
        }
    }
    
    /**
     ** see: ms-help://MS.VSCC.2003/MS.MSDNQTR.2003FEB.1033/csref/html/vclrfDeclaredAccessibilityPG.htm
     */
    private static String translateCSAccessKeyword(String kw) {
        if (kw.equals(CSKeywords.CS_Public)) {
            return JavaKeywords.J_PUBLIC;
        } else if (kw.equals(CSKeywords.CS_Protected)) {
            return JavaKeywords.J_PROTECTED;
        } else if (kw.equals(CSKeywords.CS_Internal)) {
            return "";
        } else if (kw.equals(CSKeywords.CS_Private)) {
            return JavaKeywords.J_PRIVATE;
        } else {
            throw new RuntimeException("this isn't an access keyword " + kw);
        }
    }
    
    private static String translateVBSharedKeyword(String kw) {
        if (kw.equals(VBKeywords.VB_Shared)) {
            return JavaKeywords.J_STATIC;
        } else {
            throw new RuntimeException("this isn't a shared keyword " + kw);
        }
    }
    
    // dannyc
    private static String translateShadowKeyword(String kw) {
        if (kw.equals(VBKeywords.VB_Overloads)) {
            //return "can't translate (" + kw + ")";
            return "";
        } else if (kw.equals(VBKeywords.VB_Overrides)) {
            //return "can't translate (" + kw + ")";
            return "";
        } else if (kw.equals(VBKeywords.VB_Overridable)) {
            //return "can't translate (" + kw + ")";
            return "";
        } else if (kw.equals(VBKeywords.VB_NotOverridable)) {
            return JavaKeywords.J_FINAL;
        } else if (kw.equals(VBKeywords.VB_MustOverride)) {
            return JavaKeywords.J_ABSTRACT;
        } else if (kw.equals(VBKeywords.VB_Shadows)) {
            //return "can't translate (" + kw + ")";
            return "";
        } else {
            throw new RuntimeException("this isn't a shadow keyword " + kw);
        }
    }
    
    private static String translateVBInheritanceKeyword(String kw) {
        if (kw.equals(VBKeywords.VB_MustInherit)) {
            return JavaKeywords.J_ABSTRACT;
        } else if (kw.equals(VBKeywords.VB_NotInheritable)) {
            return JavaKeywords.J_FINAL;
        } else {
            throw new RuntimeException("can't translate " + kw);
        }
    }
    
    private static Library getLibrary(InterpretationContext context) {
        return context.getLibrary();
    }
    
    public static boolean needsUpcasting(Expression left, Expression right, Library lib) {
        Debug.clogn("needsUpcasting left exp " + left, Translator.class);
        boolean isLeftPrimitive = isPrimitive(left, lib);
        boolean isRightPrimitive = isPrimitive(right, lib);
        return (isLeftPrimitive != isRightPrimitive);
    }
    
    private static boolean isPrimitive(Expression e, Library lib) {
        Debug.clogn("isPrimitive? type " + e, Translator.class);
        
        String eType = lib.getJavaTypeFor(e.getDNType().getName());
        Debug.clogn("isPrimitive? type " + eType, Translator.class);
        return JavaPrimitives.isPrimitive(eType);
    }
    
    public static String ensureJUpcast(Expression e, Library lib) {
        //System.out.println("--" + e);
        //System.out.println("--**" + lib);
        
        String jType = lib.getJavaTypeFor(e.getDNType().getName());
        //System.out.println("jType " + jType);
        if (JavaPrimitives.isPrimitive(jType)) {
            return JavaPrimitives.primitiveToClass(e.asJava(), jType);
        } else {
            return e.asJava();
        }
    }
    
    
    
    
}
 