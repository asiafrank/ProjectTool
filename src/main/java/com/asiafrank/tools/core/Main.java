package com.asiafrank.tools.core;

import com.asiafrank.tools.ProjectInfo;
import com.asiafrank.tools.util.DB;

/**
 * @author asiafrank created at 1/5/2017.
 */
public class Main {
    public static void main(String[] args) {
        ProjectInfo info = new ProjectInfo("E:\\personal",
                "test", "com.asiafrank.test");
        DBParam dbParam = new DBParam(
                "sample",
                "postgres",
                "postgres", DB.POSTGRESQL, "", "",
                new String[]{"sample"});

        MvnGenerator mg = new MvnGenerator(info, dbParam);
        mg.execute();
    }
}
