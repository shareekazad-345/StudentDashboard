import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;

public class LoginUI extends Application {

    // Helper method to validate credentials from Users.txt
    private boolean validateCredentials(String username, String password, String role) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String fileUser = parts[0].trim();
                    String filePass = parts[1].trim();
                    String fileRole = parts[2].trim();

                    if (fileUser.equals(username) && filePass.equals(password) && fileRole.equals(role)) {
                        return true; // match found
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // no match
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Login System");

        // Username + Password fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        // Role selection
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("Student", "Teacher", "Admin");
        roleBox.setValue("Student"); // default

        // Message label
        Label messageLabel = new Label();

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String user = usernameField.getText().trim();
            String pass = passwordField.getText().trim();
            String role = roleBox.getValue().trim();

            if (validateCredentials(user, pass, role)) {
                messageLabel.setText("Login successful as " + role);

                // Redirect based on role
                if (role.equals("Student")) {
                    DashboardUI dashboard = new DashboardUI(user, role);
                    try {
                        dashboard.start(stage); // show student dashboard
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (role.equals("Teacher")) {
                    DashboardUI dashboard = new DashboardUI(user, role);
                    try {
                        dashboard.start(stage); // show teacher dashboard
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (role.equals("Admin")) {
                    AdminUI adminPanel = new AdminUI(user, role);
                    try {
                        adminPanel.start(stage); // show admin panel
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                messageLabel.setText("Invalid credentials!");
            }
        });

        // Registration button
        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            RegistrationUI regUI = new RegistrationUI();
            try {
                regUI.start(stage); // switch to registration screen
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Layout
        VBox vbox = new VBox(10, usernameField, passwordField, roleBox, loginButton, registerButton, messageLabel);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 400, 300);

        // Apply CSS styling
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}