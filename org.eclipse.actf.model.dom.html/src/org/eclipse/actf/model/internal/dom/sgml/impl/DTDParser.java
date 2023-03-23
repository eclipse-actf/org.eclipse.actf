/*******************************************************************************
 * Copyright (c) 1998, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Goh KONDOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.dom.sgml.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.actf.model.dom.html.ParseException;
import org.eclipse.actf.model.internal.dom.sgml.ISGMLConstants;
import org.eclipse.actf.model.internal.dom.sgml.modelgroup.AndModelGroup;
import org.eclipse.actf.model.internal.dom.sgml.modelgroup.IModelGroup;
import org.eclipse.actf.model.internal.dom.sgml.modelgroup.OptModelGroup;
import org.eclipse.actf.model.internal.dom.sgml.modelgroup.OrModelGroup;
import org.eclipse.actf.model.internal.dom.sgml.modelgroup.PlusModelGroup;
import org.eclipse.actf.model.internal.dom.sgml.modelgroup.RepModelGroup;
import org.eclipse.actf.model.internal.dom.sgml.modelgroup.SeqModelGroup;

@SuppressWarnings("nls")
class DTDParser implements ISGMLConstants {
	private DTDTokenizer tokenizer;

	private Stack<DTDTokenizer> tokenizerStack = new Stack<DTDTokenizer>();

	private SGMLDocTypeDef dtd;

	DTDParser(DTDTokenizer tokenizer, SGMLDocTypeDef dtd) {
		this.tokenizer = tokenizer;
		this.dtd = dtd;
	}

	/**
	 * Reads a DTD. Only supports initially setted public entity. Real entity is
	 * ignored.
	 * 
	 * @return top ElementDefinition instance. If DOCTYPE declaration does not
	 * 
	 */
	final ElementDefinition readDTD() throws ParseException, IOException {
		ElementDefinition lastDef = null;
		loop: while (true) {
			if (tokenizer.nextToken() == EOF)
				break;
			while (tokenizer.ttype == COMMENT)
				tokenizer.nextToken();
			switch (tokenizer.ttype) {
			case MDO: // <!
				switch (tokenizer.nextToken()) {
				case NAME_CHAR:
					String str = tokenizer.sval;
					if (str.equalsIgnoreCase("ELEMENT")) { //
						lastDef = readElementDefinition();
					} else if (str.equalsIgnoreCase("ATTLIST")) {
						readAttributeList();
					} else if (str.equalsIgnoreCase("ENTITY")) {
						readEntity();
					} else {
						throw new DTDParseException("Unknown Declaration: "
								+ str);
					}
					// consume '>'
					while (tokenizer.nextToken() != TAGC)
						;
					break;
				case DSO: // [
					if (mark()) {
						if (tokenizer.nextToken() == '[') {
							tokenizer.switchTo(DEFAULT);
							lastDef = readDTD();
							tokenizer.switchTo(TAG);
							if (tokenizer.nextToken() == ']'
									&& tokenizer.nextToken() == ']'
									&& tokenizer.nextToken() == '>')
								break;
						}
					} else {
						if (tokenizer.nextToken() == '[') {
							tokenizer.skipToDSC();
							if (tokenizer.nextToken() == ']'
									&& tokenizer.nextToken() == '>')
								break;
						}
					}
					throw new DTDParseException("invalid mark region.");
				default:
					throw new DTDParseException("invalid declaration");
				}
				break;
			case '%':
				tokenizer.pushBack();
				SGMLEntityReference ref = readEntityReference();
				SGMLEntityDeclaration ed = ref.getEntityDeclaration();
				if (ed != null) {
					tokenizerStack.push(tokenizer);
					tokenizer = new DTDTokenizer(ed.getReplacementReader());
					readDTD();
					tokenizer = tokenizerStack.pop();
				}
				break;
			case STAGO: // <
			case ETAGO: // </
			case MISC: //
			default:
				tokenizer.pushBack();
				break loop;

			}
		}
		return lastDef;
	}

	private ElementDefinition readElementDefinition() throws ParseException,
			IOException {
		ElementDefinition defs[] = readElementType();
		boolean start = false;
		boolean end = false;
		boolean notXHTML = dtd.toString().indexOf("XHTML") < 0;

		if (notXHTML) {
			start = omit();
			end = omit();
		}
		IModelGroup content = exp();
		// System.out.println("read: "+defs[0].toString()+" "+
		// content.toString());

		// can't omit
		if (!notXHTML && content.toString().equalsIgnoreCase("EMPTY")) {
			end = true;
		}

		ElementDefinition exceptions[];
		for (int i = 0; i < defs.length; i++) {
			defs[i].setStartTag(start);
			defs[i].setEndTag(end);
			defs[i].setContentModel(content);
		}
		if (tokenizer.nextToken() == INCO) {
			exceptions = inclusion();
			for (int i = 0; i < defs.length; i++) {
				defs[i].setInclusion(exceptions);
			}
		} else if (tokenizer.ttype == EXCO) {
			exceptions = inclusion();
			for (int i = 0; i < defs.length; i++) {
				defs[i].setExclusion(exceptions);
			}
		} else {
			tokenizer.pushBack();
		}
		return defs[0];
	}

	private ElementDefinition[] readElementType() throws ParseException,
			IOException {
		if (tokenizer.nextToken() == LEFTPAR) {
			Vector<ElementDefinition[]> tmpVector = new Vector<ElementDefinition[]>();
			tmpVector.addElement(makeElementArray(atom()));
			while (tokenizer.nextToken() == '|')
				tmpVector.addElement(makeElementArray(atom()));
			if (tokenizer.ttype != ')')
				throw new DTDParseException("invalid ELEMENT type");
			int size = 0;
			for (Enumeration<ElementDefinition[]> e = tmpVector.elements(); e
					.hasMoreElements();)
				size += e.nextElement().length;
			ElementDefinition ret[] = new ElementDefinition[size];
			int i = 0;
			for (Enumeration<ElementDefinition[]> e = tmpVector.elements(); e
					.hasMoreElements();) {
				ElementDefinition defs[] = e.nextElement();
				for (int j = 0; j < defs.length; j++)
					ret[i++] = defs[j];
			}
			return ret;
		} else {
			tokenizer.pushBack();
			return makeElementArray(atom());
		}
	}

	/**
	 * Reads Entity.
	 * 
	 * <PRE>
	 * 
	 * &lt;!ENTITY ..... &gt; &circ; &circ; already read at this point. Reads until this point.
	 * 
	 * </PRE>
	 */
	private void readEntity() throws ParseException, IOException {
		String entityName;
		if (tokenizer.nextToken() != '%') {
			if (tokenizer.ttype == NAME_CHAR) {
				entityName = tokenizer.sval;
				int next = tokenizer.nextToken();
				if (next == NAME_CHAR
						&& tokenizer.sval.equalsIgnoreCase("CDATA")) {
					if (tokenizer.nextToken() == STRING) {
						// System.out.println(entityName+" : "+tokenizer.sval);
						dtd.putEntityDeclaration(entityName, tokenizer.sval);
						return;
					}
				} else if (next == STRING) {
					// add 040930 to support xhtml-*.ent
					// System.out.println(entityName+" : "+ tokenizer.sval);
					dtd.putEntityDeclaration(entityName, tokenizer.sval);
					return;
				}
			}
			throw new DTDParseException("invalid entity");
		} else if (tokenizer.nextToken() == NAME_CHAR) {
			entityName = tokenizer.sval;
			if (tokenizer.nextToken() != STRING) {
				if (tokenizer.ttype == NAME_CHAR
						&& tokenizer.sval.equalsIgnoreCase("PUBLIC")) {
					if (tokenizer.nextToken() == STRING) {
						String publicID = tokenizer.sval;
						String entityFileName = SGMLParser.pubEntityMap
								.get(publicID);
						if (entityFileName != null) {
							dtd.putPublicEntity(entityName, publicID,
									entityFileName);
						} else {
							System.out.println(entityName + " : " + publicID
									+ " : " + entityFileName);
							throw new DTDParseException("invalid entity");
						}
						// check syntax
						if (tokenizer.nextToken() == '>') {
							tokenizer.pushBack();
							return;
						} else if (tokenizer.ttype == STRING) {
							return;
						}
					}
				}
			} else { // if (tokenizer.nextToken() != STRING) {
				dtd.putEntityDeclaration(entityName, tokenizer.sval);
				return;
			}
		}
		throw new DTDParseException("invalid entity");
	}

	private SGMLEntityReference readEntityReference() throws ParseException,
			IOException {
		if (tokenizer.nextToken() == '%') {
			if (tokenizer.nextToken() == NAME_CHAR || tokenizer.ttype == PCDATA) {
				String str = tokenizer.sval;
				if (tokenizer.nextToken() != ';')
					tokenizer.pushBack();
				return dtd.getEntityReference(str);
			}
		}
		throw new DTDParseException("invalid entity reference");
	}

	private void readToken(AttributeDefinition attr) throws ParseException,
			IOException {
		if (tokenizer.nextToken() == NAME_CHAR || tokenizer.ttype == NUM) {
			attr.addNameToken(tokenizer.sval);
		} else if (tokenizer.ttype == '%') {
			tokenizer.pushBack();
			SGMLEntityReference er = readEntityReference();
			SGMLEntityDeclaration ed = er.getEntityDeclaration();
			tokenizerStack.push(tokenizer);
			tokenizer = new DTDTokenizer(ed.getReplacementReader(), TAG);
			readTokenGroup(attr);
			tokenizer = tokenizerStack.pop();
		} else {
			throw new DTDParseException("invalid token");
		}
	}

	private void readTokenGroup(AttributeDefinition attr)
			throws ParseException, IOException {
		readToken(attr);
		while (tokenizer.nextToken() != ')' && tokenizer.ttype != EOF) {
			if (tokenizer.ttype != '|')
				throw new DTDParseException("invalid token group");
			readToken(attr);
		}
	}

	/**
	 * Reads an exp EXP -> TERM [(, TERM)* | (| TERM)* | (& TERM)*]
	 */
	private IModelGroup exp() throws ParseException, IOException {
		IModelGroup ret;
		ret = term();
		// System.out.println("exp: "+ret.toString());
		int tmpC = tokenizer.nextToken();
		switch (tmpC) {
		case ',':
			SeqModelGroup seq = new SeqModelGroup(ret);
			seq.add(term());
			while (tokenizer.nextToken() == ',')
				seq.add(term());
			tokenizer.pushBack();
			if (seq.getChildLength() > dtd.maxSeqLength) {
				dtd.maxSeqLength = seq.getChildLength();
			}
			return seq;
		case '|':
			ret = new OrModelGroup(ret);
			((OrModelGroup) ret).add(term());
			while (tokenizer.nextToken() == '|')
				((OrModelGroup) ret).add(term());
			tokenizer.pushBack();
			return ret;
		case '&':
			ret = new AndModelGroup(ret);
			((AndModelGroup) ret).add(term());
			while (tokenizer.nextToken() == '&')
				((AndModelGroup) ret).add(term());
			tokenizer.pushBack();
			return ret;
		default:
			// System.out.println("expd: "+tmpC);
			tokenizer.pushBack();
			return ret;
		}
	}

	/**
	 * TERM -> FACTOR [? | + | *]
	 */
	private IModelGroup term() throws ParseException, IOException {
		IModelGroup ret = factor();
		// System.out.println("term ret: "+ret.toString());
		int tmpC = tokenizer.nextToken();
		// System.out.println("term next: "+tmpC+" "+(char)tmpC);
		switch (tmpC) {
		case QUESTION:
			// System.out.println("term: ?");
			ret = new OptModelGroup(ret);
			break;
		case PLUS:
			// System.out.println("term: +");
			ret = new PlusModelGroup(ret);
			break;
		case MULTI:
			// System.out.println("term: *");
			ret = new RepModelGroup(ret);
			break;
		default:
			// System.out.println("term: error");
			tokenizer.pushBack();
		}
		return ret;
	}

	/**
	 * Reads a factor FACTOR -> ATOM (EXP) PCDATA CDATA EMPTY
	 */
	private IModelGroup factor() throws ParseException, IOException {
		switch (tokenizer.nextToken()) {
		case LEFTPAR:
			IModelGroup ret = exp();
			if (tokenizer.nextToken() != ')') {
				// System.out.println("factor: error");
				throw new DTDParseException("ret: " + ret.toString()
						+ " sval: " + tokenizer.sval + " ttype: "
						+ tokenizer.ttype);
			} else {
				// System.out.println("factor: "+ret.toString());
				return ret;
			}
		case CDATA:
			return SGMLParser.cdata;
		case NUM: // #PCDATA
			if (tokenizer.sval.equalsIgnoreCase("#PCDATA")) {
				return SGMLParser.pcdata;
			} else {
				throw new DTDParseException("sval: " + tokenizer.sval
						+ " ttype: " + tokenizer.ttype);
			}
		default:
			if (tokenizer.ttype == NAME_CHAR
					&& tokenizer.sval.equalsIgnoreCase("EMPTY")) {
				return SGMLParser.empty;
			} else if (tokenizer.ttype == NAME_CHAR
					&& tokenizer.sval.equalsIgnoreCase("CDATA")) {
				return SGMLParser.cdata;
			}
			tokenizer.pushBack();
			return atom();
		}
	}

	/**
	 * Reads atom. ATOM -> <NAME_CHAR> ENTITY_REFERENCE
	 */
	private IModelGroup atom() throws ParseException, IOException {
		if (tokenizer.nextToken() == NAME_CHAR) {
			return dtd.createElementDefinition(tokenizer.sval);
		} else if (tokenizer.ttype == '%') {
			tokenizer.pushBack();
			SGMLEntityReference er = readEntityReference();
			SGMLEntityDeclaration ed = er.getEntityDeclaration();
			IModelGroup ret = ed.getReplacementSubtree();
			if (ret == null) {
				tokenizerStack.push(tokenizer);
				tokenizer = new DTDTokenizer(ed.getReplacementReader(), TAG);
				ret = exp();
				ed.setReplacementSubtree(ret);
				tokenizer = tokenizerStack.pop();
			}
			return ret;
		} else {
			throw new DTDParseException("sval: " + tokenizer.sval + " ttype: "
					+ tokenizer.ttype);
		}
	}

	private void declaredValue(AttributeDefinition attr) throws ParseException,
			IOException {
		if (tokenizer.nextToken() == NAME_CHAR) {
			String str = tokenizer.sval;
			if (str.equalsIgnoreCase("CDATA")) {
				attr.setDeclaredType(AttributeDefinition.CDATA);
			} else if (str.equalsIgnoreCase("ID")) {
				attr.setDeclaredType(AttributeDefinition.ID);
			} else if (str.equalsIgnoreCase("IDREF")) {
				attr.setDeclaredType(AttributeDefinition.IDREF);
			} else if (str.equalsIgnoreCase("IDREFS")) {
				attr.setDeclaredType(AttributeDefinition.IDREFS);
			} else if (str.equalsIgnoreCase("NAME")) {
				attr.setDeclaredType(AttributeDefinition.NAME);
			} else if (str.equalsIgnoreCase("NUMBER")) {
				attr.setDeclaredType(AttributeDefinition.NUMBER);
			} else if (str.equalsIgnoreCase("NMTOKEN")) {
				attr.setDeclaredType(AttributeDefinition.NMTOKEN);
			} else if (str.equalsIgnoreCase("NAMES")) {
				attr.setDeclaredType(AttributeDefinition.NAMES);
			} else {
				throw new DTDParseException(tokenizer.getCurrentLine()
						+ ": unknown declared value " + str);
			}
		} else if (tokenizer.ttype == '(') {
			attr.setDeclaredType(AttributeDefinition.NAME_TOKEN_GROUP);
			readTokenGroup(attr);
		} else if (tokenizer.ttype == '%') {
			tokenizer.pushBack();
			SGMLEntityReference er = readEntityReference();
			SGMLEntityDeclaration ed = er.getEntityDeclaration();
			tokenizerStack.push(tokenizer);
			tokenizer = new DTDTokenizer(ed.getReplacementReader(), TAG);
			declaredValue(attr);
			tokenizer = tokenizerStack.pop();
		} else {
			throw new DTDParseException("at " + tokenizer);
		}
	}

	private void defaultValue(AttributeDefinition attr) throws ParseException,
			IOException {
		switch (tokenizer.nextToken()) {
		case NAME_CHAR:
			attr.setDefaultValue(tokenizer.sval);
			break;
		case STRING:
			String str = tokenizer.sval;
			break;
		case NUM:
			str = tokenizer.sval;
			if (str.equalsIgnoreCase("#REQUIRED")) {
				attr.setDefaultType(AttributeDefinition.REQUIRED);
			} else if (str.equalsIgnoreCase("#IMPLIED")) {
				attr.setDefaultType(AttributeDefinition.IMPLIED);
			} else if (str.equalsIgnoreCase("#FIXED")) {
				if (tokenizer.nextToken() == EOF) {
					tokenizer = tokenizerStack.peek();
				} else {
					tokenizer.pushBack();
				}
				if (tokenizer.nextToken() != STRING) {
					throw new DTDParseException(tokenizer.getCurrentLine()
							+ ": STRING must follow #FIXED");
				}
				attr.setDefaultType(AttributeDefinition.FIXED);
				str = tokenizer.sval;
				// data is entity?
				tokenizerStack.push(tokenizer);
				tokenizer = new DTDTokenizer(new StringReader(str), TAG);
				SGMLEntityReference ref;
				try {
					ref = readEntityReference();
					attr.setDefaultValue(ref.getEntityDeclaration()
							.getReplacementString());
				} catch (ParseException e) {
					attr.setDefaultValue(str);
				}
				tokenizer = tokenizerStack.pop();
			} else {
				attr.setDefaultValue(str);
			}

		}
	}

	private void readAttributeList() throws ParseException, IOException {
		readAttributeList(readElementType());
	}

	/**
	 * read lines in attlist.
	 */
	private AttributeDefinition[] readAttributeList(ElementDefinition defs[])
			throws ParseException, IOException {
		Vector<AttributeDefinition> v = new Vector<AttributeDefinition>();
		while (tokenizer.nextToken() != '>' && tokenizer.ttype != EOF) {
			tokenizer.pushBack();
			AttributeDefinition[] ads = attributeDefinitionList(defs);
			for (int i = 0; i < ads.length; i++)
				v.addElement(ads[i]);
		}
		tokenizer.pushBack();
		AttributeDefinition ret[] = new AttributeDefinition[v.size()];
		v.copyInto(ret);
		return ret;
	}

	/**
	 * read each line in attlist.
	 */
	private AttributeDefinition[] attributeDefinitionList(
			ElementDefinition defs[]) throws ParseException, IOException {
		if (tokenizer.nextToken() == NAME_CHAR) {
			AttributeDefinition ad = new AttributeDefinition(tokenizer.sval);
			declaredValue(ad);
			defaultValue(ad);
			for (int i = 0; i < defs.length; i++)
				defs[i].addAttributeDefinition(ad);
			AttributeDefinition ads[] = new AttributeDefinition[] { ad };
			return ads;
		} else if (tokenizer.ttype == '%') {
			tokenizer.pushBack();
			SGMLEntityReference er = readEntityReference();
			SGMLEntityDeclaration ed = er.getEntityDeclaration();
			tokenizerStack.push(tokenizer);
			tokenizer = new DTDTokenizer(ed.getReplacementReader(), TAG);
			AttributeDefinition ads[] = readAttributeList(defs);
			tokenizer = tokenizerStack.pop();
			return ads;
		} else {
			throw new DTDParseException("illegal attlist.");
		}

	}

	private boolean mark() throws ParseException, IOException {
		if (tokenizer.nextToken() == NAME_CHAR) {
			if (tokenizer.sval.equalsIgnoreCase("INCLUDE")) {
				return true;
			} else if (tokenizer.sval.equalsIgnoreCase("IGNORE")) {
				return false;
			} else {
				throw new DTDParseException("last sval: " + tokenizer.sval);
			}
		} else if (tokenizer.ttype == '%') {
			tokenizer.pushBack();
			SGMLEntityReference er = readEntityReference();
			SGMLEntityDeclaration ed = er.getEntityDeclaration();
			tokenizerStack.push(tokenizer);
			tokenizer = new DTDTokenizer(ed.getReplacementReader(), TAG);
			boolean ret = mark();
			tokenizer = tokenizerStack.pop();
			return ret;
		} else {
			throw new DTDParseException("sval: " + tokenizer.sval + " ttype: "
					+ tokenizer.ttype);
		}
	}

	private ElementDefinition[] makeElementArray(IModelGroup model)
			throws ParseException {
		if (model instanceof ElementDefinition) {
			ElementDefinition ret[] = { (ElementDefinition) model };
			return ret;
		} else if (model instanceof OrModelGroup) {
			return ((OrModelGroup) model).getChildren();
		}
		throw new DTDParseException("sval: " + tokenizer.sval + " ttype: "
				+ tokenizer.ttype);
	}

	/**
	 * Reads a token that represents whether tag is omittable or not. OMIT -> -
	 * O
	 */
	private boolean omit() throws ParseException, IOException {
		switch (tokenizer.nextToken()) {
		case OMITTABLE:
			return true;
		case MINUS:
			return false;
		default:
			throw new DTDParseException("'O' or '-' is required at "
					+ tokenizer.getCurrentLine());
		}
	}

	private ElementDefinition[] inclusion() throws ParseException, IOException {
		Vector<ElementDefinition[]> tmpVector = new Vector<ElementDefinition[]>();
		tmpVector.addElement(makeElementArray(atom()));
		while (tokenizer.nextToken() == '|')
			tmpVector.addElement(makeElementArray(atom()));
		if (tokenizer.ttype != ')')
			throw new DTDParseException("sval: " + tokenizer.sval + " ttype: "
					+ tokenizer.ttype);
		int size = 0;
		for (Enumeration<ElementDefinition[]> e = tmpVector.elements(); e
				.hasMoreElements();)
			size += e.nextElement().length;
		ElementDefinition ret[] = new ElementDefinition[size];
		int i = 0;
		for (Enumeration<ElementDefinition[]> e = tmpVector.elements(); e
				.hasMoreElements();) {
			ElementDefinition defs[] = e.nextElement();
			for (int j = 0; j < defs.length; j++)
				ret[i++] = defs[j];
		}
		return ret;
	}
}
