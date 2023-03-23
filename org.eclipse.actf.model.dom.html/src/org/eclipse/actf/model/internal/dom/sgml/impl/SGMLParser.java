/*******************************************************************************
 * Copyright (c) 1998, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Goh KONDOH - initial API and implementation
 *    Kentarou FUKUDA - html5 support
 *******************************************************************************/

package org.eclipse.actf.model.internal.dom.sgml.impl;

import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Pattern;

import org.eclipse.actf.model.dom.html.DocumentTypeUtil;
import org.eclipse.actf.model.dom.html.IErrorHandler;
import org.eclipse.actf.model.dom.html.IErrorLogListener;
import org.eclipse.actf.model.dom.html.IParser;
import org.eclipse.actf.model.dom.html.IParserError;
import org.eclipse.actf.model.dom.html.ParseException;
import org.eclipse.actf.model.internal.dom.sgml.ISGMLConstants;
import org.eclipse.actf.model.internal.dom.sgml.ISGMLParser;
import org.eclipse.actf.model.internal.dom.sgml.errorhandler.AttributeValueErrorHandler;
import org.eclipse.actf.model.internal.dom.sgml.errorhandler.DefaultErrorHandler;
import org.eclipse.actf.model.internal.dom.sgml.errorhandler.ITokenErrorHandler;
import org.eclipse.actf.model.internal.dom.sgml.modelgroup.AndModelGroup.AndContext;
import org.eclipse.actf.model.internal.dom.sgml.modelgroup.IModelGroup;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/**
 * Pure SGML parser. Base class of a parser which parses a SGML derived markup
 * language. Language-dependent customization have to be provided by a subclass.
 */
@SuppressWarnings("nls")
public class SGMLParser implements ISGMLConstants, ISGMLParser {

	private boolean isXHTML = false;

	private boolean isEmptyElement = true;

	private boolean isEndWithSlash = false;

	private String currentTagName = "";

	private DOMImplementation domImpl;

	private static Class<?> createDocumentMethodParamTypes[] = { String.class, String.class, DocumentType.class };

	private static final Pattern VOID_ELEMENT_PATTERN = Pattern.compile("area|col|embed|link|source|track|wbr");

