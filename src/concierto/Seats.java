package concierto;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

class Seats extends StackPane {
    VBox wall = new VBox();
    VBox center = new VBox();
    HBox seats = new HBox();
    HBox head = new HBox();
    GridPane left = new GridPane();
    GridPane right = new GridPane();
    GridPane mid = new GridPane();
    GridPane last = new GridPane();
    SmallHeader title;
    SmallButton back;
    SmallButton what;
    SmallHeader performers;
    ArrayList<SmallButton> seat;
    ArrayList<Ticket> taken = new ArrayList<>();
    Seats(Double L, Double H, DataFile F, Concert C) {
        // Init Panels
        wall.setAlignment(Pos.TOP_CENTER);
        wall.setPadding(new Insets(15,0,0,0));
        wall.setPrefHeight(H);
        wall.setSpacing(60);
        center.setAlignment(Pos.CENTER);
        center.setSpacing(50);
        last.setVgap(5);
        last.setHgap(5);
        last.setAlignment(Pos.CENTER);
        seats.setAlignment(Pos.CENTER);
        seats.setSpacing(40);
        head.setAlignment(Pos.CENTER);
        head.setSpacing(15);
        left.setAlignment(Pos.CENTER);
        left.setVgap(5);
        left.setHgap(5);
        right.setAlignment(Pos.CENTER);
        right.setVgap(5);
        right.setHgap(5);
        mid.setAlignment(Pos.CENTER);
        mid.setVgap(5);
        mid.setHgap(5);
        // Init Objects
        title = new SmallHeader(new Rectangle(L-H/6-60,H/12), "Seating Arrangement", "Title");
        what = new SmallButton(new Rectangle(H/12,H/12), "?", "Title");
        back = new SmallButton(new Rectangle(H/12,H/12), "<", "Title");
        performers = new SmallHeader(new Rectangle(L/3,H/12), "Performers", "Header");
        // Init Seats
        seat = new ArrayList();
        for (int i=0; i<4; i++) {
            for (int ii=0; ii<5; ii++) {
                seat.add(new SmallButton(new Rectangle(H/12,H/12), "L"+Integer.toString(i*5+ii+1), "Texter"));
                left.add(seat.get(seat.size()-1), i, ii);
                seat.add(new SmallButton(new Rectangle(H/12,H/12), "R"+Integer.toString(i*5+ii+1), "Texter"));
                right.add(seat.get(seat.size()-1), i, ii);
                seat.add(new SmallButton(new Rectangle(H/12,H/12), "C"+Integer.toString(i*5+ii+1), "Texter"));
                mid.add(seat.get(seat.size()-1), ii, i);
                seat.add(new SmallButton(new Rectangle(H/12,H/12), "B"+Integer.toString(i*5+ii+1), "Texter"));
                last.add(seat.get(seat.size()-1), (i*5+ii)%10, i/2);
            }
        }
        for (int i=0; i<80; i++) {
            if(!C.seats.get(i).equals(seat.get(i).control.getText())) {
                seat.get(i).bounds.setId("Header");
                seat.get(i).control.setDisable(true);
            }
        }
        // Compile
        head.getChildren().addAll(back,title,what);
        center.getChildren().addAll(performers,mid);
        seats.getChildren().addAll(left,center,right);
        wall.getChildren().addAll(head,seats,last);
        getChildren().addAll(wall);
    }
}