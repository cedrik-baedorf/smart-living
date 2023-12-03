package smart.housing.ui;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Arrays;

public class ErrorMessage extends Text {

    private static final Color
        DEFAULT_TEXT_FILL = Color.BLACK,
        ERROR_TEXT_FILL = Color.RED;

    public ErrorMessage() {
        super();
        this.setWrappingWidth(200);
        this.setTextAlignment(TextAlignment.CENTER);
    }

    public void clear() {
        this.setText("");
        this.setFill(DEFAULT_TEXT_FILL);
        this.setVisible(false);
    }

    public void displayError(String errorMessage) {
        this.setFill(ERROR_TEXT_FILL);
        this.setText(errorMessage);
        this.setVisible(true);
    }

    public void displayError(String errorMessage, double seconds) {
        this.displayTemporarily(() -> displayError(errorMessage), seconds);
    }

    public void displayInfo(String infoMessage) {
        this.setFill(DEFAULT_TEXT_FILL);
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
