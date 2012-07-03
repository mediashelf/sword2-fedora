
package org.swordapp.server;

import static com.jayway.restassured.RestAssured.basic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.BeforeClass;

import com.jayway.restassured.RestAssured;

public abstract class SwordIT {

    @BeforeClass
    public static void setUpRestAssured() {
        Properties props;
        Properties defaults = new Properties();
        props = new Properties(defaults);

        InputStream stream =
                Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream("test.properties");
        try {
            try {
                props.load(stream);
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RestAssured.baseURI = props.getProperty("sword2.baseUrl");
        RestAssured.authentication = basic("fedoraAdmin", "fedoraAdmin");
    }
}
