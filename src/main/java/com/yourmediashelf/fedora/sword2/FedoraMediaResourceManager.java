
package com.yourmediashelf.fedora.sword2;

import static com.yourmediashelf.fedora.client.FedoraClient.export;
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
        String packaging = getPackaging(accept);
        
        FedoraClient fedora = getFedoraClient(auth);
        FedoraResourceIdentifier fri = new FedoraResourceIdentifier(uri);
        FedoraResponse response;
        try {
            response =
                    export(fri.pid).format(packaging).context("archive")
                    .execute(fedora);
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
        String filename = deposit.getFilename();
        // FIXME actually make sure the filename is a valid dsid 
        // (e.g. XML NC Name, not greater than 64 chars)
        String dsid = filename;

        DepositReceipt receipt =
                FedoraContainerManager.getDepositReceipt(fri.pid, auth);

        ModifyDatastreamResponse response;
        try {
            response =
                    modifyDatastream(fri.pid, dsid).mimeType(
                            deposit.getMimeType()).content(
                            deposit.getInputStream()).execute(fedora);

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

    private String getPackaging(Map<String, String> accepts) throws SwordError {
        for (String header : accepts.keySet()) {
            if (header.equalsIgnoreCase("Accept-Packaging")) {
                String candidate = accepts.get(header);
                for (String packaging : FedoraConfiguration.acceptPackaging) {
                    if (packaging.equalsIgnoreCase(candidate)) {
                        return candidate;
                    }
                }
                throw new SwordError(
                        "http://purl.net/org/sword/terms/ErrorContent", 406);
            }
        }
        // default?
        return "info:fedora/fedora-system:FOXML-1.1";
    }
}
