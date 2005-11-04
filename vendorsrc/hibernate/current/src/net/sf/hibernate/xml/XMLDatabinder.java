//$Id: XMLDatabinder.java,v 1.14 2004/06/04 01:28:53 steveebersole Exp $
package net.sf.hibernate.xml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.hibernate.Databinder;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.cfg.Environment;
import net.sf.hibernate.collection.CollectionPersister;
import net.sf.hibernate.collection.PersistentCollection;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.persister.ClassPersister;
import net.sf.hibernate.proxy.HibernateProxy;
import net.sf.hibernate.proxy.HibernateProxyHelper;
import net.sf.hibernate.proxy.LazyInitializer;
import net.sf.hibernate.type.AbstractComponentType;
import net.sf.hibernate.type.BagType;
import net.sf.hibernate.type.ListType;
import net.sf.hibernate.type.MapType;
import net.sf.hibernate.type.PersistentCollectionType;
import net.sf.hibernate.type.SetType;
import net.sf.hibernate.type.StringType;
import net.sf.hibernate.type.Type;
import net.sf.hibernate.util.StringHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DOMWriter;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * Implementation of the API class Databinder. NOT threadsafe.
 * @see Databinder
 * @author Gavin King, Brad Clow
 */
public class XMLDatabinder implements Databinder {
	private SessionFactoryImplementor factory;
	private List objects = new ArrayList();
	private Set associatedObjects;
	private Set processedObjects;
	private Transformer transform;
	private boolean initializeLazy = false;
	
	private static final Log log = LogFactory.getLog(XMLDatabinder.class);
	
	public XMLDatabinder(SessionFactoryImplementor factory, Transformer transform) {
		this.factory = factory;
		this.transform = transform;
	}
	
	private ClassPersister getPersister(Class clazz) throws MappingException {
		return factory.getPersister(clazz);
	}
	
	public void setInitializeLazy(boolean initializeLazy) {
		this.initializeLazy = initializeLazy;
	}
	
	private Document toDocument() throws HibernateException {
		this.associatedObjects = new HashSet();
		this.processedObjects = new HashSet();
		
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("hibernate-generic");
		root.setAttributeValue("datetime", Hibernate.TIMESTAMP.toString( new Date(), factory) );
		Iterator iter;
		
		// keep going until we run out of bound objects and their associated objects
		while ( objects.size()>0 ) {
			iter = objects.iterator();
			while ( iter.hasNext() ) {
				Object object = iter.next();
				Element objectElem = root.addElement("object");
				object = maybeInitializeIfProxy(object, objectElem);
				if ( object!=null ) {
					addClass( objectElem, object.getClass() );
					ClassPersister persister = getPersister( object.getClass() );
					//ID
					if ( persister.hasIdentifierPropertyOrEmbeddedCompositeIdentifier() ) {
						Object id = persister.getIdentifier(object);
						Element elemElement = renderProperty(persister.getIdentifierPropertyName(), persister.getIdentifierType(), id, "composite-id", "id", null, true);
						objectElem.add(elemElement);
					}

					//PROPERTIES
					Type[] types = persister.getPropertyTypes();
					Object[] values = persister.getPropertyValues(object);
					String[] names = persister.getPropertyNames();
					//This approach wont work for components + collections
					for ( int i=0; i<types.length; i++ ) {
						objectElem.add( renderProperty( names[i], types[i], values[i], "component", "property", "collection", true ) );
					}
				}
			}
			
			processedObjects.addAll(objects);
			objects = new ArrayList(associatedObjects);
			associatedObjects = new HashSet();
		}
		return doc;
	}
	
	private void addClass(Element element, Class clazz) {
		String className = clazz.getName();
		element.setAttributeValue( "class", StringHelper.unqualify(className) );
		element.setAttributeValue( "package", StringHelper.qualifier(className) );
	}
	
	private Object maybeInitializeIfProxy(Object object, Element element) throws HibernateException {
		if ( !(object instanceof HibernateProxy) ) return object;
		
		LazyInitializer li = HibernateProxyHelper.getLazyInitializer( (HibernateProxy) object );
		
		if (li.isUninitialized() && !initializeLazy) {
			Class clazz = li.getPersistentClass();
			ClassPersister persister = getPersister(clazz);
			//ID
			if ( persister.hasIdentifierPropertyOrEmbeddedCompositeIdentifier() ) {
				Object id = li.getIdentifier();
				Element elemElement = renderProperty(persister.getIdentifierPropertyName(), persister.getIdentifierType(), id, "composite-id", "id", null, true);
				element.add(elemElement);
			}
			addClass(element, clazz);
		
			element.setAttributeValue("proxy", "uninitialized");
			return null;
		}
		else if ( li.isUninitialized() ) {
			element.setAttributeValue("proxy", "now-initialized");
		}
		else {
			element.setAttributeValue("proxy", "initialized");
		}
		
		return li.getImplementation();
	}
	
