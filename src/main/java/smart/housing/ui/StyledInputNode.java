package smart.housing.ui;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Map;

public interface StyledInputNode extends StyledNode {

    static EventHandler<KeyEvent> createSwitchFocusEventHandler(Map<KeyCode, Node> keyMapping) {
        return event -> {
            for(KeyCode code : keyMapping.keySet()) {
                if(event.getCode().equals(code))
                    keyMapping.get(code).requestFocus();
            }
        };
    }

    void initializeKeyMappings();

    /**
     * This method must be implemented to set the {@link Node} focused when the according key is pressed.
     */
    void switchFocusOnKeyPressed(KeyCode key, Node nextNode);

}
