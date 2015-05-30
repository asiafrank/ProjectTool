package com.calanger.tools.util;

import com.calanger.tools.util.PropertyNameUtils;

import junit.framework.TestCase;

public class PropertyNameUtilsTest extends TestCase {
    public void testGetMethodNameSuffix() {
        TestCase.assertEquals("Xcoordinate", PropertyNameUtils.getMethodNameSuffix("xcoordinate"));
        TestCase.assertEquals("xCoordinate", PropertyNameUtils.getMethodNameSuffix("xCoordinate"));
        TestCase.assertEquals("XCoordinate", PropertyNameUtils.getMethodNameSuffix("XCoordinate"));
        try {
            PropertyNameUtils.getMethodNameSuffix("Xcoordinate");
            TestCase.fail();
        } catch (RuntimeException e) {

        }
        try {
            PropertyNameUtils.getMethodNameSuffix("X");
            TestCase.fail();
        } catch (RuntimeException e) {

        }
    }
}
