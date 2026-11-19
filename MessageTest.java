package com.mycompany.loginregister;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testCheckUserName() {
        ProgApp login = new ProgApp();
        assertTrue(login.checkUserName("kyl_1"));
        assertFalse(login.checkUserName("kyle!!!!!"));
    }

    @Test
    public void testCheckPasswordComplexity() {
        ProgApp login = new ProgApp();
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99"));
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void testCheckRecipientCell() {
        Messages m = new Messages();
        assertTrue(m.isValidCell("+27838884567"));
        assertFalse(m.isValidCell("0838884567"));
    }

    @Test
    public void testCreateMessageHash() {
        Messages m = new Messages();
        assertEquals("00:0:HITONIGHT", m.createMessageHash(0, 0, "Hi, I will be home tonight"));
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        Messages m = new Messages();
        m.populateTestData();
        assertArrayEquals(new String[]{"Did you get the cake?", "It is dinner time!"}, m.getSentMessagesArray());
    }

    @Test
    public void testLongestMessage() {
        Messages m = new Messages();
        m.populateTestData();
        assertEquals("Where are you? You are late! I have asked you to be on time.", m.getLongestStoredMessage());
    }

    @Test
    public void testSearchByMessageID() {
        Messages m = new Messages();
        m.populateTestData();
        assertTrue(m.searchByMessageID("0838884567").contains("It is dinner time!"));
    }

    @Test
    public void testSearchByRecipient() {
        Messages m = new Messages();
        m.populateTestData();
        String r = m.searchByRecipient("+27838884567");
        assertTrue(r.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(r.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteByHash() {
        Messages m = new Messages();
        m.populateTestData();
        String hash = m.getStoredMessages().get(0)[0];
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.", m.deleteByHash(hash));
    }

    @Test
    public void testDisplayReport() {
        Messages m = new Messages();
        m.populateTestData();
        String rep = m.displayReport();
        assertTrue(rep.contains("Message Hash"));
        assertTrue(rep.contains("Recipient"));
        assertTrue(rep.contains("Message"));
    }
}
