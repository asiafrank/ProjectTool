package com.calanger.tools.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import com.calanger.tools.constant.Constants;

import freemarker.template.Configuration;
import freemarker.template.Template;

public final class GenerateUtils {
    public static void generateFile(String pathname, String templateName, Configuration configuration, Map<Object, Object> context) throws Exception {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathname), Constants.DEFAULT_CHARSET));
        Template template = configuration.getTemplate(templateName);
        template.process(context, out);
        out.close();
    }
}
