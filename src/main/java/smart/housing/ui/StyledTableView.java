package smart.housing.ui;

import javafx.scene.control.TableView;
import java.util.Arrays;

public class StyledTableView<S> extends TableView<S> implements StyledNode {

    public StyledTableView() {
        this(TABLE_VIEW_STYLE_SHEET, SCROLL_BAR_STYLE_SHEET);
    }

    public StyledTableView(String... styleSheets) {
        if(styleSheets != null && styleSheets.length > 0)
            Arrays.stream(styleSheets).forEach(e -> this.getStylesheets().add(
                    StyledNode.convertStylePath(e)
            ));
    }

}
