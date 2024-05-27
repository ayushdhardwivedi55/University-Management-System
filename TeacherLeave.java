package university.management.system;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class TeacherLeave extends JFrame implements ActionListener {
    Choice choiceRollNo, choTime, halfDayChoice;
    JDateChooser selDate;
    JTextField startTimeField, endTimeField;
    JButton submit, cancel;

    TeacherLeave() {
        getContentPane().setBackground(new Color(210, 232, 252));

        JLabel heading = new JLabel("Apply Leave (Teacher)");
        heading.setFont(new Font("Tahoma", Font.BOLD, 20));

        JLabel RollNoSE = new JLabel("Search by Employee ID");
        RollNoSE.setFont(new Font("Tahoma", Font.PLAIN, 18));

        choiceRollNo = new Choice();
        try {
            Conn c = new Conn();
            ResultSet resultSet = c.statement.executeQuery("select * from teacher");
            while (resultSet.next()) {
                choiceRollNo.add(resultSet.getString("empId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel lbldate = new JLabel("Date");
        lbldate.setFont(new Font("Tahoma", Font.PLAIN, 18));

        selDate = new JDateChooser();

        JLabel time = new JLabel("Time Duration");
        time.setFont(new Font("Tahoma", Font.PLAIN, 18));

        choTime = new Choice();
        choTime.add("Full Day");
        choTime.add("Half Day");
        choTime.add("Custom Hours");
        choTime.addItemListener(e -> toggleTimeOptions());

        JLabel halfDayLabel = new JLabel("Half Day Options");
        halfDayLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));

        halfDayChoice = new Choice();
        halfDayChoice.add("9:00AM-12:00PM");
        halfDayChoice.add("12:00PM-5:00PM");

        JLabel startTimeLabel = new JLabel("Start Time (HH:MM)");
        startTimeLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));

        startTimeField = new JTextField();

        JLabel endTimeLabel = new JLabel("End Time (HH:MM)");
        endTimeLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));

        endTimeField = new JTextField();

        submit = new JButton("Submit");
        submit.setBackground(Color.black);
        submit.setForeground(Color.white);
        submit.addActionListener(this);

        cancel = new JButton("Cancel");
        cancel.setBackground(Color.black);
        cancel.setForeground(Color.white);
        cancel.addActionListener(this);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(heading, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        add(RollNoSE, gbc);

        gbc.gridx = 1;
        add(choiceRollNo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(lbldate, gbc);

        gbc.gridx = 1;
        add(selDate, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(time, gbc);

        gbc.gridx = 1;
        add(choTime, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(halfDayLabel, gbc);

        gbc.gridx = 1;
        add(halfDayChoice, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(startTimeLabel, gbc);

        gbc.gridx = 1;
        add(startTimeField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(endTimeLabel, gbc);

        gbc.gridx = 1;
        add(endTimeField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(submit, gbc);

        gbc.gridx = 1;
        add(cancel, gbc);

        setSize(400, 600);
        setLocation(550, 100);
        setVisible(true);

        toggleTimeOptions();
    }

    private void toggleTimeOptions() {
        boolean isHalfDay = choTime.getSelectedItem().equals("Half Day");
        boolean isCustom = choTime.getSelectedItem().equals("Custom Hours");

        halfDayChoice.setVisible(isHalfDay);
        startTimeField.setVisible(isCustom);
        endTimeField.setVisible(isCustom);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            String empId = choiceRollNo.getSelectedItem();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(selDate.getDate());
            String leaveType = choTime.getSelectedItem();
            String halfDayOption = null;
            String startTime = null;
            String endTime = null;

            if (leaveType.equals("Half Day")) {
                halfDayOption = halfDayChoice.getSelectedItem();
            } else if (leaveType.equals("Custom Hours")) {
                startTime = startTimeField.getText();
                endTime = endTimeField.getText();
                if (startTime.isEmpty() || endTime.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both start and end times for custom hours.");
                    return;
                }
            }

            String Q = "INSERT INTO teacherleave (empId, date, leave_type, half_day_option, start_time, end_time) VALUES ('"
                    + empId + "', '" + date + "', '" + leaveType + "', '" + halfDayOption + "', '" + startTime + "', '" + endTime + "')";
            try {
                Conn c = new Conn();
                c.statement.executeUpdate(Q);
                JOptionPane.showMessageDialog(null, "Leave Confirmed");
                setVisible(false);
            } catch (Exception E) {
                E.printStackTrace();
            }
        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new TeacherLeave();
    }
}
