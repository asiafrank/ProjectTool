package com.calanger.tools.util;

import java.io.File;

public final class DirUtils {
    public static void mkdir(String dirName) {
        File dir = new File(dirName);
        dir.mkdirs();

        System.out.println("mkdir: " + dirName);
    }

    private DirUtils() {
    }
}
