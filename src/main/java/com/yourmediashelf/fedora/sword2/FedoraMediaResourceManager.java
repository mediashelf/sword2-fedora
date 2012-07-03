
package com.yourmediashelf.fedora.sword2;

import static com.yourmediashelf.fedora.client.FedoraClient.getObjectXML;
import static com.yourmediashelf.fedora.client.FedoraClient.modifyDatastream;
import static com.yourmediashelf.fedora.client.FedoraClient.purgeDatastream;

import java.net.MalformedURLException;
import java.util.Map;

import org.apache.abdera.i18n.iri.IRI;
import org.swordapp.server.AuthCredentials;
import org.swordapp.server.Deposit;
import org.swordapp.server.DepositReceipt;
import org.swordapp.server.MediaResource;
import org.swordapp.server.MediaResourceManager;
import org.swordapp.server.SwordAuthException;
import org.swordapp.server.SwordConfiguration;
import org.swordapp.server.SwordError;
import org.swordapp.server.SwordServerException;

import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.FedoraCredentials;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.ModifyDatastreamResponse;

public class FedoraMediaResourceManager implements MediaResourceManager {

    public MediaResource getMediaResourceRepresentation(String uri,
            Map<String, String> accept, AuthCredentials auth,
            SwordConfiguration config) throws SwordError, SwordServerException,
            SwordAuthException {
        FedoraClient fedora = getFedoraClient(auth);
        FedoraResourceIdentifier fri = new FedoraResourceIdentifier(uri);

        //        FedoraResponse response;
        //        try {
        //            response =
        //                    getDatastreamDissemination(fri.pid, fri.dsid)
        //                            .execute(fedora);
        //        } catch (FedoraClientException e) {
        //            throw new SwordServerException(e.getMessage(), e);
        //        }

        FedoraResponse response;
        try {
            response = getObjectXML(fri.pid).execute(fedora);
        } catch (FedoraClientException e) {
            throw new SwordServerException(e.getMessage(), e);
        }

        return new MediaResource(response.getEntityInputStream(), response
                .getType(), null, true);
    }

    public DepositReceipt replaceMediaResource(String uri, Deposit deposit,
            AuthCredentials auth, SwordConfiguration config) throws SwordError,
            SwordServerException, SwordAuthException {
        FedoraClient fedora = getFedoraClient(auth);
        FedoraResourceIdentifier fri = new FedoraResourceIdentifier(uri);

        DepositReceipt receipt = new DepositReceipt();

        ModifyDatastreamResponse response;
        try {
            response =
                    modifyDatastream(fri.pid, fri.dsid).content(
                            deposit.getFile()).execute(fedora);

            //FIXME
            receipt.setContent(null, response.getType());
            receipt.setLastModified(response.getLastModifiedDate());
        } catch (FedoraClientException e) {
            throw new SwordServerException(e.getMessage(), e);
        }

        return receipt;
    }

    public void deleteMediaResource(String uri, AuthCredentials auth,
            SwordConfiguration config) throws SwordError, SwordServerException,
            SwordAuthException {
        FedoraClient fedora = getFedoraClient(auth);
        FedoraResourceIdentifier fri = new FedoraResourceIdentifier(uri);
        try {
            purgeDatastream(fri.pid, fri.dsid).execute(fedora);
        } catch (FedoraClientException e) {
            throw new SwordServerException(e.getMessage(), e);
        }
    }

    public DepositReceipt addResource(String uri, Deposit deposit,
            AuthCredentials auth, SwordConfiguration config) throws SwordError,
            SwordServerException, SwordAuthException {
        DepositReceipt receipt = new DepositReceipt();
        //TODO
        receipt.setLocation(new IRI("http://example.com"));
        return receipt;
    }

    protected static FedoraClient getFedoraClient(AuthCredentials auth)
            throws SwordServerException {
        FedoraCredentials credentials;
        try {
            credentials =
                    new FedoraCredentials(FedoraConfiguration.fedoraBaseUrl,
                            auth.getUsername(), auth.getPassword());
        } catch (MalformedURLException e) {
            throw new SwordServerException(e.getMessage(), e);
        }
        return new FedoraClient(credentials);
    }
}
