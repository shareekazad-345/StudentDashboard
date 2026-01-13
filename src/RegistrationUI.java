import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.PrintWriter;

public class RegistrationUI extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("User Registration");

        // Input fields for login credentials
        TextField nameField = new TextField();
        nameField.setPromptText("Enter username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Create password");

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("Student", "Teacher", "Admin");
        roleBox.setValue("Student"); // default role

        // Marks fields (only relevant for Students)
        TextField mathField = new TextField();
        mathField.setPromptText("Math marks");

        TextField scienceField = new TextField();
        scienceField.setPromptText("Science marks");

        TextField englishField = new TextField();
        englishField.setPromptText("English marks");

        TextField ictField = new TextField();
        ictField.setPromptText("ICT marks");

        Label messageLabel = new Label();

        // Register button
        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String password = passwordField.getText().trim();
            String role = roleBox.getValue().trim();

            if (!name.isEmpty() && !password.isEmpty()) {
                try {
                    // Save credentials into Users.txt
                    try (PrintWriter out = new PrintWriter(new FileWriter("src/Users.txt", true))) {
                        out.println(name + "," + password + "," + role);
                    }

                    // If role is Student, also save marks into marks.txt
                    if (role.equals("Student")) {
                        String math = mathField.getText().trim();
                        String science = scienceField.getText().trim();
                        String english = englishField.getText().trim();
                        String ict = ictField.getText().trim();

                        if (!math.isEmpty() && !science.isEmpty() && !english.isEmpty() && !ict.isEmpty()) {
                            try (PrintWriter out = new PrintWriter(new FileWriter("src/marks.txt", true))) {
                                out.println(name + ",Math=" + math + ",Science=" + science + ",English=" + english + ",ICT=" + ict);
                            }
                        }
                    }

                    messageLabel.setText("✅ Registered " + name + " successfully!");

                    // After registration, return to login screen
                    LoginUI loginUI = new LoginUI();
                    try {
                        loginUI.start(stage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } catch (Exception ex) {
                    messageLabel.setText("⚠️ Error saving user!");
                    ex.printStackTrace();
                }
            } else {
                messageLabel.setText("⚠️ Please enter valid details!");
            }
        });

        VBox vbox = new VBox(10,
                nameField,
                passwordField,
                roleBox,
                mathField,
                scienceField,
                englishField,
                ictField,
                registerButton,
                messageLabel
        );
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 400, 500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}