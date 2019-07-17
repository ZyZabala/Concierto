package concierto;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

class Register extends StackPane {
    SmallHeader numtext;
    SmallTexter usertext;
    SmallCrypto passtext;
    SmallHeader numlabel;
    SmallHeader userlabel;
    SmallHeader passlabel;
    SmallButton action;
    SmallButton what;
    SmallButton back;
    SmallHeader title;
    private static final VBox wall = new VBox();
    private static final HBox head = new HBox();
    private static final GridPane grid = new GridPane();
    DataFile F;
    
    Register(Double L, Double H, DataFile File, String I) {
        // Init Panels
        wall.setAlignment(Pos.TOP_CENTER);
        wall.setPadding(new Insets(15,0,0,0));
        wall.setPrefHeight(H);
        wall.setSpacing(100);
        head.setAlignment(Pos.CENTER);
        head.setSpacing(15);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(15);
        // Init Objects
        F = File;
        title = new SmallHeader(new Rectangle(L-H/6-60,H/12), "Account Details", "Title");
        what = new SmallButton(new Rectangle(H/12,H/12), "?", "Title");
        back = new SmallButton(new Rectangle(H/12,H/12), "<", "Title");
        action = new SmallButton(new Rectangle(L-30,H/12), "Submit", "Header");
        numlabel = new SmallHeader(new Rectangle(L/3, H/6), "Card Number", "Title");
        userlabel = new SmallHeader(new Rectangle(L/3, H/6), "Username", "Title");
        passlabel = new SmallHeader(new Rectangle(L/3, H/6), "Password", "Title");
        numtext = new SmallHeader(new Rectangle(2*L/3-45, H/6), I, "Header");
        usertext = new SmallTexter(new Rectangle(2*L/3-45, H/6), "Username");
        passtext = new SmallCrypto(new Rectangle(2*L/3-45, H/6), "Password");
        // Compile
        grid.add(numlabel, 0, 0);
        grid.add(numtext, 1, 0);
        grid.add(userlabel, 0, 1);
        grid.add(usertext, 1, 1);
        grid.add(passlabel, 0, 2);
        grid.add(passtext, 1, 2);
        head.getChildren().addAll(back,title,what);
        wall.getChildren().addAll(head,grid,action);
        getChildren().addAll(wall);
    }
    
    String Submit(String U, String P) {
        for (User u : F.Users) {
            if (u.name.equals(U)) return "Taken";
            else {
                if (P.isEmpty()) return "Empty";
                else {
                    if (title.heading.getText().equals("Customer Registration")) return "Customer";
                    else if (title.heading.getText().equals("Administrator Application")) return "Applicant";
                }
            }
        }
        return null;
    }
    
    User Change(User u, String n, String p) {
        u.name = n;
        u.pass = p;
        return u;
    }
}