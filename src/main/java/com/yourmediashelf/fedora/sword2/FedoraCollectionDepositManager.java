
package com.yourmediashelf.fedora.sword2;

import static com.yourmediashelf.fedora.client.FedoraClient.addDatastream;
import static com.yourmediashelf.fedora.client.FedoraClient.ingest;
import static com.yourmediashelf.fedora.client.FedoraClient.modifyDatastream;

import java.util.List;
import java.util.Map;

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
        FedoraClient fedora = FedoraMediaResourceManager.getFedoraClient(auth);

        String packaging = getPackaging(deposit);

        String pid;
        try {
            if (packaging != null && !packaging.isEmpty()) {
                pid =
                        ingest().content(deposit.getInputStream()).format(
                                packaging).execute(fedora).getPid();
            } else {
                pid = ingest().execute(fedora).getPid();
            }
        } catch (FedoraClientException e) {
            throw new SwordServerException(e.getMessage(), e);
        }

        if (deposit.isBinaryOnly() || deposit.isMultipart()) {
            String filename = deposit.getFilename();
            // FIXME actually make sure the filename is a valid dsid 
            // (e.g. XML NC Name, not greater than 64 chars)
            String dsid = filename;

            try {
                addDatastream(pid, dsid).content(deposit.getInputStream())
                        .mimeType(deposit.getMimeType()).dsLabel(
                                deposit.getFilename()).execute(fedora);
            } catch (FedoraClientException e) {
                throw new SwordServerException(e.getMessage(), e);
            }
        }

        DepositReceipt receipt =
                FedoraContainerManager.getDepositReceipt(pid, auth);

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

    private String getPackaging(Deposit deposit) {
        String candidate = deposit.getPackaging();
        for (String s : FedoraConfiguration.acceptPackaging) {
            if (s.equalsIgnoreCase(candidate)) {
                return candidate;
            }
        }
        return null;
    }
}
