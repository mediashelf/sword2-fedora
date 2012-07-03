package com.yourmediashelf.fedora.sword2;

import org.apache.abdera.Abdera;
import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.model.Feed;
import org.swordapp.server.AuthCredentials;
import org.swordapp.server.CollectionListManager;
import org.swordapp.server.SwordAuthException;
import org.swordapp.server.SwordConfiguration;
import org.swordapp.server.SwordError;
import org.swordapp.server.SwordServerException;


public class FedoraCollectionListManager implements CollectionListManager {

    public Feed listCollectionContents(IRI collectionIRI, AuthCredentials auth,
            SwordConfiguration config) throws SwordServerException,
            SwordAuthException, SwordError {
        //TODO
        // Can't really implement this without relying on ResourceIndex 
        // sparql/itql queries as FieldSearch doesn't support sorting by date
        Abdera abdera = new Abdera();
        Feed feed = abdera.newFeed();
        return feed;
    }

}
