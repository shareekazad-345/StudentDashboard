import java.util.HashMap;
import java.util.Map;

public class Student {
    private String name;
    private int grade;
    private int attendance;
    private Map<String, Double> subjectMarks;

    // Constructor
    public Student(String name, int grade, int attendance) {
        this.name = name;
        this.grade = grade;
        this.attendance = attendance;
        this.subjectMarks = new HashMap<>();
    }

    // Add a subject and its mark
    public void addMark(String subject, double mark) {
        subjectMarks.put(subject, mark);
    }

    // Getters
    public String getName() { return name; }
    public int getGrade() { return grade; }
    public int getAttendance() { return attendance; }
    public Map<String, Double> getSubjectMarks() { return subjectMarks; }

    // Calculate average marks
    public double calculateAverage() {
        if (subjectMarks.isEmpty()) return 0;
        double total = 0;
        for (double mark : subjectMarks.values()) {
            total += mark;
        }
        return total / subjectMarks.size();
    }
}