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
import java.io.Reader;
import java.util.Hashtable;

import org.eclipse.actf.model.dom.html.ParseException;

/**
 * This class represents Document Type Definition and holds <code>
 * ElementntDefinition </code>
 * instances created by the DTD, entity declarations and so on.
 */
public class SGMLDocTypeDef {
	private static Hashtable<String, SGMLDocTypeDef> table = new Hashtable<String, SGMLDocTypeDef>();

	private SGMLParser parser;

	/**
	 * key: element name value: ElementDefinition instance
	 */
	protected UnsynchronizedHashtable elementDefs = new UnsynchronizedHashtable();

	private Hashtable<String, SGMLEntityReference> entityRefs = new Hashtable<String, SGMLEntityReference>();

	private Hashtable<String, SGMLEntityDeclaration> entityDecls = new Hashtable<String, SGMLEntityDeclaration>();

	private String id;

	protected SGMLDocTypeDef(SGMLParser parser, String id) {
		this.parser = parser;
		this.id = id;
	}

	static SGMLDocTypeDef createAnonymous(SGMLParser parser) {
		return new SGMLDocTypeDef(parser, "anonymous"); //$NON-NLS-1$
	}

	/**
	 * Gets ElementDefinition instance with specified name. There is only one
	 * ElementDefinition instance that has the same name. So if a definition
	 * that has specified name already exists, return that variable. If not make
	 * new instance. Using singleton pattern.
	 * 
	 * @param name
	 *            name of element defined by a object.
	 * @return singleton instance.
	 */
	public ElementDefinition createElementDefinition(String name) {
		name = name.toUpperCase();
		ElementDefinition ret = elementDefs.get(name);
		if (ret == null) {
			ret = new ElementDefinition(name);
			elementDefs.put(name, ret);
		}
		return ret;
	}

	/**
	 * Creates public <code>SGMLDocTypeDef</code> instance.
	 * 
	 * @param publicID
	 *            id related to public entity. For example <code>
	 * "-//W3C//DTD HTML 4.0 Transitional//EN" </code>
	 */
	public static SGMLDocTypeDef createPublic(String publicID, SGMLParser parser) {
		return new SGMLDocTypeDef(parser, publicID);
	}

	static void putPublic(String publicID, SGMLDocTypeDef dtd) {
		table.put(publicID, dtd);
	}

	/**
	 * Gets the ElementDefinition instance related to the argument.
	 * 
	 * @param name
	 *            ElementDefinition's name.
	 * @return ElementDefinition named name. If no instance, return null.
	 */
	public ElementDefinition getElementDefinition(String name) {
		return elementDefs.get(name);
	}

	/**
	 * Gets the ElementDefinition instance whose Magic Number is specifed.
	 * 
	 * @param num
	 *            ElementDefinition's Magic Number.
	 * @return ElementDefinition named name. If no instance, return null.
	 */
	public ElementDefinition getElementDefinition(int num) {
		try {
			return elementDefs.get(num);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	SGMLEntityDeclaration getEntityDeclaration(String entityName) {
		return entityDecls.get(entityName);
	}

	SGMLEntityReference getEntityReference(String name) throws ParseException {
		SGMLEntityReference ret = entityRefs.get(name);
		if (ret == null) {
			SGMLEntityDeclaration ed = getEntityDeclaration(name);
			if (ed == null) {
				throw new DTDParseException("No Entity Definition: " + name); //$NON-NLS-1$
			}
			ret = new SGMLEntityReference(name, ed);
			entityRefs.put(name, ret);
		}
		return ret;
	}

	/**
	 * Gets public <code>SGMLDocTypeDef</code> instance.
	 * 
	 * @param publicID
	 *            id related to public entity. For example <code>
	 * "-//W3C//DTD HTML 4.0 Transitional//EN" </code>
	 */
	public static SGMLDocTypeDef getPublic(String publicID) {
		return table.get(publicID);
	}

	void putEntityDeclaration(String entityName, String entityString) {
		if (entityDecls.get(entityName) != null)
			return;
		SGMLEntityDeclaration dec = new SGMLEntityDeclaration(entityName,
				entityString, this);
		entityDecls.put(entityName, dec);
	}

	void putPublicEntity(String entityName, String entityString, String filename) {
		if (entityDecls.get(entityName) != null)
			return;
		class PUBLICEntity extends SGMLEntityDeclaration {
			String filename;

			Reader getReplacementReader() throws IOException {
				return parser.getResource(filename);
			}

			PUBLICEntity(String entityName, String entityString,
					String filename, SGMLDocTypeDef dtd) {
				super(entityName, entityString, dtd);
				this.filename = filename;
			}
		}
		;
		entityDecls.put(entityName, new PUBLICEntity(entityName, entityString,
				filename, this));
	}

	/**
	 * returns its public id. For example "-//W3C//DTD HTML 4.0
	 * Transitional//EN".
	 * 
	 * @return public id.
	 */
	public String toString() {
		return id;
	}

	/**
	 * Checks if a element is singleton or not. A singleton element appears only
	 * once in a document. For example, HTML, HEAD and BODY are singleton in
	 * HTML, but not TABLE, LI.
	 * 
	 * @param tagName
	 *            element name to be checked.
	 */
	public boolean singleton(String tagName) {
		ElementDefinition ed = getElementDefinition(tagName);
		return ed != null && ed.isSingleton();
	}

	private int elementNum;

	/**
	 * @return number of elements this DTD defines
	 */
	public int getElementCount() {
		return elementNum;
	}

	void rehash() {
		elementNum = elementDefs.size();
		int size = elementNum + 2; // The second last element is for #PCDATA.
		// The last element is for ANONYMOUS.
		for (int i = elementNum - 1; i >= 0; i--) {
			elementDefs.get(i).rehash(size);
		}
	}

	int maxSeqLength = 0;
}