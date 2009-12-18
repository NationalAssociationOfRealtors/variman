/*
 * Variman RETS Server
 *
 * Author: RealGo
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * An entry in a request log.
 * 
 * @author Danny
 */
public class RequestLogEntry implements Map {

    private Map map = new LinkedHashMap();
    
    /**
     * 
     * @see java.util.Map#clear()
     */
    public void clear() {
        this.map.clear();
    }
    
    /**
     * @param key
     * @return
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object key) {
        boolean containsKey = this.map.containsKey(key);
        return containsKey;
    }
    
    /**
     * @param value
     * @return
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }
    
    /**
     * @return
     * @see java.util.Map#entrySet()
     */
    public Set entrySet() {
        return this.map.entrySet();
    }
    
    /**
     * @param o
     * @return
     * @see java.util.Map#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        return this.map.equals(o);
    }
    
    /**
     * @param key
     * @return
     * @see java.util.Map#get(java.lang.Object)
     */
    public Object get(Object key) {
        return this.map.get(key);
    }
    
    /**
     * @return
     * @see java.util.Map#hashCode()
     */
    public int hashCode() {
        return this.map.hashCode();
    }
    
    /**
     * @return
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
    
    /**
     * @return
     * @see java.util.Map#keySet()
     */
    public Set keySet() {
        return this.map.keySet();
    }
    
    /**
     * @param key
     * @param value
     * @return
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Object put(String key, Object value) {
        Object prevObj = put((Object)key, value);
        return prevObj;
    }
    
    /*- (non-Javadoc)
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Object put(Object key, Object value) {
        return this.map.put(key, value);
    }
    
    /**
     * @param t
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map t) {
        this.map.putAll(t);
    }
    
    /**
     * @param key
     * @return
     * @see java.util.Map#remove(java.lang.Object)
     */
    public Object remove(Object key) {
        return this.map.remove(key);
    }
    
    /**
     * @return
     * @see java.util.Map#size()
     */
    public int size() {
        return this.map.size();
    }
    
    /**
     * @return
     * @see java.util.Map#values()
     */
    public Collection values() {
        return this.map.values();
    }
    
}
