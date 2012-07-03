package com.yourmediashelf.fedora.sword2;

import org.swordapp.server.AuthCredentials;
import org.swordapp.server.ServiceDocument;
import org.swordapp.server.ServiceDocumentManager;
import org.swordapp.server.SwordAuthException;
import org.swordapp.server.SwordCollection;
import org.swordapp.server.SwordConfiguration;
import org.swordapp.server.SwordError;
import org.swordapp.server.SwordServerException;
import org.swordapp.server.SwordWorkspace;


public class FedoraServiceDocumentManager implements ServiceDocumentManager {

    private String sdUri;

    public ServiceDocument getServiceDocument(String sdUri,
            AuthCredentials auth, SwordConfiguration config) throws SwordError,
            SwordServerException, SwordAuthException {
        this.sdUri = sdUri;
        SwordWorkspace ws = new SwordWorkspace();
        ws.setTitle("Fedora Workspace");
        ws.addCollection(getDefaultCollection());
        ServiceDocument sd = new ServiceDocument();
        sd.addWorkspace(ws);
        return sd;
    }

    private SwordCollection getDefaultCollection() {
        SwordCollection dc = new SwordCollection();
        dc.setHref(getCollectionHref(FedoraConfiguration.rootCollection));
        dc.setTitle("Root Fedora Collection");
        String accepts = "*/*";
        dc.setAccept(accepts);
        dc.setMultipartAccept(accepts);
        dc.setAbstract("The default collection of all Fedora objects");
        for (String packaging : FedoraConfiguration.acceptPackaging) {
            dc.addAcceptPackaging(packaging);
        }
        return dc;
    }

    private String getCollectionHref(String identifier) {
        // for now, hack apart sdUri with some assumptions about url mappings
        return sdUri.substring(0, sdUri.indexOf("servicedocument")) +
                "collection/" + identifier;
    }

}
