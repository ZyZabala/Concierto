package concierto;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class SmallButton extends StackPane {
    Button control;
    Rectangle bounds;
    SmallButton(Rectangle box, String title, String type) {
        // Button design setup
        control = new Button(title);
        control.setAlignment(Pos.CENTER);
        control.setPrefSize(box.getWidth(), box.getHeight());
        control.setId("SmallButton");
        // Box setup
        box.setId(type);
        if("Header".equals(type)) control.setStyle("-fx-text-fill: rgba(225,225,225,0.8);");
        bounds = box;
        bounds.setArcHeight(40);
        bounds.setArcWidth(40);
        // Stack setup
        setPrefSize(bounds.getWidth(), bounds.getHeight());
        setMaxSize(bounds.getWidth(), bounds.getHeight());
        setAlignment(Pos.CENTER);
        // Compile
        getChildren().addAll(bounds,control);
    }
}
