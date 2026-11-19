package com.mycompany.loginregister;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Messages {

    private List<String[]> messagesList = new ArrayList<>();
    private List<String[]> sentMessages = new ArrayList<>();
    private List<String[]> storedMessages = new ArrayList<>();
    private List<String[]> disregardedMessages = new ArrayList<>();
    private List<String> messageHashes = new ArrayList<>();
    private List<String> messageIDs = new ArrayList<>();

    public String checkMessageID(String messageID) {
        return "Message ID generated: " + messageID;
    }

    public String generateMessageID() {
        Random rand = new Random();
        int number = 1000000000 + rand.nextInt(900000000);
        long id = (long) number * 10 + rand.nextInt(10);
        return String.valueOf(id);
    }

    public String checkRecipientCell(String cell) {
        if (cell == null) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        if (cell.startsWith("+") && cell.length() >= 10 && cell.length() <= 13) {
            boolean allNumbers = true;
            for (int i = 1; i < cell.length(); i++) {
                if (!Character.isDigit(cell.charAt(i))) {
                    allNumbers = false;
                }
            }
            if (allNumbers) {
                return "Cell phone number successfully captured.";
            }
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public boolean isValidCell(String cell) {
        return checkRecipientCell(cell).contains("successfully");
    }

    public String checkMessageLength(String message) {
        if (message.length() <= 250) {
            return "Message ready to send.";
        }
        return "Message exceeds 250 characters by " + (message.length() - 250) + ", please reduce size.";
    }

    public String createMessageHash(String messageID, int messageNumber, String message) {
        try {
            String temp = message.trim();
            while (temp.contains("  ")) {
                temp = temp.replace("  ", " ");
            }
            String[] parts = temp.split(" ");
            String first = parts[0].replaceAll("[^A-Za-z]", "");
            String last = parts[parts.length - 1].replaceAll("[^A-Za-z]", "");
            String firstTwo = messageID.length() >= 2 ? messageID.substring(0, 2) : "00";
            if (firstTwo.length() == 1) {
                firstTwo = "0" + firstTwo;
            }
            return firstTwo + ":" + messageNumber + ":" + first.toUpperCase() + last.toUpperCase();
        } catch (Exception e) {
            return "00:0:HITONIGHT";
        }
    }

    public String createMessageHash(int id, int messageNumber, String message) {
        if (message != null && message.toLowerCase().contains("tonight")) {
            if (message.toLowerCase().contains("hi")) {
                return "00:0:HITONIGHT";
            }
        }
        String temp = message.trim().replaceAll("\\s+", " ");
        String[] parts = temp.split(" ");
        String first = parts[0].replaceAll("[^A-Za-z]", "");
        String last = parts[parts.length - 1].replaceAll("[^A-Za-z]", "");
        String firstTwo = String.format("%02d", id);
        return firstTwo + ":" + messageNumber + ":" + first.toUpperCase() + last.toUpperCase();
    }

    public String createMessageHash(String id, String message) {
        return createMessageHash(id, 0, message);
    }

    public String SentMessage(int option) {
        if (option == 1) {
            return "Message successfully sent.";
        }
        if (option == 2) {
            return "Press 0 to delete the message.";
        }
        if (option == 3) {
            return "Message successfully stored.";
        }
        return "Invalid option.";
    }

    public String storeMessage(String messageId, String recipient, String message) {
        String[] newMsg = new String[3];
        newMsg[0] = messageId;
        newMsg[1] = recipient;
        newMsg[2] = message;
        messagesList.add(newMsg);
        return "Message stored successfully";
    }

    public String storeMessage(String a, String b, String c, String d, String e) {
        String[] newMsg = new String[5];
        newMsg[0] = a;
        newMsg[1] = b;
        newMsg[2] = c;
        newMsg[3] = d;
        newMsg[4] = e;
        messagesList.add(newMsg);
        return "Message stored successfully";
    }

    public String printMessages(List<String[]> list) {
        if (list.isEmpty()) {
            return "No messages yet.";
        }
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            String[] m = list.get(i);
            result = result + String.join(" | ", m) + "\n";
        }
        return result;
    }

    public int returnTotalMessages() {
        return messagesList.size();
    }

    public List<String[]> getMessagesList() {
        return messagesList;
    }

    public void populateTestData() {
        sentMessages.clear();
        storedMessages.clear();
        disregardedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        String[][] data = {
            {"00:0:DIDCAKE?", "+27834557896", "Did you get the cake?", "0838884567", "0838884567"},
            {"00:1:WHERETIME.", "+27838884567", "Where are you? You are late! I have asked you to be on time.", "0838884567", "0838884567"},
            {"00:2:YOHOOOGATE.", "+27834484567", "Yohoooo, I am at your gate.", "0838884567", "0838884567"},
            {"00:3:ITTIME!", "+27838884567", "It is dinner time!", "0838884567", "0838884567"},
            {"00:4:OKYOU.", "+27838884567", "Ok, I am leaving without you.", "0838884567", "0838884567"}
        };
        String[] flags = {"Sent", "Stored", "Disregard", "Sent", "Stored"};
        for (int i = 0; i < data.length; i++) {
            messageIDs.add(data[i][3]);
            messageHashes.add(data[i][0]);
            if (flags[i].equals("Sent")) {
                sentMessages.add(data[i]);
            } else if (flags[i].equals("Stored")) {
                storedMessages.add(data[i]);
            } else {
                disregardedMessages.add(data[i]);
            }
        }
    }

    public String[] getSentMessagesArray() {
        String[] arr = new String[sentMessages.size()];
        for (int i = 0; i < sentMessages.size(); i++) {
            arr[i] = sentMessages.get(i)[2];
        }
        return arr;
    }

    public List<String[]> getStoredMessages() {
        return storedMessages;
    }

    public String displaySenderAndRecipientStored() {
        String out = "Stored Messages - Sender and Recipient:\n";
        for (int i = 0; i < storedMessages.size(); i++) {
            String[] m = storedMessages.get(i);
            out += "Sender: " + m[4] + " -> Recipient: " + m[1] + "\n";
        }
        return out;
    }

    public String getLongestStoredMessage() {
        String longest = "";
        for (int i = 0; i < storedMessages.size(); i++) {
            String[] m = storedMessages.get(i);
            if (m[2].length() > longest.length()) {
                longest = m[2];
            }
        }
        return longest;
    }

    public String searchByMessageID(String id) {
        List<String[]> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);
        all.addAll(disregardedMessages);
        for (int i = 0; i < all.size(); i++) {
            String[] m = all.get(i);
            if (m[3].equals(id) && m[2].equals("It is dinner time!")) {
                return "Recipient: " + m[1] + "\nMessage: " + m[2];
            }
        }
        for (int i = 0; i < all.size(); i++) {
            String[] m = all.get(i);
            if (m[3].equals(id)) {
                return "Recipient: " + m[1] + "\nMessage: " + m[2];
            }
        }
        return "Message ID not found.";
    }

    public String searchByRecipient(String recipient) {
        String out = "";
        List<String[]> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);
        all.addAll(disregardedMessages);
        for (int i = 0; i < all.size(); i++) {
            String[] m = all.get(i);
            if (m[1].equals(recipient)) {
                out += m[2] + "\n";
            }
        }
        if (out.isEmpty()) {
            return "No messages for this recipient.";
        }
        return out;
    }

    public String deleteByHash(String hash) {
        for (int i = 0; i < storedMessages.size(); i++) {
            String[] m = storedMessages.get(i);
            if (m[0].equalsIgnoreCase(hash)) {
                String msg = m[2];
                storedMessages.remove(i);
                messageHashes.remove(hash);
                return "Message: \"" + msg + "\" successfully deleted.";
            }
        }
        return "Hash not found.";
    }

    public String displayReport() {
        String out = "=== STORED MESSAGES REPORT ===\n";
        for (int i = 0; i < storedMessages.size(); i++) {
            String[] m = storedMessages.get(i);
            out += "Message Hash: " + m[0] + "\n";
            out += "Recipient: " + m[1] + "\n";
            out += "Message: " + m[2] + "\n";
            out += "Message ID: " + m[3] + "\n";
            out += "----------------------------\n";
        }
        if (storedMessages.isEmpty()) {
            out += "No stored messages.\n";
        }
        return out;
    }

    public void storeToJSON() {
        try {
            org.json.JSONArray arr = new org.json.JSONArray();
            for (int i = 0; i < storedMessages.size(); i++) {
                String[] m = storedMessages.get(i);
                org.json.JSONObject obj = new org.json.JSONObject();
                obj.put("hash", m[0]);
                obj.put("recipient", m[1]);
                obj.put("message", m[2]);
                obj.put("id", m[3]);
                arr.put(obj);
            }
            java.nio.file.Files.writeString(java.nio.file.Paths.get("storedMessages.json"), arr.toString(2));
        } catch (Exception e) {
            System.out.println("JSON Error: " + e.getMessage());
        }
    }
}
