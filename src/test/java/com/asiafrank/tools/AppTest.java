package com.asiafrank.tools;

import com.asiafrank.tools.core.CoreGenerator;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhangxiaofan 2019/05/11-10:11
 */
public class AppTest {

    @Test
    public void capitalizeTest() {
        String actual = CoreGenerator.capitalize("cpp");
        Assert.assertEquals("Cpp", actual);

        actual = CoreGenerator.uncapitalize("CppCpp");
        Assert.assertEquals("cppCpp", actual);
    }
}
