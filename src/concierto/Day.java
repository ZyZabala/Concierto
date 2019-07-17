package concierto;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class Day extends StackPane {
    VBox wall = new VBox();
    GridPane grid = new GridPane();
    HBox head = new HBox();
    SmallButton back;
    SmallButton what;
    SmallHeader title;
    ArrayList<SmallButton> days = new ArrayList<>();
    Day(Double L, Double H, DataFile F, String month, int year) {
        // Init Panels
        wall.setPrefHeight(H);
        wall.setSpacing(100);
        wall.setPadding(new Insets(15,0,0,0));
        wall.setAlignment(Pos.TOP_CENTER);
        head.setAlignment(Pos.CENTER);
        head.setSpacing(15);
        grid.setAlignment(Pos.CENTER);
        // Init Objects
        title = new SmallHeader(new Rectangle(L-H/6-60,H/12), "Select Day of "+month, "Title");
        what = new SmallButton(new Rectangle(H/12,H/12), "?", "Title");
        back = new SmallButton(new Rectangle(H/12,H/12), "<", "Title");
        // Init Days
        switch (month) {
            case "February":
                if (year%4==0) {addDay(L,29);}
                else {addDay(L,28);}
                break;
            case "January":
            case "March":
            case "May":
            case "July":
            case "August":
            case "October":
            case "December":
                addDay(L,31);
                break;
            default:
                addDay(L,30);
                break;
        }
        // Compile
        head.getChildren().addAll(back,title,what);
        wall.getChildren().addAll(head,grid);
        getChildren().addAll(wall);
    }
    private void addDay(Double L, int limit) {
        for (int i=0; i<limit; i++) {
            days.add(new SmallButton(new Rectangle(L/(limit+1),L/(limit+1)), Integer.toString(i+1), "Texter"));
            grid.add(days.get(i), i, 0);
        }
    }
}
