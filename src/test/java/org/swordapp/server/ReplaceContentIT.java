
package org.swordapp.server;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jayway.restassured.response.Response;
import com.yourmediashelf.fedora.sword2.FedoraConfiguration;
import com.yourmediashelf.fedora.sword2.FedoraResourceIdentifier;

public class ReplaceContentIT extends SwordIT {

    @Test
    public void testReplaceContent() throws Exception {
        // First, create a resource
        Response response =
                given().header("Content-Disposition",
                        "attachment; filename=foo.txt").body("<foo>bar</foo>")
                        .post("/collection/" +
                                FedoraConfiguration.rootCollection);
        assertEquals(201, response.statusCode());
        FedoraResourceIdentifier fri =
                new FedoraResourceIdentifier(response.getHeader("Location"));

        System.out.println("* pid: " + fri.pid);
        
        given().auth().preemptive().basic("fedoraAdmin", "fedoraAdmin").header(
                "Content-Disposition", "attachment; filename=foo.txt").body(
                "<bar>baz</bar>").expect().statusCode(204).when().put(
                "/edit-media/" + fri.pid);
    }
}
