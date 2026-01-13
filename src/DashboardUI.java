import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardUI extends Application {

    private String loggedInUser;
    private String loggedInRole;

    // Constructor with parameters (used when redirected from LoginUI)
    public DashboardUI(String username, String role) {
        this.loggedInUser = username;
        this.loggedInRole = role;
    }

    // Default constructor (for standalone testing)
    public DashboardUI() {
        this.loggedInUser = "student";
        this.loggedInRole = "Student";
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Dashboard - " + loggedInRole);

        Label welcomeLabel = new Label("Welcome, " + loggedInUser + " (" + loggedInRole + ")");

        VBox marksBox = new VBox(5);
        VBox attendanceBox = new VBox(5);

        if (loggedInRole.equals("Student")) {
            // Student view: only their marks + attendance
            Label marksLabel = new Label("Your Marks:");
            List<String> marks = loadMarks(loggedInUser);
            if (!marks.isEmpty()) {
                for (String m : marks) {
                    marksBox.getChildren().add(new Label(m));
                }
            } else {
                marksBox.getChildren().add(new Label("No marks found."));
            }

            Label attendanceLabel = new Label("Your Attendance:");
            List<String> attendance = loadAttendance(loggedInUser);
            if (!attendance.isEmpty()) {
                for (String a : attendance) {
                    attendanceBox.getChildren().add(new Label(a));
                }
            } else {
                attendanceBox.getChildren().add(new Label("No attendance records found."));
            }

            marksBox.getChildren().add(0, marksLabel);
            attendanceBox.getChildren().add(0, attendanceLabel);

            // Add marks chart
            marksBox.getChildren().add(createMarksChart(loggedInUser));

        } else if (loggedInRole.equals("Teacher")) {
            // Teacher view: all students' marks + attendance
            Label marksLabel = new Label("All Students' Marks:");
            List<String> marks = loadAllMarks();
            if (!marks.isEmpty()) {
                for (String m : marks) {
                    marksBox.getChildren().add(new Label(m));
                }
            } else {
                marksBox.getChildren().add(new Label("No marks found."));
            }

            Label attendanceLabel = new Label("All Students' Attendance:");
            List<String> attendance = loadAllAttendance();
            if (!attendance.isEmpty()) {
                for (String a : attendance) {
                    attendanceBox.getChildren().add(new Label(a));
                }
            } else {
                attendanceBox.getChildren().add(new Label("No attendance records found."));
            }

            marksBox.getChildren().add(0, marksLabel);
            attendanceBox.getChildren().add(0, attendanceLabel);

            // Add attendance chart
            attendanceBox.getChildren().add(createAttendanceChart());
        }

        VBox vbox = new VBox(15, welcomeLabel, marksBox, attendanceBox);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 700, 500);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    // Load marks for a single student
    private List<String> loadMarks(String studentName) {
        List<String> marksList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/marks.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(studentName + ",")) {
                    String[] parts = line.split(",");
                    for (int i = 1; i < parts.length; i++) {
                        marksList.add(parts[i]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return marksList;
    }

    // Load attendance for a single student
    private List<String> loadAttendance(String studentName) {
        List<String> attendanceList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/attendance.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(studentName + ",")) {
                    attendanceList.add(line.replace(studentName + ",", ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attendanceList;
    }

    // Load all marks (for teachers)
    private List<String> loadAllMarks() {
        List<String> marksList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/marks.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                marksList.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return marksList;
    }

    // Load all attendance (for teachers)
    private List<String> loadAllAttendance() {
        List<String> attendanceList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/attendance.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                attendanceList.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attendanceList;
    }

    // Create bar chart for student marks
    private BarChart<String, Number> createMarksChart(String studentName) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Marks Overview");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(studentName);

        List<String> marks = loadMarks(studentName);
        for (String m : marks) {
            String[] parts = m.split("=");
            if (parts.length == 2) {
                try {
                    series.getData().add(new XYChart.Data<>(parts[0], Integer.parseInt(parts[1])));
                } catch (NumberFormatException ignored) {}
            }
        }

        barChart.getData().add(series);
        return barChart;
    }

    // Create pie chart for attendance overview (teachers)
    private PieChart createAttendanceChart() {
        int presentCount = 0;
        int absentCount = 0;

        List<String> attendance = loadAllAttendance();
        for (String a : attendance) {
            if (a.contains("Status=Present")) presentCount++;
            else if (a.contains("Status=Absent")) absentCount++;
        }

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Attendance Overview");
        pieChart.getData().add(new PieChart.Data("Present", presentCount));
        pieChart.getData().add(new PieChart.Data("Absent", absentCount));

        return pieChart;
    }

    public static void main(String[] args) {
        launch(args);
    }
}