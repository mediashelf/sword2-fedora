package com.yourmediashelf.fedora.sword2;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.swordapp.server.DepositReceipt;

public class OaiDc {

    private final String oaidc;

    public OaiDc(Map<String, List<String>> dcterms, DepositReceipt receipt) {
        StringBuffer sb = new StringBuffer();
        sb.append("<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\"\n");
		sb.append("  xmlns:dc=\"http://purl.org/dc/elements/1.1/\"");
        sb.append("  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        sb.append("  xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">");

        for (Entry<String, List<String>> entry : dcterms.entrySet()) {
            for (String value : entry.getValue()) {
                sb.append("  <dc:" + entry.getKey() + ">");
                sb.append(value);
                sb.append("</dc:" + entry.getKey() + ">");
                if (receipt != null) {
                    receipt.addDublinCore(entry.getKey(), value);
                }
            }
        }
        sb.append("</oai_dc:dc>");
        oaidc = sb.toString();
    }

    public String toString() {
        return oaidc;
    }
}
