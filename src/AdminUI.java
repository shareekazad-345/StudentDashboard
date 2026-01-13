import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.StringTokenizer;

public class AdminUI extends Application {

    private TableView<User> table;
    private ObservableList<User> users;

    private String loggedInUser;
    private String loggedInRole;

    // Constructor with parameters (used when redirected from LoginUI)
    public AdminUI(String username, String role) {
        this.loggedInUser = username;
        this.loggedInRole = role;
    }

    // Default constructor (for standalone testing)
    public AdminUI() {
        this.loggedInUser = "admin";
        this.loggedInRole = "Admin";
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Admin Panel");

        Label welcomeLabel = new Label("Welcome, " + loggedInUser + " (" + loggedInRole + ")");

        // Table setup
        table = new TableView<>();
        users = FXCollections.observableArrayList();
        loadUsers();

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(data -> data.getValue().usernameProperty());

        TableColumn<User, String> passwordCol = new TableColumn<>("Password");
        // Mask password display
        passwordCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("******"));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(data -> data.getValue().roleProperty());

        table.getColumns().addAll(usernameCol, passwordCol, roleCol);
        table.setItems(users);

        // Search bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search by username or role...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterUsers(newValue);
        });

        // Buttons
        Button addButton = new Button("Add User");
        addButton.setOnAction(e -> {
            RegistrationUI regUI = new RegistrationUI();
            try {
                regUI.start(stage); // redirect to registration
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button removeButton = new Button("Remove User");
        removeButton.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                users.remove(selected);
                saveUsers();
            }
        });

        Button editMarksButton = new Button("Edit Marks");
        editMarksButton.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected != null && selected.getRole().equals("Student")) {
                openMarksEditor(selected.getUsername());
            }
        });

        Button attendanceButton = new Button("Mark Attendance");
        attendanceButton.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected != null && selected.getRole().equals("Student")) {
                openAttendanceEditor(selected.getUsername());
            }
        });

        Button resetPasswordButton = new Button("Reset Password");
        resetPasswordButton.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                openPasswordReset(selected);
            }
        });

        VBox vbox = new VBox(10, welcomeLabel, searchField, table,
                addButton, removeButton, editMarksButton, attendanceButton, resetPasswordButton);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 700, 500);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    // Load users from Users.txt
    private void loadUsers() {
        users.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ",");
                if (st.countTokens() == 3) {
                    String username = st.nextToken().trim();
                    String password = st.nextToken().trim();
                    String role = st.nextToken().trim();
                    users.add(new User(username, password, role));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Save users back to Users.txt
    private void saveUsers() {
        try (PrintWriter out = new PrintWriter(new FileWriter("src/Users.txt"))) {
            for (User u : users) {
                out.println(u.getUsername() + "," + u.getPassword() + "," + u.getRole());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Filter users by keyword
    private void filterUsers(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            table.setItems(users); // show all
            return;
        }

        ObservableList<User> filteredList = FXCollections.observableArrayList();
        for (User u : users) {
            if (u.getUsername().toLowerCase().contains(keyword.toLowerCase()) ||
                    u.getRole().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(u);
            }
        }
        table.setItems(filteredList);
    }

    // Popup form to edit marks
    private void openMarksEditor(String studentName) {
        Stage popup = new Stage();
        popup.setTitle("Edit Marks for " + studentName);

        TextField mathField = new TextField();
        mathField.setPromptText("Math");

        TextField scienceField = new TextField();
        scienceField.setPromptText("Science");

        TextField englishField = new TextField();
        englishField.setPromptText("English");

        TextField ictField = new TextField();
        ictField.setPromptText("ICT");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            updateMarks(studentName,
                    mathField.getText().trim(),
                    scienceField.getText().trim(),
                    englishField.getText().trim(),
                    ictField.getText().trim());
            popup.close();
        });

        VBox vbox = new VBox(10, mathField, scienceField, englishField, ictField, saveButton);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 300, 250);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        popup.setScene(scene);
        popup.show();
    }

    // Update marks.txt for a student
    private void updateMarks(String studentName, String math, String science, String english, String ict) {
        try {
            File file = new File("src/marks.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;

            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(studentName + ",")) {
                    sb.append(studentName)
                            .append(",Math=").append(math)
                            .append(",Science=").append(science)
                            .append(",English=").append(english)
                            .append(",ICT=").append(ict)
                            .append("\n");
                    found = true;
                } else {
                    sb.append(line).append("\n");
                }
            }
            reader.close();

            if (!found) {
                sb.append(studentName)
                        .append(",Math=").append(math)
                        .append(",Science=").append(science)
                        .append(",English=").append(english)
                        .append(",ICT=").append(ict)
                        .append("\n");
            }

            PrintWriter out = new PrintWriter(new FileWriter(file));
            out.write(sb.toString());
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Popup form to mark attendance
    private void openAttendanceEditor(String studentName) {
        Stage popup = new Stage();
        popup.setTitle("Mark Attendance for " + studentName);

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Date");

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Present", "Absent");
        statusBox.setValue("Present");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "Unknown";
            String status = statusBox.getValue();
            saveAttendance(studentName, date, status);
            popup.close();
        });

        VBox vbox = new VBox(10, datePicker, statusBox, saveButton);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 300, 200);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        popup.setScene(scene);
        popup.show();
    }

    // Save attendance record into attendance.txt
    private void saveAttendance(String studentName, String date, String status) {
        try (PrintWriter out = new PrintWriter(new FileWriter("src/attendance.txt", true))) {
            out.println(studentName + ",Date=" + date + ",Status=" + status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Popup form to reset a user's password
    private void openPasswordReset(User user) {
        Stage popup = new Stage();
        popup.setTitle("Reset Password for " + user.getUsername());

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New Password");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            // Update the user's password in memory
            user.setPassword(newPasswordField.getText().trim());
            // Save changes back to Users.txt
            saveUsers();
            popup.close();
        });

        VBox vbox = new VBox(10, newPasswordField, saveButton);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 300, 150);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        popup.setScene(scene);
        popup.show();
    }










}
