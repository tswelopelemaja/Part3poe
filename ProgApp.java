package com.mycompany.loginregister;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ProgApp {

    private String username;
    private String password;
    private String firstName;
    private String lastName;

    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            }
            if (Character.isDigit(c)) {
                hasNumber = true;
            }
            if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    public boolean checkCellPhoneNumber(String cell) {
        if (cell == null) {
            return false;
        }
        return cell.startsWith("+27") && cell.length() == 12;
    }

    public String registerUser(String username, String password, String firstName, String lastName) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than 5 characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted, please ensure that the password contains at least 8 characters, a capital letter, a number and a special character.";
        }
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        return "User registered successfully.";
    }

    public boolean loginUser(String username, String password) {
        return username.equals(this.username) && password.equals(this.password);
    }

    public String returnLoginStatus(boolean loggedIn) {
        if (loggedIn) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    public static void main(String[] args) {
        ProgApp loginObj = new ProgApp();
        Messages messageObj = new Messages();

        String fName = JOptionPane.showInputDialog("Enter First Name:");
        String lName = JOptionPane.showInputDialog("Enter Last Name:");
        String user = JOptionPane.showInputDialog("Enter Username (must contain _ and be max 5 characters):");
        String pass = JOptionPane.showInputDialog("Enter Password (min 8 chars, capital, number, special char):");

        String regStatus = loginObj.registerUser(user, pass, fName, lName);
        JOptionPane.showMessageDialog(null, regStatus);
        if (!regStatus.contains("successfully")) {
            return;
        }

        String loginUser = JOptionPane.showInputDialog("LOGIN - Enter Username:");
        String loginPass = JOptionPane.showInputDialog("LOGIN - Enter Password:");
        boolean loggedIn = loginObj.loginUser(loginUser, loginPass);
        JOptionPane.showMessageDialog(null, loginObj.returnLoginStatus(loggedIn));
        if (!loggedIn) {
            return;
        }

        JOptionPane.showMessageDialog(null, "Welcome to QuickChat.");

        String countInput = JOptionPane.showInputDialog("How many messages do you want to send?");
        int messageLimit = Integer.parseInt(countInput);
        int messagesSent = 0;
        List<String[]> sentMessages = new ArrayList<>();

        while (true) {
            String menu = JOptionPane.showInputDialog(
                    "Choose:\n1) Send Message\n2) Show recently sent messages\n3) Quit\n4) Stored Messages");

            if (menu == null) {
                break;
            }

            switch (menu) {
                case "1":
                    if (messagesSent >= messageLimit) {
                        JOptionPane.showMessageDialog(null, "You have reached the message limit.");
                        break;
                    }
                    String recipient = JOptionPane.showInputDialog("Enter recipient cell number (e.g. +27731234567):");
                    String message = JOptionPane.showInputDialog("Enter message (max 250 chars):");

                    if (!messageObj.isValidCell(recipient)) {
                        JOptionPane.showMessageDialog(null, "Cell phone number is incorrectly formatted or does not contain an international code.");
                        break;
                    }
                    if (message.length() > 250) {
                        JOptionPane.showMessageDialog(null, "Please enter a message of less than 250 characters.");
                        break;
                    }

                    String hash = messageObj.createMessageHash(String.valueOf(messagesSent), message);
                    String result = messageObj.storeMessage(recipient, message, hash);
                    JOptionPane.showMessageDialog(null, result);

                    if (result.contains("successfully")) {
                        sentMessages.add(new String[]{hash, recipient, message});
                        messagesSent++;
                    }
                    break;

                case "2":
                    JOptionPane.showMessageDialog(null, messageObj.printMessages(sentMessages));
                    break;

                case "3":
                    JOptionPane.showMessageDialog(null, "Total messages sent: " + messagesSent);
                    System.exit(0);
                    break;

                case "4":
                    if (messageObj.getStoredMessages().isEmpty()) {
                        messageObj.populateTestData();
                        messageObj.storeToJSON();
                    }
                    String[] sub = {"a. Sender & Recipient", "b. Longest Message", "c. Search by ID", "d. Search by Recipient", "e. Delete by Hash", "f. Display Report", "Back"};
                    int c2 = JOptionPane.showOptionDialog(null, "Stored Messages Menu", "Stored Messages", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, sub, sub[0]);
                    if (c2 == 0) {
                        JOptionPane.showMessageDialog(null, messageObj.displaySenderAndRecipientStored());
                    } else if (c2 == 1) {
                        JOptionPane.showMessageDialog(null, "Longest Stored Message:\n" + messageObj.getLongestStoredMessage());
                    } else if (c2 == 2) {
                        String id = JOptionPane.showInputDialog("Enter Message ID:");
                        JOptionPane.showMessageDialog(null, messageObj.searchByMessageID(id));
                    } else if (c2 == 3) {
                        String rec = JOptionPane.showInputDialog("Enter Recipient e.g. +27838884567:");
                        JOptionPane.showMessageDialog(null, messageObj.searchByRecipient(rec));
                    } else if (c2 == 4) {
                        String h = JOptionPane.showInputDialog("Enter Message Hash:");
                        JOptionPane.showMessageDialog(null, messageObj.deleteByHash(h));
                        messageObj.storeToJSON();
                    } else if (c2 == 5) {
                        JOptionPane.showMessageDialog(null, messageObj.displayReport());
                    }
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Invalid option.");
                    break;
            }
        }
    }
}
