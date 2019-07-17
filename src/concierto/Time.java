package concierto;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class Time extends StackPane {
    SmallHeader title;
    SmallButton back;
    SmallButton what;
    String[] now = {"12","00","AM"};
    String[] name = {"Hour","Minute","Time"};
    ArrayList<SmallButton> minute = new ArrayList<>();
    ArrayList<SmallButton> hour = new ArrayList<>();
    ArrayList<SmallButton> slot = new ArrayList<>();
    ArrayList<SmallHeader> type = new ArrayList<>();
    VBox wall = new VBox();
    HBox head = new HBox();
    HBox beam = new HBox();
    GridPane gridhour = new GridPane();
    GridPane gridminute = new GridPane();
    SmallButton select;
    String initial;
    Time(Double L, Double H, DataFile F) {
         // Init Panels
        wall.setPrefHeight(H);
        wall.setSpacing(30);
        wall.setPadding(new Insets(15,0,0,0));
        wall.setAlignment(Pos.TOP_CENTER);
        head.setAlignment(Pos.CENTER);
        head.setSpacing(15);
        beam.setAlignment(Pos.CENTER);
        beam.setSpacing(20);
        gridhour.setAlignment(Pos.CENTER);
        gridhour.setVgap(5);
        gridhour.setHgap(5);
        gridminute.setAlignment(Pos.CENTER);
        gridminute.setVgap(5);
        gridminute.setHgap(5);
        // Init Objects
        title = new SmallHeader(new Rectangle(L-H/6-60,H/12), "Select Call Time", "Title");
        what = new SmallButton(new Rectangle(H/12,H/12), "?", "Title");
        back = new SmallButton(new Rectangle(H/12,H/12), "<", "Title");
        select = new SmallButton(new Rectangle(L-30, H/12), "Confirm "+now[0]+":"+now[1]+" "+now[2], "Title");
        // Init Time
        for (int i=0; i<12; i++) {
            // Hour
            hour.add(new SmallButton(new Rectangle(L/12-10,H/12), Integer.toString(i+1), "Texter"));
            SmallButton h = hour.get(i);
            h.control.setOnAction((ActionEvent ae_hour) -> {schedule(0,h);});
            gridhour.add(hour.get(i), i, 0);
            // Minute
            if (i<2) initial = "0"+Integer.toString(i*5);
            else initial = ""+Integer.toString(i*5);
            minute.add(new SmallButton(new Rectangle(L/12-10,H/12), initial, "Texter"));
            SmallButton m = minute.get(i);
            m.control.setOnAction((ActionEvent ae_minute) -> {schedule(1,m);});
            gridminute.add(minute.get(i), i, 0);
        }
        for (int i=0; i<3; i++) {type.add(new SmallHeader(new Rectangle(L-30,H/12), name[i], "Header"));}
        slot.add(new SmallButton(new Rectangle(L/2-30, H/12), "AM", "Texter"));
        slot.add(new SmallButton(new Rectangle(L/2-30, H/12), "PM", "Texter"));
        for (int i=0; i<2; i++) {
            SmallButton s = slot.get(i);
            s.control.setOnAction((ActionEvent ae_slot) -> {schedule(2,s);});
        }
        // Compile
        beam.getChildren().addAll(slot.get(0),slot.get(1));
        head.getChildren().addAll(back,title,what);
        wall.getChildren().addAll(head,type.get(0),gridhour,type.get(1),gridminute,type.get(2),beam,select);
        getChildren().addAll(wall);
    }
    private void schedule(int i, SmallButton b) {
        now[i] = b.control.getText();
        select.control.setText("Confirm "+now[0]+":"+now[1]+" "+now[2]);
    }
}