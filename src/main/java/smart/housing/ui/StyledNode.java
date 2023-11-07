package smart.housing.ui;

import java.net.URL;

public interface StyledNode {

    String COMBO_BOX_STYLE_SHEET = "combo-box.css";
    String TAB_PANE_STYLE_SHEET = "tab-pane.css";
    String TABLE_VIEW_STYLE_SHEET = "table-view.css";
    String SCROLL_BAR_STYLE_SHEET = "scroll-bar.css";

    static String convertStylePath(String path) {
        URL url = StyledNode.class.getResource(path);
        return url != null ? url.toExternalForm() : null;
    }

}