	public String toGenericXML() throws HibernateException {
		StringWriter writer = new StringWriter();
		XMLWriter outputter = new XMLWriter( writer, OutputFormat.createPrettyPrint() );
		//outputter.setNewlines(true);
		try {
			outputter.write( toDocument() );
		}
		catch (IOException ioe) {
			throw new HibernateException("could not XML to String", ioe);
		}
		return writer.toString();
	}
	
	public String toXML() throws HibernateException, TransformerException {

		Source source = new DocumentSource( toDocument() );
		StringWriter writer = new StringWriter();
		Result result = new StreamResult(writer);
		
		transform.setErrorListener( new ErrorListener() {
			public void warning(TransformerException te)
			throws TransformerException {
				
				log.warn("problem transforming to Custom XML: " + te.getMessageAndLocation(), te);
			}
			public void error(TransformerException te)
			throws TransformerException {
				
				log.error("problem transforming to Custom XML: " + te.getMessageAndLocation(), te);
			}
			public void fatalError(TransformerException te)
			throws TransformerException {
				error(te);
			}
		} );
		
		transform.transform(source, result);
		return writer.toString();
	}
	
	public org.w3c.dom.Document toDOM() throws HibernateException, TransformerException {
		Source source = new DocumentSource( toDocument() );
		DOMResult result = new DOMResult();
		transform.transform(source, result);
		return (org.w3c.dom.Document) result.getNode();
	}
	
	public Databinder bind(Object object) {
		objects.add(object);
		return this;
	}
	
	public Databinder bindAll(Collection collection) {
		this.objects.addAll(collection);
		return this;
	}
	
	public org.w3c.dom.Document toGenericDOM() throws HibernateException {
		DOMWriter outputter = new DOMWriter();
		try {
			return outputter.write( toDocument() );
		}
		catch (DocumentException jde) {
			throw new HibernateException( "Could not transform XML to a DOM", jde );
		}
	}

	public static Templates getOutputStyleSheetTemplates(Properties properties) {
		String xsltInputFile = properties.getProperty(Environment.OUTPUT_STYLESHEET);
		Templates templates = null;
		try {
			InputStream stream;
			if (xsltInputFile!=null) {
				// try getting stylesheet from the classpath first
				stream = Environment.class.getResourceAsStream(xsltInputFile);
				if (stream == null) {
					// not found, so try getting it from the filesystem
					stream = new FileInputStream(xsltInputFile);
				}
			}
			else {
				stream = Environment.class.getClassLoader().getResourceAsStream("net/sf/hibernate/hibernate-default.xslt");
			}
			templates = TransformerFactory.newInstance().newTemplates( new StreamSource(stream) );
		}
		catch (Exception e) {
			log.warn("Problem opening output stylesheet - databinding disabled", e);
			//throw new HibernateException( "Problem opening output stylesheet: " + e.getMessage() );
		}
		catch (TransformerFactoryConfigurationError tfce) {
			log.warn("no XSLT implementation found - databinding disabled");
		}
		return templates;
		
	}
	
	private Element renderProperty(String name, Type type, Object value, String componentName, String propertyName, String collectionName, boolean doType) throws HibernateException {
		if ( type.isComponentType() ) {
			return renderComponentType(name, type, value, componentName, doType);
		}
		else if ( type.isPersistentCollectionType() ) {
			return renderCollectionType(name, type, value, collectionName, doType);
		}
		else if ( type.isEntityType() ) {
			return renderEntityType(name, type, value, propertyName, doType);
		}
		else {
			return renderOtherType(name, type, value, propertyName, doType);
		}
	}
	
	private Element renderOtherType(String name, Type type, Object value, String propertyName, boolean doType)
	throws HibernateException {
		
		Element propertyElem = DocumentHelper.createElement(propertyName);
		if ( name!=null) propertyElem.setAttributeValue( "name", name );
		//propertyElem.setAttributeValue( "value", types[i].toXML( values[i] ) );
		if ( value!=null ) {
			String xmlValue = type.toString( value, factory );
			if ( type instanceof StringType ) { //TODO: make this polymorphic!!
				propertyElem.addCDATA(xmlValue);
			}
			else {
				propertyElem.setText(xmlValue);
			}
		}
		if (doType) propertyElem.setAttributeValue( "type", type.getName() );
		return propertyElem;
	}

