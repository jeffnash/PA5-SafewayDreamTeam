/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.

Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/

// This is a project skeleton file

import java.io.PrintStream;
import java.util.Vector;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
/** This class is used for representing the inheritance tree during code
    generation. You will need to fill in some of its methods and
    potentially extend it in other useful ways. */
class CgenClassTable extends SymbolTable {

    /** All classes in the program, represented as CgenNode */
    private Vector nds;

    /** This is the stream to which assembly instructions are output */
    private PrintStream str;

    private int stringclasstag;
    private int intclasstag;
    private int boolclasstag;


    // The following methods emit code for constants and global
    // declarations.

    /** Emits code to start the .data segment and to
     * declare the global names.
     * */
    private void codeGlobalData() {
	// The following global names must be defined first.

	str.print("\t.data\n" + CgenSupport.ALIGN);
	str.println(CgenSupport.GLOBAL + CgenSupport.CLASSNAMETAB);
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Main, str);						// need to do something
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Int, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Str, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	BoolConst.falsebool.codeRef(str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	BoolConst.truebool.codeRef(str);
	str.println("");
	str.println(CgenSupport.GLOBAL + CgenSupport.INTTAG);
	str.println(CgenSupport.GLOBAL + CgenSupport.BOOLTAG);
	str.println(CgenSupport.GLOBAL + CgenSupport.STRINGTAG);

	// We also need to know the tag of the Int, String, and Bool classes
	// during code generation.

	str.println(CgenSupport.INTTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + intclasstag);
	str.println(CgenSupport.BOOLTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + boolclasstag);
	str.println(CgenSupport.STRINGTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + stringclasstag);

    }

    /** Emits code to start the .text segment and to
     * declare the global names.
     * */
    private void codeGlobalText() {
	str.println(CgenSupport.GLOBAL + CgenSupport.HEAP_START);
	str.print(CgenSupport.HEAP_START + CgenSupport.LABEL);
	str.println(CgenSupport.WORD + 0);
	str.println("\t.text");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Main, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Int, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Str, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Bool, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitMethodRef(TreeConstants.Main, TreeConstants.main_meth, str);
	str.println("");
    }

    /** Emits code definitions for boolean constants. */
    private void codeBools(int classtag) {
	BoolConst.falsebool.codeDef(classtag, str);
	BoolConst.truebool.codeDef(classtag, str);
    }

    /** Generates GC choice constants (pointers to GC functions) */
    private void codeSelectGc() {
	str.println(CgenSupport.GLOBAL + "_MemMgr_INITIALIZER");
	str.println("_MemMgr_INITIALIZER:");
	str.println(CgenSupport.WORD 
		    + CgenSupport.gcInitNames[Flags.cgen_Memmgr]);

	str.println(CgenSupport.GLOBAL + "_MemMgr_COLLECTOR");
	str.println("_MemMgr_COLLECTOR:");
	str.println(CgenSupport.WORD 
		    + CgenSupport.gcCollectNames[Flags.cgen_Memmgr]);

	str.println(CgenSupport.GLOBAL + "_MemMgr_TEST");
	str.println("_MemMgr_TEST:");
	str.println(CgenSupport.WORD 
		    + ((Flags.cgen_Memmgr_Test == Flags.GC_TEST) ? "1" : "0"));
    }

    /** Emits code to reserve space for and initialize all of the
     * constants.  Class names should have been added to the string
     * table (in the supplied code, is is done during the construction
     * of the inheritance graph), and code for emitting string constants
     * as a side effect adds the string's length to the integer table.
     * The constants are emmitted by running through the stringtable and
     * inttable and producing code for each entry. */
    private void codeConstants() {
	// Add constants that are required by the code generator.
	AbstractTable.stringtable.addString("");
	//add all string constants here
	/*Adding 0 to the int table. */
	AbstractTable.inttable.addString("0");
	/* Setting stringtable and inttable to the corresponding class tag values.
	Doing a similar procedure for the bools, via the codeBools method, even though
	BoolConst.java is technically not an AbstractTable. */
	AbstractTable.stringtable.codeStringTable(stringclasstag, str);
	AbstractTable.inttable.codeStringTable(intclasstag, str);
	codeBools(boolclasstag);
    }


