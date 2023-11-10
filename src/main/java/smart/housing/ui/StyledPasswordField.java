package smart.housing.ui;

import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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
    private String[] styleSheets = { UI_ELEMENT_STYLE_SHEET };

    private Map<KeyCode, String> keyMapping = new HashMap<>();

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
    public void switchFocusOnKeyPressed(KeyCode key, Node nextNode) {

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
