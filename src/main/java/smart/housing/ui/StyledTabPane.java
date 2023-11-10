package smart.housing.ui;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import java.util.Arrays;

/**
 * This class is a child of {@link TabPane} which automatically loads predefined
 * style sheets at construction of an object of this class.
 */
public class StyledTabPane extends TabPane implements StyledNode {

    /**
     * Instance field that contains all style sheets of this object.
     * Contains default value if assigned.
     */
    private final String[] STYLE_SHEETS = { TAB_PANE_STYLE_SHEET };

    /**
     * Default constructor calls parent constructor <code>super()</code>
     * and initializes style sheets.
     */
    public StyledTabPane() {
        super();
        initializeStyleSheets();
    }

    /**
     * Overloaded constructor calls parent constructor <code>super(Tab... tabs)</code>
     * and initializes style sheets.
     * @param tabs parameter that is handed over to super constructor.
     */
    public StyledTabPane(Tab... tabs) {
        super(tabs);
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