    /** Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String).  Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     * */
    private void installBasicClasses() {
	AbstractSymbol filename 
	    = AbstractTable.stringtable.addString("<basic class>");
	
	// A few special class names are installed in the lookup table
	// but not the class list.  Thus, these classes exist, but are
	// not part of the inheritance hierarchy.  No_class serves as
	// the parent of Object and the other special classes.
	// SELF_TYPE is the self class; it cannot be redefined or
	// inherited.  prim_slot is a class known to the code generator.

	addId(TreeConstants.No_class,
	      new CgenNode(new class_c(0,
				      TreeConstants.No_class,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	addId(TreeConstants.SELF_TYPE,
	      new CgenNode(new class_c(0,
				      TreeConstants.SELF_TYPE,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));
	
	addId(TreeConstants.prim_slot,
	      new CgenNode(new class_c(0,
				      TreeConstants.prim_slot,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	// The Object class has no parent class. Its methods are
	//        cool_abort() : Object    aborts the program
	//        type_name() : Str        returns a string representation 
	//                                 of class name
	//        copy() : SELF_TYPE       returns a copy of the object

	class_c Object_class = 
	    new class_c(0, 
		       TreeConstants.Object_, 
		       TreeConstants.No_class,
		       new Features(0)
			   .appendElement(new method(0, 
					      TreeConstants.cool_abort, 
					      new Formals(0), 
					      TreeConstants.Object_, 
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.type_name,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.copy,
					      new Formals(0),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(Object_class, CgenNode.Basic, this));
	
	// The IO class inherits from Object. Its methods are
	//        out_string(Str) : SELF_TYPE  writes a string to the output
	//        out_int(Int) : SELF_TYPE      "    an int    "  "     "
	//        in_string() : Str            reads a string from the input
	//        in_int() : Int                "   an int     "  "     "

	class_c IO_class = 
	    new class_c(0,
		       TreeConstants.IO,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new method(0,
					      TreeConstants.out_string,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Str)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.out_int,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Int)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_string,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_int,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0))),
		       filename);

	CgenNode IO_node = new CgenNode(IO_class, CgenNode.Basic, this);
	installClass(IO_node);

	// The Int class has no methods and only a single attribute, the
	// "val" for the integer.

	class_c Int_class = 
	    new class_c(0,
		       TreeConstants.Int,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	installClass(new CgenNode(Int_class, CgenNode.Basic, this));

	// Bool also has only the "val" slot.
	class_c Bool_class = 
	    new class_c(0,
		       TreeConstants.Bool,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	installClass(new CgenNode(Bool_class, CgenNode.Basic, this));

	// The class Str has a number of slots and operations:
	//       val                              the length of the string
	//       str_field                        the string itself
	//       length() : Int                   returns length of the string
	//       concat(arg: Str) : Str           performs string concatenation
	//       substr(arg: Int, arg2: Int): Str substring selection

	class_c Str_class =
	    new class_c(0,
		       TreeConstants.Str,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.Int,
					    new no_expr(0)))
			   .appendElement(new attr(0,
					    TreeConstants.str_field,
					    TreeConstants.prim_slot,
					    new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.length,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.concat,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg, 
								     TreeConstants.Str)),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.substr,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Int))
						  .appendElement(new formalc(0,
								     TreeConstants.arg2,
								     TreeConstants.Int)),
					      TreeConstants.Str,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(Str_class, CgenNode.Basic, this));
    }
	
    // The following creates an inheritance graph from
    // a list of classes.  The graph is implemented as
    // a tree of `CgenNode', and class names are placed
    // in the base class symbol table.
    
    private void installClass(CgenNode nd) {
	AbstractSymbol name = nd.getName();
	if (probe(name) != null) return;
	nds.addElement(nd);
	addId(name, nd);
    }

    private void installClasses(Classes cs) {
    	Vector<Class_> allClasses = new Vector<Class_>();
    	Vector<Class_> organizedClassList = new Vector<Class_>();
    	/* First pass, we simply see which classes inherit from Object. */

    	Vector<Class_> daddyClasses = new Vector<Class_>();
    	for (Enumeration e = cs.getElements(); e.hasMoreElements(); ) {
        	allClasses.add((Class_)e.nextElement());
        }

        for (int i = 0; i < allClasses.size(); i += 1) {
        	if (allClasses.get(i).getParent().getString().equals("Object") || allClasses.get(i).getParent().getString().equals("IO")) {
        		daddyClasses.add(allClasses.get(i));
        	}
        }

        for (int i = 0; i < daddyClasses.size(); i += 1) {
        	organizedClassList.add(daddyClasses.get(i));
        	recursiveDepthFirst(daddyClasses.get(i), allClasses, organizedClassList);
        }

        for (int i = 0; i < organizedClassList.size(); i += 1) {
        	installClass(new CgenNode(organizedClassList.get(i), 
				       CgenNode.NotBasic, this));
        }


       
    }

    private void recursiveDepthFirst(Class_ parentClass, Vector<Class_> allClasses, Vector<Class_> organizedClassList) {
    	if (allClasses.size() == organizedClassList.size()) {
    		return;
    	} 
    	String parentClassString = parentClass.getName().getString();
    	for (int i = 0; i < allClasses.size(); i += 1) {
    		if (allClasses.get(i).getParent().getString().equals(parentClassString)) {
    			organizedClassList.add(allClasses.get(i));
    			recursiveDepthFirst(allClasses.get(i), allClasses, organizedClassList);
    		}
    	}
    }

    public static int getMax(ArrayList<Integer> findMax) {
    	return Collections.max(findMax);
    }

    public static int numTempCounterFunctionEntry(Expression expr) {
    	ArrayList<Integer> findMax = new ArrayList<Integer>();

    	findMax.clear();
    	numTempCounter(expr, findMax);
    	return getMax(findMax);
    }

    public static int numTempCounter(Expression expr, ArrayList<Integer> findMax) {
    	
    	//need to add case for id to return 0
    	if (expr instanceof int_const || expr instanceof bool_const) {
    		return 0;
    	} else if (expr instanceof eq) {
	    	findMax.add(numTempCounter(((eq)expr).e1, findMax));
	    	findMax.add(numTempCounter(((eq)expr).e2, findMax) + 1);
	    	System.out.println("eq" + getMax(findMax));
    	} else if (expr instanceof leq) {
	    	findMax.add(numTempCounter(((leq)expr).e1, findMax));
	    	findMax.add(numTempCounter(((leq)expr).e2, findMax) + 1);
	    	System.out.println("leq" + getMax(findMax));

    	} else if (expr instanceof plus) {
	    	findMax.add(numTempCounter(((plus)expr).e1, findMax));
	    	findMax.add(numTempCounter(((plus)expr).e2, findMax) + 1);
	    		    		    	System.out.println("plus_" + getMax(findMax));

    	} else if (expr instanceof sub) {
	    	findMax.add(numTempCounter(((sub)expr).e1, findMax));
	    	findMax.add(numTempCounter(((sub)expr).e2, findMax) + 1);
	    		    		    	System.out.println("plus_" + getMax(findMax));

    	} else if (expr instanceof mul) {
	    	findMax.add(numTempCounter(((mul)expr).e1, findMax));
	    	findMax.add(numTempCounter(((mul)expr).e2, findMax) + 1);
	    		    		    	System.out.println("plus_" + getMax(findMax));

    	} else if (expr instanceof divide) {
	    	findMax.add(numTempCounter(((divide)expr).e1, findMax));
	    	findMax.add(numTempCounter(((divide)expr).e2, findMax) + 1);
	    		    		    	System.out.println("plus_" + getMax(findMax));

    	}else if (expr instanceof cond) {
    		cond cond = (cond) expr;
    		findMax.add(numTempCounter(cond.pred, findMax));
    		findMax.add(numTempCounter(cond.then_exp, findMax));
    		findMax.add(numTempCounter(cond.else_exp, findMax));
    			    		    	System.out.println("plus_" + getMax(findMax));

    	}
    	return 0;
    }

    private void buildInheritanceTree() {
	for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
	    setRelations((CgenNode)e.nextElement());
	}
    }

    private void setRelations(CgenNode nd) {
	CgenNode parent = (CgenNode)probe(nd.getParent());
	nd.setParentNd(parent);
	parent.addChild(nd);
    }

    /** Constructs a new class table and invokes the code generator */
    public CgenClassTable(Classes cls, PrintStream str) {
    	//NDS CONTAINS ALL THE CLASSES WOW RARE AWESOME DISCOVERY BY JAESEO
	nds = new Vector();

	this.str = str;

	stringclasstag = 0 /* Change to your String class tag here */;
	intclasstag =    1 /* Change to your Int class tag here */;
	boolclasstag =   2 /* Change to your Bool class tag here */;

	enterScope();
	if (Flags.cgen_debug) System.out.println("Building CgenClassTable");
	//This should install the basic classes in the nds vector first, so they have indeces of 0, 1, and 2
	installBasicClasses();
	// Now install other defined classes in inheritance tree
	installClasses(cls);
	buildInheritanceTree();

	code();

	exitScope();
    }

    /** This method is the meat of the code generator.  It is to be
        filled in programming assignment 5 */
    public void code() {
	if (Flags.cgen_debug) System.out.println("coding global data");
	codeGlobalData();

	if (Flags.cgen_debug) System.out.println("choosing gc");
	codeSelectGc();

	if (Flags.cgen_debug) System.out.println("coding constants");
	codeConstants();

	//                 Add your code to emit

	//                   - class_nameTab
	/*Print out the class name table, iterating through the 'nds' vector,
	 * which contains CgenNodes of all classes.*/
	str.print(CgenSupport.CLASSNAMETAB + CgenSupport.LABEL);
	for (int i = 0; i < nds.size(); i += 1) {
		CgenNode curNDS = (CgenNode)nds.get(i);
		str.println(CgenSupport.WORD + CgenSupport.STRCONST_PREFIX + AbstractTable.stringtable.lookup(curNDS.getName().str).index);
	}




	//                   - prototype objects

	//first, we print the class_objTab

	str.print(CgenSupport.CLASSOBJTAB + CgenSupport.LABEL);
	for (int i = 0; i < nds.size(); i += 1) {
		CgenNode curNDS = (CgenNode)nds.get(i);
		//emitting a word for each prototype object
		str.println(CgenSupport.WORD + curNDS.getName().toString() + CgenSupport.PROTOBJ_SUFFIX);

		//emitting the word for each corresponding class initialization
		str.println(CgenSupport.WORD + curNDS.getName().toString() + CgenSupport.CLASSINIT_SUFFIX);
	}


	for (int i = 0; i < nds.size(); i += 1) {
		CgenNode curNDS = (CgenNode)nds.get(i);
		//save the original class's name
		String dispatchedClassName = curNDS.getName().getString();
		Vector<String> concatenatedClassMethodStrings = new Vector<String>();
		Vector<String> classMethodStrings = new Vector<String>();
		while (curNDS.getParentNd() != null) {
			AbstractSymbol className = curNDS.getName();
			for (Enumeration e = curNDS.features.getElements(); e.hasMoreElements();) {
				Feature curElement = (Feature)e.nextElement();
				if (curElement instanceof method) {
					method curMethod = (method)curElement;
					boolean alreadyThere = classMethodStrings.indexOf(curMethod.name.getString()) != -1;
					if (!alreadyThere) {
						concatenatedClassMethodStrings.add(className.getString() + CgenSupport.METHOD_SEP + curMethod.name.getString());
						classMethodStrings.add(curMethod.name.getString());
					}
				} 
			}
			curNDS = curNDS.getParentNd();
		}

		Collections.reverse(concatenatedClassMethodStrings);								//SWAG
		Collections.reverse(classMethodStrings);
		GlobalData.class_method_map.put(dispatchedClassName, classMethodStrings);

		str.print(dispatchedClassName + CgenSupport.DISPTAB_SUFFIX + CgenSupport.LABEL);
		for (int j = 0; j < concatenatedClassMethodStrings.size(); j += 1) {

			String concatenatedClassMethodString = concatenatedClassMethodStrings.get(j);
			str.println(CgenSupport.WORD + concatenatedClassMethodString);
		}
				

	}



	for (int i = 0; i < nds.size(); i += 1) {
		str.println(CgenSupport.WORD + "-1");

		CgenNode curNDS = (CgenNode)nds.get(i);
		CgenNode curNDS2 = (CgenNode)nds.get(i); // this is for getting attribute count
		CgenSupport.emitProtObjRef(curNDS.getName(), str);
		String protClassName = curNDS.getName().getString();
		/* The first three 32-bit words of each object are assumed to contain a class tag, the object size, and a
			pointer for dispatch information. In addition, the garbage collector requires that the word immediately
			before an object contain -1; this word is not part of the object.*/

		str.println(CgenSupport.LABEL + CgenSupport.WORD + i);
		int attrCount = 3;
		while (curNDS2.getParentNd() != null) {
			for (Enumeration e = curNDS2.features.getElements(); e.hasMoreElements();) {
				Feature curElement = (Feature)e.nextElement();
				if (curElement instanceof attr) {
					attrCount += 1;
				}
			}
			curNDS2 = curNDS2.getParentNd();
		}

		str.println(CgenSupport.WORD + attrCount);
		str.println(CgenSupport.WORD + curNDS.getName() + CgenSupport.DISPTAB_SUFFIX);
		//we need to add attributes (11/14)
		Vector<String> classAttrStrings = new Vector<String>();
		Vector<attr> classAttr = new Vector<attr>();
		Vector<attr> classInitAttr = new Vector<attr>();
		boolean init_set = false;
		while (curNDS.getParentNd() != null) {
			Vector<attr> tempVector = new Vector<attr>();
			for (Enumeration e = curNDS.features.getElements(); e.hasMoreElements();) {
				Feature curElement = (Feature)e.nextElement();
				if (curElement instanceof attr) {
					tempVector.add((attr)curElement);
				}
			}
			for (int j = tempVector.size() - 1; j >= 0; j--) {
				attr curAttr = tempVector.get(j);
				classAttrStrings.add(curAttr.name.getString());
				classAttr.add(curAttr);
				if (!init_set) {
					classInitAttr.add(curAttr);
				}
			}
			if (!init_set) {
				GlobalData.class_attr_init_map.put(protClassName, classInitAttr);
				init_set = true;
			}
			curNDS = curNDS.getParentNd();
		}

		Collections.reverse(classAttrStrings);
		Collections.reverse(classAttr);
		GlobalData.class_attr_map.put(protClassName, classAttrStrings);
		

		for (int j = 0; j < classAttr.size(); j += 1) {
			attr curAttr = classAttr.get(j);
			if (curAttr.type_decl.getString().equals(TreeConstants.Bool.getString())) {
				str.println(CgenSupport.WORD + CgenSupport.BOOLCONST_PREFIX + "0");
			} else if (curAttr.type_decl.getString().equals(TreeConstants.Int.getString())) {
				int indexofzero = ((IntSymbol)AbstractTable.inttable.lookup("0")).index;
				str.println(CgenSupport.WORD + CgenSupport.INTCONST_PREFIX + indexofzero);
			} else if (curAttr.type_decl.getString().equals(TreeConstants.Str.getString())) {
				int indexofemptystring = ((StringSymbol)AbstractTable.stringtable.lookup("")).index;
				str.println(CgenSupport.WORD + CgenSupport.STRCONST_PREFIX + indexofemptystring);
			} else {
				str.println(CgenSupport.WORD + "0");
			}
		}
	}
	





	if (Flags.cgen_debug) System.out.println("coding global text");
	codeGlobalText();

	//                 Add your code to emit
	//                   - object initializer
	for (int i = 0; i < nds.size(); i += 1) {
		//we will have to add attributes here later
		CgenNode curNDS = (CgenNode)nds.get(i);
		CgenSupport.emitInitRef(curNDS.getName(), str);
		str.print(CgenSupport.LABEL);

		CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -12, str);
		CgenSupport.emitStore(CgenSupport.FP, 12/4, CgenSupport.SP, str);
		CgenSupport.emitStore(CgenSupport.SELF, 8/4, CgenSupport.SP, str);
		CgenSupport.emitStore(CgenSupport.RA, 4/4, CgenSupport.SP, str);
		CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP, 16, str);
		CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, str);
		//Object_init skips this line
		if (!curNDS.getName().getString().equals(TreeConstants.Object_.getString())) {
			CgenSupport.emitJal(curNDS.getParentNd().getName().getString() + CgenSupport.CLASSINIT_SUFFIX, str);
		}

		Vector<attr> attrInitVector = GlobalData.class_attr_init_map.get(curNDS.name.getString());
		Vector<String> attrVector = GlobalData.class_attr_map.get(curNDS.name.getString());
		for (int j = 0; j < attrInitVector.size(); j++) {
			attr initAttr = attrInitVector.get(j);
			if (initAttr.init instanceof no_expr) {

				continue;
			}
			initAttr.init.code(str);
			int realIndex = attrVector.indexOf(initAttr.name.getString());
			CgenSupport.emitStore(CgenSupport.ACC, realIndex + 3, CgenSupport.SELF, str);
		}


		CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, str);
		CgenSupport.emitLoad(CgenSupport.FP, 12/4, CgenSupport.SP, str);
		CgenSupport.emitLoad(CgenSupport.SELF, 8/4, CgenSupport.SP, str);
		CgenSupport.emitLoad(CgenSupport.RA, 4/4, CgenSupport.SP, str);
		CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 12, str);
		CgenSupport.emitReturn(str);

	}


	for (int i = 0; i < nds.size(); i += 1) {
		CgenNode curNDS = (CgenNode)nds.get(i);
		GlobalData.current_class = curNDS.getName().getString();
		if (curNDS.getName().getString().equals("Object")) {
			continue;
		} else if (curNDS.getName().getString().equals("IO")) {
			continue;
		} else if (curNDS.getName().getString().equals("Int")) {
			continue;
		} else if (curNDS.getName().getString().equals("String")) {
			continue;
		} else if (curNDS.getName().getString().equals("Bool")) {
			continue;
		}
		/*if (curNDS.getName().getString().equals("Cat")) {
			curNDS.dump_with_types(System.out, 0);
		}*/
		for (Enumeration e = curNDS.features.getElements(); e.hasMoreElements();) {
			Feature curElement = (Feature)e.nextElement();
			if (curElement instanceof method) {
				method curMeth = (method)curElement;
				int letDepth = LetCaseHelper.getLetCaseDepth(curMeth.expr);
				GlobalData.curMethodLetDepth = letDepth;
				GlobalData.cur_method_parameters.clear();
				for (Enumeration f = curMeth.formals.getElements(); f.hasMoreElements();) {
					formalc formal = (formalc)f.nextElement();
					GlobalData.cur_method_parameters.add(formal.name.getString());
				}
				Collections.reverse(GlobalData.cur_method_parameters);
				int paramNumbers = curMeth.formals.getLength();
				CgenSupport.emitMethodRef(curNDS.getName(), curMeth.name, str);
				str.print(CgenSupport.LABEL);
				int NT = paramNumbers;
				int offset = (3 + letDepth) * 4;
		        /* 12 is the amount of space we need for the temporaries in this frame = WORD_SIZE*(1 + NT), 
		                # alternatively, this is the max NT_offset that is valid = -12
		                # we adjust the stack pointer first by convention 
		                # (to deal with interrupts, which should not be an issue in this class) */
		        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -offset, str);
		        /*We need to save $ra - we use the next empty slot, which is where $sp used to point to*/
		        CgenSupport.emitStore(CgenSupport.FP, (3 * 4) / 4, CgenSupport.SP, str);
		        CgenSupport.emitStore(CgenSupport.SELF, (2 * 4) / 4, CgenSupport.SP, str);
		        CgenSupport.emitStore(CgenSupport.RA, (1 * 4) / 4, CgenSupport.SP, str);


		        /*# we now make sure that $fp points to the $ra slot 
		                # (so our current perspective looks like the diagram above, again)*/
		        CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP, 4 * 4, str);
		        CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, str);

				curMeth.expr.code(str);

				        //epilogue
		        //# restore $ra - same code as above in reverse
		        CgenSupport.emitLoad(CgenSupport.FP, (3 * 4) / 4, CgenSupport.SP, str);
		        CgenSupport.emitLoad(CgenSupport.SELF, (2 * 4) / 4, CgenSupport.SP, str);
		        CgenSupport.emitLoad(CgenSupport.RA, (1 * 4) / 4, CgenSupport.SP, str);

		        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, offset + paramNumbers * 4, str);
		        CgenSupport.emitReturn(str);
				// curMeth.expr.dump_with_types(str, 0);

				//check if we have expressions! or expression

					
				}
			}
	}

	//                   - the class methods
	//                   - etc...
    }

    /** Gets the root of the inheritance tree */
    public CgenNode root() {
	return (CgenNode)probe(TreeConstants.Object_);
    }
}
			  


