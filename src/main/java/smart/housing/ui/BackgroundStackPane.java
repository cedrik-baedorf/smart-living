package smart.housing.ui;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.awt.*;

public class BackgroundStackPane extends StackPane {

    private final double
        DEFAULT_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
        DEFAULT_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;
    private static final Color
        DEFAULT_BACKGROUND = Color.WHITE;

    public BackgroundStackPane() {
        this(null);
    }

    public BackgroundStackPane(String imageURL) {
        super();
        this.setPrefSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.setBackgroundImage(imageURL);
    }

    public void setBackgroundImage(String imageURL) {
        Background background;
        try {
            background = new Background(new BackgroundImage(
                    new Image(imageURL),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, true, true)
            ));
        } catch (RuntimeException exception) {
            background = new Background(
                    new BackgroundFill(DEFAULT_BACKGROUND, null, null)
            );
        }
        this.setBackground(background);
    }

}
