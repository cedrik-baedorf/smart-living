package smart.housing.ui;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import java.util.Arrays;

/**
 * This class is a child of {@link ComboBox<T>} which automatically loads predefined
 * style sheets at construction of an object of this class.
 */
public class StyledComboBox<T> extends ComboBox<T> implements StyledNode {

    /**
     * Instance field that contains all style sheets of this object.
     * Contains default value if assigned.
     */
    private final String[] STYLE_SHEETS = { COMBO_BOX_STYLE_SHEET, SCROLL_BAR_STYLE_SHEET };

    /**
     * Default constructor calls parent constructor <code>super()</code>
     * and initializes style sheets.
     */
    public StyledComboBox() {
        super();
        initializeStyleSheets();
    }

    /**
     * Overloaded constructor calls parent constructor <code>super(ObservableList<T> items)</code>
     * and initializes style sheets.
     * @param items parameter that is handed over to super constructor.
     */
    public StyledComboBox(ObservableList<T> items) {
        super(items);
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

}
