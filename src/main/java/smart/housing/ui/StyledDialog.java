package smart.housing.ui;

import javafx.scene.control.Dialog;

public class StyledDialog<R> extends Dialog<R> {

    public StyledDialog() {
        this.setDialogPane(new BackgroundDialogPane("smart/housing/ui/images/database_dialog_background.jpg"));
    }

}