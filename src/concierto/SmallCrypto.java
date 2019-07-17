package concierto;

import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class SmallCrypto extends StackPane {
    PasswordField input;
    Rectangle bounds;
    SmallCrypto(Rectangle box, String title) {
        // Stack setup
        setMinSize(box.getWidth(), box.getHeight());
        setPrefSize(box.getWidth(), box.getHeight());
        setMaxSize(box.getWidth(), box.getHeight());
        setAlignment(Pos.CENTER);
        // Box Setup
        bounds = box;
        bounds.setId("Texter");
        bounds.setArcHeight(40);
        bounds.setArcWidth(40);
        // Input design setup
        input = new PasswordField();
        input.setAlignment(Pos.CENTER);
        input.setPrefSize(bounds.getWidth(), bounds.getHeight());
        input.setPromptText(title);
        input.setId("SmallTexter");
        // Compile
        getChildren().addAll(bounds,input);
    }
}