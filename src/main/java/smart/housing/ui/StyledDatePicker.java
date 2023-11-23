package smart.housing.ui;

import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * This class is a child of {@link DatePicker} which automatically loads predefined
 * style sheets at construction of an object of this class.
 */
public class StyledDatePicker extends DatePicker implements StyledNode {

    /**
     * Instance field that contains all style sheets of this object.
     * Contains default value if assigned.
     */
    private final String[] STYLE_SHEETS = { DATE_PICKER_STYLE_SHEET };

    /**
     * Default constructor calls parent constructor <code>super()</code>
     * and initializes style sheets.
     */
    public StyledDatePicker() {
        super();
        initializeStyleSheets();
    }

    /**
     * Overloaded constructor calls parent constructor <code>super(LocalDate localDate)</code>
     * and initializes style sheets.
     * @param localDate parameter that is handed over to super constructor.
     */
    public StyledDatePicker(LocalDate localDate) {
        super(localDate);
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
