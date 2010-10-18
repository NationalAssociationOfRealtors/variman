/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell, Mark Klein, Danny Hurlburt
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.mutable.MutableLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MEditMask;
import org.realtors.rets.common.metadata.types.MForeignKey;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MLookupType;
import org.realtors.rets.common.metadata.types.MObject;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MSearchHelp;
import org.realtors.rets.common.metadata.types.MSystem;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.common.metadata.types.MUpdate;
import org.realtors.rets.common.metadata.types.MUpdateHelp;
import org.realtors.rets.common.metadata.types.MUpdateType;
import org.realtors.rets.common.metadata.types.MValidationExpression;
import org.realtors.rets.common.metadata.types.MValidationExternal;
import org.realtors.rets.common.metadata.types.MValidationExternalType;
import org.realtors.rets.common.metadata.types.MValidationLookup;
import org.realtors.rets.common.metadata.types.MValidationLookupType;
import org.xml.sax.InputSource;

public class JDomCompactBuilder extends MetadataBuilder {

	private static final Log LOG = LogFactory.getLog(JDomCompactBuilder.class);

	public static final String CONTAINER_ROOT = "RETS";
	public static final String CONTAINER_METADATA = "METADATA";
	public static final String CONTAINER_SYSTEM = "METADATA-SYSTEM";
	public static final String CONTAINER_RESOURCE = "METADATA-RESOURCE";
	public static final String CONTAINER_FOREIGNKEY_1_7_2 = "METADATA-FOREIGN_KEYS";
	public static final String CONTAINER_FOREIGNKEY = "METADATA-FOREIGNKEYS";
	public static final String CONTAINER_CLASS = "METADATA-CLASS";
	public static final String CONTAINER_TABLE = "METADATA-TABLE";
	public static final String CONTAINER_UPDATE = "METADATA-UPDATE";
	public static final String CONTAINER_UPDATETYPE = "METADATA-UPDATE_TYPE";
	public static final String CONTAINER_OBJECT = "METADATA-OBJECT";
	public static final String CONTAINER_SEARCHHELP = "METADATA-SEARCH_HELP";
	public static final String CONTAINER_EDITMASK = "METADATA-EDITMASK";
	public static final String CONTAINER_UPDATEHELP = "METADATA-UPDATE_HELP";
	public static final String CONTAINER_LOOKUP = "METADATA-LOOKUP";
	public static final String CONTAINER_LOOKUPTYPE = "METADATA-LOOKUP_TYPE";
	public static final String CONTAINER_VALIDATIONLOOKUP = "METADATA-VALIDATION_LOOKUP";
	public static final String CONTAINER_VALIDATIONLOOKUPTYPE = "METADATA-VALIDATION_LOOKUP_TYPE";
	public static final String CONTAINER_VALIDATIONEXPRESSION = "METADATA-VALIDATION_EXPRESSION";
	public static final String CONTAINER_VALIDATIONEXTERNAL = "METADATA-VALIDATION_EXTERNAL";
	public static final String CONTAINER_VALIDATIONEXTERNALTYPE = "METADATA-VALIDATION_EXTERNAL_TYPE";
	public static final String ELEMENT_SYSTEM = "SYSTEM";
	public static final String COLUMNS = "COLUMNS";
	public static final String DATA = "DATA";
	public static final String ATTRIBUTE_RESOURCE = "Resource";
	public static final String ATTRIBUTE_CLASS = "Class";
	public static final String ATTRIBUTE_UPDATE = "Update";
	public static final String ATTRIBUTE_LOOKUP = "Lookup";
	public static final String ATTRIBUTE_VALIDATIONEXTERNAL = "ValidationExternal";
	public static final String ATTRIBUTE_VALIDATIONEXTERNALNAME = "ValidationExternalName";
	public static final String ATTRIBUTE_VALIDATIONLOOKUP = "ValidationLookup";

	@Override
	public Metadata doBuild(Object src) throws MetadataException {
		return build((Document) src);
	}

	/**
	 * Builds an in-memory metadata representation of the specified XML
	 * document.
	 * 
	 * @param source The document to convert to an in-memory metadata
	 *        representation. Must not be {@code null}.
	 * @return An in-memory metadata representation of the specified XML
	 * document.
	 * @throws MetadataException If unable to successfully convert.
	 */
	public Metadata build(InputSource source) throws MetadataException {
		SAXBuilder builder = new SAXBuilder();
		Document document;
		try {
			document = builder.build(source);
		} catch (JDOMException e) {
			throw new MetadataException("Couldn't build document", e);
		} catch (IOException e) {
			throw new MetadataException("Couldn't build document", e);
		}
		return build(document);
	}

	@Override
	public MetaObject[] parse(Object src) throws MetadataException {
		return parse((Document) src);
	}

	private static void setUniqueId(MetaObject metaObject, MutableLong nextId) {
		metaObject.setUniqueId(Long.valueOf(nextId.longValue()));
		nextId.increment();
	}

