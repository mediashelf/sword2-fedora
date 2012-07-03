package com.yourmediashelf.fedora.sword2;

import java.util.Map;

import org.swordapp.server.AuthCredentials;
import org.swordapp.server.Statement;
import org.swordapp.server.StatementManager;
import org.swordapp.server.SwordAuthException;
import org.swordapp.server.SwordConfiguration;
import org.swordapp.server.SwordError;
import org.swordapp.server.SwordServerException;


public class FedoraStatementManager implements StatementManager {

    public Statement getStatement(String iri, Map<String, String> accept,
            AuthCredentials auth, SwordConfiguration config)
            throws SwordServerException, SwordError, SwordAuthException {
        // TODO Auto-generated method stub
        return null;
    }

}
