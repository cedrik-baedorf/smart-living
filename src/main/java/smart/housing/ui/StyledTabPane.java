package smart.housing.ui;

import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;

import java.util.Arrays;

public class StyledTabPane extends TabPane implements StyledNode {

    public StyledTabPane() {
        this(TAB_PANE_STYLE_SHEET);
    }

    public StyledTabPane(String... styleSheets) {
        Arrays.stream(styleSheets).forEach(e -> this.getStylesheets().add(
            StyledNode.convertStylePath(e)
        ));
    }

}
