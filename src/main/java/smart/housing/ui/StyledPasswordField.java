package smart.housing.ui;

import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is a child of {@link PasswordField} which automatically loads predefined
 * style sheets at construction of an object of this class.
 */
public class StyledPasswordField extends PasswordField implements StyledInputNode {

    /**
     * Instance field that contains all style sheets of this object.
     * Contains default value if assigned.
     */
    private final String[] STYLE_SHEETS = { UI_ELEMENT_STYLE_SHEET };

    private final Map<KeyCode, Node> KEY_MAPPING = new HashMap<>();

    /**
     * Default constructor calls parent constructor <code>super()</code>
     * and initializes style sheets.
     */
    public StyledPasswordField() {
        super();
        initializeStyleSheets();
    }

    @Override
    public void initializeStyleSheets() {
        this.getStylesheets().clear();
        this.getStylesheets().addAll(Arrays.stream(STYLE_SHEETS)
                .map(StyledNode::convertStylePath)
                .toList()
        );
    }

    @Override
    public void switchFocusOnKeyPressed(KeyCode key, Node node) {
        KEY_MAPPING.put(key, node);
        initializeKeyMappings();
    }

    private void initializeKeyMappings() {
        setOnKeyPressed(StyledInputNode.createSwitchFocusEventHandler(KEY_MAPPING));
    }

}
