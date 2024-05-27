package university.management.system;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailSender {

    // Regular expression pattern for validating email addresses
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static void main(String[] args) {

        // Sender's email and password
        final String senderEmail = "use your email id";
        final String senderPassword = "use you password";

        // Database connection details
        final String dbUrl = "jdbc:mysql://localhost:3306/Universitymanagement";
        final String dbUsername = "Ayush1";
        final String dbPassword = "Ayush12345";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Create database connection
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            System.out.println("Database connection established.");

            // SQL query to retrieve recipient's email address from the database
            String query = "SELECT email FROM users WHERE registration_status = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "registered");

            // Execute the query
            rs = pstmt.executeQuery();
            System.out.println("Query executed: " + query);

            while (rs.next()) {
                // Retrieve the recipient's email address from the result set
                String recipientEmail = rs.getString("email");

                // Check if the email address is valid
                if (isValidEmail(recipientEmail)) {
                    // Send email to the recipient
                    sendEmail(senderEmail, senderPassword, recipientEmail);
                } else {
                    System.out.println("Invalid email address: " + recipientEmail);
                }
            }

            System.out.println("Emails sent successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the database resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
                System.out.println("Database resources closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendEmail(String senderEmail, String senderPassword, String recipientEmail) {
        // Email properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Get the Session object
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderPassword);
                    }
                });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field of the header
            message.setFrom(new InternetAddress(senderEmail));

            // Set To: header field of the header
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            // Set Subject: header field
            message.setSubject("Registration Confirmation");

            // Now set the actual message
            String registrationMessage = "Thank you for registering with Lovely Professional University. "
                    + "You have successfully registered!";
            message.setText(registrationMessage);

            // Send message
            Transport.send(message);

            System.out.println("Email sent to " + recipientEmail);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
}

