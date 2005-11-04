//$Id: XMLHelper.java,v 1.10 2004/06/04 01:28:53 steveebersole Exp $
package net.sf.hibernate.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.io.DOMReader;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public final class XMLHelper {
	
	/**
	 * Create a dom4j SAXReader which will append all validation errors
	 * to errorList
	 */
	public static SAXReader createSAXReader(String file, List errorsList) {
		SAXReader reader = new SAXReader();
		reader.setEntityResolver(DTD_RESOLVER);
		reader.setErrorHandler( new ErrorLogger(file, errorsList) );
		reader.setMergeAdjacentText(true);
		reader.setValidation(true);
		return reader;
	}
	
	/**
	 * Create a dom4j DOMReader
	 */
	public static DOMReader createDOMReader() {
		DOMReader reader = new DOMReader();
		return reader;
	}
	
	private static final Log log = LogFactory.getLog(XMLHelper.class);
	private static final EntityResolver DTD_RESOLVER = new DTDEntityResolver();
	
	public static class ErrorLogger implements ErrorHandler {
		private String file;
		private List errors;
		ErrorLogger(String file, List errors) {
			this.file=file;
			this.errors = errors;
		}
		public void error(SAXParseException error) {
			log.error( "Error parsing XML: " + file + '(' + error.getLineNumber() + ") " + error.getMessage() );
			errors.add(error);
		}
		public void fatalError(SAXParseException error) {
			error(error);
		}
		public void warning(SAXParseException warn) {
			log.warn( "Warning parsing XML: " + file + '(' + warn.getLineNumber() + ") " + warn.getMessage() );
		}
	}
	
	private XMLHelper() { //cannot be instantiated
	}
	
}






