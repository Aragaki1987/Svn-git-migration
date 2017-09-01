package net.san.util;


import org.apache.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by AnNN1 on 5/26/2017.
 */
public class DirectoryUtil {
    private static Logger logger = Logger.getLogger(DirectoryUtil.class);

    public static List<String> loadSubFolder(String dirLoc) {
        if(dirLoc == null || dirLoc.isEmpty()) {
            logger.error("Source Directory is not valid : " + dirLoc);
            return Collections.emptyList();
        }

        File directory = new File(dirLoc);
        if(!directory.exists() || !directory.isDirectory()) {
            logger.error("Directory location is not exist or not a directory");
            return Collections.emptyList();
        } else {
            List<String> result = new ArrayList<String>();
            String[] directories = directory.list(new FilenameFilter() {
                public boolean accept(File current, String name) {
                    return new File(current, name).isDirectory();
                }
            });

            result.addAll(Arrays.asList(directories));

            return result;
        }
    }

}
