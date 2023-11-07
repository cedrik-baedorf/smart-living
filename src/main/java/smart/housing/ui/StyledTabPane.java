package smart.housing.ui;

import javafx.scene.control.TabPane;

import java.util.Arrays;

public class StyledTabPane extends TabPane implements StyledNode {

    public StyledTabPane() {
        this(TAB_PANE_STYLE_SHEET);
    }

    public StyledTabPane(String... styleSheets) {
        if(styleSheets != null && styleSheets.length > 0)
            Arrays.stream(styleSheets).forEach(e -> this.getStylesheets().add(
                    StyledNode.convertStylePath(e)
            ));
    }

}
