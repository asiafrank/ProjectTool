package com.asiafrank.tools;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

/**
 * @author asiafrank created at 1/5/2017.
 */
public abstract class Generator {
    public abstract void exec();

    static void mkdir(String dirName) {
        File dir = new File(dirName);
        dir.mkdirs();
    }

    void gen(String filePath, String ftlPath, Configuration ftlConfig, Map<Object, Object> map) {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
            Template template = ftlConfig.getTemplate(ftlPath);
            template.process(map, out);
            out.close();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
}
