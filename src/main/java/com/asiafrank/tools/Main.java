package com.asiafrank.tools;

import com.asiafrank.tools.util.ProjectInfo;
import com.asiafrank.tools.core.DBParam;
import com.asiafrank.tools.core.MvnGenerator;
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

        /*CoreGenerator cg = new CoreGenerator(info, dbParam);
        cg.execute();*/
    }
}
