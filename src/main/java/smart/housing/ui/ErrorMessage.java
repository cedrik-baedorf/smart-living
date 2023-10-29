package smart.housing.ui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class ErrorMessage extends Label {

    private static final Color
        DEFAULT_TEXT_FILL = Color.BLACK,
        ERROR_TEXT_FILL = Color.RED;

    public ErrorMessage() {
        super();
    }

    public void clear() {
        this.setTextFill(DEFAULT_TEXT_FILL);
        this.setText("");
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
