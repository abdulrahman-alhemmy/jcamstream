/*
 * JCamStream, simple Java application for video surveillance from webcams.
 * Copyright (C) 2011 Papa Issa DIAKHATE (paissad).
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.paissad.jcamstream.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class tends to work as the same as the class {@link Properties} except
 * that this class does not remove comments and/or blanks lines when
 * updating/storing the properties into a file.
 * 
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class PropertiesConf implements Serializable, Cloneable {

    private static final long          serialVersionUID = 1L;

    private static final String        LINE_SEPARATOR   = System.getProperty("line.separator");
    private static final int           BUFFER_SIZE      = 2048;
    private static final Charset       CHARSET          = Charset.forName("ISO-8859-1");

    private int                        totalLines       = 0;

    private SortedMap<Integer, Object> map              = null;

    // _________________________________________________________________________

    public PropertiesConf() {
    }

    // _________________________________________________________________________

    /**
     * Verify whether or not a properties is empty.
     * 
     * @return <code>true</code> if this properties has no key/value pair,
     *         <code>false</code> otherwise. Comments and blanks lines are
     *         ignored during the process, they do not take part of the
     *         properties.
     */
    public boolean isEmpty() {
        Collection<Object> values = map.values();
        synchronized (map) {
            Iterator<Object> iter = values.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if (obj instanceof Entry<?, ?>)
                    return false;
            }

        }
        return true;
    }

    // _________________________________________________________________________

    /**
     * Removes all the mappings for this properties.<br>
     * Even comments and blanks lines are removed.
     */
    public synchronized void clear() {
        map.clear();
        totalLines = 0;
    }

    // _________________________________________________________________________

    /**
     * Retrieves all keys of this properties.
     * 
     * @return The {@link Set} of keys of this properties, or <code>null</code>
     *         if there is no key.
     */
    public Set<String> keys() {
        Collection<Object> values = map.values();
        synchronized (map) {
            Iterator<Object> iter = values.iterator();
            Set<String> mapKeys = new LinkedHashSet<String>();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if (obj instanceof Entry<?, ?>) {
                    @SuppressWarnings("unchecked")
                    Entry<String, String> entry = ((Entry<String, String>) obj);
                    mapKeys.add(entry.getKey());
                }
            }
            return (mapKeys.size() != 0) ? mapKeys : null;
        }
    }

    // _________________________________________________________________________

    /**
     * Retrieves all values of this properties.
     * 
     * @return The {@link Collection} of values for this properties, or
     *         <code>null</code> if there is no value yet.
     */
    public Collection<String> values() {
        Collection<Object> values = map.values();
        synchronized (map) {
            Iterator<Object> iter = values.iterator();
            List<String> mapKeys = new LinkedList<String>();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if (obj instanceof Entry<?, ?>) {
                    @SuppressWarnings("unchecked")
                    Entry<String, String> entry = ((Entry<String, String>) obj);
                    mapKeys.add(entry.getValue());
                }
            }
            return (mapKeys.size() != 0) ? mapKeys : null;
        }
    }

    // _________________________________________________________________________

    /**
     * 
     * @param key
     *            - The key to search.
     * @return <code>true</code> if the key is present in the properties,
     *         <code>false</code> otherwise.
     */
    public boolean containsKey(final String key) {
        Collection<Object> mapValues = map.values();
        synchronized (map) {
            Iterator<Object> iter = mapValues.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if (obj instanceof Entry<?, ?>) {
                    @SuppressWarnings("unchecked")
                    Entry<String, String> entry = ((Entry<String, String>) obj);
                    if (entry.getKey().equals(key)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    // _________________________________________________________________________

    /**
     * 
     * @param value
     *            - The value to search.
     * @return <code>true</code> if the value is present in the properties,
     *         <code>false</code> otherwise.
     */
    public boolean containsValue(final String value) {
        Collection<Object> objs = map.values();
        synchronized (map) {
            Iterator<Object> iter = objs.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if (obj instanceof Entry<?, ?>) {
                    @SuppressWarnings("unchecked")
                    Entry<String, String> entry = ((Entry<String, String>) obj);
                    if (entry.getValue().equals(value)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    // _________________________________________________________________________

    /**
     * Retrieve the value for a specified key.
     * 
     * @param key
     *            - The key for which we want its value.
     * @return The value for the specified key, or <code>null</code> if the key
     *         is not found or value is itself null.
     */
    public String getProperty(final String key) {
        String result = null;
        synchronized (map) {
            Collection<Object> values = map.values();
            Iterator<Object> iter = values.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if (obj instanceof Entry<?, ?>) {
                    @SuppressWarnings("unchecked")
                    Entry<String, String> entry = (Entry<String, String>) obj;
                    if (entry.getKey().equals(key)) {
                        result = entry.getValue();
                        break;
                    }
                }
            }
            return result;
        }
    }

    // _________________________________________________________________________

    /**
     * This method is an alias to {@link #put(String, String)}.
     * 
     * @param key
     * @param value
     * @return Sets one property.
     * @see #put(String, String)
     */
    public String setProperty(final String key, final String value) {
        return put(key, value);
    }

    // _________________________________________________________________________

    /**
     * 
     * @param in
     * @throws IOException
     */
    public void load(final InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, CHARSET));
        load(reader);
    }

    /**
     * 
     * @param reader
     * @throws IOException
     */
    private void load(final Reader reader) throws IOException {
        LineNumberReader propsReader = new LineNumberReader(reader, BUFFER_SIZE);

        map = Collections.synchronizedSortedMap(new TreeMap<Integer, Object>());
        String line;

        while ((line = propsReader.readLine()) != null) {

            if (isBlank(line) || isComment(line)) {
                totalLines++;
                map.put(totalLines, line);

            } else {
                Entry<String, String> entry = getLineProps(line);
                String key = entry.getKey();
                String value = entry.getValue();
                put(key, value);
            }
        }
    }

    // _________________________________________________________________________

    /**
     * 
     * @param out
     * @throws IOException
     */
    public void store(OutputStream out) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, CHARSET));
        store(writer);
    }

    /**
     * 
     * @param writer
     * @throws IOException
     */
    private void store(Writer writer) throws IOException {
        String line;
        Object obj;
        Collection<Object> mapValues = map.values();
        synchronized (map) {
            Iterator<Object> iter = mapValues.iterator();
            while (iter.hasNext()) {
                obj = iter.next();
                if (obj instanceof String) { // A comment
                    line = (String) obj;
                }
                else { // A property (key/value pair)
                    @SuppressWarnings("unchecked")
                    Entry<String, String> entry = ((Entry<String, String>) obj);
                    line = entry.getKey() + " = " + entry.getValue();
                }
                line += LINE_SEPARATOR;
                writer.write(line, 0, line.length());
            }
        }
    }

    // _________________________________________________________________________

    /**
     * Adds a new property if not present yet, or updates an existent key.
     * 
     * @param key
     *            - The key to add or update if already present.
     * @param value
     *            - The value of for the specified key.
     * @return The previous value of the key or <code>null</code> if the key did
     *         not have a value or did not exist.
     */
    public String put(final String key, final String value) {
        Entry<String, String> newEntry =
                new SimpleEntry<String, String>(key, value);
        String previousValue = null;

        if (!containsKey(key)) { // If the key was not present yet !
            totalLines++;
            map.put(totalLines, newEntry);

        } else {

            // If the key is already present in the map, so let's update the
            // value of the property !
            Set<Integer> lineNumbers = map.keySet();
            synchronized (map) {
                Iterator<Integer> iter = lineNumbers.iterator();
                while (iter.hasNext()) {
                    int lineNum = iter.next();
                    Object obj = map.get(lineNum);
                    if (obj instanceof Entry<?, ?>) {
                        @SuppressWarnings("unchecked")
                        Entry<String, String> e = ((Entry<String, String>) obj);
                        if (e.getKey().equals(key)) {
                            previousValue = e.getValue();
                            map.put(lineNum, newEntry);
                            break;
                        }
                    }
                }
            }
        }
        return previousValue;
    }

    // _________________________________________________________________________

    /**
     * Adds a new property if not present yet, or updates an existent key.
     * 
     * @param key
     *            - The key to add or update if already present.
     * @param value
     *            - The value of for the specified key.
     * @param keyReferer
     *            - The name key from which we place the new key <b>after</b>.
     * @return The previous value of the key or <code>null</code> if the key did
     *         not have a value or did not exist.
     * @see #put(String, String)
     * @see #putBefore(String, String, String)
     */
    public synchronized String putAfter(final String key, final String value, final String keyReferer) {
        if (containsKey(key)) {
            return put(key, value);
        }
        Integer lineNum = getLineNumberOfSpecifiedKey(keyReferer);
        if (lineNum == null) {
            return put(key, value);

        } else {
            for (int i = totalLines; i > lineNum; i--) {
                Object obj = map.get(i); // Can be a comment or a property.
                map.put(i + 1, obj);
            }
            Entry<String, String> entry = new SimpleEntry<String, String>(key, value);
            map.put(lineNum + 1, entry);
            totalLines++;
            return null;
        }
    }

    // _________________________________________________________________________

    /**
     * 
     * Adds a new property if not present yet, or updates an existent key.
     * 
     * @param key
     *            - The key to add or update if already present.
     * @param value
     *            - The value of for the specified key.
     * @param keyReferer
     *            - The name key from which we place the new key <b>before</b>.
     * @return The previous value of the key or <code>null</code> if the key did
     *         not have a value or did not exist.
     * @see #put(String, String)
     * @see #putAfter(String, String, String)
     */
    public synchronized String putBefore(final String key, final String value, final String keyReferer) {
        if (containsKey(key)) {
            return put(key, value);
        }
        Integer lineNum = getLineNumberOfSpecifiedKey(keyReferer);
        if (lineNum == null) {
            return put(key, value);
        } else {
            for (int i = totalLines; i >= lineNum; i--) {
                Object obj = map.get(i);
                map.put(i + 1, obj);
            }
            Entry<String, String> entry = new SimpleEntry<String, String>(key, value);
            map.put(lineNum, entry);
            totalLines++;
            return null;
        }
    }

    // _________________________________________________________________________

    /**
     * Removes one property from the properties.
     * 
     * @param key
     *            - The name of the property to remove.
     * @return The value of the key which is removed, or <code>null</code> if
     *         the key did not exist or its associated value was null.
     */
    public String remove(final String key) {
        String currentValue = null;
        Set<Integer> keysObject = map.keySet();
        synchronized (map) {
            Iterator<Integer> iter = keysObject.iterator();
            while (iter.hasNext()) {
                int lineNum = iter.next();
                Object obj = map.get(lineNum);
                if (obj instanceof Entry<?, ?>) {
                    @SuppressWarnings("unchecked")
                    Entry<String, String> e = ((Entry<String, String>) obj);
                    if (e.getKey().equals(key)) {
                        currentValue = e.getValue();
                        map.remove(lineNum);
                        totalLines--;
                        break;
                    }
                }
            }
        }
        return currentValue;
    }

    // _________________________________________________________________________

    /**
     * Converts a line into an {@link Entry} if the specified line is not a
     * comment, nor a blank line.
     * 
     * @param line
     *            - The line to convert.
     * @return An <code>Entry<String, String></code> if the specified line is a
     *         valid key/value pair.
     * @throws IOException
     */
    private Entry<String, String> getLineProps(final String line) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(line.getBytes(CHARSET));

        try {
            Properties props = new Properties();
            props.load(in);

            if (!props.isEmpty()) {
                Enumeration<Object> keyObject = props.keys();
                String key = (String) keyObject.nextElement();
                String value = (String) props.get(key);
                return new SimpleEntry<String, String>(key, value);

            } else {
                // (empty) No key/value pair in this line ...
                return null;
            }

        } finally {
            if (in != null)
                in.close();
        }
    }

    /**
     * 
     * @param key
     *            - The key for which we want its line position/number
     * @return the line position/number if the key is found, <code>null</code>
     *         otherwise.
     */
    private Integer getLineNumberOfSpecifiedKey(final String key) {
        Set<Integer> mapKeys = map.keySet();
        synchronized (map) {
            Iterator<Integer> iter = mapKeys.iterator();
            while (iter.hasNext()) {
                Integer lineNum = iter.next();
                Object obj = map.get(lineNum);
                if (obj instanceof Entry<?, ?>) {
                    @SuppressWarnings("unchecked")
                    Entry<String, String> e = ((Entry<String, String>) obj);
                    if (e.getKey().equals(key)) {
                        return lineNum;
                    }
                }
            }
            return null;
        }
    }

    private boolean isComment(final String line) {
        String regex = "^\\s*(#|!)";
        return isMatch(line, regex);
    }

    private boolean isBlank(final String line) {
        String regex = "^\\s*$";
        return isMatch(line, regex);
    }

    private boolean isMatch(final String line, final String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    // _________________________________________________________________________

    @Override
    public Object clone() {
        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    // _________________________________________________________________________
}
