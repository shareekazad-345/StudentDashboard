public class Dashboard {
    public static void showStudentReport(Student student) {
        System.out.println("\n--- Student Dashboard ---");
        System.out.println("Name: " + student.getName());
        System.out.println("Grade: " + student.getGrade());
        System.out.println("Average Marks: " + student.calculateAverage());
        System.out.println("Attendance: " + student.getAttendance() + "%");

        // Call alerts
        AlertManager.checkAlerts(student);
    }
}