	/**
	 * @return <code>null</code> if failed.
	 */
	public DOMImplementation setDOMImplementation(DOMImplementation domImpl) {
		Class<DOMImplementation> domImpleInterface = DOMImplementation.class;
		try {
			java.lang.reflect.Method createDocumentMethod = domImpleInterface.getMethod("createDocument",
					createDocumentMethodParamTypes);
			if (createDocumentMethod != null) {
				this.domImpl = domImpl;
				doc = null;
				return domImpl;
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * @return A DOMImplementation instace this parser users.
	 */
	public DOMImplementation getDOMImplementation() {
		return this.domImpl;
	}

	/**
	 * Constructs this instance and {@link DefaultErrorHandler}instance is added.
	 */
	public SGMLParser() {
		addErrorHandler(new DefaultErrorHandler());
		addTokenErrorHandler(new AttributeValueErrorHandler());
		setDOMImplementation(SGMLDOMImpl.getDOMImplementation());
		if (getDOMImplementation() == null) {
			setDocument(new SGMLDocument());
		}
		anonymousElementDef = new ElementDefinition("ANONYMOUS", any);
	}

	public IErrorHandler errorHandlers[] = new IErrorHandler[8];

	private ITokenErrorHandler tokenErrorHandlers[] = new ITokenErrorHandler[8];

	public int errorHandlerNum = 0;

	private int tokenErrorHandlerNum = 0;

	/**
	 * Sets an error handler that recovers error.
	 * 
	 * @param errorHandler
	 * @deprecated {@link #addErrorHandler(org.eclipse.actf.model.dom.html.IErrorHandler)}
	 */
	public void setErrorHandler(IErrorHandler errorHandler) {
		addErrorHandler(errorHandler);
	}

	/**
	 * Adds an ErrorHandler instance. An errorHandler added later is invoked earlier
	 * by this parser instance than errorHandlers added earlier. If one errorHandler
	 * handles error (eg. returns <code>
	 * true</code>), no more errorHandlers are invoked.
	 * 
	 * @param errorHandler errorHandler instance to be added to this parser
	 */
	public void addErrorHandler(IErrorHandler errorHandler) {
		if (errorHandlerNum == errorHandlers.length) {
			IErrorHandler newErrorHandlers[] = new IErrorHandler[errorHandlers.length * 2];
			for (int i = 0; i < errorHandlerNum; i++) {
				newErrorHandlers[i] = errorHandlers[i];
			}
			errorHandlers = newErrorHandlers;
		}
		this.errorHandlers[errorHandlerNum++] = errorHandler;
	}

	/**
	 * Adds an ErrorHandler instance. An errorHandler added later is invoked earlier
	 * by this parser instance than errorHandlers added earlier. If one errorHandler
	 * handles error (eg. returns <code>
	 * true</code>), no more errorHandlers are invoked.
	 * 
	 * @param errorHandler errorHandler instance to be added to this parser
	 */
	public void addTokenErrorHandler(ITokenErrorHandler errorHandler) {
		if (tokenErrorHandlerNum == tokenErrorHandlers.length) {
			ITokenErrorHandler newErrorHandlers[] = new ITokenErrorHandler[errorHandlers.length * 2];
			for (int i = 0; i < tokenErrorHandlerNum; i++) {
				newErrorHandlers[i] = tokenErrorHandlers[i];
			}
			tokenErrorHandlers = newErrorHandlers;
		}
		this.tokenErrorHandlers[tokenErrorHandlerNum++] = errorHandler;
	}

	/**
	 * Gets node-level error handlers included in this parser
	 * 
	 * @return error handlers
	 */
	public IErrorHandler[] getErrorHandlers() {
		IErrorHandler ret[] = new IErrorHandler[errorHandlerNum];
		for (int i = 0; i < errorHandlerNum; i++) {
			ret[i] = errorHandlers[i];
		}
		return ret;
	}

	/**
	 * Gets token-level error handlers included in this parser.
	 */
	public ITokenErrorHandler[] getTokenErrorHandlers() {
		ITokenErrorHandler ret[] = new ITokenErrorHandler[errorHandlerNum];
		for (int i = 0; i < tokenErrorHandlerNum; i++) {
			ret[i] = tokenErrorHandlers[i];
		}
		return ret;
	}

	/**
	 * Removes a node-level error handler.
	 * 
	 * @param errorHandler error handler to remove
	 */
	public void removeErrorHandler(IErrorHandler errorHandler) {
		for (int i = 0; i < errorHandlerNum; i++) {
			if (errorHandlers[i] == errorHandler) {
				errorHandlers[i] = null;
				for (i++; i < errorHandlerNum; i++) {
					errorHandlers[i - 1] = errorHandlers[i];
				}
				errorHandlerNum--;
				return;
			}
		}
	}

	/**
	 * Removes a token-level error handler.
	 * 
	 * @param errorHandler error handler to remove
	 */
	public void removeTokenErrorHandler(ITokenErrorHandler errorHandler) {
		for (int i = 0; i < tokenErrorHandlerNum; i++) {
			if (tokenErrorHandlers[i] == errorHandler) {
				tokenErrorHandlers[i] = null;
				for (i++; i < tokenErrorHandlerNum; i++) {
					tokenErrorHandlers[i - 1] = tokenErrorHandlers[i];
				}
				errorHandlerNum--;
				return;
			}
		}
	}

	/**
	 * This variable is for debugging.
	 */
	public static final boolean _DEBUG = false;

	private boolean extractNum = true;

	private boolean extractChar = true;

	/**
	 * Determines if this parser extracts both character and number entities or not.
	 * Default value is <code>true</code>.
	 */
	public void extractEntity(boolean b) {
		this.extractChar = this.extractNum = b;
	}

	/**
	 * Sets if it parses and extracts number entities or not. By default, it parses
	 * and number character entities.
	 * 
	 * @param b if true, extracts number entities.
	 */
	public void extractNumEntity(boolean b) {
		this.extractNum = b;
	}

	/**
	 * Checks if it parses and extracts number entities or not. By default, it
	 * parses and extracts number entities.
	 * 
	 * @return true if extracts number entities. Otherwise false
	 */
	public boolean extractNumEntity() {
		return this.extractNum;
	}

	/**
	 * Sets if it parses and extracts character entities or not. By default, it
	 * parses and extracts character entities.
	 * 
	 * @param b if true, extracts character entities.
	 */
	public void extractCharEntity(boolean b) {
		this.extractChar = b;
	}

	/**
	 * Checks if it parses and extracts character entities or not. By default, it
	 * parses and extracts character entities.
	 * 
	 * @return true if extracts character entities. Otherwise false
	 */
	public boolean extractCharEntity() {
		return extractChar;
	}

	public InsTokenizer tokenizer;

	private Document doc = null;

	/**
	 * Public entities are stored in this variable. Keys are id and values are file
	 * name. Both are instances of <code>java.lang.String</code>. For example, for
	 * the key "-//W3C//DTD HTML 4.0 Transitional//EN", the value is "loose.dtd"
	 */
	public static Hashtable<String, String> pubEntityMap = new Hashtable<String, String>();

	/**
	 * Gets public entity map.
	 * 
	 * @return Hashtable instance whose keys are public id and values are file name.
	 */
	public static Hashtable<String, String> getPublicEntityMap() {
		return pubEntityMap;
	}

	/**
	 * Top element's ElementDefinition.
	 */
	public ElementDefinition lastDef = null;

	static IModelGroup pcdata = new IModelGroup() {
		public boolean match(ISGMLParser parser, Node parent, Node child) {
			if (child instanceof Text && !(child instanceof CDATASection)) {
				parent.appendChild(child);
				return true;
			} else {
				return false;
			}
		}

		public boolean optional() {
			return false;
		}

		public void refer(boolean infinite) {
		}

		public String toString() {
			return "#PCDATA";
		}

		public boolean match(int number) {
			return true;
		}

		public boolean[] rehash(int totalSize) {
			boolean ret[] = new boolean[totalSize];
			ret[totalSize - 2] = true;
			return ret;
		}
	};

	static IModelGroup cdata = new IModelGroup() {
		public boolean match(ISGMLParser parser, Node parent, Node child) {
			if (child instanceof CDATASection) {
				parent.appendChild(child);
				return true;
			} else {
				return false;
			}
		}

		public boolean optional() {
			return false;
		}

		public void refer(boolean infinite) {
		}

		public String toString() {
			return "CDATA";
		}

		public boolean match(int number) {
			return false;
		}

		public boolean[] rehash(int totalSize) {
			return null;
		}
	};

	static IModelGroup empty = new IModelGroup() {
		public boolean match(ISGMLParser parser, Node parent, Node child) {
			return false;
		}

		public boolean optional() {
			return true;
		}

		public void refer(boolean infinite) {
		}

		public String toString() {
			return "EMPTY";
		}

		public boolean match(int number) {
			return false;
		}

		public boolean[] rehash(int totalSize) {
			return null;
		}
	};

	static IModelGroup any = new IModelGroup() {
		public boolean match(ISGMLParser parser, Node parent, Node child) {
			parent.appendChild(child);
			return true;
		}

		public boolean optional() {
			return true;
		}

		public void refer(boolean infinite) {
		}

		public boolean match(int number) {
			return true;
		}

		public boolean[] rehash(int totalSize) {
			return null;
		}
	};

	public ElementDefinition anonymousElementDef;

	/**
	 * Gets a element definition for undefined elements.
	 */
	public ElementDefinition getAnonymousElementDefinition() {
		return anonymousElementDef;
	}

	public SGMLDocTypeDef dtd;

	/**
	 * Gets DTD that defines this Document's syntax.
	 * 
	 * @return DTD that defines this Document's syntax.
	 */
	public SGMLDocTypeDef getDTD() {
		return this.dtd;
	}

	/**
	 * Sets DTD that defines this Document's syntax.
	 * 
	 * @param dtd DTD that defines this Document's syntax.
	 */
	public final void setDTD(SGMLDocTypeDef dtd) {
		this.dtd = dtd;
		if (doc instanceof SGMLDocument) {
			((SGMLDocument) doc).setDTD(dtd);
		}

		isXHTML = (dtd.toString().indexOf("XHTML") > -1);

	}

	public Hashtable<Node, AndContext> andMap = new Hashtable<Node, AndContext>();

	public Hashtable<Node, AndContext> getAndMap() {
		return andMap;
	}

	public Hashtable<Node, Integer> seqMap = new Hashtable<Node, Integer>();

	public Hashtable<Node, Integer> getSeqMap() {
		return seqMap;
	}

	public Hashtable<Node, Node> plusMap = new Hashtable<Node, Node>();

	public Hashtable<Node, Node> getPlusMap() {
		return plusMap;
	}

	public void clearContextMap(Node parent) {
		andMap.remove(parent);
		seqMap.remove(parent);
		plusMap.remove(parent);
	}

	private Attr attribute(ElementDefinition ed, AttributeListImpl attrlist)
			throws IOException, ParseException, SAXException {
		Attr ret = null;
		if (tokenizer.nextToken() != NAME_CHAR) {
			if (handleError(IParserError.TAG_NAME, tokenizer.sval)) {
				return attribute(ed, attrlist);
			}
		} else {
			String attName = changeAttrNameCase(tokenizer.sval);
			AttributeDefinition ad = ed != null ? ed.getAttributeDef(attName) : null;
			String attValue = attName;
			if (tokenizer.nextToken() == EQ) {
				attValue = tokenizer.readAttributeValue(ad, ed);
				ret = doc.createAttribute(attName);
				ret.setValue(attValue);
			} else {
				tokenizer.pushBack();
				ret = doc.createAttribute(attName);
				// HTML5 allows to omit attr val
				if (DocumentTypeUtil.isOriginalHTML5(doc.getDoctype())) {
					ret.setValue("");
				}
			}
			if (ad == null) {
				if (attrlist != null) {
					attrlist.addAttribute(attName, "CDATA", attValue);
				}
				if (ed != null && ed != anonymousElementDef) {
					if (handleError(IParserError.ILLEGAL_ATTRIBUTE, ret)) {
						ret = attribute(ed, attrlist);
					} else {
						error(IParserError.ILLEGAL_ATTRIBUTE,
								"Illegal attribute '" + attName + "' for " + ed.getName());
					}
				}
			} else if (attrlist != null) {
				attrlist.addAttribute(attName, ad.getDeclaredTypeStr(), attValue);
			}
		}
		return ret;
	}

	/**
	 * Records an error. Does nothing by default. If it has ErrorLogListener
	 * instances, calls their {@link IErrorLogListener#errorLog(int,String)} method.
	 * 
	 * @param code error code.
	 * @param msg  message of the error.
	 */
	public final void error(int code, String msg) {
		for (int i = 0; i < errorLogListenerNum; i++) {
			if (tokenizer != null) {
				errorLogListeners[i].errorLog(code, tokenizer.getCurrentLine() + ": " + msg);
			} else {
				errorLogListeners[i].errorLog(code, msg);
			}
		}
	}

	public IErrorLogListener errorLogListeners[] = new IErrorLogListener[8];

	public int errorLogListenerNum = 0;

	/**
	 * Adds an error log listerner. Listener is invoked when error is occured.
	 */
	public void addErrorLogListener(IErrorLogListener listener) {
		if (errorLogListenerNum == errorLogListeners.length) {
			IErrorLogListener newListeners[] = new IErrorLogListener[errorLogListenerNum * 2];
			for (int i = 0; i < errorLogListenerNum; i++) {
				newListeners[i] = errorLogListeners[i];
			}
			errorLogListeners = newListeners;
		}
		errorLogListeners[errorLogListenerNum++] = listener;
	}

	/**
	 * Removes an error log listener.
	 * 
	 * @param lister error log listener to remove.
	 */
	public void removeErrorLogListener(IErrorLogListener listener) {
		for (int i = 0; i < errorLogListenerNum; i++) {
			if (errorLogListeners[i] == listener) {
				errorLogListeners[i] = null;
				for (i++; i < errorLogListenerNum; i++) {
					errorLogListeners[i - 1] = errorLogListeners[i];
				}
				errorLogListenerNum--;
				return;
			}
		}
	}

	private EndTag etag() throws IOException, ParseException, SAXException {
		String tagName;
		if (tokenizer.nextToken() == NAME_CHAR) {
			tagName = changeTagCase(tokenizer.sval);
			while (tokenizer.nextToken() != TAGC && tokenizer.ttype != EOF) {
			}
			;

			EndTag et = new EndTag(tagName);
			currentNode = et;
			if (docHandler != null && !eHandleLogical) {
				docHandler.endElement(tagName);
			}
			ElementDefinition ed = dtd.getElementDefinition(tagName);

			if (ed != null) {
				lastElementNumber = ed.number;
				return et;
			} else if (keepUnknowns) {
				lastElementDef = anonymousElementDef;
				lastElementNumber = pcdataNumber + 1;
				return et;
			} else if (handleError(IParserError.UNKNOWN_ELEMENT, et)) {

				return null;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.dom.sgml.impl.ISGMLParser#getDocument()
	 */
	public Document getDocument() {
		return doc;
	}

	/**
	 * push back buffer size
	 */
	private static final int BUF_SIZ = 256;

	public int bufCount = 0;

	public Node buf[] = new Node[BUF_SIZ];

	/**
	 * Gets a Node or {@link EndTag}from a currently reading stream as a result of
	 * tokenizing.
	 * 
	 * @return {@link org.w3c.dom.Node Node}or {@link EndTag}
	 * @exception ParseException
	 * @exception IOException
	 * @see #pushBackNode(org.w3c.dom.Node)
	 */
	public Node getNode() throws ParseException, IOException, SAXException {
		if (bufCount == 0) {
			return node();
		} else {
			return buf[--bufCount];
		}
	}

	/**
	 * Pushes back a node to this parser.
	 * 
	 * @param node node to be pushed back.
	 * @see #getNode()
	 */
	public void pushBackNode(Node node) {
		buf[bufCount++] = node;
		if (node instanceof Element) {
			lastElementDef = dtd.getElementDefinition(node.getNodeName());
			lastElementNumber = lastElementDef.number;
		}
	}

	/**
	 * Gets a resource reader for this parser. By default, this class has no
	 * resource. So if some resource is required, override this method in a
	 * subclass.
	 * 
	 * @exception IOException always thrown.
	 */
	protected Reader getResource(String resourceName) throws IOException {
		throw new IOException("cannot find " + resourceName);
	}

	public Node node() throws ParseException, IOException, SAXException {
		Node ret;

		// TODO emulate <tag /> by using EndTag
		if (isEndWithSlash && !isEmptyElement) {
			// if(isXHTML && isEndWithSlash){
			isEndWithSlash = false;

			EndTag et = new EndTag(currentTagName);
			currentNode = et;
			if (docHandler != null && !eHandleLogical) {
				docHandler.endElement(currentTagName);
			}
			ElementDefinition ed = dtd.getElementDefinition(currentTagName);
			if (ed != null) {
				lastElementNumber = ed.number;
				return et;
			} else if (keepUnknowns) {
				lastElementDef = anonymousElementDef;
				lastElementNumber = pcdataNumber + 1;
				return et;
			} else if (handleError(IParserError.UNKNOWN_ELEMENT, et)) {

				return null;
			}
		}
		switch (tokenizer.nextToken()) {
		case STAGO:
			ret = stag();
			if (ret == null) { // Unknown Element. Skip it.
				ret = getNode();
			}
			break;
		case ETAGO:
			ret = etag();
			if (ret == null) { // Unknown Element. Skip it.
				ret = node();
			}
			break;
		case WHITESPACE:
			currentNode = ret = doc.createTextNode(tokenizer.sval);
			if (ret instanceof SGMLText) {
				((SGMLText) ret).setIsWhitespaceInElementContent(true);
			}
			if (docHandler != null) {
				if (saxch != null) {
					docHandler.characters(saxch, begin, len);
					saxch = null;
				}
			}
			break;
		case PCDATA:
			currentNode = ret = doc.createTextNode(tokenizer.sval);
			if (docHandler != null) {
				if (saxch != null) {
					docHandler.characters(saxch, begin, len);
					saxch = null;
				}
			}
			break;
		case COMMENT:
			currentNode = ret = doc.createComment(tokenizer.sval);
			if (lexHandler != null) {
				lexHandler.comment(saxch, begin, len);
				saxch = null;
			}
			break;
		case PI:
			currentNode = ret = doc.createProcessingInstruction(null, tokenizer.sval);
			if (docHandler != null) {
				docHandler.processingInstruction(null, tokenizer.sval);
			}
			break;
		case EOF:
			ret = null;
			break;
		case MDO:
			error(IParserError.ILLEGAL_DOCTYPE, "Illegal Declaration. Discarding to next '>'");
			if (tokenizer.nextToken() != '>') {
				// consume '>'
				tokenizer.consumeUntil('>');
				tokenizer.switchTo(DEFAULT);
			}
			return node();
		default:
			error(IParserError.MISC_ERR, "Internal Parser Error: character encoding may be wrong.");
			return node();
		}
		return ret;
	}

	/**
	 * Set up syntax information described by DTD.
	 * 
	 * @param publicID DTD's public id that specifies which to set up.
	 */
	public void setupDTD(String publicID) throws ParseException, IOException {
		publicID = makeUnique(publicID);
		SGMLDocTypeDef ret = SGMLDocTypeDef.getPublic(publicID);
		if (ret != null) {
			setDTD(ret);
			lastDef = ret.getElementDefinition(getDefaultTopElement());
		} else {
			synchronized (publicID) {
				ret = SGMLDocTypeDef.getPublic(publicID);
				if (ret == null) {
					ret = SGMLDocTypeDef.createPublic(publicID, this);
					setDTD(ret);
					Reader dr = getResource(pubEntityMap.get(publicID));
					DTDTokenizer tok = new DTDTokenizer(dr);
					new DTDParser(tok, ret).readDTD();
					dr.close();
					SGMLDocTypeDef.putPublic(publicID, ret);
					ret.rehash();
				} else {
					setDTD(ret);
				}
			}
		}
		lastDef = dtd.getElementDefinition(getDefaultTopElement());
		pcdataNumber = dtd.getElementCount();
		for (int i = depth - 1; i >= 0; i--) {
			ancesterElementDefs[i] = dtd.getElementDefinition(ancesterElementDefs[i].getName());
			if (ancesterElementDefs[i] == null) {
				error(IParserError.UNKNOWN_ELEMENT, ancesters[i].getNodeName() + " is not defined in " + publicID);
				ancesterElementDefs[i] = anonymousElementDef;
			}
		}
		anonymousElementDef.rehash(pcdataNumber + 2);
		anonymousElementDef.number = pcdataNumber + 1;
	}

	public int pcdataNumber;

	/**
	 * Parses SGML a document and return its top element. SGML documents are
	 * consists of three parts. 1. SGML declaration. 2. Dcument type definition. 3.
	 * SGML instances. If a document misses 1. and 2., this parser try to read the
	 * default declaration specified by {@link #setDefaultDTD(java.lang.String)}.
	 * 
	 * @param reader parse to read.
	 * @return document.
	 * @exception PaserException If unrecoverable syntax or token error occured,
	 *                           throwed
	 * @exception IOException
	 */
	public Node parse(Reader reader) throws ParseException, IOException, SAXException {
		if (domImpl == null && doc == null) {
			throw new ParseException("No factory instance.");
		}
		init();

		this.tokenizer = new InsTokenizer(reader, this);
		if (docHandler != null) {
			docHandler.setDocumentLocator(this.tokenizer);
			docHandler.startDocument();
		}
		setDTD(SGMLDocTypeDef.createAnonymous(this));
		DocumentType docType = readDocType();
		if (docType == null) {
			error(IParserError.DOCTYPE_MISSED,
					"<!DOCTYPE ...> is missing.  Try to use \"" + defaultDTD + "\" as document type");
			setupDTD(defaultDTD);
		}
		tokenizer.extractNumEntity(extractNum);
		tokenizer.extractCharEntity(extractChar);
		tokenizer.setPreserveWhitespace(preserveWhitespace);
		if (doc == null) {
			doc = createDocument(docType);
			if (doc instanceof SGMLDocument && ((SGMLDocument) doc).getDTD() == null) {
				((SGMLDocument) doc).setDTD(this.dtd);
			}
			while (!commentsBeforeDoctype.isEmpty()) {
				CATB catb = commentsBeforeDoctype.lastElement();
				Node node = catb.comment ? (Node) doc.createComment(catb.str)
						: doc.createProcessingInstruction(null, catb.str);
				doc.insertBefore(node, doc.getFirstChild());
				commentsBeforeDoctype.removeElement(catb);
			}
		}

		// //041026 handle comments after doctype
		// comment_loop: while (true) {
		// switch (tokenizer.nextToken()) {
		// case COMMENT:
		// doc.appendChild(doc.createComment(tokenizer.sval));
		// if (lexHandler != null) {
		// lexHandler.comment(saxch, begin, len);
		// }
		// break;
		// case PI:
		// doc.appendChild(doc.createComment(tokenizer.sval));
		// if (docHandler != null) {
		// docHandler.processingInstruction(null, tokenizer.sval);
		// }
		// break;
		// case MDO:
		// break comment_loop;
		// default:
		// tokenizer.pushBack();
		// break comment_loop;
		// }
		// }

		// dummy
		context = doc.createElement("dummy0");
		seqArray = new IModelGroup[dtd.maxSeqLength];
		Node ret = readInstances();
		if (docHandler != null)
			docHandler.endDocument();
		return ret;
	}

	public IModelGroup seqArray[];

	public IModelGroup[] getSeqArray() {
		return this.seqArray;
	}

	protected Document createDocument(DocumentType docType) {
		Document ret = domImpl.createDocument("dummy1", "dummy1", docType);
		if (ret.getDocumentElement() != null) {
			ret.removeChild(ret.getDocumentElement());
		}
		return ret;
	}

	/**
	 * Context element.
	 */
	public Element context = null;

	/**
	 * Gets a current context element.
	 * 
	 * @return context element
	 * @see #setContext(org.w3c.dom.Element)
	 */
	public Element getContext() {
		return this.context;
	}

	public Element forwardPath[] = new Element[BUF_SIZ];

	public ElementDefinition ancesterElementDefs[] = new ElementDefinition[BUF_SIZ];

	public Element ancesters[] = new Element[BUF_SIZ];

	public int depth = 0;
	public int bufSize = BUF_SIZ;

	public AttributeListImpl nullAttributeList = createAttributeList();

	private void setContextForward(Element element) throws SAXException {
		ElementDefinition ed;
		if (depth >= bufSize) {
			Element tmpAncesters[] = new Element[bufSize * 2];
			System.arraycopy(ancesters, 0, tmpAncesters, 0, bufSize);
			ancesters = tmpAncesters;
			ElementDefinition tmpAncesterElementDefs[] = new ElementDefinition[bufSize * 2];
			System.arraycopy(ancesterElementDefs, 0, tmpAncesterElementDefs, 0, bufSize);
			ancesterElementDefs = tmpAncesterElementDefs;
			bufSize = bufSize * 2;
		}
		if (eHandleLogical && docHandler != null) {
			for (Node down = context.getLastChild(); down instanceof Element; down = down.getLastChild()) {
				if (down == element) {
					ancesters[depth] = element;
					ancesterElementDefs[depth] = lastElementDef;
					depth++;
					docHandler.startElement(element.getNodeName(), attrlist);
					attrlist = null;
					this.context = element;
					return;
				} else {
					ancesters[depth] = (Element) down;
					ancesterElementDefs[depth] = ed = dtd.getElementDefinition(down.getNodeName());
					if (ed == null) {
						ancesterElementDefs[depth] = anonymousElementDef;
					}
					docHandler.startElement(down.getNodeName(), nullAttributeList);
					depth++;
				}
			}
		} else {
			for (Node down = context.getLastChild(); down instanceof Element; down = down.getLastChild()) {
				if (down == element) {
					ancesters[depth] = element;
					ancesterElementDefs[depth] = lastElementDef;
					depth++;
					attrlist = null;
					this.context = element;
					return;
				} else {
					ancesters[depth] = (Element) down;
					ancesterElementDefs[depth] = ed = dtd.getElementDefinition(down.getNodeName());
					if (ed == null) {
						ancesterElementDefs[depth] = anonymousElementDef;
					}
				}
				depth++;
			}
		}
		throw new RuntimeException("Internal Parser Error.");
	}

	private void setContextBackward(int newDepth) throws SAXException {
		if (eHandleLogical && docHandler != null) {
			for (int i = depth - 1; i >= newDepth; i--) {
				docHandler.endElement(ancesters[i].getNodeName());
			}
		}
		depth = newDepth;
		this.context = ancesters[newDepth - 1];
		return;
	}

	/**
	 * Sets current context element node.
	 * 
	 * @param element new context.
	 * @see #getContext()
	 */
	public final void setContext(Element element) throws SAXException {
		for (int i = depth - 1; i >= 0; i--) {
			Element up = ancesters[i];
			if (up == element) {
				if (eHandleLogical && docHandler != null) {
					for (int j = depth - 1; j > i; j--) {
						docHandler.endElement(ancesters[j].getNodeName());
					}
				}
				depth = i + 1;
				this.context = element;
				return;
			}
			int forwardPathLen = 0;
			for (Node down = up.getLastChild(); down instanceof Element; down = down.getLastChild()) {
				if (down == element) {
					if (eHandleLogical && docHandler != null) {
						for (int j = depth - 1; j > i; j--) {
							docHandler.endElement(ancesters[j].getNodeName());
						}
						for (int j = 0; j < forwardPathLen; j++) {
							docHandler.startElement(forwardPath[j].getNodeName(), nullAttributeList);
							ancesters[i + j + 1] = forwardPath[j];
							ancesterElementDefs[i + j + 1] = dtd.getElementDefinition(forwardPath[j].getNodeName());
							if (ancesterElementDefs[i + j + 1] == null) {
								ancesterElementDefs[i + j + 1] = anonymousElementDef;
							}
						}
						docHandler.startElement(down.getNodeName(), attrlist);
					} else {
						for (int j = 0; j < forwardPathLen; j++) {
							ancesters[i + j + 1] = forwardPath[j];
							ancesterElementDefs[i + j + 1] = dtd.getElementDefinition(forwardPath[j].getNodeName());
							if (ancesterElementDefs[i + j + 1] == null) {
								ancesterElementDefs[i + j + 1] = anonymousElementDef;
							}
						}
					}
					depth = i + forwardPathLen + 1;
					ancesters[depth] = element;
					ancesterElementDefs[depth] = lastElementDef;
					depth++;
					attrlist = null;
					this.context = element;
					return;
				} else {
					forwardPath[forwardPathLen++] = (Element) down;
				}
			}
		}
		// creates backward context.
		int newDepth = 0;
		for (Node up = element; up instanceof Element; up = up.getParentNode()) {
			newDepth++;
		}
		Element newAncesters[] = new Element[newDepth];
		int i = 1;
		for (Node up = element; up instanceof Element; up = up.getParentNode()) {
			newAncesters[newDepth - i] = (Element) up;
			i++;
		}
		for (i = 0; i < newDepth; i++) {
			if (ancesters[i] != newAncesters[i]) {
				if (eHandleLogical && docHandler != null) {
					for (int j = depth - 1; j >= i; j--) {
						docHandler.endElement(ancesters[j].getNodeName());
					}
					for (int j = i; j < newDepth - 1; j++) {
						docHandler.startElement(newAncesters[j].getNodeName(), nullAttributeList);
					}
					docHandler.startElement(element.getNodeName(), attrlist);
				}
				while (i < newDepth - 1) {
					ancesters[i] = newAncesters[i];
					ancesterElementDefs[i] = dtd.getElementDefinition(newAncesters[i].getNodeName());
				}
				ancesters[newDepth - 1] = element;
				ancesterElementDefs[newDepth - 1] = lastElementDef;
				depth = newDepth;
				attrlist = null;
				this.context = element;
				return;
			}
		}
	}

	public Vector<Element> nodesWithEndtag = new Vector<Element>();

	/**
	 * Checks if a specified element has its end tag or not.
	 * 
	 * @param element element to be checked.
	 * @return <code>true</code> if <code>element</code> has its end tag. Otherwise,
	 *         false.
	 */
	public boolean hasEndTag(Element element) {
		return nodesWithEndtag.contains(element);
	}

	/**
	 * Determines that a specified element has its end tag.
	 */
	public void setHasEndTag(Element element) {
		nodesWithEndtag.addElement(element);
	}

	private void setTopElement(Element element) throws SAXException {
		Element prev = doc.getDocumentElement();
		if (prev != null) {
			doc.replaceChild(element, prev);
		} else {
			doc.appendChild(element);
		}
		context = element;
		if (docHandler != null && eHandleLogical) {
			AttributeListImpl al = attrlist != null ? attrlist : nullAttributeList;
			docHandler.startElement(element.getNodeName(), al);
			attrlist = null;
		}
		ancesterElementDefs[0] = lastDef;
		ancesters[0] = element;
		depth++;
	}

	private Vector<EndTag> missedEndtags = new Vector<EndTag>();

	private Node readInstances() throws ParseException, IOException, SAXException {
		Node node = getNode();
		if (node == null)
			return doc;
		while (node.getNodeType() == Node.COMMENT_NODE || node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
			if (keepComment)
				doc.appendChild(node);
			node = getNode();
		}

		switch (node.getNodeType()) {
		case ENDTAG:

			if (!handleError(IParserError.FLOATING_ENDTAG, node)) {
				if (eHandleLogical && docHandler != null) {
					docHandler.ignorableWhitespace(saxch, begin, len);
				}
				error(IParserError.FLOATING_ENDTAG, "Illegal end tag: " + node + ".  Ignore it.");
			}
			return readInstances();
		case Node.ELEMENT_NODE:
			if (lastDef.instance(node)) {
				setTopElement((Element) node);
			} else {
				AttributeListImpl attrlisttmp = attrlist;
				attrlist = null;
				setTopElement(doc.createElement(changeDefaultTagCase(lastDef.getName())));
				attrlist = attrlisttmp;
				addAutoGenerated(context);
				if (lastDef.getContentModel().match(this, context, node)) {
					if (!lastDef.startTagOmittable()) {
						error(IParserError.ILLEGAL_TOP_ELEMENT, node + " can't be a top element.");
					}
					setContextForward((Element) node);
				} else if (!handleError(IParserError.ILLEGAL_CHILD, node)) {
					addErrorNode(context);
					error(IParserError.ILLEGAL_CHILD, node + " is not allowed as a child of " + context);
					context.appendChild(node);
					setContextForward((Element) node);
				}
			}
			postElement((Element) node);
			break;
		case Node.TEXT_NODE:
			if (preserveWhitespace && whitespaceText((Text) node)) {
				return readInstances();
			}
			error(IParserError.ILLEGAL_TOP_ELEMENT, "#text can't be a top element");
			setTopElement(doc.createElement(changeDefaultTagCase(lastDef.getName())));
			addAutoGenerated(context);
			context.appendChild(node);
			break;
		default:
			throw new ParseException(tokenizer.getCurrentLine() + ": Internal Parser Error " + node);
		}
		return readInstances2();
	}

	@SuppressWarnings("unused")
	private Node readInstances2() throws ParseException, IOException, SAXException {
		ElementDefinition ed;
		Node node;
		outer: for (node = getNode(); node != null; node = getNode()) {
			// System.out.println(node.getNodeType()+" : "+node);

			// System.out.println(node.toString());

			node_sel: switch (node.getNodeType()) {
			case Node.COMMENT_NODE:
			case Node.PROCESSING_INSTRUCTION_NODE:
				if (keepComment)
					context.appendChild(node);
				break;
			case ENDTAG:

				missedEndtags.removeAllElements();
				if (_DEBUG && node.getNodeName().equalsIgnoreCase(System.getProperty("DEBUG_ENDTAG"))) {
					System.err.println("DEBUG: " + node);
				}
				for (int i = depth - 1; i >= 0; i--) {
					if (ancesterElementDefs[i].number == lastElementNumber && (lastElementNumber != pcdataNumber + 1
							|| ancesters[i].getNodeName().equalsIgnoreCase(node.getNodeName()))) {

						if (!missedEndtags.isEmpty()) {

							extraErrInfo = missedEndtags;
							if (handleError(IParserError.SUDDEN_ENDTAG, node)) {
								extraErrInfo = null;
								break node_sel;
							}
							extraErrInfo = null;
							error(IParserError.SUDDEN_ENDTAG,
									missedEndtags + " have been forced to be inserted by " + node);
						}
						/*
						 * if (ancesterElementDefs[depth - 1].number == lastElementNumber) {
						 * nodesWithEndtag.addElement(context); }
						 */
						((EndTag) node).setElement(ancesters[i]);
						nodesWithEndtag.addElement(ancesters[i]);
						if (i > 0) {
							setContextBackward(i);

						} else {
							break outer;
						}
						break node_sel;
					} else {
						if (!ancesterElementDefs[i].endTagOmittable()) {
							missedEndtags.insertElementAt(new EndTag(ancesters[i].getNodeName()), 0);
						}
					}

				}
				if (!handleError(IParserError.FLOATING_ENDTAG, node)) {
					if (eHandleLogical && docHandler != null) {
						docHandler.ignorableWhitespace(saxch, begin, len);
					}
					error(IParserError.FLOATING_ENDTAG, "Illegal end tag: " + node + ".  Ignore it");
				}
				break;
			case Node.ELEMENT_NODE:
				Element element = (Element) node;
				Element exParent = null;
				if (_DEBUG && element.getTagName().equalsIgnoreCase(System.getProperty("DEBUG_STARTTAG"))) {
					System.err.println("DEBUG: " + element);
				}

				// System.out.println("element");

				for (int i = depth - 1; i >= 0; i--) {
					ed = ancesterElementDefs[i];
					if (!ed.endTagOmittable() && exParent == null) {
						exParent = ancesters[i];
					}

					// System.out.println(ed.toString());

					if (ed.exclusion(lastElementNumber)) {
						if (exParent != null) {
							if (!handleError(IParserError.ILLEGAL_CHILD, node)) {
								addErrorNode(context);
								error(IParserError.ILLEGAL_CHILD, node + " is an exception uner " + ancesters[i]);
							} else {
								if (context != node && eHandleLogical && docHandler != null) {
									docHandler.startElement(node.getNodeName(), attrlist);
								}
								postElement(element);
								break node_sel;
							}
						} else if (ancesters[i - 1] != null) {
							setContextBackward(i);
						}
						break;
					}

					// System.out.println("elenent: mid");

					if (ed.inclusion(lastElementNumber)) {
						context.appendChild(node);
						setContextForward(element);
						postElement(element);
						break node_sel;
					}
				}
				ed = ancesterElementDefs[depth - 1];
				IModelGroup contentModel = ed.getContentModel();

				// TODO correct this
				if (contentModel.match(lastElementNumber) && contentModel.match(this, context, node)) {

					// System.out.println("model: fow");

					setContextForward(element);
				} else if (ed.endTagOmittable()) {

					// System.out.println("model: omit");

					boolean found = false;
					for (int i = depth - 2; i >= 0; i--) {
						ed = ancesterElementDefs[i];
						contentModel = ed.getContentModel();
						if (contentModel.match(lastElementNumber)
								&& (found = contentModel.match(this, ancesters[i], node)) || !ed.endTagOmittable()) {
							break;
						}
					}

					// System.out.println("model: omit2 "+found);

					if (found) {
						// System.out.println("model: ok");
						setContext(element);
					} else if (!handleError(IParserError.ILLEGAL_CHILD, node)) {
						// System.out.println("model: child");

						context.appendChild(element);
						addErrorNode(context);
						error(IParserError.ILLEGAL_CHILD, node + " is not allowed as a child of " + context);

						setContextForward(element);
					} else if (element.getParentNode() != null) {
						// unless the error handlers ignore the node

						// System.out.println("model: post");

						postElement(element);

						break;
					}

					// System.out.println("model: omit3");

				} else if (!handleError(IParserError.ILLEGAL_CHILD, node)) {
					// System.out.println("model: error");
					// TODO ???
					context.appendChild(element);
					addErrorNode(context);
					error(IParserError.ILLEGAL_CHILD, node + " is not allowed as a child of " + context);
					// System.out.println(node.getNodeName()+context);
					setContextForward(element);
				}

				// System.out.println("model: end");

				if (context != node && eHandleLogical && docHandler != null) {
					docHandler.startElement(node.getNodeName(), attrlist);
				}
				postElement(element);
				break;
			case Node.TEXT_NODE:
				if (preserveWhitespace && whitespaceText((Text) node)) {
					getContext().appendChild(node);
					break;
				}
				ed = ancesterElementDefs[depth - 1];
				contentModel = ed.getContentModel();
				if (contentModel.match(pcdataNumber) && contentModel.match(this, context, node)) {
					break;
				} else if (ed.endTagOmittable()) {
					for (int i = depth - 2; i >= 0; i--) {
						ed = ancesterElementDefs[i];
						contentModel = ed.getContentModel();
						if (contentModel.match(pcdataNumber) && contentModel.match(this, ancesters[i], node)) {
							break node_sel;
						} else if (!ed.endTagOmittable()) {
							break;
						}
					}
				}
				if (handleError(IParserError.ILLEGAL_CHILD, node))
					break node_sel;
				addErrorNode(context);
				error(IParserError.ILLEGAL_CHILD, "#text(" + node + ") is not allowed as a child of " + context);
				context.appendChild(node);
				break;
			default:
				throw new ParseException(tokenizer.getCurrentLine() + ": Internal parser error " + node);
			}
		}

		// System.out.println("SGMLParser: a");

		if (docHandler != null && eHandleLogical) {
			Element top = doc.getDocumentElement();
			if (getContext() != top) {
				setContext(top);
			}
			if (top != null)
				docHandler.endElement(top.getNodeName());
		}
		if ((node = getNode()) != null) {
			this.context = doc.getDocumentElement();
			if (docHandler != null && eHandleLogical) {
				docHandler.startElement(this.context.getNodeName(), nullAttributeList);
			}
			// System.out.println("SGMLParser: pushback");

			pushBackNode(node);
			readInstances2();
		}
		// System.out.println("SGMLParser: end");
		return doc;
	}

	private boolean whitespaceText(Text text) {
		if (text instanceof SGMLText) {
			return ((SGMLText) text).getIsWhitespaceInElementContent();
		}
		char str[] = text.getData().toCharArray();
		for (int i = str.length - 1; i >= 0; i--) {
			if (!Character.isWhitespace(str[i]))
				return false;
		}
		return true;
	}

	private void postElement(Element element) throws ParseException, IOException, SAXException {
		IModelGroup mg = lastElementDef.getContentModel();

		if (mg == cdata) {
			String tagName = element.getNodeName();
			Node cdata = readCDATA(tagName);
			currentNode = new EndTag(tagName);
			((EndTag) currentNode).setElement(element);
			nodesWithEndtag.addElement(element);
			if (docHandler != null && !eHandleLogical) {
				docHandler.endElement(tagName);
			}
			element.appendChild(cdata);
			if (context == element)
				setContextBackward(depth - 1);

		} else if (mg == empty && lastElementDef.endTagOmittable()) { // must
			// be omitted

			//System.out.println(element.toString() + lastElementDef.toString() + " : " + context);
			if (context == element) {
				setContextBackward(depth - 1);
			} else if (eHandleLogical && docHandler != null) {
				docHandler.endElement(element.getNodeName());
			}
		} else if (isXHTML && isEndWithSlash) {
			// System.out.println(element.toString()+lastElementDef.toString());
		} else if (DocumentTypeUtil.isOriginalHTML5(doc.getDoctype())) {
			// support void element of HTML Living Standard
			String tagName = element.getNodeName();
			if (VOID_ELEMENT_PATTERN.matcher(tagName).matches()) {
				if (context == element) {
					setContextBackward(depth - 1);
				} else if (eHandleLogical && docHandler != null) {
					docHandler.endElement(element.getNodeName());
				}
			}

		}
	}

	private CDATASection readCDATA(String arg) throws ParseException, IOException, SAXException {
		if (lexHandler != null) {
			lexHandler.startCDATA();
		}
		String str = tokenizer.rawText(arg);
		CDATASection ret = doc.createCDATASection(str);
		currentNode = ret;
		if (lexHandler != null) {
			lexHandler.endCDATA();
		}
		tokenizer.eatCDATAEndTag();
		return ret;
	}

	private Vector<CATB> commentsBeforeDoctype = new Vector<CATB>();

	// private Vector commentsAftereDoctype = new Vector();

	/**
	 * comment at the beginning
	 */
	class CATB {
		/**
		 * If comment, true. Otherwise, false.
		 */
		boolean comment = true;

		String str;
	}

	private DocumentType readDocType() throws ParseException, IOException, SAXException {
		if (lastDef != null) {
			throw new ParseException("Already read DOCTYPE declaration");
		}

		comment_loop: while (true) {
			switch (tokenizer.nextToken()) {
			case COMMENT:
				if (doc == null) {
					CATB catb = new CATB();
					catb.str = tokenizer.sval;
					commentsBeforeDoctype.addElement(catb);
				} else {
					currentNode = doc.createComment(tokenizer.sval);
					doc.appendChild(currentNode);
				}
				if (lexHandler != null) {
					lexHandler.comment(saxch, begin, len);
				}
				break;
			case PI:
				if (doc == null) {
					CATB catb = new CATB();
					catb.comment = false;
					catb.str = tokenizer.sval;
					commentsBeforeDoctype.addElement(catb);
				} else {
					currentNode = doc.createProcessingInstruction(null, tokenizer.sval);
					doc.appendChild(currentNode);
				}
				if (docHandler != null) {
					docHandler.processingInstruction(null, tokenizer.sval);
				}
				break;
			case MDO:
				break comment_loop;
			default:
				tokenizer.pushBack();
				return null;
			}
		}

		if (tokenizer.nextToken() != NAME_CHAR && !tokenizer.sval.equals("DOCTYPE")) {
			throw new ParseException("Unknown declaration at " + tokenizer.getCurrentLine());
		}

		String docTypeName = "";
		/*
		 * Only supports initially setted public entity. For example, <!DOCTYPE HTML
		 * PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
		 * 
		 * 2016: added support for html5 doctype
		 */
		if (tokenizer.nextToken() == NAME_CHAR) {
			docTypeName = tokenizer.sval;
			this.defaultTopElement = docTypeName;
			if (tokenizer.nextToken() == NAME_CHAR) {
				String type = tokenizer.sval;
				if (type.equalsIgnoreCase("PUBLIC") || type.equalsIgnoreCase("SYSTEM")) {
					// to cover html5
					// (<!DOCTYPE html SYSTEM "about:legacy-compat">)

					if (tokenizer.nextToken() == '"') {
						String id = tokenizer.eatUntil('"');
						String orgId = id;
						if (enforcedDoctype != null) {
							id = enforcedDoctype;
						}
						if (lexHandler != null) {
							lexHandler.startDTD(docTypeName, id, null);
						}

						// TODO consider to use pseudo DTD for html5
						String entityFileName = pubEntityMap.get(id);
						if (entityFileName == null) {
							if (defaultDTD != null) {
								if (type.equalsIgnoreCase("PUBLIC")) {
									error(IParserError.ILLEGAL_DOCTYPE,
											"Instead of \"" + id + "\" use \"" + defaultDTD + "\" as a DTD.");
								} else {
									// for SYSTEM

									if (orgId.equalsIgnoreCase("about:legacy-compat")) {
										error(IParserError.ILLEGAL_DOCTYPE, "Instead of SYSTEM \"" + id
												+ "\" use PUBLIC \"" + defaultDTD + "\" as a DTD.");
									} else {
										error(IParserError.ILLEGAL_DOCTYPE,
												"Invalid DOCTYPE declaration. Use " + defaultDTD);
									}
								}

								entityFileName = pubEntityMap.get(defaultDTD);
							}
							if (entityFileName == null) {
								throw new ParseException(
										tokenizer.getCurrentLine() + ": this parser does not support " + id);
							}
							id = defaultDTD;
						}
						setupDTD(id);
						if (domImpl == null) {
							domImpl = doc.getImplementation();
						}
						DocumentType ret = null;
						if (domImpl != null) {
							currentNode = ret = createDocType(domImpl, docTypeName, id);
							if (!id.equals(orgId) && ret instanceof SGMLDocType) {
								((SGMLDocType) ret).setOrgId(orgId);
							}
						}
						// consume '>'
						tokenizer.consumeUntil('>');
						tokenizer.switchTo(DEFAULT);
						lastDef = dtd.getElementDefinition(docTypeName);
						if (lastDef == null) {
							String topElementName = getDefaultTopElement();
							error(IParserError.ILLEGAL_DOCTYPE,
									docTypeName + " is not defined as a root element.  Use " + topElementName + '.');
							lastDef = dtd.getElementDefinition(topElementName);
						}
						if (lexHandler != null) {
							lexHandler.endDTD();
						}
						return ret;
					}
				}

			}
		}

		// for html5
		if (docTypeName.equalsIgnoreCase("html")) {

			// TODO consider to use pseudo DTD for html5
			String systemID = "about:legacy-compat";// temp for html5
			if (enforcedDoctype != null) {
				systemID = enforcedDoctype;
			}
			// if (lexHandler != null) {
			// lexHandler.startDTD(docTypeName, systemID, null);
			// }
			String entityFileName = pubEntityMap.get(systemID);
			if (entityFileName == null) {
				if (defaultDTD != null) {
					error(IParserError.ILLEGAL_DOCTYPE,
							"For html5 document use PUBLIC \"" + defaultDTD + "\" as a DTD.");
					entityFileName = pubEntityMap.get(defaultDTD);
				}
				if (entityFileName == null) {
					throw new ParseException(tokenizer.getCurrentLine() + ": this parser does not support " + systemID);
				}
				systemID = defaultDTD;
			}
			setupDTD(systemID);
			if (domImpl == null) {
				domImpl = doc.getImplementation();
			}
			DocumentType ret = null;
			if (domImpl != null) {
				currentNode = ret = createDocType(domImpl, docTypeName, systemID);
				((SGMLDocType) ret).setOrgId(""); // use empty string
			}

			// if current sval is not '>' then consume '>'
			if (!tokenizer.sval.equals(">")) {
				tokenizer.consumeUntil('>');
				error(IParserError.ILLEGAL_DOCTYPE, "Invalid DOCTYPE declaration. Use " + defaultDTD);
			}
			tokenizer.switchTo(DEFAULT);
			lastDef = dtd.getElementDefinition(docTypeName);
			if (lastDef == null) {
				String topElementName = getDefaultTopElement();
				error(IParserError.ILLEGAL_DOCTYPE,
						docTypeName + " is not defined as a root element.  Use " + topElementName + '.');
				lastDef = dtd.getElementDefinition(topElementName);
			}
			// if (lexHandler != null) {
			// lexHandler.endDTD();
			// }
			return ret;
		}

		tokenizer.consumeUntil('>');

		// others
		error(IParserError.ILLEGAL_DOCTYPE, "Invalid DOCTYPE declaration. Use " + defaultDTD);
		setupDTD(defaultDTD);
		lastDef = dtd.getElementDefinition(getDefaultTopElement());
		return null;
	}

	private DocumentType createDocType(DOMImplementation domImpl, String docTypeName, String publicID) {
		/*
		 * For compatibility to DOM level 1
		 */
		Class<? extends DOMImplementation> domImplClass = domImpl.getClass();
		Class<? extends String> stringClass = docTypeName.getClass();
		Class<?> parameterTypes[] = { stringClass, stringClass, stringClass };
		java.lang.reflect.Method method;
		try {
			method = domImplClass.getMethod("createDocumentType", parameterTypes);
		} catch (NoSuchMethodException e) {
			return null;
		}
		String args[] = { docTypeName, publicID, "" };
		try {
			return (DocumentType) method.invoke(domImpl, (Object[]) args);
		} catch (IllegalAccessException e) {
			return null;
		} catch (java.lang.reflect.InvocationTargetException e) {
			return null;
		} catch (DOMException e) {
			return null;
		}
		/*
		 * For DOM level 2 try { return domImpl.createDocumentType(docTypeName,
		 * publicID, ""); } catch (DOMException e) { return null; }
		 */
	}

	private Node nodesWithIllegalChildren[] = new Node[BUF_SIZ];

	private int nodeWithIllegalChildNum = 0;

	private void expandNodesWithIllegalChildren() {
		Node newNodes[] = new Node[nodeWithIllegalChildNum * 2];
		System.arraycopy(nodesWithIllegalChildren, 0, newNodes, 0, nodeWithIllegalChildNum);
		nodesWithIllegalChildren = newNodes;
	}

	private void addErrorNode(Node node) {
		for (int i = nodeWithIllegalChildNum - 1; i >= 0; i--) {
			if (nodesWithIllegalChildren[i] == node)
				return;
		}
		if (nodesWithIllegalChildren.length == nodeWithIllegalChildNum) {
			expandNodesWithIllegalChildren();
		}
		nodesWithIllegalChildren[nodeWithIllegalChildNum++] = node;
	}

	/**
	 * Checks if a specified node has an error or not.
	 * 
	 * @param node node to be ckecked.
	 * @return <code>true</code> if <code>node</code> is an error node. Otherwise
	 *         <code>false</code>
	 */
	public boolean isErrorNode(Node node) {
		for (int i = nodeWithIllegalChildNum - 1; i >= 0; i--) {
			if (nodesWithIllegalChildren[i] == node)
				return true;
		}
		return false;
	}

	/**
	 * Initialized this parser.
	 */
	protected void init() {
		lastDef = null;
		context = null;
		nodeWithIllegalChildNum = 0;
		depth = 0;
		andMap.clear();
		seqMap.clear();
		plusMap.clear();
		commentsBeforeDoctype.removeAllElements();
		// commentsAftereDoctype.removeAllElements();
		if (getDOMImplementation() != null) {
			setDOMImplementation(getDOMImplementation());
		} else {
			for (Node child = doc.getFirstChild(); child != null; child = doc.getFirstChild()) {
				doc.removeChild(child);
			}
		}
	}

	int getCharEntity(String entity) throws IOException, ParseException, SAXException {
		SGMLEntityReference er = null;
		try {
			er = dtd.getEntityReference(entity);
		} catch (ParseException e) {
			return -1;
		}
		SGMLEntityDeclaration ed = er.getEntityDeclaration();
		int ch = ed.getReplacementChar();
		if (ch != -1) {
			return ch;
		}
		InsTokenizer tokenizer2 = new InsTokenizer(ed.getReplacementReader(), this);
		if (tokenizer2.nextToken() == PCDATA && tokenizer2.sval.length() == 1) {
			char ret = tokenizer2.sval.charAt(0);
			ed.setReplacementChar(ret);
			return ret;
		} else {
			throw new ParseException("Internal Parser Error: " + entity + " not defined.");
		}
	}

	/**
	 * Sets a <i>Document </i> instance that will be a factory of nodes in DOM tree.
	 * This parser can use any implementation of W3C's DOM. By default, Parsers use
	 * {@link SGMLDocument}. This method make the parser instance ignore
	 * DOMImplementation by side effects.
	 * 
	 * @param doc new Document instance.
	 * @see #getDocument()
	 * @deprecated See {@link #setDOMImplementation(DOMImplementation)}
	 */
	public void setDocument(Document doc) {
		this.doc = doc;
		domImpl = null;
	}

	public AttributeListImpl attrlist = null;

	public int lastElementNumber;

	public ElementDefinition lastElementDef;

	private Node stag() throws IOException, ParseException, SAXException {
		Element ret;
		isEmptyElement = true;
		isEndWithSlash = false;
		if (tokenizer.nextToken() != NAME_CHAR) {
			if (handleError(IParserError.TAG_NAME, tokenizer.sval)) {
				return stag();
			}
			error(IParserError.STARTTAG_SYNTAX_ERR, "Perhaps character encoding may not be correct.");
			while (tokenizer.nextToken() != NAME_CHAR) {
				if (tokenizer.ttype == EOF || tokenizer.ttype == TAGC) {
					return null;
				}
			}
		}

		ElementDefinition ed = this.dtd.getElementDefinition(tokenizer.sval);
		if (ed != null) {
			lastElementNumber = ed.number;
			lastElementDef = ed;
			isEmptyElement = ed.getContentModel().toString().equalsIgnoreCase("EMPTY");
		} else if (keepUnknowns) {
			lastElementNumber = pcdataNumber + 1;
			ed = lastElementDef = anonymousElementDef;
		}

		currentNode = ret = doc.createElement(changeTagCase(tokenizer.sval));
		/*
		 * boolean syntaxError = false;
		 */
		Attr attr = null;
		if (docHandler != null)
			attrlist = createAttributeList();
		isEndWithSlash = false;
		while (tokenizer.nextToken() != TAGC) {
			if (tokenizer.ttype == '/') {
				isEndWithSlash = true;
			} else {
				isEndWithSlash = false;
			}
			if (tokenizer.ttype == STAGO || tokenizer.ttype == ETAGO) {
				if (handleError(IParserError.BEFORE_ATTRNAME, tokenizer.sval)) {
					continue;
				}
				error(IParserError.STARTTAG_SYNTAX_ERR, "requires an attribute in " + ret);
				tokenizer.pushBack();
				break;
			} else if (tokenizer.ttype == EOF) {
				break;
			}

			tokenizer.pushBack();
			attr = attribute(ed, attrlist);
			if (attr != null) {
				ret.setAttributeNode(attr);
				/*
				 * } else if (!syntaxError) { syntaxError = true; error(STARTTAG_SYNTAX_ERR,
				 * "requires an attribute after " + ret);
				 */
			}
		}

		if (docHandler != null && !eHandleLogical) {
			// if(dtd.toString().indexOf("XHTML") < 0 || !endWithSlash){
			docHandler.startElement(ret.getNodeName(), attrlist);
			// }
		}

		// TODO html5 support

		if (ed == null) {

			if (!handleError(IParserError.UNKNOWN_ELEMENT, ret)) {
				error(IParserError.UNKNOWN_ELEMENT, "Unknown Element: " + ret.getTagName() + ".  Ignore it.");
			}
			return null;
		} else if (ed == anonymousElementDef) {

			if (handleError(IParserError.UNKNOWN_ELEMENT, ret)) {
				return null;
			} else {
				error(IParserError.UNKNOWN_ELEMENT, "Unknown Element: " + ret.getTagName()
						+ ".  Define its definition as <!ELEMENT " + ret.getNodeName().toUpperCase() + " - - ANY>");
			}
		}

		currentTagName = ret.getNodeName();
		// if (isXHTML && isEndWithSlash) {
		// System.out.println("slash end tag");
		// if (docHandler != null && !eHandleLogical) {
		// docHandler.endElement(ret.getNodeName());
		// }
		// System.out.println(docHandler);
		// }
		return ret;
	}

	public String makeUnique(String id) {
		for (Enumeration<String> e = pubEntityMap.elements(); e.hasMoreElements();) {
			String ret = e.nextElement();
			if (id.equals(ret))
				return ret;
		}
		return id;
	}

	/**
	 * Changes default dtd. If &lt;!DOCTYPE ... &gt; statement is missing in a top
	 * of a document, a parser reads it by specifed dtd.
	 * 
	 * @param dtd dtd's public id for default like
	 *            <code>"-//W3C//DTD HTML 4.0 Transitional//EN"</code>
	 */
	public void setDefaultDTD(String dtd) {
		defaultDTD = makeUnique(dtd);
	}

	/**
	 * This instance variable holds a default DTD's public ID. If a parser meets a
	 * strange DOCTYPE declaration, it reads a document with this default DTD.
	 */
	protected String defaultDTD = null;

	/**
	 * Gets top element's name. If you want the parser to read document without
	 * DOCTYPE declaration at the top, you must override this method to return some
	 * element's name. For HTML documents, it returns "HTML".
	 * 
	 * @return top element's name
	 * @exception ParseException always thrown.
	 */
	protected String getDefaultTopElement() throws ParseException {
		if (defaultTopElement != null) {
			return defaultTopElement;
		}
		throw new ParseException("doesn't know which element must be at the top.");
	}

	private String defaultTopElement = null;

	public int defaultTagCase = IParser.UPPER_CASE;

	/**
	 * Specifies element names' case whose start tags are omitted. Default behavier
	 * makes them uppercased.
	 * 
	 * @param tagCase this must be {@link IParser#UPPER_CASE}or
	 *                {@link IParser#LOWER_CASE}. If otherwise, ignore.
	 */
	public void setDefaultTagCase(int tagCase) {
		if (tagCase == 0 || tagCase == IParser.LOWER_CASE) {
			this.defaultTagCase = tagCase;
		}
	}

	/**
	 * Change a specified string to specified cased.
	 * 
	 * @see #setDefaultTagCase(int)
	 */
	public String changeDefaultTagCase(String tag) {
		switch (this.defaultTagCase) {
		case IParser.UPPER_CASE:
			return tag.toUpperCase();
		case IParser.LOWER_CASE:
			return tag.toLowerCase();
		default:
			throw new RuntimeException("Internal Parser Error");
		}
	}

	public int tagCase = IParser.ORIGINAL_CASE;

	/**
	 * Specifies element names' case. Default behavier makes them original cased.
	 * 
	 * @param tagCase this must be {@link IParser#UPPER_CASE},
	 *                {@link IParser#LOWER_CASE}or {@link IParser#ORIGINAL_CASE}. If
	 *                otherwise, ignore.
	 */
	public void setTagCase(int tagCase) {
		if (tagCase == IParser.UPPER_CASE || tagCase == IParser.LOWER_CASE || tagCase == IParser.ORIGINAL_CASE) {
			this.tagCase = tagCase;
		}
	}

	final String changeTagCase(String tag) {
		switch (this.tagCase) {
		case IParser.UPPER_CASE:
			return tag.toUpperCase();
		case IParser.LOWER_CASE:
			return tag.toLowerCase();
		case IParser.ORIGINAL_CASE:
			return tag;
		default:
			throw new RuntimeException("Internal Parser Error");
		}
	}

	public int attrCase = IParser.ORIGINAL_CASE;

	/**
	 * Specifies attribute names' case. Default behavier makes them original cased.
	 * 
	 * @param attrCase this must be {@link IParser#UPPER_CASE},
	 *                 {@link IParser#LOWER_CASE}or {@link IParser#ORIGINAL_CASE}.
	 *                 If otherwise, ignore.
	 */
	public void setAttrNameCase(int attrCase) {
		if (attrCase == IParser.UPPER_CASE || attrCase == IParser.LOWER_CASE || attrCase == IParser.ORIGINAL_CASE) {
			this.attrCase = attrCase;
		}
	}

	final String changeAttrNameCase(String attr) {
		switch (this.attrCase) {
		case IParser.UPPER_CASE:
			return attr.toUpperCase();
		case IParser.LOWER_CASE:
			return attr.toLowerCase();
		case IParser.ORIGINAL_CASE:
			return attr;
		default:
			throw new RuntimeException("Internal Parser Error");
		}
	}

	private String enforcedDoctype = null;

	/**
	 * Ignores a declaration at the top of the document and enforces document type
	 * specified by <code>publicId</code>
	 */
	public void enforceDoctype(String publicId) {
		enforcedDoctype = makeUnique(publicId);
	}

	/**
	 * Closes input stream.
	 */
	public void close() throws IOException {
		tokenizer.close();
		tokenizer = null;
	}

	public Object extraErrInfo = null;

	/**
	 * Gets extra error information. A parser passes two error information (e.g.
	 * error code and error node) to node-level error handlers. However, it is now
	 * enough for some kind of error handlers to recover the error. If
	 * {@link IParserError#SUDDEN_ENDTAG}error occurs, parser set missed end tags
	 * between error node and future context to extra error information.
	 */
	public Object getExtraErrInfo() {
		return extraErrInfo;
	}

	private boolean handleError(int code, Node node) throws ParseException, IOException, SAXException {
		for (int i = errorHandlerNum - 1; i >= 0; i--) {
			if (errorHandlers[i].handleError(code, this, node)) {
				return true;
			}
		}
		return false;
	}

	boolean handleError(int code, String errorStr) throws ParseException, IOException {
		for (int i = tokenErrorHandlerNum - 1; i >= 0; i--) {
			if (tokenErrorHandlers[i].handleError(code, this, errorStr)) {
				return true;
			}
		}
		return false;
	}

	void putCharNumEntity(Character C, String ent) {
		Document doc = getDocument();
		if (doc instanceof SGMLDocument) {
			((SGMLDocument) doc).putCharNumEntity(C, ent);
		}
	}

	public DocumentHandler docHandler = null;

	/**
	 * Gets a <i>DocumentHandler </i> instance included in this parser.
	 * 
	 * @see #setDocumentHandler(org.xml.sax.DocumentHandler)
	 */
	public DocumentHandler getDocumentHandler() {
		return this.docHandler;
	}

	/**
	 * Sets a <i>DocumentHandler </i> instace for this parser.
	 * 
	 * @see #getDocumentHandler()
	 */
	public void setDocumentHandler(DocumentHandler handler) {
		this.docHandler = handler;
	}

	public boolean preserveWhitespace = false;

	/**
	 * Checks if parser preserve whitespaces or not.
	 * 
	 * @return <code>true</code> if this parser preserve whitespaces. Otherwise
	 *         <code>false</code>
	 */
	public boolean getPreserveWhitespace() {
		return preserveWhitespace;
	}

	/**
	 * Determines if this parser preserve whitespaces or not. If preserve, the
	 * parser create text node for whitespace between tags and does not ignore
	 * carriage return and line feed after start tags and before end tags. By
	 * default a parser ignores whitespaces.
	 * 
	 * @see #getPreserveWhitespace()
	 */
	public void setPreserveWhitespace(boolean preserv) {
		if (tokenizer != null) {
			tokenizer.setPreserveWhitespace(preserv);
		}
		this.preserveWhitespace = preserv;
	}

	/**
	 * Determines which this parser invokes
	 * {@link org.xml.sax.DocumentHandler#startElement(java.lang.String, org.xml.sax.AttributeList)}
	 * and {@link org.xml.sax.DocumentHandler#endElement(java.lang.String)}
	 * <code>logically</code> or <code>physically</code>.<code>
	 * Logical</code> means that if a start or end tag of a element is omitted, a
	 * parser invokes each method. <code>Physical</code> means that parsers invokes
	 * each method if and only if their tag appearently exist. If
	 * <code>physical</code>, a parser does not care if the tag is illegal or not.
	 * Default is <code>physical</code>
	 * 
	 * @param logical if true, deal with tags as logical. Otherwise, as physical
	 */
	public void elementHandle(boolean logical) {
		this.eHandleLogical = logical;
	}

	public boolean eHandleLogical = true;

	public Vector<Element> autoGenerated = new Vector<Element>();

	/**
	 * Checks if the specified element is automatically generated by this parser or
	 * not. For example, <code>TBODY</code> under <code>TABLE</code> is
	 * automatically generated in following document.
	 * 
	 * <PRE>
	 * 
	 * &lt;TABLE&gt; &lt;TR&gt;&lt;TD&gt;&lt;TD&gt; &lt;/TABLE&gt;
	 * 
	 * <PRE>
	 * 
	 * &#064;param an element node to be checked &#064;return
	 * &lt;code&gt;true&lt;/code&gt; if &lt;code&gt;element&lt;/code&gt; is
	 * automatically generated by this. Otherwise false.
	 */
	public boolean autoGenerated(Element element) {
		return autoGenerated.contains(element);
	}

	public final void addAutoGenerated(Element element) {
		autoGenerated.addElement(element);
	}

	/**
	 * Inserts a string to current position in read stream. This method is usually
	 * invoked by token-level error handlers.
	 * 
	 * @param str String to insert
	 */
	public void insert(String str) throws IOException {
		tokenizer.unread(str);
	}

	private AttributeListImpl createAttributeList() {
		return new AttributeListImpl();
	}

	// TODO to track this boolean
	private boolean keepUnknowns;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.dom.sgml.impl.ISGMLParser#keepUnknownElements(
	 * boolean)
	 */
	public void keepUnknownElements(boolean keep) {
		this.keepUnknowns = keep;
	}

	/**
	 * Gets current line number of source document.
	 * 
	 * @return line number
	 * @deprecated Use {@link org.xml.sax.Locator#getLineNumber()}
	 */
	public int getCurrentLineNumber() {
		return tokenizer.getCurrentLine();
	}

	/**
	 * Gets current column number of source document.
	 * 
	 * @return column number
	 * @deprecated Use {@link org.xml.sax.Locator#getColumnNumber()}
	 */
	public int getCurrentColumnNumber() {
		return tokenizer.getCurrentCol();
	}

	/**
	 * Gets current reading node
	 */
	public Node getCurrentNode() {
		return this.currentNode;
	}

	public Node currentNode;

	public void setCurrentNode(Node node) {
		this.currentNode = node;
	}

	private boolean keepComment = true;

	/**
	 * Determines if this parser keeps comments and processing instructions in the
	 * tree or not. By default, it keeps.
	 * 
	 * @param <code>true</code> if it keeps, Otherwise <code>false</code>
	 */
	public void setKeepComment(boolean keep) {
		this.keepComment = keep;
	}

	private char saxch[];

	private int begin, len;

	void setCharacter(char ch[], int begin, int len) {
		this.saxch = ch;
		this.begin = begin;
		this.len = len;
	}

	private LexicalHandler lexHandler;

	/**
	 * Note: does not support
	 * {@link org.xml.sax.ext.LexicalHandler#startEntity(java.lang.String)}and
	 * {@link org.xml.sax.ext.LexicalHandler#endEntity(java.lang.String)}.
	 */
	public void setLexicalHandler(LexicalHandler lexHandler) {
		this.lexHandler = lexHandler;
	}

	LexicalHandler getLexicalHandler() {
		return this.lexHandler;
	}

	void incrementDepth(int i) {
		this.depth += i;
	}

	/**
	 * @param i depth
	 */
	public void reopenContext(int i) throws SAXException {
		this.depth += i;
		this.context = ancesters[depth - 1];
		if (!eHandleLogical || docHandler == null)
			return;
		for (int j = depth; j < i; j++) {
			docHandler.startElement(ancesters[j].getNodeName(), nullAttributeList);
		}
	}

	public Element[] getContextElements() {
		Element ret[] = new Element[depth];
		System.arraycopy(ancesters, 0, ret, 0, depth);
		return ret;
	}

	/**
	 * Parses a fragment under specified context.
	 * 
	 */
	public void parseFragment(Element parent, Reader reader) throws IOException, ParseException, SAXException {
		if (dtd == null) {
			throw new ParseException("Can't parse without DTD");
		} else if (doc == null) {
			throw new ParseException("Can't parse without a Document");
		}
		this.ancesterElementDefs[0] = dtd.getElementDefinition(parent.getNodeName());
		if (this.ancesterElementDefs[0] == null) {
			this.ancesterElementDefs[0] = anonymousElementDef;
		}
		this.ancesters[0] = parent;
		this.context = parent;
		depth = 1;
		this.tokenizer = new InsTokenizer(reader, this);
		if (docHandler != null) {
			docHandler.setDocumentLocator(this.tokenizer);
		}
		tokenizer.extractNumEntity(extractNum);
		tokenizer.extractCharEntity(extractChar);
		tokenizer.setPreserveWhitespace(preserveWhitespace);

		readInstances2();
	}

	/**
	 * Parses a fragment. As a side effect, wastes a element node.
	 * 
	 */
	public DocumentFragment parseFragment(Reader reader) throws IOException, ParseException, SAXException {
		Element dummy = doc != null ? doc.createElement("dummy") : null;
		parseFragment(dummy, reader);
		DocumentFragment ret = doc.createDocumentFragment();
		for (Node child = dummy.getFirstChild(); child != null; child = dummy.getFirstChild()) {
			dummy.removeChild(child);
			ret.appendChild(child);
		}
		return ret;
	}

	public int getPushbackBufferSize() {
		return BUF_SIZ;
	}
}