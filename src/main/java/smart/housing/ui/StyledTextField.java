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

    private Map<KeyCode, String> keyMapping = new HashMap<>();

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
    public void initializeKeyMappings() {
        Map<KeyCode, Node> keyToNodeMap = new HashMap<>();
        for(KeyCode code : keyMapping.keySet())
            keyToNodeMap.put(code, this.getScene().lookup(keyMapping.get(code)));
        setOnKeyPressed(StyledInputNode.createSwitchFocusEventHandler(keyToNodeMap));
    }

    @Override
    public void switchFocusOnKeyPressed(KeyCode key, Node node) {

    }

    public void setDownKeyFocus(String node) {
        keyMapping.put(KeyCode.DOWN, node);
    }

    public String getDownKeyFocus() {
        return keyMapping.get(KeyCode.DOWN);
    }

    public void setUpKeyFocus(String node) {
        keyMapping.put(KeyCode.DOWN, node);
    }

    public String getUpKeyFocus() {
        return keyMapping.get(KeyCode.UP);
    }

}
