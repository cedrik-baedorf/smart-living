package smart.housing.ui;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;

import java.util.Arrays;

/**
 * This class is a child of {@link TableView<S>} which automatically loads predefined
 * style sheets at construction of an object of this class.
 */
public class StyledTableView<S> extends TableView<S> implements StyledNode {

    /**
     * Instance field that contains all style sheets of this object.
     * Contains default value if assigned.
     */
    private final String[] STYLE_SHEETS = { TABLE_VIEW_STYLE_SHEET, SCROLL_BAR_STYLE_SHEET };

    /**
     * Default constructor calls parent constructor <code>super()</code>
     * and initializes style sheets.
     */
    public StyledTableView() {
        super();
        initializeStyleSheets();
    }

    /**
     * Overloaded constructor calls parent constructor <code>super(ObservableList<S> items)</code>
     * and initializes style sheets.
     * @param items parameter that is handed over to super constructor.
     */
    public StyledTableView(ObservableList<S> items) {
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

    public void bindColumnWidth() {
        double totalWidth = this.getColumns()
                .stream()
                .map(TableColumnBase::getPrefWidth)
                .mapToDouble(Double::doubleValue)
                .sum();

        for(var column : this.getColumns())
            column.prefWidthProperty().bind(
                Bindings.max(
                    column.getMinWidth(),
                    Bindings.min(
                        column.getMaxWidth(),
                        widthProperty().multiply(column.getPrefWidth() / totalWidth)
                    )
                )
            );
    }

}
