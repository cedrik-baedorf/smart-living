package smart.housing.ui;

import javafx.collections.ObservableList;
import org.controlsfx.control.CheckComboBox;

public class StyledCheckComboBox<T> extends CheckComboBox<T> {

    public void setItems(ObservableList<T> items) {
        this.getItems().setAll(items);
    }

    public void setPromptText(String promptText) {

    }

    public String getPromptText() {
        return null;
    }

}