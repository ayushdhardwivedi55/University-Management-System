package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class About extends JFrame {
    About() {
        // Setting up the frame
        setTitle("About Us");
        setSize(800, 600);
        setLocation(300, 100);
        getContentPane().setBackground(new Color(252, 228, 210));
        setLayout(null);

        // Adding university logo
        ImageIcon i1 = loadImageIcon("icon/about.png", 300, 200);
        if (i1 != null) {
            JLabel img = new JLabel(i1);
            img.setBounds(250, 20, 300, 200);
            add(img);
        }

        // Adding university name
        JLabel heading = new JLabel("Lovely Professional University");
        heading.setBounds(100, 220, 600, 40);
        heading.setFont(new Font("Tahoma", Font.BOLD, 30));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        add(heading);

        // Adding developer's name
        JLabel name = new JLabel("Ayush Dhar Dwivedi");
        name.setBounds(100, 280, 600, 30);
        name.setFont(new Font("Tahoma", Font.BOLD, 24));
        name.setHorizontalAlignment(SwingConstants.CENTER);
        add(name);

        // Adding contact information
        JLabel contact = new JLabel("Contact us @: princedhardwivedi55@gmail.com");
        contact.setBounds(100, 320, 600, 30);
        contact.setFont(new Font("Tahoma", Font.PLAIN, 18));
        contact.setHorizontalAlignment(SwingConstants.CENTER);
        add(contact);

        JLabel phone = new JLabel("Phone: +917009127961");
        phone.setBounds(100, 360, 600, 30);
        phone.setFont(new Font("Tahoma", Font.PLAIN, 18));
        phone.setHorizontalAlignment(SwingConstants.CENTER);
        add(phone);

        // Adding a brief description
        JTextArea description = new JTextArea("This application is developed for managing university operations, including student fee management, teacher updates, and more. It aims to streamline administrative tasks and provide a user-friendly interface for students and staff.");
        description.setBounds(50, 420, 700, 60);
        description.setFont(new Font("Tahoma", Font.ITALIC, 16));
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setOpaque(false);
        description.setEditable(false);
        description.setFocusable(false);
        add(description);

        // Adding social media icons
        JPanel socialMediaPanel = new JPanel();
        socialMediaPanel.setBounds(250, 500, 300, 50);
        socialMediaPanel.setOpaque(false);
        socialMediaPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        String[] socialMediaIcons = {"icon/facebook.png", "icon/twitter.png", "icon/linkedin.png"};
        String[] tooltips = {"Facebook", "Twitter", "LinkedIn"};
        String[] links = {"https://www.facebook.com/", "https://www.twitter.com/", "https://www.linkedin.com/in/ayush0505/"};

        for (int i = 0; i < socialMediaIcons.length; i++) {
            ImageIcon icon = loadImageIcon(socialMediaIcons[i], 30, 30);
            if (icon != null) {
                JLabel label = new JLabel(icon);
                label.setToolTipText(tooltips[i]);
                final String link = links[i];
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        openWebPage(link);
                    }
                });
                socialMediaPanel.add(label);
            }
        }

        add(socialMediaPanel);

        setVisible(true);
    }

    // Utility method to load image icon
    private ImageIcon loadImageIcon(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(path));
            Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
            return null;
        }
    }

    // Utility method to open a web page
    private void openWebPage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URI(urlString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new About();
    }
}