	private Element renderEntityType(String name, Type type, Object value, String propertyName, boolean doType)
	throws HibernateException {
		
		Element referenceElem = DocumentHelper.createElement(propertyName);
		if ( name!=null) referenceElem.setAttributeValue( "name", name );
		//propertyElem.setAttributeValue( "value", types[i].toXML( values[i] ) );
		if ( ( value = maybeInitializeIfProxy(value, referenceElem) ) != null) {
			ClassPersister persister = getPersister( value.getClass() );
			if ( persister.hasIdentifierPropertyOrEmbeddedCompositeIdentifier() ) {
				Type idType = persister.getIdentifierType();
				Object id = persister.getIdentifier(value);
				referenceElem.add( renderProperty( persister.getIdentifierPropertyName(), idType, id, "composite-id", "id", null, true) );
			}
			addClass( referenceElem, value.getClass() );

			// avoid duplications (including objects that have a field referencing to themselves)
			if ( !processedObjects.contains(value) && !objects.contains(value) ) {
				associatedObjects.add(value);
			}
		}
		if (doType) referenceElem.setAttributeValue( "type", type.getName() );
		return referenceElem;
	}

	private Element renderCollectionType(String name, Type type, Object value, String collectionName, boolean doType)
	throws HibernateException {
		
		PersistentCollectionType collectiontype = (PersistentCollectionType) type;
		String role = collectiontype.getRole();
		CollectionPersister persister = factory.getCollectionPersister(role);
		Element collectionElem = DocumentHelper.createElement(collectionName);
		if (name!=null) collectionElem.setAttributeValue( "name", name );
		if ( persister.isArray() ) {
			collectionElem.setName("array");
		}
		else {
			if (doType) collectionElem.setAttributeValue( "class", type.getName() );
		}
		Type elemType = persister.getElementType();
		collectionElem.setAttributeValue( "element-type", elemType.getName() );

		if (value!=null) {
			// arrays can't be lazily initialized
			if ( persister.isArray() ) {
				collectionElem.setAttributeValue( "index-type", "integer" );
				int length = Array.getLength(value);
				for ( int i=0; i<length; i++ ) {
					Element elemElement = renderProperty(null, elemType, Array.get(value, i), "composite-element", "element", "subcollection", false);
					elemElement.setAttributeValue( "index", Integer.toString(i) );
					collectionElem.add(elemElement);
				}
			}
			else {
				// "real" collections
				PersistentCollection persistentCollection = (PersistentCollection) value;

				if ( persister.isLazy() && !this.initializeLazy && !persistentCollection.wasInitialized() ) {
					collectionElem.setAttributeValue("lazy", "uninitialized");
				}
				else {
					if ( persistentCollection.wasInitialized() ) {
						collectionElem.setAttributeValue("lazy", "initialized");
					}
					else {
						collectionElem.setAttributeValue("lazy", "now-initialized");
					}

					// Try to do this next bit polymorphically, instead of the following:
					if ( type instanceof ListType ) {
						collectionElem.setAttributeValue( "index-type", "integer" );
						Iterator iter = ( (List) value ).iterator();
						int i=0;
						while ( iter.hasNext() ) {
							Element elemElement = renderProperty(null, elemType, iter.next(), "composite-element", "element", "subcollection", false);
							elemElement.setAttributeValue( "index", Integer.toString(i++) );
							collectionElem.add(elemElement);
						}
					}
					else if ( (type instanceof SetType) || (type instanceof BagType) ) {
						Iterator iter = ( (Collection) value ).iterator();
						while ( iter.hasNext() ) {
							Element elemElement = renderProperty(null, elemType, iter.next(), "composite-element", "element", "subcollection", false);
							collectionElem.add(elemElement);
						}
					}
					else if ( type instanceof MapType ) {
						Type indexType = persister.getIndexType();
						collectionElem.setAttributeValue( "index-type", indexType.getName() );
						Iterator iter = ( (Map) value ).entrySet().iterator();
						while ( iter.hasNext() ) {
							Map.Entry e = (Map.Entry) iter.next();
							Object idx = e.getKey();
							Element elemElement = renderProperty(null, elemType, e.getValue(), "composite-element", "element", "subcollection", false);
							elemElement.setAttributeValue( "index", indexType.toString(idx, factory) ); //index not allowed to be null currently
							collectionElem.add(elemElement);
						}
					}
				}
			}
		}
		return collectionElem;
	}

	private Element renderComponentType(String name, Type type, Object value, String componentName, boolean doType)
	throws HibernateException {
		AbstractComponentType componenttype = (AbstractComponentType) type;
		Element componentElem = DocumentHelper.createElement(componentName);
		if ( name!=null) componentElem.setAttributeValue( "name", name );
		if (doType) componentElem.setAttributeValue( "class", type.getName() );
		if ( value!=null ) {
			String[] properties = componenttype.getPropertyNames();
			Object[] subvalues = componenttype.getPropertyValues(value, null); //We know that null is okay here .. at least for ComponentType .... TODO: something safer??
			Type[] subtypes = componenttype.getSubtypes();
			for ( int j=0; j<properties.length; j++ ) {
				componentElem.add( renderProperty( properties[j], subtypes[j], subvalues[j], "component", "property", "collection", true ) );
			}
		}
		return componentElem;
	}
	
}






