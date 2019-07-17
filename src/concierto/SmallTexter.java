package concierto;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class SmallTexter extends StackPane {
    TextField input;
    Rectangle bounds;
    SmallTexter(Rectangle box, String title) {
        // Text design setup
        input = new TextField();
        input.setAlignment(Pos.CENTER);
        input.setPrefSize(box.getWidth(), box.getHeight());
        input.setId("SmallTexter");
        // Box setup
        box.setId("Texter");
        bounds = box;
        bounds.setArcHeight(40);
        bounds.setArcWidth(40);
        // Stack setup
        setPrefSize(box.getWidth(), box.getHeight());
        setMaxSize(box.getWidth(), box.getHeight());
        setAlignment(Pos.CENTER);
        // Compile
        getChildren().addAll(bounds,input);
    }
}