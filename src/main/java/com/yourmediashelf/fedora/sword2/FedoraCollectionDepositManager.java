
package com.yourmediashelf.fedora.sword2;

import static com.yourmediashelf.fedora.client.FedoraClient.addDatastream;
import static com.yourmediashelf.fedora.client.FedoraClient.ingest;
import static com.yourmediashelf.fedora.client.FedoraClient.modifyDatastream;

import java.util.List;
import java.util.Map;

import org.apache.abdera.i18n.iri.IRI;
import org.swordapp.server.AuthCredentials;
import org.swordapp.server.CollectionDepositManager;
import org.swordapp.server.Deposit;
import org.swordapp.server.DepositReceipt;
import org.swordapp.server.SwordAuthException;
import org.swordapp.server.SwordConfiguration;
import org.swordapp.server.SwordError;
import org.swordapp.server.SwordServerException;

import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;

public class FedoraCollectionDepositManager implements CollectionDepositManager {

    public DepositReceipt createNew(String collectionURI, Deposit deposit,
            AuthCredentials auth, SwordConfiguration config) throws SwordError,
            SwordServerException, SwordAuthException {
        DepositReceipt receipt = new DepositReceipt();
        FedoraClient fedora = FedoraMediaResourceManager.getFedoraClient(auth);

        String pid;
        try {
            pid = ingest().execute(fedora).getPid();
            IRI editIri = new IRI(FedoraConfiguration.editBaseUrl + pid);
            IRI editMediaIri =
                    new IRI(FedoraConfiguration.editMediaBaseUrl + pid);
            receipt.setLocation(editIri);
            receipt.setEditIRI(editIri);
            receipt.setEditMediaIRI(editMediaIri);
        } catch (FedoraClientException e) {
            throw new SwordServerException(e.getMessage(), e);
        }

        if (deposit.isBinaryOnly() || deposit.isMultipart()) {
            String filename = deposit.getFilename();
            // FIXME actually make sure the filename is a valid dsid 
            // (e.g. XML NC Name, not greater than 64 chars)
            String dsid = filename;

            try {
                pid = ingest().execute(fedora).getPid();
                addDatastream(pid, dsid).content(deposit.getInputStream())
                        .mimeType(deposit.getMimeType()).dsLabel(
                                deposit.getFilename()).execute(fedora);
            } catch (FedoraClientException e) {
                throw new SwordServerException(e.getMessage(), e);
            }
        }

        if (deposit.isEntryOnly() || deposit.isMultipart()) {
            Map<String, List<String>> dc =
                    deposit.getSwordEntry().getDublinCore();
            if (!dc.isEmpty()) {
                try {
                    modifyDatastream(pid, "DC").content(
                            new OaiDc(dc, receipt).toString()).execute(fedora);
                } catch (FedoraClientException e) {
                    throw new SwordServerException(e.getMessage(), e);
                }
            }
        }
        return receipt;
    }
}
