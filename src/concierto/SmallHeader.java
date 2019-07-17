package concierto;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class SmallHeader extends StackPane {
    Label heading;
    Rectangle bounds;
    SmallHeader(Rectangle box, String title, String type) {
        // Label setup
        heading = new Label(title);
        heading.setAlignment(Pos.CENTER);
        heading.setPrefSize(box.getWidth(), box.getHeight());
        heading.setId("SmallHeader");
        // box setup
        box.setId(type);
        bounds = box;
        bounds.setArcHeight(40);
        bounds.setArcWidth(40);
        // Stack setup
        setPrefSize(box.getWidth(), box.getHeight());
        setMaxSize(box.getWidth(), box.getHeight());
        setAlignment(Pos.CENTER);
        // Compile
        getChildren().addAll(bounds,heading);
    }
}