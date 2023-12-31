// Make a way to clear the details for the entered details
// how to automate it in the OSTicket


// 



// this project is Created by Jericho G. Del Rosario




import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Properties;


import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
public class SupportDetailsGeneratorGUI {

    private JFrame frame;
    private JTextField issueField, acknowledgeField, resolutionField, issuedForField, remarks;
    private JComboBox<String> statusComboBox;
    private JDatePickerImpl raisedDatePicker, closedDatePicker;
    private JSpinner raisedTimeSpinner, closedTimeSpinner;
    private JTextArea resultTextArea;
    private SupportDetailsLogger logger;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new SupportDetailsGeneratorGUI().initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void initialize() {
        String logFilePath = askForLogFilePath();
        SupportDetailsLogger logger = new SupportDetailsLogger(logFilePath);
        frame = new JFrame();
        frame.setBounds(100, 100, 500, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Support Details Generator");
        JPanel panel = new JPanel(new GridBagLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // First column
        gbc.gridx = 0;

        panel.add(new JLabel("Issue/Request:"), gbc);
        panel.add(new JLabel("Ticket Issued to:"), gbc);
        panel.add(new JLabel("Acknowledged by:"), gbc);
        panel.add(new JLabel("Status:"), gbc);
        panel.add(new JLabel("Resolution:"), gbc);
        panel.add(new JLabel("Date Raised:"), gbc);
        panel.add(new JLabel("Time Raised:"), gbc);
        panel.add(new JLabel("Date Closed:"), gbc);
        panel.add(new JLabel("Time Closed:"), gbc);

        // Second column
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        issueField = new JTextField();
        gbc.gridwidth = 2; // Span across 2 columns
        panel.add(issueField, gbc);

        issuedForField = new JTextField();
        gbc.gridwidth = 2; // Span across 2 columns
        panel.add(issuedForField, gbc);

        acknowledgeField = new JTextField();
        panel.add(acknowledgeField, gbc);

        String[] statusOptions = {"Open", "Closed", "In Progress", "On Hold"};
        statusComboBox = new JComboBox<>(statusOptions);
        panel.add(statusComboBox, gbc);

        resolutionField = new JTextField();
        panel.add(resolutionField, gbc);

        raisedDatePicker = createDatePicker();
        panel.add(raisedDatePicker, gbc);

        raisedTimeSpinner = createTimeSpinner();
        panel.add(raisedTimeSpinner, gbc);

        closedDatePicker = createDatePicker();
        panel.add(closedDatePicker, gbc);

        closedTimeSpinner = createTimeSpinner();
        panel.add(closedTimeSpinner, gbc);

        // remarks = new JTextField();
        // panel.add(remarks,gbc);
        gbc.gridy = 9; // Adjust the gridy value as needed
        gbc.gridx = 0;
        JLabel remarksLabel = new JLabel("Remarks:");
        panel.add(remarksLabel, gbc);
        //remarks
        gbc.gridy = 10; // Adjust the gridy value as needed
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        remarks = new JTextField();
        panel.add(remarks, gbc);

        // Text area
        gbc.gridy = 11; // Adjust the gridy value to the next row
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Span across both columns
        gbc.fill = GridBagConstraints.BOTH;

        resultTextArea = new JTextArea();
        panel.add(new JScrollPane(resultTextArea), gbc);
        remarks.setPreferredSize(new Dimension(200, 60));

        JButton submitButton = new JButton("Submit");
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(submitButton, gbc);

        // Copy-Paste button
        gbc.gridy = 16; // Adjusted gridy value
        gbc.gridx = 1;
        gbc.gridwidth = 1; // Span only one column
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton copyButton = new JButton("Copy-Paste");
        panel.add(copyButton, gbc);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date raisedDate = (Date) raisedDatePicker.getModel().getValue();
                Date raisedTime = (Date) raisedTimeSpinner.getValue();
                Date closedDate = (Date) closedDatePicker.getModel().getValue();
                Date closedTime = (Date) closedTimeSpinner.getValue();

                // Check if raisedDate and closedDate are not null before formatting
                String result =
                        "Good Day Ms/Mr :" + issuedForField.getText() + "\n" +
                                "\n" +
                                "Support Details:\n" +
                                "Issue/Request: " + issueField.getText() + "\n" +
                                "Ticket Request from:" + issuedForField.getText() + "\n" +
                                "Acknowledged by: " + acknowledgeField.getText() + "\n" +
                                "Status: " + statusComboBox.getSelectedItem() + "\n" +
                                "Resolution: " + resolutionField.getText() + "\n" +
                                "Date Raised: " + (raisedDate != null ? formatDate(raisedDate) : "") + "\n" +
                                "Time Raised: " + formatTime(raisedTime) + "\n";

                // Check status and set Date Closed and Time Closed accordingly
                if ("In Progress".equals(statusComboBox.getSelectedItem()) ||
                        "Open".equals(statusComboBox.getSelectedItem()) ||
                        "On Hold".equals(statusComboBox.getSelectedItem())) {
                    result += "Date Closed: n/a\nTime Closed: n/a\n";
                } else {
                    result += "Date Closed: " + (closedDate != null ? formatDate(closedDate) : "") + "\n" +
                            "Time Closed: " + formatTime(closedTime) + "\n";
                }

                result += "Remarks: " + remarks.getText() + "\n" +
                        "\n" +
                        "Thank you for your patience 😀";

                resultTextArea.setText(result);
                logger.logDetails("Generated Support Details:\n" + result);
            }
        });

        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection selection = new StringSelection(resultTextArea.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, null);
                JOptionPane.showMessageDialog(frame, "Text copied to clipboard!");
            }
        });

        frame.setVisible(true);
    }
    private String askForLogFilePath() {
        String logFilePath = null;
    
        // Display a dialog prompt for the user to enter the log file path
        while (logFilePath == null || logFilePath.trim().isEmpty()) {
            Object[] message = {"Enter the path where you want to save the log file:"};
            logFilePath = JOptionPane.showInputDialog(frame, message, "Log File Path", JOptionPane.QUESTION_MESSAGE);
            if (logFilePath == null) {
                // User clicked Cancel, exit the application or handle accordingly
                System.exit(0);
            }
        }
        return logFilePath;
    }
    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    private JSpinner createTimeSpinner() {
        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "hh:mm a");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date()); // set the current time
        return timeSpinner;
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    private String formatTime(Date time) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        return timeFormat.format(time);
    }

    private static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String datePattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws java.text.ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
}
