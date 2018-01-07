package com.ka.cert.transparency.service.test.integration;

import org.certificatetransparency.ctlog.ParsedLogEntry;
import org.certificatetransparency.ctlog.SignedTreeHead;
import org.certificatetransparency.ctlog.comm.HttpLogClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.List;

/**
 * Description: Certificate service integration tests
 * Project: cert-transparency-service
 * Package: org.cert.transparency.service.test.unit
 * Author: kakyurek
 * Date: 2018.01.06
 */
@Category(IntegrationTest.class)
public class CertEndpointTest {

    private static final HttpLogClient client = new HttpLogClient("http://sabre.ct.comodo.com/ct/v1/");

    @Test
    public void getLogSTHTest() {
        SignedTreeHead head = client.getLogSTH();
        Assert.assertNotNull(head);
    }

    @Test
    public void getEntriesTest() {
        List<ParsedLogEntry> list = client.getLogEntries(0, 10);
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals(11, list.size());
    }

    @Test
    public void getEntriesTimestampTest() throws InterruptedException {
        List<ParsedLogEntry> list = client.getLogEntries(0, 0);
        Assert.assertFalse(list.isEmpty());
        long previousTimestamp = list.get(0).getMerkleTreeLeaf().timestampedEntry.timestamp;
        Thread.sleep(5000);
        list = client.getLogEntries(0, 0);
        Assert.assertFalse(list.isEmpty());
        long currentTimestamp = list.get(0).getMerkleTreeLeaf().timestampedEntry.timestamp;
        Assert.assertEquals(previousTimestamp, currentTimestamp);
    }

}
