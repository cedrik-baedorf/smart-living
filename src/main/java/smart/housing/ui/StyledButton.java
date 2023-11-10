package smart.housing.ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import java.util.Arrays;

/**
 * This class is a child of {@link Button} which automatically loads predefined
 * style sheets at construction of an object of this class.
 */
public class StyledButton extends Button implements StyledNode {

    /**
     * This enum is used to apply predefined styles to an object of this class.
     * The selected style determines the style sheets that are applied to an object.
     */
    public enum ButtonStyle {
        NONE("none", ""),
        DEFAULT("default", UI_ELEMENT_STYLE_SHEET),
        CONFIRM("confirm", UI_ELEMENT_STYLE_SHEET, CONFIRM_BUTTON_STYLE_SHEET);

        /**
         * Name of this style as a {@link String}.
         */
        private final String STYLE;

        /**
         * Array of all style sheets of this style.
         */
        private final String[] STYLE_SHEETS;

        /**
         * Constructor assigns instance variables of this enum.
         * @param style value of instance variable <code>STYLE</code>.
         * @param styleSheets value of instance variable <code>STYLE_SHEETS</code>.
         */
        ButtonStyle(String style, String... styleSheets) {
            STYLE = style;
            STYLE_SHEETS = styleSheets;
        }

        @Override
        public String toString() {
            return STYLE;
        }
    }

    /**
     * Instance variable that contains the style of this button.
     * Contains default value if assigned.
     */
    private ButtonStyle buttonStyle = ButtonStyle.DEFAULT;

    /**
     * Default constructor calls parent constructor <code>super()</code>
     * and initializes style sheets.
     */
    public StyledButton() {
        super();
        initializeStyleSheets();
    }

    /**
     * Overloaded constructor calls parent constructor <code>super(String text)</code>
     * and initializes style sheets.
     * @param text parameter that is handed over to super constructor.
     */
    public StyledButton(String text) {
        super(text);
        initializeStyleSheets();
    }

    /**
     * Overloaded constructor calls parent constructor <code>super(String text, Node graphic)</code>
     * and initializes style sheets.
     * @param text parameter that is handed over to super constructor.
     * @param graphic parameter that is handed over to super constructor.
     */
    public StyledButton(String text, Node graphic) {
        super(text, graphic);
        initializeStyleSheets();
    }

    @Override
    public void initializeStyleSheets() {
        this.getStylesheets().clear();
        this.getStylesheets().addAll(Arrays.stream(buttonStyle.STYLE_SHEETS)
            .map(StyledNode::convertStylePath)
            .toList()
        );
    }

    public void setButtonStyle(ButtonStyle buttonStyle) {
        this.buttonStyle = buttonStyle;
        initializeStyleSheets();
    }

    public ButtonStyle getButtonStyle() {
        return buttonStyle;
    }

}
