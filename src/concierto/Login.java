package concierto;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

class Login extends StackPane{
    User U;
    SmallTexter usertext;
    SmallCrypto passtext;
    SmallHeader userlabel;
    SmallHeader passlabel;
    Label title;
    SmallButton signin;
    SmallButton signup;
    SmallButton apply;
    SmallButton exit;
    VBox wall = new VBox();
    HBox beam = new HBox();
    GridPane grid = new GridPane();
    DataFile F;
    
    Login(Double L, Double H, DataFile File) {
        // Init Panels
        wall.setAlignment(Pos.CENTER);
        wall.setSpacing(70);
        beam.setAlignment(Pos.CENTER);
        beam.setSpacing(30);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        // Init Objects
        userlabel = new SmallHeader(new Rectangle(L/4,H/10), "Username: ", "Header");
        passlabel = new SmallHeader(new Rectangle(L/4,H/10), "Password: ", "Header");
        usertext = new SmallTexter(new Rectangle(L/2,H/10), "Username");
        passtext = new SmallCrypto(new Rectangle(L/2,H/10), "Password");
        title = new Label("C O N C I E R T O");
        signin = new SmallButton(new Rectangle(L/5,H/12), "Login", "Title");
        signup = new SmallButton(new Rectangle(L/5,H/12), "Register", "Title");
        apply = new SmallButton(new Rectangle(L/5,H/12), "Apply", "Title");
        exit = new SmallButton(new Rectangle(L/7,H/14), "Exit", "Header");
        F = File;
        // Compile
        grid.add(userlabel, 0, 0);
        grid.add(usertext, 1, 0);
        grid.add(passlabel, 0, 1);
        grid.add(passtext, 1, 1);
        beam.getChildren().addAll(signin,signup,apply);
        wall.getChildren().addAll(title,grid,beam,exit);
        getChildren().add(wall);
    }
    
    String Verify() {
        for (User u : F.Users) {
            if (usertext.input.getText().equals(u.name)) {
                if (passtext.input.getText().equals(u.pass) || u.type.equals("Forgot Applicant") || u.type.equals("Forgot Administrator") || u.type.equals("Forgot Customer") || u.type.equals("Reset Applicant") || u.type.equals("Reset Administrator") || u.type.equals("Reset Customer")) return u.type;
                else return "Incorrect";
            }
        }
        return "Nothing";
    }
}