	public MetaObject[] parse(Document src) throws MetadataException {
		Element root = src.getRootElement();
		if (!root.getName().equals(CONTAINER_ROOT)) {
			throw new MetadataException("Invalid root element");
		}
		Element container = root.getChild(CONTAINER_SYSTEM);
		if (container != null) {
			MSystem system = processSystem(container);
			MutableLong nextId = new MutableLong(1);
			setUniqueId(system, nextId);
			if (root.getChild(CONTAINER_RESOURCE) != null) {
				Metadata metadata = new Metadata(system);
				recurseAll(root, nextId, metadata);
			}
			return new MetaObject[] { system };
		}
		container = root.getChild(CONTAINER_RESOURCE);
		if (container != null) {
			return processResource(container);
		}
		container = root.getChild(CONTAINER_FOREIGNKEY);
		if (container != null) {
			return processForeignKey(container);
		}
		container = root.getChild(CONTAINER_CLASS);
		if (container != null) {
			return processClass(container);
		}
		container = root.getChild(CONTAINER_TABLE);
		if (container != null) {
			return processTable(container);
		}
		container = root.getChild(CONTAINER_UPDATE);
		if (container != null) {
			return processUpdate(container);
		}
		container = root.getChild(CONTAINER_UPDATETYPE);
		if (container != null) {
			return processUpdateType(container);
		}
		container = root.getChild(CONTAINER_OBJECT);
		if (container != null) {
			return processObject(container);
		}
		container = root.getChild(CONTAINER_SEARCHHELP);
		if (container != null) {
			return processSearchHelp(container);
		}
		container = root.getChild(CONTAINER_EDITMASK);
		if (container != null) {
			return processEditMask(container);
		}
		container = root.getChild(CONTAINER_UPDATEHELP);
		if (container != null) {
			return processUpdateHelp(container);
		}
		container = root.getChild(CONTAINER_LOOKUP);
		if (container != null) {
			return processLookup(container);
		}
		container = root.getChild(CONTAINER_LOOKUPTYPE);
		if (container != null) {
			return processLookupType(container);
		}
		container = root.getChild(CONTAINER_VALIDATIONLOOKUP);
		if (container != null) {
			return processValidationLookup(container);
		}
		container = root.getChild(CONTAINER_VALIDATIONLOOKUPTYPE);
		if (container != null) {
			return processValidationLookupType(container);
		}
		container = root.getChild(CONTAINER_VALIDATIONEXTERNAL);
		if (container != null) {
			return processValidationExternal(container);
		}
		container = root.getChild(CONTAINER_VALIDATIONEXTERNALTYPE);
		if (container != null) {
			return processValidationExternalType(container);
		}
		container = root.getChild(CONTAINER_VALIDATIONEXPRESSION);
		if (container != null) {
			return processValidationExpression(container);
		}
		return null;
	}

	/**
	 * Builds an in-memory metadata representation of the specified XML
	 * document.
	 * 
	 * @param src The document to convert to an in-memory metadata
	 *         representation. Must not be {@code null}.
	 * @return An in-memory metadata representation of the specified XML
	 * document.
	 * @throws MetadataException If unable to successfully convert.
	 */
	public Metadata build(Document src) throws MetadataException {
		Element root = src.getRootElement();
		if (!root.getName().equals(CONTAINER_ROOT)) {
			throw new MetadataException("Invalid root element");
		}
		Element element = root.getChild(CONTAINER_SYSTEM);
		if (element == null) {
			throw new MetadataException("Missing element " + CONTAINER_SYSTEM);
		}
		MSystem system = processSystem(element);
		MutableLong nextId = new MutableLong(1);
		setUniqueId(system, nextId);
		
		Metadata metadata = new Metadata(system);
		recurseAll(root, nextId, metadata);
		return metadata;
	}

	private void recurseAll(Element root, MutableLong nextId, Metadata metadata) throws MetaParseException {
		Map<String, MResource> resourcesMap = attachResource(root, nextId, metadata);
		Map<String, MClass> classesMap = attachClass(root, nextId, metadata, resourcesMap);
		Map<String, MEditMask> editMasksMap = attachEditMask(root, nextId, metadata, resourcesMap);
		Map<String, MLookup> lookupsMap = attachLookup(root, nextId, metadata, resourcesMap);
		attachLookupType(root, nextId, metadata, lookupsMap);
		attachObject(root, nextId, metadata, resourcesMap);
		Map<String, MSearchHelp> searchHelpsMap = attachSearchHelp(root, nextId, metadata, resourcesMap);
		Map<String, MUpdateHelp> updateHelpsMap = attachUpdateHelp(root, nextId, metadata, resourcesMap);
		Map<String, MValidationExpression> validationExpressionsMap = attachValidationExpression(root, nextId, metadata, resourcesMap);
		Map<String, MValidationExternal> validationExternalsMap = attachValidationExternal(root, nextId, metadata, resourcesMap, classesMap);
		attachValidationExternalType(root, nextId, metadata, validationExternalsMap);
		Map<String, MValidationLookup> validationLookupsMap = attachValidationLookup(root, nextId, metadata, resourcesMap);
		attachValidationLookupType(root, nextId, metadata, validationLookupsMap);
		Map<String, MTable> tablesMap = attachTable(root, nextId, metadata, classesMap, editMasksMap, lookupsMap, searchHelpsMap);
		Map<String, MUpdate> updatesMap = attachUpdate(root, nextId, metadata, classesMap);
		attachUpdateType(root, nextId, metadata, updatesMap, tablesMap, updateHelpsMap, validationExpressionsMap, validationExternalsMap, validationLookupsMap);
		attachForeignKey(root, nextId, metadata, tablesMap);
	}

