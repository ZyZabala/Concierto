package concierto;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

class SmallDialog extends StackPane {
    SmallButton yeah;
    SmallButton nope;
    SmallButton what;
    SmallHeader title;
    SmallTexter text;
    Label message;
    Rectangle box;
    VBox wall = new VBox();
    VBox call = new VBox();
    SmallDialog(StackPane P, Double L, Double H, DataFile F, String msg, SmallButton Y, SmallButton N, SmallButton W, Boolean with) {
        // Init Panels
        wall.setAlignment(Pos.TOP_CENTER);
        wall.setPrefHeight(H);
        wall.setPadding(new Insets(15,0,0,0));
        wall.setSpacing(60);
        call.setAlignment(Pos.CENTER);
        call.setSpacing(15);
        // Init Box
        box = new Rectangle(L,H);
        box.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("Filter.png"))));
        box.setOpacity(0.9);
        // Init Objects
        title = new SmallHeader(new Rectangle(L-30,H/12), "Confirmation Operator", "Header");
        message = new Label(msg);
        message.setAlignment(Pos.CENTER);
        yeah = Y;
        what = W;
        nope = N;
        // Compile
        wall.getChildren().addAll(title,message);
        if (with) {
            text = new SmallTexter(new Rectangle(L-30,H/12), "Type here");
            wall.getChildren().addAll(text);
        }
        call.getChildren().add(yeah);
        if (what!=null) call.getChildren().add(what);
        if (nope!=null) call.getChildren().add(nope);
        
        wall.getChildren().addAll(call);
        getChildren().addAll(box,wall);
        P.getChildren().add(this);
    }
}