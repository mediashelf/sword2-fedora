
package org.swordapp.server;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.hasXPath;

import org.junit.Test;

public class ListCollectionMembersIT extends SwordIT {

    @Test
    public void testListCollectionMembers() {
        expect().statusCode(200)
                .contentType("application/atom+xml").body(hasXPath("/feed"))
                .when().get("/collection");

    }

}
