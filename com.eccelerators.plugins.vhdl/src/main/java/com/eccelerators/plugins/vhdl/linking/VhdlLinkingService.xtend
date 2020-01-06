package com.eccelerators.plugins.vhdl.linking

import org.eclipse.xtext.linking.impl.DefaultLinkingService
import java.util.Collections
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.nodemodel.INode
import org.eclipse.xtext.linking.impl.IllegalNodeException
import java.util.List
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import com.eccelerators.plugins.vhdl.vhdl.VhdlFactory
import com.eccelerators.plugins.vhdl.vhdl.TypeIdentifierReference
import com.eccelerators.plugins.vhdl.vhdl.ParameterizedTypeIdentifierReference
import com.eccelerators.plugins.vhdl.vhdl.IdentifierReference
import java.util.ArrayList
import com.eccelerators.plugins.vhdl.vhdl.Identifier
import com.eccelerators.plugins.vhdl.vhdl.TypeIdentifier
import com.eccelerators.plugins.vhdl.vhdl.ComponentDeclaration

class VhdlLinkingService extends DefaultLinkingService {
	//
	// FIXME: Distinguish between signals, functions, casts, etc.
	//
	static final String BUILTIN_TYPE_URI = "com.eccelerators.plugins.vhdl.builtin:/inmemory.vhd";
	
	final List<String> _typeNames = createListOfTypeNames();
	final List<String> _identifierNames = createListOfIdentifierNames();

	final List<EObject> _types = fillListWithTypes(_typeNames);
	final List<EObject> _identifiers = fillListWithIdentifiers(_identifierNames);

	override List<EObject> getLinkedObjects(EObject context, EReference ref, INode node) throws IllegalNodeException {
		var list = super.getLinkedObjects(context, ref, node);

		if (!list.isEmpty()) {
			return list;
		}
		
		switch context {
			ComponentDeclaration : {
				list = createObject(context, node);
			}
			TypeIdentifierReference,
			ParameterizedTypeIdentifierReference : {
				list = getBuiltInObjects(context, node, _types);
			}
			IdentifierReference : {
				list = getBuiltInObjects(context, node, _identifiers);
			}
		}

		return list;
	}

	def List<String> createListOfTypeNames() {
		var typeNames = new ArrayList<String>();
		
		//
		// Types taken from: STANDARD package (predefined in the compiler)
		//
		// Predefined enumeration types
		//
		typeNames.add("boolean");
		typeNames.add("bit");
		typeNames.add("character");
		typeNames.add("severity_level");
		//
		// Predefined numeric types
		//
		typeNames.add("integer");
		typeNames.add("$universal_integer");
		typeNames.add("$universal_real");
		typeNames.add("real");
		typeNames.add("time");
		typeNames.add("$natural_time");
		typeNames.add("delay_length");
		//
		// Predefined numeric subtypes
		//
		typeNames.add("natural");
		typeNames.add("positive");
		typeNames.add("string");
		typeNames.add("bit_vector");
		typeNames.add("file_open_kind");
		typeNames.add("file_open_status");
		//
		// Types taken from: numeric_std.vhd, 
		//				   std_logic_arith.vhd
		//
		// Numeric array type definitions
		//
		typeNames.add("unsigned");
		typeNames.add("signed");
		typeNames.add("small_int");
		//
		// Types taken from: std_logic_1164.vhd
		//
		// Logic state system
		//
		typeNames.add("std_ulogic");
		typeNames.add("std_ulogic_vector");
		typeNames.add("std_logic");
		typeNames.add("std_logic_vector");
		//
		// Subtypes
		//
		typeNames.add("X01");
		typeNames.add("X01Z");
		typeNames.add("UX01");
		typeNames.add("UX01Z");
		//
		// Types taken from: std_logic_misc.vhd
		//
		typeNames.add("strength");
		typeNames.add("minomax");
		//
		// Types taken from: textio.vhd
		//
		typeNames.add("LINE");
		typeNames.add("TEXT");
		typeNames.add("SIDE");
		typeNames.add("WIDTH");
		
		return typeNames;
	}
	
