package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Marks extends JFrame implements ActionListener {

    JComboBox<String> courseDropdown, branchDropdown, semesterDropdown;
    String rollNumber;
    JTable table;
    JLabel percentageLabel, cgpaLabel;

    Marks(String rollNumber) {
        this.rollNumber = rollNumber;
        setTitle("Result");
        setSize(800, 600);
        setLocation(400, 150);
        getContentPane().setBackground(new Color(241, 252, 210));
        setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel("Lovely Professional University", JLabel.CENTER);
        header.setFont(new Font("Tahoma", Font.BOLD, 24));
        header.setPreferredSize(new Dimension(800, 50));
        add(header, BorderLayout.NORTH);

        // Dropdown Panels
        JPanel dropdownPanel = new JPanel();
        dropdownPanel.setLayout(new GridLayout(1, 6, 10, 10));

        JLabel courseLabel = new JLabel("Select Course: ");
        courseDropdown = new JComboBox<>(new String[]{"BTech", "Bsc", "BCA", "MTech", "MSc", "MCA", "Bcom", "Mcom"});
        courseDropdown.addActionListener(this);

        JLabel branchLabel = new JLabel("Select Branch: ");
        branchDropdown = new JComboBox<>(new String[]{"CSE", "ECE", "ME", "CE", "EE", "IT"});
        branchDropdown.addActionListener(this);

        JLabel semesterLabel = new JLabel("Select Semester: ");
        semesterDropdown = new JComboBox<>(new String[]{"1st Semester", "2nd Semester", "3rd Semester", "4th Semester", "5th Semester", "6th Semester", "7th Semester", "8th Semester"});
        semesterDropdown.addActionListener(this);

        dropdownPanel.add(courseLabel);
        dropdownPanel.add(courseDropdown);
        dropdownPanel.add(branchLabel);
        dropdownPanel.add(branchDropdown);
        dropdownPanel.add(semesterLabel);
        dropdownPanel.add(semesterDropdown);

        add(dropdownPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Subject", "Marks"};
        String[][] data = getData((String) semesterDropdown.getSelectedItem());
        table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Footer with percentage and CGPA
        JPanel footer = new JPanel();
        footer.setLayout(new GridLayout(3, 1));

        JLabel rollNumberLabel = new JLabel("Roll Number: " + rollNumber);
        percentageLabel = new JLabel("Percentage: ");
        cgpaLabel = new JLabel("CGPA: ");

        rollNumberLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        percentageLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        cgpaLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));

        footer.add(rollNumberLabel);
        footer.add(percentageLabel);
        footer.add(cgpaLabel);

        add(footer, BorderLayout.SOUTH);

        updateFooter(data);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == courseDropdown || e.getSource() == branchDropdown || e.getSource() == semesterDropdown) {
            String semester = (String) semesterDropdown.getSelectedItem();
            String[][] data = getData(semester);
            table.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{"Subject", "Marks"}));
            updateFooter(data);
        }
    }

    private String[][] getData(String semester) {
        String[][] data = new String[5][2]; // Assuming 5 subjects
        try {
            Conn c = new Conn();
            String query = "SELECT sub1, sub2, sub3, sub4, sub5, mrk1, mrk2, mrk3, mrk4, mrk5 " +
                    "FROM subject s JOIN marks m ON s.rollno = m.rollno " +
                    "WHERE s.rollno = '" + rollNumber + "' AND m.semester = '" + semester + "'";
            ResultSet rs = c.statement.executeQuery(query);

            if (rs.next()) {
                data[0][0] = rs.getString("sub1");
                data[0][1] = rs.getString("mrk1");
                data[1][0] = rs.getString("sub2");
                data[1][1] = rs.getString("mrk2");
                data[2][0] = rs.getString("sub3");
                data[2][1] = rs.getString("mrk3");
                data[3][0] = rs.getString("sub4");
                data[3][1] = rs.getString("mrk4");
                data[4][0] = rs.getString("sub5");
                data[4][1] = rs.getString("mrk5");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void updateFooter(String[][] data) {
        int totalMarks = 0;
        int numberOfSubjects = data.length;
        for (String[] row : data) {
            totalMarks += Integer.parseInt(row[1]);
        }
        int percentage = totalMarks / numberOfSubjects;
        double cgpa = percentage / 9.5;

        percentageLabel.setText("Percentage: " + percentage + "%");
        cgpaLabel.setText("CGPA: " + String.format("%.2f", cgpa));
    }

    public static void main(String[] args) {
        new Marks("14093845");
    }
}