	private void setAttributes(MetaObject obj, String[] columns, String[] data) {
		int count = columns.length;
		if (count > data.length) {
			count = data.length;
		}
		for (int i = 0; i < count; i++) {
			String column = columns[i];
			String datum = data[i];
			if (!datum.equals("")) {
				setAttribute(obj, column, datum);
			}
		}
	}

	private String[] getColumns(Element el) {
		Element cols = el.getChild(COLUMNS);
		return split(cols);
	}

	/* do NOT use string.split() unless you're prepared to deal with loss due to token boundary conditions */
	private String[] split(Element el) {
		if( el == null ) return null;
		// TODO: Allow the delimiter to be configurable. Will need to do the same in MetadataCompactFormatter.
		final String delimiter = "\t";
		StringTokenizer tkn = new StringTokenizer(el.getText(), delimiter, true);
		List<String> list = new ArrayList<String>();
		tkn.nextToken(); // junk the first element
		String last = null;
		while (tkn.hasMoreTokens()) {
			String next = tkn.nextToken();
			if (next.equals(delimiter)) {
				if (last == null) {
					list.add("");
				} else {
					last = null;
				}
			} else {
				list.add(next);
				last = next;
			}
		}
		String[] data = list.toArray(new String[0]);
		return data;
	}

	/**
	 * Gets an attribute that is not expected to be null (i.e. an attribute that
	 * MUST exist).
	 *
	 * @param element Element
	 * @param name Attribute name
	 * @return value of attribute
	 * @throws MetaParseException if the value is null.
	 */
	private String getNonNullAttribute(Element element, String name) throws MetaParseException {
		String value = element.getAttributeValue(name);
		if (value == null) {
			throw new MetaParseException("Attribute '" + name + "' not found on tag " + toString(element));
		}
		return value;
	}

	private String toString(Element element) {
		StringBuilder sb = new StringBuilder();
		String elementName = element.getName();
		List<?> attributes = element.getAttributes();
		sb.append("'");
		sb.append(elementName);
		sb.append("'");
		sb.append(", attributes: ");
		sb.append(attributes);
		return sb.toString();
	}

	private MSystem processSystem(Element container) {
		Element element = container.getChild(ELEMENT_SYSTEM);
		MSystem system = buildSystem();
		// system metadata is such a hack.  the first one here is by far my favorite
		String comment = container.getChildText(MSystem.COMMENTS);
		String systemId = element.getAttributeValue(MSystem.SYSTEMID);
		String systemDescription = element.getAttributeValue(MSystem.SYSTEMDESCRIPTION);
		String timeZoneOffset = element.getAttributeValue(MSystem.TIMEZONEOFFSET);
		String version = container.getAttributeValue(MSystem.VERSION);
		String date = container.getAttributeValue(MSystem.DATE);
		setAttribute(system, MSystem.COMMENTS, comment);
		setAttribute(system, MSystem.SYSTEMID, systemId);
		setAttribute(system, MSystem.SYSTEMDESCRIPTION, systemDescription);
		setAttribute(system, MSystem.VERSION, version);
		setAttribute(system, MSystem.DATE, date);
		setAttribute(system, MSystem.TIMEZONEOFFSET, timeZoneOffset);
		return system;
	}

