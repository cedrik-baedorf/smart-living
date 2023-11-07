package smart.housing.ui;

import javafx.scene.control.ComboBox;
import java.util.Arrays;

public class StyledComboBox<T> extends ComboBox<T> implements StyledNode {

    public StyledComboBox() {
        this(COMBO_BOX_STYLE_SHEET, SCROLL_BAR_STYLE_SHEET);
    }

    public StyledComboBox(String... styleSheets) {
        Arrays.stream(styleSheets).forEach(e -> this.getStylesheets().add(
                StyledNode.convertStylePath(e)
        ));
    }

}
