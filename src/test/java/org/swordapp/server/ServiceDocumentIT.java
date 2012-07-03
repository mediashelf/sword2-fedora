
package org.swordapp.server;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.path.xml.element.Node;

public class ServiceDocumentIT extends SwordIT {

    /**
     * <p>Test for the following condition defined in the rfc:
     * 
     * <ul>
     *   <li>The SWORD server MUST specify the sword:version element with a value of 2.0 
     * </ul>
     */
    @Test
    public void testServiceDocumentVersion() {
        expect().statusCode(200).log().all().contentType(
                "application/atomserv+xml").body("service.version",
                equalTo("2.0")).when().get("/servicedocument");
    }

    /**
     * <p>If the service document contains any collection elements, test for 
     * the following conditions defined in the rfc:
     * 
     * <ul>
     *   <li>[It] MUST specify the app:accept element
     *   <li>It MUST also specify an app:accept element with an alternate 
     *   attribute set to multipart-related
     * </ul>
     */
    @Test
    public void testServiceDocumentCollection() {
        String sd = get("/servicedocument").asString();

        List<Node> collections =
                new XmlPath(sd).getList("service.workspace.collection",
                        Node.class);
        if (collections.size() > 0) {
            for (Node collection : collections) {
                boolean acceptsMultipart = false;
                assertNotNull(collection.getAttribute("href"));
                List<Node> accepts = collection.getNodes("accept");
                // There should be at least 2 accept elements, one of which
                // has an "alternate" attribute set to "multipart-related".
                assertThat(accepts.size(), greaterThanOrEqualTo(2));
                for (Node accept : accepts) {
                    String alternate = accept.getAttribute("alternate");
                    if (alternate != null &&
                            alternate.equalsIgnoreCase("multipart-related")) {
                        acceptsMultipart = true;
                    }
                }
                assertTrue(acceptsMultipart);
            }
        }
    }

}
