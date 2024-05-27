package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Login extends JFrame implements ActionListener {
    JTextField textFieldName, textFieldEmail, textFieldPhone, textFieldOTP;
    JPasswordField passwordField;
    RoundedButton register, login, back, verifyOTP;
    JLabel profilePic;
    JFrame otpFrame;

    public Login() {
        setUndecorated(true);
        setLayout(new BorderLayout());

        JPanel contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        add(contentPane, BorderLayout.CENTER);

        JLabel labelName = new JLabel("Username");
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(labelName, gbc);

        textFieldName = new JTextField(20);
        gbc.gridx = 1;
        contentPane.add(textFieldName, gbc);

        JLabel labelEmail = new JLabel("Email");
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(labelEmail, gbc);

        textFieldEmail = new JTextField(20);
        gbc.gridx = 1;
        contentPane.add(textFieldEmail, gbc);

        JLabel labelPhone = new JLabel("Phone");
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPane.add(labelPhone, gbc);

        textFieldPhone = new JTextField(20);
        gbc.gridx = 1;
        contentPane.add(textFieldPhone, gbc);

        JLabel labelPass = new JLabel("Password");
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPane.add(labelPass, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        contentPane.add(passwordField, gbc);

        register = new RoundedButton("Register");
        register.setBackground(new Color(50, 205, 50));
        register.setForeground(Color.WHITE);
        register.setFocusPainted(false); // Remove focus border
        register.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(register, gbc);

        login = new RoundedButton("Login");
        login.setBackground(new Color(30, 144, 255));
        login.setForeground(Color.WHITE);
        login.setFocusPainted(false); // Remove focus border
        login.addActionListener(this);
        gbc.gridy = 6;
        contentPane.add(login, gbc);

        back = new RoundedButton("Back");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setFocusPainted(false); // Remove focus border
        back.addActionListener(this);
        gbc.gridy = 7;
        contentPane.add(back, gbc);

        profilePic = new JLabel(new ImageIcon(ClassLoader.getSystemResource("icon/second.png")));
        profilePic.setPreferredSize(new Dimension(250, 210));
        profilePic.setHorizontalAlignment(SwingConstants.CENTER);
        add(profilePic, BorderLayout.EAST);

        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == register) {
            // Check if all fields are filled
            if (textFieldName.getText().isEmpty() || textFieldEmail.getText().isEmpty() || textFieldPhone.getText().isEmpty() || passwordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                return;
            }

            // Validate email and phone format
            if (!isValidEmail(textFieldEmail.getText())) {
                JOptionPane.showMessageDialog(null, "Invalid email format.");
                return;
            }
            if (!isValidPhone(textFieldPhone.getText())) {
                JOptionPane.showMessageDialog(null, "Invalid phone number format.");
                return;
            }

            String username = textFieldName.getText();
            String email = textFieldEmail.getText();
            String phone = textFieldPhone.getText();
            String password = String.valueOf(passwordField.getPassword());

            // Generate OTP for email
            String otpEmail = generateOTP();

            // Save user and OTPs to the database
            saveUser(username, password, email, phone, otpEmail);

            try {
                // Send OTP to email
                sendOTPToEmail(email, otpEmail);

                // Show OTP verification dialog
                showOTPDialog();
            } catch (MessagingException ex) {
                JOptionPane.showMessageDialog(null, "Failed to send OTP. Please check your email address.");
            }
        } else if (e.getSource() == login) {
            // Check if username and password fields are filled
            if (textFieldName.getText().isEmpty() || passwordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(null, "Please enter username and password.");
                return;
            }

            String username = textFieldName.getText();
            String password = String.valueOf(passwordField.getPassword());

            // Verify username and password
            if (verifyLogin(username, password)) {
                JOptionPane.showMessageDialog(null, "Login successful.");
                // Proceed to main screen
                new main_class().setVisible(true);
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password.");
            }
        } else if (e.getSource() == back) {
            setVisible(false);
        } else if (e.getSource() == verifyOTP) {
            String otp = textFieldOTP.getText();
            // Verify OTP
            if (verifyOTP(otp)) {
                JOptionPane.showMessageDialog(null, "OTP verified successfully. User registered.");

                // Send registration confirmation email
                String email = textFieldEmail.getText();
                sendRegistrationEmail(email);

                // Close OTP dialog
                otpFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid OTP. Please try again.");
            }
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhone(String phone) {
        String phoneRegex = "^\\d{10}$";
        return phone.matches(phoneRegex);
    }

    private void showOTPDialog() {
        otpFrame = new JFrame("OTP Verification");
        otpFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel labelOTP = new JLabel("Enter OTP sent to your email:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        otpFrame.add(labelOTP, gbc);

        textFieldOTP = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        otpFrame.add(textFieldOTP, gbc);

        verifyOTP = new RoundedButton("Verify OTP");
        verifyOTP.setBackground(new Color(30, 144, 255));
        verifyOTP.setForeground(Color.WHITE);
        verifyOTP.setFocusPainted(false); // Remove focus border
        verifyOTP.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        otpFrame.add(verifyOTP, gbc);

        otpFrame.setSize(300, 200);
        otpFrame.setLocationRelativeTo(null);
        otpFrame.setVisible(true);
    }

    private void saveUser(String username, String password, String email, String phone, String otpEmail) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Universitymanagement", "Ayush12", "Ayush12345");
            String query = "INSERT INTO users (username, password, email, phone, otp_email) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, otpEmail);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean verifyLogin(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Universitymanagement", "Ayush12", "Ayush12345");
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean verifyOTP(String otp) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Universitymanagement", "Ayush", "Ayush123");
            String query = "SELECT * FROM users WHERE otp_email = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, otp);
            rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private void sendOTPToEmail(String email, String otp) throws MessagingException {
        String host = "smtp.gmail.com";
        String from = "use your own";
        String pass = "use your own";// Replace with your app-specific password

        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("OTP Verification");
        message.setText("Your OTP for registration is: " + otp);

        Transport.send(message);
    }

    private void sendRegistrationEmail(String email) {
        try {
            String subject = "Welcome to University Management System!";
            String body = "Dear user,\n" +
                    "Congratulations on successfully registering to the University Management System! We are thrilled to have you with us.\n\n" +
                    "Here are some things you can do next:\n" +
                    "1. Explore our features: Get familiar with the platform and explore all the functionalities we offer to make your university life easier.\n" +
                    "2. Update your profile: Add more information to your profile to get personalized recommendations and updates.\n" +
                    "3. Contact support: If you have any questions or need assistance, don't hesitate to reach out to our support team.\n\n" +
                    "We hope you have a great experience using our system. If you have any feedback or suggestions, feel free to share them with us.\n\n" +
                    "Best regards,\n" +
                    "University Management System Team";

            sendEmail(email, subject, body);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    private void sendEmail(String to, String subject, String body) throws MessagingException {
        String host = "smtp.gmail.com";
        String from = "use your own";
        String pass = "use your own"; // Replace with your app-specific password

        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }

    public static void main(String[] args) {
        new Login();
    }
}
