package net.san.repos;

import net.san.util.CredentialUtil;
import net.san.util.PropertyCache;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.TagCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by AnNN1 on 5/26/2017.
 */
public class RepositoryUtil {
    Logger logger = Logger.getLogger(RepositoryUtil.class);

    private Repository localRepo;
    private Git git;
    private String location;

    public RepositoryUtil(String location) {
        this.location = location;
    }

    public void init() {
        logger.info("Init local repository");
        try {
            localRepo = new FileRepository(location + "\\.git");
            git = new Git(localRepo);
            localRepo.create();
        } catch (Exception e) {
            logger.error("Exception when git init at " + location, e);
        }
    }

    public void add() {
        logger.info("Add files to staging state");
        try {
            git.add().addFilepattern(".").call();
        } catch (Exception e) {
            logger.error("Exception when add files to staging", e);
        }
    }

    public void commit() {
        logger.info("Commit files to repos");
        try {
            git.commit().setMessage("init repos").call();
        } catch (GitAPIException e) {
            logger.error("Exception when commit ", e);
        }
    }

    public void addRemote() {
        logger.info("Add remote to local repos");
        try {
            StoredConfig config = git.getRepository().getConfig();
            config.setString("remote", "origin", "url", PropertyCache.get("remote_repos"));
            config.save();
        } catch (IOException e) {
            logger.error("AddRemote exception ", e);
        }
    }

    public void createNewBranch(String branchName) {
        logger.info("Create branch with name : " + branchName);
        try {
            CreateBranchCommand command = git.branchCreate();
            command.setName(branchName);
            command.call();
        } catch (GitAPIException e) {
            logger.error("createNewBranch Exception ", e);
        }
    }

    public void checkout(String branchName) {
        logger.info("Checkout branch " + branchName);
        try {
            CheckoutCommand command = git.checkout();
            command.setName(branchName);
            command.call();
        } catch (GitAPIException e) {
            logger.error("Checkout Exception ", e);
        }
    }

    public void push(String localBranch, String remoteBranch) {
        logger.info("Pushing....");
        try {
            PushCommand pushCommand = git.push();
            pushCommand.setCredentialsProvider(CredentialUtil.getCredential(
                    PropertyCache.get("username"),
                    PropertyCache.get("password")));
            pushCommand.setRemote("origin");
            pushCommand.setRefSpecs(new RefSpec(localBranch + ":" + remoteBranch));
            Iterable<PushResult> pushResults = pushCommand.call();
            Iterator<PushResult> resultIterator = pushResults.iterator();

            while (resultIterator.hasNext()) {
                PushResult result = resultIterator.next();
                logger.info(result.getMessages());
            }

        } catch (GitAPIException e) {
            logger.error("Push Exception ", e);
        }

    }

    public void createNewTag(String folderName) {
        logger.info("Create tag with name : " + folderName);
        try {
            TagCommand command = git.tag();
            command.setName(folderName);
            command.setMessage("Create tag " + folderName);
            command.call();
        } catch (GitAPIException e) {
            logger.error("createNewBranch Exception ", e);
        }
    }

    public void pushTag() {
        logger.info("Pushing Tag....");
        try {
            PushCommand pushCommand = git.push();
            pushCommand.setCredentialsProvider(CredentialUtil.getCredential(
                    PropertyCache.get("username"),
                    PropertyCache.get("password")));
            pushCommand.setPushTags();
            Iterable<PushResult> pushResults = pushCommand.call();
            Iterator<PushResult> resultIterator = pushResults.iterator();

            while (resultIterator.hasNext()) {
                PushResult result = resultIterator.next();
                logger.info(result.getMessages());
            }

        } catch (GitAPIException e) {
            logger.error("Push Exception ", e);
        }

    }
}
