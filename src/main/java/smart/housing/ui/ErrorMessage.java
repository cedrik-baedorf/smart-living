package smart.housing.ui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.Arrays;

/**
 * This class is a child of {@link Label <T>} which automatically loads predefined
 * style sheets at construction of an object of this class.
 * In addition, it provides methods to temporarily display messages
 */
public class ErrorMessage extends Label implements StyledNode {

    /**
     * Instance field that contains all style sheets of this object.
     * Contains default value if assigned.
     */
    private final String[] STYLE_SHEETS = { UI_ELEMENT_STYLE_SHEET };

    private static final Color
        DEFAULT_TEXT_FILL = Color.BLACK,
        ERROR_TEXT_FILL = Color.RED;

    /**
     * Default constructor calls parent constructor <code>super()</code>
     * and initializes style sheets and object properties.
     */
    public ErrorMessage() {
        super();
        this.initializeStyleSheets();
        this.setWrapText(true);
        this.setPrefWidth(250);
        this.setAlignment(Pos.CENTER);
        this.setTextAlignment(TextAlignment.CENTER);
        this.clear();
    }

    @Override
    public void initializeStyleSheets() {
        this.getStylesheets().clear();
        this.getStylesheets().addAll(Arrays.stream(STYLE_SHEETS)
                .map(StyledNode::convertStylePath)
                .toList()
        );
    }

    public void clear() {
        this.setText("");
        this.setTextFill(DEFAULT_TEXT_FILL);
        this.setVisible(false);
    }

    public void displayError(String errorMessage) {
        this.setTextFill(ERROR_TEXT_FILL);
        this.setText(errorMessage);
        this.setVisible(true);
    }

    public void displayError(String errorMessage, double seconds) {
        this.displayTemporarily(() -> displayError(errorMessage), seconds);
    }

    public void displayInfo(String infoMessage) {
        this.setTextFill(DEFAULT_TEXT_FILL);
        this.setText(infoMessage);
        this.setVisible(true);
    }

    public void displayInfo(String infoMessage, double seconds) {
        this.displayTemporarily(() -> displayInfo(infoMessage), seconds);
    }

    private void displayTemporarily(Runnable runnable, double seconds) {
        new Thread(() -> {
            Platform.runLater(runnable);
            try {
                Thread.sleep((long) (seconds * 1000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                Platform.runLater(this::clear);
            }
        }).start();
    }

}