class GlobalData {
    public static int nextLabel = 0;
    public static int getLabelIndex() {
        return nextLabel++;
    }
    public static String current_class;
    public static int curMethodLetDepth;

    public static HashMap<String, Vector<String>> class_method_map = new HashMap<String, Vector<String>>();
    public static HashMap<String, Vector<String>> class_attr_map = new HashMap<String, Vector<String>>();
    public static HashMap<String, Vector<attr>> class_attr_init_map = new HashMap<String, Vector<attr>>();
    public static Vector<String> cur_method_parameters = new Vector<String>();
}

class LetCaseHelper {
	public static int cur_let_depth = 0;
	public static HashMap<String, Vector<Integer>> id_location_map = new HashMap<String, Vector<Integer>>();
	public static void put(String id, int location) {
		if (!id_location_map.containsKey(id)) {
			id_location_map.put(id, new Vector<Integer>());
		}
		(id_location_map.get(id)).add(0, (Integer) location);
	}
	public static int get(String id) {
		Vector<Integer> vec = id_location_map.get(id);
		if (vec == null || vec.size() == 0) {
			return -1;
		} 
		return (id_location_map.get(id)).get(0);
	}
	public static void upOneLevel(String id) {
		Vector<Integer> vec = id_location_map.get(id);
		if (vec == null || vec.size() == 0) {
			System.out.println ("weird thing happened in LetCaseHelper");
			return;
		}
		vec.removeElementAt(0);
		if (vec.size() == 0) {
			deleteIDEntry(id);
		}
	}
	public static void deleteIDEntry(String id) {
		id_location_map.remove(id);
	}
	public static int getLetCaseDepth(Expression e) {
		if (e instanceof assign){
			return getLetCaseDepth(((assign)e).expr);
		} 

		else if (e instanceof static_dispatch) {
			static_dispatch s = (static_dispatch)e;
			int max = getLetCaseDepth(s.expr);
			for (Enumeration n = s.actual.getElements(); n.hasMoreElements(); ) {
				Expression x = (Expression) n.nextElement();
				int curDepth = getLetCaseDepth(x);
				if (max < curDepth) {
					max = curDepth;
				}
			}
			return max;
		} 

		else if (e instanceof dispatch) {
			dispatch d = (dispatch)e;
			int max = getLetCaseDepth(d.expr);
			for (Enumeration n = d.actual.getElements(); n.hasMoreElements(); ) {
				Expression x = (Expression) n.nextElement();
				int curDepth = getLetCaseDepth(x);
				if (max < curDepth) {
					max = curDepth;
				}
			}
			return max;
		} 

		else if (e instanceof cond) {
			int max = getLetCaseDepth(((cond)e).pred);
			int curDepth = getLetCaseDepth(((cond)e).then_exp);
			if (max < curDepth) max = curDepth;
			curDepth = getLetCaseDepth(((cond)e).else_exp);
			if (max < curDepth) max = curDepth;
			return max;
		} 

		else if (e instanceof loop) {
			int max = getLetCaseDepth(((loop)e).pred);
			int curDepth = getLetCaseDepth(((loop)e).body);
			if (max < curDepth) max = curDepth;
			return max;
		} 

		else if (e instanceof typcase) {
			int max = getLetCaseDepth(((typcase)e).expr);
			for (Enumeration n = ((typcase)e).cases.getElements(); n.hasMoreElements(); ) {
				branch b = (branch) (n.nextElement());
				int curDepth = getLetCaseDepth(b.expr) + 1;
				if (max < curDepth) max = curDepth;
			}
			return max;
		} 

		else if (e instanceof block) {
			block b = (block)e;
			int max = 0;
			for (Enumeration n = b.body.getElements(); n.hasMoreElements(); ) {
				Expression x = (Expression) n.nextElement();
				int curDepth = getLetCaseDepth(x);
				if (max < curDepth) {
					max = curDepth;
				}
			}
			return max;
		} 

		else if (e instanceof let) {
			let l = (let)e;
			int max = getLetCaseDepth(l.init);
			int curDepth = getLetCaseDepth(l.body) + 1;
			if (max < curDepth) max = curDepth;
			return max;
		} 

		else if (e instanceof plus) {
			plus p = (plus)e;
			int max = getLetCaseDepth(p.e1);
			int curDepth = getLetCaseDepth(p.e2);
			if (max < curDepth) max = curDepth;
			return max;
		} 

		else if (e instanceof sub) {
			sub p = (sub)e;
			int max = getLetCaseDepth(p.e1);
			int curDepth = getLetCaseDepth(p.e2);
			if (max < curDepth) max = curDepth;
			return max;
		} 

		else if (e instanceof mul) {
			mul p = (mul)e;
			int max = getLetCaseDepth(p.e1);
			int curDepth = getLetCaseDepth(p.e2);
			if (max < curDepth) max = curDepth;
			return max;
		} 

		else if (e instanceof divide) {
			divide p = (divide)e;
			int max = getLetCaseDepth(p.e1);
			int curDepth = getLetCaseDepth(p.e2);
			if (max < curDepth) max = curDepth;
			return max;
		} 

		else if (e instanceof neg) {
			return getLetCaseDepth(((neg)e).e1);
		} 

		else if (e instanceof lt) {
			lt p = (lt)e;
			int max = getLetCaseDepth(p.e1);
			int curDepth = getLetCaseDepth(p.e2);
			if (max < curDepth) max = curDepth;
			return max;
		} 

		else if (e instanceof leq) {
			leq p = (leq)e;
			int max = getLetCaseDepth(p.e1);
			int curDepth = getLetCaseDepth(p.e2);
			if (max < curDepth) max = curDepth;
			return max;
			
		} 

		else if (e instanceof eq) {
			eq p = (eq)e;
			int max = getLetCaseDepth(p.e1);
			int curDepth = getLetCaseDepth(p.e2);
			if (max < curDepth) max = curDepth;
			return max;
		} 

		else if (e instanceof comp) {
			return getLetCaseDepth(((comp)e).e1);
		} 

		else if (e instanceof isvoid) {
			return getLetCaseDepth(((isvoid)e).e1);
		}

		else {
			return 0;
		}

		// else if (e instanceof int_const) {
		// 	return 0;
		// } else if (e instanceof bool_const) {
		// 	return 0;	
		// } else if (e instanceof string_const) {
		// 	return 0;
		// } else if (e instanceof new_) {
		// 	return 0;
		// } else if (e instanceof no_expr) {
		// 	return 0;
		// } else if (e instanceof object) {
		// 	return 0;
		// }
	}
}
