package com.yourmediashelf.fedora.sword2;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.swordapp.server.SwordServerException;

public class FedoraResourceIdentifier {
    private static Pattern pattern = Pattern
            .compile(".*/([A-Za-z0-9-\\.]+:.+)");

    public String pid;

    public String dsid;

    public FedoraResourceIdentifier(String pid, String dsid) {
        this.pid = pid;
        this.dsid = dsid;
    }

    public FedoraResourceIdentifier(String uri)
            throws SwordServerException {
        String path;
        try {
            URI tmp = new URI(uri);
            path = tmp.getPath();
        } catch (URISyntaxException e) {
            throw new SwordServerException(
                    "Not a valid media resource uri: " + uri);
        }

        Matcher m = pattern.matcher(path);
        if (m.matches()) {
            String[] parts = m.group(1).split("/", 2);
            pid = parts[0];
            if (parts.length == 2) {
                dsid = parts[1];
            }
        } else {
            throw new SwordServerException("Not a valid identifier: " + uri);
        }
    }
}