	private Map<String, MResource> attachResource(Element root, MutableLong nextId, Metadata metadata) {
		Map<String, MResource> resourcesMap = new LinkedHashMap<String, MResource>();
		List<?> containers = root.getChildren(CONTAINER_RESOURCE);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			MSystem parent = metadata.getSystem();
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MResource[] resources = processResource(container);
			for (MResource resource : resources) {
				setUniqueId(resource, nextId);
				parent.addChild(MetadataType.RESOURCE, resource);
			}
			/*
			 * Note: extractPaths must be called after adding child to parent
			 * so that path has been properly set.
			 */
			extractPaths(resources, resourcesMap);
		}
		return resourcesMap;
	}

	private MResource[] processResource(Element resourceContainer) {
		String[] columns = getColumns(resourceContainer);
		List<?> rows = resourceContainer.getChildren(DATA);
		MResource[] resources = new MResource[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MResource resource = buildResource();
			setAttributes(resource, columns, data);
			resources[i] = resource;
		}
		return resources;
	}

	private void attachForeignKey(Element root, MutableLong nextId, Metadata metadata, Map<String, MTable> tablesMap) {
		List<?> containers = root.getChildren(CONTAINER_FOREIGNKEY);
		// In case someone is following the Spec and not the DTD.
		if (containers != null && containers.isEmpty()) {
			containers = root.getChildren(CONTAINER_FOREIGNKEY_1_7_2);
		}
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			MSystem parent = metadata.getSystem();
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MForeignKey[] foreignKeys = this.processForeignKey(container);
			for (MForeignKey foreignKey : foreignKeys) {
				setUniqueId(foreignKey, nextId);
				String resourceId = foreignKey.getChildResourceID();
				String className = foreignKey.getChildClassID();
				String systemName = foreignKey.getChildSystemName();
				String path = resourceId + ":" + className + ":" + systemName;
				MTable table = tablesMap.get(path);
				if (table == null) {
					if (LOG.isWarnEnabled()) {
						String foreignKeyId = foreignKey.getForeignKeyID();
						String msg = "Unable to find METADATA-FOREIGN_KEYS(" + path + ") for foreign-key '" + foreignKeyId + "'.";
						LOG.warn(msg);
					}
				} else {
					foreignKey.setChildTable(table);
				}
				resourceId = foreignKey.getParentResourceID();
				className = foreignKey.getParentClassID();
				systemName = foreignKey.getParentSystemName();
				path = resourceId + ":" + className + ":" + systemName;
				table = tablesMap.get(path);
				if (table == null) {
					if (LOG.isWarnEnabled()) {
						String foreignKeyId = foreignKey.getForeignKeyID();
						String msg = "Unable to find METADATA-FOREIGN_KEYS(" + path + ") for foreign-key '" + foreignKeyId + "'.";
						LOG.warn(msg);
					}
				} else {
					foreignKey.setParentTable(table);
				}
				parent.addChild(MetadataType.FOREIGN_KEYS, foreignKey);
			}
		}
	}

	private MForeignKey[] processForeignKey(Element foreignkeyContainer) {
		String[] columns = getColumns(foreignkeyContainer);
		List<?> rows = foreignkeyContainer.getChildren(DATA);
		MForeignKey[] foreignKeys = new MForeignKey[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MForeignKey foreignKey = buildForeignKey();
			setAttributes(foreignKey, columns, data);
			foreignKeys[i] = foreignKey;
		}
		return foreignKeys;
	}

	private Map<String, MClass> attachClass(Element root, MutableLong nextId, Metadata metadata, Map<String, MResource> resourcesMap) throws MetaParseException {
		Map<String, MClass> classesMap = new LinkedHashMap<String, MClass>();
		List<?> containers = root.getChildren(CONTAINER_CLASS);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String path = resourceId;
			MResource parent = resourcesMap.get(path);
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MClass[] classes = processClass(container);
			for (MClass clazz : classes) {
				setUniqueId(clazz, nextId);
				parent.addChild(MetadataType.CLASS, clazz);
			}
			/*
			 * Note: extractPaths must be called after adding child to parent
			 * so that path has been properly set.
			 */
			extractPaths(classes, classesMap);
		}
		return classesMap;
	}

	private MClass[] processClass(Element classContainer) throws MetaParseException {
		if (LOG.isDebugEnabled()) {
			String name = classContainer.getName();
			String resourceId = getNonNullAttribute(classContainer, ATTRIBUTE_RESOURCE);
			LOG.debug("resource name: " + resourceId + " for container " + name);
		}
		String[] columns = getColumns(classContainer);
		List<?> rows = classContainer.getChildren(DATA);
		MClass[] classes = new MClass[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MClass clazz = buildClass();
			setAttributes(clazz, columns, data);
			classes[i] = clazz;
		}
		return classes;
	}

	private Map<String, MTable> attachTable(Element root, MutableLong nextId, Metadata metadata, Map<String, MClass> classesMap, Map<String, MEditMask> editMasksMap, Map<String, MLookup> lookupsMap, Map<String, MSearchHelp> searchHelpsMap) throws MetaParseException {
		Map<String, MTable> tablesMap = new LinkedHashMap<String, MTable>();
		List<?> containers = root.getChildren(CONTAINER_TABLE);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String className = getNonNullAttribute(container, ATTRIBUTE_CLASS);
			String path = resourceId + ":" + className;
			MClass parent = classesMap.get(path);
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MTable[] tables = processTable(container);
			for (MTable table : tables) {
				setUniqueId(table, nextId);
				String editMaskIdCsv = table.getEditMaskID();
				Collection<String> editMaskIds = MTable.toEditMaskIds(editMaskIdCsv);
				if (editMaskIds != null) {
					Set<MEditMask> editMasks = new LinkedHashSet<MEditMask>();
					for (String editMaskId : editMaskIds) {
						path = resourceId + ":" + editMaskId;
						MEditMask editMask = editMasksMap.get(path);
						if (editMask == null) {
							if (LOG.isWarnEnabled()) {
								String systemName = table.getSystemName();
								String msg = "Unable to find METADATA-EDITMASK(" + path + ") for table '" + systemName + "'.";
								LOG.warn(msg);
							}
						} else {
							editMasks.add(editMask);
						}
					}
					table.setEditMasks(editMasks);
				}
				String lookupName = table.getLookupName();
				if (StringUtils.isNotBlank(lookupName)) {
					path = resourceId + ":" + lookupName;
					MLookup lookup = lookupsMap.get(path);
					if (lookup == null) {
						if (LOG.isWarnEnabled()) {
							String systemName = table.getSystemName();
							String msg = "Unable to find METADATA-LOOKUP(" + path + ") for table '" + systemName + "'.";
							LOG.warn(msg);
						}
					} else {
						table.setMLookup(lookup);
					}
				}
				String searchHelpId = table.getSearchHelpID();
				if (StringUtils.isNotBlank(searchHelpId)) {
					path = resourceId + ":" + searchHelpId;
					MSearchHelp searchHelp = searchHelpsMap.get(path);
					if (searchHelp == null) {
						if (LOG.isWarnEnabled()) {
							String systemName = table.getSystemName();
							String msg = "Unable to find METADATA-SEARCH_HELP(" + path + ") for table '" + systemName + "'.";
							LOG.warn(msg);
						}
					} else {
						table.setMSearchHelp(searchHelp);
					}
				}
				parent.addChild(MetadataType.TABLE, table);
			}
			/*
			 * Note: extractPaths must be called after adding child to parent
			 * so that path has been properly set.
			 */
			extractPaths(tables, tablesMap);
		}
		return tablesMap;
	}

	private MTable[] processTable(Element tableContainer) {
		String[] columns = getColumns(tableContainer);
		List<?> rows = tableContainer.getChildren(DATA);
		MTable[] fieldMetadata = new MTable[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MTable mTable = buildTable();
			setAttributes(mTable, columns, data);
			fieldMetadata[i] = mTable;
		}
		return fieldMetadata;
	}

	private Map<String, MUpdate> attachUpdate(Element root, MutableLong nextId, Metadata metadata, Map<String, MClass> classesMap) throws MetaParseException {
		Map<String, MUpdate> updatesMap = new LinkedHashMap<String, MUpdate>();
		List<?> containers = root.getChildren(CONTAINER_UPDATE);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String className = getNonNullAttribute(container, ATTRIBUTE_CLASS);
			String path = resourceId + ":" + className;
			MClass parent = classesMap.get(path);
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MUpdate[] updates = processUpdate(container);
			for (MUpdate update : updates) {
				setUniqueId(update, nextId);
				parent.addChild(MetadataType.UPDATE, update);
			}
			/*
			 * Note: extractPaths must be called after adding child to parent
			 * so that path has been properly set.
			 */
			extractPaths(updates, updatesMap);
		}
		return updatesMap;
	}

	private MUpdate[] processUpdate(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		MUpdate[] updates = new MUpdate[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MUpdate update = buildUpdate();
			setAttributes(update, columns, data);
			updates[i] = update;
		}
		return updates;
	}

	private void attachUpdateType(Element root, MutableLong nextId, Metadata metadata, Map<String, MUpdate> updatesMap, Map<String, MTable> tablesMap, Map<String, MUpdateHelp> updateHelpsMap, Map<String, MValidationExpression> validationExpressionsMap, Map<String, MValidationExternal> validationExternalsMap, Map<String, MValidationLookup> validationLookupsMap) throws MetaParseException {
		List<?> containers = root.getChildren(CONTAINER_UPDATETYPE);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String className = getNonNullAttribute(container, ATTRIBUTE_CLASS);
			String updateName = getNonNullAttribute(container, ATTRIBUTE_UPDATE);
			String path = resourceId + ":" + className + ":" + updateName;
			MUpdate parent = updatesMap.get(path);
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MUpdateType[] updateTypes = processUpdateType(container);
			for (MUpdateType updateType : updateTypes) {
				setUniqueId(updateType, nextId);
				String systemName = updateType.getSystemName();
				path = resourceId + ":" + className + ":" + systemName;
				MTable table = tablesMap.get(path);
				if (table == null) {
					if (LOG.isWarnEnabled()) {
						String msg = "Unable to find METADATA-TABLE(" + path + ") for update-type '" + systemName + "'.";
						LOG.warn(msg);
					}
				} else {
					updateType.setMTable(table);
				}
				String updateHelpId = updateType.getUpdateHelpID();
				path = resourceId + ":" + updateHelpId;
				MUpdateHelp updateHelp = updateHelpsMap.get(path);
				if (updateHelp == null) {
					if (LOG.isWarnEnabled()) {
						String msg = "Unable to find METADATA-UPDATE_HELP(" + path + ") for update-type '" + systemName + "'.";
						LOG.warn(msg);
					}
				} else {
					updateType.setMUpdateHelp(updateHelp);
				}
				String validationExpressionIdCsv = updateType.getValidationExpressionID();
				Collection<String> validationExpressionIds = MUpdateType.toValidationExpressionIds(validationExpressionIdCsv);
				if (validationExpressionIds != null) {
					Set<MValidationExpression> validationExpressions = new LinkedHashSet<MValidationExpression>();
					for (String validationExpressionId : validationExpressionIds) {
						path = resourceId + ":" + validationExpressionId;
						MValidationExpression validationExpression = validationExpressionsMap.get(path);
						if (validationExpression == null) {
							if (LOG.isWarnEnabled()) {
								String msg = "Unable to find METADATA-VALIDATION_EXPRESSION(" + path + ") for update-type '" + systemName + "'.";
								LOG.warn(msg);
							}
						} else {
							validationExpressions.add(validationExpression);
						}
					}
					updateType.setMValidationExpressions(validationExpressions);
				}
				String validationExternalName = updateType.getValidationExternalName();
				path = resourceId + ":" + validationExternalName;
				MValidationExternal validationExternal = validationExternalsMap.get(path);
				if (validationExternal == null) {
					if (LOG.isWarnEnabled()) {
						String msg = "Unable to find METADATA-VALIDATION_EXTERNAL(" + path + ") for update-type '" + systemName + "'.";
						LOG.warn(msg);
					}
				} else {
					updateType.setMValidationExternal(validationExternal);
				}
				String validationLookupName = updateType.getValidationLookupName();
				path = resourceId + ":" + validationLookupName;
				MValidationLookup validationLookup = validationLookupsMap.get(path);
				if (validationLookup == null) {
					if (LOG.isWarnEnabled()) {
						String msg = "Unable to find METADATA-VALIDATION_LOOKUP(" + path + ") for update-type '" + systemName + "'.";
						LOG.warn(msg);
					}
				} else {
					updateType.setMValidationLookup(validationLookup);
				}
				parent.addChild(MetadataType.UPDATE_TYPE, updateType);
			}
		}
	}

	private MUpdateType[] processUpdateType(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		MUpdateType[] updateTypes = new MUpdateType[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MUpdateType updateType = buildUpdateType();
			setAttributes(updateType, columns, data);
			updateTypes[i] = updateType;
		}
		return updateTypes;
	}

	private void attachObject(Element root, MutableLong nextId, Metadata metadata, Map<String, MResource> resourcesMap) throws MetaParseException {
		List<?> containers = root.getChildren(CONTAINER_OBJECT);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String path = resourceId;
			MResource parent = resourcesMap.get(path);
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MObject[] objects = processObject(container);
			for (MObject object : objects) {
				setUniqueId(object, nextId);
				parent.addChild(MetadataType.OBJECT, object);
			}
		}
	}

	private MObject[] processObject(Element objectContainer) {
		String[] columns = getColumns(objectContainer);
		List<?> rows = objectContainer.getChildren(DATA);
		MObject[] objects = new MObject[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MObject object = buildObject();
			setAttributes(object, columns, data);
			objects[i] = object;
		}
		return objects;
	}

	private Map<String, MSearchHelp> attachSearchHelp(Element root, MutableLong nextId, Metadata metadata, Map<String, MResource> resourcesMap) throws MetaParseException {
		Map<String, MSearchHelp> searchHelpsMap = new LinkedHashMap<String, MSearchHelp>();
		List<?> containers = root.getChildren(CONTAINER_SEARCHHELP);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String path = resourceId;
			MResource parent = resourcesMap.get(path);
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MSearchHelp[] searchHelps = processSearchHelp(container);
			for (MSearchHelp searchHelp : searchHelps) {
				setUniqueId(searchHelp, nextId);
				parent.addChild(MetadataType.SEARCH_HELP, searchHelp);
			}
			/*
			 * Note: extractPaths must be called after adding child to parent
			 * so that path has been properly set.
			 */
			extractPaths(searchHelps, searchHelpsMap);
		}
		return searchHelpsMap;
	}

	private MSearchHelp[] processSearchHelp(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		MSearchHelp[] searchHelps = new MSearchHelp[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MSearchHelp searchHelp = buildSearchHelp();
			setAttributes(searchHelp, columns, data);
			searchHelps[i] = searchHelp;
		}
		return searchHelps;
	}

	private Map<String, MEditMask> attachEditMask(Element root, MutableLong nextId, Metadata metadata, Map<String, MResource> resourcesMap) throws MetaParseException {
		Map<String, MEditMask> editMasksMap = new LinkedHashMap<String, MEditMask>();
		List<?> containers = root.getChildren(CONTAINER_EDITMASK);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String path = resourceId;
			MResource parent = resourcesMap.get(path);
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MEditMask[] editMasks = processEditMask(container);
			for (MEditMask editMask : editMasks) {
				setUniqueId(editMask, nextId);
				parent.addChild(MetadataType.EDITMASK, editMask);
			}
			/*
			 * Note: extractPaths must be called after adding child to parent
			 * so that path has been properly set.
			 */
			extractPaths(editMasks, editMasksMap);
		}
		return editMasksMap;
	}

	private MEditMask[] processEditMask(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		MEditMask[] editMasks = new MEditMask[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MEditMask editMask = buildEditMask();
			setAttributes(editMask, columns, data);
			editMasks[i] = editMask;
		}
		return editMasks;
	}
	
	private Map<String, MUpdateHelp> attachUpdateHelp(Element root, MutableLong nextId, Metadata metadata, Map<String, MResource> resourcesMap) throws MetaParseException {
		Map<String, MUpdateHelp> updateHelpsMap = new LinkedHashMap<String, MUpdateHelp>();
		List<?> containers = root.getChildren(CONTAINER_UPDATEHELP);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String path = resourceId;
			MResource parent = resourcesMap.get(path);
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MUpdateHelp[] updateHelps = processUpdateHelp(container);
			for (MUpdateHelp updateHelp : updateHelps) {
				setUniqueId(updateHelp, nextId);
				parent.addChild(MetadataType.UPDATE_HELP, updateHelp);
			}
			/*
			 * Note: extractPaths must be called after adding child to parent
			 * so that path has been properly set.
			 */
			extractPaths(updateHelps, updateHelpsMap);
		}
		return updateHelpsMap;
	}

	private MUpdateHelp[] processUpdateHelp(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		MUpdateHelp[] updateHelps = new MUpdateHelp[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MUpdateHelp updateHelp = buildUpdateHelp();
			setAttributes(updateHelp, columns, data);
			updateHelps[i] = updateHelp;
		}
		return updateHelps;
	}

	private Map<String, MLookup> attachLookup(Element root, MutableLong nextId, Metadata metadata, Map<String, MResource> resourcesMap) throws MetaParseException {
		Map<String, MLookup> lookupsMap = new LinkedHashMap<String, MLookup>();
		List<?> containers = root.getChildren(CONTAINER_LOOKUP);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String path = resourceId;
			MResource parent = resourcesMap.get(path);
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MLookup[] lookups = processLookup(container);
			for (MLookup lookup : lookups) {
				setUniqueId(lookup, nextId);
				parent.addChild(MetadataType.LOOKUP, lookup);
			}
			/*
			 * Note: extractPaths must be called after adding child to parent
			 * so that path has been properly set.
			 */
			extractPaths(lookups, lookupsMap);
		}
		return lookupsMap;
	}

	private MLookup[] processLookup(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		MLookup[] lookups = new MLookup[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MLookup lookup = buildLookup();
			setAttributes(lookup, columns, data);
			lookups[i] = lookup;
		}
		return lookups;
	}

	private void attachLookupType(Element root, MutableLong nextId, Metadata metadata, Map<String, MLookup> lookupsMap) throws MetaParseException {
		List<?> containers = root.getChildren(CONTAINER_LOOKUPTYPE);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String lookupName = getNonNullAttribute(container, ATTRIBUTE_LOOKUP);
			String path = resourceId + ":" + lookupName;
			MLookup parent = lookupsMap.get(path);
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MLookupType[] lookupTypes = processLookupType(container);
			for (MLookupType lookupType : lookupTypes) {
				setUniqueId(lookupType, nextId);
				parent.addChild(MetadataType.LOOKUP_TYPE, lookupType);
			}
		}
	}

	private MLookupType[] processLookupType(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		MLookupType[] lookupTypes = new MLookupType[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MLookupType lookupType = buildLookupType();
			setAttributes(lookupType, columns, data);
			lookupTypes[i] = lookupType;
		}
		return lookupTypes;
	}

	private Map<String, MValidationLookup> attachValidationLookup(Element root, MutableLong nextId, Metadata metadata, Map<String, MResource> resourcesMap) throws MetaParseException {
		Map<String, MValidationLookup> validationLookupsMap = new LinkedHashMap<String, MValidationLookup>();
		List<?> containers = root.getChildren(CONTAINER_VALIDATIONLOOKUP);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String path = resourceId;
			MResource parent = resourcesMap.get(path);
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MValidationLookup[] validationLookups = processValidationLookup(container);
			for (MValidationLookup validationLookup : validationLookups) {
				setUniqueId(validationLookup, nextId);
				parent.addChild(MetadataType.VALIDATION_LOOKUP, validationLookup);
			}
			/*
			 * Note: extractPaths must be called after adding child to parent
			 * so that path has been properly set.
			 */
			extractPaths(validationLookups, validationLookupsMap);
		}
		return validationLookupsMap;
	}

	private MValidationLookup[] processValidationLookup(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		MValidationLookup[] validationLookups = new MValidationLookup[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MValidationLookup validationLookup = buildValidationLookup();
			setAttributes(validationLookup, columns, data);
			validationLookups[i] = validationLookup;
		}
		return validationLookups;
	}

	private void attachValidationLookupType(Element root, MutableLong nextId, Metadata metadata, Map<String, MValidationLookup> validationLookupsMap) throws MetaParseException {
		List<?> containers = root.getChildren(CONTAINER_VALIDATIONLOOKUPTYPE);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String validationLookupName = getNonNullAttribute(container, ATTRIBUTE_VALIDATIONLOOKUP);
			String path = resourceId + ":" + validationLookupName;
			MValidationLookup parent = validationLookupsMap.get(path);
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MValidationLookupType[] validationLookupTypes = processValidationLookupType(container);
			for (MValidationLookupType validationLookupType : validationLookupTypes) {
				setUniqueId(validationLookupType, nextId);
				parent.addChild(MetadataType.VALIDATION_LOOKUP_TYPE, validationLookupType);
			}
		}
	}

	private MValidationLookupType[] processValidationLookupType(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		MValidationLookupType[] validationLookupTypes = new MValidationLookupType[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MValidationLookupType validationLookupType = buildValidationLookupType();
			setAttributes(validationLookupType, columns, data);
			validationLookupTypes[i] = validationLookupType;
		}
		return validationLookupTypes;
	}

	private Map<String, MValidationExternal> attachValidationExternal(Element root, MutableLong nextId, Metadata metadata, Map<String, MResource> resourcesMap, Map<String, MClass> classesMap) {
		Map<String, MValidationExternal> validationExternalsMap = new LinkedHashMap<String, MValidationExternal>();
		List<?> containers = root.getChildren(CONTAINER_VALIDATIONEXTERNAL);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String path = resourceId;
			MResource parent = resourcesMap.get(path);
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MValidationExternal[] validationExternals = processValidationExternal(container);
			for (MValidationExternal validationExternal : validationExternals) {
				setUniqueId(validationExternal, nextId);
				String searchResourceId = validationExternal.getSearchResource();
				String searchClassName = validationExternal.getSearchClass();
				path = searchResourceId + ":" + searchClassName;
				MClass clazz = classesMap.get(path);
				if (clazz == null) {
					if (LOG.isWarnEnabled()) {
						String validationExternalName = validationExternal.getValidationExternalName();
						String msg = "Unable to find METADATA-CLASS(" + path + ") for validation external '" + validationExternalName + "'.";
						LOG.warn(msg);
					}
				} else {
					validationExternal.setMClass(clazz);
				}
				parent.addChild(MetadataType.VALIDATION_EXTERNAL, validationExternal);
			}
			/*
			 * Note: extractPaths must be called after adding child to parent
			 * so that path has been properly set.
			 */
			extractPaths(validationExternals, validationExternalsMap);
		}
		return validationExternalsMap;
	}

	private MValidationExternal[] processValidationExternal(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		MValidationExternal[] validationExternals = new MValidationExternal[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MValidationExternal validationExternal = buildValidationExternal();
			setAttributes(validationExternal, columns, data);
			validationExternals[i] = validationExternal;
		}
		return validationExternals;
	}

	private void attachValidationExternalType(Element root, MutableLong nextId, Metadata metadata, Map<String, MValidationExternal> validationExternalsMap) throws MetaParseException {
		List<?> containers = root.getChildren(CONTAINER_VALIDATIONEXTERNALTYPE);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String validationExternalName = getNonNullAttribute(container, ATTRIBUTE_VALIDATIONEXTERNALNAME);
			String path = resourceId + ":" + validationExternalName;
			MValidationExternal parent = validationExternalsMap.get(path);
			if (parent == null) {
				/*
				 * If this is RETS 1.5 metadata, the attribute could be called
				 * "ValidationExternal". Try based on that.
				 */
				validationExternalName = getNonNullAttribute(container, ATTRIBUTE_VALIDATIONEXTERNAL);
				path = resourceId + ":" + validationExternalName;
				parent = validationExternalsMap.get(path);
			}
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MValidationExternalType[] validationExternalTypes = processValidationExternalType(container);
			for (MValidationExternalType validationExternalType : validationExternalTypes) {
				setUniqueId(validationExternalType, nextId);
				parent.addChild(MetadataType.VALIDATION_EXTERNAL_TYPE, validationExternalType);
			}
		}
	}

	private MValidationExternalType[] processValidationExternalType(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		MValidationExternalType[] validationExternalTypes = new MValidationExternalType[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			MValidationExternalType validationExternalType = buildValidationExternalType();
			setAttributes(validationExternalType, columns, data);
			validationExternalTypes[i] = validationExternalType;
		}
		return validationExternalTypes;
	}

	private Map<String, MValidationExpression> attachValidationExpression(Element root, MutableLong nextId, Metadata metadata, Map<String, MResource> resourcesMap) throws MetaParseException {
		Map<String, MValidationExpression> validationExpressionsMap = new LinkedHashMap<String, MValidationExpression>();
		List<?> containers = root.getChildren(CONTAINER_VALIDATIONEXPRESSION);
		for (int i = 0; i < containers.size(); i++) {
			Element container = (Element) containers.get(i);
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String path = resourceId;
			MResource parent = resourcesMap.get(path);
			if (parent == null) {
				if (LOG.isWarnEnabled()) {
					String msg = "Unable to find parent for " + toString(container);
					LOG.warn(msg);
				}
				continue;
			}
			MValidationExpression[] validationExpressions = processValidationExpression(container);
			for (MValidationExpression validationExpression : validationExpressions) {
				setUniqueId(validationExpression, nextId);
				parent.addChild(MetadataType.VALIDATION_EXPRESSION, validationExpression);
			}
			/*
			 * Note: extractPaths must be called after adding child to parent
			 * so that path has been properly set.
			 */
			extractPaths(validationExpressions, validationExpressionsMap);
		}
		return validationExpressionsMap;
	}

	private MValidationExpression[] processValidationExpression(Element container) {
		String[] columns = getColumns(container);
		List<?> elmts = container.getChildren(DATA);
		int numElmts = elmts.size();
		MValidationExpression[] validationExpressions = new MValidationExpression[elmts.size()];
		for (int i = 0; i < numElmts; i++) {
			Element element = (Element) elmts.get(i);
			String[] data = split(element);
			MValidationExpression validationExpression = buildValidationExpression();
			setAttributes(validationExpression, columns, data);
			validationExpressions[i] = validationExpression;
		}
		return validationExpressions;
	}

	private static void extractPaths(MetaObject[] metaObjects, Map pathsMap) {
		if (metaObjects == null || pathsMap == null) {
			return;
		}
		for (MetaObject metaObject : metaObjects) {
			extractPath(metaObject, pathsMap);
		}
	}

	private static void extractPaths(List<? extends MetaObject> metaObjects, Map pathsMap) {
		if (metaObjects == null || pathsMap == null) {
			return;
		}
		for (MetaObject metaObject : metaObjects) {
			extractPath(metaObject, pathsMap);
		}
	}
	
	private static void extractPath(MetaObject metaObject, Map pathsMap) {
		if (metaObject == null) {
			return;
		}
		String path = metaObject.getPath();
		if (StringUtils.isBlank(path)) {
			if (LOG.isWarnEnabled()) {
				String idName = metaObject.getIdAttr();
				String typeName = metaObject.getMetadataTypeName();
				String msg = "Unable to extract " + idName + " from " + typeName + ".";
				LOG.warn(msg);
			}
		} else {
			pathsMap.put(path, metaObject);
		}
	}

}
