import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SupportDetailsLogger {

    private String logFolderPath;

    public SupportDetailsLogger(String logFolderPath) {
        this.logFolderPath = logFolderPath;
        new File(logFolderPath).mkdirs();
    }

    public void logDetails(String details) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String logFileName = "support_details_log_" + timestamp + ".txt";
        String logFilePath = logFolderPath + File.separator + logFileName;

        try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath))) {
            writer.println(details);
            System.out.println("Log file created: " + logFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
