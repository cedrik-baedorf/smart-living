package smart.housing;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import smart.housing.controllers.LoginPageController;
import smart.housing.controllers.SmartHousingController;
import smart.housing.database.DatabaseConnector;
import smart.housing.entities.User;
import java.io.IOException;

public class SmartLivingApplication extends Application {

    private final SmartHousingController START_CONTROLLER = new LoginPageController(this);

    private DatabaseConnector connector;

    private User user;

    private final String
        START_VIEW = START_CONTROLLER.getViewName(),
        VIEW_DIR = "views/";

    private Stage stage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        addStageIcon("smart/housing/views/images/icon_plain.png");
        stage.setMaximized(true);
        stage.setTitle("Smart Living Application");
        setRoot(START_VIEW, START_CONTROLLER);
        stage.setOnCloseRequest(event -> Platform.exit());
    }

    private void addStageIcon(String path) {
        try {
            stage.getIcons().add(new Image(path));
        } catch (RuntimeException exception) {
            exception.printStackTrace();
        }
    }

    public void setRoot(String rootView, SmartHousingController controller) {
        boolean maximized = stage.isMaximized();
        double width = stage.getWidth();
        double height = stage.getHeight();
        FXMLLoader loader = createFXMLLoader(rootView);
        loader.setControllerFactory(c -> controller);
        Scene scene = createScene(loader);
        stage.setScene(scene);
        stage.show();
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setMaximized(maximized);
    }

    private Scene createScene(FXMLLoader loader) {
        try {
            return new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException("Unable to load fxml file at location " + loader.getLocation());
        }
    }

    private FXMLLoader createFXMLLoader(String fxmlFile) {
        return new FXMLLoader(SmartLivingApplication.class.getResource(VIEW_DIR + fxmlFile));
    }

    public <T> T loadFXML(String fxmlFile, SmartHousingController controller) {
        FXMLLoader loader = createFXMLLoader(fxmlFile);
        try {
            loader.setControllerFactory(c -> controller);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Unable to load fxml file at location " + loader.getLocation());
        }
    }

    public void setDatabaseConnector(DatabaseConnector connector) {
        this.connector = connector;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DatabaseConnector getDatabaseConnector() {
        return connector;
    }

    public User getUser() { return user; }

}
