package net.san.util;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * Created by AnNN1 on 5/26/2017.
 */
public class CredentialUtil {

    public static CredentialsProvider getCredential(String username, String password) {
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(username, password);
        return credentialsProvider;
    }
}
