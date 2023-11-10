package smart.housing.ui;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is a child of {@link TextField} which automatically loads predefined
 * style sheets at construction of an object of this class.
 */
public class StyledTextField extends TextField implements StyledInputNode {

    /**
     * Instance field that contains all style sheets of this object.
     * Contains default value if assigned.
     */
    private String[] styleSheets = { UI_ELEMENT_STYLE_SHEET };

    private Map<KeyCode, Node> KEY_MAPPING = new HashMap<>();

    /**
     * Default constructor calls parent constructor <code>super()</code>
     * and initializes style sheets.
     */
    public StyledTextField() {
        super();
        initializeStyleSheets();
    }

    /**
     * Overloaded constructor calls parent constructor <code>super(String text)</code>
     * and initializes style sheets.
     * @param text parameter that is handed over to super constructor.
     */
    public StyledTextField(String text) {
        super(text);
        initializeStyleSheets();
    }

    @Override
    public void initializeStyleSheets() {
        this.getStylesheets().clear();
        this.getStylesheets().addAll(Arrays.stream(styleSheets)
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
