import java.util.Map;

public class AlertManager {
    public static void checkAlerts(Student student) {
        // Attendance alert
        if (student.getAttendance() < 75) {
            System.out.println("⚠️ Attendance below 75%! Risk of exam ineligibility.");
        }

        // Average performance alert
        double avg = student.calculateAverage();
        if (avg < 50) {
            System.out.println("⚠️ Average marks below 50%. Needs improvement.");
        } else if (avg >= 80) {
            System.out.println("🎉 Excellent overall performance! Keep it up.");
        }

        // Subject-level alerts (loop through Map instead of array)
        for (Map.Entry<String, Double> entry : student.getSubjectMarks().entrySet()) {
            String subject = entry.getKey();
            double mark = entry.getValue();

            if (mark < 40) {
                System.out.println("⚠️ Needs improvement in " + subject + " (mark: " + mark + ")");
            } else if (mark >= 90) {
                System.out.println("🎉 Outstanding in " + subject + " (mark: " + mark + ")");
            }
        }
    }
}