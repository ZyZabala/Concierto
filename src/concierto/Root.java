package concierto;

import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

class Root extends StackPane {
    Rectangle People = new Rectangle();
    Rectangle Filter = new Rectangle();
    Rectangle Aesthetic = new Rectangle();
    StackPane Others = new StackPane();
    Root(double L, double H) {
        // Background Setup
        People.setWidth(L);
        People.setHeight(H);
        People.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("People.jpeg"))));
        // Filter setup
        Filter.setWidth(L);
        Filter.setHeight(H);
        Filter.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("Filter.png"))));
        Filter.setBlendMode(BlendMode.HARD_LIGHT);
        // Aesthetic setup
        Aesthetic.setWidth(L);
        Aesthetic.setHeight(H);
        Aesthetic.setFill(Color.MEDIUMVIOLETRED);
        Aesthetic.setBlendMode(BlendMode.COLOR_BURN);
        Aesthetic.setOpacity(0.3);
        // Compile
        getChildren().addAll(People,Filter,Aesthetic,Others);
    }
    StackPane swap(StackPane pane) {
        Others.getChildren().clear();
        Others.getChildren().add(pane);
        return this;
    }
}
