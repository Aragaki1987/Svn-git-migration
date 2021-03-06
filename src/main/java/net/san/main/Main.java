package net.san.main;

import net.san.repos.RepositoryUtil;
import net.san.util.DirectoryUtil;
import net.san.util.GitUlti;
import net.san.util.PropertyCache;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AnNN1 on 5/26/2017.
 */
public class Main {
    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws IOException, GitAPIException {
        PropertyCache.load("git.properties");
        GitUlti gitUlti = new GitUlti();
        gitUlti.deletePushedTag();
        gitUlti.deletePushedBranch();

        String localFolder = PropertyCache.get("local_folder");

        List<String> directoryList = DirectoryUtil.loadSubFolder(localFolder);

        //commitTrunkAsMaster();

        for (String dir : directoryList) {
            if (DirectoryUtil.validateFolderName(dir)) {
                continue;
            } else if (dir.equals("branches")) {
                String branchLocation = localFolder + "/branches";
                commitBranches(branchLocation);
            } else if (dir.equals("tags")) {
                String tagLocaltion = localFolder + "/tags";
                commitTags(tagLocaltion);
            }
        }


    }



    private static void commitBranches(String dir) {
        List<String> branchList = DirectoryUtil.loadSubFolder(dir);

        for (String branch : branchList) {
            commitFolderToBranch(dir, branch);
        }
    }

    private static void commitTags(String dir) {
        List<String> tagList = DirectoryUtil.loadSubFolder(dir);

        for (String tag : tagList) {
            commitFolderToTag(dir, tag);
        }
    }

    private static void commitTrunkAsMaster() {
        logger.info("START WORKING ON MASTER BRANCH " + PropertyCache.get("local_folder") + "/trunk");
        RepositoryUtil util = new RepositoryUtil(PropertyCache.get("local_folder") + "/trunk");
        util.init();
        util.add();
        util.commit();
        util.addRemote();
        util.push("master", "master");
        logger.info("END WORKING ON MASTER BRANCH ");
    }

    private static void commitFolderToTag(String dir, String folderName) {
        if (DirectoryUtil.validateFolderName(folderName)) {
            logger.info("START WORKING ON TAG " + folderName);
            RepositoryUtil util = new RepositoryUtil(dir + "/" + folderName);
            util.init();
            util.add();
            util.commit();
            util.addRemote();
            util.createNewTag(folderName);
            util.checkout(folderName);
            util.pushTag();
            logger.info("END WORKING ON TAG " + folderName);
        } else {
            logger.warn("Invalid foldername");
        }
    }

    private static void commitFolderToBranch(String dir, String folderName) {
        if (DirectoryUtil.validateFolderName(folderName)) {
            logger.info("START WORKING ON BRANCH " + folderName);
            RepositoryUtil util = new RepositoryUtil(dir + "/" + folderName);
            util.init();
            util.add();
            util.commit();
            util.addRemote();
            util.createNewBranch(folderName);
            util.checkout(folderName);
            util.push(folderName, folderName);
            logger.info("END WORKING ON BRANCH " + folderName);
        } else {
            logger.warn("Invalid foldername");
        }
    }


}
