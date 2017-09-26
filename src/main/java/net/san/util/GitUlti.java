package net.san.util;

import net.san.repos.RepositoryUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by san on 9/4/17.
 */
public class GitUlti {

    private static Logger logger = Logger.getLogger(GitUlti.class);

    public List<String> listTagName() {
        List<String> fullStringTagsName = new RepositoryUtil().listRemoteTagsName();
        List<String> tagsName = new ArrayList<String>();
        for (String fullName : fullStringTagsName) {
            int lastIndex = fullName.lastIndexOf("/");
            String name = fullName.substring(lastIndex + 1);
            tagsName.add(name);
        }

        return tagsName;
    }

    public List<String> listBranchName(String localGitRepo) {
        List<String> fullStringBranchesName = new RepositoryUtil().listRemoteBranchesName(localGitRepo);
        List<String> branchesName = new ArrayList<String>();
        for (String fullName : fullStringBranchesName) {
            int lastIndex = fullName.lastIndexOf("/");
            String name = fullName.substring(lastIndex + 1);
            branchesName.add(name);
        }

        return branchesName;
    }

    /*Delete local branch if it is already pushed to remote*/
    public void deletePushedBranch() {
        String localFolder = PropertyCache.get("local_folder");
        String branchFolder = localFolder + "/branches";

        List<String> remoteBranchesName = listBranchName(PropertyCache.get("local_git"));
        List<String> localBranchesName = DirectoryUtil.loadSubFolder(branchFolder);

        List<String> removedBranchesName = new ArrayList<String>(localBranchesName);
        remoteBranchesName.retainAll(remoteBranchesName);

        for (String branch : remoteBranchesName) {
            File file = new File(branchFolder + "/" + branch);
            if (file.exists() && file.isDirectory()) {
                try {
                    logger.info("Delete local branch " + branch);
                    FileUtils.deleteDirectory(file);
                } catch (IOException e) {
                    logger.error("Error delete local branch " + branch, e);
                }
            }
        }
    }

    /*Delete local tag if it is already pushed to remote*/
    public void deletePushedTag() {
        String localFolder = PropertyCache.get("local_folder");
        String tagFolder = localFolder + "/tags";

        List<String> remoteTagsName = listTagName();
        List<String> localTagsName = DirectoryUtil.loadSubFolder(tagFolder);

        List<String> removedTagsName = new ArrayList<String>(localTagsName);
        removedTagsName.retainAll(remoteTagsName);

        for (String tag : remoteTagsName) {
            File file = new File(tagFolder + "/" + tag);
            if (file.exists() && file.isDirectory()) {
                try {
                    logger.info("Delete local tag " + tag);
                    FileUtils.deleteDirectory(file);
                } catch (IOException e) {
                    logger.error("Error delete local tag " + tag, e);
                }
            }
        }
    }
}
