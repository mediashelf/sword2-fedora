
package com.yourmediashelf.fedora.sword2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FedoraConfiguration {

    private static Properties props;

    /**
     * Base URL for Fedora (e.g. http://localhost:8080/fedora/)
     */
    public static String fedoraBaseUrl;

    /**
     * Base URL for SWORD2 (e.g. http://localhost:9090/sword2/)
     */
    public static String swordBaseUrl;

    public static String editBaseUrl;

    public static String editMediaBaseUrl;

    public static String statementBaseUrl;

    static {
        Properties defaults = new Properties();
        defaults.put("fedora.baseUrl", "http://localhost/fedora/");
        defaults.put("sword2.baseUrl", "http://localhost/sword2/");
        defaults.put("sword2.editMediaPath", "edit-media/");
        defaults.put("sword2.editPath", "edit/");
        props = new Properties(defaults);

        InputStream stream =
                Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream("/sword2fedora.properties");
        try {
            try {
                props.load(stream);

                fedoraBaseUrl = props.getProperty("fedora.baseUrl");

                swordBaseUrl = props.getProperty("sword2.baseUrl");

                editBaseUrl =
                        swordBaseUrl + props.getProperty("sword2.editPath");

                editMediaBaseUrl =
                        swordBaseUrl +
                                props.getProperty("sword2.editMediaPath");

            } finally {
                stream.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