	def List<String> createListOfIdentifierNames() {
		var identifierNames = new ArrayList<String>();
		
		identifierNames.add("character\'pos");
		//
		// Functions take from: STANDARD package (predefined in the compiler)
		//
		identifierNames.add("now");
		//
		// Functions taken from: std_logic_1164.vhd
		//
		identifierNames.add("resolved");
		//
		// Conversion functions
		//
		identifierNames.add("to_bit");
		identifierNames.add("to_bitvector");
		identifierNames.add("to_stdulogic");
		identifierNames.add("to_stdlogicvector");
		identifierNames.add("to_stdulogicvector");
		//
		// Strength strippers and type converters
		//
		identifierNames.add("to_x01");
		identifierNames.add("to_x01z");
		identifierNames.add("to_ux01");
		//
		// Edge detection
		//
		identifierNames.add("rising_edge");
		identifierNames.add("falling_edge");
		//
		// Object contains an unknown
		//
		identifierNames.add("is_x");
		// 
		// Functions taken from: std_logic_unsigned.vhd
		// 
		identifierNames.add("shr");
		identifierNames.add("shl");
		identifierNames.add("conv_integer");
		//
		// Functions take from: std_logic_arith.vhd
		//
		identifierNames.add("conv_unsigned");
		identifierNames.add("conv_signed");
		identifierNames.add("conv_std_logic_vector");
		identifierNames.add("ext");
		identifierNames.add("sxt");
		//
		// Functions take from: numeric_bit.vhd
		//
		identifierNames.add("shift_left");
		identifierNames.add("shift_right");
		identifierNames.add("rotate_left");
		identifierNames.add("rotate_right");
		identifierNames.add("sll");
		identifierNames.add("srl");
		identifierNames.add("rol");
		identifierNames.add("ror");
		identifierNames.add("resize");
		//
		// Conversion functions
		//
		identifierNames.add("to_integer");
		identifierNames.add("to_unsigned");
		identifierNames.add("to_signed");
		identifierNames.add("to_stdlogicvector");
		//
		// Match functions
		//
		identifierNames.add("std_match");
		//
		// Functions taken from: std_logic_misc.vhd
		//
		identifierNames.add("strength_map");
		identifierNames.add("drive");
		identifierNames.add("sense");
		identifierNames.add("STD_LOGIC_VECTORtoBIT_VECTOR");
		identifierNames.add("STD_ULOGIC_VECTORtoBIT_VECTOR");
		identifierNames.add("STD_ULOGICtoBIT");
		identifierNames.add("and_reduce");
		identifierNames.add("nand_reduce");
		identifierNames.add("or_reduce");
		identifierNames.add("nor_reduce");
		identifierNames.add("xor_reduce");
		identifierNames.add("xnor_reduce");
		identifierNames.add("fun_BUF3S");
		identifierNames.add("fun_BUF3SL");
		identifierNames.add("fun_MUX2x1");
		identifierNames.add("fun_MAJ23");
		identifierNames.add("fun_WiredX");
		//
		// Identifiers taken from: textio.vhd
		//
		identifierNames.add("WRITE_MODE");
		identifierNames.add("READ_MODE");
		//
		// Procedures / functions taken from: textio.vhd
		//
		identifierNames.add("WRITELINE");
		identifierNames.add("WRITE");
		identifierNames.add("READLINE");
		identifierNames.add("READ");
		identifierNames.add("ENDLINE");
		identifierNames.add("LEFT");
		identifierNames.add("RIGHT");
		identifierNames.add("ENDFILE");
		identifierNames.add("FILE_CLOSE");
		identifierNames.add("HREAD");
		//
		// FIXME: Built-in identifiers that should be put into their specializations 
		//		(e.g. FunctionIdentifierReference, TypeIdentifierReference, etc.)
		//		and only be referenced from there!!!
		//
		identifierNames.add("unsigned");
		identifierNames.add("signed");
		identifierNames.add("integer");
		identifierNames.add("std_logic_vector");
		identifierNames.add("natural");
		identifierNames.add("bit");
		identifierNames.add("failure");
		identifierNames.add("false");
		identifierNames.add("true");
		identifierNames.add("string");
		
		return identifierNames;
	}
	
	def List<EObject> fillListWithTypes(List<String> typeNames) {
		var list = new ArrayList<EObject>;
		
		for(String typeName : typeNames) {
			var type = VhdlFactory.eINSTANCE.createTypeIdentifier();
			type.name = typeName;
			list.add(type);
		}
		
		return list;
	}
	
	def List<EObject> fillListWithIdentifiers(List<String> identifierNames) {
		var list = new ArrayList<EObject>;
		
		for(String identifierName : identifierNames) {
			var identifier = VhdlFactory.eINSTANCE.createIdentifier();
			identifier.name = identifierName;
			list.add(identifier);
		}
		
		return list;
	}
	
	def List<EObject> getBuiltInObjects(EObject context, INode node, List<EObject> objects) {
		var list = Collections.<EObject>emptyList();
		val uri = URI.createURI(BUILTIN_TYPE_URI);
		var resource = context.eResource().getResourceSet().getResource(uri, false);

		if (resource === null) {
			resource = context.eResource().getResourceSet().createResource(uri);
		}

		val token = NodeModelUtils.getTokenText(node);

		for(EObject object : objects) {
			switch object {
				// FIXME: Maybe TypeIdentifier should inherit from Identifier!!!
				Identifier : {
					if (object.name.equalsIgnoreCase(token)) {
						resource.getContents().add(object);
						list = Collections.singletonList(object);
					}
				}
				TypeIdentifier : {
					if (object.name.equalsIgnoreCase(token)) {
						resource.getContents().add(object);
						list = Collections.singletonList(object);
					}
				}
			}
		}
		
		return list;
	}

	def List<EObject> createObject(EObject context, INode node) {
		var list = Collections.<EObject>emptyList();
		val uri = URI.createURI(BUILTIN_TYPE_URI);
		var resource = context.eResource().getResourceSet().getResource(uri, false);

		if (resource === null) {
			resource = context.eResource().getResourceSet().createResource(uri);
		}

		val tokenText = NodeModelUtils.getTokenText(node);

		var entityDeclaration = VhdlFactory.eINSTANCE.createEntityDeclaration();
		entityDeclaration.name = tokenText;
		resource.getContents().add(entityDeclaration);
		list = Collections.singletonList(entityDeclaration);
		
		return list;
	}
}
