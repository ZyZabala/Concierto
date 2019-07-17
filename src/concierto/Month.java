package concierto;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class Month extends StackPane {
    String[] name = {
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    };
    SmallHeader title;
    SmallButton back;
    SmallButton what;
    ArrayList<SmallButton> month = new ArrayList<>();
    VBox wall = new VBox();
    HBox head = new HBox();
    GridPane grid = new GridPane();
    Month(Double L, Double H, DataFile F) {
        // Init Panels
        wall.setAlignment(Pos.TOP_CENTER);
        wall.setPrefHeight(H);
        wall.setPadding(new Insets(15,0,0,0));
        wall.setSpacing(30);
        head.setAlignment(Pos.CENTER);
        head.setSpacing(15);
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(20);
        grid.setHgap(20);
        // Init Objects
        title = new SmallHeader(new Rectangle(L-H/6-60,H/12), "Select Month", "Title");
        what = new SmallButton(new Rectangle(H/12,H/12), "?", "Title");
        back = new SmallButton(new Rectangle(H/12,H/12), "<", "Title");
        // Init Months 
        for (int i=0; i<4; i++) {
            for (int ii=0; ii<3; ii++) {
                month.add(new SmallButton(new Rectangle(L/4-75,H/3-60), name[i*3+ii], "Header"));
                grid.add(month.get(i*3+ii), i, ii);
            }
        }
        // Compile
        head.getChildren().addAll(back,title,what);
        wall.getChildren().addAll(head,grid);
        getChildren().addAll(wall);
    }
}