package smart.housing.ui;

import javafx.scene.control.ComboBox;
import java.util.Arrays;
import static smart.housing.ui.StyleSheets.*;

public class StyledComboBox<T> extends ComboBox<T> {

    public StyledComboBox() {
        this(COMBO_BOX_STYLE_SHEET, SCROLL_BAR_STYLE_SHEET);
    }

    public StyledComboBox(String... styleSheets) {
        Arrays.stream(styleSheets).forEach(e -> this.getStylesheets().add(e));
    }

}
