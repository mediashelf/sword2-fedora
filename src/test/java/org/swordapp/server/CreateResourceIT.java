
package org.swordapp.server;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.xml.namespace.QName;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.Entry;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Response;
import com.yourmediashelf.fedora.sword2.FedoraConfiguration;

public class CreateResourceIT extends SwordIT {

    /**
     * <p>Test creation of binary resource and validate the server response
     * (Deposit Receipt) for the following conditions defined in the rfc:
     * 
     * <ul>
     *   <li>It MUST contain a Media Entry IRI (Edit-IRI), defined by atom:link@rel="edit"
     *   <li>It MUST contain a Media Resource IRI (EM-IRI), defined by atom:link@rel="edit-media"
     *   <li>It MUST contain a SWORD Edit IRI (SE-IRI), defined by atom:link@rel="http://purl.org/net/sword/terms/add"
     *   <li>It MUST contain a single sword:treatment element
     * </ul>
     */
    @Test
    public void testCreateBinaryResource() throws Exception {
        Response response =
                given().header("Content-Disposition",
                        "attachment; filename=foo.txt").body("<foo>bar</foo>")
                        .post("/collection/" +
                                FedoraConfiguration.rootCollection);
        assertEquals(201, response.statusCode());
        assertTrue(response.getHeader("Location").startsWith(
                RestAssured.baseURI + "edit/"));

        XmlPath receipt = new XmlPath(response.prettyPrint()).setRoot("entry");

        verifyBinaryReceipt(receipt);
    }

    @Test
    public void testCreateMultipartResource() throws Exception {
        Abdera abdera = new Abdera();
        Entry e = abdera.newEntry();
        e.setTitle("My title");
        e.setId("foo:bar");
        e.setUpdated(new Date());
        e.addAuthor("John Doe");
        e.setSummary("A summary");
        addDcDescriptionElement(e);

        MimeMultipart mp = new MimeMultipart("related");
        MimeBodyPart entry = new MimeBodyPart();
        entry.setContent(e.toString(), "application/atom+xml");
        entry.setHeader("Content-Disposition", "attachment; name=atom");
        entry.setHeader("Content-Type", "application/atom+xml; charset=utf-8");

        MimeBodyPart media = new MimeBodyPart();
        media.setText("<foo>bar</foo>");
        media.setHeader("Content-Disposition",
                "attachment; name=payload; filename=foo.txt");
        media.setHeader("Content-Type", "text/plain");

        mp.addBodyPart(entry);
        mp.addBodyPart(media);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        mp.writeTo(os);
        os.flush();
        String mpString = os.toString("utf-8");
        os.close();

        Response response =
                given().header("Content-Type", mp.getContentType()).body(
                        mpString).post(
                        "/collection/" + FedoraConfiguration.rootCollection);
        assertEquals(201, response.statusCode());

        XmlPath receipt = new XmlPath(response.prettyPrint()).setRoot("entry");
        verifyBinaryReceipt(receipt);

        verifyEntryReceipt(receipt);
    }

    @Test
    public void testCreateEntryResource() {
        Abdera abdera = new Abdera();
        Entry entry = abdera.newEntry();
        entry.setTitle("My Title");
        entry.addAuthor("Jim Bob");
        entry.setId("blah:blah123");
        addDcDescriptionElement(entry);

        given().header("Content-Type", "application/atom+xml;type=entry").body(
                entry.toString()).expect().statusCode(201).log().all().when()
                .post("/collection/" + FedoraConfiguration.rootCollection);
    }

    private void verifyBinaryReceipt(XmlPath receipt) throws Exception {
        int editLinks =
                receipt.get("link.findAll { it.@rel == 'edit' }.size()");
        assertThat(editLinks, equalTo(1));

        int editMediaLinks =
                receipt.get("link.findAll { it.@rel == 'edit-media' }.size()");
        assertThat(editMediaLinks, equalTo(1));

        int swordEditLinks =
                receipt.get("link.findAll { it.@rel == 'http://purl.org/net/sword/terms/add' }.size()");
        assertThat(swordEditLinks, equalTo(1));

        int treatment = receipt.get("treatment.size()");
        assertThat(treatment, equalTo(1));
    }

    /**
     * Validates a deposit receipt for a dc:description element
     * 
     * @param receipt
     * @throws Exception
     */
    private void verifyEntryReceipt(XmlPath receipt) throws Exception {
        int dcDescription = receipt.get("description.size()");
        assertThat(dcDescription, greaterThanOrEqualTo(1));
    }

    private void addDcDescriptionElement(Entry entry) {
        QName dcDescQname =
                new QName("http://purl.org/dc/terms/", "description", "dcterms");
        Element description = entry.addExtension(dcDescQname);
        description.setText("My description");
    }
}
