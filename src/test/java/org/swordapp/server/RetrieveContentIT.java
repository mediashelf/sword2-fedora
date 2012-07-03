
package org.swordapp.server;

import static com.jayway.restassured.RestAssured.given;

import org.junit.Test;

public class RetrieveContentIT extends SwordIT {

    /**
     * <p>Validates the following requirement of the SWORD 2.0 spec (6.4):
     * 
     * <ul>
     *   <li>If the Accept-Packaging header is supplied but contains a IRI (or 
     *   other allowed value) for a format that the server does not support, the 
     *   server MUST respond with 406 Not Acceptable
     * </ul>
     * 
     * @throws Exception
     */
    @Test
    public void testUnsupportedPackaging() throws Exception {
        given().header("Accept-Packaging", "foo:bar").expect().statusCode(406)
                .when().get("/edit-media/bogus:identifier");
    }

    @Test
    public void testDefaultPackaging() throws Exception {
        given().auth().preemptive().basic("fedoraAdmin", "fedoraAdmin")
                .expect().statusCode(200).when().get(
                        "/edit-media/fedora-system:FedoraObject-3.0");
    }

    @Test
    public void testAtomZipPackaging() throws Exception {
        given().header("Accept-Packaging",
                "info:fedora/fedora-system:ATOMZip-1.1").auth().preemptive()
                .basic("fedoraAdmin", "fedoraAdmin").expect().statusCode(200)
                .contentType("application/zip").when().get(
                        "/edit-media/fedora-system:FedoraObject-3.0");
    }

}
