import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class SupportDetailsGeneratorGUI {

    private JFrame frame;
    private JTextField issueField, acknowledgeField, resolutionField;
    private JComboBox<String> statusComboBox;
    private JDatePickerImpl raisedDatePicker, closedDatePicker;
    private JSpinner raisedTimeSpinner, closedTimeSpinner;
    private JTextArea resultTextArea;

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
        frame = new JFrame();
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // First column
        gbc.gridx = 0;

        panel.add(new JLabel("Issue/Request:"), gbc);
        panel.add(new JLabel("Acknowledge by:"), gbc);
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

        // Text area
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Span across both columns
        gbc.fill = GridBagConstraints.BOTH;

        resultTextArea = new JTextArea();
        panel.add(new JScrollPane(resultTextArea), gbc);

        // Copy-Paste button
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Span across both columns
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton copyButton = new JButton("Copy-Paste");
        panel.add(copyButton, gbc);

        JButton submitButton = new JButton("Submit");
        gbc.gridy = 10;
        panel.add(submitButton, gbc);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date raisedDate = (Date) raisedDatePicker.getModel().getValue();
                Date raisedTime = (Date) raisedTimeSpinner.getValue();
                Date closedDate = (Date) closedDatePicker.getModel().getValue();
                Date closedTime = (Date) closedTimeSpinner.getValue();

                String result = "Support Details:\n" +
                        "Issue/Request: " + issueField.getText() + "\n" +
                        "Acknowledge by: " + acknowledgeField.getText() + "\n" +
                        "Status: " + statusComboBox.getSelectedItem() + "\n" +
                        "Resolution: " + resolutionField.getText() + "\n" +
                        "Date Raised: " + formatDate(raisedDate) + "\n" +
                        "Time Raised: " + formatTime(raisedTime) + "\n" +
                        "Date Closed: " + formatDate(closedDate) + "\n" +
                        "Time Closed: " + formatTime(closedTime) + "\n";
                resultTextArea.setText(result);
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
