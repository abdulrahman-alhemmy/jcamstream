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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class PropertiesConfTest {

    private static File testPropsFile = new File("src/test/resources/testProps.conf");
    private static File tempFile      = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        tempFile = File.createTempFile("testPropsTemp", ".conf", null);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (tempFile != null && tempFile.exists())
            FileUtils.forceDelete(tempFile);
    }

    /**
     * Test method for
     * {@link net.paissad.jcamstream.utils.PropertiesConf#PropertiesConf()}.
     */
    @Test
    public final void testPropertiesConf() {
        new PropertiesConf();
    }

    /**
     * Test method for
     * {@link net.paissad.jcamstream.utils.PropertiesConf#isEmpty()}.
     * 
     * @throws IOException
     */
    @Test
    public final void testIsEmpty() throws IOException {
        int[] expecteds = new int[] { 0, 0 };
        int[] actuals = new int[] {
                this.isEmpty_1(),
                this.isEmpty_2()
        };
        Assert.assertArrayEquals(expecteds, actuals);
    }

    private final int isEmpty_1() throws IOException {
        PropertiesConf props = new PropertiesConf();
        FileUtils.touch(tempFile);
        props.load(new FileInputStream(tempFile));
        return (props.isEmpty()) ? 0 : 1;
    }

    private final int isEmpty_2() throws IOException {
        PropertiesConf props = new PropertiesConf();
        props.load(new FileInputStream(testPropsFile));
        return (props.isEmpty()) ? 2 : 0;
    }

    /**
     * Test method for
     * {@link net.paissad.jcamstream.utils.PropertiesConf#clear()}.
     * 
     * @throws IOException
     */
    @Test
    public final void testClear() throws IOException {
        int[] expecteds = new int[] { 0 };
        int[] actuals = new int[] {
                this.clear_1(),
        };
        Assert.assertArrayEquals(expecteds, actuals);
    }

    private final int clear_1() throws FileNotFoundException, IOException {
        PropertiesConf props = new PropertiesConf();
        props.load(new FileInputStream(testPropsFile));
        props.clear();
        return (props.isEmpty()) ? 0 : 1;
    }

    /**
     * Test method for
     * {@link net.paissad.jcamstream.utils.PropertiesConf#keys()}.
     * 
     * @throws IOException
     */
    @Test
    public final void testKeys() throws IOException {
        PropertiesConf props = new PropertiesConf();
        props.load(new FileInputStream(testPropsFile));

        Set<String> expected_keys = new HashSet<String>(), actual_keys;

        expected_keys.add("aa");
        expected_keys.add("bb");
        expected_keys.add("cc");

        actual_keys = props.keys();
        Assert.assertEquals(expected_keys, actual_keys);

        props.put("dd", "DDD");
        expected_keys.add("dd");
        actual_keys = props.keys();
        Assert.assertEquals(expected_keys, actual_keys);

        props.remove("aa");
        expected_keys.remove("aa");
        actual_keys = props.keys();
        Assert.assertEquals(expected_keys, actual_keys);

        props.clear();
        actual_keys = props.keys();
        Assert.assertNull(actual_keys);
    }

    /**
     * Test method for
     * {@link net.paissad.jcamstream.utils.PropertiesConf#values()}.
     * 
     * @throws IOException
     */
    @Test
    public final void testValues() throws IOException {
        PropertiesConf props = new PropertiesConf();
        props.load(new FileInputStream(testPropsFile));

        Collection<String> expected_values = new ArrayList<String>(), actual_values;

        expected_values.add("AAA");
        expected_values.add("BBB");
        expected_values.add("CCC");

        actual_values = props.values();
        Assert.assertEquals(expected_values, actual_values);

        props.put("dd", "DDD");
        expected_values.add("DDD");
        actual_values = props.values();
        Assert.assertEquals(expected_values, actual_values);

        props.remove("aa");
        expected_values.remove("AAA");
        actual_values = props.values();
        Assert.assertEquals(expected_values, actual_values);

        props.clear();
        actual_values = props.values();
        Assert.assertNull(actual_values);
    }

    /**
     * Test method for
     * {@link net.paissad.jcamstream.utils.PropertiesConf#containsKey(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testContainsKey() throws IOException {
        PropertiesConf props = new PropertiesConf();
        props.load(new FileInputStream(testPropsFile));
        Assert.assertTrue(props.containsKey("aa"));
        Assert.assertFalse(props.containsKey("AAA"));
        props.clear();
        Assert.assertFalse(props.containsKey("aa"));
        props.put("xx", "XXX");
        Assert.assertTrue(props.containsKey("xx"));
    }

    /**
     * Test method for
     * {@link net.paissad.jcamstream.utils.PropertiesConf#containsValue(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testContainsValue() throws IOException {
        PropertiesConf props = new PropertiesConf();
        props.load(new FileInputStream(testPropsFile));
        Assert.assertTrue(props.containsValue("AAA"));
        props.put("aa", "newAAA");
        Assert.assertFalse(props.containsValue("AAA"));
    }

    /**
     * Test method for
     * {@link net.paissad.jcamstream.utils.PropertiesConf#getProperty(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testGetProperty() throws IOException {
        PropertiesConf props = new PropertiesConf();
        props.load(new FileInputStream(testPropsFile));
        String actual = props.getProperty("aa");
        String expected = "AAA";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for
     * {@link net.paissad.jcamstream.utils.PropertiesConf#setProperty(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testSetProperty() {
        // This is a alias of put() .... just refer to the test for the method
        // put()
    }

    /**
     * Test method for
     * {@link net.paissad.jcamstream.utils.PropertiesConf#load(java.io.InputStream)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testLoad() throws IOException {
        PropertiesConf props = new PropertiesConf();
        props.load(new FileInputStream(testPropsFile));
    }

    /**
     * Test method for
     * {@link net.paissad.jcamstream.utils.PropertiesConf#store(java.io.OutputStream)}
     * .
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Test
    public final void testStore() throws FileNotFoundException, IOException {
        PropertiesConf props = new PropertiesConf();
        props.load(new FileInputStream(testPropsFile));
        FileOutputStream out = new FileOutputStream(tempFile);
        props.store(out);
        // TODO make a better test !
    }

    /**
     * Test method for
     * {@link net.paissad.jcamstream.utils.PropertiesConf#put(java.lang.String, java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPut() throws IOException {
        PropertiesConf props = new PropertiesConf();
        props.load(new FileInputStream(testPropsFile));
        props.put("aa", "XXX");
        Assert.assertEquals("XXX", props.getProperty("aa"));
        props.put("ee", "EEE");
        Assert.assertEquals("EEE", props.getProperty("ee"));
    }

    /**
     * Test method for
     * {@link net.paissad.jcamstream.utils.PropertiesConf#putAfter(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Test
    public final void testPutAfter() throws FileNotFoundException, IOException {
        PropertiesConf props = new PropertiesConf();
        props.load(new FileInputStream(testPropsFile));
        props.putAfter("aa_after", "AFTER", "aa");
        // TODO make a better test !
    }

    /**
     * Test method for
     * {@link net.paissad.jcamstream.utils.PropertiesConf#putBefore(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Test
    public final void testPutBefore() throws FileNotFoundException, IOException {
        PropertiesConf props = new PropertiesConf();
        props.load(new FileInputStream(testPropsFile));
        props.putBefore("bb_BEFORE", "BEFORE", "bb");
        // TODO make a better test !
    }

    /**
     * Test method for
     * {@link net.paissad.jcamstream.utils.PropertiesConf#remove(java.lang.String)}
     * .
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Test
    public final void testRemove() throws FileNotFoundException, IOException {
        PropertiesConf props = new PropertiesConf();
        props.load(new FileInputStream(testPropsFile));
        props.remove("cc");
        Assert.assertFalse(props.containsKey("cc"));
    }

    /**
     * Test method for
     * {@link net.paissad.jcamstream.utils.PropertiesConf#clone()}.
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Test
    public final void testClone() throws FileNotFoundException, IOException {
        PropertiesConf props = new PropertiesConf();
        props.load(new FileInputStream(testPropsFile));
        PropertiesConf props2 = (PropertiesConf) props.clone();
        Assert.assertEquals(props2.keys(), props.keys());
        Assert.assertEquals(props2.values(), props.values());
        Assert.assertNotSame(props2.hashCode(), props.hashCode());
    }

}
