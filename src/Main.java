import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Create a list of students
        ArrayList<Student> students = new ArrayList<>();

        // Add multiple students
        Student shareeka = new Student("Shareeka", 11, 72);
        shareeka.addMark("Math", 85);
        shareeka.addMark("Science", 70);
        shareeka.addMark("ICT", 90);
        shareeka.addMark("English", 60);
        students.add(shareeka);

        Student amal = new Student("Amal", 10, 80);
        amal.addMark("Math", 45);
        amal.addMark("Science", 55);
        amal.addMark("ICT", 60);
        amal.addMark("English", 40);
        students.add(amal);

        Student nisha = new Student("Nisha", 11, 85);
        nisha.addMark("Math", 95);
        nisha.addMark("Science", 88);
        nisha.addMark("ICT", 92);
        nisha.addMark("English", 90);
        students.add(nisha);

        // Loop through each student and show their dashboard
        for (Student s : students) {
            Dashboard.showStudentReport(s);
            System.out.println("----------------------------");
        }
    }
}