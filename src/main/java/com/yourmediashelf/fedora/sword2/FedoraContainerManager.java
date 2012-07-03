package com.yourmediashelf.fedora.sword2;

import java.util.Map;

import org.apache.abdera.i18n.iri.IRI;
import org.swordapp.server.AuthCredentials;
import org.swordapp.server.ContainerManager;
import org.swordapp.server.Deposit;
import org.swordapp.server.DepositReceipt;
import org.swordapp.server.SwordAuthException;
import org.swordapp.server.SwordConfiguration;
import org.swordapp.server.SwordError;
import org.swordapp.server.SwordServerException;


public class FedoraContainerManager implements ContainerManager {

    public DepositReceipt getEntry(String editIRI, Map<String, String> accept,
            AuthCredentials auth, SwordConfiguration config)
            throws SwordServerException, SwordError, SwordAuthException {
        FedoraResourceIdentifier fri = new FedoraResourceIdentifier(editIRI);

        DepositReceipt receipt = new DepositReceipt();
        IRI editIri = new IRI(editIRI);
        receipt.setLocation(editIri);
        receipt.setEditIRI(editIri);

        IRI editMediaIri =
                new IRI(FedoraConfiguration.editMediaBaseUrl + fri.pid + "/" +
                        fri.dsid);
        receipt.setEditMediaIRI(editMediaIri);

        return receipt;
    }

    public DepositReceipt replaceMetadata(String editIRI, Deposit deposit,
            AuthCredentials auth, SwordConfiguration config) throws SwordError,
            SwordServerException, SwordAuthException {
        //FIXME
        throw new UnsupportedOperationException();
    }

    public DepositReceipt replaceMetadataAndMediaResource(String editIRI,
            Deposit deposit, AuthCredentials auth, SwordConfiguration config)
            throws SwordError, SwordServerException, SwordAuthException {
        //FIXME
        throw new UnsupportedOperationException();
    }

    public DepositReceipt addMetadataAndResources(String editIRI,
            Deposit deposit, AuthCredentials auth, SwordConfiguration config)
            throws SwordError, SwordServerException, SwordAuthException {
        //FIXME
        throw new UnsupportedOperationException();
    }

    public DepositReceipt addMetadata(String editIRI, Deposit deposit,
            AuthCredentials auth, SwordConfiguration config) throws SwordError,
            SwordServerException, SwordAuthException {
        //FIXME
        throw new UnsupportedOperationException();
    }

    public DepositReceipt addResources(String editIRI, Deposit deposit,
            AuthCredentials auth, SwordConfiguration config) throws SwordError,
            SwordServerException, SwordAuthException {
        //FIXME
        throw new UnsupportedOperationException();
    }

    public void deleteContainer(String editIRI, AuthCredentials auth,
            SwordConfiguration config) throws SwordError, SwordServerException,
            SwordAuthException {
        //FIXME
        throw new UnsupportedOperationException();
    }

    public DepositReceipt useHeaders(String editIRI, Deposit deposit,
            AuthCredentials auth, SwordConfiguration config) throws SwordError,
            SwordServerException, SwordAuthException {
        //FIXME
        throw new UnsupportedOperationException();
    }

    public boolean isStatementRequest(String editIRI,
            Map<String, String> accept, AuthCredentials auth,
            SwordConfiguration config) throws SwordError, SwordServerException,
            SwordAuthException {
        //TODO check accept Map for content-type that we support for statements
        return false;
    }

}
