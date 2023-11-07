package smart.housing.ui;

import javafx.beans.DefaultProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.Arrays;

public class StyledButton extends Button implements StyledNode {

    /**
     * This enum can be used to define the style of the button
     */
    public enum ButtonStyle {
        DEFAULT("default", UI_ELEMENT_STYLE_SHEET),
        CONFIRM("confirm", UI_ELEMENT_STYLE_SHEET, CONFIRM_BUTTON_STYLE_SHEET);

        private final String STYLE;
        private final String[] STYLE_SHEETS;

        private ButtonStyle(String style, String... styleSheets) {
            STYLE = style;
            STYLE_SHEETS = styleSheets;
        }

        @Override
        public String toString() {
            return STYLE;
        }
    }

    private ButtonStyle buttonStyle = ButtonStyle.DEFAULT;

    public StyledButton(String text) {
        super(text);
        initialize();
    }

    public StyledButton(String text, Node graphic) {
        super(text, graphic);
        initialize();
    }
    public StyledButton() {
        super();
        initialize();
    }

    private void initialize() {
        this.getStylesheets().clear();
        this.getStylesheets().addAll(Arrays.stream(buttonStyle.STYLE_SHEETS)
            .map(StyledNode::convertStylePath)
            .toList()
        );
    }

    public void setButtonStyle(ButtonStyle buttonStyle) {
        this.buttonStyle = buttonStyle;
        initialize();
    }

    public ButtonStyle getButtonStyle() {
        return buttonStyle;
    }

}
