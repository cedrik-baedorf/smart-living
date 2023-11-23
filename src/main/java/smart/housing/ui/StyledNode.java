package smart.housing.ui;
import java.net.URL;

public interface StyledNode {

    String STYLES_DIRECTORY = "style_sheets/";
    String UI_ELEMENT_STYLE_SHEET =     STYLES_DIRECTORY + "ui-element.css";
    String CONFIRM_BUTTON_STYLE_SHEET =   STYLES_DIRECTORY + "confirm-button.css";
    String COMBO_BOX_STYLE_SHEET =      STYLES_DIRECTORY + "combo-box.css";
    String TAB_PANE_STYLE_SHEET =       STYLES_DIRECTORY + "tab-pane.css";
    String TABLE_VIEW_STYLE_SHEET =     STYLES_DIRECTORY + "table-view.css";
    String SCROLL_BAR_STYLE_SHEET =     STYLES_DIRECTORY + "scroll-bar.css";
    String CHECK_BOX_STYLE_SHEET =      STYLES_DIRECTORY + "check-combo-box.css";
    String DATE_PICKER_STYLE_SHEET =    STYLES_DIRECTORY + "date-picker.css";

    static String convertStylePath(String path) {
        URL url = StyledNode.class.getResource(path);
        return url != null ? url.toExternalForm() : null;
    }

    /**
     * This method loads the style sheets of a {@link StyledNode} implementation.
     */
    void initializeStyleSheets();

}
