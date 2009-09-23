package org.realtors.rets.client;

import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;


/**
 * Representation of a single object returned
 * from a RETS server.
 * 
 * @author jrayburn
 */
public class SingleObjectResponse {

	public static final String CONTENT_TYPE = "Content-Type";
	public static final String LOCATION = "Location";
	public static final String CONTENT_DESCRIPTION = "Content-Description";
	public static final String OBJECT_ID = "Object-ID";
	public static final String CONTENT_ID = "Content-ID";

	private Map<String, String> headers;
	private InputStream inputStream;

	public SingleObjectResponse(Map<String, String> headers, InputStream in) {
		this.headers = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        this.headers.putAll(headers);
        this.inputStream = in;
	}

	public String getType() {
		return (String) this.headers.get(CONTENT_TYPE);
	}

	public String getContentID() {
		return (String) this.headers.get(CONTENT_ID);
	}

	public String getObjectID() {
		return (String) this.headers.get(OBJECT_ID);
	}

	public String getDescription() {
		return (String) this.headers.get(CONTENT_DESCRIPTION);
	}

	public String getLocation() {
		return (String) this.headers.get(LOCATION);
	}

	public InputStream getInputStream() {
		return this.inputStream;
	}
}
