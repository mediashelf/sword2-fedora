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
        return getDepositReceipt(fri.pid, auth);
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

    protected static DepositReceipt
            getDepositReceipt(String pid, AuthCredentials auth)
                    throws SwordServerException {
        IRI editIri = new IRI(FedoraConfiguration.editBaseUrl + pid);
        IRI editMediaIri = new IRI(FedoraConfiguration.editMediaBaseUrl + pid);

        DepositReceipt receipt = new DepositReceipt();
        receipt.setLocation(editIri);
        receipt.setEditIRI(editIri);
        receipt.setEditMediaIRI(editMediaIri);

        // parse DC datastream to fill out receipt metadata
        // FedoraClient fedora = FedoraMediaResourceManager.getFedoraClient(auth);
        //        try {
        //            AddDatastreamResponse response =
        //                    addDatastream(pid, "DC").execute(fedora);
        //
        //        } catch (FedoraClientException e) {
        //            throw new SwordServerException(e.getMessage(), e);
        //        }

        for (String packaging : FedoraConfiguration.acceptPackaging) {
            receipt.addPackaging(packaging);
        }

        return receipt;
    }
}
