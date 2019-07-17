package concierto;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

class Menu extends StackPane {
    User usernow;
    VBox wall = new VBox();
    VBox left = new VBox();
    HBox head = new HBox();
    HBox beam = new HBox();
    SmallHeader title;
    SmallButton back;
    SmallButton what;
    SmallButton make;
    SmallButton view;
    SmallButton mine;
    SmallButton mode;
    String maketext;
    String viewtext;
    String modetext;
    Menu(Double L, Double H, DataFile F, User U) {
        // Init Panels
        wall.setAlignment(Pos.TOP_CENTER);
        wall.setPadding(new Insets(15,0,0,0));
        wall.setPrefHeight(H);
        wall.setSpacing(150);
        head.setAlignment(Pos.CENTER);
        head.setSpacing(15);
        left.setAlignment(Pos.CENTER);
        left.setSpacing(15);
        beam.setAlignment(Pos.CENTER);
        beam.setSpacing(15);
        // Init Objects
        usernow = U;
        title = new SmallHeader(new Rectangle(L-H/6-60,H/12), "Welcome, "+usernow.name+"!", "Title");
        what = new SmallButton(new Rectangle(H/12,H/12), "?", "Title");
        back = new SmallButton(new Rectangle(H/12,H/12), "O", "Title");
        if (usernow.type.equals("Administrator")) {
            maketext = "Upcoming Concerts";
            viewtext = "External Contacts";
            modetext = "Administrative Privileges";
        }
        else {
            maketext = "My Tickets";
            viewtext = "Upcoming Concerts";
            modetext = "Upgrade Account";
        }
        make = new SmallButton(new Rectangle(L/2-45,30+H/2), maketext, "Header");
        view = new SmallButton(new Rectangle(L/2,H/6), viewtext, "Header");
        mine = new SmallButton(new Rectangle(L/2,H/6), "Account Details", "Header");
        mode = new SmallButton(new Rectangle(L/2,H/6), modetext, "Header");
        // Compile
        left.getChildren().addAll(view,mine,mode);
        beam.getChildren().addAll(left,make);
        head.getChildren().addAll(back,title,what);
        wall.getChildren().addAll(head,beam);
        getChildren().addAll(wall);
    }
}